package org.egov.works.workorder.web.contract;

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
 * An Object holds the basic data of Letter Of Acceptance
 */
@ApiModel(description = "An Object holds the basic data of Letter Of Acceptance")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-21T14:55:23.877Z")

public class LetterOfAcceptance {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("contractor")
    private Contractor contractor = null;

    @JsonProperty("loaDate")
    private Long loaDate = null;

    @JsonProperty("loaNumber")
    private String loaNumber = null;

    @JsonProperty("contractPeriod")
    private BigDecimal contractPeriod = null;

    @JsonProperty("emdAmountDeposited")
    private BigDecimal emdAmountDeposited = null;

    @JsonProperty("stampPaperAmount")
    private BigDecimal stampPaperAmount = null;

    @JsonProperty("engineerIncharge")
    private Employee engineerIncharge = null;

    @JsonProperty("defectLiabilityPeriod")
    private Double defectLiabilityPeriod = null;

    @JsonProperty("loaAmount")
    private BigDecimal loaAmount = null;

    @JsonProperty("documentDetails")
    private List<DocumentDetail> documentDetails = null;

    @JsonProperty("status")
    private WorksStatus status = null;

    @JsonProperty("tenderFinalizedPercentage")
    private Double tenderFinalizedPercentage = null;

    @JsonProperty("approvedBy")
    private User approvedBy = null;

    @JsonProperty("approvedDate")
    private Long approvedDate = null;

    @JsonProperty("fileNumber")
    private String fileNumber = null;

    @JsonProperty("fileDate")
    private Long fileDate = null;

    @JsonProperty("parent")
    private String parent = null;

    @JsonProperty("stateId")
    private String stateId = null;

    @JsonProperty("cancellationReason")
    private String cancellationReason = null;

    @JsonProperty("cancellationRemarks")
    private String cancellationRemarks = null;

    @JsonProperty("councilResolutionNumber")
    private String councilResolutionNumber = null;

    @JsonProperty("councilResolutionDate")
    private Long councilResolutionDate = null;

    @JsonProperty("spillOverFlag")
    private Boolean spillOverFlag = false;

    @JsonProperty("letterOfAcceptanceEstimates")
    private List<LetterOfAcceptanceEstimate> letterOfAcceptanceEstimates = new ArrayList<LetterOfAcceptanceEstimate>();

    @JsonProperty("securityDeposits")
    private List<SecurityDeposit> securityDeposits = null;

    @JsonProperty("workFlowDetails")
    private WorkFlowDetails workFlowDetails = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    @JsonProperty("deleted")
    private Boolean deleted = false;

    public LetterOfAcceptance id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Letter Of Acceptance
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Letter Of Acceptance")

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LetterOfAcceptance tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Letter Of Acceptance
     * @return tenantId
     **/
    @ApiModelProperty(required = true, value = "Tenant id of the Letter Of Acceptance")
    @NotNull

    @Size(min = 2, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public LetterOfAcceptance contractor(Contractor contractor) {
        this.contractor = contractor;
        return this;
    }

    /**
     * L1 Tender Finalized Contractor
     * @return contractor
     **/
    @ApiModelProperty(required = true, value = "L1 Tender Finalized Contractor")
    @NotNull

    // @Valid

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }

    public LetterOfAcceptance loaDate(Long loaDate) {
        this.loaDate = loaDate;
        return this;
    }

    /**
     * Epoch time of LOA Date. Default the current date for new LOA and do not allow to modify. This field is user entered for
     * spillover LOA. Allowed to edit during rejected status or drafts for LOA. Future date is not allowed to enter in this field.
     * LOA date should be on or after the L1 Tender Finalized status date of Detailed Estimate.
     * @return loaDate
     **/
    @ApiModelProperty(required = true, value = "Epoch time of LOA Date. Default the current date for new LOA and do not allow to modify. This field is user entered for spillover LOA. Allowed to edit during rejected status or drafts for LOA. Future date is not allowed to enter in this field. LOA date should be on or after the L1 Tender Finalized status date of Detailed Estimate.")
//    @NotNull

    public Long getLoaDate() {
        return loaDate;
    }

