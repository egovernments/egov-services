package org.egov.web.indexer.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Service
public class ESDateTimeFormatter {

    private String timeZone;
    private static final String ES_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public ESDateTimeFormatter(@Value("${app.timezone}") String timeZone) {
        this.timeZone = timeZone;
    }

    public String toESDateTimeString(Date date) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(ES_DATE_TIME_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        return dateFormat.format(date);
    }
}
