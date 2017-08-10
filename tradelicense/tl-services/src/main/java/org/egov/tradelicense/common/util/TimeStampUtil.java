package org.egov.tradelicense.common.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeStampUtil {

	/**
	 * this method will generate the timestamp
	 * 
	 * @param date
	 * @return Timestamp
	 * @exception RuntimeException
	 */
	public static Timestamp getTimeStamp(String date) {

		DateFormat formatter = null;
		Date dateObj = null;
		try {

			formatter = new SimpleDateFormat("dd/MM/yyyy");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			dateObj = formatter.parse(date);

		} catch (Exception e) {

			throw new RuntimeException();

		}

		return new java.sql.Timestamp(dateObj.getTime());
	}
	
	public static String getDateFromTimeStamp(Timestamp timestamp) {

		DateFormat formatter = null;
		String dateObj = null;
		try {

			formatter = new SimpleDateFormat("dd/MM/yyyy");
			formatter.setTimeZone(TimeZone.getTimeZone("ITC"));
			dateObj = formatter.format(timestamp);

		} catch (Exception e) {

			throw new RuntimeException();

		}

		return dateObj;
	}
}
