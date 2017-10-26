package org.egov.inv.domain.service.validator;


import org.egov.common.contract.response.ErrorField;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Service
public class CommonMasterValidator {

    private static final String CODE_LENGTH_CODE = "inv.004";

    private static final String NAME_MANDATORY_CODE = "inv.005";

    private static final String PATTERN_CODE = "inv.005";


    public void addNotNullValidationErrors(String fieldName, String fieldValue, List<ErrorField> errorFields) {

        if (!isEmpty(fieldValue)) {
            return;
        }
        final ErrorField errorField = ErrorField.builder()
                .code(NAME_MANDATORY_CODE)
                .message(fieldName + " is not resent")
                .field(fieldName)
                .build();

        errorFields.add(errorField);
    }

    public void addLengthValidationError(String fieldName, String fieldValue, Integer minimum, Integer maximum, List<ErrorField> errorFields) {
        if (fieldValue.length() >= minimum && fieldValue.length() <= maximum) {
            return;
        }
        final ErrorField errorField = ErrorField.builder()
                .code(CODE_LENGTH_CODE)
                .message(fieldName + " must be between " + maximum + " and " + maximum + " characters")
                .field(fieldName)
                .build();


        errorFields.add(errorField);
    }

    public void validatePattern(String fieldName, String fieldValue, String patternValue, List<ErrorField> errorFields) {
        Pattern pattern = Pattern.compile(patternValue);
        Matcher matcher = pattern.matcher(fieldValue);
        if (!matcher.matches()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(PATTERN_CODE)
                    .message(fieldName + " pattern is invalid")
                    .field(fieldName)
                    .build();
            errorFields.add(errorField);
        }
    }

}
