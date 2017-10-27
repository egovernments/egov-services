package org.egov.inv.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Hold the material receipt specific information.
 */
@ApiModel(description = "Hold the material receipt specific information.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-27T06:37:03.617Z")

public class MaterialReceiptHeader {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("mrnNumber")
    private String mrnNumber = null;

    @JsonProperty("receiptDate")
    private Long receiptDate = null;
    @JsonProperty("receiptType")
    private ReceiptTypeEnum receiptType = null;
    @JsonProperty("receivingStore")
    private String receivingStore = null;
    @JsonProperty("supplierCode")
    private String supplierCode = null;
    @JsonProperty("supplierBillNo")
    private String supplierBillNo = null;
    @JsonProperty("supplierBillDate")
    private Long supplierBillDate = null;
    @JsonProperty("challanNo")
    private Boolean challanNo = null;
    @JsonProperty("challanDate")
    private Long challanDate = null;
    @JsonProperty("description")
    private String description = null;
    @JsonProperty("receivedBy")
    private String receivedBy = null;
    @JsonProperty("designation")
    private String designation = null;
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
    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    public MaterialReceiptHeader id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Material Receipt Header
     *
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Material Receipt Header ")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MaterialReceiptHeader tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Material Receipt Header
     *
     * @return tenantId
     **/
    @ApiModelProperty(value = "Tenant id of the Material Receipt Header")

    @Size(min = 4, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public MaterialReceiptHeader mrnNumber(String mrnNumber) {
        this.mrnNumber = mrnNumber;
        return this;
    }

    /**
     * Unique number generated internally on creating a receipt.
     *
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

    public MaterialReceiptHeader receiptDate(Long receiptDate) {
        this.receiptDate = receiptDate;
        return this;
    }

    /**
     * The date on which the receipt was made.
     *
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

    public MaterialReceiptHeader receiptType(ReceiptTypeEnum receiptType) {
        this.receiptType = receiptType;
        return this;
    }

    /**
     * Different receipt types enumeration. By default the value will be \"PURCHASE RECEIPT\".
     *
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

    public MaterialReceiptHeader receivingStore(String receivingStore) {
        this.receivingStore = receivingStore;
        return this;
    }

    /**
     * The unique code of the store that is receiving the materials.
     *
     * @return receivingStore
     **/
    @ApiModelProperty(required = true, value = "The unique code of the store that is receiving the materials.")
    @NotNull


    public String getReceivingStore() {
        return receivingStore;
    }

    public void setReceivingStore(String receivingStore) {
        this.receivingStore = receivingStore;
    }

    public MaterialReceiptHeader supplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
        return this;
    }

    /**
     * Code of the supplier from whom the receipt is made.
     *
     * @return supplierCode
     **/
    @ApiModelProperty(value = "Code of the supplier from whom the receipt is made.")


    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public MaterialReceiptHeader supplierBillNo(String supplierBillNo) {
        this.supplierBillNo = supplierBillNo;
        return this;
    }

    /**
     * The bill number what is mentioned in the bill given by the supplier.
     *
     * @return supplierBillNo
     **/
    @ApiModelProperty(value = "The bill number what is mentioned in the bill given by the supplier.")


    public String getSupplierBillNo() {
        return supplierBillNo;
    }

    public void setSupplierBillNo(String supplierBillNo) {
        this.supplierBillNo = supplierBillNo;
    }

    public MaterialReceiptHeader supplierBillDate(Long supplierBillDate) {
        this.supplierBillDate = supplierBillDate;
        return this;
    }

    /**
     * The date on which the supplier has raised the bill for the delivered materials.
     *
     * @return supplierBillDate
     **/
    @ApiModelProperty(value = "The date on which the supplier has raised the bill for the delivered materials.")


    public Long getSupplierBillDate() {
        return supplierBillDate;
    }

    public void setSupplierBillDate(Long supplierBillDate) {
        this.supplierBillDate = supplierBillDate;
    }

    public MaterialReceiptHeader challanNo(Boolean challanNo) {
        this.challanNo = challanNo;
        return this;
    }

    /**
     * The challan number associated with this receipt.
     *
     * @return challanNo
     **/
    @ApiModelProperty(value = "The challan number associated with this receipt.")


