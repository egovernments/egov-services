package org.egov.pgr.contracts.grievance;

import java.util.HashMap;
import java.util.Map;

public class ServiceRequest {

    public static final String USER_ID = "user_id";
    public static final String LOCATION_ID = "locationId";
    public static final String CHILD_LOCATION_ID = "child_location_id";
    public static final String LOCATION_NAME = "location_name";
    public static final String VALUES_ASSIGNEE_ID = "assignment_id";
    public static final String VALUES_STATE_ID = "stateId";
    public static final String VALUES_STATUS = "status";
    public static final String VALUES_RECIEVING_MODE = "receivingMode";
    public static final String VALUES_COMPLAINANT_ADDRESS = "complainantAddress";
    public static final String CRN = "service_request_id";
    public static final String TENANT_ID = "tenantId";
    public static final String DESCRIPTION = "description";
    public static final String ADDRESS = "address";
    public static final String LAT = "lat";
    public static final String LNG = "lng";
    public static final String FIRST_NAME = "first_name";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    public static final String SERVICE_CODE = "service_code";
    private Map<String, Object> serviceRequestMap;

    public ServiceRequest(Map<String, Object> serviceRequestMap) {
        this.serviceRequestMap = serviceRequestMap;
    }

    public String getCrn() {
        return (String) this.serviceRequestMap.get(CRN);
    }

    public String getTenantId() {
        return (String) this.serviceRequestMap.get(TENANT_ID);
    }

    public String getComplaintTypeCode() {
        return (String) this.serviceRequestMap.get(SERVICE_CODE);
    }

    public String getDetails() {
        return (String) this.serviceRequestMap.get(DESCRIPTION);
    }

    public String getLandmarkDetails() {
        return (String) this.serviceRequestMap.get(ADDRESS);
    }

    public Double getLat() {
        return (Double) this.serviceRequestMap.get(LAT);
    }

    public Double getLng() {
        return (Double) this.serviceRequestMap.get(LNG);
    }

    public String getFirstName() {
        return (String) this.serviceRequestMap.get(FIRST_NAME);
    }

    public String getPhone() {
        return (String) this.serviceRequestMap.get(PHONE);
    }

    public String getEmail() {
        return (String) this.serviceRequestMap.get(EMAIL);
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, String> getValues() {
        return (HashMap<String, String>) this.serviceRequestMap.get("values");
    }

}