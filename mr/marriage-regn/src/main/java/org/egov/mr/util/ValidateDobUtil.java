package org.egov.mr.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ValidateDobUtil {

	public int getAge(Long margDate,Long dob1)  {

		Calendar mrDate = Calendar.getInstance();
		mrDate.setTimeInMillis(margDate);
		
		Calendar dob = Calendar.getInstance();
		dob.setTimeInMillis(dob1);
		
		int curYear = mrDate.get(Calendar.YEAR);
        int dobYear = dob.get(Calendar.YEAR);

        int age = curYear - dobYear;

        // if dob is month or day is behind today's month or day
        // reduce age by 1
        int curMonth = mrDate.get(Calendar.MONTH);
        int dobMonth = dob.get(Calendar.MONTH);
        if (dobMonth > curMonth) { // this year can't be counted!
            age--;
        } else if (dobMonth == curMonth) { // same month? check for day
            int curDay = mrDate.get(Calendar.DAY_OF_MONTH);
            int dobDay = dob.get(Calendar.DAY_OF_MONTH);
            if (dobDay > curDay) { // this year can't be counted!
                age--;
            }
        }
        log.info("age is on marriage day is:"+age);
        return age;

		/*Date marriageDate = new Date(margDate);
		Date dateOfBirth = new Date(margDate);
		 System.err.println(":::::marriageDate::::::"+marriageDate);
		 System.err.println(":::::dateOfBirth::::::"+dateOfBirth);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",Locale.US);
        try {
        	marriageDate = sdf.parse(marriageDate.toString());
        	dateOfBirth = sdf.parse(dateOfBirth.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.err.println(":::::marriageDate::::::"+marriageDate);
        System.err.println(":::::dateOfBirth::::::"+dateOfBirth);
        System.err.println(":::::dateOfBirth.compareTo(dateOfBirth)::::::"+dateOfBirth.compareTo(dateOfBirth));*/
//        return getAge(dateOfBirth,marriageDate);
	}
	
	private int getAge(Calendar dob,Calendar mrDate) throws Exception {

        int curYear = mrDate.get(Calendar.YEAR);
        int dobYear = dob.get(Calendar.YEAR);

        int age = curYear - dobYear;

        // if dob is month or day is behind today's month or day
        // reduce age by 1
        int curMonth = mrDate.get(Calendar.MONTH);
        int dobMonth = dob.get(Calendar.MONTH);
        if (dobMonth > curMonth) { // this year can't be counted!
            age--;
        } else if (dobMonth == curMonth) { // same month? check for day
            int curDay = mrDate.get(Calendar.DAY_OF_MONTH);
            int dobDay = dob.get(Calendar.DAY_OF_MONTH);
            if (dobDay > curDay) { // this year can't be counted!
                age--;
            }
        }

        return age;
    }
}
