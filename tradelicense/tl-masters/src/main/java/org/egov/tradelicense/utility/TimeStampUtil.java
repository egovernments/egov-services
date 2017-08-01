package org.egov.tradelicense.utility;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeStampUtil {

	public static Timestamp getTimeStamp(String date) {
		Timestamp timestamp = null;
		if (date == null) {
			return null;
		} else {
			DateTimeFormatter[] formatter = new DateTimeFormatter[] { DateTimeFormatter.ofPattern("dd/MM/yyyy"),
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") };
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
}
