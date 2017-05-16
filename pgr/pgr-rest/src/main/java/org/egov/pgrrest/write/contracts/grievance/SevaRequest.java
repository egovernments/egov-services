package org.egov.pgrrest.write.contracts.grievance;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.pgrrest.common.model.AttributeEntry;
import org.egov.pgrrest.write.model.ComplaintRecord;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.egov.pgrrest.write.contracts.grievance.ServiceRequest.*;
import static org.hibernate.internal.util.StringHelper.isEmpty;

public class SevaRequest {

    private static final String SERVICE_REQUEST = "serviceRequest";
    private static final String REQUEST_INFO = "RequestInfo";
    private static final String CITIZEN = "CITIZEN";
    private static final String VALUES_CITIZENFEEDBACK = "citizenFeedback";
    public static final String ATTRIBUTE_ENTRY_KEY = "key";
    public static final String ATTRIBUTE_ENTRY_NAME = "name";

    private HashMap<String, Object> sevaRequestMap;
    private ObjectMapper objectMapper;

    public SevaRequest(HashMap<String, Object> sevaRequestMap) {
        this.sevaRequestMap = sevaRequestMap;
        this.objectMapper = new ObjectMapper();
    }

    public ComplaintRecord toDomain() {
        return ComplaintRecord.builder()
            .CRN(getCRN())
            .latitude(getLatitude())
            .latitude(getLongitude())
            .description(this.getServiceRequest().getDetails())
            .landmarkDetails(this.getServiceRequest().getLandmarkDetails())
            .createdBy(this.getRequesterId())
            .lastModifiedBy(this.getRequesterId())
            .complainantName(this.getServiceRequest().getFirstName())
            .complainantMobileNumber(this.getServiceRequest().getPhone())
            .complainantEmail(this.getServiceRequest().getEmail())
            .complainantAddress(this.getServiceRequest().getDynamicSingleValue(VALUES_COMPLAINANT_ADDRESS))
            .complainantUserId(getUserId())
            .receivingMode(this.getServiceRequest().getDynamicSingleValue(VALUES_RECEIVING_MODE))
            .receivingCenter(getReceivingCenter())
            .complaintTypeCode(this.getServiceRequest().getComplaintTypeCode())
            .complaintStatus(this.getServiceRequest().getDynamicSingleValue(VALUES_STATUS))
            .assigneeId(getAssigneeId())
            .location(getLocation())
            .childLocation(getChildLocation())
            .escalationDate(this.getServiceRequest().getEscalationDate())
            .workflowStateId(getStateId())
            .department(getDepartment())
            .citizenFeedback(getCitizenFeedback())
            .tenantId(this.getServiceRequest().getTenantId())
            .attributeEntries(getAttributeEntries())
            .build();
    }

    private List<AttributeEntry> getAttributeEntries() {
        return this.getServiceRequest().getAttributeValues().stream()
            .map(this::mapToAttributeEntry)
            .collect(Collectors.toList());
    }

    private AttributeEntry mapToAttributeEntry(HashMap<String, String> entry) {
        return new AttributeEntry(entry.get(ATTRIBUTE_ENTRY_KEY), entry.get(ATTRIBUTE_ENTRY_NAME));
    }

    @SuppressWarnings("unchecked")
    private ServiceRequest getServiceRequest() {
        return new ServiceRequest((HashMap<String, Object>) sevaRequestMap.get(SERVICE_REQUEST));
    }

    private String getCRN() {
        return getServiceRequest().getCrn();
    }

    private String getCitizenFeedback() {
        return this.getServiceRequest().getDynamicSingleValue(VALUES_CITIZENFEEDBACK);
    }

    private Long getRequesterId() {
        return getRequestInfo().getUserInfo().getId();
    }

    private RequestInfo getRequestInfo() {
        return objectMapper.convertValue(sevaRequestMap.get(REQUEST_INFO), RequestInfo.class);
    }

    private User getUserInfo() {
        return getRequestInfo().getUserInfo();
    }


    private double getLongitude() {
        return this.getServiceRequest().getLng() == null ? 0.0 : this.getServiceRequest().getLng();
    }

    private double getLatitude() {
        return this.getServiceRequest().getLat() == null ? 0.0 : this.getServiceRequest().getLat();
    }

    public Long getReceivingCenter() {
        final String receivingCenter = this.getServiceRequest().getDynamicSingleValue(VALUES_RECEIVING_CENTER);
        return isEmpty(receivingCenter) ? null : Long.valueOf(receivingCenter);
    }

    private Long getDepartment() {
        return Long.valueOf(this.getServiceRequest().getDynamicSingleValue(VALUES_DEPARTMENT));
    }

    private Long getStateId() {
        final String stateId = this.getServiceRequest().getDynamicSingleValue(VALUES_STATE_ID);
        return isEmpty(stateId) ? null : Long.valueOf(stateId);
    }

    public Long getLocation() {
        final String locationId = this.getServiceRequest().getDynamicSingleValue(LOCATION_ID);
        return isEmpty(locationId) ? null : Long.valueOf(locationId);
    }

    private Long getChildLocation() {
        final String childLocationId = this.getServiceRequest().getDynamicSingleValue(CHILD_LOCATION_ID);
        return isEmpty(childLocationId) ? null : Long.valueOf(childLocationId);
    }

    private Long getAssigneeId() {
        final String assigneeId = this.getServiceRequest().getDynamicSingleValue(VALUES_ASSIGNEE_ID);
        return isEmpty(assigneeId) ? null : Long.valueOf(assigneeId);
    }

    private Long getUserId() {
        if (getUserInfo() == null) {
            return null;
        }
        return isAuthenticatedRoleCitizen() ? getUserInfo().getId() : null;
    }

    private boolean isAuthenticatedRoleCitizen() {
        return CITIZEN.equals(getUserInfo().getType());
    }
}