package org.egov.works.masters.domain.repository.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.works.masters.web.contract.EstimateTemplateActivities;
import org.egov.works.masters.web.contract.ScheduleOfRate;
import org.egov.works.masters.web.contract.UOM;

import java.math.BigDecimal;

/**
 * Created by ramki on 7/11/17.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EstimateTemplateActivitiesHelper {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("estimateTemplate")
    private String estimateTemplate = null;

    @JsonProperty("scheduleOfRate")
    private String scheduleOfRate = null;

    @JsonProperty("uom")
    private String uom = null;

    @JsonProperty("nonSOR")
    private String nonSOR = null;

    @JsonProperty("createdBy")
    private String createdBy = null;

    @JsonProperty("lastModifiedBy")
    private String lastModifiedBy = null;

    @JsonProperty("createdTime")
    private Long createdTime = null;

    @JsonProperty("lastModifiedTime")
    private Long lastModifiedTime = null;

    public EstimateTemplateActivities toDomain() {
        EstimateTemplateActivities estimateTemplateActivities = new EstimateTemplateActivities();
        estimateTemplateActivities.setId(this.id);
        estimateTemplateActivities.setTenantId(this.tenantId);
        estimateTemplateActivities.setEstimateTemplate(this.estimateTemplate);
        estimateTemplateActivities.setScheduleOfRate(new ScheduleOfRate());
        estimateTemplateActivities.getScheduleOfRate().setId(this.scheduleOfRate);
        estimateTemplateActivities.setUom(new UOM());
        estimateTemplateActivities.getUom().setCode(this.uom);
        return estimateTemplateActivities;
    }
}
