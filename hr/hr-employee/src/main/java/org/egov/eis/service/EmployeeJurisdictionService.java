package org.egov.eis.service;

import org.egov.eis.model.Employee;
import org.egov.eis.repository.EmployeeJurisdictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeJurisdictionService {

	@Autowired
	private EmployeeJurisdictionRepository employeeJurisdictionRepository;
	
	public void update(Employee employee) {
		employee.getJurisdictions().forEach((jurisdiction) -> {
			// if jurisdiction id already exists in table, we don't do anything. When absent, we insert the record.
			if (!employeeJurisdictionRepository.jurisdictionAlreadyExists(jurisdiction, employee.getId(), employee.getTenantId()))
				employeeJurisdictionRepository.insert(jurisdiction, employee.getId(), employee.getTenantId());
			
			// employeeJurisdictionRepository.findAndDeleteInDBThatAreNotInList(employee.getServiceHistory());
		});
	}


}
