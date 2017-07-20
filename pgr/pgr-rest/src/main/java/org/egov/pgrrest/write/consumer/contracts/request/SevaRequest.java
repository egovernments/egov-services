package org.egov.pgrrest.write.consumer.contracts.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestInfo;
import org.egov.pgrrest.common.domain.model.AttributeEntry;
import org.egov.pgrrest.write.domain.model.ServiceRequestRecord;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.egov.pgrrest.write.consumer.contracts.request.ServiceRequest.*;
import static org.hibernate.internal.util.StringHelper.isEmpty;
import static org.hibernate.internal.util.StringHelper.isNotEmpty;

public class SevaRequest {

    private static final String SERVICE_REQUEST = "serviceRequest";
    private static final String REQUEST_INFO = "RequestInfo";
    private static final String ATTRIBUTE_ENTRY_KEY = "key";
    private static final String ATTRIBUTE_ENTRY_NAME = "name";

    private HashMap<String, Object> sevaRequestMap;
    private ObjectMapper objectMapper;

    public SevaRequest(HashMap<String, Object> sevaRequestMap,
                       ObjectMapper objectMapper) {
        this.sevaRequestMap = sevaRequestMap;
        this.objectMapper = objectMapper;
    }

    public ServiceRequestRecord toDomain() {
        return ServiceRequestRecord.builder()
            .CRN(getCRN())
            .latitude(getLatitude())
            .longitude(getLongitude())
            .description(this.getServiceRequest().getDetails())
            .landmarkDetails(this.getServiceRequest().getLandmarkDetails())
            .createdBy(this.getRequesterId())
            .lastModifiedBy(this.getRequesterId())
            .createdDate(this.getServiceRequest().getCreatedDate())
            .lastModifiedDate(this.getServiceRequest().getLastModifiedDate())
            .requesterName(this.getServiceRequest().getFirstName())
            .requesterMobileNumber(this.getServiceRequest().getPhone())
            .requesterEmail(this.getServiceRequest().getEmail())
            .requesterAddress(this.getServiceRequest().getDynamicSingleValue(VALUES_COMPLAINANT_ADDRESS))
            .loggedInRequesterUserId(getCitizenUserId())
            .serviceRequestTypeCode(this.getServiceRequest().getComplaintTypeCode())
            .serviceRequestStatus(this.getServiceRequest().getDynamicSingleValue(VALUES_STATUS))
            .positionId(getPositionId())
            .escalationDate(this.getServiceRequest().getEscalationDate())
            .department(getDepartment())
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

    private Long getRequesterId() {
        return getRequestInfo().getUserInfo().getId();
    }

    private RequestInfo getRequestInfo() {
        return objectMapper.convertValue(sevaRequestMap.get(REQUEST_INFO), RequestInfo.class);
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

    private Long getPositionId() {
        final String positionId = this.getServiceRequest().getDynamicSingleValue(VALUES_POSITION_ID);
        return isEmpty(positionId) ? null : Long.valueOf(positionId);
    }

    private Long getCitizenUserId() {
        final String citizenUserId = this.getServiceRequest().getDynamicSingleValue(VALUES_CITIZEN_USER_ID);
        return isNotEmpty(citizenUserId) ? Long.valueOf(citizenUserId) : null;
    }

}