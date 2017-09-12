package org.egov.eis.web.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.eis.model.Assignment;
import org.egov.eis.model.Employee;
import org.egov.eis.repository.EmployeeRepository;
import org.egov.eis.repository.NonVacantPositionsRepository;
import org.egov.eis.service.HRMastersService;
import org.egov.eis.utils.FileUtils;
import org.egov.eis.web.contract.EmployeeRequest;
import org.egov.eis.web.contract.EmployeeResponse;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
	private HRMastersService hrMastersService;

	@Mock
	private NonVacantPositionsRepository nonVacantPositionsRepository;

	@InjectMocks
	private DataIntegrityValidatorForCreateEmployee dataIntegrityValidatorForCreate;

	@Before
	public void setUp() throws Exception {
		when(nonVacantPositionsRepository.checkIfPositionIsOccupied(any(Assignment.class), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString())).thenReturn(false);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * This scenario has no documents set in input
	 */
	@Ignore
	@Test
	public void testValidateEmployeeAndCheckForEmployeeUniqueFields() {
		RequestInfo requestInfo = new RequestInfo();
		// gpf, passport and code are valid and hence hasErrors returns false
		assertFalse(validateEmployee(true, true, "employees1.json").hasErrors());
		// gpfNo is invalid and hence hasErrors returns true
		assertTrue(validateEmployee(false, false, "employees1.json").hasErrors());
		// passportNo is invalid
		assertTrue(validateEmployee(true, false, "employees1.json").hasErrors());
	}

	private Errors validateEmployee(boolean validGpfNo, boolean validPassportNo, String employeeFileName) {
		Map<String, List<String>> hrConfigurations = new HashMap<String, List<String>>() {{
			put("Autogenerate_employeecode", new ArrayList<String>() {{
				add("Y");
			}});
		}};

		when(hrMastersService.getHRConfigurations(anyString(), any(RequestInfoWrapper.class))).thenReturn(hrConfigurations);
		when(employeeRepository.duplicateExists("egeis_employee", "gpfNo", "12393243", "1"))
        .thenReturn(!validGpfNo);
		when(employeeRepository.duplicateExists("egeis_employee", "passportNo", "ASD234234", "1"))
        .thenReturn(!validPassportNo);

		// get employee from different files based on test case
		Employee employee = null;
		try {
			employee = getEmployee(employeeFileName);
		} catch(Exception e) {
			e.printStackTrace();
			fail();
		}

		EmployeeResponse employeeResponse = new EmployeeResponse();
		RequestInfo requestInfo = new RequestInfo();
		EmployeeRequest employeeRequest = new EmployeeRequest(requestInfo, employee);
		BindException errors = new BindException(employeeResponse, "employeeResponse");
		ValidationUtils.invokeValidator(dataIntegrityValidatorForCreate, employeeRequest, errors);
		
		return errors;
	}
	
	private Employee getEmployee(String fileName) throws IOException {
		String empJson = new FileUtils().getFileContents(
				"org/egov/eis/web/validator/DataIntegrityValidatorForCreate/" + fileName);
		return new ObjectMapper().readValue(empJson, Employee.class);
	}

}
