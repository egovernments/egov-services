package org.egov.works.workorder.persistence.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.works.workorder.web.contract.Milestone;
import org.egov.works.workorder.web.contract.TrackMilestone;
import org.egov.works.workorder.web.contract.WorksStatus;

import java.math.BigDecimal;

/**
 * Created by ramki on 20/12/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrackMilestoneHelper {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("milestone")
    private String milestone = null;

    @JsonProperty("status")
    private String status = null;

    @JsonProperty("totalPercentage")
    private BigDecimal totalPercentage = null;

    @JsonProperty("projectCompleted")
    private Boolean projectCompleted = null;

    @JsonProperty("deleted")
    private Boolean deleted = false;

    public TrackMilestone toDomain() {
        TrackMilestone trackMilestone = new TrackMilestone();

        trackMilestone.setId(this.id);
        trackMilestone.setTenantId(this.tenantId);
        trackMilestone.setMilestone(new Milestone());
        trackMilestone.getMilestone().setId(this.milestone);
        trackMilestone.setStatus(new WorksStatus());
        trackMilestone.getStatus().setCode(this.status);
        trackMilestone.setTotalPercentage(this.totalPercentage);
        trackMilestone.setProjectCompleted(this.projectCompleted);
        trackMilestone.setDeleted(this.deleted);

        return trackMilestone;
    }
}
