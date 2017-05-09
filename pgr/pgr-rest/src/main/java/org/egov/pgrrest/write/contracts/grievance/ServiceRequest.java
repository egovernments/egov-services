package org.egov.pgrrest.write.contracts.grievance;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceRequest {

	static final String LOCATION_ID = "locationId";
	static final String CHILD_LOCATION_ID = "childLocationId";
	static final String VALUES_ASSIGNEE_ID = "assignmentId";
	static final String VALUES_STATE_ID = "stateId";
	static final String VALUES_STATUS = "status";
	static final String VALUES_DEPARTMENT = "departmentId";
	static final String VALUES_RECEIVING_MODE = "receivingMode";
	static final String VALUES_RECEIVING_CENTER = "receivingCenter";
	static final String VALUES_COMPLAINANT_ADDRESS = "complainantAddress";
	private static final String CRN = "serviceRequestId";
	private static final String TENANT_ID = "tenantId";
	private static final String DESCRIPTION = "description";
	private static final String ADDRESS = "address";
	private static final String LAT = "lat";
	private static final String LNG = "lng";
	private static final String FIRST_NAME = "firstName";
	private static final String PHONE = "phone";
	private static final String EMAIL = "email";
	private static final String SERVICE_CODE = "serviceCode";
    private static final String EXPECTED_DATETIME = "expectedDatetime";
    private static final String ATTRIBUTE_VALUES = "attribValues";
    private static final String ATTRIBUTE_VALUES_KEY_FIELD = "key";
    private static final String ATTRIBUTE_VALUES_NAME_FIELD = "name";
    private static final String ATTRIBUTE_VALUES_POPULATED_FLAG = "isAttribValuesPopulated";

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
    private HashMap<String, String> getValues() {
		return (HashMap<String, String>) this.serviceRequestMap.get("values");
	}

    private boolean isAttributeValuesPopulated() {
        return (boolean) this.serviceRequestMap.get(ATTRIBUTE_VALUES_POPULATED_FLAG);
    }

    @SuppressWarnings("unchecked")
    private List<HashMap<String, String>> getAttributeValues() {
        return (List<HashMap<String, String>>) this.serviceRequestMap.get(ATTRIBUTE_VALUES);
    }

    public String getDynamicSingleValue(String key) {
        if (isAttributeValuesPopulated()) {
            return getAttributeValues().stream()
                .filter(attribute -> key.equals(attribute.get(ATTRIBUTE_VALUES_KEY_FIELD)))
                .findFirst()
                .map(attribute -> attribute.get(ATTRIBUTE_VALUES_NAME_FIELD))
                .orElse(null);
        } else {
            return getValues().get(key);
        }
    }

}