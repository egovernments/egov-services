package org.egov.eis.model;

import java.util.ArrayList;
import org.egov.eis.entity.Department;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DepartmentRes {
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("Department")
	private List<Department> department = new ArrayList<Department>();

	public DepartmentRes responseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
		return this;
	}

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public DepartmentRes department(List<Department> department) {
		this.department = department;
		return this;
	}

	public DepartmentRes addDepartmentItem(Department departmentItem) {
		this.department.add(departmentItem);
		return this;
	}

	public List<Department> getDepartment() {
		return department;
	}

	public void setDepartment(List<Department> department) {
		this.department = department;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DepartmentRes departmentRes = (DepartmentRes) o;
		return Objects.equals(this.responseInfo, departmentRes.responseInfo)
				&& Objects.equals(this.department, departmentRes.department);
	}

	@Override
	public int hashCode() {
		return Objects.hash(responseInfo, department);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class DepartmentRes {\n");

		sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
		sb.append("    department: ").append(toIndentedString(department)).append("\n");
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
