package org.egov.swm.utils;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class Utils {

    public Boolean isFutureDate(final Date date) {

        return !date.before(new Date());
    }

}
