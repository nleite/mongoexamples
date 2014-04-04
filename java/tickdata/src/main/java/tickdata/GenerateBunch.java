package tickdata;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import tickdata.api.MovingAverage;
import tickdata.engine.RandomTickGenerator;
import tickdata.ticks.BasicTick;
import tickdata.ticks.Tick;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

public class GenerateBunch {

    public static void main(String[] args) throws InterruptedException {

        RandomTickGenerator generator = new RandomTickGenerator();
        
        generator.init();

        String[] hosts = { "nair.local" };
        ConnectionManager conm = new ConnectionManager(hosts);
        String dbname = "ticks";
        DB db = conm.getDB(dbname, true);

        
        //collect the a sample of the last 10minutes
        //10 minute
        int timeInSeconds= 6000;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, timeInSeconds*-1);
        Date lastMovingAvgDate = calendar.getTime();
        
        int seedSize = 100;
        DBCollection bunch = db.getCollection("bunch");
        DBObject query = QueryBuilder.start("instrument").is("GOOG").and("time").greaterThan(lastMovingAvgDate).get();
        BasicDBObject orderBy = new BasicDBObject("_id", -1);
        DBCursor cur = bunch.find(query).sort(orderBy).limit(seedSize);
        
        
        DB local = conm.getDB("local", false);
        DBCollection oplog = local.getCollection("oplog.rs");
        
        AtomicBoolean running = new AtomicBoolean(true);
        Tick[] seed = new Tick[seedSize];
        
        
        for(int j=0; j < 10; j++){
            BasicTick t = new BasicTick();
            if (!cur.hasNext()){
                break;
            }
            t.fromMongoDB(cur.next());
            
            seed[j] = t;
        }
        
        MovingAverage ma = new MovingAverage(seed, lastMovingAvgDate, oplog, "ticks.bunch", running);
        Thread tAvg = new Thread(ma);
        tAvg.start();
        
        while(true){
             
            
            for (Tick tick : generator) {
                
                BasicDBObject dbo = tick.toMongoDB();
            
                bunch.insert(dbo);
            }
            
            System.out.println("AVERAGE : "+ma.getAverage());
            
        }

    }

}
