package org.egov.pgrrest.read.domain.factory.utility;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.function.BiFunction;

public class DateDifferenceInDays implements BiFunction<LocalDate, LocalDate, Integer> {
    public static final String NAME = "dateDifferenceInDays";

    @Override
    public Integer apply(LocalDate earlierDate, LocalDate laterDate) {
        if (earlierDate == null || laterDate == null) {
            return null;
        }
        return Days.daysBetween(earlierDate, laterDate).getDays();
    }
}
