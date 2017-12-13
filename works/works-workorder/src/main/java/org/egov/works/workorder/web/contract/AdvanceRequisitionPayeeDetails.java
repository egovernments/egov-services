package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * An Object holds the payee details of Advance Requisition
 */
@ApiModel(description = "An Object holds the payee details of Advance Requisition")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-30T11:45:24.744Z")

public class AdvanceRequisitionPayeeDetails {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("accountDetailType")
    private AccountDetailType accountDetailType = null;

    @JsonProperty("accountdetailKey")
    private AccountDetailKey accountdetailKey = null;

    @JsonProperty("creditAmount")
    private BigDecimal creditAmount = null;

    @JsonProperty("debitAmount")
    private BigDecimal debitAmount = null;

    @JsonProperty("narration")
    private String narration = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    public AdvanceRequisitionPayeeDetails id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Advance Requisition Payee Details
     *
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Advance Requisition Payee Details")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AdvanceRequisitionPayeeDetails tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Advance Requisition Payee Details
     *
     * @return tenantId
     **/
    @ApiModelProperty(required = true, value = "Tenant id of the Advance Requisition Payee Details")
    @NotNull

    @Size(min = 2, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public AdvanceRequisitionPayeeDetails accountDetailType(AccountDetailType accountDetailType) {
        this.accountDetailType = accountDetailType;
        return this;
    }

    /**
     * Account Detail Type of the Advance Requisition
     *
     * @return accountDetailType
     **/
    @ApiModelProperty(required = true, value = "Account Detail Type of the Advance Requisition")
    @NotNull

    @Valid

    public AccountDetailType getAccountDetailType() {
        return accountDetailType;
    }

    public void setAccountDetailType(AccountDetailType accountDetailType) {
        this.accountDetailType = accountDetailType;
    }

    public AdvanceRequisitionPayeeDetails accountdetailKey(AccountDetailKey accountdetailKey) {
        this.accountdetailKey = accountdetailKey;
        return this;
    }

    /**
     * Account Detail Key Unique Identifier
     *
     * @return accountdetailKey
     **/
    @ApiModelProperty(required = true, value = "Account Detail Key Unique Identifier")
    @NotNull

    @Valid

    public AccountDetailKey getAccountdetailKey() {
        return accountdetailKey;
    }

    public void setAccountdetailKey(AccountDetailKey accountdetailKey) {
        this.accountdetailKey = accountdetailKey;
    }

    public AdvanceRequisitionPayeeDetails creditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
        return this;
    }

    /**
     * Advance Requisition Payee Details credit amount
     *
     * @return creditAmount
     **/
    @ApiModelProperty(value = "Advance Requisition Payee Details credit amount")

    @Valid

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public AdvanceRequisitionPayeeDetails debitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
        return this;
    }

    /**
     * Advance Requisition Payee Details debit amount
     *
     * @return debitAmount
     **/
    @ApiModelProperty(value = "Advance Requisition Payee Details debit amount")

    @Valid

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public AdvanceRequisitionPayeeDetails narration(String narration) {
        this.narration = narration;
        return this;
    }

    /**
     * Descritpion for Advance Requisition Payee Details.
     *
     * @return narration
     **/
    @ApiModelProperty(value = "Descritpion for Advance Requisition Payee Details.")

    @Pattern(regexp = "[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+")
    @Size(min = 3, max = 1024)
    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public AdvanceRequisitionPayeeDetails auditDetails(AuditDetails auditDetails) {
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
        AdvanceRequisitionPayeeDetails advanceRequisitionPayeeDetails = (AdvanceRequisitionPayeeDetails) o;
        return Objects.equals(this.id, advanceRequisitionPayeeDetails.id) &&
                Objects.equals(this.tenantId, advanceRequisitionPayeeDetails.tenantId) &&
                Objects.equals(this.accountDetailType, advanceRequisitionPayeeDetails.accountDetailType) &&
                Objects.equals(this.accountdetailKey, advanceRequisitionPayeeDetails.accountdetailKey) &&
                Objects.equals(this.creditAmount, advanceRequisitionPayeeDetails.creditAmount) &&
                Objects.equals(this.debitAmount, advanceRequisitionPayeeDetails.debitAmount) &&
                Objects.equals(this.narration, advanceRequisitionPayeeDetails.narration) &&
                Objects.equals(this.auditDetails, advanceRequisitionPayeeDetails.auditDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, accountDetailType, accountdetailKey, creditAmount, debitAmount, narration, auditDetails);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AdvanceRequisitionPayeeDetails {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    accountDetailType: ").append(toIndentedString(accountDetailType)).append("\n");
        sb.append("    accountdetailKey: ").append(toIndentedString(accountdetailKey)).append("\n");
        sb.append("    creditAmount: ").append(toIndentedString(creditAmount)).append("\n");
        sb.append("    debitAmount: ").append(toIndentedString(debitAmount)).append("\n");
        sb.append("    narration: ").append(toIndentedString(narration)).append("\n");
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

