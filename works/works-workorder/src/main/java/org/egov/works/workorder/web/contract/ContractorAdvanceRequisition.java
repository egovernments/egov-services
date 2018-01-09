package org.egov.works.workorder.web.contract;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * An Object that holds the basic data for Contractor Advance Requisition
 */
@ApiModel(description = "An Object that holds the basic data for Contractor Advance Requisition")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-09T05:23:36.544Z")

public class ContractorAdvanceRequisition extends AdvanceRequisition {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("letterOfAcceptanceEstimate")
    private LetterOfAcceptanceEstimate letterOfAcceptanceEstimate = null;

    @JsonProperty("workFlowDetails")
    private WorkFlowDetails workFlowDetails = null;

    @JsonProperty("stateId")
    private String stateId = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    public ContractorAdvanceRequisition id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Contractor Advance Requisition Form
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Contractor Advance Requisition Form")

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ContractorAdvanceRequisition tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * tenant id of the Contractor Advance Requisition Form
     * @return tenantId
     **/
    @ApiModelProperty(value = "tenant id of the Contractor Advance Requisition Form")

    @Size(min = 2, max = 128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public ContractorAdvanceRequisition letterOfAcceptanceEstimate(LetterOfAcceptanceEstimate letterOfAcceptanceEstimate) {
        this.letterOfAcceptanceEstimate = letterOfAcceptanceEstimate;
        return this;
    }

    /**
     * Letter of acceptance reference
     * @return letterOfAcceptanceEstimate
     **/
    @ApiModelProperty(value = "Letter of acceptance reference")

//    @Valid

    public LetterOfAcceptanceEstimate getLetterOfAcceptanceEstimate() {
        return letterOfAcceptanceEstimate;
    }

    public void setLetterOfAcceptanceEstimate(LetterOfAcceptanceEstimate letterOfAcceptanceEstimate) {
        this.letterOfAcceptanceEstimate = letterOfAcceptanceEstimate;
    }

    public ContractorAdvanceRequisition workFlowDetails(WorkFlowDetails workFlowDetails) {
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

    public ContractorAdvanceRequisition stateId(String stateId) {
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

    public ContractorAdvanceRequisition auditDetails(AuditDetails auditDetails) {
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

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContractorAdvanceRequisition contractorAdvanceRequisition = (ContractorAdvanceRequisition) o;
        return Objects.equals(this.id, contractorAdvanceRequisition.id) &&
                Objects.equals(this.tenantId, contractorAdvanceRequisition.tenantId) &&
                Objects.equals(this.letterOfAcceptanceEstimate, contractorAdvanceRequisition.letterOfAcceptanceEstimate) &&
                Objects.equals(this.workFlowDetails, contractorAdvanceRequisition.workFlowDetails) &&
                Objects.equals(this.stateId, contractorAdvanceRequisition.stateId) &&
                Objects.equals(this.auditDetails, contractorAdvanceRequisition.auditDetails) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, letterOfAcceptanceEstimate, workFlowDetails, stateId, auditDetails, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ContractorAdvanceRequisition {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    letterOfAcceptanceEstimate: ").append(toIndentedString(letterOfAcceptanceEstimate)).append("\n");
        sb.append("    workFlowDetails: ").append(toIndentedString(workFlowDetails)).append("\n");
        sb.append("    stateId: ").append(toIndentedString(stateId)).append("\n");
        sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
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
