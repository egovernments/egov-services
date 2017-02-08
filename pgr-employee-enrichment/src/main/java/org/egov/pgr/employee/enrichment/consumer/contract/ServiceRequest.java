package org.egov.pgr.employee.enrichment.consumer.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ServiceRequest {

    enum ComplaintStatus {
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

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:enrichSevaWithAssignee", timezone = "IST")
    @JsonProperty("requested_datetime")
    private Date createdDate = null;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:enrichSevaWithAssignee", timezone = "IST")
    @JsonProperty("updated_datetime")
    private Date lastModifiedDate = null;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:enrichSevaWithAssignee", timezone = "IST")
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

}