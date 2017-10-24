package org.egov.works.commons.domain.model;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * SubTypeOfWork
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-24T13:08:31.335Z")

public class SubTypeOfWork {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("code")
	private String code = null;

	@JsonProperty("description")
	private String description = null;

	@JsonProperty("active")
	private Boolean active = true;

	@JsonProperty("parent")
	private TypeOfWork parent = null;

	public SubTypeOfWork id(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Unique Identifier of the Sub Type Of Work
	 * 
	 * @return id
	 **/
	@ApiModelProperty(value = "Unique Identifier of the Sub Type Of Work")

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SubTypeOfWork tenantId(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	/**
	 * Tenant id of the Sub Type Of Work
	 * 
	 * @return tenantId
	 **/
	@ApiModelProperty(required = true, value = "Tenant id of the Sub Type Of Work")
	@NotNull

	@Size(min = 4, max = 128)
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public SubTypeOfWork name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Name of the Sub Type Of Work
	 * 
	 * @return name
	 **/
	@ApiModelProperty(required = true, value = "Name of the Sub Type Of Work")
	@NotNull

	@Size(min = 1, max = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SubTypeOfWork code(String code) {
		this.code = code;
		return this;
	}

	/**
	 * Unique code of the Sub Type Of Work
	 * 
	 * @return code
	 **/
	@ApiModelProperty(required = true, value = "Unique code of the Sub Type Of Work")
	@NotNull

	@Pattern(regexp = "[a-zA-Z0-9-\\\\]")
	@Size(min = 1, max = 100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public SubTypeOfWork description(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Description of the Sub Type Of Work
	 * 
	 * @return description
	 **/
	@ApiModelProperty(value = "Description of the Sub Type Of Work")

	@Pattern(regexp = "[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]")
	@Size(max = 1024)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SubTypeOfWork active(Boolean active) {
		this.active = active;
		return this;
	}

	/**
	 * Status of Sub Type Of Work
	 * 
	 * @return active
	 **/
	@ApiModelProperty(value = "Status of Sub Type Of Work")

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public SubTypeOfWork parent(TypeOfWork parent) {
		this.parent = parent;
		return this;
	}

	/**
	 * Get parent
	 * 
	 * @return parent
	 **/
	@ApiModelProperty(required = true, value = "")
	@NotNull

	@Valid

	public TypeOfWork getParent() {
		return parent;
	}

	public void setParent(TypeOfWork parent) {
		this.parent = parent;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SubTypeOfWork subTypeOfWork = (SubTypeOfWork) o;
		return Objects.equals(this.id, subTypeOfWork.id) && Objects.equals(this.tenantId, subTypeOfWork.tenantId)
				&& Objects.equals(this.name, subTypeOfWork.name) && Objects.equals(this.code, subTypeOfWork.code)
				&& Objects.equals(this.description, subTypeOfWork.description)
				&& Objects.equals(this.active, subTypeOfWork.active)
				&& Objects.equals(this.parent, subTypeOfWork.parent);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tenantId, name, code, description, active, parent);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class SubTypeOfWork {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    code: ").append(toIndentedString(code)).append("\n");
		sb.append("    description: ").append(toIndentedString(description)).append("\n");
		sb.append("    active: ").append(toIndentedString(active)).append("\n");
		sb.append("    parent: ").append(toIndentedString(parent)).append("\n");
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
