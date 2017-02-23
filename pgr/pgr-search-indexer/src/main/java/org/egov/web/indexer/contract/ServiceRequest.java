package org.egov.web.indexer.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class ServiceRequest {
    @JsonProperty("serviceRequestId")
    private String crn = null;

    @JsonProperty("status")
    private Boolean status = null;

    @JsonProperty("statusNotes")
    private String statusDetails = null;

    @JsonProperty("serviceName")
    private String complaintTypeName = null;

    @JsonProperty("serviceCode")
    private String complaintTypeCode = null;

    @JsonProperty("description")
    private String details = null;

    @JsonProperty("agencyResponsible")
    private String agencyResponsible = null;

    @JsonProperty("serviceNotice")
    private String serviceNotice = null;

    // @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    @JsonProperty("requestedDatetime")
    private String createdDate = null;

    //@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    @JsonProperty("updatedDatetime")
    private String lastModifiedDate = null;

    // @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    @JsonProperty("expectedDatetime")
    private String escalationDate = null;

    @JsonProperty("address")
    private String landmarkDetails = null;

    @JsonProperty("addressId")
    private String crossHierarchyId = null;

    @JsonProperty("zipcode")
    private Integer zipcode = null;

    @JsonProperty("lat")
    private Double lat = null;

    @JsonProperty("lng")
    private Double lng = null;

    @JsonProperty("mediaUrl")
    private String mediaUrl = null;

    @JsonProperty("firstName")
    private String firstName = null;

    @JsonProperty("lastName")
    private String lastName = null;

    @JsonProperty("phone")
    private String phone = null;

    @JsonProperty("email")
    private String email = null;

    @JsonProperty("deviceId")
    private String deviceId = null;

    @JsonProperty("accountId")
    private String accountId = null;

    @JsonProperty("values")
    private Map<String, String> values = new HashMap<>();

    public String getCrn() {
        return crn;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getStatusDetails() {
        return statusDetails;
    }

    public String getComplaintTypeName() {
        return complaintTypeName;
    }

    public String getComplaintTypeCode() {
        return complaintTypeCode;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public String getEscalationDate() {
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

    public Map<String, String> getValues() {
        return values;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setStatusDetails(String statusDetails) {
        this.statusDetails = statusDetails;
    }

    public void setComplaintTypeName(String complaintTypeName) {
        this.complaintTypeName = complaintTypeName;
    }

    public void setComplaintTypeCode(String complaintTypeCode) {
        this.complaintTypeCode = complaintTypeCode;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setAgencyResponsible(String agencyResponsible) {
        this.agencyResponsible = agencyResponsible;
    }

    public void setServiceNotice(String serviceNotice) {
        this.serviceNotice = serviceNotice;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public void setEscalationDate(String escalationDate) {
        this.escalationDate = escalationDate;
    }

    public void setLandmarkDetails(String landmarkDetails) {
        this.landmarkDetails = landmarkDetails;
    }

    public void setCrossHierarchyId(String crossHierarchyId) {
        this.crossHierarchyId = crossHierarchyId;
    }

    public void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }
}