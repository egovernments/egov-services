package org.egov.eis.web.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.eis.model.Assignment;
import org.egov.eis.model.Employee;
import org.egov.eis.repository.EmployeeRepository;
import org.egov.eis.repository.NonVacantPositionsRepository;
import org.egov.eis.utils.FileUtils;
import org.egov.eis.web.contract.EmployeeResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataIntegrityValidatorForCreateTest {

	@Mock
	private EmployeeRepository employeeRepository;

	@Mock
	private NonVacantPositionsRepository nonVacantPositionsRepository;

	@InjectMocks
	private DataIntegrityValidatorForCreate dataIntegrityValidatorForCreate;

	@Before
	public void setUp() throws Exception {
		when(nonVacantPositionsRepository.checkIfPositionIsOccupied(Mockito.any(Assignment.class), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString())).thenReturn(false);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * This scenario has no documents set in input
	 */
	@Test
	public void testValidateEmployeeAndCheckForEmployeeUniqueFields() {
		// gpf, passport and code are valid and hence hasErrors returns false
		assertFalse(validateEmployee(true, true, true, "employees1.json").hasErrors());
		// gpfNo is invalid and hence hasErrors returns true
		assertTrue(validateEmployee(false, false, true, "employees1.json").hasErrors());
		// passportNo is invalid
		assertTrue(validateEmployee(true, false, true, "employees1.json").hasErrors());
		// code is invalid
		assertTrue(validateEmployee(true, true, false, "employees1.json").hasErrors());
	}

	private Errors validateEmployee(boolean validGpfNo, boolean validPassportNo, boolean validCode,
			String employeeFileName) {
		when(employeeRepository.duplicateExists("egeis_employee", "gpfNo", "12393243", "1"))
        .thenReturn(!validGpfNo);
		when(employeeRepository.duplicateExists("egeis_employee", "passportNo", "ASD234234", "1"))
        .thenReturn(!validPassportNo);
		when(employeeRepository.duplicateExists("egeis_employee", "code", "00100", "1"))
        .thenReturn(!validCode);
		
		// get employee from different files based on test case
		Employee employee = null;
		try {
			employee = getEmployee(employeeFileName);
		} catch(Exception e) {
			e.printStackTrace();
			fail();
		}
		
		EmployeeResponse employeeResponse = new EmployeeResponse();
		BindException errors = new BindException(employeeResponse, "employeeResponse");
		ValidationUtils.invokeValidator(dataIntegrityValidatorForCreate, employee, errors);
		
		return errors;
		
	}
	
	private Employee getEmployee(String fileName) throws IOException {
		String empJson = new FileUtils().getFileContents(
				"org/egov/eis/web/validator/DataIntegrityValidatorForCreate/" + fileName);
		return new ObjectMapper().readValue(empJson, Employee.class);
	}

}
