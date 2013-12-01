package emlx

import (
	"testing"
    "time"
)

func TestReadMailsFolder(t *testing.T) {

	path := "/Users/norberto/Library/Mail/"
	limit := 10
	queue := make(chan Mail, 10)

	go ReadMails(path, limit, queue)
    
    time.Sleep( 2 * time.Second )

	actual := len(queue)
	expected := 10

	if expected != actual {
		t.Error("FAIL: Should be the same number of elements on channel", actual, expected)
	}

}

func TestNewEmlx(t *testing.T) {
	subject := "bla"
	to := "to"
	from := "from"
	body := "Yeah Body"
	date := ""
	//expected := Mail{subject, from, to, body, date}

	_, ok := NewMail(subject, from, to, body, date)

	if !ok {
		t.Error("Fail: should not return nil")
	}
}

func TestNewEmlxInvalidArgs(t *testing.T) {
	subject := "body"
	to := ""
	from := "from"
	body := "Yeah Body"
	date := ""

	_, ok := NewMail(subject, from, to, body, date)

	if ok {
		t.Error("Fail: should return false")
	}
}

func TestNewMailFromDict(t *testing.T) {
	dic := make(map[string]string)
	dic["Subject"] = "some subject"

	_, ok := NewMailMap(dic)

	if !ok {
		t.Error("Incorrect Mail from dictionary")
	}
}

func TestParseMailFile(t *testing.T) {

	path := "/Users/norberto/Library/Mail/V2/IMAP-norberto@10gen.com@imap.gmail.com/[Gmail].mbox/All Mail.mbox/BB4A8E2F-5296-4417-9B54-5209B68176F6/Data/1/1/3/Messages/311045.emlx"

	ParseMailFile(path)

}
