package org.egov.web.indexer.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.springframework.util.StringUtils.isEmpty;

@Service
public class ESDateTimeFormatter {

    private static final SimpleDateFormat ES_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private static final SimpleDateFormat INPUT_DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");


    public ESDateTimeFormatter(@Value("${app.timezone}") String timeZone) {
        ES_DATE_TIME_FORMAT.setTimeZone(TimeZone.getTimeZone(timeZone));
        INPUT_DATE_TIME_FORMAT.setTimeZone(TimeZone.getTimeZone(timeZone));
    }

    public String toESDateTimeString(String date) {
        if (isEmpty(date)) {
            return null;
        }
        try {
            final Date inputDate = INPUT_DATE_TIME_FORMAT.parse(date);
            return ES_DATE_TIME_FORMAT.format(inputDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
