package org.egov.pgr.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@AllArgsConstructor
@Builder
public class ServiceRequest {

    @JsonProperty("service_request_id")
    private String crn;

    @JsonProperty("status")
    private Boolean status;

    @JsonProperty("service_name")
    private String complaintTypeName;

    @JsonProperty("service_code")
    private String complaintTypeCode;

    @JsonProperty("service_id")
    private String complaintTypeId;

    @JsonProperty("description")
    private String details;

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
    private String landmarkDetails;

    @JsonProperty("address_id")
    private String crossHierarchyId;

    public boolean isCrossHierarchyIdPresent() {
        return isNotEmpty(this.crossHierarchyId);
    }

    @JsonProperty("zipcode")
    private Integer zipcode;

    @JsonProperty("lat")
    private Double lat;

    public String getLatitude() {
        return lat.toString();
    }

    @JsonProperty("lng")
    private Double lng;

    public String getLongitude() {
        return lng.toString();
    }

    @JsonProperty("media_url")
    private String mediaUrl;

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

    @JsonProperty("approval_position")
    private Long approvalPosition;

    @JsonProperty("approval_comment")
    private String approvalComment;

    @JsonProperty("values")
    @Setter
    private Map<String, String> values;

    public boolean isLocationCoordinatesPresent() {
        return this.getLat() != null && this.getLat() > 0 && this.getLng() != null && this.getLng() > 0;
    }

}