package org.egov.pgr.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Instance of Service request raised for a particular service. As per extension propsed in the Service definition \&quot;attributes\&quot; carry the input values requried by metadata definition in the structure as described by the corresponding schema. 
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-02-20T05:39:55.235Z")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceReq   {
  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("serviceCode")
  private String serviceCode = null;

  @JsonProperty("serviceRequestId")
  private String serviceRequestId = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("lat")
  private Integer lat = null;

  @JsonProperty("long")
  private Integer _long = null;

  @JsonProperty("address")
  private String address = null;

  @JsonProperty("addressId")
  private String addressId = null;

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("deviceId")
  private String deviceId = null;

  @JsonProperty("accountId")
  private String accountId = null;

  @JsonProperty("firstName")
  private String firstName = null;

  @JsonProperty("lastName")
  private String lastName = null;

  @JsonProperty("phone")
  private Long phone = null;

  @JsonProperty("attributes")
  private Object attributes = null;

  /**
   * The current status of the service request
   */
  public enum StatusEnum {
    NEW("New"),
    
    INPROGRESS("InProgress"),
    
    CLOSED("Closed"),
    
    CANCELLED("Cancelled"),
    
    REJECTED("Rejected");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("status")
  private StatusEnum status = null;

  @JsonProperty("assignedTo")
  private String assignedTo = null;

  @JsonProperty("media")
  private List<Media> media = null;

  /**
   * source of the complaint - Text, Mobile app, Phone, CSC, WhatsApp
   */
  public enum SourceEnum {
    SMS("sms"),
    
    EMAIL("email"),
    
    PHONE("phone"),
    
    MOBILEAPP("mobileapp"),
    
    WHATSAPP("whatsapp"),
    
    CSC("csc");

    private String value;

    SourceEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static SourceEnum fromValue(String text) {
      for (SourceEnum b : SourceEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("source")
  private SourceEnum source = null;

  @JsonProperty("expectedTime")
  private Long expectedTime = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public ServiceReq tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id for which the service is being created
   * @return tenantId
  **/
  @NotNull


  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public ServiceReq serviceCode(String serviceCode) {
    this.serviceCode = serviceCode;
    return this;
  }

   /**
   * Code of the service being requested (as per teh service definition)
   * @return serviceCode
  **/
  @NotNull


  public String getServiceCode() {
    return serviceCode;
  }

  public void setServiceCode(String serviceCode) {
    this.serviceCode = serviceCode;
  }

  public ServiceReq serviceRequestId(String serviceRequestId) {
    this.serviceRequestId = serviceRequestId;
    return this;
  }

   /**
   * The server generated unique ID of the service request created.
   * @return serviceRequestId
  **/


  public String getServiceRequestId() {
    return serviceRequestId;
  }

  public void setServiceRequestId(String serviceRequestId) {
    this.serviceRequestId = serviceRequestId;
  }

  public ServiceReq description(String description) {
    this.description = description;
    return this;
  }

   /**
   * A full description of the request provided by the user
   * @return description
  **/
  @NotNull


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ServiceReq lat(Integer lat) {
    this.lat = lat;
    return this;
  }

   /**
   * Latitude of the location.
   * @return lat
  **/


  public Integer getLat() {
    return lat;
  }

  public void setLat(Integer lat) {
    this.lat = lat;
  }

  public ServiceReq _long(Integer _long) {
    this._long = _long;
    return this;
  }

   /**
   * Longitude of location.
   * @return _long
  **/


  public Integer getLong() {
    return _long;
  }

  public void setLong(Integer _long) {
    this._long = _long;
  }

  public ServiceReq address(String address) {
    this.address = address;
    return this;
  }

   /**
   * Human readable address or description of location.
   * @return address
  **/


  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public ServiceReq addressId(String addressId) {
    this.addressId = addressId;
    return this;
  }

   /**
   * The internal address ID used by a tenant master address repository or other addressing system - for eGov it can be boundary id in the boundary service
   * @return addressId
  **/


  public String getAddressId() {
    return addressId;
  }

  public void setAddressId(String addressId) {
    this.addressId = addressId;
  }

  public ServiceReq email(String email) {
    this.email = email;
    return this;
  }

   /**
   * The email address of the person submitting the request.
   * @return email
  **/


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public ServiceReq deviceId(String deviceId) {
    this.deviceId = deviceId;
    return this;
  }

   /**
   * The unique device ID of the device submitting the request.
   * @return deviceId
  **/


  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public ServiceReq accountId(String accountId) {
    this.accountId = accountId;
    return this;
  }

   /**
   * userid of the user requesting the srervice - in our case it may be same as phone as we are using mobile number as the userid
   * @return accountId
  **/


  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public ServiceReq firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

   /**
   * The given name of the person submitting the request.
   * @return firstName
  **/


  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public ServiceReq lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

   /**
   * The family name of the person submitting the request.
   * @return lastName
  **/


  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public ServiceReq phone(Long phone) {
    this.phone = phone;
    return this;
  }

   /**
   * The phone number of the person submitting the request.
   * @return phone
  **/
  @NotNull


  public Long getPhone() {
    return phone;
  }

  public void setPhone(Long phone) {
    this.phone = phone;
  }

  public ServiceReq attributes(Object attributes) {
    this.attributes = attributes;
    return this;
  }

   /**
   * This is the json object that will carry the actual input (whereever the metadata requries input). Structure should be same as the schema definition provided in the metadata of the service (schema compliance check to be performed at client/server)
   * @return attributes
  **/


  public Object getAttributes() {
    return attributes;
  }

  public void setAttributes(Object attributes) {
    this.attributes = attributes;
  }

  public ServiceReq status(StatusEnum status) {
    this.status = status;
    return this;
  }

   /**
   * The current status of the service request
   * @return status
  **/


  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public ServiceReq assignedTo(String assignedTo) {
    this.assignedTo = assignedTo;
    return this;
  }

   /**
   * Either of employee/designation/agency/authority this request is assigned to
   * @return assignedTo
  **/


  public String getAssignedTo() {
    return assignedTo;
  }

  public void setAssignedTo(String assignedTo) {
    this.assignedTo = assignedTo;
  }

  public ServiceReq media(List<Media> media) {
    this.media = media;
    return this;
  }

  public ServiceReq addMediaItem(Media mediaItem) {
    if (this.media == null) {
      this.media = new ArrayList<Media>();
    }
    this.media.add(mediaItem);
    return this;
  }

   /**
   * List of media associated with this service request sorted by attachment date 
   * @return media
  **/

  @Valid

  public List<Media> getMedia() {
    return media;
  }

  public void setMedia(List<Media> media) {
    this.media = media;
  }

  public ServiceReq source(SourceEnum source) {
    this.source = source;
    return this;
  }

   /**
   * source of the complaint - Text, Mobile app, Phone, CSC, WhatsApp
   * @return source
  **/


  public SourceEnum getSource() {
    return source;
  }

  public void setSource(SourceEnum source) {
    this.source = source;
  }

  public ServiceReq expectedTime(Long expectedTime) {
    this.expectedTime = expectedTime;
    return this;
  }

   /**
   * epoch of the time object is last modified
   * @return expectedTime
  **/


  public Long getExpectedTime() {
    return expectedTime;
  }

  public void setExpectedTime(Long expectedTime) {
    this.expectedTime = expectedTime;
  }

  public ServiceReq auditDetails(AuditDetails auditDetails) {
    this.auditDetails = auditDetails;
    return this;
  }

   /**
   * Get auditDetails
   * @return auditDetails
  **/

  @Valid

  public AuditDetails getAuditDetails() {
    return auditDetails;
  }

  public void setAuditDetails(AuditDetails auditDetails) {
    this.auditDetails = auditDetails;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceReq serviceReq = (ServiceReq) o;
    return Objects.equals(this.tenantId, serviceReq.tenantId) &&
        Objects.equals(this.serviceCode, serviceReq.serviceCode) &&
        Objects.equals(this.serviceRequestId, serviceReq.serviceRequestId) &&
        Objects.equals(this.description, serviceReq.description) &&
        Objects.equals(this.lat, serviceReq.lat) &&
        Objects.equals(this._long, serviceReq._long) &&
        Objects.equals(this.address, serviceReq.address) &&
        Objects.equals(this.addressId, serviceReq.addressId) &&
        Objects.equals(this.email, serviceReq.email) &&
        Objects.equals(this.deviceId, serviceReq.deviceId) &&
        Objects.equals(this.accountId, serviceReq.accountId) &&
        Objects.equals(this.firstName, serviceReq.firstName) &&
        Objects.equals(this.lastName, serviceReq.lastName) &&
        Objects.equals(this.phone, serviceReq.phone) &&
        Objects.equals(this.attributes, serviceReq.attributes) &&
        Objects.equals(this.status, serviceReq.status) &&
        Objects.equals(this.assignedTo, serviceReq.assignedTo) &&
        Objects.equals(this.media, serviceReq.media) &&
        Objects.equals(this.source, serviceReq.source) &&
        Objects.equals(this.expectedTime, serviceReq.expectedTime) &&
        Objects.equals(this.auditDetails, serviceReq.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tenantId, serviceCode, serviceRequestId, description, lat, _long, address, addressId, email, deviceId, accountId, firstName, lastName, phone, attributes, status, assignedTo, media, source, expectedTime, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceReq {\n");
    
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    serviceCode: ").append(toIndentedString(serviceCode)).append("\n");
    sb.append("    serviceRequestId: ").append(toIndentedString(serviceRequestId)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    lat: ").append(toIndentedString(lat)).append("\n");
    sb.append("    _long: ").append(toIndentedString(_long)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    addressId: ").append(toIndentedString(addressId)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    deviceId: ").append(toIndentedString(deviceId)).append("\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
    sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    assignedTo: ").append(toIndentedString(assignedTo)).append("\n");
    sb.append("    media: ").append(toIndentedString(media)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    expectedTime: ").append(toIndentedString(expectedTime)).append("\n");
    sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

