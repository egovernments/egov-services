package org.egov.works.masters.domain.repository.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.works.masters.web.contract.MilestoneTemplateActivities;

import java.math.BigDecimal;

/**
 * Created by ramki on 15/12/17.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MilestoneTemplateActivitiesHelper {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("milestoneTemplate")
    private String milestoneTemplate = null;

    @JsonProperty("stageOrderNumber")
    private BigDecimal stageOrderNumber = null;

    @JsonProperty("stageDescription")
    private String stageDescription = null;

    @JsonProperty("percentage")
    private Double percentage = null;

    @JsonProperty("createdBy")
    private String createdBy = null;

    @JsonProperty("lastModifiedBy")
    private String lastModifiedBy = null;

    @JsonProperty("createdTime")
    private Long createdTime = null;

    @JsonProperty("lastModifiedTime")
    private Long lastModifiedTime = null;

    public MilestoneTemplateActivities toDomain() {
        MilestoneTemplateActivities milestoneTemplateActivities = new MilestoneTemplateActivities();
        milestoneTemplateActivities.setId(this.id);
        milestoneTemplateActivities.setTenantId(this.tenantId);
        milestoneTemplateActivities.setMilestoneTemplate(this.milestoneTemplate);
        milestoneTemplateActivities.setStageDescription(this.stageDescription);
        milestoneTemplateActivities.setStageOrderNumber(this.stageOrderNumber);
        milestoneTemplateActivities.setPercentage(this.percentage);
        return milestoneTemplateActivities;
    }
}
