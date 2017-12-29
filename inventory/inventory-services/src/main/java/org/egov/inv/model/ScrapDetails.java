package org.egov.inv.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.egov.inv.model.Material;
import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * This object holds the scrap details information.   
 */
@ApiModel(description = "This object holds the scrap details information.   ")
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

public class ScrapDetails   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("scrapNumber")
  private String scrapNumber = null;

  @JsonProperty("material")
  private Material material = null;

  @JsonProperty("uom")
  private Uom uom = null;

  @JsonProperty("receiptDetail")
  private MaterialReceiptDetail receiptDetail = null;

  @JsonProperty("issueDetail")
  private MaterialIssueDetail issueDetail = null;

  @JsonProperty("lotNumber")
  private String lotNumber = null;

  @JsonProperty("expiryDate")
  private Integer expiryDate = null;

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

  @JsonProperty("quantity")
  private BigDecimal quantity = null;

  @JsonProperty("disposalQuantity")
  private BigDecimal disposalQuantity = null;

  @JsonProperty("scrapValue")
  private BigDecimal scrapValue = null;

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

  public ScrapDetails scrapNumber(String scrapNumber) {
    this.scrapNumber = scrapNumber;
    return this;
  }

   /**
   * scrapNumber reference in scrap details. 
   * @return scrapNumber
  **/
  @ApiModelProperty(readOnly = true, value = "scrapNumber reference in scrap details. ")


  public String getScrapNumber() {
    return scrapNumber;
  }

  public void setScrapNumber(String scrapNumber) {
    this.scrapNumber = scrapNumber;
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

  public ScrapDetails uom(Uom uom) {
    this.uom = uom;
    return this;
  }

   /**
   * Auto populate unit of material from receipt or issue
   * @return uom
  **/
  @ApiModelProperty(value = "Auto populate unit of material from receipt or issue")

  @Valid

  public Uom getUom() {
    return uom;
  }

  public void setUom(Uom uom) {
    this.uom = uom;
  }

  public ScrapDetails receiptDetail(MaterialReceiptDetail receiptDetail) {
    this.receiptDetail = receiptDetail;
    return this;
  }

   /**
   * id reference from receipt detail.
   * @return receiptDetail
  **/
  @ApiModelProperty(value = "id reference from receipt detail.")

  @Valid

  public MaterialReceiptDetail getReceiptDetail() {
    return receiptDetail;
  }

  public void setReceiptDetail(MaterialReceiptDetail receiptDetail) {
    this.receiptDetail = receiptDetail;
  }

  public ScrapDetails issueDetail(MaterialIssueDetail issueDetail) {
    this.issueDetail = issueDetail;
    return this;
  }

   /**
   * id reference from material issue detail.
   * @return issueDetail
  **/
  @ApiModelProperty(value = "id reference from material issue detail.")

  @Valid

  public MaterialIssueDetail getIssueDetail() {
    return issueDetail;
  }

  public void setIssueDetail(MaterialIssueDetail issueDetail) {
    this.issueDetail = issueDetail;
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

  public ScrapDetails expiryDate(Integer expiryDate) {
    this.expiryDate = expiryDate;
    return this;
  }

   /**
   * expiry date of material    
   * @return expiryDate
  **/
  @ApiModelProperty(value = "expiry date of material    ")


  public Integer getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(Integer expiryDate) {
    this.expiryDate = expiryDate;
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

  public ScrapDetails quantity(BigDecimal quantity) {
    this.quantity = quantity;
    return this;
  }

   /**
   * scrap quantity of the ScrapDetails 
   * @return scrapQuantity
  **/
  @ApiModelProperty(required = true, value = "scrap quantity of the ScrapDetails ")
  @NotNull

  @Valid

  public BigDecimal getQuantity() {
    return quantity;
  }

  public void setQuantity(BigDecimal quantity) {
    this.quantity = quantity;
  }

  public ScrapDetails disposalQuantity(BigDecimal disposalQuantity) {
    this.disposalQuantity = disposalQuantity;
    return this;
  }

   /**
   * scrap quantity utilized in disposal of scrap materials 
   * @return disposalQuantity
  **/
  @ApiModelProperty(value = "scrap quantity utilized in disposal of scrap materials ")

  @Valid

  public BigDecimal getDisposalQuantity() {
    return disposalQuantity;
  }

  public void setDisposalQuantity(BigDecimal disposalQuantity) {
    this.disposalQuantity = disposalQuantity;
  }

  public ScrapDetails scrapValue(BigDecimal scrapValue) {
    this.scrapValue = scrapValue;
    return this;
  }

   /**
   * scrap value of the ScrapDetails     
   * @return scrapValue
  **/
  @ApiModelProperty(value = "scrap value of the ScrapDetails     ")

  @Valid

  public BigDecimal getScrapValue() {
    return scrapValue;
  }

  public void setScrapValue(BigDecimal scrapValue) {
    this.scrapValue = scrapValue;
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
        Objects.equals(this.scrapNumber, scrapDetails.scrapNumber) &&
        Objects.equals(this.material, scrapDetails.material) &&
        Objects.equals(this.uom, scrapDetails.uom) &&
        Objects.equals(this.receiptDetail, scrapDetails.receiptDetail) &&
        Objects.equals(this.issueDetail, scrapDetails.issueDetail) &&
        Objects.equals(this.lotNumber, scrapDetails.lotNumber) &&
        Objects.equals(this.expiryDate, scrapDetails.expiryDate) &&
        Objects.equals(this.scrapReason, scrapDetails.scrapReason) &&
        Objects.equals(this.quantity, scrapDetails.quantity) &&
        Objects.equals(this.disposalQuantity, scrapDetails.disposalQuantity) &&
        Objects.equals(this.scrapValue, scrapDetails.scrapValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, scrapNumber, material, uom, receiptDetail, issueDetail, lotNumber, expiryDate, scrapReason, quantity, disposalQuantity, scrapValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScrapDetails {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    scrapNumber: ").append(toIndentedString(scrapNumber)).append("\n");
    sb.append("    material: ").append(toIndentedString(material)).append("\n");
    sb.append("    uom: ").append(toIndentedString(uom)).append("\n");
    sb.append("    receiptDetail: ").append(toIndentedString(receiptDetail)).append("\n");
    sb.append("    issueDetail: ").append(toIndentedString(issueDetail)).append("\n");
    sb.append("    lotNumber: ").append(toIndentedString(lotNumber)).append("\n");
    sb.append("    expiryDate: ").append(toIndentedString(expiryDate)).append("\n");
    sb.append("    scrapReason: ").append(toIndentedString(scrapReason)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    disposalQuantity: ").append(toIndentedString(disposalQuantity)).append("\n");
    sb.append("    scrapValue: ").append(toIndentedString(scrapValue)).append("\n");
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

