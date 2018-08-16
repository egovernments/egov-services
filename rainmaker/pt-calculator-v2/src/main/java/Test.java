import java.util.Calendar;

public class Test {

	public static void main(String[] args) {
		
		long val = System.currentTimeMillis();
		Calendar cal = Calendar.getInstance();
		cal.set(2017, 00, 01);
		
		System.err.println(cal.getTimeInMillis());
		
		long old = 1483284106119l;
		
		long newd = 1533914523686l;
		
		System.err.println(" the diff : " + Double.valueOf(newd- old)/3600000/24);
	}
}
