package account.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateConverterUtil {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM-yyyy");

    public static LocalDate fromString(String dateString) {
        if (dateString == null)
            return null;

        int month = Integer.parseInt(dateString.substring(0, 2));
        int year = Integer.parseInt(dateString.substring(3));
        return LocalDate.of(year, month, 1);
    }

    public static String fromLocalDate(LocalDate localDate) {
        if (localDate == null)
            return null;
       return localDate.format(formatter);
    }
}
