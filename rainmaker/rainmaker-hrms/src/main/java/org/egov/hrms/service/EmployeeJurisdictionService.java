package org.egov.hrms.service;

import org.egov.hrms.model.Employee;
import org.egov.hrms.repository.EmployeeJurisdictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeeJurisdictionService {

	@Autowired
	private EmployeeJurisdictionRepository employeeJurisdictionRepository;

	public void update(Employee employee) {
		List<Long> jurisdictionsFromDb = employeeJurisdictionRepository.findByEmployeeId(employee.getId(),
				employee.getTenantId());
		employee.getJurisdictions().forEach((jurisdiction) -> {
			if (needsInsert(jurisdiction, jurisdictionsFromDb))
				employeeJurisdictionRepository.insert(jurisdiction, employee.getId(), employee.getTenantId());
		});
		deleteJurisdictionsInDbThatAreNotInInput(employee.getJurisdictions(), jurisdictionsFromDb, employee.getId(),
				employee.getTenantId());

	}

	private void deleteJurisdictionsInDbThatAreNotInInput(List<Long> jurisdictions, List<Long> jurisdictionsFromDb,
			Long employeeId, String tenantId) {
		List<Long> jurisdictionsIdsToDelete = getListOfJurisdictionsIdsToDelete(jurisdictions, jurisdictionsFromDb);
		if (!jurisdictionsIdsToDelete.isEmpty())
			employeeJurisdictionRepository.delete(jurisdictionsIdsToDelete, employeeId, tenantId);

	}

	private List<Long> getListOfJurisdictionsIdsToDelete(List<Long> jurisdictions, List<Long> jurisdictionsFromDb) {
		List<Long> assignmentsIdsToDelete = new ArrayList<>();
		for (Long jurisdictionInDb : jurisdictionsFromDb) {
			boolean found = false;
			for (Long jurisdiction : jurisdictions)
				if (jurisdiction.equals(jurisdictionInDb)) {
					found = true;
					break;
				}
			if (!found)
				assignmentsIdsToDelete.add(jurisdictionInDb);
		}
		return assignmentsIdsToDelete;
	}

	private boolean needsInsert(Long jurisdiction, List<Long> jurisdictionsFromDb) {
		for (Long jurisdictionInDb : jurisdictionsFromDb)
			if (jurisdiction.equals(jurisdictionInDb))
				return false;
		return true;
	}
}
