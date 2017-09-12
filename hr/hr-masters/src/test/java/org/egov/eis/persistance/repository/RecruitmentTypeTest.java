package org.egov.eis.persistance.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.eis.TestConfiguration;
import org.egov.eis.model.DepartmentDesignation;
import org.egov.eis.model.Position;
import org.egov.eis.model.RecruitmentType;
import org.egov.eis.repository.PositionRepository;
import org.egov.eis.repository.RecruitmentTypeRepository;
import org.egov.eis.web.contract.PositionGetRequest;
import org.egov.eis.web.contract.PositionRequest;
import org.egov.eis.web.contract.RecruitmentTypeGetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@Import(TestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)

public class RecruitmentTypeTest {

    @Autowired
    private RecruitmentTypeRepository recruitmentTypeRepository;

    @Test
    @Sql(scripts = {"/sql/clearrecruitmenttype.sql", "/sql/insertrecruitmenttype.sql"})
    public void test_should_find_all_recruitmenttype() {
        RecruitmentTypeGetRequest recruitmentTypeGetRequest = new RecruitmentTypeGetRequest();
        recruitmentTypeGetRequest.builder().name("Direct").tenantId("default").build();
        List<RecruitmentType> recruitmentTypes = recruitmentTypeRepository.findForCriteria(recruitmentTypeGetRequest);
        assertEquals("Direct",recruitmentTypes.get(0).getName());
    }

    @Test
    @Sql(scripts = {"/sql/clearrecruitmenttype.sql", "/sql/insertrecruitmenttype.sql"})
    public void test_should_find_all_recruitmenttype_not_exist() {
        RecruitmentTypeGetRequest recruitmentTypeGetRequest = new RecruitmentTypeGetRequest();
        recruitmentTypeGetRequest.setName("Direct1");
        recruitmentTypeGetRequest.setTenantId("default");
        List<RecruitmentType> recruitmentTypes = recruitmentTypeRepository.findForCriteria(recruitmentTypeGetRequest);
        assertEquals(Boolean.TRUE,recruitmentTypes.isEmpty());
    }
}
