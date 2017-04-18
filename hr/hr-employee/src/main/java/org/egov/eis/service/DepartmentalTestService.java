package org.egov.eis.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;

import org.egov.eis.model.DepartmentalTest;
import org.egov.eis.model.Employee;
import org.egov.eis.model.enums.EntityType;
import org.egov.eis.repository.DepartmentalTestRepository;
import org.egov.eis.repository.EmployeeDocumentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DepartmentalTestService {

	@Autowired
	private DepartmentalTestRepository departmentalTestRepository;

	@Autowired
	private EmployeeDocumentsRepository employeeDocumentsRepository;

	public void update(Employee employee) {
		if (isEmpty(employee.getTest()))
			return;
		List<DepartmentalTest> tests = departmentalTestRepository.findByEmployeeId(employee.getId(),
				employee.getTenantId());

		employee.getTest().forEach((test) -> {
			if (needsInsert(test, tests)) {
				departmentalTestRepository.insert(test, employee.getId());
			} else if (needsUpdate(test, tests)) {
				test.setTenantId(employee.getTenantId());
				departmentalTestRepository.update(test);
			}
		});
		deleteTestsInDBThatAreNotInInput(employee.getTest(), tests, employee.getId(), employee.getTenantId());
	}

	private void deleteTestsInDBThatAreNotInInput(List<DepartmentalTest> inputTests, List<DepartmentalTest> testsFromDb,
			Long employeeId, String tenantId) {
		List<Long> testsIdsToDelete = getListOfTestsIdsToDelete(inputTests, testsFromDb);
		if (!testsIdsToDelete.isEmpty()) {
			employeeDocumentsRepository.deleteForReferenceIds(employeeId, EntityType.TEST, testsIdsToDelete, tenantId);
			departmentalTestRepository.delete(testsIdsToDelete, employeeId, tenantId);
		}
	}

	private List<Long> getListOfTestsIdsToDelete(List<DepartmentalTest> inputTests,
			List<DepartmentalTest> testsFromDb) {
		List<Long> testsIdsToDelete = new ArrayList<>();
		for (DepartmentalTest testInDb : testsFromDb) {
			boolean found = false;
			if (!isEmpty(inputTests)) {
				// if empty, found remains false and the record becomes eligible for deletion.
				for (DepartmentalTest inputTest : inputTests)
					if (inputTest.getId().equals(testInDb.getId())) {
						found = true;
						break;
					}
			}
			if (!found)
				testsIdsToDelete.add(testInDb.getId());
		}
		return testsIdsToDelete;
	}

	/**
	 * Note: needsUpdate checks if any field has changed by comparing with
	 * contents from db. If yes, then only do an update.
	 */
	private boolean needsUpdate(DepartmentalTest test, List<DepartmentalTest> tests) {
		for (DepartmentalTest oldTest : tests)
			if (test.equals(oldTest))
				return false;
		return true;
	}

	private boolean needsInsert(DepartmentalTest test, List<DepartmentalTest> tests) {
		for (DepartmentalTest oldTest : tests) {
			if (test.getId().equals(oldTest.getId()))
				return false;
		}
		return true;
	}

}
