package org.egov.eis.web.validator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

import org.egov.eis.model.Employee;
import org.egov.eis.model.EmployeeDocument;
import org.egov.eis.model.enums.EntityType;
import org.egov.eis.repository.EmployeeDocumentsRepository;
import org.egov.eis.repository.EmployeeRepository;
import org.egov.eis.utils.FileUtils;
import org.egov.eis.web.contract.EmployeeResponse;
import org.egov.eis.web.validator.DataIntegrityValidatorForCreate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class DataIntegrityValidatorForCreateTest {
	
	@Mock
	private EmployeeRepository employeeRepository;
	
	@Mock
	private EmployeeDocumentsRepository employeeDocumentsRepository;
	
	@InjectMocks
	private DataIntegrityValidatorForCreate dataIntegrityValidatorForCreate;

	@Before
	public void setUp() throws Exception {
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
		assertTrue(validateEmployee(false, true, true, "employees1.json").hasErrors());
		// passportNo is invalid
		assertTrue(validateEmployee(true, false, true, "employees1.json").hasErrors());
		// code is invalid
		assertTrue(validateEmployee(true, true, false, "employees1.json").hasErrors());
	}
	
	@Test
	public void checkForErrorsWhenDocumentsWithDuplicatesWithinInput() {
		// documents within input has duplicates(repeated) and hence invalid
		assertTrue(validateEmployee(true, true, true, "employeesWithDuplicateDocumentsWithin.json").hasErrors());
	}
	
	@Test
	public void checkForErrorsWhenDocumentsWithDuplicatesWithinInputAcrossEntities() {
		// documents within input has duplicates(repeated) and hence invalid
		assertTrue(validateEmployee(true, true, true, "employeesWithDuplicateDocumentsWithin2.json").hasErrors());
	}
	
	@Test
	public void checkForErrorsWhenDocumentsWithDuplicatesInDb() {
		List<EmployeeDocument> documentsFromDb = new ArrayList<>();
		EmployeeDocument ed1 = EmployeeDocument.builder().id(100L).document("three")
				.referenceType(EntityType.EMPLOYEE_HEADER.getValue()).build();
		documentsFromDb.add(ed1);
		when(employeeDocumentsRepository.findByDocuments(any(), anyString()))
		.thenReturn(documentsFromDb);
		
		// documentsin db has duplicates and hance last argument is false
		assertTrue(validateEmployee(true, true, true, "employees1.json").hasErrors());
	}
	
	@Test
	public void checkForErrorsWhenDocumentsWithDuplicatesInEducationInDb() {
		List<EmployeeDocument> documentsFromDb = new ArrayList<>();
		EmployeeDocument ed1 = EmployeeDocument.builder().id(100L).document("three")
				.referenceType(EntityType.EDUCATION.getValue()).build();
		documentsFromDb.add(ed1);
		when(employeeDocumentsRepository.findByDocuments(any(), anyString()))
		.thenReturn(documentsFromDb);
		
		// documentsin db has duplicates and hance last argument is false
		assertTrue(validateEmployee(true, true, true, "employees1.json").hasErrors());
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
