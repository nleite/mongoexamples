package tickdata.engine;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;

import tickdata.ticks.BasicTick;

public class BasicTickFileGenerator extends BaseFileGenerator implements TickGenerator<BasicTick> {

    
    BufferedReader reader;
    
    public BasicTickFileGenerator(String path) {
        super(path);
        // TODO Auto-generated constructor stub
    }

    public BasicTick next() throws EOFException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    void init() {
        this.file = new File(path);
        try{
            this.reader = new BufferedReader(new FileReader(this.file));
            reader.readLine();
        } catch (IOException e){
            System.out.println(MessageFormat.format("IO not well: {}",  e));
            
        }
        
    }

}
