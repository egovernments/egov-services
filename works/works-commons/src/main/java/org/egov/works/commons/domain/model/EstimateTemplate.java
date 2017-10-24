package org.egov.works.commons.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * EstimateTemplate
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-24T13:08:31.335Z")

public class EstimateTemplate {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("code")
	private String code = null;

	@JsonProperty("status")
	private String status = null;

	@JsonProperty("description")
	private String description = null;

	@JsonProperty("typeOfWork")
	private TypeOfWork typeOfWork = null;

	@JsonProperty("subTypeOfWork")
	private SubTypeOfWork subTypeOfWork = null;

	@JsonProperty("estimateTemplateActivities")
	private List<EstimateTemplateActivities> estimateTemplateActivities = new ArrayList<EstimateTemplateActivities>();

	public EstimateTemplate id(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Unique Identifier of the Estimate Template
	 * 
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
	 * 
	 * @return tenantId
	 **/
	@ApiModelProperty(required = true, value = "Tenant id of the Estimate Template")
	@NotNull

	@Size(min = 4, max = 128)
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
	 * 
	 * @return name
	 **/
	@ApiModelProperty(required = true, value = "Name of the Estimate Template")
	@NotNull

	@Size(min = 1, max = 100)
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
	 * 
	 * @return code
	 **/
	@ApiModelProperty(required = true, value = "Code of the Estimate Template")
	@NotNull

	@Pattern(regexp = "[a-zA-Z0-9-\\\\]")
	@Size(min = 1, max = 100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public EstimateTemplate status(String status) {
		this.status = status;
		return this;
	}

	/**
	 * Status of the Estimate Template
	 * 
	 * @return status
	 **/
	@ApiModelProperty(required = true, value = "Status of the Estimate Template")
	@NotNull

	@Size(min = 1, max = 20)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public EstimateTemplate description(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Description of the Estimate Template
	 * 
	 * @return description
	 **/
	@ApiModelProperty(value = "Description of the Estimate Template")

	@Pattern(regexp = "[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]")
	@Size(min = 1, max = 1024)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public EstimateTemplate typeOfWork(TypeOfWork typeOfWork) {
		this.typeOfWork = typeOfWork;
		return this;
	}

	/**
	 * Get typeOfWork
	 * 
	 * @return typeOfWork
	 **/
	@ApiModelProperty(required = true, value = "")
	@NotNull

	@Valid

	public TypeOfWork getTypeOfWork() {
		return typeOfWork;
	}

	public void setTypeOfWork(TypeOfWork typeOfWork) {
		this.typeOfWork = typeOfWork;
	}

	public EstimateTemplate subTypeOfWork(SubTypeOfWork subTypeOfWork) {
		this.subTypeOfWork = subTypeOfWork;
		return this;
	}

	/**
	 * Get subTypeOfWork
	 * 
	 * @return subTypeOfWork
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public SubTypeOfWork getSubTypeOfWork() {
		return subTypeOfWork;
	}

	public void setSubTypeOfWork(SubTypeOfWork subTypeOfWork) {
		this.subTypeOfWork = subTypeOfWork;
	}

	public EstimateTemplate estimateTemplateActivities(List<EstimateTemplateActivities> estimateTemplateActivities) {
		this.estimateTemplateActivities = estimateTemplateActivities;
		return this;
	}

	public EstimateTemplate addEstimateTemplateActivitiesItem(
			EstimateTemplateActivities estimateTemplateActivitiesItem) {
		this.estimateTemplateActivities.add(estimateTemplateActivitiesItem);
		return this;
	}

	/**
	 * Array of Estimate Template Activities
	 * 
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

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		EstimateTemplate estimateTemplate = (EstimateTemplate) o;
		return Objects.equals(this.id, estimateTemplate.id) && Objects.equals(this.tenantId, estimateTemplate.tenantId)
				&& Objects.equals(this.name, estimateTemplate.name) && Objects.equals(this.code, estimateTemplate.code)
				&& Objects.equals(this.status, estimateTemplate.status)
				&& Objects.equals(this.description, estimateTemplate.description)
				&& Objects.equals(this.typeOfWork, estimateTemplate.typeOfWork)
				&& Objects.equals(this.subTypeOfWork, estimateTemplate.subTypeOfWork)
				&& Objects.equals(this.estimateTemplateActivities, estimateTemplate.estimateTemplateActivities);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tenantId, name, code, status, description, typeOfWork, subTypeOfWork,
				estimateTemplateActivities);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class EstimateTemplate {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    code: ").append(toIndentedString(code)).append("\n");
		sb.append("    status: ").append(toIndentedString(status)).append("\n");
		sb.append("    description: ").append(toIndentedString(description)).append("\n");
		sb.append("    typeOfWork: ").append(toIndentedString(typeOfWork)).append("\n");
		sb.append("    subTypeOfWork: ").append(toIndentedString(subTypeOfWork)).append("\n");
		sb.append("    estimateTemplateActivities: ").append(toIndentedString(estimateTemplateActivities)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
