package org.egov.eis.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.eis.model.EducationalQualification;
import org.egov.eis.model.Employee;
import org.egov.eis.repository.EducationalQualificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EducationalQualificationService {

	@Autowired
	private EducationalQualificationRepository educationalQualificationRepository;

	public void update(Employee employee) {
		List<EducationalQualification> educations = educationalQualificationRepository.findByEmployeeId(employee.getId(), employee.getTenantId());
		employee.getEducation().forEach((education) -> {
			if (needsInsert(education, educations)) {
				educationalQualificationRepository.insert(education, employee.getId());
			} else if (needsUpdate(education, educations)) {
				education.setTenantId(employee.getTenantId());
				educationalQualificationRepository.update(education);
			}		
	 });
	deleteEducationsInDBThatAreNotInInput(employee.getEducation(),educations,  employee.getId(),employee.getTenantId());
	}
	
	private void deleteEducationsInDBThatAreNotInInput(List<EducationalQualification> inputEducations, List<EducationalQualification> educationsFromDb, Long employeeId, String tenantId) {
		List<Long> educationsIdsToDelete = getListOfEducationIdsToDelete(inputEducations, educationsFromDb);
		if (!educationsIdsToDelete.isEmpty())
			educationalQualificationRepository.delete(educationsIdsToDelete, employeeId, tenantId);
	}

	private List<Long> getListOfEducationIdsToDelete(List<EducationalQualification> inputEducations,
			List<EducationalQualification> educationsFromDb) {
		List<Long> educationsIdsToDelete = new ArrayList<>();
		for (EducationalQualification educationInDb : educationsFromDb) {
			boolean found = false;
			for (EducationalQualification inputEducation : inputEducations)
				if (inputEducation.getId().equals(educationInDb.getId())) {
					found = true;
					break;
				}
			if (!found) educationsIdsToDelete.add(educationInDb.getId());
		}
		return educationsIdsToDelete;
	}

	private boolean needsUpdate(EducationalQualification education, List<EducationalQualification> educations) {
		for (EducationalQualification oldEducation : educations)
			if (education.equals(oldEducation)) return false;
		return true;
	}

	private boolean needsInsert(EducationalQualification education, List<EducationalQualification> educations) {
		for (EducationalQualification oldEducation : educations) 
			if (education.getId().equals(oldEducation.getId())) return false;
		return true;
	}
}
