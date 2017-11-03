/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.AuditDetails;
import io.swagger.model.Bank;
import io.swagger.model.CommonEnum;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * This object holds the Supplier information. 
 */
@ApiModel(description = "This object holds the Supplier information. ")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-02T13:59:35.200+05:30")

public class Supplier   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("supplierType")
  private CommonEnum supplierType = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("address")
  private String address = null;

  @JsonProperty("status")
  private CommonEnum status = null;

  @JsonProperty("inActiveDate")
  private Long inActiveDate = null;

  @JsonProperty("supplierContactNo")
  private String supplierContactNo = null;

  @JsonProperty("faxNo")
  private String faxNo = null;

  @JsonProperty("website")
  private String website = null;

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("narration")
  private String narration = null;

  @JsonProperty("panNo")
  private String panNo = null;

  @JsonProperty("tinNo")
  private String tinNo = null;

  @JsonProperty("cstNo")
  private String cstNo = null;

  @JsonProperty("vatNo")
  private String vatNo = null;

  @JsonProperty("contactPerson")
  private String contactPerson = null;

  @JsonProperty("contactPersonNo")
  private String contactPersonNo = null;

  @JsonProperty("bankInfo")
  private Bank bankInfo = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public Supplier id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Supplier 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Supplier ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Supplier tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Transfer Indent Note
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenant id of the Transfer Indent Note")

@Size(min=4,max=128) 
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public Supplier supplierType(CommonEnum supplierType) {
    this.supplierType = supplierType;
    return this;
  }

   /**
   * Get supplierType
   * @return supplierType
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public CommonEnum getSupplierType() {
    return supplierType;
  }

  public void setSupplierType(CommonEnum supplierType) {
    this.supplierType = supplierType;
  }

  public Supplier code(String code) {
    this.code = code;
    return this;
  }

   /**
   * code of the Supplier 
   * @return code
  **/
  @ApiModelProperty(required = true, value = "code of the Supplier ")
  @NotNull

@Pattern(regexp="^[a-zA-Z0-9]+$") @Size(min=5,max=50) 
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Supplier name(String name) {
    this.name = name;
    return this;
  }

   /**
   * name of the Material 
   * @return name
  **/
  @ApiModelProperty(required = true, value = "name of the Material ")
  @NotNull

@Pattern(regexp="^[a-zA-Z ]+$") @Size(min=5,max=50) 
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Supplier address(String address) {
    this.address = address;
    return this;
  }

   /**
   * address of the Supplier   
   * @return address
  **/
  @ApiModelProperty(required = true, value = "address of the Supplier   ")
  @NotNull

