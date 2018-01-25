package org.egov.works.measurementbook.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An Object that holds the basic data for Measurement Book
 */
@ApiModel(description = "An Object that holds the basic data for Measurement Book")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-26T07:36:21.338Z")

public class MeasurementBook   {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("mbRefNo")
    private String mbRefNo = null;

    @JsonProperty("contractorComments")
    private String contractorComments = null;

    @JsonProperty("mbDate")
    private Long mbDate = null;

    @JsonProperty("mbIssuedDate")
    private Long mbIssuedDate = null;

    @JsonProperty("mbAbstract")
    private String mbAbstract = null;

    @JsonProperty("fromPageNo")
    private Integer fromPageNo = null;

    @JsonProperty("toPageNo")
    private Integer toPageNo = null;

    @JsonProperty("letterOfAcceptanceEstimate")
    private LetterOfAcceptanceEstimate letterOfAcceptanceEstimate = null;

    @JsonProperty("revisionLOA")
    private String revisionLOA = null;

    @JsonProperty("status")
    private WorksStatus status = null;

    @JsonProperty("measurementBookDetails")
    private List<MeasurementBookDetail> measurementBookDetails = null;

    @JsonProperty("lumpSumMBDetails")
    private List<MeasurementBookDetail> lumpSumMBDetails = null;

    @JsonProperty("isLegacyMB")
    private Boolean isLegacyMB = null;

    @JsonProperty("mbAmount")
    private BigDecimal mbAmount = null;

    @JsonProperty("approvedDate")
    private Long approvedDate = null;

    @JsonProperty("documentDetails")
    private List<DocumentDetail> documentDetails = null;

    @JsonProperty("workFlowDetails")
    private WorkFlowDetails workFlowDetails = null;

    @JsonProperty("stateId")
    private String stateId = null;

    @JsonProperty("cancellationReason")
    private String cancellationReason = null;

    @JsonProperty("cancellationRemarks")
    private String cancellationRemarks = null;

    @JsonProperty("billNumber")
    private String billNumber = null;

    @JsonProperty("billDate")
    private Long billDate = null;

    @JsonProperty("isPartRate")
    private Boolean isPartRate = false;

    @JsonProperty("isBillCreated")
    private Boolean isBillCreated = false;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    @JsonProperty("deleted")
    private Boolean deleted = false;

    public MeasurementBook id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Measurement Book
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Measurement Book")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MeasurementBook tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Measurement Book
     * @return tenantId
     **/
    @ApiModelProperty(required = true, value = "Tenant id of the Measurement Book")
    @NotNull

