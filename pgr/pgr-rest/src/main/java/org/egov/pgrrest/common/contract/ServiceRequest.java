package org.egov.pgrrest.common.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.pgr.common.contract.AttributeEntry;
import org.egov.pgr.common.contract.AttributeValues;
import org.egov.pgrrest.common.model.AuthenticatedUser;
import org.egov.pgrrest.common.model.Requester;
import org.egov.pgrrest.read.domain.model.ServiceRequestLocation;
import org.egov.pgrrest.read.domain.model.ServiceRequestType;
import org.egov.pgrrest.read.domain.model.Coordinates;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Service request raised by the citizen
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceRequest {

    private static final String LOCATION_ID = "locationId";
    private static final String COMPLAINANT_ADDRESS = "complainantAddress";
    private static final String RECEIVING_MODE = "receivingMode";
    private static final String RECEIVING_CENTER = "receivingCenter";
    private static final String USER_ID = "userId";

    private String tenantId;

    @JsonProperty("serviceRequestId")
    @Setter
    private String crn;

    private Boolean status;

    @JsonProperty("serviceName")
    private String complaintTypeName;

    @JsonProperty("serviceCode")
    private String complaintTypeCode;

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
    
    private Map<String, String> values = new HashMap<>();

//  Short term feature flag - to support values and attribValues usage
//  This flag should be set by the consumer for the service to consider attribValues instead of existing values field.
    @JsonProperty("isAttribValuesPopulated")
    private boolean attribValuesPopulated;

    private List<AttributeEntry> attribValues = new ArrayList<>();

    public ServiceRequest(org.egov.pgrrest.read.domain.model.ServiceRequest complaint) {
        crn = complaint.getCrn();
        status = complaint.isClosed();
        complaintTypeName = complaint.getComplaintType().getName();
        complaintTypeCode = complaint.getComplaintType().getCode();
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
        values = getAdditionalValues(complaint);
        if(CollectionUtils.isEmpty(complaint.getAttributeEntries())) {
            attribValues = getAttributeValues(complaint);
        } else {
            attribValues = complaint.getAttributeEntries()
                .stream()
                .map(attribute -> new AttributeEntry(attribute.getKey(), attribute.getCode()))
                .collect(Collectors.toList());
        }
        tenantId = complaint.getTenantId();
    }

    private List<AttributeEntry> getAttributeValues(org.egov.pgrrest.read.domain.model.ServiceRequest complaint) {
        final ArrayList<AttributeEntry> attributeEntries = new ArrayList<>();
        attributeEntries.add(new AttributeEntry("receivingMode", complaint.getReceivingMode()));
        attributeEntries.add(new AttributeEntry("complaintStatus", complaint.getComplaintStatus()));
        addAttributeEntryIfPresent(attributeEntries, "receivingCenter", complaint.getReceivingCenter());
        addAttributeEntryIfPresent(attributeEntries, "locationId", complaint.getServiceRequestLocation().getLocationId());
        addAttributeEntryIfPresent(attributeEntries, "childLocationId", complaint.getChildLocation());
        addAttributeEntryIfPresent(attributeEntries, "stateId", complaint.getState());
        addAttributeEntryIfPresent(attributeEntries, "assigneeId", toString(complaint.getAssignee()));
        addAttributeEntryIfPresent(attributeEntries, "departmentId", toString(complaint.getDepartment()));
        addAttributeEntryIfPresent(attributeEntries, "citizenFeedback",complaint.getCitizenFeedback());
        return attributeEntries;
    }

    private String toString(Long longValue) {
        return longValue == null ? null : longValue.toString();
    }

    private void addAttributeEntryIfPresent(ArrayList<AttributeEntry> attributeEntries, String key, String name) {
        if (isEmpty(name)) {
            return;
        }
        attributeEntries.add(new AttributeEntry(key, name));
    }

    private Map<String, String> getAdditionalValues(org.egov.pgrrest.read.domain.model.ServiceRequest complaint) {
        final HashMap<String, String> map = new HashMap<>();
        map.put("receivingMode", complaint.getReceivingMode());
        map.put("complaintStatus", complaint.getComplaintStatus());
        addEntryIfPresent(map, "receivingCenter", complaint.getReceivingCenter());
        addEntryIfPresent(map, "locationId", complaint.getServiceRequestLocation().getLocationId());
        addEntryIfPresent(map, "childLocationId", complaint.getChildLocation());
        addEntryIfPresent(map, "stateId", complaint.getState());
        addEntryIfPresent(map, "assigneeId", toString(complaint.getAssignee()));
        addEntryIfPresent(map, "departmentId", toString(complaint.getDepartment()));
        addEntryIfPresent(map, "citizenFeedback",complaint.getCitizenFeedback());
        return map;
    }

    private void addEntryIfPresent(Map<String, String> map, String key, String item) {
        if (!isEmpty(item)) {
            map.put(key, item);
        }
    }

    public org.egov.pgrrest.read.domain.model.ServiceRequest toDomainForCreateRequest(AuthenticatedUser authenticatedUser) {
        return toDomain(authenticatedUser, false);
    }

    public org.egov.pgrrest.read.domain.model.ServiceRequest toDomainForUpdateRequest(AuthenticatedUser authenticatedUser) {
        return toDomain(authenticatedUser, true);
    }

    private org.egov.pgrrest.read.domain.model.ServiceRequest toDomain(AuthenticatedUser authenticatedUser, boolean isUpdate) {
        final ServiceRequestLocation serviceRequestLocation = getComplaintLocation();
        final Requester complainant = getComplainant();
        return org.egov.pgrrest.read.domain.model.ServiceRequest.builder()
            .authenticatedUser(authenticatedUser)
            .crn(crn)
            .complaintType(new ServiceRequestType(complaintTypeName, complaintTypeCode, tenantId))
            .address(address)
            .mediaUrls(mediaUrls)
            .serviceRequestLocation(serviceRequestLocation)
            .requester(complainant)
            .tenantId(tenantId)
            .description(description)
            .receivingMode(getReceivingMode())
            .receivingCenter(getReceivingCenter())
            .modifyServiceRequest(isUpdate)
            .build();
    }

    private Requester getComplainant() {
        final String complainantAddress = getComplainantAddress();
        final String complainantUserId = getComplainantUserId();
        return Requester.builder()
            .firstName(firstName)
            .mobile(phone)
            .email(email)
            .userId(complainantUserId)
            .address(complainantAddress)
            .build();
    }

    private ServiceRequestLocation getComplaintLocation() {
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

    private String getComplainantUserId() {
        return getDynamicSingleValue(USER_ID);
    }

    private String getComplainantAddress() {
        return getDynamicSingleValue(COMPLAINANT_ADDRESS);
    }

    private String getDynamicSingleValue(String key) {
        if (attribValuesPopulated) {
            return AttributeValues.getAttributeSingleValue(attribValues, key);
        } else {
            return values.get(key);
        }
    }

}