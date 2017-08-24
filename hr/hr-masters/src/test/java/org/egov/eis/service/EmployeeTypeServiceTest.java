package org.egov.eis.service;

import org.egov.eis.model.EmployeeType;
import org.egov.eis.repository.EmployeeTypeRepository;
import org.egov.eis.web.contract.EmployeeTypeGetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeTypeServiceTest {

	@Mock
	private EmployeeTypeRepository employeeTypeRepository;
	
	@InjectMocks
	private EmployeeTypeService employeeTypeService;
	
	@Test
	public void test_getEmployeeTypes() {
		
		List<EmployeeType> employeeTypes = new ArrayList<>();
		when(employeeTypeRepository.findForCriteria(any(EmployeeTypeGetRequest.class))).thenReturn(employeeTypes);
	    List<EmployeeType> result = employeeTypeService.getEmployeeTypes(any(EmployeeTypeGetRequest.class));
	    assertThat(result).isEqualTo(employeeTypes);
	}
}
