package org.egov.inv.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * An Object holds the basic data for a Advance Requisition
 */
@ApiModel(description = "An Object holds the basic data for a Advance Requisition")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-28T09:20:06.607Z")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdvanceRequisition {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("advanceRequisitionNumber")
    private String advanceRequisitionNumber = null;

    @JsonProperty("advanceRequisitionDate")
    private Long advanceRequisitionDate = null;

    @JsonProperty("arfType")
    private AdvanceRequisitionType arfType = null;

    @JsonProperty("amount")
    private BigDecimal amount = null;

    @JsonProperty("narration")
    private String narration = null;

    @JsonProperty("status")
    private AdvanceRequisitionStatus status = null;

    @JsonProperty("field")
    private Boundary field = null;

    @JsonProperty("subField")
    private Boundary subField = null;

    @JsonProperty("department")
    private Department department = null;

    @JsonProperty("scheme")
    private Scheme scheme = null;

    @JsonProperty("subScheme")
    private SubScheme subScheme = null;

    @JsonProperty("fund")
    private Fund fund = null;

    @JsonProperty("fundSource")
    private Fundsource fundSource = null;

    @JsonProperty("functionary")
    private Functionary functionary = null;

    @JsonProperty("voucherNumber")
    private String voucherNumber = null;

    @JsonProperty("payTo")
    private User payTo = null;

    @JsonProperty("payByDate")
    private Long payByDate = null;

    @JsonProperty("referenceNumber")
    private String referenceNumber = null;

    @JsonProperty("sourcePath")
    private String sourcePath = null;

    @JsonProperty("partyBillNumber")
    private String partyBillNumber = null;

    @JsonProperty("partyBillDate")
    private Long partyBillDate = null;

    @JsonProperty("function")
    private Function function = null;

    @JsonProperty("advanceRequisitionDetails")
    private List<AdvanceRequisitionDetails> advanceRequisitionDetails = new ArrayList<AdvanceRequisitionDetails>();

    @JsonProperty("workFlowDetails")
    private WorkFlowDetails workFlowDetails = null;

    @JsonProperty("stateId")
    private String stateId = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    public AdvanceRequisition id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Advance Requisition
     *
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Advance Requisition")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AdvanceRequisition tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Advance Requisition
     *
     * @return tenantId
     **/
    @ApiModelProperty(required = true, value = "Tenant id of the Advance Requisition")
    @NotNull

    @Size(min = 2, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public AdvanceRequisition advanceRequisitionNumber(String advanceRequisitionNumber) {
        this.advanceRequisitionNumber = advanceRequisitionNumber;
        return this;
    }

    /**
     * Unique number for the Advance Requisition
     *
     * @return advanceRequisitionNumber
     **/
    @ApiModelProperty(required = true, value = "Unique number for the Advance Requisition")
    @NotNull

    @Pattern(regexp = "[a-zA-Z0-9-\\\\]+")
    @Size(min = 1, max = 20)
    public String getAdvanceRequisitionNumber() {
        return advanceRequisitionNumber;
    }

    public void setAdvanceRequisitionNumber(String advanceRequisitionNumber) {
        this.advanceRequisitionNumber = advanceRequisitionNumber;
    }

    public AdvanceRequisition advanceRequisitionDate(Long advanceRequisitionDate) {
        this.advanceRequisitionDate = advanceRequisitionDate;
        return this;
    }

    /**
     * Epoch time of when Advance Requisition Created
     *
     * @return advanceRequisitionDate
     **/
    @ApiModelProperty(required = true, value = "Epoch time of when Advance Requisition Created")
    @NotNull


    public Long getAdvanceRequisitionDate() {
        return advanceRequisitionDate;
    }

    public void setAdvanceRequisitionDate(Long advanceRequisitionDate) {
        this.advanceRequisitionDate = advanceRequisitionDate;
    }

    public AdvanceRequisition arfType(AdvanceRequisitionType arfType) {
        this.arfType = arfType;
        return this;
    }

    /**
     * Get arfType
     *
     * @return arfType
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    @Valid

    public AdvanceRequisitionType getArfType() {
        return arfType;
    }

    public void setArfType(AdvanceRequisitionType arfType) {
        this.arfType = arfType;
    }

    public AdvanceRequisition amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Advance Requisition amount
     *
     * @return amount
     **/
    @ApiModelProperty(required = true, value = "Advance Requisition amount")
    @NotNull

    @Valid

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public AdvanceRequisition narration(String narration) {
        this.narration = narration;
        return this;
    }

    /**
     * Descritpion for Advance Requisition.
     *
     * @return narration
     **/
    @ApiModelProperty(value = "Descritpion for Advance Requisition.")

    @Pattern(regexp = "[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+")
    @Size(max = 1024)
    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public AdvanceRequisition status(AdvanceRequisitionStatus status) {
        this.status = status;
        return this;
    }

    /**
     * Get status
     *
     * @return status
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    @Valid

    public AdvanceRequisitionStatus getStatus() {
        return status;
    }

    public void setStatus(AdvanceRequisitionStatus status) {
        this.status = status;
    }

    public AdvanceRequisition field(Boundary field) {
        this.field = field;
        return this;
    }

    /**
     * Field(boundary) of the Advance Requisition
     *
     * @return field
     **/
    @ApiModelProperty(value = "Field(boundary) of the Advance Requisition")

    @Valid

    public Boundary getField() {
        return field;
    }

    public void setField(Boundary field) {
        this.field = field;
    }

    public AdvanceRequisition subField(Boundary subField) {
        this.subField = subField;
        return this;
    }

    /**
     * Sub Field(boundary) of the Advance Requisition
     *
     * @return subField
     **/
    @ApiModelProperty(value = "Sub Field(boundary) of the Advance Requisition")

    @Valid

    public Boundary getSubField() {
        return subField;
    }

    public void setSubField(Boundary subField) {
        this.subField = subField;
    }

    public AdvanceRequisition department(Department department) {
        this.department = department;
        return this;
    }

    /**
     * Department for which Advance Requisition belongs to
     *
     * @return department
     **/
    @ApiModelProperty(value = "Department for which Advance Requisition belongs to")

    @Valid

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public AdvanceRequisition scheme(Scheme scheme) {
        this.scheme = scheme;
        return this;
    }

    /**
     * Scheme of the Advance Requisition
     *
     * @return scheme
     **/
    @ApiModelProperty(value = "Scheme of the Advance Requisition")

    @Valid

    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }

    public AdvanceRequisition subScheme(SubScheme subScheme) {
        this.subScheme = subScheme;
        return this;
    }

    /**
     * Sub scheme of the Advance Requisition
     *
     * @return subScheme
     **/
    @ApiModelProperty(value = "Sub scheme of the Advance Requisition")

    @Valid

    public SubScheme getSubScheme() {
        return subScheme;
    }

    public void setSubScheme(SubScheme subScheme) {
        this.subScheme = subScheme;
    }

    public AdvanceRequisition fund(Fund fund) {
        this.fund = fund;
        return this;
    }

    /**
     * Fund of the Advance Requisition
     *
     * @return fund
     **/
    @ApiModelProperty(value = "Fund of the Advance Requisition")

    @Valid

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }

    public AdvanceRequisition fundSource(Fundsource fundSource) {
        this.fundSource = fundSource;
        return this;
    }

    /**
     * Fund Source of the Advance Requisition
     *
     * @return fundSource
     **/
    @ApiModelProperty(value = "Fund Source of the Advance Requisition")

    @Valid

    public Fundsource getFundSource() {
        return fundSource;
    }

    public void setFundSource(Fundsource fundSource) {
        this.fundSource = fundSource;
    }

    public AdvanceRequisition functionary(Functionary functionary) {
        this.functionary = functionary;
        return this;
    }

    /**
     * Functionary of the Advance Requisition
     *
     * @return functionary
     **/
    @ApiModelProperty(value = "Functionary of the Advance Requisition")

    @Valid

    public Functionary getFunctionary() {
        return functionary;
    }

    public void setFunctionary(Functionary functionary) {
        this.functionary = functionary;
    }

    public AdvanceRequisition voucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
        return this;
    }

    /**
     * Voucher Number of the Advance Requisition
     *
     * @return voucherNumber
     **/
    @ApiModelProperty(value = "Voucher Number of the Advance Requisition")


    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public AdvanceRequisition payTo(User payTo) {
        this.payTo = payTo;
        return this;
    }

    /**
     * Payee Name of the Advance Requisition
     *
     * @return payTo
     **/
    @ApiModelProperty(value = "Payee Name of the Advance Requisition")

    @Valid

    public User getPayTo() {
        return payTo;
    }

    public void setPayTo(User payTo) {
        this.payTo = payTo;
    }

    public AdvanceRequisition payByDate(Long payByDate) {
        this.payByDate = payByDate;
        return this;
    }

    /**
     * Epoch time of Pay By Date
     *
     * @return payByDate
     **/
    @ApiModelProperty(value = "Epoch time of Pay By Date")


    public Long getPayByDate() {
        return payByDate;
    }

    public void setPayByDate(Long payByDate) {
        this.payByDate = payByDate;
    }

    public AdvanceRequisition referenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
        return this;
    }

    /**
     * Reference Number like Work Order Number, Purchase Order Number etc
     *
     * @return referenceNumber
     **/
    @ApiModelProperty(value = "Reference Number like Work Order Number, Purchase Order Number etc")

    @Size(min = 1, max = 100)
    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public AdvanceRequisition sourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
        return this;
    }

    /**
     * Source Path to view the Advance requisition
     *
     * @return sourcePath
     **/
    @ApiModelProperty(value = "Source Path to view the Advance requisition")


    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public AdvanceRequisition partyBillNumber(String partyBillNumber) {
        this.partyBillNumber = partyBillNumber;
        return this;
    }

    /**
     * Party Bill Number of Advance Requisition
     *
     * @return partyBillNumber
     **/
    @ApiModelProperty(value = "Party Bill Number of Advance Requisition")

    @Pattern(regexp = "[a-zA-Z0-9-\\\\]+")
    @Size(min = 3, max = 50)
    public String getPartyBillNumber() {
        return partyBillNumber;
    }

    public void setPartyBillNumber(String partyBillNumber) {
        this.partyBillNumber = partyBillNumber;
    }

    public AdvanceRequisition partyBillDate(Long partyBillDate) {
        this.partyBillDate = partyBillDate;
        return this;
    }

    /**
     * Epoch time of Party Bill
     *
     * @return partyBillDate
     **/
    @ApiModelProperty(value = "Epoch time of Party Bill")


    public Long getPartyBillDate() {
        return partyBillDate;
    }

    public void setPartyBillDate(Long partyBillDate) {
        this.partyBillDate = partyBillDate;
    }

    public AdvanceRequisition function(Function function) {
        this.function = function;
        return this;
    }

    /**
     * Function of the Advance Requisition
     *
     * @return function
     **/
    @ApiModelProperty(value = "Function of the Advance Requisition")

    @Valid

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public AdvanceRequisition advanceRequisitionDetails(List<AdvanceRequisitionDetails> advanceRequisitionDetails) {
        this.advanceRequisitionDetails = advanceRequisitionDetails;
        return this;
    }

    public AdvanceRequisition addAdvanceRequisitionDetailsItem(AdvanceRequisitionDetails advanceRequisitionDetailsItem) {
        this.advanceRequisitionDetails.add(advanceRequisitionDetailsItem);
        return this;
    }

    /**
     * Array of Advance Requisition details
     *
     * @return advanceRequisitionDetails
     **/
    @ApiModelProperty(required = true, value = "Array of Advance Requisition details")
    @NotNull

    @Valid

    public List<AdvanceRequisitionDetails> getAdvanceRequisitionDetails() {
        return advanceRequisitionDetails;
    }

    public void setAdvanceRequisitionDetails(List<AdvanceRequisitionDetails> advanceRequisitionDetails) {
        this.advanceRequisitionDetails = advanceRequisitionDetails;
    }

    public AdvanceRequisition workFlowDetails(WorkFlowDetails workFlowDetails) {
        this.workFlowDetails = workFlowDetails;
        return this;
    }

    /**
     * Get workFlowDetails
     *
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

    public AdvanceRequisition stateId(String stateId) {
        this.stateId = stateId;
        return this;
    }

    /**
     * State id of the workflow
     *
     * @return stateId
     **/
    @ApiModelProperty(value = "State id of the workflow")


    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public AdvanceRequisition auditDetails(AuditDetails auditDetails) {
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
        AdvanceRequisition advanceRequisition = (AdvanceRequisition) o;
        return Objects.equals(this.id, advanceRequisition.id) &&
                Objects.equals(this.tenantId, advanceRequisition.tenantId) &&
                Objects.equals(this.advanceRequisitionNumber, advanceRequisition.advanceRequisitionNumber) &&
                Objects.equals(this.advanceRequisitionDate, advanceRequisition.advanceRequisitionDate) &&
                Objects.equals(this.arfType, advanceRequisition.arfType) &&
                Objects.equals(this.amount, advanceRequisition.amount) &&
                Objects.equals(this.narration, advanceRequisition.narration) &&
                Objects.equals(this.status, advanceRequisition.status) &&
                Objects.equals(this.field, advanceRequisition.field) &&
                Objects.equals(this.subField, advanceRequisition.subField) &&
                Objects.equals(this.department, advanceRequisition.department) &&
                Objects.equals(this.scheme, advanceRequisition.scheme) &&
                Objects.equals(this.subScheme, advanceRequisition.subScheme) &&
                Objects.equals(this.fund, advanceRequisition.fund) &&
                Objects.equals(this.fundSource, advanceRequisition.fundSource) &&
                Objects.equals(this.functionary, advanceRequisition.functionary) &&
                Objects.equals(this.voucherNumber, advanceRequisition.voucherNumber) &&
                Objects.equals(this.payTo, advanceRequisition.payTo) &&
                Objects.equals(this.payByDate, advanceRequisition.payByDate) &&
                Objects.equals(this.referenceNumber, advanceRequisition.referenceNumber) &&
                Objects.equals(this.sourcePath, advanceRequisition.sourcePath) &&
                Objects.equals(this.partyBillNumber, advanceRequisition.partyBillNumber) &&
                Objects.equals(this.partyBillDate, advanceRequisition.partyBillDate) &&
                Objects.equals(this.function, advanceRequisition.function) &&
                Objects.equals(this.advanceRequisitionDetails, advanceRequisition.advanceRequisitionDetails) &&
                Objects.equals(this.workFlowDetails, advanceRequisition.workFlowDetails) &&
                Objects.equals(this.stateId, advanceRequisition.stateId) &&
                Objects.equals(this.auditDetails, advanceRequisition.auditDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, advanceRequisitionNumber, advanceRequisitionDate, arfType, amount, narration, status, field, subField, department, scheme, subScheme, fund, fundSource, functionary, voucherNumber, payTo, payByDate, referenceNumber, sourcePath, partyBillNumber, partyBillDate, function, advanceRequisitionDetails, workFlowDetails, stateId, auditDetails);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AdvanceRequisition {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    advanceRequisitionNumber: ").append(toIndentedString(advanceRequisitionNumber)).append("\n");
        sb.append("    advanceRequisitionDate: ").append(toIndentedString(advanceRequisitionDate)).append("\n");
        sb.append("    arfType: ").append(toIndentedString(arfType)).append("\n");
        sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
        sb.append("    narration: ").append(toIndentedString(narration)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    field: ").append(toIndentedString(field)).append("\n");
        sb.append("    subField: ").append(toIndentedString(subField)).append("\n");
        sb.append("    department: ").append(toIndentedString(department)).append("\n");
        sb.append("    scheme: ").append(toIndentedString(scheme)).append("\n");
        sb.append("    subScheme: ").append(toIndentedString(subScheme)).append("\n");
        sb.append("    fund: ").append(toIndentedString(fund)).append("\n");
        sb.append("    fundSource: ").append(toIndentedString(fundSource)).append("\n");
        sb.append("    functionary: ").append(toIndentedString(functionary)).append("\n");
        sb.append("    voucherNumber: ").append(toIndentedString(voucherNumber)).append("\n");
        sb.append("    payTo: ").append(toIndentedString(payTo)).append("\n");
        sb.append("    payByDate: ").append(toIndentedString(payByDate)).append("\n");
        sb.append("    referenceNumber: ").append(toIndentedString(referenceNumber)).append("\n");
        sb.append("    sourcePath: ").append(toIndentedString(sourcePath)).append("\n");
        sb.append("    partyBillNumber: ").append(toIndentedString(partyBillNumber)).append("\n");
        sb.append("    partyBillDate: ").append(toIndentedString(partyBillDate)).append("\n");
        sb.append("    function: ").append(toIndentedString(function)).append("\n");
        sb.append("    advanceRequisitionDetails: ").append(toIndentedString(advanceRequisitionDetails)).append("\n");
        sb.append("    workFlowDetails: ").append(toIndentedString(workFlowDetails)).append("\n");
        sb.append("    stateId: ").append(toIndentedString(stateId)).append("\n");
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
}

