package org.egov.pgr.contracts.grievance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Service request raised by the citizen
 */
public class ServiceRequest {

    public static final String USER_ID = "user_id";
    public static final String LOCATION_ID = "location_id";
    public static final String CHILD_LOCATION_ID = "child_location_id";
    public static final String LOCATION_NAME = "location_name";
    public static final String ASSIGNEE_ID = "assignee_id";

    public enum ComplaintStatus {
        COMPLETED, FORWARDED, REJECTED, REGISTERED, WITHDRAWN, REOPENED, PROCESSING;
    }

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

    public String getCrn() {
        return crn;
    }

    public Boolean getStatus() {
        return status;
    }

    public ComplaintStatus getStatusDetails() {
        return statusDetails;
    }

    public String getComplaintTypeName() {
        return complaintTypeName;
    }

    public String getComplaintTypeCode() {
        return complaintTypeCode;
    }

    public String getComplaintTypeId() {
        return complaintTypeId;
    }

    public String getDetails() {
        return details;
    }

    public String getAgencyResponsible() {
        return agencyResponsible;
    }

    public String getServiceNotice() {
        return serviceNotice;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Date getEscalationDate() {
        return escalationDate;
    }

    public String getLandmarkDetails() {
        return landmarkDetails;
    }

    public String getCrossHierarchyId() {
        return crossHierarchyId;
    }

    public Integer getZipcode() {
        return zipcode;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getAccountId() {
        return accountId;
    }

    public Long getApprovalPosition() {
        return approvalPosition;
    }

    public String getApprovalComment() {
        return approvalComment;
    }

    public Map<String, String> getValues() {
        return values;
    }
}