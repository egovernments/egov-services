package org.egov.works.estimate.web.contract;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(description = "An Object that holds the basic data for a Project Code")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-12-26T08:10:27.515Z")
@Data
public class ProjectCodeHelper {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("status")
    private String status = null;

    @JsonProperty("active")
    private Boolean active = false;

    @JsonProperty("projectValue")
    private BigDecimal projectValue = null;

    @JsonProperty("completionDate")
    private Long completionDate = null;

    @JsonProperty("deleted")
    private Boolean deleted = false;

    @JsonProperty("createdBy")
    private String createdBy = null;

    @JsonProperty("lastModifiedBy")
    private String lastModifiedBy = null;

    @JsonProperty("createdTime")
    private Long createdTime = null;

    @JsonProperty("lastModifiedTime")
    private Long lastModifiedTime = null;

    public ProjectCode toDomain() {
        ProjectCode projectCode = new ProjectCode();
        projectCode.setActive(this.active);
        projectCode.setCode(this.code);
        projectCode.setId(this.id);
        projectCode.tenantId(this.tenantId);
        projectCode.setDeleted(this.deleted);
        projectCode.setName(this.name);
        projectCode.description(this.description);
        WorksStatus status = new WorksStatus();
        status.setCode(this.status);
        projectCode.setStatus(status);
        projectCode.setProjectValue(this.projectValue);
        projectCode.setCompletionDate(this.completionDate);
        projectCode.setAuditDetails(new AuditDetails());
        projectCode.getAuditDetails().setCreatedBy(this.createdBy);
        projectCode.getAuditDetails().setCreatedTime(this.createdTime);
        projectCode.getAuditDetails().setLastModifiedBy(this.lastModifiedBy);
        projectCode.getAuditDetails().setLastModifiedTime(this.lastModifiedTime);

        return projectCode;
    }

}
