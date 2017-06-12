package org.egov.domain.model;

import org.apache.commons.lang3.StringUtils;
import org.egov.pgr.common.date.DateFormatter;

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
    private static final String DESCRIPTION = "description";
    private static final String TENANT_ID = "tenantId";
    private static final String FIRST_NAME = "firstName";
    private static final String LOCATION_NAME = "locationName";
    private static final String REQUESTED_DATE = "requestedDatetime";
    private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm";
    private static final String SERVICE_REQUEST = "serviceRequest";
    private static final String REQUEST_INFO = "RequestInfo";
    private static final String ACTION = "action";
    private static final String ATTRIBUTE_VALUES = "attribValues";
    private static final String ATTRIBUTE_VALUES_KEY_FIELD = "key";
    private static final String ATTRIBUTE_VALUES_NAME_FIELD = "name";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String PROCESSING_FEE = "PROCESSINGFEE";
    private static final String EMPLOYEE_TYPE = "EMPLOYEE";
    private static final String USER_TYPE_KEY = "type";
    private static final String USER_INFO_KEY = "userInfo";
    private static final String IN_PROGRESS_STATUS = "IN PROGRESS";
    private static final String VALUES_ASSIGNEE_ID = "assignmentId";

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
        return (String) this.serviceRequest.get(TENANT_ID);
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

    public boolean isUpdate() {
        return PUT.equals(getRequestInfo().get(ACTION));
    }

    public String getProcessingFee() {
        return getDynamicSingleValue(PROCESSING_FEE);
    }

    public boolean isProcessingFeePresent() {
        return StringUtils.isNotEmpty(getProcessingFee());
    }

    public boolean isInProgress() {
        return IN_PROGRESS_STATUS.equalsIgnoreCase(getStatusName());
    }

    @SuppressWarnings("unchecked")
    public boolean isEmployeeLoggedIn() {
        final Map<String, Object> requestInfo = getRequestInfo();
        final Map<String, Object> userInfo = (HashMap<String, Object>) requestInfo
            .getOrDefault(USER_INFO_KEY, new HashMap<String, Object>());
        return EMPLOYEE_TYPE.equals(userInfo.get(USER_TYPE_KEY));
    }

    public Long getAssigneeId() {
        final String assigneeId = getDynamicSingleValue(VALUES_ASSIGNEE_ID);
        return Long.valueOf(assigneeId);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getRequestInfo() {
        return (HashMap<String, Object>) this.sevaRequest.get(REQUEST_INFO);
    }

    private Date getCreatedDate() {
        return DateFormatter.toDate((String) this.serviceRequest.get(REQUESTED_DATE));
    }

    @SuppressWarnings("unchecked")
    private List<HashMap<String, String>> getAttributeValues() {
        final List<HashMap<String, String>> attributeValues =
            (List<HashMap<String, String>>) this.serviceRequest.get(ATTRIBUTE_VALUES);
        return attributeValues == null ? Collections.emptyList() : attributeValues;
    }

    private String getDynamicSingleValue(String key) {
        return getAttributeValues().stream()
            .filter(attribute -> key.equals(attribute.get(ATTRIBUTE_VALUES_KEY_FIELD)))
            .findFirst()
            .map(attribute -> attribute.get(ATTRIBUTE_VALUES_NAME_FIELD))
            .orElse(null);
    }

}
