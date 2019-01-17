package org.egov.hrms.service;

import org.egov.hrms.model.Employee;
import org.egov.hrms.model.Regularisation;
import org.egov.hrms.model.enums.EntityType;
import org.egov.hrms.repository.EmployeeDocumentsRepository;
import org.egov.hrms.repository.RegularisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class RegularisationService {
	
	@Autowired
	private RegularisationRepository regularisationRepository;
	
	@Autowired
	private EmployeeDocumentsRepository employeeDocumentsRepository; 
	
	public void update(Employee employee) {
		List<Regularisation> regularisations = regularisationRepository.findByEmployeeId(employee.getId(),
				employee.getTenantId());
		if (!isEmpty(employee.getRegularisation())) {
			employee.getRegularisation().forEach((regularisation) -> {
				if (needsInsert(regularisation, regularisations)) {
					regularisationRepository.insert(regularisation, employee.getId());
				} else if (needsUpdate(regularisation, regularisations)) {
					regularisation.setTenantId(employee.getTenantId());
					regularisationRepository.update(regularisation);
				}
			});
		}
		deleteRegularisationsInDBThatAreNotInInput(employee.getRegularisation(), regularisations,  employee.getId(),employee.getTenantId());
	}

	private void deleteRegularisationsInDBThatAreNotInInput(List<Regularisation> inputRegularisations,
			List<Regularisation> regularisationsFromDb, Long employeeId, String tenantId) {
		
		List<Long> regularisationsIdsToDelete = getListOfRegulationIdsToDelete(inputRegularisations, regularisationsFromDb);
		if (!regularisationsIdsToDelete.isEmpty()) {
			employeeDocumentsRepository.deleteForReferenceIds(employeeId, EntityType.REGULARISATION, regularisationsIdsToDelete, tenantId);
			regularisationRepository.delete(regularisationsIdsToDelete, employeeId, tenantId);
		}
	}

	private List<Long> getListOfRegulationIdsToDelete(List<Regularisation> inputRegularisations,
			List<Regularisation> regularisationsFromDb) {
		List<Long> regularisationsIdsToDelete = new ArrayList<>();
		for (Regularisation regularisationInDb : regularisationsFromDb) {
			boolean found = false;
			if (!isEmpty(inputRegularisations)) {
				// if empty, found remains false and the record becomes eligible for deletion.
				for (Regularisation inputRegularisation : inputRegularisations)
					if (inputRegularisation.getId().equals(regularisationInDb.getId())) {
						found = true;
						break;
					}
			}
			
			if (!found) regularisationsIdsToDelete.add(regularisationInDb.getId());
		}
		return regularisationsIdsToDelete;
	}

	/**
	 * Note: needsUpdate checks if any field has changed by comparing with contents
	 * from db. If yes, then only do an update.
	 */
	private boolean needsUpdate(Regularisation regularisation, List<Regularisation> regularisations) {
		for (Regularisation oldRegularisation : regularisations) 
			if (regularisation.equals(oldRegularisation)) return false; 
		return true;
	}

	private boolean needsInsert(Regularisation regularisation, List<Regularisation> regularisations) {
		for (Regularisation oldRegularisation : regularisations) {
			if (regularisation.getId().equals(oldRegularisation.getId()))
				return false;
		}
		return true;
	}

}
