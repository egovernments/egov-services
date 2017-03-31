package org.egov.pgr.common.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.pgr.common.model.AuthenticatedUser;
import org.egov.pgr.read.domain.model.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @JsonProperty("service_request_id")
    @Setter
    private String crn;

    @JsonProperty("status")
    private Boolean status;

    @JsonProperty("service_name")
    private String complaintTypeName;

    @JsonProperty("service_code")
    private String complaintTypeCode;

    @JsonProperty("description")
    private String description;

    @JsonProperty("agency_responsible")
    private String agencyResponsible;

    @JsonProperty("service_notice")
    private String serviceNotice;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    @JsonProperty("requested_datetime")
    @Setter
    private Date createdDate;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    @JsonProperty("updated_datetime")
    @Setter
    private Date lastModifiedDate;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    @JsonProperty("expected_datetime")
    private Date escalationDate;

    @JsonProperty("address")
    private String address;

    @JsonProperty("address_id")
    private String crossHierarchyId;

    @JsonProperty("zipcode")
    private Integer zipcode;

    @JsonProperty("lat")
    private Double latitude;

    @JsonProperty("lng")
    private Double longitude;

    @JsonProperty("media_urls")
    private List<String> mediaUrls;

    @Setter
    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    private String email;

    @JsonProperty("device_id")
    private String deviceId;

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("values")
    private Map<String, String> values = new HashMap<>();

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
    }

    private Map<String, String> getAdditionalValues(Complaint complaint) {
        final HashMap<String, String> map = new HashMap<>();
        map.put("receivingMode", complaint.getReceivingMode());
        map.put("complaintStatus", complaint.getComplaintStatus());
        addEntryIfPresent(map, "receivingCenter", complaint.getReceivingCenter());
        addEntryIfPresent(map, "locationId", complaint.getComplaintLocation().getLocationId());
        addEntryIfPresent(map, "childLocationId", complaint.getChildLocation());
        addEntryIfPresent(map, "stateId", complaint.getState());
        addEntryIfPresent(map, "assigneeId", complaint.getAssignee());
        addEntryIfPresent(map, "departmentId", complaint.getDepartment());
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
                .complaintType(new ComplaintType(complaintTypeName, complaintTypeCode))
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
        final String complainantAddress = getcomplainantAddress();
        final String complainantUserId = getComplainantUserId();
        return Complainant.builder()
                .firstName(firstName)
                .mobile(phone)
                .email(email)
                .userId(complainantUserId)
                .address(complainantAddress)
                .build();
    }

    private String getcomplainantAddress() {
        return values.get(COMPLAINANT_ADDRESS);
    }

    private ComplaintLocation getComplaintLocation() {
        final Coordinates coordinates = new Coordinates(latitude, longitude);
        return ComplaintLocation.builder()
                .coordinates(coordinates)
                .crossHierarchyId(crossHierarchyId)
                .locationId(getLocationId())
                .build();
    }

    private String getLocationId() {
        return values.get(LOCATION_ID);
    }

    private String getReceivingMode() {
        return values.get(RECEIVING_MODE);
    }

    private String getReceivingCenter() {
        return values.get(RECEIVING_CENTER);
    }

    private String getComplainantUserId() {
        return values.get(USER_ID);
    }

}