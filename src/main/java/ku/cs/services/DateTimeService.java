package ku.cs.services;

import java.time.LocalDateTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeService {

    public SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    public SimpleDateFormat detailedFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public String dateToString(Date date){
        return dateFormatter.format(date);
    }

    public String detailedDateToString(Date date){
        return detailedFormatter.format(date);
    }

}