    @Size(min=2,max=128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public MeasurementBook mbRefNo(String mbRefNo) {
        this.mbRefNo = mbRefNo;
        return this;
    }

    /**
     * Reference number for the Measurement book.
     * @return mbRefNo
     **/
    @ApiModelProperty(required = true, value = "Reference number for the Measurement book.")
    @NotNull

    @Pattern(regexp="[a-zA-Z0-9-/]+") @Size(min=1,max=50)
    public String getMbRefNo() {
        return mbRefNo;
    }

    public void setMbRefNo(String mbRefNo) {
        this.mbRefNo = mbRefNo;
    }

    public MeasurementBook contractorComments(String contractorComments) {
        this.contractorComments = contractorComments;
        return this;
    }

    /**
     * Comments for the measurement book
     * @return contractorComments
     **/
    @ApiModelProperty(value = "Comments for the measurement book")

    @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(min=1,max=1024)
    public String getContractorComments() {
        return contractorComments;
    }

    public void setContractorComments(String contractorComments) {
        this.contractorComments = contractorComments;
    }

    public MeasurementBook mbDate(Long mbDate) {
        this.mbDate = mbDate;
        return this;
    }

    /**
     * Epoch time of when the measurement book created
     * @return mbDate
     **/
    @ApiModelProperty(required = true, value = "Epoch time of when the measurement book created")
    @NotNull


    public Long getMbDate() {
        return mbDate;
    }

    public void setMbDate(Long mbDate) {
        this.mbDate = mbDate;
    }

    public MeasurementBook mbIssuedDate(Long mbIssuedDate) {
        this.mbIssuedDate = mbIssuedDate;
        return this;
    }

    /**
     * Epoch time of Issued date of the measurement book
     * @return mbIssuedDate
     **/
    @ApiModelProperty(value = "Epoch time of Issued date of the measurement book")


    public Long getMbIssuedDate() {
        return mbIssuedDate;
    }

    public void setMbIssuedDate(Long mbIssuedDate) {
        this.mbIssuedDate = mbIssuedDate;
    }

    public MeasurementBook mbAbstract(String mbAbstract) {
        this.mbAbstract = mbAbstract;
        return this;
    }

    /**
     * Abstract comments for the measurement book
     * @return mbAbstract
     **/
    @ApiModelProperty(required = true, value = "Abstract comments for the measurement book")
    @NotNull

    @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(min=1,max=1024)
    public String getMbAbstract() {
        return mbAbstract;
    }

    public void setMbAbstract(String mbAbstract) {
        this.mbAbstract = mbAbstract;
    }

    public MeasurementBook fromPageNo(Integer fromPageNo) {
        this.fromPageNo = fromPageNo;
        return this;
    }

    /**
     * From page number for the measurement book
     * @return fromPageNo
     **/
    @ApiModelProperty(required = true, value = "From page number for the measurement book")
    @NotNull


    public Integer getFromPageNo() {
        return fromPageNo;
    }

    public void setFromPageNo(Integer fromPageNo) {
        this.fromPageNo = fromPageNo;
    }

    public MeasurementBook toPageNo(Integer toPageNo) {
        this.toPageNo = toPageNo;
        return this;
    }

    /**
     * To page number for the measurement book
     * @return toPageNo
     **/
    @ApiModelProperty(value = "To page number for the measurement book")


    public Integer getToPageNo() {
        return toPageNo;
    }

    public void setToPageNo(Integer toPageNo) {
        this.toPageNo = toPageNo;
    }

    public MeasurementBook letterOfAcceptanceEstimate(LetterOfAcceptanceEstimate letterOfAcceptanceEstimate) {
        this.letterOfAcceptanceEstimate = letterOfAcceptanceEstimate;
        return this;
    }

    /**
     * Reference of Letter of Acceptance Estimate
     * @return letterOfAcceptanceEstimate
     **/
    @ApiModelProperty(required = true, value = "Reference of Letter of Acceptance Estimate")
    @NotNull

//  @Valid

    public LetterOfAcceptanceEstimate getLetterOfAcceptanceEstimate() {
        return letterOfAcceptanceEstimate;
    }

    public void setLetterOfAcceptanceEstimate(LetterOfAcceptanceEstimate letterOfAcceptanceEstimate) {
        this.letterOfAcceptanceEstimate = letterOfAcceptanceEstimate;
    }

    public String getRevisionLOA() {
        return revisionLOA;
    }

    public void setRevisionLOA(String revisionLOA) {
        this.revisionLOA = revisionLOA;
    }

    public MeasurementBook status(WorksStatus status) {
        this.status = status;
        return this;
    }

    /**
     * Status of the Measurement Book
     * @return status
     **/
    @ApiModelProperty(required = true, value = "Status of the Measurement Book")
    @NotNull

    @Valid

    public WorksStatus getStatus() {
        return status;
    }

    public void setStatus(WorksStatus status) {
        this.status = status;
    }

    public MeasurementBook measurementBookDetails(List<MeasurementBookDetail> measurementBookDetails) {
        this.measurementBookDetails = measurementBookDetails;
        return this;
    }

    public MeasurementBook addMeasurementBookDetailsItem(MeasurementBookDetail measurementBookDetailsItem) {
        if (this.measurementBookDetails == null) {
            this.measurementBookDetails = new ArrayList<MeasurementBookDetail>();
        }
        this.measurementBookDetails.add(measurementBookDetailsItem);
        return this;
    }

    /**
     * Array of Measurement Book Details
     * @return measurementBookDetails
     **/
    @ApiModelProperty(value = "Array of Measurement Book Details")

//  @Valid
    @Size(min=1)
    public List<MeasurementBookDetail> getMeasurementBookDetails() {
        return measurementBookDetails;
    }

    public void setMeasurementBookDetails(List<MeasurementBookDetail> measurementBookDetails) {
        this.measurementBookDetails = measurementBookDetails;
    }



    public List<MeasurementBookDetail> getLumpSumMBDetails() {
        return lumpSumMBDetails;
    }

    public void setLumpSumMBDetails(List<MeasurementBookDetail> lumpSumMBDetails) {
        this.lumpSumMBDetails = lumpSumMBDetails;
    }

    public MeasurementBook isLegacyMB(Boolean isLegacyMB) {
        this.isLegacyMB = isLegacyMB;
        return this;
    }

    /**
     * True if Measurement Book is legacy record
     * @return isLegacyMB
     **/
    @ApiModelProperty(value = "True if Measurement Book is legacy record")


    public Boolean getIsLegacyMB() {
        return isLegacyMB;
    }

    public void setIsLegacyMB(Boolean isLegacyMB) {
        this.isLegacyMB = isLegacyMB;
    }

    public MeasurementBook mbAmount(BigDecimal mbAmount) {
        this.mbAmount = mbAmount;
        return this;
    }

    /**
     * Amount for the Measurement book.
     * @return mbAmount
     **/
    @ApiModelProperty(required = true, value = "Amount for the Measurement book.")
    @NotNull

    @Valid

    public BigDecimal getMbAmount() {
        return mbAmount;
    }

    public void setMbAmount(BigDecimal mbAmount) {
        this.mbAmount = mbAmount;
    }

    public MeasurementBook approvedDate(Long approvedDate) {
        this.approvedDate = approvedDate;
        return this;
    }

    /**
     * Epoch time of the Measurement book Approved date
     * @return approvedDate
     **/
    @ApiModelProperty(value = "Epoch time of the Measurement book Approved date")


    public Long getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Long approvedDate) {
        this.approvedDate = approvedDate;
    }