    public Boolean getChallanNo() {
        return challanNo;
    }

    public void setChallanNo(Boolean challanNo) {
        this.challanNo = challanNo;
    }

    public MaterialReceiptHeader challanDate(Long challanDate) {
        this.challanDate = challanDate;
        return this;
    }

    /**
     * Date on which the challan was made.
     *
     * @return challanDate
     **/
    @ApiModelProperty(value = "Date on which the challan was made.")


    public Long getChallanDate() {
        return challanDate;
    }

    public void setChallanDate(Long challanDate) {
        this.challanDate = challanDate;
    }

    public MaterialReceiptHeader description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Any general description pertaining to the material receipt.
     *
     * @return description
     **/
    @ApiModelProperty(value = "Any general description pertaining to the material receipt.")

    @Size(max = 1000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MaterialReceiptHeader receivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
        return this;
    }

    /**
     * The code of the employee who has received the materials in store.
     *
     * @return receivedBy
     **/
    @ApiModelProperty(value = "The code of the employee who has received the materials in store.")


    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public MaterialReceiptHeader designation(String designation) {
        this.designation = designation;
        return this;
    }

    /**
     * Designation of the employee who has received the materials in store.
     *
     * @return designation
     **/
    @ApiModelProperty(value = "Designation of the employee who has received the materials in store.")


    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public MaterialReceiptHeader inspectedBy(String inspectedBy) {
        this.inspectedBy = inspectedBy;
        return this;
    }

    /**
     * The unique code of the employee who has inspected the receipt of materials.
     *
     * @return inspectedBy
     **/
    @ApiModelProperty(value = "The unique code of the employee who has inspected the receipt of materials.")


    public String getInspectedBy() {
        return inspectedBy;
    }

    public void setInspectedBy(String inspectedBy) {
        this.inspectedBy = inspectedBy;
    }

    public MaterialReceiptHeader inspectionDate(Long inspectionDate) {
        this.inspectionDate = inspectionDate;
        return this;
    }

    /**
     * Date on which the inspection was done.
     *
     * @return inspectionDate
     **/
    @ApiModelProperty(value = "Date on which the inspection was done.")


