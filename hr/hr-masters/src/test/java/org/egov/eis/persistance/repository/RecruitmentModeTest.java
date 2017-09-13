package org.egov.eis.persistance.repository;

import org.egov.eis.TestConfiguration;
import org.egov.eis.model.RecruitmentMode;
import org.egov.eis.repository.RecruitmentModeRepository;
import org.egov.eis.repository.RecruitmentModeRepository;
import org.egov.eis.web.contract.RecruitmentModeGetRequest;
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

public class RecruitmentModeTest {

    @Autowired
    private RecruitmentModeRepository recruitmentModeRepository;

    @Test
    @Sql(scripts = {"/sql/clearrecruitmentmode.sql", "/sql/insertrecruitmentmode.sql"})
    public void test_should_find_all_recruitmenttype() {
        RecruitmentModeGetRequest recruitmentModeGetRequest = new RecruitmentModeGetRequest();
        recruitmentModeGetRequest.builder().name("Sports").tenantId("default").build();
        List<RecruitmentMode> recruitmentModes = recruitmentModeRepository.findForCriteria(recruitmentModeGetRequest);
        assertEquals("Sports",recruitmentModes.get(0).getName());
    }

    @Test
    @Sql(scripts = {"/sql/clearrecruitmentmode.sql", "/sql/insertrecruitmentmode.sql"})
    public void test_should_find_all_recruitmenttype_not_exist() {
        RecruitmentModeGetRequest recruitmentModeGetRequest = new RecruitmentModeGetRequest();
        recruitmentModeGetRequest.setName("Sports1");
        recruitmentModeGetRequest.setTenantId("default");
        List<RecruitmentMode> recruitmentModes = recruitmentModeRepository.findForCriteria(recruitmentModeGetRequest);
        assertEquals(Boolean.TRUE,recruitmentModes.isEmpty());
    }
}
