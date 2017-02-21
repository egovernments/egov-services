package org.egov.eis.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.egov.eis.persistence.entity.Designation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DesignationRes {
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("Designation")
	private List<Designation> designation = new ArrayList<Designation>();

	public DesignationRes responseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
		return this;
	}

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public DesignationRes designation(List<Designation> designation) {
		this.designation = designation;
		return this;
	}

	public DesignationRes addDesignationItem(Designation designationItem) {
		this.designation.add(designationItem);
		return this;
	}

	public List<Designation> getDesignation() {
		return designation;
	}

	public void setDesignation(List<Designation> designation) {
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
		DesignationRes designationRes = (DesignationRes) o;
		return Objects.equals(this.responseInfo, designationRes.responseInfo)
				&& Objects.equals(this.designation, designationRes.designation);
	}

	@Override
	public int hashCode() {
		return Objects.hash(responseInfo, designation);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class DesignationRes {\n");

		sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
		sb.append("    designation: ").append(toIndentedString(designation)).append("\n");
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
