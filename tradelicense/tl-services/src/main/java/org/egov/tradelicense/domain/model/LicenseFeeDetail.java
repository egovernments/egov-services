package org.egov.tradelicense.domain.model;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;


public class LicenseFeeDetail   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("licenseId")
  private Long licenseId = null;

  @JsonProperty("financialYear")
  private String financialYear = null;

  @JsonProperty("amount")
  private Double amount = null;

  @JsonProperty("paid")
  private Boolean paid = false;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public LicenseFeeDetail id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the LicenseFeeDetail
   * @return id
  **/


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LicenseFeeDetail licenseId(Long licenseId) {
    this.licenseId = licenseId;
    return this;
  }



  public Long getLicenseId() {
    return licenseId;
  }

  public void setLicenseId(Long licenseId) {
    this.licenseId = licenseId;
  }

  public LicenseFeeDetail financialYear(String financialYear) {
    this.financialYear = financialYear;
    return this;
  }

 @Size(min=4,max=128)
  public String getFinancialYear() {
    return financialYear;
  }

  public void setFinancialYear(String financialYear) {
    this.financialYear = financialYear;
  }

  public LicenseFeeDetail amount(Double amount) {
    this.amount = amount;
    return this;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public LicenseFeeDetail paid(Boolean paid) {
    this.paid = paid;
    return this;
  }

  public Boolean getPaid() {
    return paid;
  }

  public void setPaid(Boolean paid) {
    this.paid = paid;
  }

  public LicenseFeeDetail auditDetails(AuditDetails auditDetails) {
    this.auditDetails = auditDetails;
    return this;
  }


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
    LicenseFeeDetail licenseFeeDetail = (LicenseFeeDetail) o;
    return Objects.equals(this.id, licenseFeeDetail.id) &&
        Objects.equals(this.licenseId, licenseFeeDetail.licenseId) &&
        Objects.equals(this.financialYear, licenseFeeDetail.financialYear) &&
        Objects.equals(this.amount, licenseFeeDetail.amount) &&
        Objects.equals(this.paid, licenseFeeDetail.paid) &&
        Objects.equals(this.auditDetails, licenseFeeDetail.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, licenseId, financialYear, amount, paid, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LicenseFeeDetail {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    licenseId: ").append(toIndentedString(licenseId)).append("\n");
    sb.append("    financialYear: ").append(toIndentedString(financialYear)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    paid: ").append(toIndentedString(paid)).append("\n");
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

