package org.egov.egf.bill.domain.service;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;
import org.egov.BillTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@Import(BillTestConfiguration.class)
@RunWith(SpringRunner.class)
public class DateFactoryTest {

    @Test
    public void test_should_return_current_date() {
        TimeZone.setDefault(TimeZone.getTimeZone("IST"));

        assertTrue(DateUtils.truncatedEquals(new Date(), new DateFactory().create(), Calendar.SECOND));
    }
}