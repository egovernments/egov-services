package org.egov.eis.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;

import org.egov.eis.model.DepartmentalTest;
import org.egov.eis.model.Employee;
import org.egov.eis.repository.DepartmentalTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DepartmentalTestService {

	@Autowired
	private DepartmentalTestRepository departmentalTestRepository;
	
	public void update(Employee employee) {
		if(isEmpty(employee.getTest()))
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
		if (!testsIdsToDelete.isEmpty())
			departmentalTestRepository.delete(testsIdsToDelete, employeeId, tenantId);
	}

	private List<Long> getListOfTestsIdsToDelete(List<DepartmentalTest> inputTests,
			List<DepartmentalTest> testsFromDb) {
		List<Long> testsIdsToDelete = new ArrayList<>();
		for (DepartmentalTest testInDb : testsFromDb) {
			boolean found = false;
			for (DepartmentalTest inputTest : inputTests)
				if (inputTest.getId().equals(testInDb.getId())) {
					found = true;
					break;
				}
			if (!found)
				testsIdsToDelete.add(testInDb.getId());
		}
		return testsIdsToDelete;
	}

	private boolean needsUpdate(DepartmentalTest test, List<DepartmentalTest> tests) {
		for (DepartmentalTest oldTest : tests) 
			if (test.equals(oldTest)) return false;
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
