package org.egov.inv.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This object holds information about materials from different suppliers.
 */
@ApiModel(description = "This object holds information about materials from different suppliers. ")
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceListDetails   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;
  
  @JsonProperty("priceList")
  private String priceList = null;

  @JsonProperty("material")
  private Material material = null;

  @JsonProperty("fromDate")
  private Long fromDate = null;

  @JsonProperty("toDate")
  private Long toDate = null;

  @JsonProperty("ratePerUnit")
  private Double ratePerUnit = null;

  @JsonProperty("quantity")
  private Double quantity = null;
  
  @JsonProperty("tenderUsedQuantity")
  private BigDecimal tenderUsedQuantity = null;

  @JsonProperty("uom")
  private Uom uom = null;

  @JsonProperty("active")
  private Boolean active = null;

  @JsonProperty("deleted")
  private Boolean deleted = null;
  
  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public PriceListDetails id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the material. 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the material. ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public PriceListDetails tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the material
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenant id of the material")

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }
  
  public PriceListDetails priceList(String priceList) {
	    this.priceList = priceList;
	    return this;
	  }

	   /**
	   * PriceList Id of the header
	   * @return tenantId
	  **/
	  @ApiModelProperty(value = "Pricelist id of the header")

	 @Size(min=2,max=128)
	  public String getPriceList() {
	    return priceList;
	  }

	  public void setPriceList(String priceList) {
	    this.priceList = priceList;
	  }

  public PriceListDetails material(Material material) {
    this.material = material;
    return this;
  }

   /**
   * name of a material stored in the system
   * @return material
  **/
  @ApiModelProperty(value = "name of a material stored in the system")

  public Material getMaterial() {
    return material;
  }

  public void setMaterial(Material material) {
    this.material = material;
  }

  public PriceListDetails fromDate(Long fromDate) {
    this.fromDate = fromDate;
    return this;
  }

   /**
   * date from which the material becomes active in the system 
   * @return fromDate
  **/
  @ApiModelProperty(value = "date from which the material becomes active in the system ")


  public Long getFromDate() {
    return fromDate;
  }

  public void setFromDate(Long fromDate) {
    this.fromDate = fromDate;
  }

  public PriceListDetails toDate(Long toDate) {
    this.toDate = toDate;
    return this;
  }

   /**
   * date upto which the material will be active in the system 
   * @return toDate
  **/
  @ApiModelProperty(value = "date upto which the material will be active in the system ")


  public Long getToDate() {
    return toDate;
  }

  public void setToDate(Long toDate) {
    this.toDate = toDate;
  }

  public PriceListDetails ratePerUnit(Double ratePerUnit) {
    this.ratePerUnit = ratePerUnit;
    return this;
  }

   /**
   * Get ratePerUnit
   * @return ratePerUnit
  **/
  @ApiModelProperty(value = "")


  public Double getRatePerUnit() {
    return ratePerUnit;
  }

  public void setRatePerUnit(Double ratePerUnit) {
    this.ratePerUnit = ratePerUnit;
  }

  public PriceListDetails quantity(Double quantity) {
    this.quantity = quantity;
    return this;
  }

   /**
   * Get quantity
   * @return quantity
  **/
  @ApiModelProperty(value = "")


  public Double getQuantity() {
    return quantity;
  }

  public void setQuantity(Double quantity) {
    this.quantity = quantity;
  }


  public PriceListDetails tenderUsedQuantity(BigDecimal tenderUsedQuantity) {
    this.tenderUsedQuantity = tenderUsedQuantity;
    return this;
  }

  /**
   * Get tenderUsedQuantity
   * @return tenderUsedQuantity
  **/
  @ApiModelProperty(value = "")


  public BigDecimal getTenderUsedQuantity() {
    return tenderUsedQuantity;
  }

  public void setTenderUsedQuantity(BigDecimal tenderUsedQuantity) {
    this.tenderUsedQuantity = tenderUsedQuantity;
  }
  
  public PriceListDetails uom(Uom uom) {
    this.uom = uom;
    return this;
  }

   /**
   * Get uom
   * @return uom
  **/
  @ApiModelProperty(value = "")

  @Valid

  public Uom getUom() {
    return uom;
  }

  public void setUom(Uom uom) {
    this.uom = uom;
  }

  public PriceListDetails active(Boolean active) {
    this.active = active;
    return this;
  }

   /**
   * Active or inactive flag of the material in the system
   * @return active
  **/
  @ApiModelProperty(value = "Active or inactive flag of the material in the system")


  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }
  
  public PriceListDetails deleted(Boolean deleted) {
	    this.deleted = deleted;
	    return this;
	  }

  /**
   * Delete flag of the material in the system
   * @return deleted
  **/
  @ApiModelProperty(value = "Delete flag of the material in the system")


  public Boolean getDeleted() {
    return deleted;
  }

  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }

  public PriceListDetails auditDetails(AuditDetails auditDetails) {
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
    PriceListDetails priceListDetails = (PriceListDetails) o;
    return Objects.equals(this.id, priceListDetails.id) &&
        Objects.equals(this.tenantId, priceListDetails.tenantId) &&
        Objects.equals(this.material, priceListDetails.material) &&
        Objects.equals(this.fromDate, priceListDetails.fromDate) &&
        Objects.equals(this.toDate, priceListDetails.toDate) &&
        Objects.equals(this.ratePerUnit, priceListDetails.ratePerUnit) &&
        Objects.equals(this.quantity, priceListDetails.quantity) &&
        Objects.equals(this.tenderUsedQuantity, priceListDetails.tenderUsedQuantity) &&
        Objects.equals(this.uom, priceListDetails.uom) &&
        Objects.equals(this.active, priceListDetails.active) &&
        Objects.equals(this.auditDetails, priceListDetails.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, material, fromDate, toDate, ratePerUnit, quantity, tenderUsedQuantity, uom, active, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PriceListDetails {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    material: ").append(toIndentedString(material)).append("\n");
    sb.append("    fromDate: ").append(toIndentedString(fromDate)).append("\n");
    sb.append("    toDate: ").append(toIndentedString(toDate)).append("\n");
    sb.append("    ratePerUnit: ").append(toIndentedString(ratePerUnit)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    tenderUsedQuantity: ").append(toIndentedString(tenderUsedQuantity)).append("\n");
    sb.append("    uom: ").append(toIndentedString(uom)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
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

