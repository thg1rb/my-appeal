package ku.cs.services;

import java.time.LocalDateTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class DateTimeService {

    public static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    public static DateTimeFormatter detailedFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static String dateToString(Date date){
        return dateFormatter.format(date);
    }

    public static String detailedDateToString(LocalDateTime date){
        return detailedFormatter.format(date);
    }

    public static String updateTime(){
        LocalDateTime date = LocalDateTime.now();
        return detailedFormatter.format(date);
    }

//    public static int compareDate(String date1, String date2){
//        try{
//            Date dateTime1 = detailedFormatter.parse(date1);
//            Date dateTime2 = detailedFormatter.parse(date2);
//            return dateTime1.compareTo(dateTime2);
//        }
//        catch(Exception e){
//            e.printStackTrace();
//            return date1.compareTo(date2);
//        }
//
//    }
public static int compareDate(String date1, String date2) {
    try {
        // Parse the date strings into LocalDateTime objects
        LocalDateTime dateTime1 = LocalDateTime.parse(date1, detailedFormatter);
        LocalDateTime dateTime2 = LocalDateTime.parse(date2, detailedFormatter);
        // Compare the LocalDateTime objects
        return dateTime2.compareTo(dateTime1);
    } catch (DateTimeParseException e) {
        // Provide detailed error feedback on failed parsing
        System.out.println("Error parsing dates: " + date1 + " or " + date2);
        System.out.println("Expected format: " + detailedFormatter.toString());
        e.printStackTrace();
        // Return a comparison fallback or an indicator value
        return 0; // Or use a default string comparison as a fallback: date1.compareTo(date2)
    }
}


}
