package org.egov.eis.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.persistence.entity.Assignment;
import org.egov.eis.persistence.entity.Employee;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeeRes {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("Qualifications")
	private List<EducationalQualification> qualifications = new ArrayList<EducationalQualification>();

	@JsonProperty("Assignments")
	private List<Assignment> assignments = new ArrayList<Assignment>();

	@JsonProperty("Jurisdictions")
	private List<Boundary> jurisdictions = new ArrayList<Boundary>();

	@JsonProperty("Probations")
	private List<Probation> probations = new ArrayList<Probation>();

	@JsonProperty("Services")
	private List<ServiceHistory> services = new ArrayList<ServiceHistory>();

	@JsonProperty("Employees")
	private List<Employee> employees = new ArrayList<Employee>();

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

	public EmployeeRes qualifications(List<EducationalQualification> qualifications) {
		this.qualifications = qualifications;
		return this;
	}

	public EmployeeRes addEducationItem(EducationalQualification qualifications) {
		this.qualifications.add(qualifications);
		return this;
	}

	public List<EducationalQualification> getQualifications() {
		return qualifications;
	}

	public void setQualifications(List<EducationalQualification> qualifications) {
		this.qualifications = qualifications;
	}

	public List<Assignment> getAssignments() {
		return assignments;
	}

	public void setAssignments(List<Assignment> assignments) {
		this.assignments = assignments;
	}

	public List<Boundary> getJurisdictions() {
		return jurisdictions;
	}

	public void setJurisdictions(List<Boundary> jurisdictions) {
		this.jurisdictions = jurisdictions;
	}

	public List<Probation> getProbations() {
		return probations;
	}

	public void setProbations(List<Probation> probations) {
		this.probations = probations;
	}

	public List<ServiceHistory> getServices() {
		return services;
	}

	public void setServices(List<ServiceHistory> services) {
		this.services = services;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public List<EducationalQualification> getEducation() {
		return qualifications;
	}

	public EmployeeRes assignments(List<Assignment> assignments) {
		this.assignments = assignments;
		return this;
	}

	public EmployeeRes addAssignmentItem(Assignment assignmentItem) {
		this.assignments.add(assignmentItem);
		return this;
	}

	public EmployeeRes jurisdictions(List<Boundary> jurisdictions) {
		this.jurisdictions = jurisdictions;
		return this;
	}

	public EmployeeRes addJurisdictionItem(Boundary jurisdictionItem) {
		this.jurisdictions.add(jurisdictionItem);
		return this;
	}

	public EmployeeRes probations(List<Probation> probations) {
		this.probations = probations;
		return this;
	}

	public EmployeeRes addProbationItem(Probation probationItem) {
		this.probations.add(probationItem);
		return this;
	}

	public EmployeeRes services(List<ServiceHistory> services) {
		this.services = services;
		return this;
	}

	public EmployeeRes addServiceItem(ServiceHistory serviceItem) {
		this.services.add(serviceItem);
		return this;
	}

	public EmployeeRes employees(List<Employee> employees) {
		this.employees = employees;
		return this;
	}

	public EmployeeRes addEmployeeItem(Employee employeeItem) {
		this.employees.add(employeeItem);
		return this;
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
				&& Objects.equals(this.qualifications, employeeRes.qualifications)
				&& Objects.equals(this.assignments, employeeRes.assignments)
				&& Objects.equals(this.jurisdictions, employeeRes.jurisdictions)
				&& Objects.equals(this.probations, employeeRes.probations)
				&& Objects.equals(this.services, employeeRes.services)
				&& Objects.equals(this.employees, employeeRes.employees);
	}

	@Override
	public int hashCode() {
		return Objects.hash(responseInfo, qualifications, assignments, jurisdictions, probations, services, employees);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class EmployeeRes {\n");

		sb.append("    responseInfo: ").append(toIndentedString(responseInfo)).append("\n");
		sb.append("    education: ").append(toIndentedString(qualifications)).append("\n");
		sb.append("    assignment: ").append(toIndentedString(assignments)).append("\n");
		sb.append("    jurisdiction: ").append(toIndentedString(jurisdictions)).append("\n");
		sb.append("    probation: ").append(toIndentedString(probations)).append("\n");
		sb.append("    service: ").append(toIndentedString(services)).append("\n");
		sb.append("    employee: ").append(toIndentedString(employees)).append("\n");
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
