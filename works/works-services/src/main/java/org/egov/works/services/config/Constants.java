package org.egov.works.services.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Constants {

    public static final String APPROPRIATION_NUMBER_GENERATION_ERROR = "Appropriation numebr can not be generated";

    public static final String WORKS_MODULE_CODE = "Works";

    public static final String KEY_FILESTORE_INVALID = "INVALID_FILESTORE";
    public static final String MESSAGE_FILESTORE_INVALID = "Plese provide valid value for filestore which exists in the system";

    public static final String KEY_TENANTID_INVALID = "INVALID_TENANTID";
    public static final String MESSAGE_TENANTID_INVALID = "Plese provide valid value for tenantid";

    public static final String KEY_OBJECTTYPE_INVALID = "INVALID_OBJECTTYPE";
    public static final String MESSAGE_OBJECTTYPE_INVALID = "Object Type is mandatory";

}
