package org.egov.works.masters.web.contract;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * An Object that holds the Contractor Agency/Firm Information
 */
@ApiModel(description = "An Object that holds the Contractor Agency/Firm Information")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-14T13:05:23.935Z")

public class Contractor   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("localName")
  private String localName = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("correspondenceAddress")
  private String correspondenceAddress = null;

  @JsonProperty("paymentAddress")
  private String paymentAddress = null;

  @JsonProperty("contactPerson")
  private String contactPerson = null;

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("narration")
  private String narration = null;

  @JsonProperty("mobileNumber")
  private BigDecimal mobileNumber = null;

  @JsonProperty("panNumber")
  private String panNumber = null;

  @JsonProperty("tinNumber")
  private String tinNumber = null;

  @JsonProperty("bank")
  private Bank bank = null;

  @JsonProperty("bankAccountNumber")
  private BigDecimal bankAccountNumber = null;

  @JsonProperty("pwdApprovalCode")
  private String pwdApprovalCode = null;

  @JsonProperty("exemptedFrom")
  private ContractorExemption exemptedFrom = null;

  @JsonProperty("pwdApprovalValidTill")
  private Long pwdApprovalValidTill = null;

  @JsonProperty("epfRegistrationNumber")
  private String epfRegistrationNumber = null;

  @JsonProperty("accountCode")
  private ChartOfAccount accountCode = null;

  @JsonProperty("ifscCode")
  private BigDecimal ifscCode = null;

  @JsonProperty("contractorClass")
  private ContractorClass contractorClass = null;

  @JsonProperty("pmc")
  private Boolean pmc = false;

  @JsonProperty("status")
  private String status = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public Contractor id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Contractor.
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Contractor.")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Contractor tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Contractor Agency/Firm.
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Contractor Agency/Firm.")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public Contractor name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Name of the Contractor Agency/Firm.
   * @return name
  **/
  @ApiModelProperty(required = true, value = "Name of the Contractor Agency/Firm.")
  @NotNull

 @Pattern(regexp="[a-zA-Z0-9\\s\\.,]+") @Size(min=1,max=100)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Contractor localName(String localName) {
    this.localName = localName;
    return this;
  }

   /**
   * Name of the Contractor Agency/Firm in Local Language.
   * @return localName
  **/
  @ApiModelProperty(value = "Name of the Contractor Agency/Firm in Local Language.")

 @Size(max=100)
  public String getLocalName() {
    return localName;
  }

  public void setLocalName(String localName) {
    this.localName = localName;
  }

  public Contractor code(String code) {
    this.code = code;
    return this;
  }

   /**
   * Code of the Contractor Agency/Firm.
   * @return code
  **/
  @ApiModelProperty(required = true, value = "Code of the Contractor Agency/Firm.")
  @NotNull

 @Pattern(regexp="[a-zA-Z0-9-\\\\]+") @Size(min=1,max=100)
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Contractor correspondenceAddress(String correspondenceAddress) {
    this.correspondenceAddress = correspondenceAddress;
    return this;
  }

   /**
   * Correspondence Address of the Contractor Agency/Firm.
   * @return correspondenceAddress
  **/
  @ApiModelProperty(required = true, value = "Correspondence Address of the Contractor Agency/Firm.")
  @NotNull

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(min=1,max=512)
  public String getCorrespondenceAddress() {
    return correspondenceAddress;
  }

  public void setCorrespondenceAddress(String correspondenceAddress) {
    this.correspondenceAddress = correspondenceAddress;
  }

  public Contractor paymentAddress(String paymentAddress) {
    this.paymentAddress = paymentAddress;
    return this;
  }

   /**
   * Payment Address of the Contractor.
   * @return paymentAddress
  **/
  @ApiModelProperty(value = "Payment Address of the Contractor.")

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(max=512)
  public String getPaymentAddress() {
    return paymentAddress;
  }

  public void setPaymentAddress(String paymentAddress) {
    this.paymentAddress = paymentAddress;
  }

  public Contractor contactPerson(String contactPerson) {
    this.contactPerson = contactPerson;
    return this;
  }

   /**
   * Contact Person of the Contractor Agency/Firm.
   * @return contactPerson
  **/
  @ApiModelProperty(value = "Contact Person of the Contractor Agency/Firm.")

 @Size(max=100)
  public String getContactPerson() {
    return contactPerson;
  }

  public void setContactPerson(String contactPerson) {
    this.contactPerson = contactPerson;
  }

  public Contractor email(String email) {
    this.email = email;
    return this;
  }

   /**
   * Email of the Contractor Agency/Firm.
   * @return email
  **/
  @ApiModelProperty(value = "Email of the Contractor Agency/Firm.")

  //@Pattern(regexp="^([a-zA-Z0-9_\\\\-\\\\.]+)@([a-zA-Z0-9_\\\\-\\\\.]+)\\\\.([a-zA-Z]{2,5})$") @Size(max=100)
  //TODO : there is a difference in autogen email pattern and original pattern. Commenting autogen code.
  @Pattern(regexp="^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$") @Size(max=100)
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Contractor narration(String narration) {
    this.narration = narration;
    return this;
  }

   /**
   * Narration of the Contractor Agency/Firm.
   * @return narration
  **/
  @ApiModelProperty(value = "Narration of the Contractor Agency/Firm.")

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(max=1024)
  public String getNarration() {
    return narration;
  }

  public void setNarration(String narration) {
    this.narration = narration;
  }

  public Contractor mobileNumber(BigDecimal mobileNumber) {
    this.mobileNumber = mobileNumber;
    return this;
  }

   /**
   * Mobile Number of the Contractor Agency/Firm
   * @return mobileNumber
  **/
  @ApiModelProperty(required = true, value = "Mobile Number of the Contractor Agency/Firm")
  @NotNull

  @Valid

  public BigDecimal getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(BigDecimal mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public Contractor panNumber(String panNumber) {
    this.panNumber = panNumber;
    return this;
  }

   /**
   * PAN Number of the Contractor Agency/Firm
   * @return panNumber
  **/
  @ApiModelProperty(value = "PAN Number of the Contractor Agency/Firm")

 @Pattern(regexp="[A-Z]{5}[0-9]{4}[A-Z]{1}") @Size(max=10)
  public String getPanNumber() {
    return panNumber;
  }

  public void setPanNumber(String panNumber) {
    this.panNumber = panNumber;
  }

  public Contractor tinNumber(String tinNumber) {
    this.tinNumber = tinNumber;
    return this;
  }

   /**
   * TIN Number of the Contractor Agency/Firm
   * @return tinNumber
  **/
  @ApiModelProperty(value = "TIN Number of the Contractor Agency/Firm")

 @Size(max=12)
  public String getTinNumber() {
    return tinNumber;
  }

  public void setTinNumber(String tinNumber) {
    this.tinNumber = tinNumber;
  }

  public Contractor bank(Bank bank) {
    this.bank = bank;
    return this;
  }

   /**
   * Bank of the Contractor Agency/Firm. If Financial integration is true at state/ULB level configuation then bank is mandatory else its optional.
   * @return bank
  **/
  @ApiModelProperty(value = "Bank of the Contractor Agency/Firm. If Financial integration is true at state/ULB level configuation then bank is mandatory else its optional.")

  //@Valid

  public Bank getBank() {
    return bank;
  }

  public void setBank(Bank bank) {
    this.bank = bank;
  }

  public Contractor bankAccountNumber(BigDecimal bankAccountNumber) {
    this.bankAccountNumber = bankAccountNumber;
    return this;
  }

   /**
   * Bank Account Number of the Contractor Agency/Firm. If Financial integration is true at state/ULB level configuation then bank account number is mandatory else its optional.
   * @return bankAccountNumber
  **/
  @ApiModelProperty(value = "Bank Account Number of the Contractor Agency/Firm. If Financial integration is true at state/ULB level configuation then bank account number is mandatory else its optional.")

  @Valid

  public BigDecimal getBankAccountNumber() {
    return bankAccountNumber;
  }

  public void setBankAccountNumber(BigDecimal bankAccountNumber) {
    this.bankAccountNumber = bankAccountNumber;
  }

  public Contractor pwdApprovalCode(String pwdApprovalCode) {
    this.pwdApprovalCode = pwdApprovalCode;
    return this;
  }

   /**
   * PWD Approval Code of the Contractor Agency/Firm
   * @return pwdApprovalCode
  **/
  @ApiModelProperty(value = "PWD Approval Code of the Contractor Agency/Firm")

 @Pattern(regexp="[a-zA-Z0-9-\\\\]+") @Size(max=20)
  public String getPwdApprovalCode() {
    return pwdApprovalCode;
  }

  public void setPwdApprovalCode(String pwdApprovalCode) {
    this.pwdApprovalCode = pwdApprovalCode;
  }

  public Contractor exemptedFrom(ContractorExemption exemptedFrom) {
    this.exemptedFrom = exemptedFrom;
    return this;
  }

   /**
   * Excemption allowed for of the Contractor Agency/Firm
   * @return exemptedFrom
  **/
  @ApiModelProperty(value = "Excemption allowed for of the Contractor Agency/Firm")

  @Valid

  public ContractorExemption getExemptedFrom() {
    return exemptedFrom;
  }

  public void setExemptedFrom(ContractorExemption exemptedFrom) {
    this.exemptedFrom = exemptedFrom;
  }

  public Contractor pwdApprovalValidTill(Long pwdApprovalValidTill) {
    this.pwdApprovalValidTill = pwdApprovalValidTill;
    return this;
  }

   /**
   * PWD Approval Valid Till date of Contractor Agency/Firm
   * @return pwdApprovalValidTill
  **/
  @ApiModelProperty(value = "PWD Approval Valid Till date of Contractor Agency/Firm")


  public Long getPwdApprovalValidTill() {
    return pwdApprovalValidTill;
  }

  public void setPwdApprovalValidTill(Long pwdApprovalValidTill) {
    this.pwdApprovalValidTill = pwdApprovalValidTill;
  }

  public Contractor epfRegistrationNumber(String epfRegistrationNumber) {
    this.epfRegistrationNumber = epfRegistrationNumber;
    return this;
  }

   /**
   * EPF Registration Number of the Contractor,Only Number value with decimal should be accepted
   * @return epfRegistrationNumber
  **/
  @ApiModelProperty(value = "EPF Registration Number of the Contractor,Only Number value with decimal should be accepted")

 @Pattern(regexp="[a-zA-Z0-9-\\\\]+") @Size(max=50)
  public String getEpfRegistrationNumber() {
    return epfRegistrationNumber;
  }

  public void setEpfRegistrationNumber(String epfRegistrationNumber) {
    this.epfRegistrationNumber = epfRegistrationNumber;
  }

  public Contractor accountCode(ChartOfAccount accountCode) {
    this.accountCode = accountCode;
    return this;
  }

   /**
   * Chart of Account Code of the Contractor from Financials. If Financial integration is true at state/ULB level configuation then accountCode is mandatory else its optional.
   * @return accountCode
  **/
  @ApiModelProperty(value = "Chart of Account Code of the Contractor from Financials. If Financial integration is true at state/ULB level configuation then accountCode is mandatory else its optional.")

//  @Valid

  public ChartOfAccount getAccountCode() {
    return accountCode;
  }

  public void setAccountCode(ChartOfAccount accountCode) {
    this.accountCode = accountCode;
  }

  public Contractor ifscCode(BigDecimal ifscCode) {
    this.ifscCode = ifscCode;
    return this;
  }

   /**
   * IFSC Code of the Bank Account for the Contractor Agency/Firm. If Financial integration is true at state/ULB level configuation then ifscCode is mandatory else its optional.
   * @return ifscCode
  **/
  @ApiModelProperty(value = "IFSC Code of the Bank Account for the Contractor Agency/Firm. If Financial integration is true at state/ULB level configuation then ifscCode is mandatory else its optional.")

  @Valid

  public BigDecimal getIfscCode() {
    return ifscCode;
  }

  public void setIfscCode(BigDecimal ifscCode) {
    this.ifscCode = ifscCode;
  }

  public Contractor contractorClass(ContractorClass contractorClass) {
    this.contractorClass = contractorClass;
    return this;
  }

   /**
   * Contractor Class for which Contractor Agency/Firm belongs to
   * @return contractorClass
  **/
  @ApiModelProperty(required = true, value = "Contractor Class for which Contractor Agency/Firm belongs to")
  @NotNull

//  @Valid

  public ContractorClass getContractorClass() {
    return contractorClass;
  }

  public void setContractorClass(ContractorClass contractorClass) {
    this.contractorClass = contractorClass;
  }

  public Contractor pmc(Boolean pmc) {
    this.pmc = pmc;
    return this;
  }

   /**
   * The boolean value to indicate whether the contractor Agency/Firm is PMC(Project Management Consultant). The default value is false.
   * @return pmc
  **/
  @ApiModelProperty(value = "The boolean value to indicate whether the contractor Agency/Firm is PMC(Project Management Consultant). The default value is false.")


  public Boolean getPmc() {
    return pmc;
  }

  public void setPmc(Boolean pmc) {
    this.pmc = pmc;
  }

  public Contractor status(String status) {
    this.status = status;
    return this;
  }

   /**
   * Status of the Contractor
   * @return status
  **/
  @ApiModelProperty(required = true, value = "Status of the Contractor")
  @NotNull


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Contractor auditDetails(AuditDetails auditDetails) {
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
    Contractor contractor = (Contractor) o;
    return Objects.equals(this.id, contractor.id) &&
        Objects.equals(this.tenantId, contractor.tenantId) &&
        Objects.equals(this.name, contractor.name) &&
        Objects.equals(this.localName, contractor.localName) &&
        Objects.equals(this.code, contractor.code) &&
        Objects.equals(this.correspondenceAddress, contractor.correspondenceAddress) &&
        Objects.equals(this.paymentAddress, contractor.paymentAddress) &&
        Objects.equals(this.contactPerson, contractor.contactPerson) &&
        Objects.equals(this.email, contractor.email) &&
        Objects.equals(this.narration, contractor.narration) &&
        Objects.equals(this.mobileNumber, contractor.mobileNumber) &&
        Objects.equals(this.panNumber, contractor.panNumber) &&
        Objects.equals(this.tinNumber, contractor.tinNumber) &&
        Objects.equals(this.bank, contractor.bank) &&
        Objects.equals(this.bankAccountNumber, contractor.bankAccountNumber) &&
        Objects.equals(this.pwdApprovalCode, contractor.pwdApprovalCode) &&
        Objects.equals(this.exemptedFrom, contractor.exemptedFrom) &&
        Objects.equals(this.pwdApprovalValidTill, contractor.pwdApprovalValidTill) &&
        Objects.equals(this.epfRegistrationNumber, contractor.epfRegistrationNumber) &&
        Objects.equals(this.accountCode, contractor.accountCode) &&
        Objects.equals(this.ifscCode, contractor.ifscCode) &&
        Objects.equals(this.contractorClass, contractor.contractorClass) &&
        Objects.equals(this.pmc, contractor.pmc) &&
        Objects.equals(this.status, contractor.status) &&
        Objects.equals(this.auditDetails, contractor.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, name, localName, code, correspondenceAddress, paymentAddress, contactPerson, email, narration, mobileNumber, panNumber, tinNumber, bank, bankAccountNumber, pwdApprovalCode, exemptedFrom, pwdApprovalValidTill, epfRegistrationNumber, accountCode, ifscCode, contractorClass, pmc, status, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Contractor {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    localName: ").append(toIndentedString(localName)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    correspondenceAddress: ").append(toIndentedString(correspondenceAddress)).append("\n");
    sb.append("    paymentAddress: ").append(toIndentedString(paymentAddress)).append("\n");
    sb.append("    contactPerson: ").append(toIndentedString(contactPerson)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    narration: ").append(toIndentedString(narration)).append("\n");
    sb.append("    mobileNumber: ").append(toIndentedString(mobileNumber)).append("\n");
    sb.append("    panNumber: ").append(toIndentedString(panNumber)).append("\n");
    sb.append("    tinNumber: ").append(toIndentedString(tinNumber)).append("\n");
    sb.append("    bank: ").append(toIndentedString(bank)).append("\n");
    sb.append("    bankAccountNumber: ").append(toIndentedString(bankAccountNumber)).append("\n");
    sb.append("    pwdApprovalCode: ").append(toIndentedString(pwdApprovalCode)).append("\n");
    sb.append("    exemptedFrom: ").append(toIndentedString(exemptedFrom)).append("\n");
    sb.append("    pwdApprovalValidTill: ").append(toIndentedString(pwdApprovalValidTill)).append("\n");
    sb.append("    epfRegistrationNumber: ").append(toIndentedString(epfRegistrationNumber)).append("\n");
    sb.append("    accountCode: ").append(toIndentedString(accountCode)).append("\n");
    sb.append("    ifscCode: ").append(toIndentedString(ifscCode)).append("\n");
    sb.append("    contractorClass: ").append(toIndentedString(contractorClass)).append("\n");
    sb.append("    pmc: ").append(toIndentedString(pmc)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

