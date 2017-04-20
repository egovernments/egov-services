package org.egov.eis.web.contract;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DepartmentDesignation {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("department")
	private Object department = null;

	@JsonProperty("designation")
	private Object designation = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	public DepartmentDesignation id(Long id) {
		this.id = id;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DepartmentDesignation department(Object department) {
		this.department = department;
		return this;
	}

	public Object getDepartment() {
		return department;
	}

	public void setDepartment(Object department) {
		this.department = department;
	}

	public DepartmentDesignation designation(Object designation) {
		this.designation = designation;
		return this;
	}

	public Object getDesignation() {
		return designation;
	}

	public void setDesignation(Object designation) {
		this.designation = designation;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DepartmentDesignation departmentDesignation = (DepartmentDesignation) o;
		return Objects.equals(this.id, departmentDesignation.id)
				&& Objects.equals(this.department, departmentDesignation.department)
				&& Objects.equals(this.tenantId, departmentDesignation.tenantId)
				&& Objects.equals(this.designation, departmentDesignation.designation);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, department, designation);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class DepartmentDesignation {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    department: ").append(toIndentedString(department)).append("\n");
		sb.append("    designation: ").append(toIndentedString(designation)).append("\n");
		sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}
