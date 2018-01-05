package org.egov.inv.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
 * This object holds the purchase order information.   
 */
@ApiModel(description = "This object holds the purchase order information.   ")
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-11-08T13:51:07.770Z")

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
  public enum PurchaseTypeEnum {
    INDENT("Indent"),
    
    NON_INDENT("Non Indent");

    private String value;

    PurchaseTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static PurchaseTypeEnum fromValue(String text) {
      for (PurchaseTypeEnum b : PurchaseTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("purchaseType")
  private PurchaseTypeEnum purchaseType = null;

  /**
   * rate type of the PurchaseOrder 
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

  @JsonProperty("supplier")
  private Supplier supplier = null;

  @JsonProperty("advanceAmount")
  private BigDecimal advanceAmount = null;

  @JsonProperty("totalAdvancePaidAmount")
  private BigDecimal totalAdvancePaidAmount=null;
  
  @JsonProperty("advancePercentage")
  private BigDecimal advancePercentage = null;

  @JsonProperty("expectedDeliveryDate")
  private Long expectedDeliveryDate = null;

  @JsonProperty("deliveryTerms")
  private String deliveryTerms = null;

  @JsonProperty("paymentTerms")
  private String paymentTerms = null;

  @JsonProperty("remarks")
  private String remarks = null;
  
  @JsonProperty("totalAmount")
  private BigDecimal totalAmount = null;

  /**
   * status of the Ind PurchaseOrder 
   */
  public enum StatusEnum {
    CREATED("Created"),
    
    APPROVED("Approved"),
    
    REJECTED("Rejected"),
    
    FULFILLED_AND_PAID("Fulfilled and Paid"),
    
    FULFILLED_AND_UNPAID("Fulfilled and Unpaid");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("status")
  private StatusEnum status = null;

  @JsonProperty("workFlowDetails")
  private WorkFlowDetails workFlowDetails = null;

  @JsonProperty("stateId")
  private Long stateId = null;

  @JsonProperty("purchaseOrderDetails")
  private List<PurchaseOrderDetail> purchaseOrderDetails = new ArrayList<PurchaseOrderDetail>();

  @JsonProperty("fileStoreId")
  private String fileStoreId = null;

  @JsonProperty("designation")
  private String designation = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  @JsonProperty("indentNumbers")
  private List<String> indentNumbers = null;
  
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
   * Store which orders item for purchase order
   * @return store
  **/
  @ApiModelProperty(required = true, value = "Store which orders item for purchase order")
  @NotNull
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
  @ApiModelProperty(readOnly = true, value = "purchaseOrderNumber  Auto generated number, read only ")
 

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

  public PurchaseOrder purchaseType(PurchaseTypeEnum purchaseType) {
    this.purchaseType = purchaseType;
    return this;
  }

   /**
   * rate type of the PurchaseOrder 
   * @return purchaseType
  **/
  @ApiModelProperty(value = "rate type of the PurchaseOrder ")


  public PurchaseTypeEnum getPurchaseType() {
    return purchaseType;
  }

  public void setPurchaseType(PurchaseTypeEnum purchaseType) {
    this.purchaseType = purchaseType;
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

  public PurchaseOrder supplier(Supplier supplier) {
    this.supplier = supplier;
    return this;
  }

   /**
   * supplier code of the purchase order 
   * @return supplier
  **/
  @ApiModelProperty(required = true, value = "supplier code of the purchase order ")
  @NotNull

  

  public Supplier getSupplier() {
    return supplier;
  }

  public void setSupplier(Supplier supplier) {
    this.supplier = supplier;
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

  

  public BigDecimal getAdvanceAmount() {
    return advanceAmount;
  }

  public void setAdvanceAmount(BigDecimal advanceAmount) {
    this.advanceAmount = advanceAmount;
  }
  
  public PurchaseOrder totalAdvancePaidAmount(BigDecimal totalAdvancePaidAmount) {
	    this.totalAdvancePaidAmount = totalAdvancePaidAmount;
	    return this;
	  }

 /**
   * totalAdvancePaidAmount of the PurchaseOrder 
   * @return totalAdvancePaidAmount
  **/
  @ApiModelProperty(value = "totalAdvancePaidAmount of the PurchaseOrder ")

  public BigDecimal getTotalAdvancePaidAmount() {
    return totalAdvancePaidAmount;
  }

  public void setTotalAdvancePaidAmount(BigDecimal totalAdvancePaidAmount) {
    this.totalAdvancePaidAmount = totalAdvancePaidAmount;
  }
  
  public PurchaseOrder totalAmount(BigDecimal totalAmount) {
	    this.totalAmount = totalAmount;
	    return this;
	  }

  /**
   * total amount of the PurchaseOrder 
   * @return totalAmount
  **/
  @ApiModelProperty(value = "total amount of the PurchaseOrder ")

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
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
  @ApiModelProperty(value = "expected delivery date of the PurchaseOrder ")


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
  @ApiModelProperty(value = "delivery terms of the PurchaseOrder ")

 @Size(max=512)
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

 @Size(max=512)
  public String getPaymentTerms() {
    return paymentTerms;
  }

  public void setPaymentTerms(String paymentTerms) {
    this.paymentTerms = paymentTerms;
  }

  public PurchaseOrder remarks(String remarks) {
    this.remarks = remarks;
    return this;
  }

   /**
   * description of the PurchaseOrder 
   * @return remarks
  **/
  @ApiModelProperty(value = "description of the PurchaseOrder ")

 @Size(max=1000)
  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public PurchaseOrder status(StatusEnum status) {
    this.status = status;
    return this;
  }

   /**
   * status of the Ind PurchaseOrder 
   * @return status
  **/
  @ApiModelProperty(value = "status of the Ind PurchaseOrder ")


  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public PurchaseOrder workFlowDetails(WorkFlowDetails workFlowDetails) {
    this.workFlowDetails = workFlowDetails;
    return this;
  }

   /**
   * Get workFlowDetails
   * @return workFlowDetails
  **/
  @ApiModelProperty(value = "")

  

  public WorkFlowDetails getWorkFlowDetails() {
    return workFlowDetails;
  }

  public void setWorkFlowDetails(WorkFlowDetails workFlowDetails) {
    this.workFlowDetails = workFlowDetails;
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

  public PurchaseOrder purchaseOrderDetails(List<PurchaseOrderDetail> purchaseOrderDetails) {
    this.purchaseOrderDetails = purchaseOrderDetails;
    return this;
  }

  public PurchaseOrder addPurchaseOrderDetailsItem(PurchaseOrderDetail purchaseOrderDetailsItem) {
    this.purchaseOrderDetails.add(purchaseOrderDetailsItem);
    return this;
  }

   /**
   * Get purchaseOrderDetails
   * @return purchaseOrderDetails
  **/
  @ApiModelProperty(required = true, value = "")
  @Valid
  @NotNull
   public List<PurchaseOrderDetail> getPurchaseOrderDetails() {
    return purchaseOrderDetails;
  }

  public void setPurchaseOrderDetails(List<PurchaseOrderDetail> purchaseOrderDetails) {
    this.purchaseOrderDetails = purchaseOrderDetails;
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
   * Designation of the created by user at the time of Purchase Order creation.
   * @return designation
  **/
  @ApiModelProperty(value = "Designation of the created by user at the time of Purchase Order creation.")


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

  

  public AuditDetails getAuditDetails() {
    return auditDetails;
  }

  public void setAuditDetails(AuditDetails auditDetails) {
    this.auditDetails = auditDetails;
  }

  public PurchaseOrder indentNumbers(List<String> indents) {
    this.indentNumbers = indents;
    return this;
  }

  public PurchaseOrder addIndentNumbersItem(String indentsItem) {
    if (this.indentNumbers == null) {
      this.indentNumbers = new ArrayList<String>();
    }
    this.indentNumbers.add(indentsItem);
    return this;
  }

   /**
   * Used for indent search result and used in indent purchase order creation
   * @return indents
  **/
  @ApiModelProperty(value = "Used for indent search result and used in indent purchase order creation")


  public List<String> getIndentNumbers() {
    return indentNumbers;
  }

  public void setIndentNumbers(List<String> indents) {
    this.indentNumbers = indents;
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
        Objects.equals(this.purchaseType, purchaseOrder.purchaseType) &&
        Objects.equals(this.rateType, purchaseOrder.rateType) &&
        Objects.equals(this.supplier, purchaseOrder.supplier) &&
        Objects.equals(this.advanceAmount, purchaseOrder.advanceAmount) &&
        Objects.equals(this.totalAdvancePaidAmount, purchaseOrder.totalAdvancePaidAmount) &&
        Objects.equals(this.advancePercentage, purchaseOrder.advancePercentage) &&
        Objects.equals(this.expectedDeliveryDate, purchaseOrder.expectedDeliveryDate) &&
        Objects.equals(this.deliveryTerms, purchaseOrder.deliveryTerms) &&
        Objects.equals(this.paymentTerms, purchaseOrder.paymentTerms) &&
        Objects.equals(this.remarks, purchaseOrder.remarks) &&
        Objects.equals(this.status, purchaseOrder.status) &&
        Objects.equals(this.workFlowDetails, purchaseOrder.workFlowDetails) &&
        Objects.equals(this.stateId, purchaseOrder.stateId) &&
        Objects.equals(this.purchaseOrderDetails, purchaseOrder.purchaseOrderDetails) &&
        Objects.equals(this.fileStoreId, purchaseOrder.fileStoreId) &&
        Objects.equals(this.designation, purchaseOrder.designation) &&
        Objects.equals(this.auditDetails, purchaseOrder.auditDetails) &&
        Objects.equals(this.indentNumbers, purchaseOrder.indentNumbers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, store, purchaseOrderNumber, purchaseOrderDate, purchaseType, rateType, supplier, advanceAmount, totalAdvancePaidAmount, advancePercentage, expectedDeliveryDate, deliveryTerms, paymentTerms, remarks, status, workFlowDetails, stateId, purchaseOrderDetails, fileStoreId, designation, auditDetails, indentNumbers);
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
    sb.append("    purchaseType: ").append(toIndentedString(purchaseType)).append("\n");
    sb.append("    rateType: ").append(toIndentedString(rateType)).append("\n");
    sb.append("    supplier: ").append(toIndentedString(supplier)).append("\n");
    sb.append("    advanceAmount: ").append(toIndentedString(advanceAmount)).append("\n");
    sb.append("    totalAdvancePaidAmount: ").append(toIndentedString(totalAdvancePaidAmount)).append("\n");
    sb.append("    advancePercentage: ").append(toIndentedString(advancePercentage)).append("\n");
    sb.append("    expectedDeliveryDate: ").append(toIndentedString(expectedDeliveryDate)).append("\n");
    sb.append("    deliveryTerms: ").append(toIndentedString(deliveryTerms)).append("\n");
    sb.append("    paymentTerms: ").append(toIndentedString(paymentTerms)).append("\n");
    sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    workFlowDetails: ").append(toIndentedString(workFlowDetails)).append("\n");
    sb.append("    stateId: ").append(toIndentedString(stateId)).append("\n");
    sb.append("    purchaseOrderDetails: ").append(toIndentedString(purchaseOrderDetails)).append("\n");
    sb.append("    fileStoreId: ").append(toIndentedString(fileStoreId)).append("\n");
    sb.append("    designation: ").append(toIndentedString(designation)).append("\n");
    sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
    sb.append("    indentNumbers: ").append(toIndentedString(indentNumbers)).append("\n");
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

