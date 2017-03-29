package org.egov.pgr.employee.enrichment;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateConverter {

    public static final String IST = "Asia/Calcutta";
    private LocalDateTime dateTime;

    public DateConverter(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public java.util.Date toDate() {
        return java.util.Date.from(ZonedDateTime.of(dateTime, ZoneId.of(IST)).toInstant());
    }

}
