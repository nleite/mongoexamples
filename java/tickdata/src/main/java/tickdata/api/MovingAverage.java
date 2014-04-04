package tickdata.api;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bson.types.BSONTimestamp;

import tickdata.ticks.BasicTick;
import tickdata.ticks.Tick;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.Bytes;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MovingAverage implements Runnable {

    private int size = 0;
    private Queue<Tick> sample;
    final private DBCollection col;
    private String ns;
    private Date from;
    private AtomicBoolean _running;
    private Tick head = null;
    Set<String> instrumentsSet;
    

    private long totalPrice = 0;

    public MovingAverage(final Tick[] seed, final Date from, final DBCollection col, final String namespace,
            AtomicBoolean running) {
        sample = new ArrayBlockingQueue<Tick>(seed.length);
        instrumentsSet = new HashSet<String>();
        for (Tick t : seed) {
            if (t != null) {
                sample.add(t);
                totalPrice += t.getPrice();
                size++;
                instrumentsSet.add(t.getInstrument());
            }
        }
        
        this.col = col;
        this.ns = namespace;
        this.from = from;
        this._running = running;
    }

    private DBCursor createCursor(final Date bdate) {
        // insert operations on tick data
        BasicDBObject query = new BasicDBObject("op", "i");
        query.put("ns", ns);

        Date date = (from.before(bdate)) ? bdate : from;
        // get time in seconds
        query.put("ts", new BasicDBObject("$gt", date.getTime() * 1000));
        query.put("op", "i");
        if(!instrumentsSet.isEmpty()){
            BasicDBList instruments = new BasicDBList();
            instruments.addAll(instrumentsSet);
            query.put("o.instrument", new BasicDBObject("$in",  instruments));
        }
        System.out.println("QUERY: " + query.toString());
        
        return col.find().sort(new BasicDBObject("$natural", 1)).addOption(Bytes.QUERYOPTION_TAILABLE)
                .addOption(Bytes.QUERYOPTION_AWAITDATA);
    }

    public synchronized long getAverage() {
        if (size == 0) {
            return 0;
        }
        return totalPrice / size;
    }

    public void run() {

        while (_running.get()) {
            
            DBCursor cursor = createCursor(from);

            try {
                while (cursor.hasNext()) {
                    BasicDBObject dbo = (BasicDBObject) cursor.next();
                    Tick tick = new BasicTick();
                    tick.fromMongoDB((DBObject) dbo.get("o"));
                    BSONTimestamp ts = (BSONTimestamp) dbo.get("ts");
                    this.from = new Date(ts.getTime());
                    if (!this.sample.offer(tick)) {
                        head = this.sample.remove();
                        this.sample.add(tick);
                        totalPrice -= head.getBidPrice();
                    } else {
                        size++;
                    }
                    totalPrice += tick.getBidPrice();
                }
                

            } finally {
                try {
                    if (cursor != null) {
                        cursor.close();
                    }
                } catch (final Throwable e) {
                    System.out.println(e);
                }
            }
            try { System.out.println("Dormidina");Thread.sleep(100); } catch (final InterruptedException ie) { break; }
        }
        System.out.println("DONE");
    }

}
