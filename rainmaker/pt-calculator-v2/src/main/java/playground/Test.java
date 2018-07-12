package playground;

import java.util.Calendar;

public class Test {

	public static void main(String[] args) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(2018, 0, 6);
		System.err.println(" the calendar is : "+ calendar.getTime());
	}
}
