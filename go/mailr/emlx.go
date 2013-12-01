package emlx

import (
	"fmt"
	"os"
	//"net/mail"
	"bufio"
	"io/ioutil"
	"strings"
	//"labix.org/v2/mgo"
	"path/filepath"
)

const EMPTY string = ""

type Mail struct {
	subjet string
	from   string
	to     string
	body   string
	date   string
	//data
}

type MailBox struct {
	rootpath string
}

/*
func InsertMails( mongo session, dbname string, collname string, in chan Mail) {

    c := mongo.DB(dbname).C(collname)
    for i, mail := range in {
        err := c.Insert( &mail )
        if err != nil { panic(err)}
        fmt.Println("inserted one more")
    }

}
*/

//give a file info
// return a Mail structure
func ParseMailFile(path string) (Mail, error) {

	fi, err := os.Open(path)
	if err != nil {
		fmt.Println("ERROR")
	}

	defer fi.Close()

	//data, err := mail.ReadMessage(fi)
	data, err := ioutil.ReadAll(fi)

	fi2, err := os.Open(path)
	if err != nil {
		fmt.Println("ERROR")
	}

	reader := bufio.NewReader(fi2)

	scanner := bufio.NewScanner(reader)

	mp := make(map[string]string)

	for scanner.Scan() {

		text := scanner.Text()

		if strings.Contains(text, ":") {

			ss := strings.SplitN(text, ":", 2)

			if len(ss) < 2 {
				fmt.Println(ss[0])
			}

			if len(ss) == 2 {
				mp[ss[0]] = ss[1]
			}
		}

	}

	if err != nil {
		panic(err)
	}
	data = data
	mail, ok := NewMailMap(mp)
	if !ok {
		panic("Problem")
	}
	return mail, err

}

/*

func FindMailFiles(ch chan Mail, extension string, path string, info os.FileInfo, err error) (error){

	if info.IsDir() { return nil }

	//check file extension
	if path[len(path)-3 : len(path)] == extension {

		mail, err := ParseMailFile( path )

		if err != nil{ return err}

		queue <- mail
	}
	return nil
} */

//get a folder to read from
// limit on number of files to produce (if > 0 )
// channel to send the parsed emails
func ReadMails(folderPath string, limit int, queue chan Mail) {
	extension := "emlx"
    current := 0
	//walk the folder
	filepath.Walk(folderPath, func(path string, info os.FileInfo, err error) error {
		//    if err != nil { panic("NOOOOOOOOOO")}
		//    return FindMailFiles( queue, path, info, err)
		if info.IsDir() {
			return nil
		}
        

        fmt.Println(extension)
        ac := path[len(path)- len(extension):len(path)]  
        fmt.Println(ac)
		//check file extension and limit 
		if path[len(path)-len(extension):len(path)] == extension && current < limit {
            fmt.Println("OKOKOKOKO")
			mail, err := ParseMailFile(path)
            

			if err != nil {
				return err
			}

			queue <- mail
		}
		return nil
	})

}

func NewMailMap(dic map[string]string) (Mail, bool) {
	subject, ok := dic["Subject"]
	if !ok {
		panic("No Subject")
	}
	from, ok := dic["From"]
	to, ok := dic["To"]
	body, ok := dic["Body"]
	date, ok := dic["Date"]

	fmt.Println(subject)

	mail := Mail{subject, from, to, body, date}

	return mail, true
}

func NewMail(subject string, from string, to string, body string, date string) (Mail, bool) {

	if from == EMPTY || to == EMPTY {
		return *(new(Mail)), false
	}

	mail := Mail{subject, from, to, body, date}

	//fmt.Println(mail)

	return mail, true
}
