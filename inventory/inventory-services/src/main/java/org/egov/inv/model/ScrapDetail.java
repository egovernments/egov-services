package org.egov.inv.model;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * This object holds the scrap details information. 
 */
@ApiModel(description = "This object holds the scrap details information. ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-04T10:25:40.605Z")

public class ScrapDetail   {
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
   * scrap reason of the ScrapDetail 
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

  @JsonProperty("scrapQuantity")
  private BigDecimal scrapQuantity = null;

  @JsonProperty("userQuantity")
  private BigDecimal userQuantity = null;

  @JsonProperty("disposalQuantity")
  private BigDecimal disposalQuantity = null;

  @JsonProperty("existingValue")
  private BigDecimal existingValue = null;

  @JsonProperty("scrapValue")
  private BigDecimal scrapValue = null;

  public ScrapDetail id(String id) {
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

  public ScrapDetail tenantId(String tenantId) {
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

  public ScrapDetail scrapNumber(String scrapNumber) {
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

  public ScrapDetail material(Material material) {
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

  public ScrapDetail uom(Uom uom) {
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

  public ScrapDetail receiptDetail(MaterialReceiptDetail receiptDetail) {
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

  public ScrapDetail issueDetail(MaterialIssueDetail issueDetail) {
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

  public ScrapDetail lotNumber(String lotNumber) {
    this.lotNumber = lotNumber;
    return this;
  }

   /**
   * lot number of the ScrapDetail 
   * @return lotNumber
  **/
  @ApiModelProperty(required = true, value = "lot number of the ScrapDetail ")
  @NotNull


  public String getLotNumber() {
    return lotNumber;
  }

  public void setLotNumber(String lotNumber) {
    this.lotNumber = lotNumber;
  }

  public ScrapDetail expiryDate(Integer expiryDate) {
    this.expiryDate = expiryDate;
    return this;
  }

   /**
   * expiry date of material 
   * @return expiryDate
  **/
  @ApiModelProperty(value = "expiry date of material ")


  public Integer getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(Integer expiryDate) {
    this.expiryDate = expiryDate;
  }

  public ScrapDetail scrapReason(ScrapReasonEnum scrapReason) {
    this.scrapReason = scrapReason;
    return this;
  }

   /**
   * scrap reason of the ScrapDetail 
   * @return scrapReason
  **/
  @ApiModelProperty(required = true, value = "scrap reason of the ScrapDetail ")
  @NotNull


  public ScrapReasonEnum getScrapReason() {
    return scrapReason;
  }

  public void setScrapReason(ScrapReasonEnum scrapReason) {
    this.scrapReason = scrapReason;
  }

  public ScrapDetail quantity(BigDecimal quantity) {
    this.quantity = quantity;
    return this;
  }

   /**
   * scrap quantity of the ScrapDetail 
   * @return quantity
  **/
  @ApiModelProperty(required = true, value = "scrap quantity of the ScrapDetail ")
  @NotNull

  @Valid

  public BigDecimal getQuantity() {
    return quantity;
  }

  public void setQuantity(BigDecimal quantity) {
    this.quantity = quantity;
  }

  public ScrapDetail scrapQuantity(BigDecimal scrapQuantity) {
    this.scrapQuantity = scrapQuantity;
    return this;
  }

   /**
   * scrap quantity of the ScrapDetail 
   * @return scrapQuantity
  **/
  @ApiModelProperty(value = "scrap quantity of the ScrapDetail ")

  @Valid

  public BigDecimal getScrapQuantity() {
    return scrapQuantity;
  }

  public void setScrapQuantity(BigDecimal scrapQuantity) {
    this.scrapQuantity = scrapQuantity;
  }

  public ScrapDetail userQuantity(BigDecimal userQuantity) {
    this.userQuantity = userQuantity;
    return this;
  }

   /**
   * The quantity of the material received for this particular scrap.
   * @return userQuantity
  **/
  @ApiModelProperty(value = "The quantity of the material received for this particular scrap.")

  @Valid

  public BigDecimal getUserQuantity() {
    return userQuantity;
  }

  public void setUserQuantity(BigDecimal userQuantity) {
    this.userQuantity = userQuantity;
  }

  public ScrapDetail disposalQuantity(BigDecimal disposalQuantity) {
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

  public ScrapDetail existingValue(BigDecimal existingValue) {
    this.existingValue = existingValue;
    return this;
  }

   /**
   * existing value present in the system 
   * @return existingValue
  **/
  @ApiModelProperty(value = "existing value present in the system ")

  @Valid

  public BigDecimal getExistingValue() {
    return existingValue;
  }

  public void setExistingValue(BigDecimal existingValue) {
    this.existingValue = existingValue;
  }

  public ScrapDetail scrapValue(BigDecimal scrapValue) {
    this.scrapValue = scrapValue;
    return this;
  }

   /**
   * scrap value of the ScrapDetail 
   * @return scrapValue
  **/
  @ApiModelProperty(value = "scrap value of the ScrapDetail ")

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
    ScrapDetail scrapDetail = (ScrapDetail) o;
    return Objects.equals(this.id, scrapDetail.id) &&
        Objects.equals(this.tenantId, scrapDetail.tenantId) &&
        Objects.equals(this.scrapNumber, scrapDetail.scrapNumber) &&
        Objects.equals(this.material, scrapDetail.material) &&
        Objects.equals(this.uom, scrapDetail.uom) &&
        Objects.equals(this.receiptDetail, scrapDetail.receiptDetail) &&
        Objects.equals(this.issueDetail, scrapDetail.issueDetail) &&
        Objects.equals(this.lotNumber, scrapDetail.lotNumber) &&
        Objects.equals(this.expiryDate, scrapDetail.expiryDate) &&
        Objects.equals(this.scrapReason, scrapDetail.scrapReason) &&
        Objects.equals(this.quantity, scrapDetail.quantity) &&
        Objects.equals(this.scrapQuantity, scrapDetail.scrapQuantity) &&
        Objects.equals(this.userQuantity, scrapDetail.userQuantity) &&
        Objects.equals(this.disposalQuantity, scrapDetail.disposalQuantity) &&
        Objects.equals(this.existingValue, scrapDetail.existingValue) &&
        Objects.equals(this.scrapValue, scrapDetail.scrapValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, scrapNumber, material, uom, receiptDetail, issueDetail, lotNumber, expiryDate, scrapReason, quantity, scrapQuantity, userQuantity, disposalQuantity, existingValue, scrapValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScrapDetail {\n");
    
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
    sb.append("    scrapQuantity: ").append(toIndentedString(scrapQuantity)).append("\n");
    sb.append("    userQuantity: ").append(toIndentedString(userQuantity)).append("\n");
    sb.append("    disposalQuantity: ").append(toIndentedString(disposalQuantity)).append("\n");
    sb.append("    existingValue: ").append(toIndentedString(existingValue)).append("\n");
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

