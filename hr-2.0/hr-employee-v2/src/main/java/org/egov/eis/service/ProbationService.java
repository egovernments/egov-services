package org.egov.eis.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;

import org.egov.eis.model.Employee;
import org.egov.eis.model.Probation;
import org.egov.eis.model.enums.EntityType;
import org.egov.eis.repository.EmployeeDocumentsRepository;
import org.egov.eis.repository.ProbationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProbationService {

	@Autowired
	private ProbationRepository probationRepository;

	@Autowired
	private EmployeeDocumentsRepository employeeDocumentsRepository;

	public void update(Employee employee) {
		if (isEmpty(employee.getProbation()))
			return;
		List<Probation> probations = probationRepository.findByEmployeeId(employee.getId(), employee.getTenantId());
		employee.getProbation().forEach((probation) -> {
			if (needsInsert(probation, probations)) {
				probationRepository.insert(probation, employee.getId());
			} else if (needsUpdate(probation, probations)) {
				probation.setTenantId(employee.getTenantId());
				probationRepository.update(probation);
			}
		});
		deleteProbationsInDBThatAreNotInInput(employee.getProbation(), probations, employee.getId(),
				employee.getTenantId());
	}

	private void deleteProbationsInDBThatAreNotInInput(List<Probation> inputProbations,
			List<Probation> probationsFromDb, Long employeeId, String tenantId) {

		List<Long> probationsIdsToDelete = getListOfProbationIdsToDelete(inputProbations, probationsFromDb);
		if (!probationsIdsToDelete.isEmpty()) {
			employeeDocumentsRepository.deleteForReferenceIds(employeeId, EntityType.PROBATION, probationsIdsToDelete, tenantId);
			probationRepository.delete(probationsIdsToDelete, employeeId, tenantId);
		}
	}

	private List<Long> getListOfProbationIdsToDelete(List<Probation> inputProbations,
			List<Probation> probationsFromDb) {
		List<Long> probationsIdsToDelete = new ArrayList<>();
		for (Probation probationInDb : probationsFromDb) {
			boolean found = false;
			for (Probation inputProbation : inputProbations)
				if (inputProbation.getId().equals(probationInDb.getId())) {
					found = true;
					break;
				}
			if (!found)
				probationsIdsToDelete.add(probationInDb.getId());
		}
		return probationsIdsToDelete;
	}

	private boolean needsUpdate(Probation probation, List<Probation> probations) {
		for (Probation oldProbation : probations)
			if (probation.equals(oldProbation))
				return false;
		return true;
	}

	private boolean needsInsert(Probation probation, List<Probation> probations) {
		for (Probation oldProbation : probations) {
			if (probation.getId().equals(oldProbation.getId()))
				return false;
		}
		return true;
	}

}
