package org.egov.works.measurementbook.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * An Object that holds the basic data for a Project Code
 */
@ApiModel(description = "An Object that holds the basic data for a Project Code")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-23T09:58:12.227Z")

public class ProjectCode   {
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
  private ProjectCodeStatus status = null;

  @JsonProperty("active")
  private Boolean active = false;

  @JsonProperty("projectValue")
  private BigDecimal projectValue = null;

  @JsonProperty("completionDate")
  private Long completionDate = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public ProjectCode id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Project Code
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Project Code")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ProjectCode tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Project Code
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Project Code")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public ProjectCode code(String code) {
    this.code = code;
    return this;
  }

   /**
   * Code of the Project
   * @return code
  **/
  @ApiModelProperty(value = "Code of the Project")

 @Pattern(regexp="[a-zA-Z0-9-/]+") @Size(max=100)
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public ProjectCode name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Name of the Project
   * @return name
  **/
  @ApiModelProperty(required = true, value = "Name of the Project")
  @NotNull

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(min=1,max=1024)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ProjectCode description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Description of the Project
   * @return description
  **/
  @ApiModelProperty(value = "Description of the Project")

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(max=1024)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ProjectCode status(ProjectCodeStatus status) {
    this.status = status;
    return this;
  }

   /**
   * Enum representing the list of possible values for Project code status
   * @return status
  **/
  @ApiModelProperty(required = true, value = "Enum representing the list of possible values for Project code status")
  @NotNull

  @Valid

  public ProjectCodeStatus getStatus() {
    return status;
  }

  public void setStatus(ProjectCodeStatus status) {
    this.status = status;
  }

  public ProjectCode active(Boolean active) {
    this.active = active;
    return this;
  }

   /**
   * The active/inactive status of the project which refresents the boolean value True/False. By default the boolean value will be True.
   * @return active
  **/
  @ApiModelProperty(value = "The active/inactive status of the project which refresents the boolean value True/False. By default the boolean value will be True.")


  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public ProjectCode projectValue(BigDecimal projectValue) {
    this.projectValue = projectValue;
    return this;
  }

   /**
   * The cost of the Project. This value will be updated on Project closure which will include the complete expenditure on the project.
   * @return projectValue
  **/
  @ApiModelProperty(value = "The cost of the Project. This value will be updated on Project closure which will include the complete expenditure on the project.")

  @Valid

  public BigDecimal getProjectValue() {
    return projectValue;
  }

  public void setProjectValue(BigDecimal projectValue) {
    this.projectValue = projectValue;
  }

  public ProjectCode completionDate(Long completionDate) {
    this.completionDate = completionDate;
    return this;
  }

   /**
   * Epoch time for the project completion date
   * @return completionDate
  **/
  @ApiModelProperty(value = "Epoch time for the project completion date")


  public Long getCompletionDate() {
    return completionDate;
  }

  public void setCompletionDate(Long completionDate) {
    this.completionDate = completionDate;
  }

  public ProjectCode auditDetails(AuditDetails auditDetails) {
    this.auditDetails = auditDetails;
    return this;
  }

   /**
   * Get auditDetails
   * @return auditDetails
  **/
  @ApiModelProperty(value = "")

  @Valid

  public AuditDetails getAuditDetails() {
    return auditDetails;
  }

  public void setAuditDetails(AuditDetails auditDetails) {
    this.auditDetails = auditDetails;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProjectCode projectCode = (ProjectCode) o;
    return Objects.equals(this.id, projectCode.id) &&
        Objects.equals(this.tenantId, projectCode.tenantId) &&
        Objects.equals(this.code, projectCode.code) &&
        Objects.equals(this.name, projectCode.name) &&
        Objects.equals(this.description, projectCode.description) &&
        Objects.equals(this.status, projectCode.status) &&
        Objects.equals(this.active, projectCode.active) &&
        Objects.equals(this.projectValue, projectCode.projectValue) &&
        Objects.equals(this.completionDate, projectCode.completionDate) &&
        Objects.equals(this.auditDetails, projectCode.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, code, name, description, status, active, projectValue, completionDate, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProjectCode {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    projectValue: ").append(toIndentedString(projectValue)).append("\n");
    sb.append("    completionDate: ").append(toIndentedString(completionDate)).append("\n");
    sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

