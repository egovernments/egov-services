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
    
    public static final String KEY_OFFLINESTATUS_INVALID = "works.offlinestatus.invalid.data";
    public static final String MESSAGE_OFFLINESTATUS_INVALID = "Please pass valid offline status to modify";

    public static final String KEY_OFFLINESTATUS_INVALID_OBJECTTYPE = "works.offlinestatus.invalid.objecttype";
    public static final String MESSAGE_OFFLINESTATUS_INVALID_OBJECTTYPE = "Please pass valid object type for offline status to modify";

    public static final String KEY_OFFLINESTATUS_INVALID_OBJECTNUMBER = "works.offlinestatus.invalid.objectnumber";
    public static final String MESSAGE_OFFLINESTATUS_INVALID_OBJECTNUMBER = "Please pass valid object number for offline status to modify";
    
    public static final String KEY_OBJECTDATE_STATUSDATE_DE_INVALID = "works.offlinestatus.de.statusdate.invalid";
    public static final String MESSAGE_OBJECTDATE_STATUSDATE_DE_INVALID = "Status Date cannot be greater then Technical Sanctioned date of detailed estimate";


    public static final String KEY_OBJECTDATE_STATUSDATE_WO_INVALID = "works.offlinestatus.wo.statusdate.invalid";
    public static final String MESSAGE_OBJECTDATE_STATUSDATE_WO_INVALID = "Status Date cannot be greater then Approved date of work order";
    
    public static final String KEY_OBJECTDATE_STATUSDATE_LOA_INVALID = "works.offlinestatus.loa.statusdate.invalid";
    public static final String MESSAGE_OBJECTDATE_STATUSDATE_LOA_INVALID = "Status Date cannot be greater then Approved date of letter of acceptance";

}
