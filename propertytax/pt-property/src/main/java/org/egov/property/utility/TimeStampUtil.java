package org.egov.property.utility;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TimeStampUtil {

	public static Timestamp getTimeStamp(String date) {
		Timestamp timestamp = null;
		if (date == null) {
			return null;
		} else {
			DateTimeFormatter[] formatter = new DateTimeFormatter[] { DateTimeFormatter.ofPattern("dd/MM/yyyy"),
					DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"),
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

	public static String getDateFormat(String date) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat connvertFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return connvertFormat.format(simpleDateFormat.parse(date));

	}

	public static int getYear(String date) {
		int year = 0;
		if (date == null) {
			return 0;
		} else {
			DateTimeFormatter[] formatter = new DateTimeFormatter[] { DateTimeFormatter.ofPattern("dd/MM/yyyy"),
					DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"),
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
					DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.S") };
			for (int i = 0; i < formatter.length; i++) {
				try {
					LocalDateTime time = LocalDateTime.from(LocalDate.parse(date, formatter[i]).atStartOfDay());
					year = time.getYear();
				} catch (Exception ex) {
					continue;
				}

			}
		}

		return year;
	}
	
public static Boolean checkValidPeriod(String fromDate, String toDate) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate startDate = LocalDate.parse(fromDate, formatter);
		LocalDate endDate = LocalDate.parse(toDate, formatter);
		long numberOfmonths = ChronoUnit.MONTHS.between(startDate, endDate);
		if (numberOfmonths>3)
			return true;
		else 
			return false;
	}
	
	public static Boolean compareDates(String fromDate, String toDate) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date1 = sdf.parse(fromDate);
		Date date2 = sdf.parse(toDate);
		// after() will return true if and only if date1 is after date 2
		if (date1.after(date2))
			return false;
		else
			return true;
	}
	
	public static Boolean ValidDates(String fromDate, String toDate) throws Exception {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");		
		String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
		Date date1 = sdf.parse(fromDate);
		Date date2 = sdf.parse(toDate);
		Date date = sdf.parse(currentDate);
		
		if ((date1.equals(date) || date1.after(date)) && (date2.equals(date) || date2.after(date)))
			return true;
		else
			return false;
	}
}
