#!/bin/python
from pymongo import MongoClient
from datetime import datetime as date

class Series:

    def __init__(self, host, dbname, collname):
        self.mc = MongoClient( host)
        self.db = self.mc[dbname]
        self.col = self.db[collname]

    def get_series_count(self, instrument, bdate, edate):
        query = { 'instrument': instrument,
                "time":  {"$gt": bdate, "$lt": edate } }

        print "%s" % query
        return self.col.find( query ).count()

series = Series( 'shard0/20.1.67.40:27017', 'TicksDirect', 'nticks' )
instrument = "BBVA.MC" #"DTEGn.DE"
bdate = date(2012, 1, 1)
edate = date(2013, 12, 31)
print( "Number of series %s between %s and %s" % (series.get_series_count(instrument, bdate, edate), bdate.isoformat(), edate.isoformat()))


