package org.egov.lams.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by rafeek on 4/5/18.
 */
public class DateUtils {

    public Date getEffectiveFinYearToDate() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        if (month < 3) {
            cal.set(Calendar.MONTH, 2);
        } else {
            cal.set(Calendar.MONTH, 2);
            cal.add(Calendar.YEAR, 1);
        }
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        return cal.getTime();
    }
}
