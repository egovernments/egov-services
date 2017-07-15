package org.egov.web.indexer.repository;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ESDateTimeFormatter {

    private static final String ES_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static String toESDateTimeString(Date date) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(ES_DATE_TIME_FORMAT);
        return dateFormat.format(date);
    }
}
