package org.egov.inv.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.egov.inv.model.IndentDetail;
import org.egov.inv.model.Material;
import org.egov.inv.model.MaterialReceipt;
import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * This object holds the materail issue detail information for both indent and non indent. 
 */
@ApiModel(description = "This object holds the materail issue detail information for both indent and non indent. ")
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

public class MaterialIssueDetails   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("indentDetail")
  private IndentDetail indentDetail = null;

  @JsonProperty("quantityToBeIssued")
  private BigDecimal quantityToBeIssued = null;

  @JsonProperty("materialReceipt")
  private MaterialReceipt materialReceipt = null;

  @JsonProperty("material")
  private Material material = null;

  @JsonProperty("quantityIssued")
  private BigDecimal quantityIssued = null;

  @JsonProperty("description")
  private String description = null;

  public MaterialIssueDetails id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Material Issue Details 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Material Issue Details ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public MaterialIssueDetails tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Material Issue Details
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenant id of the Material Issue Details")

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public MaterialIssueDetails indentDetail(IndentDetail indentDetail) {
    this.indentDetail = indentDetail;
    return this;
  }

   /**
   * Applicable for Indent Issue. Balance quantity to be issued of the IndentDetails. 
   * @return indentDetail
  **/
  @ApiModelProperty(value = "Applicable for Indent Issue. Balance quantity to be issued of the IndentDetails. ")

  @Valid

  public IndentDetail getIndentDetail() {
    return indentDetail;
  }

  public void setIndentDetail(IndentDetail indentDetail) {
    this.indentDetail = indentDetail;
  }

  public MaterialIssueDetails quantityToBeIssued(BigDecimal quantityToBeIssued) {
    this.quantityToBeIssued = quantityToBeIssued;
    return this;
  }

   /**
   * Balance quantity to be issued of the IndentDetails. 
   * @return quantityToBeIssued
  **/
  @ApiModelProperty(value = "Balance quantity to be issued of the IndentDetails. ")

  @Valid

  public BigDecimal getQuantityToBeIssued() {
    return quantityToBeIssued;
  }

  public void setQuantityToBeIssued(BigDecimal quantityToBeIssued) {
    this.quantityToBeIssued = quantityToBeIssued;
  }

  public MaterialIssueDetails materialReceipt(MaterialReceipt materialReceipt) {
    this.materialReceipt = materialReceipt;
    return this;
  }

   /**
   * Applicable for Non Indent Issue.
   * @return materialReceipt
  **/
  @ApiModelProperty(value = "Applicable for Non Indent Issue.")

  @Valid

  public MaterialReceipt getMaterialReceipt() {
    return materialReceipt;
  }

  public void setMaterialReceipt(MaterialReceipt materialReceipt) {
    this.materialReceipt = materialReceipt;
  }

  public MaterialIssueDetails material(Material material) {
    this.material = material;
    return this;
  }

   /**
   * Applicable for Non Indent Issue. 
   * @return material
  **/
  @ApiModelProperty(value = "Applicable for Non Indent Issue. ")

  @Valid

  public Material getMaterial() {
    return material;
  }

  public void setMaterial(Material material) {
    this.material = material;
  }

  public MaterialIssueDetails quantityIssued(BigDecimal quantityIssued) {
    this.quantityIssued = quantityIssued;
    return this;
  }

   /**
   * Quantity issued of the Material Issue Detail. 
   * @return quantityIssued
  **/
  @ApiModelProperty(required = true, value = "Quantity issued of the Material Issue Detail. ")
  @NotNull

  @Valid

  public BigDecimal getQuantityIssued() {
    return quantityIssued;
  }

  public void setQuantityIssued(BigDecimal quantityIssued) {
    this.quantityIssued = quantityIssued;
  }

  public MaterialIssueDetails description(String description) {
    this.description = description;
    return this;
  }

   /**
   * description of the Material Issue Detail. 
   * @return description
  **/
  @ApiModelProperty(value = "description of the Material Issue Detail. ")

 @Size(max=1000)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaterialIssueDetails materialIssueDetails = (MaterialIssueDetails) o;
    return Objects.equals(this.id, materialIssueDetails.id) &&
        Objects.equals(this.tenantId, materialIssueDetails.tenantId) &&
        Objects.equals(this.indentDetail, materialIssueDetails.indentDetail) &&
        Objects.equals(this.quantityToBeIssued, materialIssueDetails.quantityToBeIssued) &&
        Objects.equals(this.materialReceipt, materialIssueDetails.materialReceipt) &&
        Objects.equals(this.material, materialIssueDetails.material) &&
        Objects.equals(this.quantityIssued, materialIssueDetails.quantityIssued) &&
        Objects.equals(this.description, materialIssueDetails.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, indentDetail, quantityToBeIssued, materialReceipt, material, quantityIssued, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaterialIssueDetails {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    indentDetail: ").append(toIndentedString(indentDetail)).append("\n");
    sb.append("    quantityToBeIssued: ").append(toIndentedString(quantityToBeIssued)).append("\n");
    sb.append("    materialReceipt: ").append(toIndentedString(materialReceipt)).append("\n");
    sb.append("    material: ").append(toIndentedString(material)).append("\n");
    sb.append("    quantityIssued: ").append(toIndentedString(quantityIssued)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

