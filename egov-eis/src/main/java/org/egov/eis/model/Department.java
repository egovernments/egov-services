package org.egov.eis.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Department {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("code")
	private String code = null;

	@JsonProperty("active")
	private Boolean active = null;

	public Department id(Long id) {
		this.id = id;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Department name(String name) {
		this.name = name;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Department code(String code) {
		this.code = code;
		return this;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Department active(Boolean active) {
		this.active = active;
		return this;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Department department = (Department) o;
		return Objects.equals(this.id, department.id) && Objects.equals(this.name, department.name)
				&& Objects.equals(this.code, department.code) && Objects.equals(this.active, department.active);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, code, active);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Department {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    code: ").append(toIndentedString(code)).append("\n");
		sb.append("    active: ").append(toIndentedString(active)).append("\n");
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
