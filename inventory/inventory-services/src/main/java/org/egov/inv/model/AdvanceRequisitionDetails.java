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
 * An Object holds the Advance Requisition Details
 */
@ApiModel(description = "An Object holds the Advance Requisition Details")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-28T09:20:06.607Z")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdvanceRequisitionDetails {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("chartOfAccounts")
    private ChartOfAccount chartOfAccounts = null;

    @JsonProperty("function")
    private Function function = null;

    @JsonProperty("creditAmount")
    private BigDecimal creditAmount = null;

    @JsonProperty("debitAmount")
    private BigDecimal debitAmount = null;

    @JsonProperty("narration")
    private String narration = null;

    @JsonProperty("advanceRequisitionPayeeDetails")
    private List<AdvanceRequisitionPayeeDetails> advanceRequisitionPayeeDetails = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    public AdvanceRequisitionDetails id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Advance Requisition Details
     *
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Advance Requisition Details")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AdvanceRequisitionDetails tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Advance Requisition Details
     *
     * @return tenantId
     **/
    @ApiModelProperty(required = true, value = "Tenant id of the Advance Requisition Details")
    @NotNull

    @Size(min = 2, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public AdvanceRequisitionDetails chartOfAccounts(ChartOfAccount chartOfAccounts) {
        this.chartOfAccounts = chartOfAccounts;
        return this;
    }

    /**
     * Chart Of Accounts of the Advance Requisition Details
     *
     * @return chartOfAccounts
     **/
    @ApiModelProperty(required = true, value = "Chart Of Accounts of the Advance Requisition Details")
    @NotNull

    @Valid

    public ChartOfAccount getChartOfAccounts() {
        return chartOfAccounts;
    }

    public void setChartOfAccounts(ChartOfAccount chartOfAccounts) {
        this.chartOfAccounts = chartOfAccounts;
    }

    public AdvanceRequisitionDetails function(Function function) {
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

    public AdvanceRequisitionDetails creditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
        return this;
    }

    /**
     * Advance Requisition credit amount
     *
     * @return creditAmount
     **/
    @ApiModelProperty(value = "Advance Requisition credit amount")

    @Valid

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public AdvanceRequisitionDetails debitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
        return this;
    }

    /**
     * Advance Requisition debit amount
     *
     * @return debitAmount
     **/
    @ApiModelProperty(value = "Advance Requisition debit amount")

    @Valid

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public AdvanceRequisitionDetails narration(String narration) {
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
    @Size(min = 3, max = 1024)
    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public AdvanceRequisitionDetails advanceRequisitionPayeeDetails(List<AdvanceRequisitionPayeeDetails> advanceRequisitionPayeeDetails) {
        this.advanceRequisitionPayeeDetails = advanceRequisitionPayeeDetails;
        return this;
    }

    public AdvanceRequisitionDetails addAdvanceRequisitionPayeeDetailsItem(AdvanceRequisitionPayeeDetails advanceRequisitionPayeeDetailsItem) {
        if (this.advanceRequisitionPayeeDetails == null) {
            this.advanceRequisitionPayeeDetails = new ArrayList<AdvanceRequisitionPayeeDetails>();
        }
        this.advanceRequisitionPayeeDetails.add(advanceRequisitionPayeeDetailsItem);
        return this;
    }

    /**
     * Array of Advance Requisition details
     *
     * @return advanceRequisitionPayeeDetails
     **/
    @ApiModelProperty(value = "Array of Advance Requisition details")

    @Valid

    public List<AdvanceRequisitionPayeeDetails> getAdvanceRequisitionPayeeDetails() {
        return advanceRequisitionPayeeDetails;
    }

    public void setAdvanceRequisitionPayeeDetails(List<AdvanceRequisitionPayeeDetails> advanceRequisitionPayeeDetails) {
        this.advanceRequisitionPayeeDetails = advanceRequisitionPayeeDetails;
    }

    public AdvanceRequisitionDetails auditDetails(AuditDetails auditDetails) {
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
        AdvanceRequisitionDetails advanceRequisitionDetails = (AdvanceRequisitionDetails) o;
        return Objects.equals(this.id, advanceRequisitionDetails.id) &&
                Objects.equals(this.tenantId, advanceRequisitionDetails.tenantId) &&
                Objects.equals(this.chartOfAccounts, advanceRequisitionDetails.chartOfAccounts) &&
                Objects.equals(this.function, advanceRequisitionDetails.function) &&
                Objects.equals(this.creditAmount, advanceRequisitionDetails.creditAmount) &&
                Objects.equals(this.debitAmount, advanceRequisitionDetails.debitAmount) &&
                Objects.equals(this.narration, advanceRequisitionDetails.narration) &&
                Objects.equals(this.advanceRequisitionPayeeDetails, advanceRequisitionDetails.advanceRequisitionPayeeDetails) &&
                Objects.equals(this.auditDetails, advanceRequisitionDetails.auditDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, chartOfAccounts, function, creditAmount, debitAmount, narration, advanceRequisitionPayeeDetails, auditDetails);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AdvanceRequisitionDetails {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    chartOfAccounts: ").append(toIndentedString(chartOfAccounts)).append("\n");
        sb.append("    function: ").append(toIndentedString(function)).append("\n");
        sb.append("    creditAmount: ").append(toIndentedString(creditAmount)).append("\n");
        sb.append("    debitAmount: ").append(toIndentedString(debitAmount)).append("\n");
        sb.append("    narration: ").append(toIndentedString(narration)).append("\n");
        sb.append("    advanceRequisitionPayeeDetails: ").append(toIndentedString(advanceRequisitionPayeeDetails)).append("\n");
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

