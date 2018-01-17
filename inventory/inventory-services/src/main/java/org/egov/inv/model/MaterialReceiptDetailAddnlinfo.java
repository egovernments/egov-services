package org.egov.inv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Hold the material receipt note material level  additional information. This will helpful to maintain/issue material in lot number,expiry date wise, serial number wise and manufacture number wise. This object will be useful in legacy data collection also.
 */
@ApiModel(description = "Hold the material receipt note material level  additional information. This will helpful to maintain/issue material in lot number,expiry date wise, serial number wise and manufacture number wise. This object will be useful in legacy data collection also.")
@javax.annotation.Generated(value = "org.egov.inv.codegen.languages.SpringCodegen", date = "2017-12-27T10:36:36.253Z")

public class MaterialReceiptDetailAddnlinfo {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("lotNo")
    private String lotNo = null;

    @JsonProperty("serialNo")
    private String serialNo = null;

    @JsonProperty("batchNo")
    private String batchNo = null;

    @JsonProperty("manufactureDate")
    private Long manufactureDate = null;

    @JsonProperty("receiptDetailId")
    private String receiptDetailId = null;

    @JsonProperty("oldReceiptNumber")
    private String oldReceiptNumber = null;

    @JsonProperty("receivedDate")
    private Long receivedDate = null;

    @JsonProperty("expiryDate")
    private Long expiryDate = null;

    @JsonProperty("userQuantity")
    private BigDecimal userQuantity = null;

    @JsonProperty("quantity")
    private BigDecimal quantity = null;

    public MaterialReceiptDetailAddnlinfo id(String id) {
        this.id = id;
        return this;
    }

