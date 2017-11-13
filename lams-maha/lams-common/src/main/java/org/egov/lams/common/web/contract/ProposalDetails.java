package org.egov.lams.common.web.contract;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Land acquisition proposal details
 */
@ApiModel(description = "Land acquisition proposal details")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-09T07:54:53.690Z")

public class ProposalDetails   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("adhaarNumber")
  private String adhaarNumber = null;

  @JsonProperty("proposalDate")
  private Long proposalDate = null;

  @JsonProperty("proposerName")
  private String proposerName = null;

  @JsonProperty("proposerDepartment")
  private Department proposerDepartment = null;

  @JsonProperty("landOwnerName")
  private String landOwnerName = null;

  /**
   * Purpose of land acquisition
   */
  public enum PurposeOfLandAcquisitionEnum {
    NEW("NEW"),
    
    EXISTING("EXISTING");

    private String value;

    PurposeOfLandAcquisitionEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static PurposeOfLandAcquisitionEnum fromValue(String text) {
      for (PurposeOfLandAcquisitionEnum b : PurposeOfLandAcquisitionEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("purposeOfLandAcquisition")
  private PurposeOfLandAcquisitionEnum purposeOfLandAcquisition = null;

  @JsonProperty("organizationName")
  private String organizationName = null;

  @JsonProperty("contactNumber")
  private String contactNumber = null;

  @JsonProperty("emailId")
  private String emailId = null;

  @JsonProperty("mobileNumber")
  private String mobileNumber = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public ProposalDetails id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Proposal Details
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Proposal Details")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ProposalDetails tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * tenant id of the Land Acquisition
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "tenant id of the Land Acquisition")
  @NotNull

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public ProposalDetails adhaarNumber(String adhaarNumber) {
    this.adhaarNumber = adhaarNumber;
    return this;
  }

   /**
   * Adhaar Number of proposer
   * @return adhaarNumber
  **/
  @ApiModelProperty(value = "Adhaar Number of proposer")

 @Pattern(regexp="[0-9]") @Size(max=12)
  public String getAdhaarNumber() {
    return adhaarNumber;
  }

  public void setAdhaarNumber(String adhaarNumber) {
    this.adhaarNumber = adhaarNumber;
  }

  public ProposalDetails proposalDate(Long proposalDate) {
    this.proposalDate = proposalDate;
    return this;
  }

   /**
   * proposal date  in epoch
   * @return proposalDate
  **/
  @ApiModelProperty(required = true, value = "proposal date  in epoch")
  @NotNull


  public Long getProposalDate() {
    return proposalDate;
  }

  public void setProposalDate(Long proposalDate) {
    this.proposalDate = proposalDate;
  }

  public ProposalDetails proposerName(String proposerName) {
    this.proposerName = proposerName;
    return this;
  }

   /**
   * Proposer Name detail
   * @return proposerName
  **/
  @ApiModelProperty(required = true, value = "Proposer Name detail")
  @NotNull

 @Size(max=128)
  public String getProposerName() {
    return proposerName;
  }

  public void setProposerName(String proposerName) {
    this.proposerName = proposerName;
  }

  public ProposalDetails proposerDepartment(Department proposerDepartment) {
    this.proposerDepartment = proposerDepartment;
    return this;
  }

   /**
   * Proposer department.Dropdown list refer department. Use department code.
   * @return proposerDepartment
  **/
  @ApiModelProperty(required = true, value = "Proposer department.Dropdown list refer department. Use department code.")
  @NotNull

  @Valid

  public Department getProposerDepartment() {
    return proposerDepartment;
  }

  public void setProposerDepartment(Department proposerDepartment) {
    this.proposerDepartment = proposerDepartment;
  }

  public ProposalDetails landOwnerName(String landOwnerName) {
    this.landOwnerName = landOwnerName;
    return this;
  }

   /**
   * Name of the land owner name. Mandatory field.
   * @return landOwnerName
  **/
  @ApiModelProperty(required = true, value = "Name of the land owner name. Mandatory field.")
  @NotNull

 @Size(max=128)
  public String getLandOwnerName() {
    return landOwnerName;
  }

  public void setLandOwnerName(String landOwnerName) {
    this.landOwnerName = landOwnerName;
  }

  public ProposalDetails purposeOfLandAcquisition(PurposeOfLandAcquisitionEnum purposeOfLandAcquisition) {
    this.purposeOfLandAcquisition = purposeOfLandAcquisition;
    return this;
  }

   /**
   * Purpose of land acquisition
   * @return purposeOfLandAcquisition
  **/
  @ApiModelProperty(required = true, value = "Purpose of land acquisition")
  @NotNull


  public PurposeOfLandAcquisitionEnum getPurposeOfLandAcquisition() {
    return purposeOfLandAcquisition;
  }

  public void setPurposeOfLandAcquisition(PurposeOfLandAcquisitionEnum purposeOfLandAcquisition) {
    this.purposeOfLandAcquisition = purposeOfLandAcquisition;
  }

  public ProposalDetails organizationName(String organizationName) {
    this.organizationName = organizationName;
    return this;
  }

   /**
   * Organization name
   * @return organizationName
  **/
  @ApiModelProperty(value = "Organization name")

 @Size(max=128)
  public String getOrganizationName() {
    return organizationName;
  }

  public void setOrganizationName(String organizationName) {
    this.organizationName = organizationName;
  }

  public ProposalDetails contactNumber(String contactNumber) {
    this.contactNumber = contactNumber;
    return this;
  }

   /**
   * Contact number of owner if any
   * @return contactNumber
  **/
  @ApiModelProperty(value = "Contact number of owner if any")

 @Pattern(regexp="[0-9]") @Size(max=20)
  public String getContactNumber() {
    return contactNumber;
  }

  public void setContactNumber(String contactNumber) {
    this.contactNumber = contactNumber;
  }

  public ProposalDetails emailId(String emailId) {
    this.emailId = emailId;
    return this;
  }

   /**
   * EmailID of the owner
   * @return emailId
  **/
  @ApiModelProperty(value = "EmailID of the owner")

 @Pattern(regexp="/^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$/") @Size(min=6,max=50)
  public String getEmailId() {
    return emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public ProposalDetails mobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
    return this;
  }

   /**
   * Mobile Number of the proposer.
   * @return mobileNumber
  **/
  @ApiModelProperty(value = "Mobile Number of the proposer.")

 @Pattern(regexp="[0-9]") @Size(max=10)
  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public ProposalDetails auditDetails(AuditDetails auditDetails) {
    this.auditDetails = auditDetails;
    return this;
  }

   /**
   * Get auditDetails
   * @return auditDetails
  **/
  @ApiModelProperty(value = "")

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
    ProposalDetails proposalDetails = (ProposalDetails) o;
    return Objects.equals(this.id, proposalDetails.id) &&
        Objects.equals(this.tenantId, proposalDetails.tenantId) &&
        Objects.equals(this.adhaarNumber, proposalDetails.adhaarNumber) &&
        Objects.equals(this.proposalDate, proposalDetails.proposalDate) &&
        Objects.equals(this.proposerName, proposalDetails.proposerName) &&
        Objects.equals(this.proposerDepartment, proposalDetails.proposerDepartment) &&
        Objects.equals(this.landOwnerName, proposalDetails.landOwnerName) &&
        Objects.equals(this.purposeOfLandAcquisition, proposalDetails.purposeOfLandAcquisition) &&
        Objects.equals(this.organizationName, proposalDetails.organizationName) &&
        Objects.equals(this.contactNumber, proposalDetails.contactNumber) &&
        Objects.equals(this.emailId, proposalDetails.emailId) &&
        Objects.equals(this.mobileNumber, proposalDetails.mobileNumber) &&
        Objects.equals(this.auditDetails, proposalDetails.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, adhaarNumber, proposalDate, proposerName, proposerDepartment, landOwnerName, purposeOfLandAcquisition, organizationName, contactNumber, emailId, mobileNumber, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProposalDetails {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    adhaarNumber: ").append(toIndentedString(adhaarNumber)).append("\n");
    sb.append("    proposalDate: ").append(toIndentedString(proposalDate)).append("\n");
    sb.append("    proposerName: ").append(toIndentedString(proposerName)).append("\n");
    sb.append("    proposerDepartment: ").append(toIndentedString(proposerDepartment)).append("\n");
    sb.append("    landOwnerName: ").append(toIndentedString(landOwnerName)).append("\n");
    sb.append("    purposeOfLandAcquisition: ").append(toIndentedString(purposeOfLandAcquisition)).append("\n");
    sb.append("    organizationName: ").append(toIndentedString(organizationName)).append("\n");
    sb.append("    contactNumber: ").append(toIndentedString(contactNumber)).append("\n");
    sb.append("    emailId: ").append(toIndentedString(emailId)).append("\n");
    sb.append("    mobileNumber: ").append(toIndentedString(mobileNumber)).append("\n");
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

