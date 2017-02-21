package org.egov.user.model;

import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Role {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("name")
	private String name = null;

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

	public Role id(Long id) {
		this.id = id;
		return this;
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Role description(String description) {
		this.description = description;
		return this;
	}

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

	public LocalDate getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(LocalDate lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
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
				&& Objects.equals(this.description, role.description) && Objects.equals(this.createdBy, role.createdBy)
				&& Objects.equals(this.createdDate, role.createdDate)
				&& Objects.equals(this.lastModifiedBy, role.lastModifiedBy)
				&& Objects.equals(this.lastModifiedDate, role.lastModifiedDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, description, createdBy, createdDate, lastModifiedBy, lastModifiedDate);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Role {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    description: ").append(toIndentedString(description)).append("\n");
		sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
		sb.append("    createdDate: ").append(toIndentedString(createdDate)).append("\n");
		sb.append("    lastModifiedBy: ").append(toIndentedString(lastModifiedBy)).append("\n");
		sb.append("    lastModifiedDate: ").append(toIndentedString(lastModifiedDate)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
