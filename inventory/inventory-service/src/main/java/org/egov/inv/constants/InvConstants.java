package org.egov.inv.constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = { "classpath:messages/messages.properties",
        "classpath:messages/errors.properties" }, ignoreResourceNotFound = true)
@Order(0)
public class InvConstants {

    @Autowired
    private Environment environment;

    public static final String INVALID_STORES_REQUEST_MESSAGE = "Store Request is invalid";

    public static final String TENANTID_MANDATORY_CODE = "inv.0001";
    public static final String TENANTID_MANADATORY_FIELD_NAME = "tenantId";
    public static final String TENANTID_MANADATORY_ERROR_MESSAGE = "tenantId is required";

    public static final String STORE_CODE_MANDATORY_CODE = "inv.0002";
    public static final String STORE_CODE_MANDATORY_FIELD_NAME = "code";
    public static final String STORE_CODE_MANDATORY_ERROR_MESSAGE = "Store code is required";

    public static final String STORE_NAME_MANDATORY_CODE = "inv.0003";
    public static final String STORE_NAME_MANADATORY_FIELD_NAME = "name";
    public static final String STORE_NAME_MANADATORY_ERROR_MESSAGE = "Store name is required";

    public static final String STORE_DELIVERYADDRESS_MANDATORY_CODE = "inv.0004";
    public static final String STORE_DELIVERYADDRESS_MANADATORY_FIELD_NAME = "deliveryAddress";
    public static final String STORE_DELIVERYADDRESS_MANADATORY_ERROR_MESSAGE = "Store Delivery Address is required";

    public static final String STORE_BILLINGADDRESS_MANDATORY_CODE = "inv.0005";
    public static final String STORE_BILLINGADDRESS_MANADATORY_FIELD_NAME = "billingAddress";
    public static final String STORE_BILLINGADDRESS_MANADATORY_ERROR_MESSAGE = "Store Billing Address is required";

    public static final String STORE_DESCRIPTION_MANDATORY_CODE = "inv.0006";
    public static final String STORE_DESCRIPTION_MANADATORY_FIELD_NAME = "description";
    public static final String STORE_DESCRIPTION_MANADATORY_ERROR_MESSAGE = "Store description is required";

    public static final String STORE_CONTACTNO1_MANDATORY_CODE = "inv.0007";
    public static final String STORE_CONTACTNO1_MANADATORY_FIELD_NAME = "contactNo1";
    public static final String STORE_CONTACTNO1_MANADATORY_ERROR_MESSAGE = "Store contactNo1 is required";

    public static final String STORE_EMAIL_MANDATORY_CODE = "inv.0008";
    public static final String STORE_EMAIL_MANADATORY_FIELD_NAME = "email";
    public static final String STORE_EMAIL_MANADATORY_ERROR_MESSAGE = "Store emailid is required";

    public static final String STORE_DEPARTMENT_MANDATORY_CODE = "inv.0009";
    public static final String STORE_DEPARTMENT_MANADATORY_FIELD_NAME = "department";
    public static final String STORE_DEPARTMENT_MANADATORY_ERROR_MESSAGE = "Department is required";

    public static final String STORE_DEPARTMENT_DETAILS_MANDATORY_CODE = "inv.0010";
    public static final String STORE_DEPARTMENT_DETAILS_MANADATORY_FIELD_NAME = "code";
    public static final String STORE_DEPARTMENT_DETAILS_MANADATORY_ERROR_MESSAGE = "Department details are required";

    public static final String STORE_CODE_UNIQUE_CODE = "inv.0011";
    public static final String STORE_CODE_UNIQUE_FIELD_NAME = "code";
    public static final String STORE_CODE_UNIQUE_ERROR_MESSAGE = "Store Code already exists";

    public static final String STORE_STOREINCHARGE_MANDATORY_CODE = "inv.0012";
    public static final String STORE_STOREINCHARGE_MANADATORY_FIELD_NAME = "storeIncharge";
    public static final String STORE_STOREINCHARGE_MANADATORY_ERROR_MESSAGE = "Store In-Charge is required";

    public static final String STORE_STOREINCHARGE_DETAILS_MANDATORY_CODE = "inv.0013";
    public static final String STORE_STOREINCHARGE_DETAILS_MANADATORY_FIELD_NAME = "code";
    public static final String STORE_STOREINCHARGE_DETAILS_MANADATORY_ERROR_MESSAGE = "Store In-Charge details are required";

    public String getErrorMessage(final String property) {
        return environment.getProperty(property);
    }

}
