package tickdata.engine;

import java.io.EOFException;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import tickdata.ticks.BasicTick;
import tickdata.ticks.Tick;
import tickdata.ticks.TickBidType;

public class RandomTickGenerator implements TickGenerator<BasicTick>, Iterable<Tick>{

    private int counter = 0;
    private int max = 10000;
    private BasicTick[] ticks;
    private Random rand;
    private String[] instrumentPopulation = {"TEF", "GOOG", "APP", "CRM", "EMC"};

    public RandomTickGenerator() {
        ticks = new BasicTick[max];
    }
    
    
    public BasicTick generate(){
        
        long bidPrice = rand.nextInt(400);
        Date time = new Date();
        TickBidType type = TickBidType.getBidTypebyPos(rand.nextInt(2));
        String instrument = instrumentPopulation[ rand.nextInt(instrumentPopulation.length-1) ];
        int volume = rand.nextInt(400);
        BasicTick tick = new BasicTick(bidPrice, time, type, instrument);
        tick.setAskPrice(bidPrice-1);
        tick.setAskSize(volume);
        tick.setVolume(volume);
        tick.setPrice(bidPrice+1);
        return tick;
        
        
    }
    
    
    public void init(){
        Date date = new Date();
        long seed = this.hashCode() * date.getTime();
        rand = new Random(seed);
        for(int i=0; i < max; i++){
            ticks[i] = generate();
        }
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + counter;
        result = prime * result + max;
        result = prime * result + ((rand == null) ? 0 : rand.hashCode());
        result = prime * result + Arrays.hashCode(ticks);
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RandomTickGenerator other = (RandomTickGenerator) obj;
        if (counter != other.counter)
            return false;
        if (max != other.max)
            return false;
        if (!Arrays.equals(ticks, other.ticks))
            return false;
        return true;
    }


    public Iterator<Tick> iterator() {
        return new TickDataIterator(ticks);
    }


    public BasicTick next() throws EOFException {
        // TODO Auto-generated method stub
        return null;
    }

}
