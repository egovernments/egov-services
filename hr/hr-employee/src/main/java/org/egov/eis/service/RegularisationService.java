package org.egov.eis.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;

import org.egov.eis.model.Employee;
import org.egov.eis.model.Regularisation;
import org.egov.eis.repository.RegularisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegularisationService {
	
	@Autowired
	private RegularisationRepository regularisationRepository;
	
	public void update(Employee employee) {
		if(isEmpty(employee.getRegularisations()))
			return;
		List<Regularisation> regularisations = regularisationRepository.findByEmployeeId(employee.getId(), employee.getTenantId());
		employee.getRegularisations().forEach((regularisation) -> {
			if (needsInsert(regularisation, regularisations)) {
				regularisationRepository.insert(regularisation, employee.getId());
			} else if (needsUpdate(regularisation, regularisations)) {
				regularisation.setTenantId(employee.getTenantId());
				regularisationRepository.update(regularisation);
			}
     });
		deleteRegularisationsInDBThatAreNotInInput(employee.getRegularisations(), regularisations,  employee.getId(),employee.getTenantId());
	}

	private void deleteRegularisationsInDBThatAreNotInInput(List<Regularisation> inputRegularisations,
			List<Regularisation> RegularisationsFromDb, Long employeeId, String tenantId) {
		
		List<Long> regularisationsIdsToDelete = getListOfRegulationIdsToDelete(inputRegularisations, RegularisationsFromDb);
		if (!regularisationsIdsToDelete.isEmpty())
			regularisationRepository.delete(regularisationsIdsToDelete, employeeId, tenantId);
	}

	private List<Long> getListOfRegulationIdsToDelete(List<Regularisation> inputRegularisations,
			List<Regularisation> regularisationsFromDb) {
		List<Long> regularisationsIdsToDelete = new ArrayList<>();
		for (Regularisation regularisationInDb : regularisationsFromDb) {
			boolean found = false;
			for (Regularisation inputRegularisation : inputRegularisations)
				if (inputRegularisation.getId().equals(regularisationInDb.getId())) {
					found = true;
					break;
				}
			if (!found) regularisationsIdsToDelete.add(regularisationInDb.getId());
		}
		return regularisationsIdsToDelete;
	}

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
