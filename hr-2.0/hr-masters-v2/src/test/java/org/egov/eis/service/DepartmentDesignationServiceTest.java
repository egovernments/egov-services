package org.egov.eis.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.egov.eis.model.DepartmentDesignation;
import org.egov.eis.repository.DepartmentDesignationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DepartmentDesignationServiceTest {

	@Mock
	private DepartmentDesignationRepository departmentDesignationRepository;
	
	@Mock
	private DepartmentDesignation departmentDesignation;
	
	@InjectMocks
	private DepartmentDesignationService departmentDesignationService;
	
	@Test
	public void test_getDepartmentDesignation() {
		DepartmentDesignation departmentDesignation = new DepartmentDesignation().builder().departmentCode("code").id(1L).tenantId("default").build();
		when(departmentDesignationRepository.findForId(any(Long.class))).thenReturn(departmentDesignation);
		DepartmentDesignation result = departmentDesignationService.getDepartmentDesignation(1L);
		assertThat(result).isEqualTo(departmentDesignation);
	}
	
	@Test
	public void test_getByDepartmentAndDesignation(){
		DepartmentDesignation departmentDesignation = new DepartmentDesignation().builder().departmentCode("code").id(1L).tenantId("default").build();
        when(departmentDesignationRepository.findByDepartmentAndDesignation(any(String.class), any(String.class), any(String.class))).thenReturn(departmentDesignation);
        DepartmentDesignation result = departmentDesignationService.getByDepartmentAndDesignation("dept", "desg", "default");
		assertThat(result).isEqualTo(departmentDesignation);
	}
	
	@Test
	public void test_create() {
		DepartmentDesignation departmentDesignation = new DepartmentDesignation().builder().departmentCode("code").id(1L).tenantId("default").build();
		doNothing().when(departmentDesignationRepository).create(any(DepartmentDesignation.class));
		departmentDesignationService.create(departmentDesignation);
        verify(departmentDesignationRepository).create(any(DepartmentDesignation.class));
	}
}
