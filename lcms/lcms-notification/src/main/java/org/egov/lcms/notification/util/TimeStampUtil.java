package org.egov.lcms.notification.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStampUtil {
	public static String getDateWithoutTimezone(Long val) {
		
		String dateText = null;
		try {
			Date date = new Date(val);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			dateText = sdf.format(date);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateText;		
	}
}
