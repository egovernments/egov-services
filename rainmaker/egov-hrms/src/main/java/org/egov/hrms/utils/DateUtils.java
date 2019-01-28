package org.egov.hrms.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
	public static boolean areDatesEqualWithoutTimePart(Date date1, Date date2) {
		
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date1);
		c2.setTime(date2);
		//return (c1.equals(c2));
		
		int yearDiff = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
		int monthDiff = c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
		int dayDiff = c1.get(Calendar.DAY_OF_MONTH) - c2.get(Calendar.DAY_OF_MONTH);
		return (yearDiff + monthDiff + dayDiff == 0);
	}

}
