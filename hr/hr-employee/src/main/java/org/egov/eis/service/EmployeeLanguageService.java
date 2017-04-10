package org.egov.eis.service;

import java.util.List;

import org.egov.eis.model.Employee;
import org.egov.eis.repository.EmployeeLanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeLanguageService {

	@Autowired
	private EmployeeLanguageRepository employeeLanguageRepository;
	
	public void update(Employee employee) {
		
		List<Long> languagesFromDb = employeeLanguageRepository.findByEmployeeId(employee.getId(), employee.getTenantId());
		if(employee.getLanguagesKnown() != null){
			updateLanguagesKnown(employee.getLanguagesKnown(), languagesFromDb, employee.getId(), employee.getTenantId());
		}
	}

	private void updateLanguagesKnown(List<Long> inputlanguages, List<Long> languagesFromDb, Long employeeId, String tenantId) {
		
			for (Long inputlanguage : inputlanguages) {
				if (!LanguagesInDB(inputlanguage, languagesFromDb)) {
					employeeLanguageRepository.save(inputlanguage, employeeId, tenantId);
				}
			}
			// delete all languages from db that are not in input list
			for (Long languageInDb : languagesFromDb) {
				if (!inputlanguages.contains(languageInDb)) {
					employeeLanguageRepository.delete(employeeId, languageInDb, tenantId);
			}
		}
	}

	private boolean LanguagesInDB(Long inputlanguage, List<Long> languagesFromDb) {
		boolean foundLanguageInDb = false;
		for (Long languageInDb : languagesFromDb) {
			if (languageInDb.equals(inputlanguage)) {
				foundLanguageInDb = true;
					break;
			}
		}
		return foundLanguageInDb;
	}

}
