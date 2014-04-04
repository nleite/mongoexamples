package tickdata.ticks;

import java.util.Date;

public interface Tick extends MDom {

    void setBidPrice(final long bidPrice);

    void setTime(final Date time);

    void setInstrument(final String inst);

    void setBidType(TickBidType type);

    void setPrice(final long price);

    void setVolume(final int volume);

    void setAskSize(final int askSize);

    void setAskPrice(final long askPrice);

    public long getBidPrice();

    public Date getTime();

    public TickBidType getType();

    public String getInstrument();

    public long getPrice();

    public long getAskPrice();

    public int getVolume();

    public int getAskSize();
}
