package org.egov.hrms.service;

import org.egov.hrms.model.Employee;
import org.egov.hrms.repository.EmployeeLanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class EmployeeLanguageService {

	@Autowired
	private EmployeeLanguageRepository employeeLanguageRepository;

	public void update(Employee employee) {
		if (isEmpty(employee.getLanguagesKnown()))
			return;
		List<Long> languagesFromDb = employeeLanguageRepository.findByEmployeeId(employee.getId(),
				employee.getTenantId());
		employee.getLanguagesKnown().forEach((language) -> {
			if (needsInsert(language, languagesFromDb))
				employeeLanguageRepository.insert(language, employee.getId(), employee.getTenantId());
		});
		deleteLanguagesInDbThatAreNotInInput(employee.getLanguagesKnown(), languagesFromDb, employee.getId(),
				employee.getTenantId());

	}

	private void deleteLanguagesInDbThatAreNotInInput(List<Long> languages, List<Long> languagesFromDb,
			Long employeeId, String tenantId) {
		List<Long> languagesIdsToDelete = getListOfLanguagesIdsToDelete(languages, languagesFromDb);
		if (!languagesIdsToDelete.isEmpty())
			employeeLanguageRepository.delete(languagesIdsToDelete, employeeId, tenantId);
	}

	private List<Long> getListOfLanguagesIdsToDelete(List<Long> languages, List<Long> languagesFromDb) {
		List<Long> languagesIdsToDelete = new ArrayList<>();
		for (Long languageInDb : languagesFromDb) {
			boolean found = false;
			for (Long language : languages)
				if (language.equals(languageInDb)) {
					found = true;
					break;
				}
			if (!found)
				languagesIdsToDelete.add(languageInDb);
		}
		return languagesIdsToDelete;
	}

	private boolean needsInsert(Long language, List<Long> languagesFromDb) {
		for (Long languageInDb : languagesFromDb)
			if (language.equals(languageInDb))
				return false;
		return true;
	}

}
