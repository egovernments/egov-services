package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ContractorAdvanceRequisitionHelper extends AdvanceRequisition {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("letterOfAcceptanceEstimate")
    private String letterOfAcceptanceEstimate = null;

    @JsonProperty("stateId")
    private String stateId = null;

    private String createdBy = null;
    
    private Long createdTime;
    
    private String lastModifiedBy = null;
    
    private Long lastModifiedTime;
    
    public ContractorAdvanceRequisition toDomain() {
        ContractorAdvanceRequisition contractorAdvanceRequisition = new ContractorAdvanceRequisition();
        contractorAdvanceRequisition.setId(this.id);
        contractorAdvanceRequisition.setTenantId(this.tenantId);
        LetterOfAcceptanceEstimate estimate = new LetterOfAcceptanceEstimate();
        estimate.setId(this.letterOfAcceptanceEstimate);
        contractorAdvanceRequisition.setLetterOfAcceptanceEstimate(estimate);
        AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy(this.createdBy);
        auditDetails.setCreatedTime(this.createdTime);
        auditDetails.setLastModifiedBy(this.lastModifiedBy);
        auditDetails.lastModifiedTime(this.lastModifiedTime);
        contractorAdvanceRequisition.setAuditDetails(auditDetails);
        return contractorAdvanceRequisition;
    }

}
