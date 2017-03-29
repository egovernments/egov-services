package org.egov.pgr.contracts.grievance;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ServiceRequest {

	public static final String USER_ID = "userId";
	public static final String LOCATION_ID = "locationId";
	public static final String CHILD_LOCATION_ID = "child_location_id";
	public static final String LOCATION_NAME = "location_name";
	public static final String VALUES_ASSIGNEE_ID = "assignment_id";
	public static final String VALUES_STATE_ID = "stateId";
	public static final String VALUES_STATUS = "status";
	public static final String VALUES_RECIEVING_MODE = "receivingMode";
	public static final String VALUES_RECEIVING_CENTER = "receivingCenter";
	public static final String VALUES_COMPLAINANT_ADDRESS = "complainantAddress";
	private static final String CRN = "service_request_id";
	private static final String TENANT_ID = "tenantId";
	private static final String DESCRIPTION = "description";
	private static final String ADDRESS = "address";
	private static final String LAT = "lat";
	private static final String LNG = "lng";
	private static final String FIRST_NAME = "first_name";
	private static final String PHONE = "phone";
	private static final String EMAIL = "email";
	private static final String SERVICE_CODE = "service_code";
	private final static String REQUEST_INFO = "RequestInfo";
    private static final String EXPECTED_DATETIME = "expected_datetime";

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

	public Date getEscalationDate() {
        return new Date((Long) this.serviceRequestMap.get(EXPECTED_DATETIME));
    }

    @SuppressWarnings("unchecked")
	public HashMap<String, String> getValues() {
		return (HashMap<String, String>) this.serviceRequestMap.get("values");
	}

}