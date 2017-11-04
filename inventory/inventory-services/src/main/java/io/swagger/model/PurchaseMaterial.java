package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.AuditDetails;
import io.swagger.model.Material;
import io.swagger.model.Uom;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * 
 */
@ApiModel(description = "")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class PurchaseMaterial   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("material")
  private Material material = null;

  @JsonProperty("uom")
  private Uom uom = null;

  @JsonProperty("orderQuantity")
  private BigDecimal orderQuantity = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public PurchaseMaterial id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Purchase Material 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Purchase Material ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public PurchaseMaterial tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Purchase Material
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenant id of the Purchase Material")

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public PurchaseMaterial material(Material material) {
    this.material = material;
    return this;
  }

   /**
   * Get material
   * @return material
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public Material getMaterial() {
    return material;
  }

  public void setMaterial(Material material) {
    this.material = material;
  }

  public PurchaseMaterial uom(Uom uom) {
    this.uom = uom;
    return this;
  }

   /**
   * Get uom
   * @return uom
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public Uom getUom() {
    return uom;
  }

  public void setUom(Uom uom) {
    this.uom = uom;
  }

  public PurchaseMaterial orderQuantity(BigDecimal orderQuantity) {
    this.orderQuantity = orderQuantity;
    return this;
  }

   /**
   * order quantity of the PurchaseMaterial 
   * @return orderQuantity
  **/
  @ApiModelProperty(required = true, value = "order quantity of the PurchaseMaterial ")
  @NotNull

  @Valid

  public BigDecimal getOrderQuantity() {
    return orderQuantity;
  }

  public void setOrderQuantity(BigDecimal orderQuantity) {
    this.orderQuantity = orderQuantity;
  }

  public PurchaseMaterial description(String description) {
    this.description = description;
    return this;
  }

   /**
   * remarks of the PurchaseMaterial 
   * @return description
  **/
  @ApiModelProperty(value = "remarks of the PurchaseMaterial ")

 @Size(max=1000)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public PurchaseMaterial auditDetails(AuditDetails auditDetails) {
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
    PurchaseMaterial purchaseMaterial = (PurchaseMaterial) o;
    return Objects.equals(this.id, purchaseMaterial.id) &&
        Objects.equals(this.tenantId, purchaseMaterial.tenantId) &&
        Objects.equals(this.material, purchaseMaterial.material) &&
        Objects.equals(this.uom, purchaseMaterial.uom) &&
        Objects.equals(this.orderQuantity, purchaseMaterial.orderQuantity) &&
        Objects.equals(this.description, purchaseMaterial.description) &&
        Objects.equals(this.auditDetails, purchaseMaterial.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, material, uom, orderQuantity, description, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PurchaseMaterial {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    material: ").append(toIndentedString(material)).append("\n");
    sb.append("    uom: ").append(toIndentedString(uom)).append("\n");
    sb.append("    orderQuantity: ").append(toIndentedString(orderQuantity)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

