package org.egov.eis.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeeReq {
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;

	@JsonProperty("Employee")
	private Employee employee = null;

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

	public EmployeeReq requestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
		return this;
	}

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public EmployeeReq employee(Employee employee) {
		this.employee = employee;
		return this;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public EmployeeReq education(List<EducationalQualification> education) {
		this.education = education;
		return this;
	}

	public EmployeeReq addEducationItem(EducationalQualification educationItem) {
		this.education.add(educationItem);
		return this;
	}

	public List<EducationalQualification> getEducation() {
		return education;
	}

	public void setEducation(List<EducationalQualification> education) {
		this.education = education;
	}

	public EmployeeReq assignment(List<Assignment> assignment) {
		this.assignment = assignment;
		return this;
	}

	public EmployeeReq addAssignmentItem(Assignment assignmentItem) {
		this.assignment.add(assignmentItem);
		return this;
	}

	public List<Assignment> getAssignment() {
		return assignment;
	}

	public void setAssignment(List<Assignment> assignment) {
		this.assignment = assignment;
	}

	public EmployeeReq jurisdiction(List<Boundary> jurisdiction) {
		this.jurisdiction = jurisdiction;
		return this;
	}

	public EmployeeReq addJurisdictionItem(Boundary jurisdictionItem) {
		this.jurisdiction.add(jurisdictionItem);
		return this;
	}

	public List<Boundary> getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(List<Boundary> jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	public EmployeeReq probation(List<Probation> probation) {
		this.probation = probation;
		return this;
	}

	public EmployeeReq addProbationItem(Probation probationItem) {
		this.probation.add(probationItem);
		return this;
	}

	public List<Probation> getProbation() {
		return probation;
	}

	public void setProbation(List<Probation> probation) {
		this.probation = probation;
	}

	public EmployeeReq service(List<ServiceHistory> service) {
		this.service = service;
		return this;
	}

	public EmployeeReq addServiceItem(ServiceHistory serviceItem) {
		this.service.add(serviceItem);
		return this;
	}

	public List<ServiceHistory> getService() {
		return service;
	}

	public void setService(List<ServiceHistory> service) {
		this.service = service;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		EmployeeReq employeeReq = (EmployeeReq) o;
		return Objects.equals(this.requestInfo, employeeReq.requestInfo)
				&& Objects.equals(this.employee, employeeReq.employee)
				&& Objects.equals(this.education, employeeReq.education)
				&& Objects.equals(this.assignment, employeeReq.assignment)
				&& Objects.equals(this.jurisdiction, employeeReq.jurisdiction)
				&& Objects.equals(this.probation, employeeReq.probation)
				&& Objects.equals(this.service, employeeReq.service);
	}

	@Override
	public int hashCode() {
		return Objects.hash(requestInfo, employee, education, assignment, jurisdiction, probation, service);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class EmployeeReq {\n");

		sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
		sb.append("    employee: ").append(toIndentedString(employee)).append("\n");
		sb.append("    education: ").append(toIndentedString(education)).append("\n");
		sb.append("    assignment: ").append(toIndentedString(assignment)).append("\n");
		sb.append("    jurisdiction: ").append(toIndentedString(jurisdiction)).append("\n");
		sb.append("    probation: ").append(toIndentedString(probation)).append("\n");
		sb.append("    service: ").append(toIndentedString(service)).append("\n");
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