    public MeasurementBook documentDetails(List<DocumentDetail> documentDetails) {
        this.documentDetails = documentDetails;
        return this;
    }

    public MeasurementBook addDocumentDetailsItem(DocumentDetail documentDetailsItem) {
        if (this.documentDetails == null) {
            this.documentDetails = new ArrayList<DocumentDetail>();
        }
        this.documentDetails.add(documentDetailsItem);
        return this;
    }

    /**
     * Array of document details
     * @return documentDetails
     **/
    @ApiModelProperty(value = "Array of document details")

//  @Valid

    public List<DocumentDetail> getDocumentDetails() {
        return documentDetails;
    }

    public void setDocumentDetails(List<DocumentDetail> documentDetails) {
        this.documentDetails = documentDetails;
    }

    public MeasurementBook workFlowDetails(WorkFlowDetails workFlowDetails) {
        this.workFlowDetails = workFlowDetails;
        return this;
    }

    /**
     * Get workFlowDetails
     * @return workFlowDetails
     **/
    @ApiModelProperty(value = "")

    @Valid

    public WorkFlowDetails getWorkFlowDetails() {
        return workFlowDetails;
    }

    public void setWorkFlowDetails(WorkFlowDetails workFlowDetails) {
        this.workFlowDetails = workFlowDetails;
    }

    public MeasurementBook stateId(String stateId) {
        this.stateId = stateId;
        return this;
    }

