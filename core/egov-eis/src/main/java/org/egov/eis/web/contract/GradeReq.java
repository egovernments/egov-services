package org.egov.eis.web.contract;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GradeReq {
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;

	@JsonProperty("Grade")
	private Grade grade = null;

	public GradeReq requestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
		return this;
	}

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public GradeReq grade(Grade grade) {
		this.grade = grade;
		return this;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		GradeReq gradeReq = (GradeReq) o;
		return Objects.equals(this.requestInfo, gradeReq.requestInfo) && Objects.equals(this.grade, gradeReq.grade);
	}

	@Override
	public int hashCode() {
		return Objects.hash(requestInfo, grade);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class GradeReq {\n");

		sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
		sb.append("    grade: ").append(toIndentedString(grade)).append("\n");
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
