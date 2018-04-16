package org.egov.eis.persistance.repository;

import org.egov.eis.TestConfiguration;
import org.egov.eis.model.RecruitmentQuota;
import org.egov.eis.repository.RecruitmentQuotaRepository;
import org.egov.eis.web.contract.RecruitmentQuotaGetRequest;
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

public class RecruitmentQuotaTest {

    @Autowired
    private RecruitmentQuotaRepository recruitmentQuotaRepository;

    @Test
    @Sql(scripts = {"/sql/clearrecruitmentquota.sql", "/sql/insertrecruitmentquota.sql"})
    public void test_should_find_all_recruitmentquota() {
        RecruitmentQuotaGetRequest recruitmentQuotaGetRequest = new RecruitmentQuotaGetRequest();
        recruitmentQuotaGetRequest.builder().name("Sports").tenantId("default").build();
        List<RecruitmentQuota> recruitmentQuotas = recruitmentQuotaRepository.findForCriteria(recruitmentQuotaGetRequest);
        assertEquals("Sports",recruitmentQuotas.get(0).getName());
    }

    @Test
    @Sql(scripts = {"/sql/clearrecruitmentquota.sql", "/sql/insertrecruitmentquota.sql"})
    public void test_should_find_all_recruitmentquota_not_exist() {
        RecruitmentQuotaGetRequest recruitmentQuotaGetRequest = new RecruitmentQuotaGetRequest();
        recruitmentQuotaGetRequest.setName("Sports1");
        recruitmentQuotaGetRequest.setTenantId("default");
        List<RecruitmentQuota> recruitmentQuotas = recruitmentQuotaRepository.findForCriteria(recruitmentQuotaGetRequest);
        assertEquals(Boolean.TRUE,recruitmentQuotas.isEmpty());
    }
}
