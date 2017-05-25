package org.egov.pgr.common.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateFormatter {
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public String toString(java.util.Date date) {
        return DATE_TIME_FORMAT.format(date);
    }

    public java.util.Date toDate(String dateTime) {
        try {
            return DATE_TIME_FORMAT.parse(dateTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