    public void setLoaDate(Long loaDate) {
        this.loaDate = loaDate;
    }

    public LetterOfAcceptance loaNumber(String loaNumber) {
        this.loaNumber = loaNumber;
        return this;
    }

    /**
     * Unique LOA Number of the Letter Of Acceptance. If the LOA is spillover then the LOA number is user entered. Otherwise it is
     * auto generated. This field is allowed to edit during rejected status or drafts for Spillover LOA.
     * @return loaNumber
     **/
    @ApiModelProperty(required = true, value = "Unique LOA Number of the Letter Of Acceptance. If the LOA is spillover then the LOA number is user entered. Otherwise it is auto generated. This field is allowed to edit during rejected status or drafts for Spillover LOA.")
//    @NotNull

    @Pattern(regexp = "[0-9a-zA-Z-/]+")
    @Size(max = 50)
    public String getLoaNumber() {
        return loaNumber;
    }

    public void setLoaNumber(String loaNumber) {
        this.loaNumber = loaNumber;
    }

    public LetterOfAcceptance contractPeriod(BigDecimal contractPeriod) {
        this.contractPeriod = contractPeriod;
        return this;
    }

    /**
     * Contract Period (In days) for the Letter Of Acceptance
     * @return contractPeriod
     **/
    @ApiModelProperty(required = true, value = "Contract Period (In days) for the Letter Of Acceptance")
    // @NotNull

    @Valid

    public BigDecimal getContractPeriod() {
        return contractPeriod;
    }

    public void setContractPeriod(BigDecimal contractPeriod) {
        this.contractPeriod = contractPeriod;
    }

    public LetterOfAcceptance emdAmountDeposited(BigDecimal emdAmountDeposited) {
        this.emdAmountDeposited = emdAmountDeposited;
        return this;
    }

    /**
     * EMD Amount Deposited for the Letter Of Acceptance
     * @return emdAmountDeposited
     **/
    @ApiModelProperty(value = "EMD Amount Deposited for the Letter Of Acceptance")

    @Valid

    public BigDecimal getEmdAmountDeposited() {
        return emdAmountDeposited;
    }

    public void setEmdAmountDeposited(BigDecimal emdAmountDeposited) {
        this.emdAmountDeposited = emdAmountDeposited;
    }

    public LetterOfAcceptance stampPaperAmount(BigDecimal stampPaperAmount) {
        this.stampPaperAmount = stampPaperAmount;
        return this;
    }

    /**
     * Stamp Paper amount for Agreement
     * @return stampPaperAmount
     **/
    @ApiModelProperty(required = true, value = "Stamp Paper amount for Agreement")
    // @NotNull

    @Valid

    public BigDecimal getStampPaperAmount() {
        return stampPaperAmount;
    }

    public void setStampPaperAmount(BigDecimal stampPaperAmount) {
        this.stampPaperAmount = stampPaperAmount;
    }

    public LetterOfAcceptance engineerIncharge(Employee engineerIncharge) {
        this.engineerIncharge = engineerIncharge;
        return this;
    }

    /**
     * User who is incharge of the work, primary key is reference here.
     * @return engineerIncharge
     **/
    @ApiModelProperty(required = true, value = "User who is incharge of the work, primary key is reference here.")
    @NotNull

    // @Valid

    public Employee getEngineerIncharge() {
        return engineerIncharge;
    }

    public void setEngineerIncharge(Employee engineerIncharge) {
        this.engineerIncharge = engineerIncharge;
    }

    public LetterOfAcceptance defectLiabilityPeriod(Double defectLiabilityPeriod) {
        this.defectLiabilityPeriod = defectLiabilityPeriod;
        return this;
    }

    /**
     * Defect Liability Period (In Years) for the Letter Of Acceptance
     * @return defectLiabilityPeriod
     **/
    @ApiModelProperty(required = true, value = "Defect Liability Period (In Years) for the Letter Of Acceptance")
    @NotNull

    public Double getDefectLiabilityPeriod() {
        return defectLiabilityPeriod;
    }

