package org.egov.inv.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * This object holds the pricelist information of different materials from various suppliers   
 */
@ApiModel(description = "This object holds the pricelist information of different materials from various suppliers   ")
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceList   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("supplier")
  private Supplier supplier = null;

  /**
   * type of the information about the material we are getting from the supplier 
   */
  public enum RateTypeEnum {
    DGSC_RATE_CONTRACT("DGSC Rate Contract"),
    
    ULB_RATE_CONTRACT("ULB Rate Contract"),
    
    ONE_TIME_TENDER("One Time Tender"),
    
    QUOTATION("Quotation");

    private String value;

    RateTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static RateTypeEnum fromValue(String text) {
      for (RateTypeEnum b : RateTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("rateType")
  private RateTypeEnum rateType = null;

  @JsonProperty("rateContractNumber")
  private String rateContractNumber = null;

  @JsonProperty("rateContractDate")
  private Long rateContractDate = null;

  @JsonProperty("agreementNumber")
  private String agreementNumber = null;

  @JsonProperty("agreementDate")
  private Long agreementDate = null;

  @JsonProperty("agreementStartDate")
  private Long agreementStartDate = null;

  @JsonProperty("agreementEndDate")
  private Long agreementEndDate = null;

  @JsonProperty("active")
  private Boolean active = true;

  @JsonProperty("fileStoreId")
  private String fileStoreId = null;

  @JsonProperty("priceListDetails")
  private List<PriceListDetails> priceListDetails = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public PriceList id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the PriceList record. 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the PriceList record. ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public PriceList tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the PriceList
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenant id of the PriceList")

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public PriceList supplier(Supplier supplier) {
    this.supplier = supplier;
    return this;
  }

   /**
   * Name of the vendor supplying materials required 
   * @return supplier
  **/
  @ApiModelProperty(required = true, value = "Name of the vendor supplying materials required ")
  @NotNull

  public Supplier getSupplier() {
    return supplier;
  }

  public void setSupplier(Supplier supplier) {
    this.supplier = supplier;
  }

  public PriceList rateType(RateTypeEnum rateType) {
    this.rateType = rateType;
    return this;
  }

   /**
   * type of the information about the material we are getting from the supplier 
   * @return rateType
  **/
  @ApiModelProperty(required = true, value = "type of the information about the material we are getting from the supplier ")
  @NotNull


  public RateTypeEnum getRateType() {
    return rateType;
  }

  public void setRateType(RateTypeEnum rateType) {
    this.rateType = rateType;
  }

  public PriceList rateContractNumber(String rateContractNumber) {
    this.rateContractNumber = rateContractNumber;
    return this;
  }

   /**
   * reference no of the material contract from the supplier 
   * @return rateContractNumber
  **/
  @ApiModelProperty(required = true, value = "reference no of the material contract from the supplier ")
  @NotNull


  public String getRateContractNumber() {
    return rateContractNumber;
  }

  public void setRateContractNumber(String rateContractNumber) {
    this.rateContractNumber = rateContractNumber;
  }

  public PriceList rateContractDate(Long rateContractDate) {
    this.rateContractDate = rateContractDate;
    return this;
  }

   /**
   * contract date of the rate for item with the supplier.Date in epoc format. 
   * @return rateContractDate
  **/
  @ApiModelProperty(required = true, value = "contract date of the rate for item with the supplier.Date in epoc format. ")
  @NotNull


  public Long getRateContractDate() {
    return rateContractDate;
  }

  public void setRateContractDate(Long rateContractDate) {
    this.rateContractDate = rateContractDate;
  }

  public PriceList agreementNumber(String agreementNumber) {
    this.agreementNumber = agreementNumber;
    return this;
  }

   /**
   * Agreement no with the supplier of materials 
   * @return agreementNumber
  **/
  @ApiModelProperty(required = true, value = "Agreement no with the supplier of materials ")
  @NotNull


  public String getAgreementNumber() {
    return agreementNumber;
  }

  public void setAgreementNumber(String agreementNumber) {
    this.agreementNumber = agreementNumber;
  }

  public PriceList agreementDate(Long agreementDate) {
    this.agreementDate = agreementDate;
    return this;
  }

   /**
   * Date on which agreement done with supplier 
   * @return agreementDate
  **/
  @ApiModelProperty(required = true, value = "Date on which agreement done with supplier ")
  @NotNull


  public Long getAgreementDate() {
    return agreementDate;
  }

  public void setAgreementDate(Long agreementDate) {
    this.agreementDate = agreementDate;
  }

  public PriceList agreementStartDate(Long agreementStartDate) {
    this.agreementStartDate = agreementStartDate;
    return this;
  }

   /**
   * Date from which the agreement is valid with supplier 
   * @return agreementStartDate
  **/
  @ApiModelProperty(required = true, value = "Date from which the agreement is valid with supplier ")
  @NotNull


  public Long getAgreementStartDate() {
    return agreementStartDate;
  }

  public void setAgreementStartDate(Long agreementStartDate) {
    this.agreementStartDate = agreementStartDate;
  }

  public PriceList agreementEndDate(Long agreementEndDate) {
    this.agreementEndDate = agreementEndDate;
    return this;
  }

   /**
   * Date on which the agreement expires with the supplier 
   * @return agreementEndDate
  **/
  @ApiModelProperty(required = true, value = "Date on which the agreement expires with the supplier ")
  @NotNull


  public Long getAgreementEndDate() {
    return agreementEndDate;
  }

  public void setAgreementEndDate(Long agreementEndDate) {
    this.agreementEndDate = agreementEndDate;
  }

  public PriceList active(Boolean active) {
    this.active = active;
    return this;
  }

   /**
   * active or inactive flag of the supplier price detail 
   * @return active
  **/
  @ApiModelProperty(required = true, value = "active or inactive flag of the supplier price detail ")
  @NotNull


  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public PriceList fileStoreId(String fileStoreId) {
    this.fileStoreId = fileStoreId;
    return this;
  }

   /**
   * File Store id of the rate contract with the supplier 
   * @return fileStoreId
  **/
  @ApiModelProperty(value = "File Store id of the rate contract with the supplier ")


  public String getFileStoreId() {
    return fileStoreId;
  }

  public void setFileStoreId(String fileStoreId) {
    this.fileStoreId = fileStoreId;
  }

  public PriceList priceListDetails(List<PriceListDetails> priceListDetails) {
    this.priceListDetails = priceListDetails;
    return this;
  }

  public PriceList addPriceListDetailsItem(PriceListDetails priceListDetailsItem) {
    if (this.priceListDetails == null) {
      this.priceListDetails = new ArrayList<PriceListDetails>();
    }
    this.priceListDetails.add(priceListDetailsItem);
    return this;
  }

   /**
   * Get priceListDetails
   * @return priceListDetails
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<PriceListDetails> getPriceListDetails() {
    return priceListDetails;
  }

  public void setPriceListDetails(List<PriceListDetails> priceListDetails) {
    this.priceListDetails = priceListDetails;
  }

  public PriceList auditDetails(AuditDetails auditDetails) {
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
    PriceList priceList = (PriceList) o;
    return Objects.equals(this.id, priceList.id) &&
        Objects.equals(this.tenantId, priceList.tenantId) &&
        Objects.equals(this.supplier, priceList.supplier) &&
        Objects.equals(this.rateType, priceList.rateType) &&
        Objects.equals(this.rateContractNumber, priceList.rateContractNumber) &&
        Objects.equals(this.rateContractDate, priceList.rateContractDate) &&
        Objects.equals(this.agreementNumber, priceList.agreementNumber) &&
        Objects.equals(this.agreementDate, priceList.agreementDate) &&
        Objects.equals(this.agreementStartDate, priceList.agreementStartDate) &&
        Objects.equals(this.agreementEndDate, priceList.agreementEndDate) &&
        Objects.equals(this.active, priceList.active) &&
        Objects.equals(this.fileStoreId, priceList.fileStoreId) &&
        Objects.equals(this.priceListDetails, priceList.priceListDetails) &&
        Objects.equals(this.auditDetails, priceList.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, supplier, rateType, rateContractNumber, rateContractDate, agreementNumber, agreementDate, agreementStartDate, agreementEndDate, active, fileStoreId, priceListDetails, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PriceList {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    supplier: ").append(toIndentedString(supplier)).append("\n");
    sb.append("    rateType: ").append(toIndentedString(rateType)).append("\n");
    sb.append("    rateContractNumber: ").append(toIndentedString(rateContractNumber)).append("\n");
    sb.append("    rateContractDate: ").append(toIndentedString(rateContractDate)).append("\n");
    sb.append("    agreementNumber: ").append(toIndentedString(agreementNumber)).append("\n");
    sb.append("    agreementDate: ").append(toIndentedString(agreementDate)).append("\n");
    sb.append("    agreementStartDate: ").append(toIndentedString(agreementStartDate)).append("\n");
    sb.append("    agreementEndDate: ").append(toIndentedString(agreementEndDate)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    fileStoreId: ").append(toIndentedString(fileStoreId)).append("\n");
    sb.append("    priceListDetails: ").append(toIndentedString(priceListDetails)).append("\n");
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

