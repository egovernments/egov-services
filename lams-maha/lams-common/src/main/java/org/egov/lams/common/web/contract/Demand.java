package org.egov.lams.common.web.contract;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * A Object which holds the basic info about the revenue assessment for which the demand is generated like module name, consumercode, owner, etc.
 */
@ApiModel(description = "A Object which holds the basic info about the revenue assessment for which the demand is generated like module name, consumercode, owner, etc.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-09T07:10:49.937Z")

public class Demand   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("consumerCode")
  private String consumerCode = null;

  @JsonProperty("consumerType")
  private String consumerType = null;

  @JsonProperty("businessService")
  private String businessService = null;

  @JsonProperty("owner")
  private User owner = null;

  @JsonProperty("taxPeriodFrom")
  private LocalDate taxPeriodFrom = null;

  @JsonProperty("taxPeriodTo")
  private LocalDate taxPeriodTo = null;

  @JsonProperty("demandDetails")
  private List<DemandDetails> demandDetails = new ArrayList<DemandDetails>();

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  @JsonProperty("minimumAmountPayable")
  private Double minimumAmountPayable = null;

  public Demand id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * primary key of a demand.
   * @return id
  **/
  @ApiModelProperty(value = "primary key of a demand.")


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Demand tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Unique Identifier of the tenant
   * @return tenantId
  **/
  @ApiModelProperty(required = true, readOnly = true, value = "Unique Identifier of the tenant")
  @NotNull

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public Demand consumerCode(String consumerCode) {
    this.consumerCode = consumerCode;
    return this;
  }

   /**
   * Unique identification of billing entity.
   * @return consumerCode
  **/
  @ApiModelProperty(required = true, value = "Unique identification of billing entity.")
  @NotNull


  public String getConsumerCode() {
    return consumerCode;
  }

  public void setConsumerCode(String consumerCode) {
    this.consumerCode = consumerCode;
  }

  public Demand consumerType(String consumerType) {
    this.consumerType = consumerType;
    return this;
  }

   /**
   * Consumer type of billing entity.
   * @return consumerType
  **/
  @ApiModelProperty(value = "Consumer type of billing entity.")


  public String getConsumerType() {
    return consumerType;
  }

  public void setConsumerType(String consumerType) {
    this.consumerType = consumerType;
  }

  public Demand businessService(String businessService) {
    this.businessService = businessService;
    return this;
  }

   /**
   * Billing system e.g., Property Tax, Water Charges etc.
   * @return businessService
  **/
  @ApiModelProperty(required = true, value = "Billing system e.g., Property Tax, Water Charges etc.")
  @NotNull

 @Size(min=4,max=256)
  public String getBusinessService() {
    return businessService;
  }

  public void setBusinessService(String businessService) {
    this.businessService = businessService;
  }

  public Demand owner(User owner) {
    this.owner = owner;
    return this;
  }

   /**
   * Get owner
   * @return owner
  **/
  @ApiModelProperty(value = "")

  @Valid

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public Demand taxPeriodFrom(LocalDate taxPeriodFrom) {
    this.taxPeriodFrom = taxPeriodFrom;
    return this;
  }

   /**
   * start date from when the tax is applicable. Date is inclduing timestamp, dd/MM/yyyy hh24:mi:ss
   * @return taxPeriodFrom
  **/
  @ApiModelProperty(required = true, value = "start date from when the tax is applicable. Date is inclduing timestamp, dd/MM/yyyy hh24:mi:ss")
  @NotNull

  @Valid

  public LocalDate getTaxPeriodFrom() {
    return taxPeriodFrom;
  }

  public void setTaxPeriodFrom(LocalDate taxPeriodFrom) {
    this.taxPeriodFrom = taxPeriodFrom;
  }

  public Demand taxPeriodTo(LocalDate taxPeriodTo) {
    this.taxPeriodTo = taxPeriodTo;
    return this;
  }

   /**
   * end date till when the tax is applicable. Date is inclduing timestamp, dd/MM/yyyy hh24:mi:ss
   * @return taxPeriodTo
  **/
  @ApiModelProperty(required = true, value = "end date till when the tax is applicable. Date is inclduing timestamp, dd/MM/yyyy hh24:mi:ss")
  @NotNull

  @Valid

  public LocalDate getTaxPeriodTo() {
    return taxPeriodTo;
  }

  public void setTaxPeriodTo(LocalDate taxPeriodTo) {
    this.taxPeriodTo = taxPeriodTo;
  }

  public Demand demandDetails(List<DemandDetails> demandDetails) {
    this.demandDetails = demandDetails;
    return this;
  }

  public Demand addDemandDetailsItem(DemandDetails demandDetailsItem) {
    this.demandDetails.add(demandDetailsItem);
    return this;
  }

   /**
   * Get demandDetails
   * @return demandDetails
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public List<DemandDetails> getDemandDetails() {
    return demandDetails;
  }

  public void setDemandDetails(List<DemandDetails> demandDetails) {
    this.demandDetails = demandDetails;
  }

  public Demand auditDetails(AuditDetails auditDetails) {
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

  public Demand minimumAmountPayable(Double minimumAmountPayable) {
    this.minimumAmountPayable = minimumAmountPayable;
    return this;
  }

   /**
   * Minimum bill amount to be paid.
   * @return minimumAmountPayable
  **/
  @ApiModelProperty(required = true, value = "Minimum bill amount to be paid.")
  @NotNull


  public Double getMinimumAmountPayable() {
    return minimumAmountPayable;
  }

  public void setMinimumAmountPayable(Double minimumAmountPayable) {
    this.minimumAmountPayable = minimumAmountPayable;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Demand demand = (Demand) o;
    return Objects.equals(this.id, demand.id) &&
        Objects.equals(this.tenantId, demand.tenantId) &&
        Objects.equals(this.consumerCode, demand.consumerCode) &&
        Objects.equals(this.consumerType, demand.consumerType) &&
        Objects.equals(this.businessService, demand.businessService) &&
        Objects.equals(this.owner, demand.owner) &&
        Objects.equals(this.taxPeriodFrom, demand.taxPeriodFrom) &&
        Objects.equals(this.taxPeriodTo, demand.taxPeriodTo) &&
        Objects.equals(this.demandDetails, demand.demandDetails) &&
        Objects.equals(this.auditDetails, demand.auditDetails) &&
        Objects.equals(this.minimumAmountPayable, demand.minimumAmountPayable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, consumerCode, consumerType, businessService, owner, taxPeriodFrom, taxPeriodTo, demandDetails, auditDetails, minimumAmountPayable);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Demand {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    consumerCode: ").append(toIndentedString(consumerCode)).append("\n");
    sb.append("    consumerType: ").append(toIndentedString(consumerType)).append("\n");
    sb.append("    businessService: ").append(toIndentedString(businessService)).append("\n");
    sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
    sb.append("    taxPeriodFrom: ").append(toIndentedString(taxPeriodFrom)).append("\n");
    sb.append("    taxPeriodTo: ").append(toIndentedString(taxPeriodTo)).append("\n");
    sb.append("    demandDetails: ").append(toIndentedString(demandDetails)).append("\n");
    sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
    sb.append("    minimumAmountPayable: ").append(toIndentedString(minimumAmountPayable)).append("\n");
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