    public void setDefectLiabilityPeriod(Double defectLiabilityPeriod) {
        this.defectLiabilityPeriod = defectLiabilityPeriod;
    }

    public LetterOfAcceptance loaAmount(BigDecimal loaAmount) {
        this.loaAmount = loaAmount;
        return this;
    }

    /**
     * LOA Amount for the Letter Of Acceptance
     * @return loaAmount
     **/
    @ApiModelProperty(required = true, value = "LOA Amount for the Letter Of Acceptance")
    @NotNull

    @Valid

    public BigDecimal getLoaAmount() {
        return loaAmount;
    }

    public void setLoaAmount(BigDecimal loaAmount) {
        this.loaAmount = loaAmount;
    }

    public LetterOfAcceptance documentDetails(List<DocumentDetail> documentDetails) {
        this.documentDetails = documentDetails;
        return this;
    }

    public LetterOfAcceptance addDocumentDetailsItem(DocumentDetail documentDetailsItem) {
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

    // @Valid

    public List<DocumentDetail> getDocumentDetails() {
        return documentDetails;
    }

    public void setDocumentDetails(List<DocumentDetail> documentDetails) {
        this.documentDetails = documentDetails;
    }

    public LetterOfAcceptance status(WorksStatus status) {
        this.status = status;
        return this;
    }

    /**
     * Get status
     * @return status
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    @Valid

    public WorksStatus getStatus() {
        return status;
    }

    public void setStatus(WorksStatus status) {
        this.status = status;
    }

    public LetterOfAcceptance tenderFinalizedPercentage(Double tenderFinalizedPercentage) {
        this.tenderFinalizedPercentage = tenderFinalizedPercentage;
        return this;
    }

    /**
     * Tender Finalized Percentage for the Letter Of Acceptance
     * @return tenderFinalizedPercentage
     **/
    @ApiModelProperty(required = true, value = "Tender Finalized Percentage for the Letter Of Acceptance")
    @NotNull

    public Double getTenderFinalizedPercentage() {
        return tenderFinalizedPercentage;
    }

    public void setTenderFinalizedPercentage(Double tenderFinalizedPercentage) {
        this.tenderFinalizedPercentage = tenderFinalizedPercentage;
    }

    public LetterOfAcceptance approvedBy(User approvedBy) {
        this.approvedBy = approvedBy;
        return this;
    }

    /**
     * User who approved the LOA
     * @return approvedBy
     **/
    @ApiModelProperty(value = "User who approved the LOA")

//    @Valid

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LetterOfAcceptance approvedDate(Long approvedDate) {
        this.approvedDate = approvedDate;
        return this;
    }

    /**
     * Epoch time of Date on which Letter Of Acceptance approved. Future date is not allowed. LOA Approved date should be on or
     * after the LOA date.
     * @return approvedDate
     **/
    @ApiModelProperty(value = "Epoch time of Date on which Letter Of Acceptance approved. Future date is not allowed. LOA Approved date should be on or after the LOA date.")

    public Long getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Long approvedDate) {
        this.approvedDate = approvedDate;
    }

