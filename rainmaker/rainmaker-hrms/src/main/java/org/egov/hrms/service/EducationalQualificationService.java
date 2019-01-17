package org.egov.hrms.service;

import org.egov.hrms.model.EducationalQualification;
import org.egov.hrms.model.Employee;
import org.egov.hrms.model.enums.EntityType;
import org.egov.hrms.repository.EducationalQualificationRepository;
import org.egov.hrms.repository.EmployeeDocumentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class EducationalQualificationService {

	@Autowired
	private EducationalQualificationRepository educationalQualificationRepository;

	@Autowired
	private EmployeeDocumentsRepository employeeDocumentsRepository;

	public void update(Employee employee) {
		if (isEmpty(employee.getEducation()))
			return;
		List<EducationalQualification> educations = educationalQualificationRepository
				.findByEmployeeId(employee.getId(), employee.getTenantId());
		employee.getEducation().forEach((education) -> {
			if (needsInsert(education, educations)) {
				educationalQualificationRepository.insert(education, employee.getId());
			} else if (needsUpdate(education, educations)) {
				education.setTenantId(employee.getTenantId());
				educationalQualificationRepository.update(education);
			}
		});
		deleteEducationsInDBThatAreNotInInput(employee.getEducation(), educations, employee.getId(),
				employee.getTenantId());
	}

	private void deleteEducationsInDBThatAreNotInInput(List<EducationalQualification> inputEducations,
			List<EducationalQualification> educationsFromDb, Long employeeId, String tenantId) {
		List<Long> educationsIdsToDelete = getListOfEducationIdsToDelete(inputEducations, educationsFromDb);
		if (!educationsIdsToDelete.isEmpty()) {
			employeeDocumentsRepository.deleteForReferenceIds(employeeId, EntityType.EDUCATION, educationsIdsToDelete, tenantId);
			educationalQualificationRepository.delete(educationsIdsToDelete, employeeId, tenantId);
		}
	}

	private List<Long> getListOfEducationIdsToDelete(List<EducationalQualification> inputEducations,
			List<EducationalQualification> educationsFromDb) {
		List<Long> educationsIdsToDelete = new ArrayList<>();
		for (EducationalQualification educationInDb : educationsFromDb) {
			boolean found = false;
			if (!isEmpty(inputEducations)) {
				// if empty, found remains false and the record becomes eligible for deletion.
				for (EducationalQualification inputEducation : inputEducations)
					if (inputEducation.getId().equals(educationInDb.getId())) {
						found = true;
						break;
					}
			}
			if (!found)
				educationsIdsToDelete.add(educationInDb.getId());
		}
		return educationsIdsToDelete;
	}

	/**
	 * Note: needsUpdate checks if any field has changed by comparing with
	 * contents from db. If yes, then only do an update.
	 */
	private boolean needsUpdate(EducationalQualification education, List<EducationalQualification> educations) {
		for (EducationalQualification oldEducation : educations)
			if (education.equals(oldEducation))
				return false;
		return true;
	}

	private boolean needsInsert(EducationalQualification education, List<EducationalQualification> educations) {
		for (EducationalQualification oldEducation : educations)
			if (education.getId().equals(oldEducation.getId()))
				return false;
		return true;
	}
}
