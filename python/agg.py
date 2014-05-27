#!/usr/bin/python

from pymongo import MongoClient
import click
def aggcursor(collection):
    print("Cursor Result")
    #aggregate and collect the results so we can iterate
    match = {"$match": {"headers.To": "eric.bass@enron.com"}}
    group = {"$group":{"_id": "$headers.From", "sum": {"$sum": 1}  }}
    pipeline = [match, group]
    cur = collection.aggregate( pipeline, cursor={})
    for d in cur:
        print(d)

def aggdoc(collection):
    print("Result Document")
    match = {"$match": {"headers.To": "eric.bass@enron.com"}}
    group = {"$group":{"_id": "$headers.From", "sum": {"$sum": 1}  }}
    pipeline = [match, group]
    res = collection.aggregate( pipeline)
    print(res)


@click.command()
@click.option("--host", default="localhost", help="mongodb server host name")
@click.option("--colname", default="messages", help="collection name where to run the tests")
@click.option("--dbname", default="enron", help="database name")
def main(host, dbname, colname):
    mc = MongoClient(host)
    db = mc[dbname]
    col = db[colname]
    aggcursor(col)
    aggdoc(col)


if __name__ == "__main__":
    main()


