package tickdata.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mongodb.AggregationOptions.OutputMode;
import com.mongodb.*;

public class VWAP {
    
    
    private DBCollection col;
    private Date bdate;

    public VWAP(final DBCollection col, final Date bdate) {
        super();
        this.col = col;
        this.bdate = bdate;
    }
    
    //db.bunch.aggregate( {"$limit":100}, 
       
     
    
    public double onInstrument( final String instrument){
        
        double vwap = 0;
        
        List<DBObject> pipeline = new ArrayList<DBObject>();
        DBObject mbody = new BasicDBObject("instrument", instrument);
        mbody.put("time", new BasicDBObject( "$gt", this.bdate ));
        DBObject match = new BasicDBObject( "$match", mbody );
        pipeline.add(match);

        //{$project: { instrument:1, volume_price: {"$multiply": ["$volume", "$price"]}, "volume":1 }},
        DBObject pbody1 = new BasicDBObject("instrument", 1);
        pbody1.put("volume", 1);
        String[] multiples = {"$volume", "$price"};
        DBObject multiply = new BasicDBObject("$multiply", multiples);
        pbody1.put("volume_price", multiply);
        DBObject project1 = new BasicDBObject("$project", pbody1);
        pipeline.add(project1);
        
        //{ '$group': { _id: '$instrument', total_volume_price:{$sum:"$volume_price"  }, total_v:{$sum: "$volume"}    }  } ,
        DBObject gbody = new BasicDBObject("_id", "$instrument");
        gbody.put("tvp", new BasicDBObject( "$sum", "$volume_price" ));
        gbody.put("tv", new BasicDBObject("$sum", "$volume"));
        DBObject group = new BasicDBObject("$group", gbody );
        pipeline.add(group);
        
        // { $project: { vwap: {"$divide":["$total_volume_price", "$total_v"  ]}    }  }    )
        String[] fraction = {"$tvp", "$tv"};
        DBObject pbody2 = new BasicDBObject("vwap", new BasicDBObject( "$divide", fraction )  );
        DBObject project2 = new BasicDBObject("$project", pbody2);
        pipeline.add(project2);
        
        AggregationOptions options = AggregationOptions.builder().outputMode(OutputMode.CURSOR ).build();
        Cursor out = col.aggregate(pipeline, options);
        
        while(out.hasNext()){
            vwap =  (Double) out.next().get("vwap");
        }
        return vwap;
    }
    

}
