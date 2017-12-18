package org.egov.works.masters.domain.repository.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.works.masters.web.contract.MilestoneTemplate;
import org.egov.works.masters.web.contract.MilestoneTemplateActivities;
import org.egov.works.masters.web.contract.TypeOfWork;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramki on 15/12/17.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MilestoneTemplateHelper {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("active")
    private Boolean active = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("typeOfWork")
    private String typeOfWork = null;

    @JsonProperty("subTypeOfWork")
    private String subTypeOfWork = null;

    @JsonProperty("milestoneTemplateActivities")
    private List<MilestoneTemplateActivities> milestoneTemplateActivities = new ArrayList<MilestoneTemplateActivities>();

    @JsonProperty("createdBy")
    private String createdBy = null;

    @JsonProperty("lastModifiedBy")
    private String lastModifiedBy = null;

    @JsonProperty("createdTime")
    private Long createdTime = null;

    @JsonProperty("lastModifiedTime")
    private Long lastModifiedTime = null;

    public MilestoneTemplate toDomain() {
        MilestoneTemplate milestoneTemplate = new MilestoneTemplate();
        milestoneTemplate.setId(this.id);
        milestoneTemplate.setTenantId(this.tenantId);
        milestoneTemplate.setCode(this.code);
        milestoneTemplate.setName(this.name);
        milestoneTemplate.setActive(this.active);
        milestoneTemplate.setDescription(this.description);
        milestoneTemplate.setTypeOfWork(new TypeOfWork());
        milestoneTemplate.getTypeOfWork().setCode(this.typeOfWork);
        milestoneTemplate.setSubTypeOfWork(new TypeOfWork());
        milestoneTemplate.getSubTypeOfWork().setCode(this.subTypeOfWork);
        return milestoneTemplate;
    }
}
