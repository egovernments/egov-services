package org.egov.works.commons.web.contract;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * Role
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-08T13:25:44.581Z")

public class Role {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("code")
	private String code = null;

	@JsonProperty("description")
	private String description = null;

	@JsonProperty("createdBy")
	private Long createdBy = null;

	@JsonProperty("createdDate")
	private LocalDate createdDate = null;

	@JsonProperty("lastModifiedBy")
	private Long lastModifiedBy = null;

	@JsonProperty("lastModifiedDate")
	private LocalDate lastModifiedDate = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	public Role id(Long id) {
		this.id = id;
		return this;
	}

	/**
	 * Unique identifier of the role.
	 * 
	 * @return id
	 **/
	@ApiModelProperty(value = "Unique identifier of the role.")

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Role name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * The Name of the Role.
	 * 
	 * @return name
	 **/
	@ApiModelProperty(required = true, value = "The Name of the Role.")
	@NotNull

	@Size(min = 2, max = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Role code(String code) {
		this.code = code;
		return this;
	}

	/**
	 * The Code of the Role.
	 * 
	 * @return code
	 **/
	@ApiModelProperty(value = "The Code of the Role.")

	@Size(min = 2, max = 50)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Role description(String description) {
		this.description = description;
		return this;
	}

	/**
	 * The description of the Role.
	 * 
	 * @return description
	 **/
	@ApiModelProperty(value = "The description of the Role.")

	@Size(max = 256)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Role createdBy(Long createdBy) {
		this.createdBy = createdBy;
		return this;
	}

	/**
	 * Id of the user who created the record.
	 * 
	 * @return createdBy
	 **/
	@ApiModelProperty(value = "Id of the user who created the record.")

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Role createdDate(LocalDate createdDate) {
		this.createdDate = createdDate;
		return this;
	}

	/**
	 * Date on which the role master data was added into the system.
	 * 
	 * @return createdDate
	 **/
	@ApiModelProperty(value = "Date on which the role master data was added into the system.")

	@Valid

	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	public Role lastModifiedBy(Long lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
		return this;
	}

	/**
	 * Id of the user who last modified the record.
	 * 
	 * @return lastModifiedBy
	 **/
	@ApiModelProperty(value = "Id of the user who last modified the record.")

	public Long getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(Long lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Role lastModifiedDate(LocalDate lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
		return this;
	}

	/**
	 * Date on which the role master data was last modified.
	 * 
	 * @return lastModifiedDate
	 **/
	@ApiModelProperty(value = "Date on which the role master data was last modified.")

	@Valid

	public LocalDate getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(LocalDate lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Role tenantId(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	/**
	 * Unique Identifier of the tenant, Like AP, AP.Kurnool etc.
	 * 
	 * @return tenantId
	 **/
	@ApiModelProperty(required = true, value = "Unique Identifier of the tenant, Like AP, AP.Kurnool etc.")
	@NotNull

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Role role = (Role) o;
		return Objects.equals(this.id, role.id) && Objects.equals(this.name, role.name)
				&& Objects.equals(this.code, role.code) && Objects.equals(this.description, role.description)
				&& Objects.equals(this.createdBy, role.createdBy) && Objects.equals(this.createdDate, role.createdDate)
				&& Objects.equals(this.lastModifiedBy, role.lastModifiedBy)
				&& Objects.equals(this.lastModifiedDate, role.lastModifiedDate)
				&& Objects.equals(this.tenantId, role.tenantId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, code, description, createdBy, createdDate, lastModifiedBy, lastModifiedDate,
				tenantId);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Role {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    code: ").append(toIndentedString(code)).append("\n");
		sb.append("    description: ").append(toIndentedString(description)).append("\n");
		sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
		sb.append("    createdDate: ").append(toIndentedString(createdDate)).append("\n");
		sb.append("    lastModifiedBy: ").append(toIndentedString(lastModifiedBy)).append("\n");
		sb.append("    lastModifiedDate: ").append(toIndentedString(lastModifiedDate)).append("\n");
		sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
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
