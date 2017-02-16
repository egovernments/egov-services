package org.egov.pgr.persistence.queue.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.commons.lang.StringUtils;
import org.egov.pgr.domain.model.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Service request raised by the citizen
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class ServiceRequest {
    @JsonProperty("service_request_id")
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
    private Date createdDate;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    @JsonProperty("updated_datetime")
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
        phone = complaint.getComplainant().getPhone();
        email = complaint.getComplainant().getEmail();
        values = complaint.getAdditionalValues();
    }

    public org.egov.pgr.domain.model.Complaint toDomain(AuthenticatedUser authenticatedUser, String jurisdictionId) {
        final Coordinates coordinates = new Coordinates(latitude, longitude);
        final ComplaintLocation complaintLocation = new ComplaintLocation(coordinates, crossHierarchyId);
        final String complainantAddress = Objects.isNull(values.get("complainantAddress")) ? StringUtils.EMPTY : values.get("complainantAddress"); 
        final Complainant complainant = new Complainant(firstName, phone, email,complainantAddress);
        return org.egov.pgr.domain.model.Complaint.builder()
                .authenticatedUser(authenticatedUser)
                .crn(crn)
                .complaintType(new ComplaintType(complaintTypeName, complaintTypeCode))
                .additionalValues(values)
                .address(address)
                .mediaUrls(mediaUrls)
                .complaintLocation(complaintLocation)
                .complainant(complainant)
                .jurisdictionId(jurisdictionId)
                .description(description)
                .build();

    }

}