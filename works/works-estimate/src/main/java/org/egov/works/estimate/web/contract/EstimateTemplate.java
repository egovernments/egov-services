package org.egov.works.estimate.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An Object that holds Templates defined for Estimate based on Type of Works and Sub Type of work
 */
@ApiModel(description = "An Object that holds Templates defined for Estimate based on Type of Works and Sub Type of work")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-29T09:02:10.583Z")

public class EstimateTemplate   {
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

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public EstimateTemplate id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Estimate Template
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Estimate Template")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public EstimateTemplate tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Estimate Template
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Estimate Template")
  @NotNull

 @Size(min=2,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public EstimateTemplate name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Name of the Estimate Template
   * @return name
  **/
  @ApiModelProperty(required = true, value = "Name of the Estimate Template")
  @NotNull

 @Pattern(regexp="[a-zA-Z0-9\\s\\.,]+") @Size(min=1,max=100)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public EstimateTemplate code(String code) {
    this.code = code;
    return this;
  }

   /**
   * Code of the Estimate Template
   * @return code
  **/
  @ApiModelProperty(required = true, value = "Code of the Estimate Template")
  @NotNull

 @Pattern(regexp="[a-zA-Z0-9-\\\\]+") @Size(min=1,max=100)
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public EstimateTemplate active(Boolean active) {
    this.active = active;
    return this;
  }

   /**
   * If status of Estimate Template is TRUE then the Estimate Template is active else it is in-active. The default is active.
   * @return active
  **/
  @ApiModelProperty(required = true, value = "If status of Estimate Template is TRUE then the Estimate Template is active else it is in-active. The default is active.")
  @NotNull


  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public EstimateTemplate description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Description of the Estimate Template
   * @return description
  **/
  @ApiModelProperty(value = "Description of the Estimate Template")

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(min=1,max=1024)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public EstimateTemplate typeOfWork(String typeOfWork) {
    this.typeOfWork = typeOfWork;
    return this;
  }

   /**
   * The Type of work of the Template activity. Unique reference from 'TypeOfWork'.Code is ref. here.
   * @return typeOfWork
  **/
  @ApiModelProperty(required = true, value = "The Type of work of the Template activity. Unique reference from 'TypeOfWork'.Code is ref. here.")
  @NotNull


  public String getTypeOfWork() {
    return typeOfWork;
  }

  public void setTypeOfWork(String typeOfWork) {
    this.typeOfWork = typeOfWork;
  }

  public EstimateTemplate subTypeOfWork(String subTypeOfWork) {
    this.subTypeOfWork = subTypeOfWork;
    return this;
  }

   /**
   * The Sub Type of work of the Template activity. Unique reference from 'SubTypeOfWork'.Code is ref. here.
   * @return subTypeOfWork
  **/
  @ApiModelProperty(value = "The Sub Type of work of the Template activity. Unique reference from 'SubTypeOfWork'.Code is ref. here.")


  public String getSubTypeOfWork() {
    return subTypeOfWork;
  }

  public void setSubTypeOfWork(String subTypeOfWork) {
    this.subTypeOfWork = subTypeOfWork;
  }

  public EstimateTemplate estimateTemplateActivities(List<EstimateTemplateActivities> estimateTemplateActivities) {
    this.estimateTemplateActivities = estimateTemplateActivities;
    return this;
  }

  public EstimateTemplate addEstimateTemplateActivitiesItem(EstimateTemplateActivities estimateTemplateActivitiesItem) {
    this.estimateTemplateActivities.add(estimateTemplateActivitiesItem);
    return this;
  }

   /**
   * Array of Estimate Template Activities
   * @return estimateTemplateActivities
  **/
  @ApiModelProperty(required = true, value = "Array of Estimate Template Activities")
  @NotNull

  @Valid

  public List<EstimateTemplateActivities> getEstimateTemplateActivities() {
    return estimateTemplateActivities;
  }

  public void setEstimateTemplateActivities(List<EstimateTemplateActivities> estimateTemplateActivities) {
    this.estimateTemplateActivities = estimateTemplateActivities;
  }

  public EstimateTemplate auditDetails(AuditDetails auditDetails) {
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
    EstimateTemplate estimateTemplate = (EstimateTemplate) o;
    return Objects.equals(this.id, estimateTemplate.id) &&
        Objects.equals(this.tenantId, estimateTemplate.tenantId) &&
        Objects.equals(this.name, estimateTemplate.name) &&
        Objects.equals(this.code, estimateTemplate.code) &&
        Objects.equals(this.active, estimateTemplate.active) &&
        Objects.equals(this.description, estimateTemplate.description) &&
        Objects.equals(this.typeOfWork, estimateTemplate.typeOfWork) &&
        Objects.equals(this.subTypeOfWork, estimateTemplate.subTypeOfWork) &&
        Objects.equals(this.estimateTemplateActivities, estimateTemplate.estimateTemplateActivities) &&
        Objects.equals(this.auditDetails, estimateTemplate.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, name, code, active, description, typeOfWork, subTypeOfWork, estimateTemplateActivities, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EstimateTemplate {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    typeOfWork: ").append(toIndentedString(typeOfWork)).append("\n");
    sb.append("    subTypeOfWork: ").append(toIndentedString(subTypeOfWork)).append("\n");
    sb.append("    estimateTemplateActivities: ").append(toIndentedString(estimateTemplateActivities)).append("\n");
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

