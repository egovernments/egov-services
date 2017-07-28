package org.egov.pgrrest.read.domain.factory.utility;

import org.joda.time.LocalDate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DateDifferenceInDaysTest {

    @Test
    public void test_should_return_2_for_days_between_2_days_ago_and_today() {
        final DateDifferenceInDays dateDifferenceInDays = new DateDifferenceInDays();
        final LocalDate now = LocalDate.now();

        final Integer actualDays = dateDifferenceInDays.apply(now.minusDays(2), now);

        assertEquals(Integer.valueOf(2), actualDays);
    }

    @Test
    public void test_should_return_minus_2_days_between_today_and_2_days_ago() {
        final DateDifferenceInDays dateDifferenceInDays = new DateDifferenceInDays();
        final LocalDate now = LocalDate.now();

        final Integer actualDays = dateDifferenceInDays.apply(now, now.minusDays(2));

        assertEquals(Integer.valueOf(-2), actualDays);
    }

    @Test
    public void test_should_return_null_when_first_date_parameter_is_null() {
        final DateDifferenceInDays dateDifferenceInDays = new DateDifferenceInDays();
        final LocalDate now = LocalDate.now();

        final Integer actualDays = dateDifferenceInDays.apply(null, now.minusDays(2));

        assertNull(actualDays);
    }

    @Test
    public void test_should_return_null_when_second_date_parameter_is_null() {
        final DateDifferenceInDays dateDifferenceInDays = new DateDifferenceInDays();
        final LocalDate now = LocalDate.now();

        final Integer actualDays = dateDifferenceInDays.apply(now, null);

        assertNull(actualDays);
    }



}