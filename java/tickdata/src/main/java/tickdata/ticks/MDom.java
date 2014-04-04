package tickdata.ticks;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public interface MDom {

    public BasicDBObject toMongoDB();
    public void fromMongoDB(DBObject dbo);
}