@Pattern(regexp="^[a-zA-Z0-9 ]+$") @Size(max=1000) 
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Supplier status(CommonEnum status) {
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @ApiModelProperty(value = "")

  @Valid

  public CommonEnum getStatus() {
    return status;
  }

  public void setStatus(CommonEnum status) {
    this.status = status;
  }

  public Supplier inActiveDate(Long inActiveDate) {
    this.inActiveDate = inActiveDate;
    return this;
  }

   /**
   * inactive date of the Supplier 
   * @return inActiveDate
  **/
  @ApiModelProperty(value = "inactive date of the Supplier ")


  public Long getInActiveDate() {
    return inActiveDate;
  }

  public void setInActiveDate(Long inActiveDate) {
    this.inActiveDate = inActiveDate;
  }

  public Supplier supplierContactNo(String supplierContactNo) {
    this.supplierContactNo = supplierContactNo;
    return this;
  }

   /**
   * contact no of the Supplier    
   * @return supplierContactNo
  **/
  @ApiModelProperty(required = true, value = "contact no of the Supplier    ")
  @NotNull

@Pattern(regexp="^[0-9]+$") @Size(max=10) 
  public String getSupplierContactNo() {
    return supplierContactNo;
  }

  public void setSupplierContactNo(String supplierContactNo) {
    this.supplierContactNo = supplierContactNo;
  }

  public Supplier faxNo(String faxNo) {
    this.faxNo = faxNo;
    return this;
  }

   /**
   * fax number of Supplier  
   * @return faxNo
  **/
  @ApiModelProperty(value = "fax number of Supplier  ")

@Pattern(regexp="^[0-9]+$") 
  public String getFaxNo() {
    return faxNo;
  }

  public void setFaxNo(String faxNo) {
    this.faxNo = faxNo;
  }

  public Supplier website(String website) {
    this.website = website;
    return this;
  }

   /**
   * website of the Supplier 
   * @return website
  **/
  @ApiModelProperty(value = "website of the Supplier ")

@Pattern(regexp="^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$") 
  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public Supplier email(String email) {
    this.email = email;
    return this;
  }

   /**
   * email of the Supplier 
   * @return email
  **/
  @ApiModelProperty(value = "email of the Supplier ")

@Pattern(regexp="^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$") @Size(max=100) 
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Supplier narration(String narration) {
    this.narration = narration;
    return this;
  }

   /**
   * description of the Supplier 
   * @return narration
  **/
  @ApiModelProperty(value = "description of the Supplier ")

@Pattern(regexp="^[0-9a-zA-Z ]+$") @Size(max=1000) 
  public String getNarration() {
    return narration;
  }

  public void setNarration(String narration) {
    this.narration = narration;
  }

  public Supplier panNo(String panNo) {
    this.panNo = panNo;
    return this;
  }

   /**
   * pan number of supplier 
   * @return panNo
  **/
  @ApiModelProperty(value = "pan number of supplier ")

@Size(max=10) 
  public String getPanNo() {
    return panNo;
  }

  public void setPanNo(String panNo) {
    this.panNo = panNo;
  }

  public Supplier tinNo(String tinNo) {
    this.tinNo = tinNo;
    return this;
  }

   /**
   * tin number of supplier    
   * @return tinNo
  **/
  @ApiModelProperty(value = "tin number of supplier    ")

@Size(max=10) 
  public String getTinNo() {
    return tinNo;
  }

  public void setTinNo(String tinNo) {
    this.tinNo = tinNo;
  }

  public Supplier cstNo(String cstNo) {
    this.cstNo = cstNo;
    return this;
  }

   /**
   * cst number of supplier   
   * @return cstNo
  **/
  @ApiModelProperty(value = "cst number of supplier   ")

@Size(max=10) 
  public String getCstNo() {
    return cstNo;
  }

  public void setCstNo(String cstNo) {
    this.cstNo = cstNo;
  }

  public Supplier vatNo(String vatNo) {
    this.vatNo = vatNo;
    return this;
  }

   /**
   * vat number of supplier   
   * @return vatNo
  **/
  @ApiModelProperty(value = "vat number of supplier   ")

@Size(max=10) 
  public String getVatNo() {
    return vatNo;
  }

  public void setVatNo(String vatNo) {
    this.vatNo = vatNo;
  }

  public Supplier contactPerson(String contactPerson) {
    this.contactPerson = contactPerson;
    return this;
  }

   /**
   * name of the contact person    
   * @return contactPerson
  **/
  @ApiModelProperty(value = "name of the contact person    ")

@Pattern(regexp="^[a-zA-Z ]+$") @Size(min=5,max=50) 
  public String getContactPerson() {
    return contactPerson;
  }

  public void setContactPerson(String contactPerson) {
    this.contactPerson = contactPerson;
  }

  public Supplier contactPersonNo(String contactPersonNo) {
    this.contactPersonNo = contactPersonNo;
    return this;
  }

   /**
   * contact number of the contact person   
   * @return contactPersonNo
  **/
  @ApiModelProperty(value = "contact number of the contact person   ")

@Pattern(regexp="^[0-9]+$") @Size(max=10) 
  public String getContactPersonNo() {
    return contactPersonNo;
  }

  public void setContactPersonNo(String contactPersonNo) {
    this.contactPersonNo = contactPersonNo;
  }

  public Supplier bankInfo(Bank bankInfo) {
    this.bankInfo = bankInfo;
    return this;
  }

   /**
   * Get bankInfo
   * @return bankInfo
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public Bank getBankInfo() {
    return bankInfo;
  }

  public void setBankInfo(Bank bankInfo) {
    this.bankInfo = bankInfo;
  }

  public Supplier auditDetails(AuditDetails auditDetails) {
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
    Supplier supplier = (Supplier) o;
    return Objects.equals(this.id, supplier.id) &&
        Objects.equals(this.tenantId, supplier.tenantId) &&
        Objects.equals(this.supplierType, supplier.supplierType) &&
        Objects.equals(this.code, supplier.code) &&
        Objects.equals(this.name, supplier.name) &&
        Objects.equals(this.address, supplier.address) &&
        Objects.equals(this.status, supplier.status) &&
        Objects.equals(this.inActiveDate, supplier.inActiveDate) &&
        Objects.equals(this.supplierContactNo, supplier.supplierContactNo) &&
        Objects.equals(this.faxNo, supplier.faxNo) &&
        Objects.equals(this.website, supplier.website) &&
        Objects.equals(this.email, supplier.email) &&
        Objects.equals(this.narration, supplier.narration) &&
        Objects.equals(this.panNo, supplier.panNo) &&
        Objects.equals(this.tinNo, supplier.tinNo) &&
        Objects.equals(this.cstNo, supplier.cstNo) &&
        Objects.equals(this.vatNo, supplier.vatNo) &&
        Objects.equals(this.contactPerson, supplier.contactPerson) &&
        Objects.equals(this.contactPersonNo, supplier.contactPersonNo) &&
        Objects.equals(this.bankInfo, supplier.bankInfo) &&
        Objects.equals(this.auditDetails, supplier.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, supplierType, code, name, address, status, inActiveDate, supplierContactNo, faxNo, website, email, narration, panNo, tinNo, cstNo, vatNo, contactPerson, contactPersonNo, bankInfo, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Supplier {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    supplierType: ").append(toIndentedString(supplierType)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    inActiveDate: ").append(toIndentedString(inActiveDate)).append("\n");
    sb.append("    supplierContactNo: ").append(toIndentedString(supplierContactNo)).append("\n");
    sb.append("    faxNo: ").append(toIndentedString(faxNo)).append("\n");
    sb.append("    website: ").append(toIndentedString(website)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    narration: ").append(toIndentedString(narration)).append("\n");
    sb.append("    panNo: ").append(toIndentedString(panNo)).append("\n");
    sb.append("    tinNo: ").append(toIndentedString(tinNo)).append("\n");
    sb.append("    cstNo: ").append(toIndentedString(cstNo)).append("\n");
    sb.append("    vatNo: ").append(toIndentedString(vatNo)).append("\n");
    sb.append("    contactPerson: ").append(toIndentedString(contactPerson)).append("\n");
    sb.append("    contactPersonNo: ").append(toIndentedString(contactPersonNo)).append("\n");
    sb.append("    bankInfo: ").append(toIndentedString(bankInfo)).append("\n");
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
