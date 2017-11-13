package org.egov.works.commons.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * An Object that holds Templates defined for Milestone based on Type of Works
 * and Sub Type of work
 */
@ApiModel(description = "An Object that holds Templates defined for Milestone based on Type of Works and Sub Type of work")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-08T13:25:44.581Z")

public class MilestoneTemplate {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("templateName")
	private String templateName = null;

	@JsonProperty("templateCode")
	private String templateCode = null;

	@JsonProperty("active")
	private Boolean active = null;

	@JsonProperty("description")
	private String description = null;

	@JsonProperty("typeOfWork")
	private TypeOfWork typeOfWork = null;

	@JsonProperty("subTypeOfWork")
	private TypeOfWork subTypeOfWork = null;

	@JsonProperty("milestoneTemplateActivities")
	private List<MilestoneTemplateActivities> milestoneTemplateActivities = new ArrayList<MilestoneTemplateActivities>();

	public MilestoneTemplate id(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Unique Identifier of the Milestone Template
	 * 
	 * @return id
	 **/
	@ApiModelProperty(value = "Unique Identifier of the Milestone Template")

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MilestoneTemplate tenantId(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	/**
	 * Tenant id of the Milestone Template
	 * 
	 * @return tenantId
	 **/
	@ApiModelProperty(required = true, value = "Tenant id of the Milestone Template")
	@NotNull

	@Size(min = 2, max = 128)
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public MilestoneTemplate templateName(String templateName) {
		this.templateName = templateName;
		return this;
	}

	/**
	 * Name of the Milestone Template
	 * 
	 * @return templateName
	 **/
	@ApiModelProperty(required = true, value = "Name of the Milestone Template")
	@NotNull

	@Pattern(regexp = "[a-zA-Z0-9\\s\\.,]+")
	@Size(min = 1, max = 100)
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public MilestoneTemplate templateCode(String templateCode) {
		this.templateCode = templateCode;
		return this;
	}

	/**
	 * Code of the Milestone Template
	 * 
	 * @return templateCode
	 **/
	@ApiModelProperty(required = true, value = "Code of the Milestone Template")
	@NotNull

	@Pattern(regexp = "[a-zA-Z0-9-\\\\]+")
	@Size(min = 1, max = 100)
	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public MilestoneTemplate active(Boolean active) {
		this.active = active;
		return this;
	}

	/**
	 * If status of Type Of Work is TRUE then the Milestone Template is active
	 * else it is in-active. The default is active.
	 * 
	 * @return active
	 **/
	@ApiModelProperty(required = true, value = "If status of Type Of Work is TRUE then the Milestone Template is active else it is in-active. The default is active.")
	@NotNull

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public MilestoneTemplate description(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Description of the Milestone Template
	 * 
	 * @return description
	 **/
	@ApiModelProperty(value = "Description of the Milestone Template")

	@Pattern(regexp = "[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+")
	@Size(max = 1024)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MilestoneTemplate typeOfWork(TypeOfWork typeOfWork) {
		this.typeOfWork = typeOfWork;
		return this;
	}

	/**
	 * Type of work for which this Milestone template is belongs to
	 * 
	 * @return typeOfWork
	 **/
	@ApiModelProperty(required = true, value = "Type of work for which this Milestone template is belongs to")
	@NotNull

	@Valid

	public TypeOfWork getTypeOfWork() {
		return typeOfWork;
	}

	public void setTypeOfWork(TypeOfWork typeOfWork) {
		this.typeOfWork = typeOfWork;
	}

	public MilestoneTemplate subTypeOfWork(TypeOfWork subTypeOfWork) {
		this.subTypeOfWork = subTypeOfWork;
		return this;
	}

	/**
	 * Sub Type of work for which this Milestone template is belongs to
	 * 
	 * @return subTypeOfWork
	 **/
	@ApiModelProperty(value = "Sub Type of work for which this Milestone template is belongs to")

	@Valid

	public TypeOfWork getSubTypeOfWork() {
		return subTypeOfWork;
	}

	public void setSubTypeOfWork(TypeOfWork subTypeOfWork) {
		this.subTypeOfWork = subTypeOfWork;
	}

	public MilestoneTemplate milestoneTemplateActivities(
			List<MilestoneTemplateActivities> milestoneTemplateActivities) {
		this.milestoneTemplateActivities = milestoneTemplateActivities;
		return this;
	}

	public MilestoneTemplate addMilestoneTemplateActivitiesItem(
			MilestoneTemplateActivities milestoneTemplateActivitiesItem) {
		this.milestoneTemplateActivities.add(milestoneTemplateActivitiesItem);
		return this;
	}

	/**
	 * Array of Milestone Template Activities
	 * 
	 * @return milestoneTemplateActivities
	 **/
	@ApiModelProperty(required = true, value = "Array of Milestone Template Activities")
	@NotNull

	@Valid
	@Size(min = 1)
	public List<MilestoneTemplateActivities> getMilestoneTemplateActivities() {
		return milestoneTemplateActivities;
	}

	public void setMilestoneTemplateActivities(List<MilestoneTemplateActivities> milestoneTemplateActivities) {
		this.milestoneTemplateActivities = milestoneTemplateActivities;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		MilestoneTemplate milestoneTemplate = (MilestoneTemplate) o;
		return Objects.equals(this.id, milestoneTemplate.id)
				&& Objects.equals(this.tenantId, milestoneTemplate.tenantId)
				&& Objects.equals(this.templateName, milestoneTemplate.templateName)
				&& Objects.equals(this.templateCode, milestoneTemplate.templateCode)
				&& Objects.equals(this.active, milestoneTemplate.active)
				&& Objects.equals(this.description, milestoneTemplate.description)
				&& Objects.equals(this.typeOfWork, milestoneTemplate.typeOfWork)
				&& Objects.equals(this.subTypeOfWork, milestoneTemplate.subTypeOfWork)
				&& Objects.equals(this.milestoneTemplateActivities, milestoneTemplate.milestoneTemplateActivities);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tenantId, templateName, templateCode, active, description, typeOfWork, subTypeOfWork,
				milestoneTemplateActivities);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class MilestoneTemplate {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
		sb.append("    templateName: ").append(toIndentedString(templateName)).append("\n");
		sb.append("    templateCode: ").append(toIndentedString(templateCode)).append("\n");
		sb.append("    active: ").append(toIndentedString(active)).append("\n");
		sb.append("    description: ").append(toIndentedString(description)).append("\n");
		sb.append("    typeOfWork: ").append(toIndentedString(typeOfWork)).append("\n");
		sb.append("    subTypeOfWork: ").append(toIndentedString(subTypeOfWork)).append("\n");
		sb.append("    milestoneTemplateActivities: ").append(toIndentedString(milestoneTemplateActivities))
				.append("\n");
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
