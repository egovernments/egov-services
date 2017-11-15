package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.AuditDetails;
import io.swagger.model.Material;
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

public class ScrapDetails   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("material")
  private Material material = null;

  @JsonProperty("lotNumber")
  private String lotNumber = null;

  /**
   * scrap reason of the ScrapDetails 
   */
  public enum ScrapReasonEnum {
    DAMAGED("Damaged"),
    
    NOTINUSE("NotInUse"),
    
    NOTFITFORUSE("NotFitforUse"),
    
    EXPIRED("Expired"),
    
    STOCKCLEARED("StockCleared"),
    
    DEADSTOCK("DeadStock");

    private String value;

    ScrapReasonEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ScrapReasonEnum fromValue(String text) {
      for (ScrapReasonEnum b : ScrapReasonEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("scrapReason")
  private ScrapReasonEnum scrapReason = null;

  @JsonProperty("scrapQuantity")
  private BigDecimal scrapQuantity = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public ScrapDetails id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Scrap Details 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Scrap Details ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ScrapDetails tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Scrap Details
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenant id of the Scrap Details")

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public ScrapDetails material(Material material) {
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

  public ScrapDetails lotNumber(String lotNumber) {
    this.lotNumber = lotNumber;
    return this;
  }

   /**
   * lot number of the ScrapDetails 
   * @return lotNumber
  **/
  @ApiModelProperty(required = true, value = "lot number of the ScrapDetails ")
  @NotNull


  public String getLotNumber() {
    return lotNumber;
  }

  public void setLotNumber(String lotNumber) {
    this.lotNumber = lotNumber;
  }

  public ScrapDetails scrapReason(ScrapReasonEnum scrapReason) {
    this.scrapReason = scrapReason;
    return this;
  }

   /**
   * scrap reason of the ScrapDetails 
   * @return scrapReason
  **/
  @ApiModelProperty(required = true, value = "scrap reason of the ScrapDetails ")
  @NotNull


  public ScrapReasonEnum getScrapReason() {
    return scrapReason;
  }

  public void setScrapReason(ScrapReasonEnum scrapReason) {
    this.scrapReason = scrapReason;
  }

  public ScrapDetails scrapQuantity(BigDecimal scrapQuantity) {
    this.scrapQuantity = scrapQuantity;
    return this;
  }

   /**
   * scrap quantity of the ScrapDetails 
   * @return scrapQuantity
  **/
  @ApiModelProperty(value = "scrap quantity of the ScrapDetails ")

  @Valid

  public BigDecimal getScrapQuantity() {
    return scrapQuantity;
  }

  public void setScrapQuantity(BigDecimal scrapQuantity) {
    this.scrapQuantity = scrapQuantity;
  }

  public ScrapDetails auditDetails(AuditDetails auditDetails) {
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
    ScrapDetails scrapDetails = (ScrapDetails) o;
    return Objects.equals(this.id, scrapDetails.id) &&
        Objects.equals(this.tenantId, scrapDetails.tenantId) &&
        Objects.equals(this.material, scrapDetails.material) &&
        Objects.equals(this.lotNumber, scrapDetails.lotNumber) &&
        Objects.equals(this.scrapReason, scrapDetails.scrapReason) &&
        Objects.equals(this.scrapQuantity, scrapDetails.scrapQuantity) &&
        Objects.equals(this.auditDetails, scrapDetails.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, material, lotNumber, scrapReason, scrapQuantity, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScrapDetails {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    material: ").append(toIndentedString(material)).append("\n");
    sb.append("    lotNumber: ").append(toIndentedString(lotNumber)).append("\n");
    sb.append("    scrapReason: ").append(toIndentedString(scrapReason)).append("\n");
    sb.append("    scrapQuantity: ").append(toIndentedString(scrapQuantity)).append("\n");
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

