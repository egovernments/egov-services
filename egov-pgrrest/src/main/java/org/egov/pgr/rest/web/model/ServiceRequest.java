package org.egov.pgr.rest.web.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.egov.infra.utils.StringUtils;
import org.egov.pgr.entity.enums.ComplaintStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Service request raised by the citizen
 */
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

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss",timezone = "IST")
    @JsonProperty("requested_datetime")
    private Date createdDate = null;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss",timezone = "IST")
    @JsonProperty("updated_datetime")
    private Date lastModifiedDate = null;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss",timezone = "IST")
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

    @JsonProperty("long")
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
    private List<AttributeValue> values = new ArrayList<>();

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ComplaintStatus getStatusDetails() {
        return statusDetails;
    }

    public void setStatusDetails(ComplaintStatus statusDetails) {
        this.statusDetails = statusDetails;
    }

    public String getComplaintTypeName() {
        return complaintTypeName;
    }

    public void setComplaintTypeName(String complaintTypeName) {
        this.complaintTypeName = complaintTypeName;
    }

    public String getComplaintTypeCode() {
        return complaintTypeCode;
    }

    public void setComplaintTypeCode(String complaintTypeCode) {
        this.complaintTypeCode = complaintTypeCode;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getAgencyResponsible() {
        return agencyResponsible;
    }

    public void setAgencyResponsible(String agencyResponsible) {
        this.agencyResponsible = agencyResponsible;
    }

    public String getServiceNotice() {
        return serviceNotice;
    }

    public void setServiceNotice(String serviceNotice) {
        this.serviceNotice = serviceNotice;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Date getEscalationDate() {
        return escalationDate;
    }

    public void setEscalationDate(Date escalationDate) {
        this.escalationDate = escalationDate;
    }

    public String getLandmarkDetails() {
        return landmarkDetails;
    }

    public void setLandmarkDetails(String landmarkDetails) {
        this.landmarkDetails = landmarkDetails;
    }

    public String getCrossHierarchyId() {
        return crossHierarchyId;
    }

    public void setCrossHierarchyId(String crossHeirarchyId) {
        this.crossHierarchyId = crossHeirarchyId;
    }

    public Integer getZipcode() {
        return zipcode;
    }

    public void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Long getApprovalPosition() {
        return approvalPosition;
    }

    public void setApprovalPosition(Long approvalPosition) {
        this.approvalPosition = approvalPosition;
    }

    public String getApprovalComment() {
        return approvalComment;
    }

    public void setApprovalComment(String approvalComment) {
        this.approvalComment = approvalComment;
    }

    public String getComplaintTypeId() {
        return complaintTypeId;
    }

    public void setComplaintTypeId(String complaintTypeId) {
        this.complaintTypeId = complaintTypeId;
    }

    public List<AttributeValue> getValues() {
		return values;
	}

	public void setValues(List<AttributeValue> values) {
		this.values = values;
	}

	public boolean validate(ServiceRequest serviceRequest) {
        if (StringUtils.isNotBlank(serviceRequest.getComplaintTypeCode()) &&
                StringUtils.isNotBlank(serviceRequest.getDetails()) &&
                StringUtils.isNotBlank(serviceRequest.getCrossHierarchyId()))
            return true;
        else if (StringUtils.isNotBlank(serviceRequest.getCrossHierarchyId()) ||
                (Objects.nonNull(serviceRequest.getLat()) && (Objects.nonNull(serviceRequest.getLat()))))
            return true;
        else
            return false;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceRequest serviceRequest = (ServiceRequest) o;
        return Objects.equals(this.crn, serviceRequest.crn) &&
                Objects.equals(this.status, serviceRequest.status) &&
                Objects.equals(this.statusDetails, serviceRequest.statusDetails) &&
                Objects.equals(this.complaintTypeName, serviceRequest.complaintTypeName) &&
                Objects.equals(this.complaintTypeCode, serviceRequest.complaintTypeCode) &&
                Objects.equals(this.complaintTypeId, serviceRequest.complaintTypeId) &&
                Objects.equals(this.details, serviceRequest.details) &&
                Objects.equals(this.agencyResponsible, serviceRequest.agencyResponsible) &&
                Objects.equals(this.serviceNotice, serviceRequest.serviceNotice) &&
                Objects.equals(this.createdDate, serviceRequest.createdDate) &&
                Objects.equals(this.lastModifiedDate, serviceRequest.lastModifiedDate) &&
                Objects.equals(this.escalationDate, serviceRequest.escalationDate) &&
                Objects.equals(this.landmarkDetails, serviceRequest.landmarkDetails) &&
                Objects.equals(this.crossHierarchyId, serviceRequest.crossHierarchyId) &&
                Objects.equals(this.zipcode, serviceRequest.zipcode) &&
                Objects.equals(this.lat, serviceRequest.lat) &&
                Objects.equals(this.lng, serviceRequest.lng) &&
                Objects.equals(this.mediaUrl, serviceRequest.mediaUrl) &&
                Objects.equals(this.firstName, serviceRequest.firstName) &&
                Objects.equals(this.lastName, serviceRequest.lastName) &&
                Objects.equals(this.phone, serviceRequest.phone) &&
                Objects.equals(this.email, serviceRequest.email) &&
                Objects.equals(this.deviceId, serviceRequest.deviceId) &&
                Objects.equals(this.accountId, serviceRequest.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(crn, status, statusDetails, complaintTypeName, complaintTypeCode, details, agencyResponsible,
                serviceNotice, createdDate, lastModifiedDate, escalationDate, landmarkDetails, crossHierarchyId, zipcode, lat,
                lng, mediaUrl, firstName, lastName, phone, email, deviceId, accountId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ServiceRequest {\n");

        sb.append("    serviceRequestId: ").append(toIndentedString(crn)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    statusNotes: ").append(toIndentedString(statusDetails)).append("\n");
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