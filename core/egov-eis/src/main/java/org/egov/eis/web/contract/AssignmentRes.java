package org.egov.eis.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AssignmentRes {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("Assignment")
	private List<Assignment> assignment = new ArrayList<Assignment>();

	public AssignmentRes(List<org.egov.eis.persistence.entity.Assignment> entityAssignments) {
		assignment = entityAssignments.stream().map(Assignment::new).collect(Collectors.toList());
	}

	public AssignmentRes responseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
		return this;
	}

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public AssignmentRes assignment(List<Assignment> assignment) {
		this.assignment = assignment;
		return this;
	}

	public AssignmentRes addAssignmentItem(Assignment assignmentItem) {
		this.assignment.add(assignmentItem);
		return this;
	}

	public List<Assignment> getAssignment() {
		return assignment;
	}

	public void setAssignment(List<Assignment> assignment) {
		this.assignment = assignment;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		AssignmentRes assignmentRes = (AssignmentRes) o;
		return Objects.equals(this.responseInfo, assignmentRes.responseInfo)
				&& Objects.equals(this.assignment, assignmentRes.assignment);
	}

	@Override
	public int hashCode() {
		return Objects.hash(responseInfo, assignment);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class AssignmentRes {\n");

		sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
		sb.append("    assignment: ").append(toIndentedString(assignment)).append("\n");
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
