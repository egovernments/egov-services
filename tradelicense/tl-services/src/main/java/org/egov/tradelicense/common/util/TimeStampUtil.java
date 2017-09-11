package org.egov.tradelicense.common.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
	public static Timestamp getTimeStampFromDB(String date) {
		Timestamp timestamp = null;
		if (date == null) {
			return null;
		} else {
			DateTimeFormatter[] formatter = new DateTimeFormatter[] { DateTimeFormatter.ofPattern("dd/MM/yyyy"),
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
					DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.S") };
			for (int i = 0; i < formatter.length; i++) {
				try {
					LocalDateTime time = LocalDateTime.from(LocalDate.parse(date, formatter[i]).atStartOfDay());
					timestamp = Timestamp.valueOf(time);
					return timestamp;
				} catch (Exception ex) {
					continue;
				}

			}
		}

		return timestamp;
	}

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

	/**
	 * Description : This method to generate current year date in given format
	 * 
	 * @param dateFormat
	 * @return formattedDate
	 */
	public static String generateCurrentDate() {
		try {

			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String formattedDate = formatter.format(date);
			return formattedDate;

		} catch (Exception e) {

			throw new RuntimeException();

		}
	}
}
