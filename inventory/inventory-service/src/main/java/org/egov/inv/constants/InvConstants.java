package org.egov.inv.constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = {"classpath:messages/messages.properties",
        "classpath:messages/errors.properties"}, ignoreResourceNotFound = true)
@Order(0)
public class InvConstants {

    @Autowired
    private Environment environment;

    public static final String INVALID_STORES_REQUEST_MESSAGE = "Store Request is invalid";
    public static final String PATTERN_CHARACTER = "^[A-Za-z]+$";
    public static final String PATTERN_ALPHANUMERIC = "^[A-Za-z0-9]+$";
    public static final String PATTERN_ALPHANUMERIC_SPACE = "^[A-Za-z0-9 ]+$";


    public static final String TENANTID_MANDATORY_CODE = "inv.0001";

    public static final String STORE_CODE_MANDATORY_CODE = "inv.0002";

    public static final String STORE_NAME_MANDATORY_CODE = "inv.0003";

    public static final String STORE_DELIVERYADDRESS_MANDATORY_CODE = "inv.0004";

    public static final String STORE_BILLINGADDRESS_MANDATORY_CODE = "inv.0005";

    public static final String STORE_DESCRIPTION_MANDATORY_CODE = "inv.0006";

    public static final String STORE_CONTACTNO1_MANDATORY_CODE = "inv.0007";

    public static final String STORE_EMAIL_MANDATORY_CODE = "inv.0008";

    public static final String STORE_DEPARTMENT_MANDATORY_CODE = "inv.0009";

    public static final String STORE_DEPARTMENT_DETAILS_MANDATORY_CODE = "inv.0010";

    public static final String STORE_CODE_UNIQUE_CODE = "inv.0011";

    public static final String STORE_STOREINCHARGE_MANDATORY_CODE = "inv.0012";

    public static final String STORE_STOREINCHARGE_DETAILS_MANDATORY_CODE = "inv.0013";
    

    public static final String PATTERN_NUMBERS = "^[0-9]{10}$";



    public static final String PATTERN_EMAIL = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1," +
            "3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
    
    public static final String PATTERN_ALPHANUMERIC_CODE = "inv.0014";

    public static final String PATTERN_NUMBERS_CODE = "inv.0015";

    public static final String PATTERN_ALPHABETS_CODE = "inv.0016";

    public static final String PATTERN_ALPHANUMERIC_WITH_SPACE_CODE = "inv.0017";

    public static final String PATTERN_EMAIL_CODE = "inv.0018";


    

    public String getErrorMessage(final String property) {
        return environment.getProperty(property);
    }

}
