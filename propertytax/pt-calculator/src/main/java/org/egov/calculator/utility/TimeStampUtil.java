package org.egov.calculator.utility;

/**
 * 
 * @author Pavan Kumar Kamma
 * 
 * This Class contains the utility method to generate timestamp 
 */
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

			formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			dateObj = formatter.parse(date);

		} catch (Exception e) {

			throw new RuntimeException();

		}

		return new java.sql.Timestamp(dateObj.getTime());
	}
}
