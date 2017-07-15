package org.egov.web.indexer.repository;

import org.junit.Test;

import java.util.Date;
import java.util.TimeZone;

import static org.egov.pgr.common.date.DateFormatter.toDate;
import static org.junit.Assert.assertEquals;

public class ESDateTimeFormatterTest {

    @Test
    public void test_should_convert_date_to_elastic_search_string_format_with_india_timezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Calcutta"));

        final ESDateTimeFormatter esDateTimeFormatter = new ESDateTimeFormatter("Asia/Calcutta");
        final Date inputDate = toDate("03-12-2017 23:45:23");

        final String actualDateTimeString = esDateTimeFormatter.toESDateTimeString(inputDate);

        assertEquals("2017-12-03T23:45:23.000+0530", actualDateTimeString);
    }

}