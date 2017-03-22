package org.egov.eis.service.helper;

import java.util.Date;

import org.egov.eis.model.Employee;
import org.egov.eis.model.User;
import org.egov.eis.repository.EmployeeRepository;
import org.egov.eis.web.contract.EmployeeRequest;
import org.egov.eis.web.contract.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeHelper {

	@Autowired
	private EmployeeRepository employeeRepository;

	private enum Sequences {
		ASSIGNMENT("seq_egeis_assignment"),
		DEPARTMENTALTEST("seq_egeis_departmentaltest"),
		EDUCATIONALQUALIFICATION("seq_egeis_educationalqualification"),
		HODDEPARTMENT("seq_egeis_hoddepartment"),
		PROBATION("seq_egeis_probation"),
		REGULARISATION("seq_egeis_regularisation"),
		SERVICEHISTORY("seq_egeis_servicehistory"),
		TECHNICALQUALIFICATION("seq_egeis_technicalqualification");

		String sequenceName;
		private Sequences(String sequenceName) {
			this.sequenceName = sequenceName;
		}

		@Override
		public String toString() {
			return sequenceName;
		}
	}

	public String getEmployeeCode(Long id) {
		return "EMP" + id;
	}

	public UserRequest getUserRequest(EmployeeRequest employeeRequest) {
		UserRequest userRequest = new UserRequest();
		userRequest.setRequestInfo(employeeRequest.getRequestInfo());
		User user = employeeRequest.getEmployee().getUser();
		user.setTenantId(employeeRequest.getEmployee().getTenantId());
		userRequest.setUser(user);

		return userRequest;
	}

	public void populateIds(EmployeeRequest employeeRequest) {
		Employee employee = employeeRequest.getEmployee();

		employee.getAssignments().forEach((assignment) -> {
			assignment.setId(employeeRepository.generateSequence(Sequences.ASSIGNMENT.toString()));
			assignment.setTenantId(employee.getTenantId());
			assignment.setCreatedBy(Long.parseLong((employeeRequest.getRequestInfo().getRequesterId())));
			assignment.setCreatedDate(new Date());
			assignment.setLastModifiedBy((Long.parseLong((employeeRequest.getRequestInfo().getRequesterId()))));
			assignment.setLastModifiedDate((new Date()));
		});
		employee.getAssignments().forEach((assignment) -> {
			assignment.getHod().forEach((hod) -> {
				hod.setId(employeeRepository.generateSequence(Sequences.HODDEPARTMENT.toString()));
				hod.setTenantId(employee.getTenantId());
			});
		});
		employee.getEducation().forEach((eduction) -> {
			eduction.setId(employeeRepository.generateSequence(Sequences.EDUCATIONALQUALIFICATION.toString()));
			eduction.setTenantId(employee.getTenantId());
			eduction.setCreatedBy(Long.parseLong((employeeRequest.getRequestInfo().getRequesterId())));
			eduction.setCreatedDate(new Date());
			eduction.setLastModifiedBy((Long.parseLong((employeeRequest.getRequestInfo().getRequesterId()))));
			eduction.setLastModifiedDate((new Date()));
		});
		employee.getTest().forEach((test) -> {
			test.setId(employeeRepository.generateSequence(Sequences.DEPARTMENTALTEST.toString()));
			test.setTenantId(employee.getTenantId());
			test.setCreatedBy(Long.parseLong((employeeRequest.getRequestInfo().getRequesterId())));
			test.setCreatedDate(new Date());
			test.setLastModifiedBy((Long.parseLong((employeeRequest.getRequestInfo().getRequesterId()))));
			test.setLastModifiedDate((new Date()));
		});
		employee.getProbation().forEach((probation) -> {
			probation.setId(employeeRepository.generateSequence(Sequences.PROBATION.toString()));
			probation.setTenantId(employee.getTenantId());
			probation.setCreatedBy(Long.parseLong((employeeRequest.getRequestInfo().getRequesterId())));
			probation.setCreatedDate(new Date());
			probation.setLastModifiedBy((Long.parseLong((employeeRequest.getRequestInfo().getRequesterId()))));
			probation.setLastModifiedDate((new Date()));
		});
		employee.getRegularisation().forEach((regularisation) -> {
			regularisation.setId(employeeRepository.generateSequence(Sequences.REGULARISATION.toString()));
			regularisation.setTenantId(employee.getTenantId());
			regularisation.setCreatedBy(Long.parseLong((employeeRequest.getRequestInfo().getRequesterId())));
			regularisation.setCreatedDate(new Date());
			regularisation.setLastModifiedBy((Long.parseLong((employeeRequest.getRequestInfo().getRequesterId()))));
			regularisation.setLastModifiedDate((new Date()));
		});
		employee.getServiceHistory().forEach((serviceHistory) -> {
			serviceHistory.setId(employeeRepository.generateSequence(Sequences.SERVICEHISTORY.toString()));
			serviceHistory.setTenantId(employee.getTenantId());
			serviceHistory.setCreatedBy(Long.parseLong((employeeRequest.getRequestInfo().getRequesterId())));
			serviceHistory.setCreatedDate(new Date());
			serviceHistory.setLastModifiedBy((Long.parseLong((employeeRequest.getRequestInfo().getRequesterId()))));
			serviceHistory.setLastModifiedDate((new Date()));
		});
		employee.getTechnical().forEach((technical) -> {
			technical.setId(employeeRepository.generateSequence(Sequences.TECHNICALQUALIFICATION.toString()));
			technical.setTenantId(employee.getTenantId());
			technical.setCreatedBy(Long.parseLong((employeeRequest.getRequestInfo().getRequesterId())));
			technical.setCreatedDate(new Date());
			technical.setLastModifiedBy((Long.parseLong((employeeRequest.getRequestInfo().getRequesterId()))));
			technical.setLastModifiedDate((new Date()));
		});
	}
}
