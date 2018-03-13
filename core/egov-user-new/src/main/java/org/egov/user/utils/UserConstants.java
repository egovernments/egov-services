package org.egov.user.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = { "classpath:messages/messages.properties",
		"classpath:messages/errors.properties" }, ignoreResourceNotFound = true)
@Order(0)
public class UserConstants {

	@Autowired
	private Environment environment;

	public String getErrorMessage(final String property) {
		return environment.getProperty(property);
	}

	public static final String TENANTID_MANDATORY_CODE = "user.0001";
	public static final String TENANTID_MANADATORY_FIELD_NAME = "tenantId";
	public static final String TENANTID_MANADATORY_ERROR_MESSAGE = "Tenant Id is required";

	public static final String USERNAME_MANDATORY_CODE = "user.0002";
	public static final String USERNAME_MANADATORY_FIELD_NAME = "userName";
	public static final String USERNAME_MANADATORY_ERROR_MESSAGE = "UserName is required";

	public static final String NAME_MANDATORY_CODE = "user.0003";
	public static final String NAME_MANADATORY_FIELD_NAME = "name";
	public static final String NAME_MANADATORY_ERROR_MESSAGE = "Name is required";

	public static final String GENDER_MANDATORY_CODE = "user.0004";
	public static final String GENDER_MANADATORY_FIELD_NAME = "gender";
	public static final String GENDER_MANADATORY_ERROR_MESSAGE = "Gender is required";

	public static final String MOBILENUMBER_MANDATORY_CODE = "user.0005";
	public static final String MOBILENUMBER_MANADATORY_FIELD_NAME = "mobileNumber";
	public static final String MOBILENUMBER_MANADATORY_ERROR_MESSAGE = "MobileNumber is required";

	public static final String ACTIVE_MANDATORY_CODE = "user.0006";
	public static final String ACTIVE_MANADATORY_FIELD_NAME = "active";
	public static final String ACTIVE_MANADATORY_ERROR_MESSAGE = "Active is required";

	public static final String LOCALE_MANDATORY_CODE = "user.0007";
	public static final String LOCALE_MANADATORY_FIELD_NAME = "locale";
	public static final String LOCALE_MANADATORY_ERROR_MESSAGE = "Locale is required";

	public static final String TYPE_MANDATORY_CODE = "user.0008";
	public static final String TYPE_MANADATORY_FIELD_NAME = "type";
	public static final String TYPE_MANADATORY_ERROR_MESSAGE = "Type is required";
	
	public static final String USERID_MANDATORY_CODE = "user.0009";
	public static final String USERID_MANADATORY_FIELD_NAME = "id";
	public static final String USERID_MANADATORY_ERROR_MESSAGE = "UserId is Requried";

	public static final String USERNAME_UNIQUE_CODE = "user.0010";
	public static final String USERNAME_UNQ_FIELD_NAME = "userName";
	public static final String USERNAME_UNQ_ERROR_MESSAGE = "UserName already exist.";

	public static final String USERNAME_DUPLICATE_CODE = "user.0011";
	public static final String USERNAME_DUPLICATE_FIELD_NAME = "userName";
	public static final String USERNAME_DUPLICATE_ERROR_MESSAGE = "UserName Does n't exist.";
	
	public static final String USERNAME_INVALIDFORMAT_CODE = "user.0012";
	public static final String USERNAME_INVALIDFORMAT_NAME = "userName";
	public static final String USERNAME_INVALIDFORMAT_ERROR_MESSAGE = "Since we configures userLoginPasswordOtpEnabled as true ,So UserName should be mobileNumber.";
	
	
	public static final String ADDRESS_INVALIDLENGTH_CODE = "user.0013";
	public static final String ADDRESSE_INVALIDFORMAT_NAME = "address";
	public static final String ADDRESS_INVALIDFORMAT_ERROR_MESSAGE = "Address Length should not be more than 300 characters.";
	
	public static final String CITY_INVALIDLENGTH_CODE = "user.0014";
	public static final String CITY_INVALIDFORMAT_NAME = "city";
	public static final String CITY_INVALIDFORMAT_ERROR_MESSAGE = "City Length should not be more than 300 characters.";
	
	public static final String PINCODE_INVALIDLENGTH_CODE = "user.0015";
	public static final String PINCODE_INVALIDFORMAT_NAME = "pinCode";
	public static final String PINCODE_INVALIDFORMAT_ERROR_MESSAGE = "pinCode Length should not be more than 10 characters.";
}
