package org.egov.hrms.service;

import org.egov.hrms.model.Employee;
import org.egov.hrms.model.TechnicalQualification;
import org.egov.hrms.model.enums.EntityType;
import org.egov.hrms.repository.EmployeeDocumentsRepository;
import org.egov.hrms.repository.TechnicalQualificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class TechnicalQualificationService {

	@Autowired
	private TechnicalQualificationRepository technicalQualificationRepository;

	@Autowired
	private EmployeeDocumentsRepository employeeDocumentsRepository;

	public void update(Employee employee) {
		if (isEmpty(employee.getTechnical()))
			return;
		List<TechnicalQualification> technicals = technicalQualificationRepository.findByEmployeeId(employee.getId(),
				employee.getTenantId());
		employee.getTechnical().forEach((technical) -> {
			if (needsInsert(technical, technicals)) {
				technicalQualificationRepository.insert(technical, employee.getId());
			} else if (needsUpdate(technical, technicals)) {
				technical.setTenantId(employee.getTenantId());
				technicalQualificationRepository.update(technical);
			}

		});
		deleteTechnicalsInDBThatAreNotInInput(employee.getTechnical(), technicals, employee.getId(),
				employee.getTenantId());
	}

	private void deleteTechnicalsInDBThatAreNotInInput(List<TechnicalQualification> inputTechnicals,
			List<TechnicalQualification> technicalsFromDb, Long employeeId, String tenantId) {

		List<Long> technicalsIdsToDelete = getListOfTechnicalIdsToDelete(inputTechnicals, technicalsFromDb);
		if (!technicalsIdsToDelete.isEmpty()) {
			employeeDocumentsRepository.deleteForReferenceIds(employeeId, EntityType.TECHNICAL, technicalsIdsToDelete, tenantId);
			technicalQualificationRepository.delete(technicalsIdsToDelete, employeeId, tenantId);
		}
	}

	private List<Long> getListOfTechnicalIdsToDelete(List<TechnicalQualification> inputTechnicals,
			List<TechnicalQualification> technicalsFromDb) {
		List<Long> technicalsIdsToDelete = new ArrayList<>();
		for (TechnicalQualification technicalInDb : technicalsFromDb) {
			boolean found = false;
			if (!isEmpty(inputTechnicals)) {
				// if empty, found remains false and the record becomes eligible for deletion.
				for (TechnicalQualification inputTechnical : inputTechnicals)
					if (inputTechnical.getId().equals(technicalInDb.getId())) {
						found = true;
						break;
					}
			}
			if (!found)
				technicalsIdsToDelete.add(technicalInDb.getId());
		}
		return technicalsIdsToDelete;
	}

	/**
	 * Note: needsUpdate checks if any field has changed by comparing with
	 * contents from db. If yes, then only do an update.
	 */
	private boolean needsUpdate(TechnicalQualification technical, List<TechnicalQualification> technicals) {
		for (TechnicalQualification oldTechnical : technicals)
			if (technical.equals(oldTechnical))
				return false;
		return true;
	}

	private boolean needsInsert(TechnicalQualification technical, List<TechnicalQualification> technicals) {
		for (TechnicalQualification oldTechnical : technicals) {
			if (technical.getId().equals(oldTechnical.getId()))
				return false;
		}
		return true;
	}

}