    /**
     * State id of the workflow
     * @return stateId
     **/
    @ApiModelProperty(value = "State id of the workflow")


    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public MeasurementBook cancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
        return this;
    }

    /**
     * Reason for cancellation of the Measurement book, Required only while cancelling Measurement book
     * @return cancellationReason
     **/
    @ApiModelProperty(value = "Reason for cancellation of the Measurement book, Required only while cancelling Measurement book")

    @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(max=100)
    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public MeasurementBook cancellationRemarks(String cancellationRemarks) {
        this.cancellationRemarks = cancellationRemarks;
        return this;
    }

    /**
     * Remarks for cancellation of the Measurement book, Required only while cancelling Measurement book
     * @return cancellationRemarks
     **/
    @ApiModelProperty(value = "Remarks for cancellation of the Measurement book, Required only while cancelling Measurement book")

    @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(max=512)
    public String getCancellationRemarks() {
        return cancellationRemarks;
    }

    public void setCancellationRemarks(String cancellationRemarks) {
        this.cancellationRemarks = cancellationRemarks;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public Long getBillDate() {
        return billDate;
    }

    public void setBillDate(Long billDate) {
        this.billDate = billDate;
    }

    public MeasurementBook isPartRate(Boolean isPartRate) {
        this.isPartRate = isPartRate;
        return this;
    }

    /**
     * If any one of MeasurementBookDetail has Part Rate then MeasurementBook marked as part rated.
     * @return isPartRate
     **/
    @ApiModelProperty(value = "If any one of MeasurementBookDetail has Part Rate then MeasurementBook marked as part rated.")


    public Boolean getIsPartRate() {
        return isPartRate;
    }

    public void setIsPartRate(Boolean isPartRate) {
        this.isPartRate = isPartRate;
    }

    public MeasurementBook isBillCreated(Boolean isBillCreated) {
        this.isBillCreated = isBillCreated;
        return this;
    }

    /**
     * If bill is created for a this MB, value will be updated to true.
     * @return isBillCreated
     **/
    @ApiModelProperty(value = "If any one of MeasurementBookDetail has Part Rate then MeasurementBook marked as part rated.")


    public Boolean getIsBillCreated() {
        return isBillCreated;
    }

    public void setIsBillCreated(Boolean isBillCreated) {
        this.isBillCreated = isBillCreated;
    }

    public MeasurementBook auditDetails(AuditDetails auditDetails) {
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


    /**
     * Boolean value to identify whether the object is deleted or not from UI.
     * @return deleted
     **/
    @ApiModelProperty(value = "Boolean value to identify whether the object is deleted or not from UI.")


    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MeasurementBook measurementBook = (MeasurementBook) o;
        return Objects.equals(this.id, measurementBook.id) &&
                Objects.equals(this.tenantId, measurementBook.tenantId) &&
                Objects.equals(this.mbRefNo, measurementBook.mbRefNo) &&
                Objects.equals(this.contractorComments, measurementBook.contractorComments) &&
                Objects.equals(this.mbDate, measurementBook.mbDate) &&
                Objects.equals(this.mbIssuedDate, measurementBook.mbIssuedDate) &&
                Objects.equals(this.mbAbstract, measurementBook.mbAbstract) &&
                Objects.equals(this.fromPageNo, measurementBook.fromPageNo) &&
                Objects.equals(this.toPageNo, measurementBook.toPageNo) &&
                Objects.equals(this.letterOfAcceptanceEstimate, measurementBook.letterOfAcceptanceEstimate) &&
                Objects.equals(this.revisionLOA, measurementBook.revisionLOA) &&
                Objects.equals(this.status, measurementBook.status) &&
                Objects.equals(this.measurementBookDetails, measurementBook.measurementBookDetails) &&
                Objects.equals(this.isLegacyMB, measurementBook.isLegacyMB) &&
                Objects.equals(this.mbAmount, measurementBook.mbAmount) &&
                Objects.equals(this.approvedDate, measurementBook.approvedDate) &&
                Objects.equals(this.documentDetails, measurementBook.documentDetails) &&
                Objects.equals(this.workFlowDetails, measurementBook.workFlowDetails) &&
                Objects.equals(this.stateId, measurementBook.stateId) &&
                Objects.equals(this.cancellationReason, measurementBook.cancellationReason) &&
                Objects.equals(this.cancellationRemarks, measurementBook.cancellationRemarks) &&
                Objects.equals(this.isPartRate, measurementBook.isPartRate) &&
                Objects.equals(this.isBillCreated, measurementBook.isBillCreated) &&
                Objects.equals(this.auditDetails, measurementBook.auditDetails) &&
                Objects.equals(this.deleted, measurementBook.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, mbRefNo, contractorComments, mbDate, mbIssuedDate, mbAbstract, fromPageNo, toPageNo, letterOfAcceptanceEstimate, revisionLOA, status, measurementBookDetails, isLegacyMB, mbAmount, approvedDate, documentDetails, workFlowDetails, stateId, cancellationReason, cancellationRemarks, isPartRate, isBillCreated, auditDetails, deleted);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MeasurementBook {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    mbRefNo: ").append(toIndentedString(mbRefNo)).append("\n");
        sb.append("    contractorComments: ").append(toIndentedString(contractorComments)).append("\n");
        sb.append("    mbDate: ").append(toIndentedString(mbDate)).append("\n");
        sb.append("    mbIssuedDate: ").append(toIndentedString(mbIssuedDate)).append("\n");
        sb.append("    mbAbstract: ").append(toIndentedString(mbAbstract)).append("\n");
        sb.append("    fromPageNo: ").append(toIndentedString(fromPageNo)).append("\n");
        sb.append("    toPageNo: ").append(toIndentedString(toPageNo)).append("\n");
        sb.append("    letterOfAcceptanceEstimate: ").append(toIndentedString(letterOfAcceptanceEstimate)).append("\n");
        sb.append("    revisionLOA: ").append(toIndentedString(revisionLOA)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    measurementBookDetails: ").append(toIndentedString(measurementBookDetails)).append("\n");
        sb.append("    isLegacyMB: ").append(toIndentedString(isLegacyMB)).append("\n");
        sb.append("    mbAmount: ").append(toIndentedString(mbAmount)).append("\n");
        sb.append("    approvedDate: ").append(toIndentedString(approvedDate)).append("\n");
        sb.append("    documentDetails: ").append(toIndentedString(documentDetails)).append("\n");
        sb.append("    workFlowDetails: ").append(toIndentedString(workFlowDetails)).append("\n");
        sb.append("    stateId: ").append(toIndentedString(stateId)).append("\n");
        sb.append("    cancellationReason: ").append(toIndentedString(cancellationReason)).append("\n");
        sb.append("    cancellationRemarks: ").append(toIndentedString(cancellationRemarks)).append("\n");
        sb.append("    isPartRate: ").append(toIndentedString(isPartRate)).append("\n");
        sb.append("    isBillCreated: ").append(toIndentedString(isBillCreated)).append("\n");
        sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
        sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
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
}