package org.egov.works.workorder.persistence.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.works.workorder.web.contract.LetterOfAcceptanceEstimate;
import org.egov.works.workorder.web.contract.Milestone;
import org.egov.works.workorder.web.contract.WorksStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MilestoneHelper {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("letterOfAcceptanceEstimate")
    private String letterOfAcceptanceEstimate = null;

    @JsonProperty("status")
    private String status = null;

    @JsonProperty("cancellationReason")
    private String cancellationReason = null;

    @JsonProperty("cancellationRemarks")
    private String cancellationRemarks = null;

    @JsonProperty("deleted")
    private Boolean deleted = false;

    public Milestone toDomain() {
        Milestone milestone = new Milestone();

        milestone.setId(this.id);
        milestone.setTenantId(this.tenantId);
        milestone.setLetterOfAcceptanceEstimate(new LetterOfAcceptanceEstimate());
        milestone.getLetterOfAcceptanceEstimate().setId(this.letterOfAcceptanceEstimate);
        milestone.setStatus(new WorksStatus());
        milestone.getStatus().setCode(this.status);
        milestone.setCancellationReason(this.cancellationReason);
        milestone.setCancellationRemarks(this.cancellationRemarks);
        milestone.setDeleted(this.deleted);

        return milestone;
    }

}
