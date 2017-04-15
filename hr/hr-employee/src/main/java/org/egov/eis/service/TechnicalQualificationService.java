package org.egov.eis.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;

import org.egov.eis.model.Employee;
import org.egov.eis.model.TechnicalQualification;
import org.egov.eis.repository.TechnicalQualificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TechnicalQualificationService {
	
	@Autowired
	private TechnicalQualificationRepository technicalQualificationRepository;

	public void update(Employee employee) {
		if(isEmpty(employee.getTechnicals()))
			return;
		List<TechnicalQualification> technicals = technicalQualificationRepository.findByEmployeeId(employee.getId(), employee.getTenantId());
		employee.getTechnicals().forEach((technical) -> {
			if (needsInsert(technical, technicals)) {
				technicalQualificationRepository.insert(technical, employee.getId());
			} else if (needsUpdate(technical, technicals)) {
				technical.setTenantId(employee.getTenantId());
				technicalQualificationRepository.update(technical);
			}
			
     });
		deleteTechnicalsInDBThatAreNotInInput(employee.getTechnicals(), technicals,  employee.getId(),employee.getTenantId());
	}

	private void deleteTechnicalsInDBThatAreNotInInput(List<TechnicalQualification> inputTechnicals,
			List<TechnicalQualification> technicalsFromDb, Long employeeId, String tenantId) {
		
		List<Long> technicalsIdsToDelete = getListOfTechnicalIdsToDelete(inputTechnicals, technicalsFromDb);
		if (!technicalsIdsToDelete.isEmpty())
			technicalQualificationRepository.delete(technicalsIdsToDelete, employeeId, tenantId);
	}

	private List<Long> getListOfTechnicalIdsToDelete(List<TechnicalQualification> inputTechnicals,
			List<TechnicalQualification> technicalsFromDb) {
		List<Long> technicalsIdsToDelete = new ArrayList<>();
		for (TechnicalQualification technicalInDb : technicalsFromDb) {
			boolean found = false;
			for (TechnicalQualification inputTechnical : inputTechnicals)
				if (inputTechnical.getId().equals(technicalInDb.getId())) {
					found = true;
					break;
				}
			if (!found) technicalsIdsToDelete.add(technicalInDb.getId());
		}
		return technicalsIdsToDelete;
	}

	private boolean needsUpdate(TechnicalQualification technical, List<TechnicalQualification> technicals) {
		for (TechnicalQualification oldTechnical : technicals) 
			if (technical.equals(oldTechnical)) return false;
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
