package org.egov.works.masters.domain.repository.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.works.masters.web.contract.EstimateTemplate;
import org.egov.works.masters.web.contract.EstimateTemplateActivities;
import org.egov.works.masters.web.contract.TypeOfWork;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramki on 7/11/17.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EstimateTemplateHelper {
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

    @JsonProperty("estimateTemplateActivities")
    private List<EstimateTemplateActivities> estimateTemplateActivities = new ArrayList<EstimateTemplateActivities>();

    @JsonProperty("createdBy")
    private String createdBy = null;

    @JsonProperty("lastModifiedBy")
    private String lastModifiedBy = null;

    @JsonProperty("createdTime")
    private Long createdTime = null;

    @JsonProperty("lastModifiedTime")
    private Long lastModifiedTime = null;

    public EstimateTemplate toDomain() {
        EstimateTemplate estimateTemplate = new EstimateTemplate();
        estimateTemplate.setId(this.id);
        estimateTemplate.setTenantId(this.tenantId);
        estimateTemplate.setCode(this.code);
        estimateTemplate.setName(this.name);
        estimateTemplate.setActive(this.active);
        estimateTemplate.setDescription(this.description);
        estimateTemplate.setTypeOfWork(new TypeOfWork());
        estimateTemplate.getTypeOfWork().setCode(this.typeOfWork);
        estimateTemplate.setSubTypeOfWork(new TypeOfWork());
        estimateTemplate.getSubTypeOfWork().setCode(this.subTypeOfWork);
        return estimateTemplate;
    }
}
