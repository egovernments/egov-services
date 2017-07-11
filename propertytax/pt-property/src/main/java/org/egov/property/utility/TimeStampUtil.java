package org.egov.property.utility;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeStampUtil {

	public static Timestamp getTimeStamp(String date) {
		Timestamp timestamp;
		if (date == null) {
			return null;
		} else {
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDateTime time = LocalDateTime.from(LocalDate.parse(date, formatter).atStartOfDay());
			timestamp = Timestamp.valueOf(time);
		}

		return timestamp;
	}
}
