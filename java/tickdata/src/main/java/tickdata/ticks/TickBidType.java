package tickdata.ticks;

public enum TickBidType {
    
    BUY("buy", 0), SELL("sell", 1);
   
    private final String type;
    private final int pos;
    private TickBidType(final String type, final int pos) {
        this.type = type;
        this.pos = pos;
    }
    
    public String toString(){
        return this.type;
    }
    
    //TODO check this exception
    public static TickBidType getBidType(final String wantedType) throws NoSuchFieldError{
        if( BUY.type.equals(wantedType)){
            return BUY;
        } 
        if (SELL.type.equals(wantedType)){
            return SELL;
        }
        throw new NoSuchFieldError();
        
    }
    
    public static TickBidType getBidTypebyPos( final int wantedPos){
        if (BUY.pos == wantedPos){
            return BUY;
        }
        return SELL;
        
        
    }
    
}
