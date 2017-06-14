package org.egov.commons.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = { "classpath:messages/messages.properties",
        "classpath:messages/errors.properties" }, ignoreResourceNotFound = true)
@Order(0)
public class CollectionConstants {
	 @Autowired
     private Environment environment;
  
	 public static final String INVALID_CATEGORY_REQUEST_MESSAGE = "Category is invalid";
	 
	  public static final String CATEGORY_NAME_MANDATORY_CODE = "collection.0001";
	  public static final String CATEGORY_NAME_MANADATORY_FIELD_NAME = "name";
	  public static final String CATEGORY_NAME_MANADATORY_ERROR_MESSAGE = "Category Name is required";
	  
	  public static final String CATEGORY_CODE_MANDATORY_CODE = "collection.0002";
	  public static final String CATEGORY_CODE_MANADATORY_FIELD_NAME = "code";
	  public static final String CATEGORY_CODE_MANADATORY_ERROR_MESSAGE = "Category Code is required";
	  
	  public static final String TENANT_MANDATORY_CODE = "collection.0003";
	  public static final String TENANT_MANADATORY_FIELD_NAME = "tenantId";
	  public static final String TENANT_MANADATORY_ERROR_MESSAGE = "Tenant Id is required";
	  
	  public static final String CATEGORY_CODE_UNIQUE_CODE = "collection.0004";
	  public static final String CATEGORY_CODE_UNIQUE_FIELD_NAME = "code";
	  public static final String CATEGORY_CODE_UNIQUE_ERROR_MESSAGE = "Entered Category Code already exist";
	  
	  public static final String CATEGORY_NAME_UNIQUE_CODE = "collection.0005";
	  public static final String CATEGORY_NAME_UNIQUE_FIELD_NAME = "name";
	  public static final String CATEGORY_NAME_UNIQUE_ERROR_MESSAGE = "Entered Category Name already exist";
	 
	   public String getErrorMessage(final String property) {
	        return environment.getProperty(property);
	    } 

}

