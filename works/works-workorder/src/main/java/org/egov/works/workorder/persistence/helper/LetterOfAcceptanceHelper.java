package org.egov.works.workorder.persistence.helper;

import java.math.BigDecimal;

import org.egov.works.workorder.web.contract.Contractor;
import org.egov.works.workorder.web.contract.Employee;
import org.egov.works.workorder.web.contract.LetterOfAcceptance;
import org.egov.works.workorder.web.contract.WorksStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LetterOfAcceptanceHelper {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("contractor")
    private String contractor = null;

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
    private String engineerIncharge = null;

    @JsonProperty("defectLiabilityPeriod")
    private Double defectLiabilityPeriod = null;

    @JsonProperty("loaAmount")
    private BigDecimal loaAmount = null;

    @JsonProperty("status")
    private String status = null;

    @JsonProperty("tenderFinalizedPercentage")
    private Double tenderFinalizedPercentage = null;

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

    public LetterOfAcceptance toDomain() {
        LetterOfAcceptance letterOfAcceptance = new LetterOfAcceptance();
        letterOfAcceptance.setId(this.id);
        letterOfAcceptance.setTenantId(this.tenantId);
        letterOfAcceptance.setContractor(new Contractor());
        letterOfAcceptance.getContractor().setCode(this.contractor);
        letterOfAcceptance.setLoaDate(this.loaDate);
        letterOfAcceptance.loaNumber(this.loaNumber);
        letterOfAcceptance.setContractPeriod(this.contractPeriod);
        letterOfAcceptance.emdAmountDeposited(this.emdAmountDeposited);
        letterOfAcceptance.stampPaperAmount(this.stampPaperAmount);
        Employee employee = new Employee();
        employee.setCode(this.engineerIncharge);
        letterOfAcceptance.engineerIncharge(employee);
        letterOfAcceptance.defectLiabilityPeriod(this.defectLiabilityPeriod);
        letterOfAcceptance.setLoaAmount(this.loaAmount);
        WorksStatus worksStatus = new WorksStatus();
        worksStatus.setCode(this.status);
        letterOfAcceptance.setStatus(worksStatus);
        letterOfAcceptance.setTenderFinalizedPercentage(this.tenderFinalizedPercentage);
        letterOfAcceptance.setApprovedDate(this.approvedDate);
        letterOfAcceptance.setFileNumber(this.fileNumber);
        letterOfAcceptance.setFileDate(this.fileDate);
        // letterOfAcceptance.parent(this.parent);
        letterOfAcceptance.setStateId(this.stateId);
        letterOfAcceptance.setCancellationReason(this.cancellationReason);
        letterOfAcceptance.setCancellationRemarks(this.cancellationRemarks);
        letterOfAcceptance.setCouncilResolutionDate(this.councilResolutionDate);
        letterOfAcceptance.setCouncilResolutionNumber(this.councilResolutionNumber);
        letterOfAcceptance.setSpillOverFlag(this.spillOverFlag);

        return letterOfAcceptance;

    }

}
