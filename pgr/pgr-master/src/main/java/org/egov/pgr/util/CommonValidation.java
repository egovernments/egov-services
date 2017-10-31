package org.egov.pgr.util;


import org.egov.pgr.domain.exception.PGRMasterException;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hibernate.internal.util.StringHelper.isEmpty;

@Configuration
public class CommonValidation {

    public static final String CODE = "code";
    public static final String FIELD = "field";
    public static final String MESSAGE = "message";

    HashMap<String, String> error = new HashMap<>();

    public void validateCode(String code) {
        Pattern pattern = Pattern.compile("^[A-Z0-9]+$");
        Matcher matcher = pattern.matcher(code);
        if (!matcher.matches()) {
            error.put(CODE, "pgr.0063");
            error.put(FIELD, "code");
            error.put(MESSAGE, "Code pattern is invalid");
            throw new PGRMasterException(error);
        }
    }

    public void validateName(String name) {
        Pattern pattern = Pattern.compile("[a-zA-Z '_.]+");
        Matcher matcher = pattern.matcher(name);
        if (!matcher.matches()) {
            error.put(CODE, "pgr.0064");
            error.put(FIELD, "name");
            error.put(MESSAGE, "Name pattern is invalid");
            throw new PGRMasterException(error);
        }
    }

    public void validateCodeLength(String code) {
        if (!isEmpty(code)&& code.length() > 100) {
            error.put(CODE, "pgr.0065");
            error.put(FIELD, "code");
            error.put(MESSAGE, "Code must be below 20 characters");
            throw new PGRMasterException(error);
        }
    }

    public void validateNameLength(String name) {
        if (!isEmpty(name) && name.length() > 150) {
            error.put(CODE, "pgr.0066");
            error.put(FIELD, "name");
            error.put(MESSAGE, "Name must be below 100 characters");
            throw new PGRMasterException(error);
        }
    }

    public void validateDescriptionLength(String description) {
        if (!isEmpty(description) && description.length() > 250) {
            error.put(CODE, "pgr.0067");
            error.put(FIELD, "description");
            error.put(MESSAGE, "Description must be below 250 characters");
            throw new PGRMasterException(error);
        }
    }

}
