package org.egov.domain.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.commons.lang.StringUtils.isEmpty;

public class SevaRequest {

    private static final String PHONE = "phone";
    private static final String EMAIL = "email";
    private static final String CRN = "serviceRequestId";
    private static final String SERVICE_NAME = "serviceName";
    private static final String SERVICE_CODE = "serviceCode";
    private static final String VALUES_STATUS = "status";
    private static final String VALUES = "values";
    private static final String DESCRIPTION = "description";
    private static final String TENANT_ID = "tenantId";
    private static final String FIRST_NAME = "firstName";
    private static final String LOCATION_NAME = "locationName";
    private static final String REQUESTED_DATE = "requestedDatetime";
    private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm";
    private static final String SERVICE_REQUEST = "ServiceRequest";
    private static final String REQUEST_INFO = "RequestInfo";
    private static final String ACTION = "action";
    private static final String ATTRIBUTE_VALUES = "attribValues";
    private static final String ATTRIBUTE_VALUES_KEY_FIELD = "key";
    private static final String ATTRIBUTE_VALUES_NAME_FIELD = "name";
    private static final String ATTRIBUTE_VALUES_POPULATED_FLAG = "isAttribValuesPopulated";
    private static final String POST = "POST";
    private final HashMap<String, Object> serviceRequest;
    private final HashMap<String, Object> sevaRequest;

    @SuppressWarnings("unchecked")
    public SevaRequest(HashMap<String, Object> sevaRequestMap) {
        this.sevaRequest = sevaRequestMap;
        this.serviceRequest = (HashMap<String, Object>) sevaRequestMap.get(SERVICE_REQUEST);
    }

    public String getMobileNumber() {
        return (String) this.serviceRequest.get(PHONE);
    }

    public String getRequesterEmail() {
        return (String) this.serviceRequest.get(EMAIL);
    }

    public boolean isRequesterEmailAbsent() {
        return isEmpty(getRequesterEmail());
    }

    public String getServiceTypeName() {
        return (String) this.serviceRequest.get(SERVICE_NAME);
    }

    public String getServiceTypeCode() {
        return (String) this.serviceRequest.get(SERVICE_CODE);
    }

    public String getTenantId() {
        return (String) this.sevaRequest.get(TENANT_ID);
    }

    public String getCrn() {
        return (String) this.serviceRequest.get(CRN);
    }

    public String getStatusName() {
        return getDynamicSingleValue(VALUES_STATUS);
    }

    public String getDetails() {
        return (String) this.serviceRequest.get(DESCRIPTION);
    }

    public String getLocationName() {
        return getDynamicSingleValue(LOCATION_NAME);
    }

    public String getRequesterName() {
        return (String) this.serviceRequest.get(FIRST_NAME);
    }

    public String getFormattedCreatedDate() {
        return new SimpleDateFormat(DATE_FORMAT).format(getCreatedDate());
    }

    public boolean isCreate() {
       return POST.equals(getRequestInfo().get(ACTION));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getRequestInfo() {
        return (HashMap<String, Object>) this.sevaRequest.get(REQUEST_INFO);
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, String> getValues() {
        return (HashMap<String, String>) this.serviceRequest.get(VALUES);
    }

    private Date getCreatedDate() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            return dateFormat.parse((String) this.serviceRequest.get(REQUESTED_DATE));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isAttributeValuesPopulated() {
        return (boolean) this.serviceRequest.get(ATTRIBUTE_VALUES_POPULATED_FLAG);
    }

    @SuppressWarnings("unchecked")
    private List<HashMap<String, String>> getAttributeValues() {
        final List<HashMap<String, String>> attributeValues =
            (List<HashMap<String, String>>) this.serviceRequest.get(ATTRIBUTE_VALUES);
        return attributeValues == null ? Collections.emptyList() : attributeValues;
    }

    private String getDynamicSingleValue(String key) {
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
