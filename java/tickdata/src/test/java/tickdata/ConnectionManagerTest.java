package tickdata;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.mongodb.DB;
import com.mongodb.ServerAddress;

public class ConnectionManagerTest {

    ConnectionManager obj;
    
    String[] hosts;
    
    @Before
    public void setUp() throws Exception {
        hosts = new String[]{"localhost:27017"};
        obj = new ConnectionManager(hosts);
    }

    @Test
    public void testBuildServerAddress() {
        
        ServerAddress addr = ConnectionManager.buildServerAddress("wrong:27017") ;
        
        assertNull( addr );
        
        assertNotNull(ConnectionManager.buildServerAddress("nair.local:27017") );
        
    }

    @Test
    public void testConnectionManager() {
        
        assertNotNull(obj);
        
    }

    @Test
    public void testGetDBDoNotCreateNewDB() {
        DB db = obj.getDB("tickdata", false);
        
        assertNull(db);
    }
    
    @Test
    public void testGetDBCreateDB(){
        DB db = obj.getDB("tickdata", true);
        assertNull(db);
        
    }

}
