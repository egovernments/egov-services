package org.egov.inv.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.egov.inv.model.Material;
import org.egov.inv.model.PriceList;
import org.egov.inv.model.PurchaseIndentDetail;
import org.egov.inv.model.Uom;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * This object holds the purchase material information. 
 */
@ApiModel(description = "This object holds the purchase material information. ")
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-12-27T10:36:36.253Z")

public class PurchaseOrderDetail   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("material")
  private Material material = null;

  @JsonProperty("purchaseOrderNumber")
  private String purchaseOrderNumber = null;

  @JsonProperty("orderNumber")
  private BigDecimal orderNumber = null;

  @JsonProperty("uom")
  private Uom uom = null;

  @JsonProperty("priceList")
  private PriceList priceList = null;

  @JsonProperty("userQuantity")
  private BigDecimal userQuantity = null;

  @JsonProperty("orderQuantity")
  private BigDecimal orderQuantity = null;

  @JsonProperty("receivedQuantity")
  private BigDecimal receivedQuantity = null;

  @JsonProperty("usedQuantity")
  private BigDecimal usedQuantity = null;

  @JsonProperty("unitPrice")
  private BigDecimal unitPrice = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("indentNumber")
  private String indentNumber = null;

  @JsonProperty("indentQuantity")
  private BigDecimal indentQuantity = null;

  @JsonProperty("tenderQuantity")
  private BigDecimal tenderQuantity = null;

  @JsonProperty("tenderAvailableQuantity")
  private BigDecimal tenderAvailableQuantity = null;

  @JsonProperty("purchaseIndentDetails")
  private List<PurchaseIndentDetail> purchaseIndentDetails = null;

  public PurchaseOrderDetail id(String id) {
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

  public PurchaseOrderDetail tenantId(String tenantId) {
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

  public PurchaseOrderDetail material(Material material) {
    this.material = material;
    return this;
  }

   /**
   * Get material
   * @return material
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

 

  public Material getMaterial() {
    return material;
  }

  public void setMaterial(Material material) {
    this.material = material;
  }

  public PurchaseOrderDetail purchaseOrderNumber(String purchaseOrderNumber) {
    this.purchaseOrderNumber = purchaseOrderNumber;
    return this;
  }

   /**
   * purchaseOrderNumber reference from purchase order 
   * @return purchaseOrderNumber
  **/
  @ApiModelProperty(readOnly = true, value = "purchaseOrderNumber reference from purchase order ")


  public String getPurchaseOrderNumber() {
    return purchaseOrderNumber;
  }

  public void setPurchaseOrderNumber(String purchaseOrderNumber) {
    this.purchaseOrderNumber = purchaseOrderNumber;
  }

  public PurchaseOrderDetail orderNumber(BigDecimal orderNumber) {
    this.orderNumber = orderNumber;
    return this;
  }

   /**
   * Order of items selected.
   * @return orderNumber
  **/
  @ApiModelProperty(value = "Order of items selected.")

   

  public BigDecimal getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(BigDecimal orderNumber) {
    this.orderNumber = orderNumber;
  }

  public PurchaseOrderDetail uom(Uom uom) {
    this.uom = uom;
    return this;
  }

   /**
   * Get uom
   * @return uom
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  

  public Uom getUom() {
    return uom;
  }

  public void setUom(Uom uom) {
    this.uom = uom;
  }

  public PurchaseOrderDetail priceList(PriceList priceList) {
    this.priceList = priceList;
    return this;
  }

   /**
   * Get priceList
   * @return priceList
  **/
  @ApiModelProperty(value = "")

  

  public PriceList getPriceList() {
    return priceList;
  }

  public void setPriceList(PriceList priceList) {
    this.priceList = priceList;
  }

  public PurchaseOrderDetail userQuantity(BigDecimal userQuantity) {
    this.userQuantity = userQuantity;
    return this;
  }

   /**
   * user orderd quantity of the PurchaseMaterial 
   * @return userQuantity
  **/
  @ApiModelProperty(required = true, value = "user orderd quantity of the PurchaseMaterial ")
  @NotNull

  

  public BigDecimal getUserQuantity() {
    return userQuantity;
  }

  public void setUserQuantity(BigDecimal userQuantity) {
    this.userQuantity = userQuantity;
  }

  public PurchaseOrderDetail orderQuantity(BigDecimal orderQuantity) {
    this.orderQuantity = orderQuantity;
    return this;
  }

   /**
   * order quantity of the PurchaseMaterial in base uom  
   * @return orderQuantity
  **/
  @ApiModelProperty(value = "order quantity of the PurchaseMaterial in base uom  ")

  

  public BigDecimal getOrderQuantity() {
    return orderQuantity;
  }

  public void setOrderQuantity(BigDecimal orderQuantity) {
    this.orderQuantity = orderQuantity;
  }

  public PurchaseOrderDetail receivedQuantity(BigDecimal receivedQuantity) {
    this.receivedQuantity = receivedQuantity;
    return this;
  }

   /**
   * order quantity of the PurchaseMaterial 
   * @return receivedQuantity
  **/
  @ApiModelProperty(value = "order quantity of the PurchaseMaterial ")

  

  public BigDecimal getReceivedQuantity() {
    return receivedQuantity;
  }

  public void setReceivedQuantity(BigDecimal receivedQuantity) {
    this.receivedQuantity = receivedQuantity;
  }

  public PurchaseOrderDetail usedQuantity(BigDecimal usedQuantity) {
    this.usedQuantity = usedQuantity;
    return this;
  }

   /**
   * order quantity of the PurchaseMaterial 
   * @return usedQuantity
  **/
  @ApiModelProperty(value = "order quantity of the PurchaseMaterial ")

  

  public BigDecimal getUsedQuantity() {
    return usedQuantity;
  }

  public void setUsedQuantity(BigDecimal usedQuantity) {
    this.usedQuantity = usedQuantity;
  }

  public PurchaseOrderDetail unitPrice(BigDecimal unitPrice) {
    this.unitPrice = unitPrice;
    return this;
  }

   /**
   * unit price of  PurchaseMaterial 
   * @return unitPrice
  **/
  @ApiModelProperty(required = true, value = "unit price of  PurchaseMaterial ")
  @NotNull

  

  public BigDecimal getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(BigDecimal unitPrice) {
    this.unitPrice = unitPrice;
  }

  public PurchaseOrderDetail description(String description) {
    this.description = description;
    return this;
  }

   /**
   * remarks of the Purchase material details 
   * @return description
  **/
  @ApiModelProperty(value = "remarks of the Purchase material details ")

 @Size(max=512)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public PurchaseOrderDetail indentNumber(String indentNumber) {
    this.indentNumber = indentNumber;
    return this;
  }

   /**
   * temporary field used to show indent number. If multiple indent numbers used, then show multiple as string in this field. 
   * @return indentNumber
  **/
  @ApiModelProperty(value = "temporary field used to show indent number. If multiple indent numbers used, then show multiple as string in this field. ")

 @Size(max=512)
  public String getIndentNumber() {
    return indentNumber;
  }

  public void setIndentNumber(String indentNumber) {
    this.indentNumber = indentNumber;
  }

  public PurchaseOrderDetail indentQuantity(BigDecimal indentQuantity) {
    this.indentQuantity = indentQuantity;
    return this;
  }

   /**
   * temporary field used to show indent pending quantity to be used for purchase order. If multiple indent numbers used, then show total quantity by combining indent pending quantities. 
   * @return indentQuantity
  **/
  @ApiModelProperty(value = "temporary field used to show indent pending quantity to be used for purchase order. If multiple indent numbers used, then show total quantity by combining indent pending quantities. ")

  

  public BigDecimal getIndentQuantity() {
    return indentQuantity;
  }

  public void setIndentQuantity(BigDecimal indentQuantity) {
    this.indentQuantity = indentQuantity;
  }

  public PurchaseOrderDetail tenderQuantity(BigDecimal tenderQuantity) {
    this.tenderQuantity = tenderQuantity;
    return this;
  }

   /**
   * temporary field used to show tender quantity. If rate type is tender, then show tender quantity. 
   * @return tenderQuantity
  **/
  @ApiModelProperty(value = "temporary field used to show tender quantity. If rate type is tender, then show tender quantity. ")

  

  public BigDecimal getTenderQuantity() {
    return tenderQuantity;
  }

  public void setTenderQuantity(BigDecimal tenderQuantity) {
    this.tenderQuantity = tenderQuantity;
  }

  public PurchaseOrderDetail tenderAvailableQuantity(BigDecimal tenderAvailableQuantity) {
    this.tenderAvailableQuantity = tenderAvailableQuantity;
    return this;
  }

   /**
   * temporary field used to show tender available quantity. If rate type is tender, then show tender quantity which are already used in purchase orders. 
   * @return tenderAvailableQuantity
  **/
  @ApiModelProperty(value = "temporary field used to show tender available quantity. If rate type is tender, then show tender quantity which are already used in purchase orders. ")

  

  public BigDecimal getTenderAvailableQuantity() {
    return tenderAvailableQuantity;
  }

  public void setTenderAvailableQuantity(BigDecimal tenderAvailableQuantity) {
    this.tenderAvailableQuantity = tenderAvailableQuantity;
  }

  public PurchaseOrderDetail purchaseIndentDetails(List<PurchaseIndentDetail> purchaseIndentDetails) {
    this.purchaseIndentDetails = purchaseIndentDetails;
    return this;
  }

  public PurchaseOrderDetail addPurchaseIndentDetailsItem(PurchaseIndentDetail purchaseIndentDetailsItem) {
    if (this.purchaseIndentDetails == null) {
      this.purchaseIndentDetails = new ArrayList<PurchaseIndentDetail>();
    }
    this.purchaseIndentDetails.add(purchaseIndentDetailsItem);
    return this;
  }

   /**
   * Get purchaseIndentDetails
   * @return purchaseIndentDetails
  **/
  @ApiModelProperty(value = "")

  

  public List<PurchaseIndentDetail> getPurchaseIndentDetails() {
    return purchaseIndentDetails;
  }

  public void setPurchaseIndentDetails(List<PurchaseIndentDetail> purchaseIndentDetails) {
    this.purchaseIndentDetails = purchaseIndentDetails;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PurchaseOrderDetail purchaseOrderDetail = (PurchaseOrderDetail) o;
    return Objects.equals(this.id, purchaseOrderDetail.id) &&
        Objects.equals(this.tenantId, purchaseOrderDetail.tenantId) &&
        Objects.equals(this.material, purchaseOrderDetail.material) &&
        Objects.equals(this.purchaseOrderNumber, purchaseOrderDetail.purchaseOrderNumber) &&
        Objects.equals(this.orderNumber, purchaseOrderDetail.orderNumber) &&
        Objects.equals(this.uom, purchaseOrderDetail.uom) &&
        Objects.equals(this.priceList, purchaseOrderDetail.priceList) &&
        Objects.equals(this.userQuantity, purchaseOrderDetail.userQuantity) &&
        Objects.equals(this.orderQuantity, purchaseOrderDetail.orderQuantity) &&
        Objects.equals(this.receivedQuantity, purchaseOrderDetail.receivedQuantity) &&
        Objects.equals(this.usedQuantity, purchaseOrderDetail.usedQuantity) &&
        Objects.equals(this.unitPrice, purchaseOrderDetail.unitPrice) &&
        Objects.equals(this.description, purchaseOrderDetail.description) &&
        Objects.equals(this.indentNumber, purchaseOrderDetail.indentNumber) &&
        Objects.equals(this.indentQuantity, purchaseOrderDetail.indentQuantity) &&
        Objects.equals(this.tenderQuantity, purchaseOrderDetail.tenderQuantity) &&
        Objects.equals(this.tenderAvailableQuantity, purchaseOrderDetail.tenderAvailableQuantity) &&
        Objects.equals(this.purchaseIndentDetails, purchaseOrderDetail.purchaseIndentDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, material, purchaseOrderNumber, orderNumber, uom, priceList, userQuantity, orderQuantity, receivedQuantity, usedQuantity, unitPrice, description, indentNumber, indentQuantity, tenderQuantity, tenderAvailableQuantity, purchaseIndentDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PurchaseOrderDetail {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    material: ").append(toIndentedString(material)).append("\n");
    sb.append("    purchaseOrderNumber: ").append(toIndentedString(purchaseOrderNumber)).append("\n");
    sb.append("    orderNumber: ").append(toIndentedString(orderNumber)).append("\n");
    sb.append("    uom: ").append(toIndentedString(uom)).append("\n");
    sb.append("    priceList: ").append(toIndentedString(priceList)).append("\n");
    sb.append("    userQuantity: ").append(toIndentedString(userQuantity)).append("\n");
    sb.append("    orderQuantity: ").append(toIndentedString(orderQuantity)).append("\n");
    sb.append("    receivedQuantity: ").append(toIndentedString(receivedQuantity)).append("\n");
    sb.append("    usedQuantity: ").append(toIndentedString(usedQuantity)).append("\n");
    sb.append("    unitPrice: ").append(toIndentedString(unitPrice)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    indentNumber: ").append(toIndentedString(indentNumber)).append("\n");
    sb.append("    indentQuantity: ").append(toIndentedString(indentQuantity)).append("\n");
    sb.append("    tenderQuantity: ").append(toIndentedString(tenderQuantity)).append("\n");
    sb.append("    tenderAvailableQuantity: ").append(toIndentedString(tenderAvailableQuantity)).append("\n");
    sb.append("    purchaseIndentDetails: ").append(toIndentedString(purchaseIndentDetails)).append("\n");
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

