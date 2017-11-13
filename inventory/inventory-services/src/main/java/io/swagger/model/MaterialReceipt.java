package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.AuditDetails;
import io.swagger.model.MaterialReceiptDetail;
import io.swagger.model.Store;
import io.swagger.model.Supplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Hold the material receipt specific information.
 */
@ApiModel(description = "Hold the material receipt specific information.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-13T06:33:50.051Z")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaterialReceipt   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("mrnNumber")
  private String mrnNumber = null;
  
  @JsonProperty("mrnStatus")
  private String mrnStatus = null;

  @JsonProperty("manualReceiptNumber")
  private String manualReceiptNumber = null;

  @JsonProperty("receiptDate")
  private Long receiptDate = null;

  @JsonProperty("financialYear")
  private String financialYear = null;

  @JsonProperty("manualReceiptDate")
  private Long manualReceiptDate = null;

  /**
   * Different receipt types enumeration. By default the value will be \"PURCHASE RECEIPT\".
   */
  public enum ReceiptTypeEnum {
    PURCHASE_RECEIPT("PURCHASE RECEIPT"),
    
    MISCELLANEOUS_RECEIPT("MISCELLANEOUS RECEIPT"),
    
    INWARD_RECEIPT("INWARD RECEIPT"),
    
    OPENING_BALANCE("OPENING BALANCE");

    private String value;

    ReceiptTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ReceiptTypeEnum fromValue(String text) {
      for (ReceiptTypeEnum b : ReceiptTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("receiptType")
  private ReceiptTypeEnum receiptType = null;

  /**
   * Applicable if receipt type is MISCELLANEOUS RECEIPT 
   */
  public enum ReceiptPurposeEnum {
    RETURN_OF_MATERIALS("RETURN OF MATERIALS"),
    
    SCRAP("SCRAP");

    private String value;

    ReceiptPurposeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ReceiptPurposeEnum fromValue(String text) {
      for (ReceiptPurposeEnum b : ReceiptPurposeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("receiptPurpose")
  private ReceiptPurposeEnum receiptPurpose = null;

  @JsonProperty("receivingStore")
  private Store receivingStore = null;

  @JsonProperty("issueingStore")
  private Store issueingStore = null;

  @JsonProperty("supplier")
  private Supplier supplier = null;

  @JsonProperty("supplierBillNo")
  private String supplierBillNo = null;

  @JsonProperty("supplierBillDate")
  private Long supplierBillDate = null;

  @JsonProperty("challanNo")
  private String challanNo = null;

  @JsonProperty("challanDate")
  private Long challanDate = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("receivedBy")
  private String receivedBy = null;

  @JsonProperty("designation")
  private String designation = null;

  @JsonProperty("bill")
  private String bill = null;

  @JsonProperty("inspectedBy")
  private String inspectedBy = null;

  @JsonProperty("inspectionDate")
  private Long inspectionDate = null;

  @JsonProperty("inspectionRemarks")
  private String inspectionRemarks = null;

  @JsonProperty("totalReceiptValue")
  private BigDecimal totalReceiptValue = null;

  @JsonProperty("receiptDetails")
  private List<MaterialReceiptDetail> receiptDetails = null;

  @JsonProperty("fileStoreId")
  private String fileStoreId = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public MaterialReceipt id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Material Receipt Header 
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Material Receipt Header ")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public MaterialReceipt tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Material Receipt Header
   * @return tenantId
  **/
  @ApiModelProperty(value = "Tenant id of the Material Receipt Header")

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public MaterialReceipt mrnNumber(String mrnNumber) {
    this.mrnNumber = mrnNumber;
    return this;
  }
  public MaterialReceipt mrnStatus(String mrnStatus) {
	    this.mrnStatus = mrnStatus;
	    return this;
	  }
   /**
   * Unique number generated internally on creating a receipt.
   * @return mrnNumber
  **/
  @ApiModelProperty(required = true, readOnly = true, value = "Unique number generated internally on creating a receipt.")
  @NotNull


  public String getMrnNumber() {
    return mrnNumber;
  }

  public void setMrnNumber(String mrnNumber) {
    this.mrnNumber = mrnNumber;
  }
  
  public String getMrnStatus() {
	    return mrnStatus;
	  }
  
  public void setMrnStatus(String mrnStatus) {
	    this.mrnStatus = mrnStatus;
	  }

  public MaterialReceipt manualReceiptNumber(String manualReceiptNumber) {
    this.manualReceiptNumber = manualReceiptNumber;
    return this;
  }

   /**
   * Manual Receipt Number, applicable only for Opening Balanace Entry.
   * @return manualReceiptNumber
  **/
  @ApiModelProperty(value = "Manual Receipt Number, applicable only for Opening Balanace Entry.")

 @Size(max=128)
  public String getManualReceiptNumber() {
    return manualReceiptNumber;
  }

  public void setManualReceiptNumber(String manualReceiptNumber) {
    this.manualReceiptNumber = manualReceiptNumber;
  }

  public MaterialReceipt receiptDate(Long receiptDate) {
    this.receiptDate = receiptDate;
    return this;
  }

   /**
   * The date on which the receipt was made.
   * @return receiptDate
  **/
  @ApiModelProperty(required = true, value = "The date on which the receipt was made.")
  @NotNull


  public Long getReceiptDate() {
    return receiptDate;
  }

  public void setReceiptDate(Long receiptDate) {
    this.receiptDate = receiptDate;
  }

  public MaterialReceipt financialYear(String financialYear) {
    this.financialYear = financialYear;
    return this;
  }

   /**
   * This field holds the financial year information for Opening Balance Entry.
   * @return financialYear
  **/
  @ApiModelProperty(value = "This field holds the financial year information for Opening Balance Entry.")


  public String getFinancialYear() {
    return financialYear;
  }

  public void setFinancialYear(String financialYear) {
    this.financialYear = financialYear;
  }

  public MaterialReceipt manualReceiptDate(Long manualReceiptDate) {
    this.manualReceiptDate = manualReceiptDate;
    return this;
  }

   /**
   * Manual Receipt Date, applicable only for Opening Balance Entry.
   * @return manualReceiptDate
  **/
  @ApiModelProperty(value = "Manual Receipt Date, applicable only for Opening Balance Entry.")


  public Long getManualReceiptDate() {
    return manualReceiptDate;
  }

  public void setManualReceiptDate(Long manualReceiptDate) {
    this.manualReceiptDate = manualReceiptDate;
  }

  public MaterialReceipt receiptType(ReceiptTypeEnum receiptType) {
    this.receiptType = receiptType;
    return this;
  }

   /**
   * Different receipt types enumeration. By default the value will be \"PURCHASE RECEIPT\".
   * @return receiptType
  **/
  @ApiModelProperty(required = true, value = "Different receipt types enumeration. By default the value will be \"PURCHASE RECEIPT\".")
  @NotNull


  public ReceiptTypeEnum getReceiptType() {
    return receiptType;
  }

  public void setReceiptType(ReceiptTypeEnum receiptType) {
    this.receiptType = receiptType;
  }

  public MaterialReceipt receiptPurpose(ReceiptPurposeEnum receiptPurpose) {
    this.receiptPurpose = receiptPurpose;
    return this;
  }

   /**
   * Applicable if receipt type is MISCELLANEOUS RECEIPT 
   * @return receiptPurpose
  **/
  @ApiModelProperty(value = "Applicable if receipt type is MISCELLANEOUS RECEIPT ")


  public ReceiptPurposeEnum getReceiptPurpose() {
    return receiptPurpose;
  }

  public void setReceiptPurpose(ReceiptPurposeEnum receiptPurpose) {
    this.receiptPurpose = receiptPurpose;
  }

  public MaterialReceipt receivingStore(Store receivingStore) {
    this.receivingStore = receivingStore;
    return this;
  }

   /**
   * The unique code of the store that is receiving the materials.
   * @return receivingStore
  **/
  @ApiModelProperty(required = true, value = "The unique code of the store that is receiving the materials.")
  @NotNull

  @Valid

  public Store getReceivingStore() {
    return receivingStore;
  }

  public void setReceivingStore(Store receivingStore) {
    this.receivingStore = receivingStore;
  }

  public MaterialReceipt issueingStore(Store issueingStore) {
    this.issueingStore = issueingStore;
    return this;
  }

   /**
   * The unique code of the store that is Issued the materials.  Useful in case of inter store transfer inward receipt.
   * @return issueingStore
  **/
  @ApiModelProperty(value = "The unique code of the store that is Issued the materials.  Useful in case of inter store transfer inward receipt.")

  @Valid

  public Store getIssueingStore() {
    return issueingStore;
  }

  public void setIssueingStore(Store issueingStore) {
    this.issueingStore = issueingStore;
  }

  public MaterialReceipt supplier(Supplier supplier) {
    this.supplier = supplier;
    return this;
  }

   /**
   * Supplier from whom the receipt is made.
   * @return supplier
  **/
  @ApiModelProperty(value = "Supplier from whom the receipt is made.")

  @Valid

  public Supplier getSupplier() {
    return supplier;
  }

  public void setSupplier(Supplier supplier) {
    this.supplier = supplier;
  }

  public MaterialReceipt supplierBillNo(String supplierBillNo) {
    this.supplierBillNo = supplierBillNo;
    return this;
  }

   /**
   * The bill number what is mentioned in the bill given by the supplier.
   * @return supplierBillNo
  **/
  @ApiModelProperty(value = "The bill number what is mentioned in the bill given by the supplier.")


  public String getSupplierBillNo() {
    return supplierBillNo;
  }

  public void setSupplierBillNo(String supplierBillNo) {
    this.supplierBillNo = supplierBillNo;
  }

  public MaterialReceipt supplierBillDate(Long supplierBillDate) {
    this.supplierBillDate = supplierBillDate;
    return this;
  }

   /**
   * The date on which the supplier has raised the bill for the delivered materials.
   * @return supplierBillDate
  **/
  @ApiModelProperty(value = "The date on which the supplier has raised the bill for the delivered materials.")


  public Long getSupplierBillDate() {
    return supplierBillDate;
  }

  public void setSupplierBillDate(Long supplierBillDate) {
    this.supplierBillDate = supplierBillDate;
  }

  public MaterialReceipt challanNo(String challanNo) {
    this.challanNo = challanNo;
    return this;
  }

   /**
   * The challan number associated with this receipt.
   * @return challanNo
  **/
  @ApiModelProperty(value = "The challan number associated with this receipt.")

 @Size(max=52)
  public String getChallanNo() {
    return challanNo;
  }

  public void setChallanNo(String challanNo) {
    this.challanNo = challanNo;
  }

  public MaterialReceipt challanDate(Long challanDate) {
    this.challanDate = challanDate;
    return this;
  }

   /**
   * Date on which the challan was made.
   * @return challanDate
  **/
  @ApiModelProperty(value = "Date on which the challan was made.")


  public Long getChallanDate() {
    return challanDate;
  }

  public void setChallanDate(Long challanDate) {
    this.challanDate = challanDate;
  }

  public MaterialReceipt description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Any general description pertaining to the material receipt.
   * @return description
  **/
  @ApiModelProperty(value = "Any general description pertaining to the material receipt.")

 @Size(max=512)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public MaterialReceipt receivedBy(String receivedBy) {
    this.receivedBy = receivedBy;
    return this;
  }

   /**
   * The code of the employee who has received the materials in store.
   * @return receivedBy
  **/
  @ApiModelProperty(value = "The code of the employee who has received the materials in store.")


  public String getReceivedBy() {
    return receivedBy;
  }

  public void setReceivedBy(String receivedBy) {
    this.receivedBy = receivedBy;
  }

  public MaterialReceipt designation(String designation) {
    this.designation = designation;
    return this;
  }

   /**
   * Designation of the employee who has received the materials in store.
   * @return designation
  **/
  @ApiModelProperty(value = "Designation of the employee who has received the materials in store.")


  public String getDesignation() {
    return designation;
  }

  public void setDesignation(String designation) {
    this.designation = designation;
  }

  public MaterialReceipt bill(String bill) {
    this.bill = bill;
    return this;
  }

   /**
   * Bill number reference, if bill is generatd in systemm for receipt.
   * @return bill
  **/
  @ApiModelProperty(value = "Bill number reference, if bill is generatd in systemm for receipt.")


  public String getBill() {
    return bill;
  }

  public void setBill(String bill) {
    this.bill = bill;
  }

  public MaterialReceipt inspectedBy(String inspectedBy) {
    this.inspectedBy = inspectedBy;
    return this;
  }

   /**
   * The unique code of the employee who has inspected the receipt of materials.
   * @return inspectedBy
  **/
  @ApiModelProperty(value = "The unique code of the employee who has inspected the receipt of materials.")


  public String getInspectedBy() {
    return inspectedBy;
  }

  public void setInspectedBy(String inspectedBy) {
    this.inspectedBy = inspectedBy;
  }

  public MaterialReceipt inspectionDate(Long inspectionDate) {
    this.inspectionDate = inspectionDate;
    return this;
  }

   /**
   * Date on which the inspection was done.
   * @return inspectionDate
  **/
  @ApiModelProperty(value = "Date on which the inspection was done.")


  public Long getInspectionDate() {
    return inspectionDate;
  }

  public void setInspectionDate(Long inspectionDate) {
    this.inspectionDate = inspectionDate;
  }

  public MaterialReceipt inspectionRemarks(String inspectionRemarks) {
    this.inspectionRemarks = inspectionRemarks;
    return this;
  }

   /**
   * Any remarks made by the inspection authority.
   * @return inspectionRemarks
  **/
  @ApiModelProperty(value = "Any remarks made by the inspection authority.")


  public String getInspectionRemarks() {
    return inspectionRemarks;
  }

  public void setInspectionRemarks(String inspectionRemarks) {
    this.inspectionRemarks = inspectionRemarks;
  }

  public MaterialReceipt totalReceiptValue(BigDecimal totalReceiptValue) {
    this.totalReceiptValue = totalReceiptValue;
    return this;
  }

   /**
   * The sum total of the receipt value. This will be the total value of all the materials that are received as part of this receipt note.
   * @return totalReceiptValue
  **/
  @ApiModelProperty(value = "The sum total of the receipt value. This will be the total value of all the materials that are received as part of this receipt note.")

  @Valid

  public BigDecimal getTotalReceiptValue() {
    return totalReceiptValue;
  }

  public void setTotalReceiptValue(BigDecimal totalReceiptValue) {
    this.totalReceiptValue = totalReceiptValue;
  }

  public MaterialReceipt receiptDetails(List<MaterialReceiptDetail> receiptDetails) {
    this.receiptDetails = receiptDetails;
    return this;
  }

  public MaterialReceipt addReceiptDetailsItem(MaterialReceiptDetail receiptDetailsItem) {
    if (this.receiptDetails == null) {
      this.receiptDetails = new ArrayList<MaterialReceiptDetail>();
    }
    this.receiptDetails.add(receiptDetailsItem);
    return this;
  }

   /**
   * Material and Purchase order details that are associated with this receipt.
   * @return receiptDetails
  **/
  @ApiModelProperty(value = "Material and Purchase order details that are associated with this receipt.")

  @Valid

  public List<MaterialReceiptDetail> getReceiptDetails() {
    return receiptDetails;
  }

  public void setReceiptDetails(List<MaterialReceiptDetail> receiptDetails) {
    this.receiptDetails = receiptDetails;
  }

  public MaterialReceipt fileStoreId(String fileStoreId) {
    this.fileStoreId = fileStoreId;
    return this;
  }

   /**
   * fileStoreId  File Store id of the Material Receipt     
   * @return fileStoreId
  **/
  @ApiModelProperty(value = "fileStoreId  File Store id of the Material Receipt     ")


  public String getFileStoreId() {
    return fileStoreId;
  }

  public void setFileStoreId(String fileStoreId) {
    this.fileStoreId = fileStoreId;
  }

  public MaterialReceipt auditDetails(AuditDetails auditDetails) {
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
    MaterialReceipt materialReceipt = (MaterialReceipt) o;
    return Objects.equals(this.id, materialReceipt.id) &&
        Objects.equals(this.tenantId, materialReceipt.tenantId) &&
        Objects.equals(this.mrnNumber, materialReceipt.mrnNumber) &&
        Objects.equals(this.mrnStatus, materialReceipt.mrnStatus) &&
        Objects.equals(this.manualReceiptNumber, materialReceipt.manualReceiptNumber) &&
        Objects.equals(this.receiptDate, materialReceipt.receiptDate) &&
        Objects.equals(this.financialYear, materialReceipt.financialYear) &&
        Objects.equals(this.manualReceiptDate, materialReceipt.manualReceiptDate) &&
        Objects.equals(this.receiptType, materialReceipt.receiptType) &&
        Objects.equals(this.receiptPurpose, materialReceipt.receiptPurpose) &&
        Objects.equals(this.receivingStore, materialReceipt.receivingStore) &&
        Objects.equals(this.issueingStore, materialReceipt.issueingStore) &&
        Objects.equals(this.supplier, materialReceipt.supplier) &&
        Objects.equals(this.supplierBillNo, materialReceipt.supplierBillNo) &&
        Objects.equals(this.supplierBillDate, materialReceipt.supplierBillDate) &&
        Objects.equals(this.challanNo, materialReceipt.challanNo) &&
        Objects.equals(this.challanDate, materialReceipt.challanDate) &&
        Objects.equals(this.description, materialReceipt.description) &&
        Objects.equals(this.receivedBy, materialReceipt.receivedBy) &&
        Objects.equals(this.designation, materialReceipt.designation) &&
        Objects.equals(this.bill, materialReceipt.bill) &&
        Objects.equals(this.inspectedBy, materialReceipt.inspectedBy) &&
        Objects.equals(this.inspectionDate, materialReceipt.inspectionDate) &&
        Objects.equals(this.inspectionRemarks, materialReceipt.inspectionRemarks) &&
        Objects.equals(this.totalReceiptValue, materialReceipt.totalReceiptValue) &&
        Objects.equals(this.receiptDetails, materialReceipt.receiptDetails) &&
        Objects.equals(this.fileStoreId, materialReceipt.fileStoreId) &&
        Objects.equals(this.auditDetails, materialReceipt.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, mrnNumber,mrnStatus, manualReceiptNumber, receiptDate, financialYear, manualReceiptDate, receiptType, receiptPurpose, receivingStore, issueingStore, supplier, supplierBillNo, supplierBillDate, challanNo, challanDate, description, receivedBy, designation, bill, inspectedBy, inspectionDate, inspectionRemarks, totalReceiptValue, receiptDetails, fileStoreId, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaterialReceipt {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    mrnNumber: ").append(toIndentedString(mrnNumber)).append("\n");
    sb.append("    mrnStatus: ").append(toIndentedString(mrnStatus)).append("\n");
    sb.append("    manualReceiptNumber: ").append(toIndentedString(manualReceiptNumber)).append("\n");
    sb.append("    receiptDate: ").append(toIndentedString(receiptDate)).append("\n");
    sb.append("    financialYear: ").append(toIndentedString(financialYear)).append("\n");
    sb.append("    manualReceiptDate: ").append(toIndentedString(manualReceiptDate)).append("\n");
    sb.append("    receiptType: ").append(toIndentedString(receiptType)).append("\n");
    sb.append("    receiptPurpose: ").append(toIndentedString(receiptPurpose)).append("\n");
    sb.append("    receivingStore: ").append(toIndentedString(receivingStore)).append("\n");
    sb.append("    issueingStore: ").append(toIndentedString(issueingStore)).append("\n");
    sb.append("    supplier: ").append(toIndentedString(supplier)).append("\n");
    sb.append("    supplierBillNo: ").append(toIndentedString(supplierBillNo)).append("\n");
    sb.append("    supplierBillDate: ").append(toIndentedString(supplierBillDate)).append("\n");
    sb.append("    challanNo: ").append(toIndentedString(challanNo)).append("\n");
    sb.append("    challanDate: ").append(toIndentedString(challanDate)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    receivedBy: ").append(toIndentedString(receivedBy)).append("\n");
    sb.append("    designation: ").append(toIndentedString(designation)).append("\n");
    sb.append("    bill: ").append(toIndentedString(bill)).append("\n");
    sb.append("    inspectedBy: ").append(toIndentedString(inspectedBy)).append("\n");
    sb.append("    inspectionDate: ").append(toIndentedString(inspectionDate)).append("\n");
    sb.append("    inspectionRemarks: ").append(toIndentedString(inspectionRemarks)).append("\n");
    sb.append("    totalReceiptValue: ").append(toIndentedString(totalReceiptValue)).append("\n");
    sb.append("    receiptDetails: ").append(toIndentedString(receiptDetails)).append("\n");
    sb.append("    fileStoreId: ").append(toIndentedString(fileStoreId)).append("\n");
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