    public LetterOfAcceptance fileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
        return this;
    }

    /**
     * File Number of Letter Of Acceptance.
     * @return fileNumber
     **/
    @ApiModelProperty(required = true, value = "File Number of Letter Of Acceptance.")
    @NotNull

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public LetterOfAcceptance fileDate(Long fileDate) {
        this.fileDate = fileDate;
        return this;
    }

    /**
     * Epoch time of File Date of the Letter Of Acceptance. Future date is not allowed.
     * @return fileDate
     **/
    @ApiModelProperty(required = true, value = "Epoch time of File Date of the Letter Of Acceptance. Future date is not allowed.")
    @NotNull

    public Long getFileDate() {
        return fileDate;
    }

    public void setFileDate(Long fileDate) {
        this.fileDate = fileDate;
    }

    public LetterOfAcceptance parent(String parent) {
        this.parent = parent;
        return this;
    }

    /**
     * Parent of Letter Of Acceptance. This is required in case of Revision Work Order
     * @return parent
     **/
    @ApiModelProperty(value = "Parent of Letter Of Acceptance. This is required in case of Revision Work Order")

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public LetterOfAcceptance stateId(String stateId) {
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

    public LetterOfAcceptance cancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
        return this;
    }

    /**
     * Reason for cancellation of the LOA, Required only while cancelling LOA
     * @return cancellationReason
     **/
    @ApiModelProperty(value = "Reason for cancellation of the LOA, Required only while cancelling LOA")

    @Pattern(regexp = "[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+")
    @Size(max = 100)
    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public LetterOfAcceptance cancellationRemarks(String cancellationRemarks) {
        this.cancellationRemarks = cancellationRemarks;
        return this;
    }

    /**
     * remarks for cancellation of the LOA, Required only while cancelling LOA
     * @return cancellationRemarks
     **/
    @ApiModelProperty(value = "remarks for cancellation of the LOA, Required only while cancelling LOA")

    @Pattern(regexp = "[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+")
    @Size(max = 512)
    public String getCancellationRemarks() {
        return cancellationRemarks;
    }

    public void setCancellationRemarks(String cancellationRemarks) {
        this.cancellationRemarks = cancellationRemarks;
    }

    public LetterOfAcceptance councilResolutionNumber(String councilResolutionNumber) {
        this.councilResolutionNumber = councilResolutionNumber;
        return this;
    }

    /**
     * Council resolution number of the Letter Of Acceptance. This field is required on final approval of LOA.
     * @return councilResolutionNumber
     **/
    @ApiModelProperty(value = "Council resolution number of the Letter Of Acceptance. This field is required on final approval of LOA.")

    @Pattern(regexp = "[0-9a-zA-Z-/]+")
    @Size(max = 50)
    public String getCouncilResolutionNumber() {
        return councilResolutionNumber;
    }

    public void setCouncilResolutionNumber(String councilResolutionNumber) {
        this.councilResolutionNumber = councilResolutionNumber;
    }

    public LetterOfAcceptance councilResolutionDate(Long councilResolutionDate) {
        this.councilResolutionDate = councilResolutionDate;
        return this;
    }

    /**
     * Epoch time of the council resolution date of the Letter Of Acceptance. The future date is now allowed. This field is
     * required on final approval of LOA.
     * @return councilResolutionDate
     **/
    @ApiModelProperty(value = "Epoch time of the council resolution date of the Letter Of Acceptance. The future date is now allowed. This field is required on final approval of LOA.")

    public Long getCouncilResolutionDate() {
        return councilResolutionDate;
    }

    public void setCouncilResolutionDate(Long councilResolutionDate) {
        this.councilResolutionDate = councilResolutionDate;
    }

    public LetterOfAcceptance spillOverFlag(Boolean spillOverFlag) {
        this.spillOverFlag = spillOverFlag;
        return this;
    }

    /**
     * Boolean value to identify whether the LOA is spillover or not.
     * @return spillOverFlag
     **/
    @ApiModelProperty(value = "Boolean value to identify whether the LOA is spillover or not.")

    public Boolean getSpillOverFlag() {
        return spillOverFlag;
    }

    public void setSpillOverFlag(Boolean spillOverFlag) {
        this.spillOverFlag = spillOverFlag;
    }

    public LetterOfAcceptance letterOfAcceptanceEstimates(List<LetterOfAcceptanceEstimate> letterOfAcceptanceEstimates) {
        this.letterOfAcceptanceEstimates = letterOfAcceptanceEstimates;
        return this;
    }

    public LetterOfAcceptance addLetterOfAcceptanceEstimatesItem(LetterOfAcceptanceEstimate letterOfAcceptanceEstimatesItem) {
        this.letterOfAcceptanceEstimates.add(letterOfAcceptanceEstimatesItem);
        return this;
    }

    /**
     * Array of Letter Of Acceptance Estimate Details
     * @return letterOfAcceptanceEstimates
     **/
    @ApiModelProperty(required = true, value = "Array of Letter Of Acceptance Estimate Details")
    @NotNull

    // @Valid
    @Size(min = 1)
    public List<LetterOfAcceptanceEstimate> getLetterOfAcceptanceEstimates() {
        return letterOfAcceptanceEstimates;
    }

    public void setLetterOfAcceptanceEstimates(List<LetterOfAcceptanceEstimate> letterOfAcceptanceEstimates) {
        this.letterOfAcceptanceEstimates = letterOfAcceptanceEstimates;
    }

    public LetterOfAcceptance securityDeposits(List<SecurityDeposit> securityDeposits) {
        this.securityDeposits = securityDeposits;
        return this;
    }

    public LetterOfAcceptance addSecurityDepositsItem(SecurityDeposit securityDepositsItem) {
        if (this.securityDeposits == null) {
            this.securityDeposits = new ArrayList<SecurityDeposit>();
        }
        this.securityDeposits.add(securityDepositsItem);
        return this;
    }

    /**
     * Array of Security Deposit collection details
     * @return securityDeposits
     **/
    @ApiModelProperty(value = "Array of Security Deposit collection details")

    // @Valid

    public List<SecurityDeposit> getSecurityDeposits() {
        return securityDeposits;
    }

    public void setSecurityDeposits(List<SecurityDeposit> securityDeposits) {
        this.securityDeposits = securityDeposits;
    }

    public LetterOfAcceptance workFlowDetails(WorkFlowDetails workFlowDetails) {
        this.workFlowDetails = workFlowDetails;
        return this;
    }

    /**
     * Get workFlowDetails
     * @return workFlowDetails
     **/
    @ApiModelProperty(value = "")

    // @Valid

    public WorkFlowDetails getWorkFlowDetails() {
        return workFlowDetails;
    }

    public void setWorkFlowDetails(WorkFlowDetails workFlowDetails) {
        this.workFlowDetails = workFlowDetails;
    }

    public LetterOfAcceptance auditDetails(AuditDetails auditDetails) {
        this.auditDetails = auditDetails;
        return this;
    }

    /**
     * Get auditDetails
     * @return auditDetails
     **/
    @ApiModelProperty(value = "")

    // @Valid

    public AuditDetails getAuditDetails() {
        return auditDetails;
    }

    public void setAuditDetails(AuditDetails auditDetails) {
        this.auditDetails = auditDetails;
    }

    public LetterOfAcceptance deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
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
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LetterOfAcceptance letterOfAcceptance = (LetterOfAcceptance) o;
        return Objects.equals(this.id, letterOfAcceptance.id) &&
                Objects.equals(this.tenantId, letterOfAcceptance.tenantId) &&
                Objects.equals(this.contractor, letterOfAcceptance.contractor) &&
                Objects.equals(this.loaDate, letterOfAcceptance.loaDate) &&
                Objects.equals(this.loaNumber, letterOfAcceptance.loaNumber) &&
                Objects.equals(this.contractPeriod, letterOfAcceptance.contractPeriod) &&
                Objects.equals(this.emdAmountDeposited, letterOfAcceptance.emdAmountDeposited) &&
                Objects.equals(this.stampPaperAmount, letterOfAcceptance.stampPaperAmount) &&
                Objects.equals(this.engineerIncharge, letterOfAcceptance.engineerIncharge) &&
                Objects.equals(this.defectLiabilityPeriod, letterOfAcceptance.defectLiabilityPeriod) &&
                Objects.equals(this.loaAmount, letterOfAcceptance.loaAmount) &&
                Objects.equals(this.documentDetails, letterOfAcceptance.documentDetails) &&
                Objects.equals(this.status, letterOfAcceptance.status) &&
                Objects.equals(this.tenderFinalizedPercentage, letterOfAcceptance.tenderFinalizedPercentage) &&
                Objects.equals(this.approvedBy, letterOfAcceptance.approvedBy) &&
                Objects.equals(this.approvedDate, letterOfAcceptance.approvedDate) &&
                Objects.equals(this.fileNumber, letterOfAcceptance.fileNumber) &&
                Objects.equals(this.fileDate, letterOfAcceptance.fileDate) &&
                Objects.equals(this.parent, letterOfAcceptance.parent) &&
                Objects.equals(this.stateId, letterOfAcceptance.stateId) &&
                Objects.equals(this.cancellationReason, letterOfAcceptance.cancellationReason) &&
                Objects.equals(this.cancellationRemarks, letterOfAcceptance.cancellationRemarks) &&
                Objects.equals(this.councilResolutionNumber, letterOfAcceptance.councilResolutionNumber) &&
                Objects.equals(this.councilResolutionDate, letterOfAcceptance.councilResolutionDate) &&
                Objects.equals(this.spillOverFlag, letterOfAcceptance.spillOverFlag) &&
                Objects.equals(this.letterOfAcceptanceEstimates, letterOfAcceptance.letterOfAcceptanceEstimates) &&
                Objects.equals(this.securityDeposits, letterOfAcceptance.securityDeposits) &&
                Objects.equals(this.workFlowDetails, letterOfAcceptance.workFlowDetails) &&
                Objects.equals(this.auditDetails, letterOfAcceptance.auditDetails) &&
                Objects.equals(this.deleted, letterOfAcceptance.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, contractor, loaDate, loaNumber, contractPeriod, emdAmountDeposited, stampPaperAmount,
                engineerIncharge, defectLiabilityPeriod, loaAmount, documentDetails, status, tenderFinalizedPercentage,
                approvedBy, approvedDate, fileNumber, fileDate, parent, stateId, cancellationReason, cancellationRemarks,
                councilResolutionNumber, councilResolutionDate, spillOverFlag, letterOfAcceptanceEstimates, securityDeposits,
                workFlowDetails, auditDetails, deleted);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class LetterOfAcceptance {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    contractor: ").append(toIndentedString(contractor)).append("\n");
        sb.append("    loaDate: ").append(toIndentedString(loaDate)).append("\n");
        sb.append("    loaNumber: ").append(toIndentedString(loaNumber)).append("\n");
        sb.append("    contractPeriod: ").append(toIndentedString(contractPeriod)).append("\n");
        sb.append("    emdAmountDeposited: ").append(toIndentedString(emdAmountDeposited)).append("\n");
        sb.append("    stampPaperAmount: ").append(toIndentedString(stampPaperAmount)).append("\n");
        sb.append("    engineerIncharge: ").append(toIndentedString(engineerIncharge)).append("\n");
        sb.append("    defectLiabilityPeriod: ").append(toIndentedString(defectLiabilityPeriod)).append("\n");
        sb.append("    loaAmount: ").append(toIndentedString(loaAmount)).append("\n");
        sb.append("    documentDetails: ").append(toIndentedString(documentDetails)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    tenderFinalizedPercentage: ").append(toIndentedString(tenderFinalizedPercentage)).append("\n");
        sb.append("    approvedBy: ").append(toIndentedString(approvedBy)).append("\n");
        sb.append("    approvedDate: ").append(toIndentedString(approvedDate)).append("\n");
        sb.append("    fileNumber: ").append(toIndentedString(fileNumber)).append("\n");
        sb.append("    fileDate: ").append(toIndentedString(fileDate)).append("\n");
        sb.append("    parent: ").append(toIndentedString(parent)).append("\n");
        sb.append("    stateId: ").append(toIndentedString(stateId)).append("\n");
        sb.append("    cancellationReason: ").append(toIndentedString(cancellationReason)).append("\n");
        sb.append("    cancellationRemarks: ").append(toIndentedString(cancellationRemarks)).append("\n");
        sb.append("    councilResolutionNumber: ").append(toIndentedString(councilResolutionNumber)).append("\n");
        sb.append("    councilResolutionDate: ").append(toIndentedString(councilResolutionDate)).append("\n");
        sb.append("    spillOverFlag: ").append(toIndentedString(spillOverFlag)).append("\n");
        sb.append("    letterOfAcceptanceEstimates: ").append(toIndentedString(letterOfAcceptanceEstimates)).append("\n");
        sb.append("    securityDeposits: ").append(toIndentedString(securityDeposits)).append("\n");
        sb.append("    workFlowDetails: ").append(toIndentedString(workFlowDetails)).append("\n");
        sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
        sb.append("    deleted: ").append(toIndentedString(deleted)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
