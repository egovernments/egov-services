package org.egov.pgr.common.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.springframework.util.StringUtils.isEmpty;

public class DateFormatter {
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public static String toString(java.util.Date date) {
        if (date == null) {
            return null;
        }
        return DATE_TIME_FORMAT.format(date);
    }

    public static java.util.Date toDate(String dateTime) {
        if (isEmpty(dateTime)) {
            return null;
        }
        try {
            return DATE_TIME_FORMAT.parse(dateTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
