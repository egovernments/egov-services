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

public class IndentDetail   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("material")
  private Material material = null;

  @JsonProperty("uom")
  private Uom uom = null;

  @JsonProperty("projectCode")
  private String projectCode = null;

  @JsonProperty("assetCode")
  private String assetCode = null;

  @JsonProperty("indentQantity")
  private BigDecimal indentQantity = null;

  @JsonProperty("issuedQantity")
  private BigDecimal issuedQantity = null;

  @JsonProperty("receivedQuantity")
  private BigDecimal receivedQuantity = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public IndentDetail id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Indent Detail 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Indent Detail ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public IndentDetail tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Indent Detail
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenant id of the Indent Detail")

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public IndentDetail material(Material material) {
    this.material = material;
    return this;
  }

   /**
   * Get material
   * @return material
  **/
  @ApiModelProperty(value = "")

  @Valid

  public Material getMaterial() {
    return material;
  }

  public void setMaterial(Material material) {
    this.material = material;
  }

  public IndentDetail uom(Uom uom) {
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

  public IndentDetail projectCode(String projectCode) {
    this.projectCode = projectCode;
    return this;
  }

   /**
   * projectCode  If case the purpose is Capital 
   * @return projectCode
  **/
  @ApiModelProperty(value = "projectCode  If case the purpose is Capital ")


  public String getProjectCode() {
    return projectCode;
  }

  public void setProjectCode(String projectCode) {
    this.projectCode = projectCode;
  }

  public IndentDetail assetCode(String assetCode) {
    this.assetCode = assetCode;
    return this;
  }

   /**
   * assetCode  If the purpose is Repair and Maintenance 
   * @return assetCode
  **/
  @ApiModelProperty(value = "assetCode  If the purpose is Repair and Maintenance ")


  public String getAssetCode() {
    return assetCode;
  }

  public void setAssetCode(String assetCode) {
    this.assetCode = assetCode;
  }

  public IndentDetail indentQantity(BigDecimal indentQantity) {
    this.indentQantity = indentQantity;
    return this;
  }

   /**
   * indent qantity of the IndentDetail 
   * @return indentQantity
  **/
  @ApiModelProperty(value = "indent qantity of the IndentDetail ")

  @Valid

  public BigDecimal getIndentQantity() {
    return indentQantity;
  }

  public void setIndentQantity(BigDecimal indentQantity) {
    this.indentQantity = indentQantity;
  }

  public IndentDetail issuedQantity(BigDecimal issuedQantity) {
    this.issuedQantity = issuedQantity;
    return this;
  }

   /**
   * issuedQantity  Applicable for Material Issue and Material Transfer Indent 
   * @return issuedQantity
  **/
  @ApiModelProperty(value = "issuedQantity  Applicable for Material Issue and Material Transfer Indent ")

  @Valid

  public BigDecimal getIssuedQantity() {
    return issuedQantity;
  }

  public void setIssuedQantity(BigDecimal issuedQantity) {
    this.issuedQantity = issuedQantity;
  }

  public IndentDetail receivedQuantity(BigDecimal receivedQuantity) {
    this.receivedQuantity = receivedQuantity;
    return this;
  }

   /**
   * receivedQuantity  Applicable for Material Receipt Note and Material Transfer Inward Note 
   * @return receivedQuantity
  **/
  @ApiModelProperty(value = "receivedQuantity  Applicable for Material Receipt Note and Material Transfer Inward Note ")

  @Valid

  public BigDecimal getReceivedQuantity() {
    return receivedQuantity;
  }

  public void setReceivedQuantity(BigDecimal receivedQuantity) {
    this.receivedQuantity = receivedQuantity;
  }

  public IndentDetail description(String description) {
    this.description = description;
    return this;
  }

   /**
   * remarks of the IndentDetail 
   * @return description
  **/
  @ApiModelProperty(value = "remarks of the IndentDetail ")

 @Size(max=1000)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public IndentDetail auditDetails(AuditDetails auditDetails) {
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
    IndentDetail indentDetail = (IndentDetail) o;
    return Objects.equals(this.id, indentDetail.id) &&
        Objects.equals(this.tenantId, indentDetail.tenantId) &&
        Objects.equals(this.material, indentDetail.material) &&
        Objects.equals(this.uom, indentDetail.uom) &&
        Objects.equals(this.projectCode, indentDetail.projectCode) &&
        Objects.equals(this.assetCode, indentDetail.assetCode) &&
        Objects.equals(this.indentQantity, indentDetail.indentQantity) &&
        Objects.equals(this.issuedQantity, indentDetail.issuedQantity) &&
        Objects.equals(this.receivedQuantity, indentDetail.receivedQuantity) &&
        Objects.equals(this.description, indentDetail.description) &&
        Objects.equals(this.auditDetails, indentDetail.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, material, uom, projectCode, assetCode, indentQantity, issuedQantity, receivedQuantity, description, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndentDetail {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    material: ").append(toIndentedString(material)).append("\n");
    sb.append("    uom: ").append(toIndentedString(uom)).append("\n");
    sb.append("    projectCode: ").append(toIndentedString(projectCode)).append("\n");
    sb.append("    assetCode: ").append(toIndentedString(assetCode)).append("\n");
    sb.append("    indentQantity: ").append(toIndentedString(indentQantity)).append("\n");
    sb.append("    issuedQantity: ").append(toIndentedString(issuedQantity)).append("\n");
    sb.append("    receivedQuantity: ").append(toIndentedString(receivedQuantity)).append("\n");
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

