package org.egov.eis.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.egov.eis.persistence.entity.Department;
import org.egov.eis.persistence.repository.DepartmentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DepartmentServiceTest {

	@Mock
	private DepartmentRepository departmentRepository;

	@InjectMocks
	private DepartmentService departmentService;

	@Test
	public void testShouldReturnDepartmentWhenDepartmentCodeIsSpecified() {
		final Department expectedDepartment = new Department();
		expectedDepartment.setId(1L);
		when(departmentRepository.findByCode("departmentCode")).thenReturn(expectedDepartment);

		final List<Department> actualDepartments = departmentService.find("departmentCode", null);

		assertEquals(1, actualDepartments.size());
		assertEquals(expectedDepartment, actualDepartments.get(0));

	}

	@Test
	public void testShouldReturnDepartmentWhenDepartmentIdIsSpecified() {
		final Department expectedDepartment = new Department();
		expectedDepartment.setId(1L);
		when(departmentRepository.findOne(1L)).thenReturn(expectedDepartment);

		final List<Department> actualDepartments = departmentService.find(null, 1L);

		assertEquals(1, actualDepartments.size());
		assertEquals(expectedDepartment, actualDepartments.get(0));

	}

	@Test
	public void testShouldReturnAllDepartmentsWhenDepartmentCodeAndIdIsNotSpecified() {
		when(departmentRepository.findAll()).thenReturn(Arrays.asList(new Department(), new Department()));

		final List<Department> actualDepartments = departmentService.find(null, null);

		assertEquals(2, actualDepartments.size());
	}

}