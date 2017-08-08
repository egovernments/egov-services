package org.egov.tradelicense.util;

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
}
