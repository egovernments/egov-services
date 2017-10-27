package org.egov.inv.domain.service.validator;


import org.egov.common.contract.response.ErrorField;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class CommonMasterValidator {
    
   

    private static final String CODE_LENGTH_CODE = "inv.004";

    private static final String NAME_MANDATORY_CODE = "inv.005";

    private static final String PATTERN_CODE = "inv.005";


    public void addNotNullForObjectValidationErrors(String fieldName, Object fieldValue, List<ErrorField> errorFields, String code) {

        if (null != fieldValue) {
            return;
        }
        final ErrorField errorField = ErrorField.builder()
                .code(code)
                .message(fieldName + " is required")
                .field(fieldName)
                .build();

        errorFields.add(errorField);
    }

    public void addNotNullForStringValidationErrors(String fieldName, String fieldValue, List<ErrorField> errorFields, String code) {

        if (isNotBlank(fieldValue)) {
            return;
        }
        final ErrorField errorField = ErrorField.builder()
                .code(code)
                .message(fieldName + " is required")
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
                .message(fieldName + " must be between " + minimum + " and " + maximum + " characters")
                .field(fieldName)
                .build();


        errorFields.add(errorField);
    }

    public void validatePattern(String fieldName, String fieldValue, String patternValue, List<ErrorField> errorFields,String patternCode) {
        Pattern pattern = Pattern.compile(patternValue);
        Matcher matcher = pattern.matcher(fieldValue);
        if (!matcher.matches()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(patternCode)
                    .message(fieldName + " pattern is invalid")
                    .field(fieldName)
                    .build();
            errorFields.add(errorField);
        }
    }

 
        
    


}
