package org.egov.tradelicense.notification.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TimeStampUtil {

	public static String getDateFromTimeStamp(Long epoch) {

		Timestamp timestamp = new Timestamp(epoch);
		DateFormat formatter = null;
		String dateObj = null;
		try {

			formatter = new SimpleDateFormat("dd MMM, yyyy");
			dateObj = formatter.format(timestamp);

		} catch (Exception e) {

			throw new RuntimeException();

		}

		return dateObj;
	}

}