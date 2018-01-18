package org.egov.works.measurementbook.persistence.helper;

import org.egov.works.measurementbook.web.contract.BillStatus;
import org.egov.works.measurementbook.web.contract.ContractorBill;
import org.egov.works.measurementbook.web.contract.LetterOfAcceptanceEstimate;
import org.egov.works.measurementbook.web.contract.User;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContractorBillHelper {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("billSequenceNumber")
    private String billSequenceNumber = null;

    @JsonProperty("approvedDate")
    private Long approvedDate = null;

    @JsonProperty("approvedBy")
    private String approvedBy = null;

    @JsonProperty("cancellationReason")
    private String cancellationReason = null;

    @JsonProperty("cancellationRemarks")
    private String cancellationRemarks = null;

    @JsonProperty("letterOfAcceptanceEstimate")
    private String letterOfAcceptanceEstimate = null;

    @JsonProperty("stateId")
    private String stateId = null;

    @JsonProperty("spillOver")
    private Boolean spillOver = false;

    @JsonProperty("billNumber")
    private String billNumber = null;

    @JsonProperty("billSubType")
    private String billSubType = null;

    @JsonProperty("billDate")
    private Long billDate = null;

    @JsonProperty("status")
    private String status = null;

    public ContractorBill toDomain() {
        ContractorBill cb = new ContractorBill();
        cb.setTenantId(this.tenantId);
        cb.setId(this.id);
        cb.setBillSequenceNumber(this.billSequenceNumber);
        cb.setApprovedDate(this.approvedDate);
        cb.setApprovedBy(new User());
        cb.getApprovedBy().setName(this.approvedBy);
        cb.setCancellationRemarks(this.cancellationRemarks);
        cb.setCancellationReason(this.cancellationReason);
        cb.setBillDate(this.billDate);
        cb.setBillNumber(this.billNumber);
        cb.setBillSubType(this.billSubType);
        cb.setLetterOfAcceptanceEstimate(new LetterOfAcceptanceEstimate());
        cb.getLetterOfAcceptanceEstimate().setId(this.letterOfAcceptanceEstimate);
        cb.stateId(this.stateId);
        cb.setStatus(new BillStatus());
        cb.getStatus().setCode(this.status);
        cb.setSpillOver(this.spillOver);
        return cb;
    }

}
