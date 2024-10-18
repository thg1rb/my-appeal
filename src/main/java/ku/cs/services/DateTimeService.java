package ku.cs.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class DateTimeService implements Comparator<String> {

    public static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat detailedFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static String detailedDateToString(Date date){
        return detailedFormatter.format(date);
    }

    public static String updateTime(){
        Date date = new Date();
        return detailedFormatter.format(date);
    }

    @Override
    public int compare(String o1, String o2) {
        if (o1 == null || o1.equals("null") || o1.equals("ไม่เคยเข้าใช้งาน")) {
            return 1;
        }else if (o2 == null || o2.equals("null") || o2.equals("ไม่เคยเข้าใช้งาน")) {
            return -1;
        }
        try {
            Date date1 = detailedFormatter.parse(o1);
            Date date2 = detailedFormatter.parse(o2);
            return date2.compareTo(date1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
