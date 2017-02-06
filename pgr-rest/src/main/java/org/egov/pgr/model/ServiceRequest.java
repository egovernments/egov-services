package org.egov.pgr.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.enums.ComplaintStatus;

import java.util.Date;
import java.util.HashMap;
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
    private String crn = null;

    @JsonProperty("status")
    private Boolean status = null;

    @JsonProperty("status_details")
    private ComplaintStatus statusDetails = null;

    @JsonProperty("service_name")
    private String complaintTypeName = null;

    @JsonProperty("service_code")
    private String complaintTypeCode = null;

    @JsonProperty("service_id")
    private String complaintTypeId = null;

    @JsonProperty("description")
    private String details = null;

    @JsonProperty("agency_responsible")
    private String agencyResponsible = null;

    @JsonProperty("service_notice")
    private String serviceNotice = null;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    @JsonProperty("requested_datetime")
    private Date createdDate = null;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    @JsonProperty("updated_datetime")
    private Date lastModifiedDate = null;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    @JsonProperty("expected_datetime")
    private Date escalationDate = null;

    @JsonProperty("address")
    private String landmarkDetails = null;

    @JsonProperty("address_id")
    private String crossHierarchyId = null;

    @JsonProperty("zipcode")
    private Integer zipcode = null;

    @JsonProperty("lat")
    private Double lat = null;

    @JsonProperty("lng")
    private Double lng = null;

    @JsonProperty("media_url")
    private String mediaUrl = null;

    @JsonProperty("first_name")
    private String firstName = null;

    @JsonProperty("last_name")
    private String lastName = null;

    @JsonProperty("phone")
    private String phone = null;

    @JsonProperty("email")
    private String email = null;

    @JsonProperty("device_id")
    private String deviceId = null;

    @JsonProperty("account_id")
    private String accountId = null;

    @JsonProperty("approval_position")
    private Long approvalPosition = null;

    @JsonProperty("approval_comment")
    private String approvalComment = null;

    @JsonProperty("values")
    private Map<String, String> values = new HashMap<>();

    public ServiceRequest(Complaint complaint) {
        crn = complaint.getCrn();
        status = complaint.isCompleted();
        statusDetails = ComplaintStatus.valueOf(complaint.getStatus().getName());
        complaintTypeName = complaint.getComplaintType().getName();
        complaintTypeCode = complaint.getComplaintType().getCode();
        complaintTypeId = String.valueOf(complaint.getComplaintType().getId());
        details = complaint.getDetails();
        createdDate = complaint.getCreatedDate();
        lastModifiedDate = complaint.getLastModifiedDate();
        escalationDate = complaint.getEscalationDate();
        landmarkDetails = complaint.getLandmarkDetails();
        crossHierarchyId = String.valueOf(complaint.getCrossHierarchyId());
        lat = complaint.getLat();
        lng = complaint.getLng();
        firstName = complaint.getComplainant().getName();
        lastName = complaint.getComplainant().getName();
        phone = complaint.getComplainant().getMobile();
        email = complaint.getComplainant().getEmail();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ServiceRequest {\n");

        sb.append("    serviceRequestId: ").append(toIndentedString(crn)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
//        sb.append("    statusNotes: ").append(toIndentedString(statusDetails)).append("\n");
        sb.append("    serviceName: ").append(toIndentedString(complaintTypeName)).append("\n");
        sb.append("    serviceCode: ").append(toIndentedString(complaintTypeCode)).append("\n");
        sb.append("    serviceId: ").append(toIndentedString(complaintTypeId)).append("\n");
        sb.append("    description: ").append(toIndentedString(details)).append("\n");
        sb.append("    agencyResponsible: ").append(toIndentedString(agencyResponsible)).append("\n");
        sb.append("    serviceNotice: ").append(toIndentedString(serviceNotice)).append("\n");
        sb.append("    requestedDatetime: ").append(toIndentedString(createdDate)).append("\n");
        sb.append("    updatedDatetime: ").append(toIndentedString(lastModifiedDate)).append("\n");
        sb.append("    expectedDatetime: ").append(toIndentedString(escalationDate)).append("\n");
        sb.append("    address: ").append(toIndentedString(landmarkDetails)).append("\n");
        sb.append("    addressId: ").append(toIndentedString(crossHierarchyId)).append("\n");
        sb.append("    zipcode: ").append(toIndentedString(zipcode)).append("\n");
        sb.append("    lat: ").append(toIndentedString(lat)).append("\n");
        sb.append("    _long: ").append(toIndentedString(lng)).append("\n");
        sb.append("    mediaUrl: ").append(toIndentedString(mediaUrl)).append("\n");
        sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
        sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
        sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("    deviceId: ").append(toIndentedString(deviceId)).append("\n");
        sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}