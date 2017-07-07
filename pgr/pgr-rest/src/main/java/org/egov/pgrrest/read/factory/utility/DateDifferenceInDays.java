package org.egov.pgrrest.read.factory.utility;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.function.BiFunction;

public class DateDifferenceInDays implements BiFunction<LocalDate, LocalDate, Integer> {
    public static final String NAME = "dateDifferenceInDays";
    @Override
    public Integer apply(LocalDate date1, LocalDate date2) {
        return Days.daysBetween(date1, date2).getDays() ;
    }
}
