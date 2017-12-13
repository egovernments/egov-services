package org.egov.works.masters.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.joda.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * 
 */
@ApiModel(description = "")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-23T09:58:12.227Z")

public class FinancialYear   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("finYearRange")
  private String finYearRange = null;

  @JsonProperty("startingDate")
  private LocalDate startingDate = null;

  @JsonProperty("endingDate")
  private LocalDate endingDate = null;

  @JsonProperty("active")
  private Boolean active = null;

  @JsonProperty("isActiveForPosting")
  private Boolean isActiveForPosting = null;

  @JsonProperty("isClosed")
  private Boolean isClosed = null;

  @JsonProperty("transferClosingBalance")
  private Boolean transferClosingBalance = null;

  @JsonProperty("auditDetails")
  private Auditable auditDetails = null;

  public FinancialYear id(String id) {
    this.id = id;
    return this;
  }

   /**
   * id is the unique identifier. it is generated internally 
   * @return id
  **/
  @ApiModelProperty(value = "id is the unique identifier. it is generated internally ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public FinancialYear finYearRange(String finYearRange) {
    this.finYearRange = finYearRange;
    return this;
  }

   /**
   * finYearRange is the name of the Financial Year . for example for accounting year 2017 and 2018 it may be named 2017-18 
   * @return finYearRange
  **/
  @ApiModelProperty(required = true, value = "finYearRange is the name of the Financial Year . for example for accounting year 2017 and 2018 it may be named 2017-18 ")
  @NotNull

 @Size(min=1,max=25)
  public String getFinYearRange() {
    return finYearRange;
  }

  public void setFinYearRange(String finYearRange) {
    this.finYearRange = finYearRange;
  }

  public FinancialYear startingDate(LocalDate startingDate) {
    this.startingDate = startingDate;
    return this;
  }

   /**
   * startingDate is the date on which Accounting Year starts. Usually it is 1st April of that year. 
   * @return startingDate
  **/
  @ApiModelProperty(required = true, value = "startingDate is the date on which Accounting Year starts. Usually it is 1st April of that year. ")
  @NotNull

  @Valid

  public LocalDate getStartingDate() {
    return startingDate;
  }

  public void setStartingDate(LocalDate startingDate) {
    this.startingDate = startingDate;
  }

  public FinancialYear endingDate(LocalDate endingDate) {
    this.endingDate = endingDate;
    return this;
  }

   /**
   * endingDate is the date on which Financial Year ends. Usually it is 31st march of next year 
   * @return endingDate
  **/
  @ApiModelProperty(required = true, value = "endingDate is the date on which Financial Year ends. Usually it is 31st march of next year ")
  @NotNull

  @Valid

  public LocalDate getEndingDate() {
    return endingDate;
  }

  public void setEndingDate(LocalDate endingDate) {
    this.endingDate = endingDate;
  }

  public FinancialYear active(Boolean active) {
    this.active = active;
    return this;
  }

   /**
   * active says whether Financial Year is active or not . Over a period system will have number of Financial years. Reports ,searches,transactions will need to display this.If the active value is true then Financial Year is listed, if value is false it wont be listed 
   * @return active
  **/
  @ApiModelProperty(required = true, value = "active says whether Financial Year is active or not . Over a period system will have number of Financial years. Reports ,searches,transactions will need to display this.If the active value is true then Financial Year is listed, if value is false it wont be listed ")
  @NotNull


  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public FinancialYear isActiveForPosting(Boolean isActiveForPosting) {
    this.isActiveForPosting = isActiveForPosting;
    return this;
  }

   /**
   * isActiveForPosting refers whether posting allowed for the Financial Year or not . This value will be true for current year and few previous year for which data entry will be happening. Once the account is closed this value is set to false . All transactions will happen if and only if isActiveForPosting is true 
   * @return isActiveForPosting
  **/
  @ApiModelProperty(required = true, value = "isActiveForPosting refers whether posting allowed for the Financial Year or not . This value will be true for current year and few previous year for which data entry will be happening. Once the account is closed this value is set to false . All transactions will happen if and only if isActiveForPosting is true ")
  @NotNull


  public Boolean getIsActiveForPosting() {
    return isActiveForPosting;
  }

  public void setIsActiveForPosting(Boolean isActiveForPosting) {
    this.isActiveForPosting = isActiveForPosting;
  }

  public FinancialYear isClosed(Boolean isClosed) {
    this.isClosed = isClosed;
    return this;
  }

   /**
   * isClosed refers whether the account is closed or not . Once the account is closed and balance is transferred this value is set to false if the account is closed no transaction can happen on that financial year. 
   * @return isClosed
  **/
  @ApiModelProperty(value = "isClosed refers whether the account is closed or not . Once the account is closed and balance is transferred this value is set to false if the account is closed no transaction can happen on that financial year. ")


  public Boolean getIsClosed() {
    return isClosed;
  }

  public void setIsClosed(Boolean isClosed) {
    this.isClosed = isClosed;
  }

  public FinancialYear transferClosingBalance(Boolean transferClosingBalance) {
    this.transferClosingBalance = transferClosingBalance;
    return this;
  }

   /**
   * transferClosingBalance informs whether While closing account balance is transferred or not . 
   * @return transferClosingBalance
  **/
  @ApiModelProperty(value = "transferClosingBalance informs whether While closing account balance is transferred or not . ")


  public Boolean getTransferClosingBalance() {
    return transferClosingBalance;
  }

  public void setTransferClosingBalance(Boolean transferClosingBalance) {
    this.transferClosingBalance = transferClosingBalance;
  }

  public FinancialYear auditDetails(Auditable auditDetails) {
    this.auditDetails = auditDetails;
    return this;
  }

   /**
   * Get auditDetails
   * @return auditDetails
  **/
  @ApiModelProperty(value = "")

  @Valid

  public Auditable getAuditDetails() {
    return auditDetails;
  }

  public void setAuditDetails(Auditable auditDetails) {
    this.auditDetails = auditDetails;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FinancialYear financialYear = (FinancialYear) o;
    return Objects.equals(this.id, financialYear.id) &&
        Objects.equals(this.finYearRange, financialYear.finYearRange) &&
        Objects.equals(this.startingDate, financialYear.startingDate) &&
        Objects.equals(this.endingDate, financialYear.endingDate) &&
        Objects.equals(this.active, financialYear.active) &&
        Objects.equals(this.isActiveForPosting, financialYear.isActiveForPosting) &&
        Objects.equals(this.isClosed, financialYear.isClosed) &&
        Objects.equals(this.transferClosingBalance, financialYear.transferClosingBalance) &&
        Objects.equals(this.auditDetails, financialYear.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, finYearRange, startingDate, endingDate, active, isActiveForPosting, isClosed, transferClosingBalance, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FinancialYear {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    finYearRange: ").append(toIndentedString(finYearRange)).append("\n");
    sb.append("    startingDate: ").append(toIndentedString(startingDate)).append("\n");
    sb.append("    endingDate: ").append(toIndentedString(endingDate)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    isActiveForPosting: ").append(toIndentedString(isActiveForPosting)).append("\n");
    sb.append("    isClosed: ").append(toIndentedString(isClosed)).append("\n");
    sb.append("    transferClosingBalance: ").append(toIndentedString(transferClosingBalance)).append("\n");
    sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

