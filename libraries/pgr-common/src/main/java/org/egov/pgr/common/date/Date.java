package org.egov.pgr.common.date;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@AllArgsConstructor
public class Date {
    private static final String IST = "Asia/Calcutta";
    private LocalDateTime localDateTime;

    public java.util.Date toDate() {
        final ZonedDateTime zonedDateTime = ZonedDateTime.of(this.localDateTime, ZoneId.of(IST));
        return java.util.Date.from(zonedDateTime.toInstant());
    }
}
