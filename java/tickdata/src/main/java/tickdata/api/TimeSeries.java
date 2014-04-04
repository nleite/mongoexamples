package tickdata.api;

import java.util.Calendar;
import java.util.Date;

import tickdata.ticks.BaseTick;
import tickdata.ticks.BasicTick;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

public class TimeSeries {

    private DB db;
    private DBCollection col;
    
    private Date lastMovingAvgDate = null;
    
    public TimeSeries(DB db, DBCollection col) {
        super();
        this.db = db;
        this.col = col;
    }
    
    
//    public void getTimeCandle(final String instrument,final TimeInterval interval)
    
    
    public void movingAverage(final String instrument, final int timeInSeconds){
        
        if ( lastMovingAvgDate == null){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, timeInSeconds*-1);
            this.lastMovingAvgDate = calendar.getTime();
        }
        
        DBObject query = QueryBuilder.start("instrument").is(instrument).and("time").greaterThan(lastMovingAvgDate).get();
    }
    
    
}
