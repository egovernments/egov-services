package org.egov.pgr.write.contracts.grievance;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.pgr.write.model.ComplaintRecord;

import java.util.HashMap;

import static org.egov.pgr.write.contracts.grievance.ServiceRequest.*;
import static org.hibernate.internal.util.StringHelper.isEmpty;

public class SevaRequest {

    private static final String SERVICE_REQUEST = "ServiceRequest";
    private static final String REQUEST_INFO = "RequestInfo";
    public static final String CITIZEN = "CITIZEN";

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
            .department(getDepartment())
            .build();
    }

    @SuppressWarnings("unchecked")
    private ServiceRequest getServiceRequest() {
        return new ServiceRequest((HashMap<String, Object>) sevaRequestMap.get(SERVICE_REQUEST));
    }

    private String getCRN() {
        return getServiceRequest().getCrn();
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
        final String receivingCenter = this.getServiceRequest().getValues().get(VALUES_RECEIVING_CENTER);
        return isEmpty(receivingCenter) ? null : Long.valueOf(receivingCenter);
    }

    private Long getDepartment() {
        return Long.valueOf(this.getServiceRequest().getValues().get(VALUES_DEPARTMENT));
    }

    private Long getStateId() {
        final String stateId = this.getServiceRequest().getValues().get(VALUES_STATE_ID);
        return isEmpty(stateId) ? null : Long.valueOf(stateId);
    }

    public Long getLocation() {
        final String locationId = this.getServiceRequest().getValues().get(LOCATION_ID);
        return isEmpty(locationId) ? null : Long.valueOf(locationId);
    }

    private Long getChildLocation() {
        final String childLocationId = this.getServiceRequest().getValues().get(CHILD_LOCATION_ID);
        return isEmpty(childLocationId) ?  null : Long.valueOf(childLocationId);
    }

    private Long getAssigneeId() {
        final String assigneeId = this.getServiceRequest().getValues().get(VALUES_ASSIGNEE_ID);
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