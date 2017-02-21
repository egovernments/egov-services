package org.egov.eis.domain.service;

import org.egov.eis.persistence.entity.Department;
import org.egov.eis.persistence.repository.DepartmentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    @Test
    public void test_should_return_department_when_department_code_is_specified() {
        final Department expectedDepartment = new Department();
        expectedDepartment.setId(1L);
        when(departmentRepository.findByCode("departmentCode"))
                .thenReturn(expectedDepartment);

        final List<Department> actualDepartments = departmentService.find("departmentCode");

        assertEquals(1, actualDepartments.size());
        assertEquals(expectedDepartment, actualDepartments.get(0));

    }

    @Test
    public void test_should_return__all_departments_when_department_code_is_not_specified() {
        when(departmentRepository.findAll())
                .thenReturn(Arrays.asList(new Department(), new Department()));

        final List<Department> actualDepartments = departmentService.find(null);

        assertEquals(2, actualDepartments.size());
    }

}