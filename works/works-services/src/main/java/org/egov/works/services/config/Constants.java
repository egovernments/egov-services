package org.egov.works.services.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Constants {

    public static final String APPROPRIATION_NUMBER_GENERATION_ERROR = "Appropriation numebr can not be generated";

    public static final String WORKS_MODULE_CODE = "Works";
    public static final String WORKORDER="WorkOrderOffline";
    public static final String LETTEROFACCEPTANCE="LetterOfAcceptanceOffline";
    public static final String DETAILEDESTIMATE="DetailedEstimateOffline";
    public static final String LOA_OFFLINESTATUS_AGREEMENT_ORDER_SIGNED="AGREEMENT_ORDER_SIGNED";
    public static final String WORKORDER_OFFLINESTATUS_WORK_COMMENCED="WORK_COMMENCED";

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

    public static final String KEY_OFFLINE_STATUS_VALUE_INVALID = "OFFLINE_STATUS_VALUE_INVALID";
    public static final String MESSAGE_OFFLINE_STATUS_VALUE_INVALID = "Status value is invalid";
    
    public static final String KEY_OFFLINESTATUS_DE_NOT_EXIST = "works.offlinestatus.de.not.esist";
    public static final String MESSAGE_OFFLINESTATUS_DE_NOT_EXIST = "Detailed Estimate not exist for given Object Number";

    public static final String KEY_OFFLINESTATUS_WO_NOT_EXIST = "works.offlinestatus.wo.not.esist";
    public static final String MESSAGE_OFFLINESTATUS_WO_NOT_EXIST = "Work order not exist for given Object Number";
    
    public static final String KEY_OFFLINESTATUS_LOA_NOT_EXIST = "works.offlinestatus.loa.not.esist";
    public static final String MESSAGE_OFFLINESTATUS_LOA_NOT_EXIST = "Letter of acceptance not exist for given Object Number";



}
