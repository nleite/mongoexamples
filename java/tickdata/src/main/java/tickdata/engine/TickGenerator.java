package tickdata.engine;

import java.io.EOFException;

import tickdata.ticks.BaseTick;

public interface TickGenerator<T extends BaseTick> {

    T next() throws EOFException;
    
}
