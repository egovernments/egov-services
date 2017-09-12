package org.egov.property.utility;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeStampUtil {

    public static Timestamp getTimeStamp(String date) {
        Timestamp timestamp = null;
        if (date == null) {
            return null;
        } else {
            DateTimeFormatter[] formatter = new DateTimeFormatter[] { DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.S") };
            for (int i = 0; i < formatter.length; i++) {
                try {
                    LocalDateTime time = LocalDateTime.from(LocalDate.parse(date, formatter[i]).atStartOfDay());
                    timestamp = Timestamp.valueOf(time);
                    return timestamp;
                } catch (Exception ex) {
                    continue;
                }

            }
        }

        return timestamp;
    }

    public static String getDateFormat(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat connvertFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return connvertFormat.format(simpleDateFormat.parse(date));

    }

    public static int getYear(String date) {
        int year = 0;
        if (date == null) {
            return 0;
        } else {
            DateTimeFormatter[] formatter = new DateTimeFormatter[] { DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.S") };
            for (int i = 0; i < formatter.length; i++) {
                try {
                    LocalDateTime time = LocalDateTime.from(LocalDate.parse(date, formatter[i]).atStartOfDay());
                    year = time.getYear();
                } catch (Exception ex) {
                    continue;
                }

            }
        }

        return year;
    }
}
