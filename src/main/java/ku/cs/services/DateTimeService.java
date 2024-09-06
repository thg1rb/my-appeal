package ku.cs.services;

import java.time.LocalDateTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeService {

    public static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat detailedFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static String dateToString(Date date){
        return dateFormatter.format(date);
    }

    public static String detailedDateToString(Date date){
        return detailedFormatter.format(date);
    }

    public static String updateTime(){
        LocalDateTime date = LocalDateTime.now();
        return detailedFormatter.format(date);
    }

    public static int compareDate(String date1, String date2) {
        try {
            Date dateTime1 = detailedFormatter.parse(date1);
            Date dateTime2 = detailedFormatter.parse(date2);
            return dateTime2.compareTo(dateTime1);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
