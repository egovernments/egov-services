package org.egov.eis.web.contract;

import java.util.Objects;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Assignment {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("position")
	private Object position = null;

	@JsonProperty("fund")
	private String fund = null;

	@JsonProperty("functionary")
	private String functionary = null;

	@JsonProperty("function")
	private String function = null;

	@JsonProperty("designation")
	private String designation = null;

	@JsonProperty("department")
	private String department = null;

	@JsonProperty("primary")
	private Boolean primary = null;

	@JsonProperty("fromDate")
	private LocalDate fromDate = null;

	@JsonProperty("toDate")
	private LocalDate toDate = null;

	@JsonProperty("grade")
	private Object grade = null;

	@JsonProperty("govtOrderNumber")
	private String govtOrderNumber = null;

	public Assignment(org.egov.eis.persistence.entity.Assignment entityAssignment) {
		id = entityAssignment.getId();
		department = entityAssignment.getDepartment() != null ? entityAssignment.getDepartment().getName() : "";
		designation = entityAssignment.getDesignation() != null ? entityAssignment.getDesignation().getName() : "";
		fund = entityAssignment.getFund().toString();
	}

	public Assignment id(Long id) {
		this.id = id;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Assignment position(Object position) {
		this.position = position;
		return this;
	}

	public Object getPosition() {
		return position;
	}

	public void setPosition(Object position) {
		this.position = position;
	}

	public Assignment fund(String fund) {
		this.fund = fund;
		return this;
	}

	public String getFund() {
		return fund;
	}

	public void setFund(String fund) {
		this.fund = fund;
	}

	public Assignment functionary(String functionary) {
		this.functionary = functionary;
		return this;
	}

	public String getFunctionary() {
		return functionary;
	}

	public void setFunctionary(String functionary) {
		this.functionary = functionary;
	}

	public Assignment function(String function) {
		this.function = function;
		return this;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public Assignment designation(String designation) {
		this.designation = designation;
		return this;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public Assignment department(String department) {
		this.department = department;
		return this;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Assignment primary(Boolean primary) {
		this.primary = primary;
		return this;
	}

	public Boolean getPrimary() {
		return primary;
	}

	public void setPrimary(Boolean primary) {
		this.primary = primary;
	}

	public Assignment fromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
		return this;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public Assignment toDate(LocalDate toDate) {
		this.toDate = toDate;
		return this;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	public Assignment grade(Object grade) {
		this.grade = grade;
		return this;
	}

	public Object getGrade() {
		return grade;
	}

	public void setGrade(Object grade) {
		this.grade = grade;
	}

	public Assignment govtOrderNumber(String govtOrderNumber) {
		this.govtOrderNumber = govtOrderNumber;
		return this;
	}

	public String getGovtOrderNumber() {
		return govtOrderNumber;
	}

	public void setGovtOrderNumber(String govtOrderNumber) {
		this.govtOrderNumber = govtOrderNumber;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Assignment assignment = (Assignment) o;
		return Objects.equals(this.id, assignment.id) && Objects.equals(this.position, assignment.position)
				&& Objects.equals(this.fund, assignment.fund)
				&& Objects.equals(this.functionary, assignment.functionary)
				&& Objects.equals(this.function, assignment.function)
				&& Objects.equals(this.designation, assignment.designation)
				&& Objects.equals(this.department, assignment.department)
				&& Objects.equals(this.primary, assignment.primary)
				&& Objects.equals(this.fromDate, assignment.fromDate) && Objects.equals(this.toDate, assignment.toDate)
				&& Objects.equals(this.grade, assignment.grade)
				&& Objects.equals(this.govtOrderNumber, assignment.govtOrderNumber);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, position, fund, functionary, function, designation, department, primary, fromDate,
				toDate, grade, govtOrderNumber);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Assignment {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    position: ").append(toIndentedString(position)).append("\n");
		sb.append("    fund: ").append(toIndentedString(fund)).append("\n");
		sb.append("    functionary: ").append(toIndentedString(functionary)).append("\n");
		sb.append("    function: ").append(toIndentedString(function)).append("\n");
		sb.append("    designation: ").append(toIndentedString(designation)).append("\n");
		sb.append("    department: ").append(toIndentedString(department)).append("\n");
		sb.append("    primary: ").append(toIndentedString(primary)).append("\n");
		sb.append("    fromDate: ").append(toIndentedString(fromDate)).append("\n");
		sb.append("    toDate: ").append(toIndentedString(toDate)).append("\n");
		sb.append("    grade: ").append(toIndentedString(grade)).append("\n");
		sb.append("    govtOrderNumber: ").append(toIndentedString(govtOrderNumber)).append("\n");
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
