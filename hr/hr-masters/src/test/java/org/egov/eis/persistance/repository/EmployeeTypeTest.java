package org.egov.eis.persistance.repository;

import org.egov.eis.TestConfiguration;
import org.egov.eis.model.EmployeeType;
import org.egov.eis.repository.EmployeeTypeRepository;
import org.egov.eis.web.contract.EmployeeTypeGetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@Import(TestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)

public class EmployeeTypeTest {

    @Autowired
    private EmployeeTypeRepository employeeTypeRepository;

    @Test
    @Sql(scripts = {"/sql/clearemployeetype.sql", "/sql/insertemployeetype.sql"})
    public void test_should_find_all_recruitmentquota() {
        EmployeeTypeGetRequest employeeTypeGetRequest = new EmployeeTypeGetRequest();
        employeeTypeGetRequest.builder().name("Temporary").tenantId("default").build();
        List<EmployeeType> employeeTypes = employeeTypeRepository.findForCriteria(employeeTypeGetRequest);
        assertEquals("Temporary",employeeTypes.get(0).getName());
    }

    @Test
    @Sql(scripts = {"/sql/clearemployeetype.sql", "/sql/insertemployeetype.sql"})
    public void test_should_find_all_recruitmentquota_not_exist() {
        EmployeeTypeGetRequest employeeTypeGetRequest = new EmployeeTypeGetRequest();
        employeeTypeGetRequest.setName("Temporary1");
        employeeTypeGetRequest.setTenantId("default");
        List<EmployeeType> employeeTypes = employeeTypeRepository.findForCriteria(employeeTypeGetRequest);
        assertEquals(Boolean.TRUE,employeeTypes.isEmpty());
    }
}