    /**
     * The unique identifier for the receipt details additional information.
     *
     * @return id
     **/
    @ApiModelProperty(value = "The unique identifier for the receipt details additional information.")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MaterialReceiptDetailAddnlinfo tenantId(String tenantId) {
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

    public MaterialReceiptDetailAddnlinfo lotNo(String lotNo) {
        this.lotNo = lotNo;
        return this;
    }

    /**
     * LOT number for the materials that are of LOT control.
     *
     * @return lotNo
     **/
    @ApiModelProperty(value = "LOT number for the materials that are of LOT control.")


    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public MaterialReceiptDetailAddnlinfo serialNo(String serialNo) {
        this.serialNo = serialNo;
        return this;
    }

    /**
     * Serial number of the materials that are received.
     *
     * @return serialNo
     **/
    @ApiModelProperty(value = "Serial number of the materials that are received.")


    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public MaterialReceiptDetailAddnlinfo batchNo(String batchNo) {
        this.batchNo = batchNo;
        return this;
    }

    /**
     * batch number of material receipt detail.
     *
     * @return batchNo
     **/
    @ApiModelProperty(value = "batch number of material receipt detail.")


    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public MaterialReceiptDetailAddnlinfo manufactureDate(Long manufactureDate) {
        this.manufactureDate = manufactureDate;
        return this;
    }

    /**
     * The date on which the materials are received into system.
     *
     * @return manufactureDate
     **/
    @ApiModelProperty(value = "The date on which the materials are received into system.")


    public Long getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(Long manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public MaterialReceiptDetailAddnlinfo receiptDetailId(String receiptDetailId) {
        this.receiptDetailId = receiptDetailId;
        return this;
    }

    /**
     * id reference from receipt detail.
     *
     * @return receiptDetailId
     **/
    @ApiModelProperty(value = "id reference from receipt detail.")


    public String getReceiptDetailId() {
        return receiptDetailId;
    }

    public void setReceiptDetailId(String receiptDetailId) {
        this.receiptDetailId = receiptDetailId;
    }

    public MaterialReceiptDetailAddnlinfo oldReceiptNumber(String oldReceiptNumber) {
        this.oldReceiptNumber = oldReceiptNumber;
        return this;
    }

    /**
     * old receipt number. Field used in opening balance screen.
     *
     * @return oldReceiptNumber
     **/
    @ApiModelProperty(value = "old receipt number. Field used in opening balance screen.")


    public String getOldReceiptNumber() {
        return oldReceiptNumber;
    }

    public void setOldReceiptNumber(String oldReceiptNumber) {
        this.oldReceiptNumber = oldReceiptNumber;
    }

    public MaterialReceiptDetailAddnlinfo receivedDate(Long receivedDate) {
        this.receivedDate = receivedDate;
        return this;
    }

    /**
     * The date on which the materials are manufactured. Useful in case of opening balance screen
     *
     * @return receivedDate
     **/
    @ApiModelProperty(value = "The date on which the materials are manufactured. Useful in case of opening balance screen")


    public Long getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Long receivedDate) {
        this.receivedDate = receivedDate;
    }

    public MaterialReceiptDetailAddnlinfo expiryDate(Long expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    /**
     * The date of expiry of the materials. This is required in case of materials that are of type shelf life control. Date must be greater than the receipt date.
     *
     * @return expiryDate
     **/
    @ApiModelProperty(value = "The date of expiry of the materials. This is required in case of materials that are of type shelf life control. Date must be greater than the receipt date.")


    public Long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Long expiryDate) {
        this.expiryDate = expiryDate;
    }

    public MaterialReceiptDetailAddnlinfo userQuantity(BigDecimal userQuantity) {
        this.userQuantity = userQuantity;
        return this;
    }

    /**
     * The quantity of the material received for this particular purchase order or inter store  transfer .
     *
     * @return userQuantity
     **/
    @ApiModelProperty(value = "The quantity of the material received for this particular purchase order or inter store  transfer .")


    public BigDecimal getUserQuantity() {
        return userQuantity;
    }

    public void setUserQuantity(BigDecimal userQuantity) {
        this.userQuantity = userQuantity;
    }

    public MaterialReceiptDetailAddnlinfo quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    /**
     * The quantity of the material received for this particular purchase order or inter store  transfer in the base UOM.
     *
     * @return quantity
     **/
    @ApiModelProperty(value = "The quantity of the material received for this particular purchase order or inter store  transfer in the base UOM.")


    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MaterialReceiptDetailAddnlinfo materialReceiptDetailAddnlinfo = (MaterialReceiptDetailAddnlinfo) o;
        return Objects.equals(this.id, materialReceiptDetailAddnlinfo.id) &&
                Objects.equals(this.tenantId, materialReceiptDetailAddnlinfo.tenantId) &&
                Objects.equals(this.lotNo, materialReceiptDetailAddnlinfo.lotNo) &&
                Objects.equals(this.serialNo, materialReceiptDetailAddnlinfo.serialNo) &&
                Objects.equals(this.batchNo, materialReceiptDetailAddnlinfo.batchNo) &&
                Objects.equals(this.manufactureDate, materialReceiptDetailAddnlinfo.manufactureDate) &&
                Objects.equals(this.receiptDetailId, materialReceiptDetailAddnlinfo.receiptDetailId) &&
                Objects.equals(this.oldReceiptNumber, materialReceiptDetailAddnlinfo.oldReceiptNumber) &&
                Objects.equals(this.receivedDate, materialReceiptDetailAddnlinfo.receivedDate) &&
                Objects.equals(this.expiryDate, materialReceiptDetailAddnlinfo.expiryDate) &&
                Objects.equals(this.userQuantity, materialReceiptDetailAddnlinfo.userQuantity) &&
                Objects.equals(this.quantity, materialReceiptDetailAddnlinfo.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, lotNo, serialNo, batchNo, manufactureDate, receiptDetailId, oldReceiptNumber, receivedDate, expiryDate, userQuantity, quantity);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MaterialReceiptDetailAddnlinfo {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    lotNo: ").append(toIndentedString(lotNo)).append("\n");
        sb.append("    serialNo: ").append(toIndentedString(serialNo)).append("\n");
        sb.append("    batchNo: ").append(toIndentedString(batchNo)).append("\n");
        sb.append("    manufactureDate: ").append(toIndentedString(manufactureDate)).append("\n");
        sb.append("    receiptDetailId: ").append(toIndentedString(receiptDetailId)).append("\n");
        sb.append("    oldReceiptNumber: ").append(toIndentedString(oldReceiptNumber)).append("\n");
        sb.append("    receivedDate: ").append(toIndentedString(receivedDate)).append("\n");
        sb.append("    expiryDate: ").append(toIndentedString(expiryDate)).append("\n");
        sb.append("    userQuantity: ").append(toIndentedString(userQuantity)).append("\n");
        sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
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

