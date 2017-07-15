package org.egov.web.indexer.repository;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.egov.pgr.common.date.DateFormatter.toDate;
import static org.junit.Assert.assertEquals;

public class ESDateTimeFormatterTest {

    private ESDateTimeFormatter esDateTimeFormatter;
    @Before
    public void before() {
        esDateTimeFormatter = new ESDateTimeFormatter();
    }

    @Test
    public void test_should_convert_date_to_elastic_search_string_format() {
        final Date inputDate = toDate("03-12-2017 23:45:23");
        final String actualDateTimeString = ESDateTimeFormatter.toESDateTimeString(inputDate);
        assertEquals("2017-12-03T23:45:23.000Z", actualDateTimeString);
    }

}