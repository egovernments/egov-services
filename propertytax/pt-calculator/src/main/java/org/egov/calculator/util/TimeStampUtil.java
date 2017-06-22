package org.egov.calculator.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStampUtil {
	public static Timestamp getTimeStamp(String date) {

		DateFormat formatter = null;
		Date dateObj = null;
		try {

			formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			dateObj = formatter.parse(date);

		} catch (Exception e) {

			throw new RuntimeException();

		}

		return new java.sql.Timestamp(dateObj.getTime());
	}
}
