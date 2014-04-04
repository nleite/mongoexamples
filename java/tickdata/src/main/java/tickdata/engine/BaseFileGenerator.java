package tickdata.engine;

import java.io.File;

public abstract class BaseFileGenerator {

    final String path;
    File file;
    
    public BaseFileGenerator(final String path){
        this.path = path;
    }
    
    abstract void init();
    
}
