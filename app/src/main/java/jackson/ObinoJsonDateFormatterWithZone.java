package jackson;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ObinoJsonDateFormatterWithZone extends SimpleDateFormat {
    
    
    public static final String _dateFormatSting = "yyyy-MM-dd'T'HH:mm:ss'Z'Z";

    public ObinoJsonDateFormatterWithZone() {
        super(_dateFormatSting, Locale.ENGLISH);
    }

    @Override
    public Date parse(String dateString) throws ParseException {

        if(dateString == null) {
            return null;
        }

        StringBuilder newDateSB=new StringBuilder(dateString);
    
        if(dateString.indexOf("T")<0) {
            // does not contain time, so setting time to mid-night
            newDateSB.append("T00:00:00");
        }

        if(dateString.indexOf("Z")<0) {
            // does not contain timezone
            newDateSB.append("Z");
            newDateSB.append(TimeZone.getDefault().getID());
        }
        
        return super.parse(newDateSB.toString());


    }


}
