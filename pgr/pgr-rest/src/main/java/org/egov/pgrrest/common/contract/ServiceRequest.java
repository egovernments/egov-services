package org.egov.pgrrest.common.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.pgr.common.contract.AttributeEntry;
import org.egov.pgr.common.contract.AttributeValues;
import org.egov.pgrrest.common.model.AuthenticatedUser;
import org.egov.pgrrest.common.model.Complainant;
import org.egov.pgrrest.read.domain.model.Complaint;
import org.egov.pgrrest.read.domain.model.ComplaintLocation;
import org.egov.pgrrest.read.domain.model.ComplaintType;
import org.egov.pgrrest.read.domain.model.Coordinates;

import java.util.*;

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

//  Short term feature flag - to support persisting to complaint table or new submission table.
    @JsonProperty("isForNewSchema")
    private boolean useNewSchema;

    public ServiceRequest(Complaint complaint) {
        crn = complaint.getCrn();
        status = complaint.isClosed();
        complaintTypeName = complaint.getComplaintType().getName();
        complaintTypeCode = complaint.getComplaintType().getCode();
        description = complaint.getDescription();
        createdDate = complaint.getCreatedDate();
        lastModifiedDate = complaint.getLastModifiedDate();
        escalationDate = complaint.getEscalationDate();
        address = complaint.getAddress();
        crossHierarchyId = complaint.getComplaintLocation().getCrossHierarchyId();
        latitude = complaint.getComplaintLocation().getCoordinates().getLatitude();
        longitude = complaint.getComplaintLocation().getCoordinates().getLongitude();
        firstName = complaint.getComplainant().getFirstName();
        lastName = null;
        phone = complaint.getComplainant().getMobile();
        email = complaint.getComplainant().getEmail();
        values = getAdditionalValues(complaint);
        attribValues = getAttributeValues(complaint);
        tenantId = complaint.getTenantId();
    }

    private List<AttributeEntry> getAttributeValues(Complaint complaint) {
        final ArrayList<AttributeEntry> attributeEntries = new ArrayList<>();
        attributeEntries.add(new AttributeEntry("receivingMode", complaint.getReceivingMode()));
        attributeEntries.add(new AttributeEntry("complaintStatus", complaint.getComplaintStatus()));
        addAttributeEntryIfPresent(attributeEntries, "receivingCenter", complaint.getReceivingCenter());
        addAttributeEntryIfPresent(attributeEntries, "locationId", complaint.getComplaintLocation().getLocationId());
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

    private Map<String, String> getAdditionalValues(Complaint complaint) {
        final HashMap<String, String> map = new HashMap<>();
        map.put("receivingMode", complaint.getReceivingMode());
        map.put("complaintStatus", complaint.getComplaintStatus());
        addEntryIfPresent(map, "receivingCenter", complaint.getReceivingCenter());
        addEntryIfPresent(map, "locationId", complaint.getComplaintLocation().getLocationId());
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

    public Complaint toDomainForCreateRequest(AuthenticatedUser authenticatedUser) {
        return toDomain(authenticatedUser, false);
    }

    public Complaint toDomainForUpdateRequest(AuthenticatedUser authenticatedUser) {
        return toDomain(authenticatedUser, true);
    }

    private Complaint toDomain(AuthenticatedUser authenticatedUser, boolean isUpdate) {
        final ComplaintLocation complaintLocation = getComplaintLocation();
        final Complainant complainant = getComplainant();
        return Complaint.builder()
            .authenticatedUser(authenticatedUser)
            .crn(crn)
            .complaintType(new ComplaintType(complaintTypeName, complaintTypeCode, tenantId))
            .address(address)
            .mediaUrls(mediaUrls)
            .complaintLocation(complaintLocation)
            .complainant(complainant)
            .tenantId(tenantId)
            .description(description)
            .receivingMode(getReceivingMode())
            .receivingCenter(getReceivingCenter())
            .modifyComplaint(isUpdate)
            .build();
    }

    private Complainant getComplainant() {
        final String complainantAddress = getComplainantAddress();
        final String complainantUserId = getComplainantUserId();
        return Complainant.builder()
            .firstName(firstName)
            .mobile(phone)
            .email(email)
            .userId(complainantUserId)
            .address(complainantAddress)
            .tenantId(tenantId)
            .build();
    }

    private ComplaintLocation getComplaintLocation() {
        final Coordinates coordinates = new Coordinates(latitude, longitude, tenantId);
        return ComplaintLocation.builder()
            .coordinates(coordinates)
            .crossHierarchyId(crossHierarchyId)
            .locationId(getLocationId())
            .tenantId(tenantId)
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