package org.gov.eis.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
	
	public static boolean areDatesEqualWithoutTimePart(Date date1, Date date2) {
		
		LocalDateTime time = LocalDateTime.ofInstant(
				date1.toInstant(), 
				ZoneId.systemDefault());
		
	//	System.out.printf("Day:%d,hour:%d,minute:%d,seconds:%d",time.getDayOfMonth(),time.getHour(),time.getMinute(),time.getSecond());
	//	System.ou
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date1);
		c2.setTime(date2);
//		System.out.println("date1:" + date1.getTime() + "c1:" +c1.getTime().getTime() +"c2:" +c2.getTime().getTime());
//		System.out.println("diff=" + (c2.getTime().getTime() - date1.getTime())/(1000 * 60 * 60));
		//return (c1.equals(c2));
		
		int yearDiff = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
		int monthDiff = c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
		int dayDiff = c1.get(Calendar.DAY_OF_MONTH) - c2.get(Calendar.DAY_OF_MONTH);
		System.out.println(yearDiff + " " + monthDiff + " " + dayDiff);
		return (yearDiff + monthDiff + dayDiff == 0);
	}

}
