package org.egov.pgrrest.write.consumer.contracts.request;

import org.egov.pgr.common.date.DateFormatter;

import java.util.*;

public class ServiceRequest {

    static final String LOCATION_ID = "locationId";
    static final String CHILD_LOCATION_ID = "childLocationId";
    static final String VALUES_POSITION_ID = "systemPositionId";
    static final String VALUES_CITIZEN_USER_ID = "systemCitizenUserId";
    static final String VALUES_STATE_ID = "systemStateId";
    static final String VALUES_STATUS = "systemStatus";
    static final String VALUES_DEPARTMENT = "systemDepartmentId";
    static final String VALUES_RECEIVING_MODE = "systemReceivingMode";
    static final String VALUES_RECEIVING_CENTER = "systemReceivingCenter";
    static final String VALUES_COMPLAINANT_ADDRESS = "systemRequesterAddress";
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
    private static final String REQUESTED_DATETIME = "requestedDatetime";
    private static final String UPDATED_DATETIME = "updatedDatetime";
    private static final String ATTRIBUTE_VALUES = "attribValues";
    private static final String ATTRIBUTE_VALUES_KEY_FIELD = "key";
    private static final String ATTRIBUTE_VALUES_NAME_FIELD = "name";

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
        return DateFormatter.toDate((String) this.serviceRequestMap.get(EXPECTED_DATETIME));
    }

    @SuppressWarnings("unchecked")
    public List<HashMap<String, String>> getAttributeValues() {
        final List<HashMap<String, String>> attributeValues =
            (List<HashMap<String, String>>) this.serviceRequestMap.get(ATTRIBUTE_VALUES);
        return attributeValues == null ? Collections.emptyList() : attributeValues;
    }

    public String getDynamicSingleValue(String key) {
        return getAttributeValues().stream()
            .filter(attribute -> key.equals(attribute.get(ATTRIBUTE_VALUES_KEY_FIELD)))
            .findFirst()
            .map(attribute -> attribute.get(ATTRIBUTE_VALUES_NAME_FIELD))
            .orElse(null);
    }

    public Date getCreatedDate() {
        return DateFormatter.toDate((String) this.serviceRequestMap.get(REQUESTED_DATETIME));
    }

    public Date getLastModifiedDate() {
        return DateFormatter.toDate((String) this.serviceRequestMap.get(UPDATED_DATETIME));

    }
}