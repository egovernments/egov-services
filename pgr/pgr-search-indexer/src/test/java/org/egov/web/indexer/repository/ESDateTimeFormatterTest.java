package org.egov.web.indexer.repository;

import org.junit.Test;

import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ESDateTimeFormatterTest {

    @Test
    public void test_should_convert_date_to_elastic_search_string_format_with_utc_timezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        final ESDateTimeFormatter esDateTimeFormatter = new ESDateTimeFormatter("UTC");

        final String actualDateTimeString = esDateTimeFormatter.toESDateTimeString("03-12-2017 23:45:23");

        assertEquals("2017-12-03T23:45:23.000+0000", actualDateTimeString);
    }

    @Test
    public void test_should_return_null_when_input_date_is_null() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        final ESDateTimeFormatter esDateTimeFormatter = new ESDateTimeFormatter("UTC");

        final String actualDateTimeString = esDateTimeFormatter.toESDateTimeString(null);

        assertNull(actualDateTimeString);
    }

    @Test(expected = RuntimeException.class)
    public void test_should_throw_exception_when_input_date_is_not_of_expected_format() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        final ESDateTimeFormatter esDateTimeFormatter = new ESDateTimeFormatter("UTC");

        esDateTimeFormatter.toESDateTimeString("2017/12/03 23:45:23");
    }

}