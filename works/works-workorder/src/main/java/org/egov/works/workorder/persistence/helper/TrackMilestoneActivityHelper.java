package org.egov.works.workorder.persistence.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.works.workorder.web.contract.MilestoneActivity;
import org.egov.works.workorder.web.contract.TrackMilestoneActivity;
import org.egov.works.workorder.web.contract.WorksStatus;

import java.math.BigDecimal;

/**
 * Created by ramki on 20/12/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrackMilestoneActivityHelper {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("trackMilestone")
    private String trackMilestone = null;

    @JsonProperty("milestoneActivity")
    private String milestoneActivity = null;

    @JsonProperty("remarks")
    private String remarks = null;

    @JsonProperty("percentage")
    private BigDecimal percentage = null;

    @JsonProperty("actualStartDate")
    private Long actualStartDate = null;

    @JsonProperty("actualEndDate")
    private Long actualEndDate = null;

    @JsonProperty("status")
    private String status = null;

    @JsonProperty("deleted")
    private Boolean deleted = false;

    public TrackMilestoneActivity toDomain() {
        TrackMilestoneActivity trackMilestoneActivity = new TrackMilestoneActivity();

        trackMilestoneActivity.setId(this.id);
        trackMilestoneActivity.setTenantId(this.tenantId);
        trackMilestoneActivity.setTrackMilestone(this.trackMilestone);
        trackMilestoneActivity.setMilestoneActivity(new MilestoneActivity());
        trackMilestoneActivity.getMilestoneActivity().setId(this.milestoneActivity);
        trackMilestoneActivity.setRemarks(this.remarks);
        trackMilestoneActivity.setPercentage(this.percentage);
        trackMilestoneActivity.setActualStartDate(this.actualStartDate);
        trackMilestoneActivity.setActualEndDate(this.actualEndDate);
        trackMilestoneActivity.setStatus(new WorksStatus());
        trackMilestoneActivity.getStatus().setCode(this.status);
        trackMilestoneActivity.setDeleted(this.deleted);

        return trackMilestoneActivity;
    }
}
