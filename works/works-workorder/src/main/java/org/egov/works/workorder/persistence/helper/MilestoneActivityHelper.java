package org.egov.works.workorder.persistence.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.works.workorder.web.contract.MilestoneActivity;

import java.math.BigDecimal;

/**
 * Created by ramki on 20/12/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MilestoneActivityHelper {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("milestone")
    private String milestone = null;

    @JsonProperty("scheduleStartDate")
    private Long scheduleStartDate = null;

    @JsonProperty("scheduleEndDate")
    private Long scheduleEndDate = null;

    @JsonProperty("percentage")
    private BigDecimal percentage = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("stageOrderNumber")
    private String stageOrderNumber = null;

    @JsonProperty("deleted")
    private Boolean deleted = false;

    public MilestoneActivity toDomain() {
        MilestoneActivity milestoneActivity = new MilestoneActivity();

        milestoneActivity.setId(this.id);
        milestoneActivity.setTenantId(this.tenantId);
        milestoneActivity.setMilestone(this.milestone);
        milestoneActivity.setScheduleStartDate(this.scheduleStartDate);
        milestoneActivity.setScheduleEndDate(this.scheduleEndDate);
        milestoneActivity.setPercentage(this.percentage);
        milestoneActivity.setDescription(this.description);
        milestoneActivity.setStageOrderNumber(this.stageOrderNumber);
        milestoneActivity.setDeleted(this.deleted);

        return milestoneActivity;
    }
}
