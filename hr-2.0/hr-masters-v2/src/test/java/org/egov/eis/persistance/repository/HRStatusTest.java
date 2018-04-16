package org.egov.eis.persistance.repository;

import org.egov.eis.TestConfiguration;
import org.egov.eis.model.HRStatus;
import org.egov.eis.repository.HRStatusRepository;
import org.egov.eis.web.contract.HRStatusGetRequest;
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

public class HRStatusTest {

    @Autowired
    private HRStatusRepository hrstatusRepository;

    @Test
    @Sql(scripts = {"/sql/clearhrstatus.sql", "/sql/inserthrstatus.sql"})
    public void test_should_find_all_recruitmenttype() {
        HRStatusGetRequest hrstatusGetRequest = new HRStatusGetRequest();
        hrstatusGetRequest.builder().objectName("LeaveApplication").tenantId("default").build();
        List<HRStatus> hrstatuss = hrstatusRepository.findForCriteria(hrstatusGetRequest);
        assertEquals("LeaveApplication",hrstatuss.get(0).getObjectName());
    }

    @Test
    @Sql(scripts = {"/sql/clearhrstatus.sql", "/sql/inserthrstatus.sql"})
    public void test_should_find_all_recruitmenttype_not_exist() {
        HRStatusGetRequest hrstatusGetRequest = new HRStatusGetRequest();
        hrstatusGetRequest.setObjectName("LeaveApplication1");
        hrstatusGetRequest.setTenantId("default");
        List<HRStatus> hrstatuss = hrstatusRepository.findForCriteria(hrstatusGetRequest);
        assertEquals(Boolean.TRUE,hrstatuss.isEmpty());
    }
}
