package tickdata.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeInterval {

    private static final String UTC_FORMAT = "YYYY/mm/ddTHH:MM:SSZ";
    
    private Date beginDate;
    private Date endDate;
    
    public TimeInterval(Date beginDate, Date endDate) {
        super();
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    public TimeInterval( final Date beginDate){
        super();
        this.beginDate = beginDate;
        //means infinite - no end
        this.endDate = null;
    }
    
    public TimeInterval( final String beginDate, final String endDate ) throws ParseException{
        this(beginDate, endDate, UTC_FORMAT);
    }
    
    public TimeInterval(final String beginDate, final String endDate, final String format) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        this.beginDate = sdf.parse(beginDate);
        this.endDate = sdf.parse(endDate);
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }
    
}
