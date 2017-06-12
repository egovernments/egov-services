package entities.requests.pgrCollections.createComplaint;

import org.codehaus.jackson.annotate.JsonProperty;

public class ServiceRequest
{
    private String tenantId;

    private String serviceRequestId;

    private String phone;

    private String mediaUrl;

    private Boolean status;

    private String lng;

    private String addressId;

    private String serviceName;

    private String requestedDatetime;

    private AttribValues[] attribValues;

    private String address;

    private String email;

    private String description;

    private String serviceCode;

    private String firstName;

    private String lat;

    private String lastName;

    private String deviceId;

    private String accountId;

    private String updatedDateTime;

    private String expectedDateTime;

    private String zipcode;

    private String agencyResponsible;

    private String serviceNotice;

    @JsonProperty("isAttribValuesPopulated")
    private Boolean isAttribValuesPopulated;

    public Boolean getAttribValuesPopulated() {
        return isAttribValuesPopulated;
    }

    public void setAttribValuesPopulated(Boolean attribValuesPopulated) {
        isAttribValuesPopulated = attribValuesPopulated;
    }

    public AttribValues[] getAttribValues() {
        return attribValues;
    }

    public void setAttribValues(AttribValues[] attribValues) {
        this.attribValues = attribValues;
    }

    public String getTenantId ()
    {
        return tenantId;
    }

    public void setTenantId (String tenantId)
    {
        this.tenantId = tenantId;
    }

    public String getServiceRequestId ()
    {
        return serviceRequestId;
    }

    public void setServiceRequestId (String serviceRequestId)
    {
        this.serviceRequestId = serviceRequestId;
    }

    public String getPhone ()
    {
        return phone;
    }

    public void setPhone (String phone)
    {
        this.phone = phone;
    }

    public String getMediaUrl ()
    {
        return mediaUrl;
    }

    public void setMediaUrl (String mediaUrl)
    {
        this.mediaUrl = mediaUrl;
    }

    public Boolean getStatus ()
    {
        return status;
    }

    public void setStatus (Boolean status)
    {
        this.status = status;
    }

    public String getLng ()
    {
        return lng;
    }

    public void setLng (String lng)
    {
        this.lng = lng;
    }

    public String getAddressId ()
    {
        return addressId;
    }

    public void setAddressId (String addressId)
    {
        this.addressId = addressId;
    }

    public String getServiceName ()
    {
        return serviceName;
    }

    public void setServiceName (String serviceName)
    {
        this.serviceName = serviceName;
    }

    public String getRequestedDatetime ()
    {
        return requestedDatetime;
    }

    public void setRequestedDatetime (String requestedDatetime)
    {
        this.requestedDatetime = requestedDatetime;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getServiceCode ()
    {
        return serviceCode;
    }

    public void setServiceCode (String serviceCode)
    {
        this.serviceCode = serviceCode;
    }

    public String getFirstName ()
    {
        return firstName;
    }

    public void setFirstName (String firstName)
    {
        this.firstName = firstName;
    }

    public String getLat ()
    {
        return lat;
    }

    public void setLat (String lat)
    {
        this.lat = lat;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(String updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public String getExpectedDateTime() {
        return expectedDateTime;
    }

    public void setExpectedDateTime(String expectedDateTime) {
        this.expectedDateTime = expectedDateTime;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
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
}