package org.egov.calculator.utility;

/**
 * 
 * @author Pavan Kumar Kamma
 * 
 * This Class contains the utility method to generate timestamp 
 */
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

	public static Boolean compareDates(String fromDate, String toDate) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date1 = sdf.parse(fromDate);
		Date date2 = sdf.parse(toDate);

		// after() will return true if and only if date1 is after date 2
		if (date1.after(date2)) {
			return false;
		}
		
		return true;
	}

}
