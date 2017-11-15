package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.AuditDetails;
import io.swagger.model.PurchaseMaterial;
import io.swagger.model.Store;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * 
 */
@ApiModel(description = "")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T13:21:55.964+05:30")

public class PurchaseOrder   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("store")
  private Store store = null;

  @JsonProperty("purchaseOrderNumber")
  private String purchaseOrderNumber = null;

  @JsonProperty("purchaseOrderDate")
  private Long purchaseOrderDate = null;

  /**
   * rate type of the PurchaseOrder 
   */
  public enum RateTypeEnum {
    DIT("DIT"),
    
    DGSD("DGSD"),
    
    TENDER("Tender"),
    
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

  @JsonProperty("supplierCode")
  private String supplierCode = null;

  @JsonProperty("advanceAmount")
  private BigDecimal advanceAmount = null;

  @JsonProperty("advancePercentage")
  private BigDecimal advancePercentage = null;

  @JsonProperty("expectedDeliveryDate")
  private Long expectedDeliveryDate = null;

  @JsonProperty("deliveryTerms")
  private String deliveryTerms = null;

  @JsonProperty("paymentTerms")
  private String paymentTerms = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("status")
  private String status = null;

  @JsonProperty("stateId")
  private Long stateId = null;

  @JsonProperty("purchaseMaterials")
  @Valid
  private List<PurchaseMaterial> purchaseMaterials = new ArrayList<PurchaseMaterial>();

  @JsonProperty("fileStoreId")
  private String fileStoreId = null;

  @JsonProperty("designation")
  private String designation = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public PurchaseOrder id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Purchase Order 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Purchase Order ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public PurchaseOrder tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Purchase Order
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenant id of the Purchase Order")

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public PurchaseOrder store(Store store) {
    this.store = store;
    return this;
  }

   /**
   * Get store
   * @return store
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public Store getStore() {
    return store;
  }

  public void setStore(Store store) {
    this.store = store;
  }

  public PurchaseOrder purchaseOrderNumber(String purchaseOrderNumber) {
    this.purchaseOrderNumber = purchaseOrderNumber;
    return this;
  }

   /**
   * purchaseOrderNumber  Auto generated number, read only 
   * @return purchaseOrderNumber
  **/
  @ApiModelProperty(required = true, readOnly = true, value = "purchaseOrderNumber  Auto generated number, read only ")
  @NotNull


  public String getPurchaseOrderNumber() {
    return purchaseOrderNumber;
  }

  public void setPurchaseOrderNumber(String purchaseOrderNumber) {
    this.purchaseOrderNumber = purchaseOrderNumber;
  }

  public PurchaseOrder purchaseOrderDate(Long purchaseOrderDate) {
    this.purchaseOrderDate = purchaseOrderDate;
    return this;
  }

   /**
   * purchase order date of the PurchaseOrder 
   * @return purchaseOrderDate
  **/
  @ApiModelProperty(required = true, value = "purchase order date of the PurchaseOrder ")
  @NotNull


  public Long getPurchaseOrderDate() {
    return purchaseOrderDate;
  }

  public void setPurchaseOrderDate(Long purchaseOrderDate) {
    this.purchaseOrderDate = purchaseOrderDate;
  }

  public PurchaseOrder rateType(RateTypeEnum rateType) {
    this.rateType = rateType;
    return this;
  }

   /**
   * rate type of the PurchaseOrder 
   * @return rateType
  **/
  @ApiModelProperty(required = true, value = "rate type of the PurchaseOrder ")
  @NotNull


  public RateTypeEnum getRateType() {
    return rateType;
  }

  public void setRateType(RateTypeEnum rateType) {
    this.rateType = rateType;
  }

  public PurchaseOrder supplierCode(String supplierCode) {
    this.supplierCode = supplierCode;
    return this;
  }

   /**
   * supplier code of the PurchaseOrder 
   * @return supplierCode
  **/
  @ApiModelProperty(required = true, value = "supplier code of the PurchaseOrder ")
  @NotNull


  public String getSupplierCode() {
    return supplierCode;
  }

  public void setSupplierCode(String supplierCode) {
    this.supplierCode = supplierCode;
  }

  public PurchaseOrder advanceAmount(BigDecimal advanceAmount) {
    this.advanceAmount = advanceAmount;
    return this;
  }

   /**
   * advance amount of the PurchaseOrder 
   * @return advanceAmount
  **/
  @ApiModelProperty(value = "advance amount of the PurchaseOrder ")

  @Valid

  public BigDecimal getAdvanceAmount() {
    return advanceAmount;
  }

  public void setAdvanceAmount(BigDecimal advanceAmount) {
    this.advanceAmount = advanceAmount;
  }

  public PurchaseOrder advancePercentage(BigDecimal advancePercentage) {
    this.advancePercentage = advancePercentage;
    return this;
  }

   /**
   * advance percentage of the PurchaseOrder 
   * @return advancePercentage
  **/
  @ApiModelProperty(value = "advance percentage of the PurchaseOrder ")

  @Valid

  public BigDecimal getAdvancePercentage() {
    return advancePercentage;
  }

  public void setAdvancePercentage(BigDecimal advancePercentage) {
    this.advancePercentage = advancePercentage;
  }

  public PurchaseOrder expectedDeliveryDate(Long expectedDeliveryDate) {
    this.expectedDeliveryDate = expectedDeliveryDate;
    return this;
  }

   /**
   * expected delivery date of the PurchaseOrder 
   * @return expectedDeliveryDate
  **/
  @ApiModelProperty(required = true, value = "expected delivery date of the PurchaseOrder ")
  @NotNull


  public Long getExpectedDeliveryDate() {
    return expectedDeliveryDate;
  }

  public void setExpectedDeliveryDate(Long expectedDeliveryDate) {
    this.expectedDeliveryDate = expectedDeliveryDate;
  }

  public PurchaseOrder deliveryTerms(String deliveryTerms) {
    this.deliveryTerms = deliveryTerms;
    return this;
  }

   /**
   * delivery terms of the PurchaseOrder 
   * @return deliveryTerms
  **/
  @ApiModelProperty(required = true, value = "delivery terms of the PurchaseOrder ")
  @NotNull

 @Size(min=1,max=500)
  public String getDeliveryTerms() {
    return deliveryTerms;
  }

  public void setDeliveryTerms(String deliveryTerms) {
    this.deliveryTerms = deliveryTerms;
  }

  public PurchaseOrder paymentTerms(String paymentTerms) {
    this.paymentTerms = paymentTerms;
    return this;
  }

   /**
   * payment terms of the PurchaseOrder 
   * @return paymentTerms
  **/
  @ApiModelProperty(value = "payment terms of the PurchaseOrder ")

 @Size(max=500)
  public String getPaymentTerms() {
    return paymentTerms;
  }

  public void setPaymentTerms(String paymentTerms) {
    this.paymentTerms = paymentTerms;
  }

  public PurchaseOrder description(String description) {
    this.description = description;
    return this;
  }

   /**
   * description of the PurchaseOrder 
   * @return description
  **/
  @ApiModelProperty(value = "description of the PurchaseOrder ")

 @Size(max=1000)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public PurchaseOrder status(String status) {
    this.status = status;
    return this;
  }

   /**
   * status of the PurchaseOrder 
   * @return status
  **/
  @ApiModelProperty(value = "status of the PurchaseOrder ")

 @Size(min=5,max=50)
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public PurchaseOrder stateId(Long stateId) {
    this.stateId = stateId;
    return this;
  }

   /**
   * state id of the PurchaseOrder 
   * @return stateId
  **/
  @ApiModelProperty(value = "state id of the PurchaseOrder ")


  public Long getStateId() {
    return stateId;
  }

  public void setStateId(Long stateId) {
    this.stateId = stateId;
  }

  public PurchaseOrder purchaseMaterials(List<PurchaseMaterial> purchaseMaterials) {
    this.purchaseMaterials = purchaseMaterials;
    return this;
  }

  public PurchaseOrder addPurchaseMaterialsItem(PurchaseMaterial purchaseMaterialsItem) {
    this.purchaseMaterials.add(purchaseMaterialsItem);
    return this;
  }

   /**
   * Get purchaseMaterials
   * @return purchaseMaterials
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public List<PurchaseMaterial> getPurchaseMaterials() {
    return purchaseMaterials;
  }

  public void setPurchaseMaterials(List<PurchaseMaterial> purchaseMaterials) {
    this.purchaseMaterials = purchaseMaterials;
  }

  public PurchaseOrder fileStoreId(String fileStoreId) {
    this.fileStoreId = fileStoreId;
    return this;
  }

   /**
   * fileStoreId  File Store id of the Purchase Order Generated 
   * @return fileStoreId
  **/
  @ApiModelProperty(value = "fileStoreId  File Store id of the Purchase Order Generated ")


  public String getFileStoreId() {
    return fileStoreId;
  }

  public void setFileStoreId(String fileStoreId) {
    this.fileStoreId = fileStoreId;
  }

  public PurchaseOrder designation(String designation) {
    this.designation = designation;
    return this;
  }

   /**
   * Designation of the of the created by user at the time of Purchase Order creation.
   * @return designation
  **/
  @ApiModelProperty(value = "Designation of the of the created by user at the time of Purchase Order creation.")


  public String getDesignation() {
    return designation;
  }

  public void setDesignation(String designation) {
    this.designation = designation;
  }

  public PurchaseOrder auditDetails(AuditDetails auditDetails) {
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
    PurchaseOrder purchaseOrder = (PurchaseOrder) o;
    return Objects.equals(this.id, purchaseOrder.id) &&
        Objects.equals(this.tenantId, purchaseOrder.tenantId) &&
        Objects.equals(this.store, purchaseOrder.store) &&
        Objects.equals(this.purchaseOrderNumber, purchaseOrder.purchaseOrderNumber) &&
        Objects.equals(this.purchaseOrderDate, purchaseOrder.purchaseOrderDate) &&
        Objects.equals(this.rateType, purchaseOrder.rateType) &&
        Objects.equals(this.supplierCode, purchaseOrder.supplierCode) &&
        Objects.equals(this.advanceAmount, purchaseOrder.advanceAmount) &&
        Objects.equals(this.advancePercentage, purchaseOrder.advancePercentage) &&
        Objects.equals(this.expectedDeliveryDate, purchaseOrder.expectedDeliveryDate) &&
        Objects.equals(this.deliveryTerms, purchaseOrder.deliveryTerms) &&
        Objects.equals(this.paymentTerms, purchaseOrder.paymentTerms) &&
        Objects.equals(this.description, purchaseOrder.description) &&
        Objects.equals(this.status, purchaseOrder.status) &&
        Objects.equals(this.stateId, purchaseOrder.stateId) &&
        Objects.equals(this.purchaseMaterials, purchaseOrder.purchaseMaterials) &&
        Objects.equals(this.fileStoreId, purchaseOrder.fileStoreId) &&
        Objects.equals(this.designation, purchaseOrder.designation) &&
        Objects.equals(this.auditDetails, purchaseOrder.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, store, purchaseOrderNumber, purchaseOrderDate, rateType, supplierCode, advanceAmount, advancePercentage, expectedDeliveryDate, deliveryTerms, paymentTerms, description, status, stateId, purchaseMaterials, fileStoreId, designation, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PurchaseOrder {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    store: ").append(toIndentedString(store)).append("\n");
    sb.append("    purchaseOrderNumber: ").append(toIndentedString(purchaseOrderNumber)).append("\n");
    sb.append("    purchaseOrderDate: ").append(toIndentedString(purchaseOrderDate)).append("\n");
    sb.append("    rateType: ").append(toIndentedString(rateType)).append("\n");
    sb.append("    supplierCode: ").append(toIndentedString(supplierCode)).append("\n");
    sb.append("    advanceAmount: ").append(toIndentedString(advanceAmount)).append("\n");
    sb.append("    advancePercentage: ").append(toIndentedString(advancePercentage)).append("\n");
    sb.append("    expectedDeliveryDate: ").append(toIndentedString(expectedDeliveryDate)).append("\n");
    sb.append("    deliveryTerms: ").append(toIndentedString(deliveryTerms)).append("\n");
    sb.append("    paymentTerms: ").append(toIndentedString(paymentTerms)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    stateId: ").append(toIndentedString(stateId)).append("\n");
    sb.append("    purchaseMaterials: ").append(toIndentedString(purchaseMaterials)).append("\n");
    sb.append("    fileStoreId: ").append(toIndentedString(fileStoreId)).append("\n");
    sb.append("    designation: ").append(toIndentedString(designation)).append("\n");
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

