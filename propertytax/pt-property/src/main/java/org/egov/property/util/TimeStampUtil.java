package org.egov.property.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeStampUtil {

	public static Timestamp getTimeStamp(String date) {
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime time = LocalDateTime.from(LocalDate.parse(date, formatter).atStartOfDay());
		Timestamp timestamp = Timestamp.valueOf(time);
		return timestamp;

	}
}
