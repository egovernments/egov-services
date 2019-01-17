package org.egov.hrms.service;

import org.egov.hrms.model.Employee;
import org.egov.hrms.model.Probation;
import org.egov.hrms.model.enums.EntityType;
import org.egov.hrms.repository.EmployeeDocumentsRepository;
import org.egov.hrms.repository.ProbationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

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
