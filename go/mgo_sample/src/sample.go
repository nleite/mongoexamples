package main

import (
    "fmt"
    "labix.org/v2/mgo"
//    "labix.org/v2/mgo/bson"
    "os"
    "io"
    "time"
    "encoding/csv"
    "strconv"
    //"flag"
)

type bnf struct {
    Name string 
    Code string
}

type Prescription struct {
    SHA string
    PCT string
    Practice string
    BNF bnf
    items int
    nic float64
    cost float64
    period int
}

func NewPrescription( record []string) Prescription{
    // TODO this needs serious refactoring but for the time being I'll just leave it so verbose
    sha := record[0]
    pct := record[1]
    practice := record[2]
    bnf := &bnf{ record[3], record[4] }
    
    items, err := strconv.Atoi(record[5])
    if err != nil { panic(err)}

    nic, err := strconv.ParseFloat(record[6], 64)
    cost, err := strconv.ParseFloat(record[7], 64)
    period, err := strconv.Atoi(record[8])

    return Prescription{sha, pct, practice, *bnf, items, nic, cost, period}
}


func show_queue(channel chan int) {
    
    for {
        value := <- channel
        if value == -1 {
            break
        }
        fmt.Println( "Processed ", value)
        time.Sleep(1000 * time.Millisecond)

    }
}


func produce(channel chan Prescription, filename string) {
    //open file 
    fi, err := os.Open(filename)
    if err != nil { panic(err) }

    csv := csv.NewReader(fi)

    skip := true

    fmt.Println("producing")

    for {
        record, err := csv.Read()
        if err != nil { 
            break 
        } else if err == io.EOF{ 
            break
        }
        
        if skip != false { 
            skip = false
            continue
        }
        pres := NewPrescription(record) 
        channel <- pres
    }
    close(channel)

}

func consume(channel chan Prescription) {
    fmt.Println("consuming")
    session, err := mgo.Dial("localhost:27017")
    if err != nil { panic(err) }
    defer session.Close()
    
    //collection instance 
    c := session.DB("gotest").C("prescription")

    //create status channel
    status := make( chan int, 1)
    go show_queue(status)
    i := 1
    for pres := range channel {
        err := c.Insert( &pres )
        if err != nil { panic(err)}
        i++
        if len(status) == 0 { status <- i}
    }
    //end the show_queue process
    status <- -1
    close(status)

}

func main() {
    channel := make(chan Prescription)
    //filen := *flag.String( "filename", "../data/sample.csv", "File CSV to import")
    filename := "../data/data.csv"
    go produce(channel, filename)
    consume(channel)
    
}
