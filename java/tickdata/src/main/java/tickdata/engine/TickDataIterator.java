package tickdata.engine;

import java.util.Iterator;

import tickdata.ticks.Tick;

public class TickDataIterator implements Iterator<Tick> {

    private Tick[] ticks;
    
    private int currentPos = 0;
    private int size;
    
    public TickDataIterator(Tick[] ticks){
        this.size = ticks.length;
        this.ticks = ticks;
    }
    
    public boolean hasNext() {
        return currentPos < size;
    }

    public Tick next() {
        return this.ticks[currentPos++];
    }

    public void remove() {
        // TODO Auto-generated method stub
        
    }

}
