package org.egov.eis.persistance.repository;

import org.egov.eis.TestConfiguration;
import org.egov.eis.model.Grade;
import org.egov.eis.repository.GradeRepository;
import org.egov.eis.web.contract.GradeGetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@Import(TestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)

public class GradeTest {

    @Autowired
    private GradeRepository gradeRepository;

    @Test
    @Sql(scripts = {"/sql/cleargrade.sql", "/sql/insertgrade.sql"})
    public void test_should_find_all_recruitmenttype() {
        GradeGetRequest gradeGetRequest = new GradeGetRequest();
        gradeGetRequest.builder().name("A").tenantId("default").build();
        List<Grade> grades = gradeRepository.findForCriteria(gradeGetRequest);
        assertEquals("A",grades.get(0).getName());
    }

    @Test
    @Sql(scripts = {"/sql/cleargrade.sql", "/sql/insertgrade.sql"})
    public void test_should_find_all_recruitmenttype_not_exist() {
        GradeGetRequest gradeGetRequest = new GradeGetRequest();
        gradeGetRequest.setName("A1");
        gradeGetRequest.setTenantId("default");
        List<Grade> grades = gradeRepository.findForCriteria(gradeGetRequest);
        assertEquals(Boolean.TRUE,grades.isEmpty());
    }
}
