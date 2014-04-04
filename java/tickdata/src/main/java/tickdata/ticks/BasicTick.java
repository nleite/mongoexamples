package tickdata.ticks;

import java.util.Date;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class BasicTick extends BaseTick {

    private long bidPrice;
    private Date time;
    private TickBidType type;
    private String instrument;
    private ObjectId _id = null;
    private long price;
    private long askPrice;
    private int volume;
    private int askSize;
    
    public BasicTick(){
        super();
    }
    
    public BasicTick(long bidPrice, Date time, TickBidType type, String instrument) {
        super();
        this.bidPrice = bidPrice;
        this.time = time;
        this.type = type;
        this.instrument = instrument;
    }

    public BasicTick(long bidPrice, Date time, TickBidType type, String instrument, ObjectId _id) {
        super();
        this.bidPrice = bidPrice;
        this.time = time;
        this.type = type;
        this.instrument = instrument;
        this._id = _id;
    }

    public void setBidPrice(long bidPrice) {
        this.bidPrice = bidPrice;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setInstrument(String inst) {
        this.instrument = inst;
    }

    public void setBidType(TickBidType type) {
        this.type = type;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setAskSize(int askSize) {
        this.askSize = askSize;
    }

    public void setAskPrice(long askPrice) {
        this.askPrice = askPrice;
    }

    
    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public long getBidPrice() {
        return bidPrice;
    }

    public Date getTime() {
        return time;
    }

    public TickBidType getType() {
        return type;
    }

    public String getInstrument() {
        return instrument;
    }

    public long getPrice() {
        return price;
    }

    public long getAskPrice() {
        return askPrice;
    }

    public int getVolume() {
        return volume;
    }

    public int getAskSize() {
        return askSize;
    }

    public BasicDBObject toMongoDB() {
        BasicDBObject dbo = new BasicDBObject(INSTRUMENT, this.instrument);
        dbo.put(TIME, this.time);
        dbo.put(BID_PRICE, this.bidPrice);
        dbo.put(BID_TYPE, type.toString());
        dbo.put(ASK_PRICE, this.askPrice);
        dbo.put(ASK_SIZE, this.askSize);
        dbo.put(VOLUME, this.volume);
        dbo.put(PRICE, this.price);
        if(this._id != null){
            dbo.put("_id", _id);
        }
        return dbo;
    }

    public void fromMongoDB(DBObject dbo) {
        this.set_id((ObjectId) dbo.get("_id"));
        this.setAskPrice((Long) dbo.get(ASK_PRICE) );
        this.setAskSize((Integer) dbo.get(ASK_SIZE));
        this.setBidPrice((Long) dbo.get(BID_PRICE));
        this.setBidType(TickBidType.getBidType((String) dbo.get(BID_TYPE)));
        this.setInstrument((String) dbo.get(INSTRUMENT));
        this.setVolume((Integer) dbo.get(VOLUME));
        this.setPrice((Long) dbo.get(PRICE));
    }
    
}
