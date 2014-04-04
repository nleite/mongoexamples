package tickdata;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.ServerAddress;

public class ConnectionManager {

    
    private MongoClient mc;
    
    public static ServerAddress buildServerAddress(final String host){
        try {
            return new ServerAddress(host);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    public ConnectionManager(String ... hosts) {
        
        List<ServerAddress> svrAddrs = new ArrayList<ServerAddress>();
        
        for( String host : hosts){
            
            svrAddrs.add(buildServerAddress(host));
        }
        
        Builder builder = new MongoClientOptions.Builder();
        //builder.connectionsPerHost(1);
        //builder.connectTimeout(10);
        
        this.mc = new MongoClient(svrAddrs, builder.build());
    }
    
    
    public DB getDB( final String dbname, boolean create){
        DB db;
        
        if ( !create ){
            db = (this.mc.getDatabaseNames().indexOf(dbname) != -1 ) ? this.mc.getDB(dbname): null;
        } else {
            db = this.mc.getDB(dbname);
        }
        BasicDBObject cmd = new BasicDBObject("isMaster", true);
        CommandResult res = db.command(cmd);
        System.out.println(res);
        return db;
    }
    
    
}
