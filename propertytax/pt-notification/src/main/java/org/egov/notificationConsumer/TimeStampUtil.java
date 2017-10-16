package org.egov.notificationConsumer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStampUtil {
	
	public static String getDateWithoutTimezone(String date) {
		String dateWithoutTimezone = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        Date date1 = sdf.parse(date);
	        dateWithoutTimezone = new SimpleDateFormat("dd/MM/yyyy").format(date1);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateWithoutTimezone;		
	}
}
