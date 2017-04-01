package org.egov.pgr.write.contracts.grievance;

import org.egov.pgr.write.model.ComplaintRecord;

import java.util.HashMap;

import static org.egov.pgr.write.contracts.grievance.ServiceRequest.*;

public class SevaRequest {

    private static final String REQUESTER_ID = "requester_id";
    private static final String SERVICE_REQUEST = "ServiceRequest";
    private static final String REQUEST_INFO = "RequestInfo";

    private HashMap<String, Object> sevaRequestMap;

    public SevaRequest(HashMap<String, Object> sevaRequestMap) {
        this.sevaRequestMap = sevaRequestMap;
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
            .complainantAddress(this.getServiceRequest().getValues().get(VALUES_COMPLAINANT_ADDRESS))
            .complainantUserId(getUserId())
            .receivingMode(this.getServiceRequest().getValues().get(VALUES_RECEIVING_MODE))
            .receivingCenter(getReceivingCenter())
            .complaintTypeCode(this.getServiceRequest().getComplaintTypeCode())
            .complaintStatus(this.getServiceRequest().getValues().get(VALUES_STATUS))
            .assigneeId(getAssigneeId())
            .location(getLocation())
            .childLocation(getChildLocation())
            .escalationDate(this.getServiceRequest().getEscalationDate())
            .workflowStateId(getStateId())
            .department(getDesignation())
            .build();
    }

    @SuppressWarnings("unchecked")
    private ServiceRequest getServiceRequest() {
        return new ServiceRequest((HashMap<String, Object>) sevaRequestMap.get(SERVICE_REQUEST));
    }

    private String getCRN() {
        return getServiceRequest().getCrn();
    }

    @SuppressWarnings("unchecked")
    private Long getRequesterId() {
        HashMap<String, Object> requestInfoMap = (HashMap<String, Object>) sevaRequestMap.get(REQUEST_INFO);
        return Long.parseLong(String.valueOf(requestInfoMap.get(REQUESTER_ID)));
    }


    private double getLongitude() {
        return this.getServiceRequest().getLng() == null ? 0.0 : this.getServiceRequest().getLng();
    }

    private double getLatitude() {
        return this.getServiceRequest().getLat() == null ? 0.0 : this.getServiceRequest().getLat();
    }

    public Long getReceivingCenter() {
        final String receivingCenter = this.getServiceRequest().getValues().get(VALUES_RECEIVING_CENTER);
        return receivingCenter == null ? null : Long.valueOf(receivingCenter);
    }

    private Long getDesignation() {
        return Long.valueOf(this.getServiceRequest().getValues().get(VALUES_DESIGNATION));
    }

    private Long getStateId() {
        final String stateId = this.getServiceRequest().getValues().get(VALUES_STATE_ID);
        return stateId == null ? null : Long.valueOf(stateId);
    }

    public Long getLocation() {
        final String locationId = this.getServiceRequest().getValues().get(LOCATION_ID);
        return locationId != null ? Long.valueOf(locationId) : null;
    }

    private Long getChildLocation() {
        final String childLocationId = this.getServiceRequest().getValues().get(CHILD_LOCATION_ID);
        return childLocationId != null ? Long.valueOf(childLocationId) : null;
    }

    private Long getAssigneeId() {
        final String assigneeId = this.getServiceRequest().getValues().get(VALUES_ASSIGNEE_ID);
        return assigneeId == null ? null : Long.valueOf(assigneeId);
    }

    private Long getUserId() {
        final String userId = this.getServiceRequest().getValues().get(USER_ID);
        return userId != null ? Long.valueOf(userId) : null;
    }
}