    public Long getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(Long inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public MaterialReceiptHeader inspectionRemarks(String inspectionRemarks) {
        this.inspectionRemarks = inspectionRemarks;
        return this;
    }

    /**
     * Any remarks made by the inspection authority.
     *
     * @return inspectionRemarks
     **/
    @ApiModelProperty(value = "Any remarks made by the inspection authority.")


    public String getInspectionRemarks() {
        return inspectionRemarks;
    }

    public void setInspectionRemarks(String inspectionRemarks) {
        this.inspectionRemarks = inspectionRemarks;
    }

    public MaterialReceiptHeader totalReceiptValue(BigDecimal totalReceiptValue) {
        this.totalReceiptValue = totalReceiptValue;
        return this;
    }

    /**
     * The sum total of the receipt value. This will be the total value of all the materials that are received as part of this receipt note.
     *
     * @return totalReceiptValue
     **/
    @ApiModelProperty(required = true, value = "The sum total of the receipt value. This will be the total value of all the materials that are received as part of this receipt note.")
    @NotNull

    @Valid

    public BigDecimal getTotalReceiptValue() {
        return totalReceiptValue;
    }

    public void setTotalReceiptValue(BigDecimal totalReceiptValue) {
        this.totalReceiptValue = totalReceiptValue;
    }

    public MaterialReceiptHeader receiptDetails(List<MaterialReceiptDetail> receiptDetails) {
        this.receiptDetails = receiptDetails;
        return this;
    }

    public MaterialReceiptHeader addReceiptDetailsItem(MaterialReceiptDetail receiptDetailsItem) {
        if (this.receiptDetails == null) {
            this.receiptDetails = new ArrayList<MaterialReceiptDetail>();
        }
        this.receiptDetails.add(receiptDetailsItem);
        return this;
    }

    /**
     * Material and Purchase order details that are associated with this receipt.
     *
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

    public MaterialReceiptHeader auditDetails(AuditDetails auditDetails) {
        this.auditDetails = auditDetails;
        return this;
    }

    /**
     * Get auditDetails
     *
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MaterialReceiptHeader materialReceiptHeader = (MaterialReceiptHeader) o;
        return Objects.equals(this.id, materialReceiptHeader.id) &&
                Objects.equals(this.tenantId, materialReceiptHeader.tenantId) &&
                Objects.equals(this.mrnNumber, materialReceiptHeader.mrnNumber) &&
                Objects.equals(this.receiptDate, materialReceiptHeader.receiptDate) &&
                Objects.equals(this.receiptType, materialReceiptHeader.receiptType) &&
                Objects.equals(this.receivingStore, materialReceiptHeader.receivingStore) &&
                Objects.equals(this.supplierCode, materialReceiptHeader.supplierCode) &&
                Objects.equals(this.supplierBillNo, materialReceiptHeader.supplierBillNo) &&
                Objects.equals(this.supplierBillDate, materialReceiptHeader.supplierBillDate) &&
                Objects.equals(this.challanNo, materialReceiptHeader.challanNo) &&
                Objects.equals(this.challanDate, materialReceiptHeader.challanDate) &&
                Objects.equals(this.description, materialReceiptHeader.description) &&
                Objects.equals(this.receivedBy, materialReceiptHeader.receivedBy) &&
                Objects.equals(this.designation, materialReceiptHeader.designation) &&
                Objects.equals(this.inspectedBy, materialReceiptHeader.inspectedBy) &&
                Objects.equals(this.inspectionDate, materialReceiptHeader.inspectionDate) &&
                Objects.equals(this.inspectionRemarks, materialReceiptHeader.inspectionRemarks) &&
                Objects.equals(this.totalReceiptValue, materialReceiptHeader.totalReceiptValue) &&
                Objects.equals(this.receiptDetails, materialReceiptHeader.receiptDetails) &&
                Objects.equals(this.auditDetails, materialReceiptHeader.auditDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, mrnNumber, receiptDate, receiptType, receivingStore, supplierCode, supplierBillNo, supplierBillDate, challanNo, challanDate, description, receivedBy, designation, inspectedBy, inspectionDate, inspectionRemarks, totalReceiptValue, receiptDetails, auditDetails);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MaterialReceiptHeader {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    mrnNumber: ").append(toIndentedString(mrnNumber)).append("\n");
        sb.append("    receiptDate: ").append(toIndentedString(receiptDate)).append("\n");
        sb.append("    receiptType: ").append(toIndentedString(receiptType)).append("\n");
        sb.append("    receivingStore: ").append(toIndentedString(receivingStore)).append("\n");
        sb.append("    supplierCode: ").append(toIndentedString(supplierCode)).append("\n");
        sb.append("    supplierBillNo: ").append(toIndentedString(supplierBillNo)).append("\n");
        sb.append("    supplierBillDate: ").append(toIndentedString(supplierBillDate)).append("\n");
        sb.append("    challanNo: ").append(toIndentedString(challanNo)).append("\n");
        sb.append("    challanDate: ").append(toIndentedString(challanDate)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    receivedBy: ").append(toIndentedString(receivedBy)).append("\n");
        sb.append("    designation: ").append(toIndentedString(designation)).append("\n");
        sb.append("    inspectedBy: ").append(toIndentedString(inspectedBy)).append("\n");
        sb.append("    inspectionDate: ").append(toIndentedString(inspectionDate)).append("\n");
        sb.append("    inspectionRemarks: ").append(toIndentedString(inspectionRemarks)).append("\n");
        sb.append("    totalReceiptValue: ").append(toIndentedString(totalReceiptValue)).append("\n");
        sb.append("    receiptDetails: ").append(toIndentedString(receiptDetails)).append("\n");
        sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

    /**
     * Different receipt types enumeration. By default the value will be \"PURCHASE RECEIPT\".
     */
    public enum ReceiptTypeEnum {
        PURCHASE_RECEIPT("PURCHASE RECEIPT"),

        RETURN_OF_MATERIALS("RETURN OF MATERIALS"),

        SCRAP("SCRAP");

        private String value;

        ReceiptTypeEnum(String value) {
            this.value = value;
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

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }
    }
}

