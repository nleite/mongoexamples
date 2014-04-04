package tickdata.api;

import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tickdata.ticks.BasicTick;
import tickdata.ticks.TickBidType;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class VWAPTest {

    DBCollection col;

    VWAP obj;

    @Before
    public void setUp() throws Exception {
        MongoClient mc = new MongoClient();
        col = mc.getDB("hey").getCollection("vwap");
        
        DBObject tick0 = (new BasicTick(10, new Date(), TickBidType.BUY, "GOOG", 10L, 10L, 100, 10)).toMongoDB(); 
        DBObject tick1 = (new BasicTick(10, new Date(), TickBidType.BUY, "GOOG", 10L, 10L, 10, 10)).toMongoDB() ;
        DBObject tick2 = (new BasicTick(10, new Date(), TickBidType.BUY, "GOOG", 20L, 10L, 80, 10)).toMongoDB() ;
        col.insert( tick1);
        col.insert( tick0);
        col.insert( tick2);

        
        Calendar cal = Calendar.getInstance();
        //all inserts from 60 seconds ago
        cal.add(Calendar.SECOND, -60);
        Date bdate = cal.getTime();
        obj = new VWAP(col, bdate);
    }

    // vwap = sum( price * volume) / sum(volume) per period of time
    @Test
    public void test() {
        double expected =   Math.ceil( (float) ((10 * 10) + (10 * 100) + (80*20)  ) / 190);

        double actual = Math.ceil( obj.onInstrument("GOOG"));

        Assert.assertEquals(expected, actual);
    }

    @After
    public void tearDown() {
        col.drop();
    }

}
