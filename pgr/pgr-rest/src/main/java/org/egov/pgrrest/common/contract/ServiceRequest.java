package org.egov.pgrrest.common.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.pgr.common.contract.AttributeEntry;
import org.egov.pgr.common.contract.AttributeValues;
import org.egov.pgrrest.common.model.AuthenticatedUser;
import org.egov.pgrrest.common.model.Requester;
import org.egov.pgrrest.read.domain.model.Coordinates;
import org.egov.pgrrest.read.domain.model.ServiceRequestLocation;
import org.egov.pgrrest.read.domain.model.ServiceRequestType;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service request raised by the citizen
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceRequest {

    private static final String LOCATION_ID = "locationId";
    private static final String REQUESTER_ADDRESS = "requesterAddress";
    private static final String RECEIVING_MODE = "receivingMode";
    private static final String RECEIVING_CENTER = "receivingCenter";
    private static final String USER_ID = "userId";

    private String tenantId;

    @JsonProperty("serviceRequestId")
    @Setter
    private String crn;

    private Boolean status;

    @JsonProperty("serviceName")
    private String serviceTypeName;

    @JsonProperty("serviceCode")
    private String serviceTypeCode;

    private String description;

    private String agencyResponsible;

    private String serviceNotice;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    @JsonProperty("requestedDatetime")
    @Setter
    private Date createdDate;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    @JsonProperty("updatedDatetime")
    @Setter
    private Date lastModifiedDate;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    @JsonProperty("expectedDatetime")
    private Date escalationDate;

    private String address;

    @JsonProperty("addressId")
    private String crossHierarchyId;

    @JsonProperty("zipcode")
    private Integer zipcode;

    @JsonProperty("lat")
    private Double latitude;

    @JsonProperty("lng")
    private Double longitude;

    private List<String> mediaUrls;

    @Setter
    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private String deviceId;

    private String accountId;

    private List<AttributeEntry> attribValues = new ArrayList<>();

    public ServiceRequest(org.egov.pgrrest.read.domain.model.ServiceRequest complaint) {
        crn = complaint.getCrn();
        status = complaint.isClosed();
        serviceTypeName = complaint.getServiceRequestType().getName();
        serviceTypeCode = complaint.getServiceRequestType().getCode();
        description = complaint.getDescription();
        createdDate = complaint.getCreatedDate();
        lastModifiedDate = complaint.getLastModifiedDate();
        escalationDate = complaint.getEscalationDate();
        address = complaint.getAddress();
        crossHierarchyId = complaint.getServiceRequestLocation().getCrossHierarchyId();
        latitude = complaint.getServiceRequestLocation().getCoordinates().getLatitude();
        longitude = complaint.getServiceRequestLocation().getCoordinates().getLongitude();
        firstName = complaint.getRequester().getFirstName();
        lastName = null;
        phone = complaint.getRequester().getMobile();
        email = complaint.getRequester().getEmail();
        if (!CollectionUtils.isEmpty(complaint.getAttributeEntries())) {
            attribValues = complaint.getAttributeEntries()
                .stream()
                .map(attribute -> new AttributeEntry(attribute.getKey(), attribute.getCode()))
                .collect(Collectors.toList());
        }
        tenantId = complaint.getTenantId();
    }

    public org.egov.pgrrest.read.domain.model.ServiceRequest toDomainForCreateRequest(AuthenticatedUser
                                                                                          authenticatedUser) {
        return toDomain(authenticatedUser, false);
    }

    public org.egov.pgrrest.read.domain.model.ServiceRequest toDomainForUpdateRequest(AuthenticatedUser
                                                                                          authenticatedUser) {
        return toDomain(authenticatedUser, true);
    }

    private org.egov.pgrrest.read.domain.model.ServiceRequest toDomain(AuthenticatedUser authenticatedUser, boolean
        isUpdate) {
        final ServiceRequestLocation serviceRequestLocation = getServiceRequestLocation();
        final Requester complainant = getRequester();
        return org.egov.pgrrest.read.domain.model.ServiceRequest.builder()
            .authenticatedUser(authenticatedUser)
            .crn(crn)
            .serviceRequestType(new ServiceRequestType(serviceTypeName, serviceTypeCode, tenantId))
            .address(address)
            .mediaUrls(mediaUrls)
            .serviceRequestLocation(serviceRequestLocation)
            .requester(complainant)
            .tenantId(tenantId)
            .description(description)
            .receivingMode(getReceivingMode())
            .receivingCenter(getReceivingCenter())
            .modifyServiceRequest(isUpdate)
            .attributeEntries(toDomainAttributeEntries())
            .build();
    }

    private Requester getRequester() {
        final String requesterAddress = getRequesterAddress();
        final String requesterUserId = getRequesterUserId();
        return Requester.builder()
            .firstName(firstName)
            .mobile(phone)
            .email(email)
            .userId(requesterUserId)
            .address(requesterAddress)
            .build();
    }

    private ServiceRequestLocation getServiceRequestLocation() {
        final Coordinates coordinates = new Coordinates(latitude, longitude);
        return ServiceRequestLocation.builder()
            .coordinates(coordinates)
            .crossHierarchyId(crossHierarchyId)
            .locationId(getLocationId())
            .build();
    }

    private String getLocationId() {
        return getDynamicSingleValue(LOCATION_ID);
    }

    private String getReceivingMode() {
        return getDynamicSingleValue(RECEIVING_MODE);
    }

    private String getReceivingCenter() {
        return getDynamicSingleValue(RECEIVING_CENTER);
    }

    private String getRequesterUserId() {
        return getDynamicSingleValue(USER_ID);
    }

    private String getRequesterAddress() {
        return getDynamicSingleValue(REQUESTER_ADDRESS);
    }

    private String getDynamicSingleValue(String key) {
        return AttributeValues.getAttributeSingleValue(attribValues, key);
    }

    private List<org.egov.pgrrest.common.model.AttributeEntry> toDomainAttributeEntries() {
        if (CollectionUtils.isEmpty(attribValues)) {
            return Collections.emptyList();
        }
        return attribValues.stream().map(this::toDomainAttributeEntry).collect(Collectors.toList());
    }

    private org.egov.pgrrest.common.model.AttributeEntry toDomainAttributeEntry(AttributeEntry attributeEntry) {
        return new org.egov.pgrrest.common.model.AttributeEntry(attributeEntry.getKey(), attributeEntry.getName());
    }

}