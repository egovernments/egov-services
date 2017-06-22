package org.egov.pgr.employee.enrichment.factory;

import org.apache.commons.lang3.time.DateUtils;
import org.egov.pgr.employee.enrichment.DateConverter;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertTrue;

public class DateFactoryTest {

    @Test
    public void test_should_return_current_date() {
        TimeZone.setDefault(TimeZone.getTimeZone(DateConverter.IST));

        assertTrue(DateUtils.truncatedEquals(new Date(), new DateFactory().now(), Calendar.SECOND));
    }
}