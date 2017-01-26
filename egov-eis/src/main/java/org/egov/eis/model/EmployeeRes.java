package org.egov.eis.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.Employee;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeeRes {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("Education")
	private List<EducationalQualification> education = new ArrayList<EducationalQualification>();

	@JsonProperty("Assignment")
	private List<Assignment> assignment = new ArrayList<Assignment>();

	@JsonProperty("Jurisdiction")
	private List<Boundary> jurisdiction = new ArrayList<Boundary>();

	@JsonProperty("Probation")
	private List<Probation> probation = new ArrayList<Probation>();

	@JsonProperty("Service")
	private List<ServiceHistory> service = new ArrayList<ServiceHistory>();

	@JsonProperty("Employee")
	private List<Employee> employee = new ArrayList<Employee>();

	public EmployeeRes responseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
		return this;
	}

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public EmployeeRes education(List<EducationalQualification> education) {
		this.education = education;
		return this;
	}

	public EmployeeRes addEducationItem(EducationalQualification educationItem) {
		this.education.add(educationItem);
		return this;
	}

	public List<EducationalQualification> getEducation() {
		return education;
	}

	public void setEducation(List<EducationalQualification> education) {
		this.education = education;
	}

	public EmployeeRes assignment(List<Assignment> assignment) {
		this.assignment = assignment;
		return this;
	}

	public EmployeeRes addAssignmentItem(Assignment assignmentItem) {
		this.assignment.add(assignmentItem);
		return this;
	}

	public List<Assignment> getAssignment() {
		return assignment;
	}

	public void setAssignment(List<Assignment> assignment) {
		this.assignment = assignment;
	}

	public EmployeeRes jurisdiction(List<Boundary> jurisdiction) {
		this.jurisdiction = jurisdiction;
		return this;
	}

	public EmployeeRes addJurisdictionItem(Boundary jurisdictionItem) {
		this.jurisdiction.add(jurisdictionItem);
		return this;
	}

	public List<Boundary> getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(List<Boundary> jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	public EmployeeRes probation(List<Probation> probation) {
		this.probation = probation;
		return this;
	}

	public EmployeeRes addProbationItem(Probation probationItem) {
		this.probation.add(probationItem);
		return this;
	}

	public List<Probation> getProbation() {
		return probation;
	}

	public void setProbation(List<Probation> probation) {
		this.probation = probation;
	}

	public EmployeeRes service(List<ServiceHistory> service) {
		this.service = service;
		return this;
	}

	public EmployeeRes addServiceItem(ServiceHistory serviceItem) {
		this.service.add(serviceItem);
		return this;
	}

	public List<ServiceHistory> getService() {
		return service;
	}

	public void setService(List<ServiceHistory> service) {
		this.service = service;
	}

	public EmployeeRes employee(List<Employee> employee) {
		this.employee = employee;
		return this;
	}

	public EmployeeRes addEmployeeItem(Employee employeeItem) {
		this.employee.add(employeeItem);
		return this;
	}

	public List<Employee> getEmployee() {
		return employee;
	}

	public void setEmployee(List<Employee> employee) {
		this.employee = employee;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		EmployeeRes employeeRes = (EmployeeRes) o;
		return Objects.equals(this.responseInfo, employeeRes.responseInfo)
				&& Objects.equals(this.education, employeeRes.education)
				&& Objects.equals(this.assignment, employeeRes.assignment)
				&& Objects.equals(this.jurisdiction, employeeRes.jurisdiction)
				&& Objects.equals(this.probation, employeeRes.probation)
				&& Objects.equals(this.service, employeeRes.service)
				&& Objects.equals(this.employee, employeeRes.employee);
	}

	@Override
	public int hashCode() {
		return Objects.hash(responseInfo, education, assignment, jurisdiction, probation, service, employee);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class EmployeeRes {\n");

		sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
		sb.append("    education: ").append(toIndentedString(education)).append("\n");
		sb.append("    assignment: ").append(toIndentedString(assignment)).append("\n");
		sb.append("    jurisdiction: ").append(toIndentedString(jurisdiction)).append("\n");
		sb.append("    probation: ").append(toIndentedString(probation)).append("\n");
		sb.append("    service: ").append(toIndentedString(service)).append("\n");
		sb.append("    employee: ").append(toIndentedString(employee)).append("\n");
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
