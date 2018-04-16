package org.egov.eis.persistance.repository;

import org.egov.eis.TestConfiguration;
import org.egov.eis.model.Group;
import org.egov.eis.repository.GroupRepository;
import org.egov.eis.web.contract.GroupGetRequest;
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

public class GroupTest {

    @Autowired
    private GroupRepository groupRepository;

    @Test
    @Sql(scripts = {"/sql/cleargroup.sql", "/sql/insertgroup.sql"})
    public void test_should_find_all_recruitmenttype() {
        GroupGetRequest groupGetRequest = new GroupGetRequest();
        groupGetRequest.builder().name("State").tenantId("default").build();
        List<Group> groups = groupRepository.findForCriteria(groupGetRequest);
        assertEquals("State",groups.get(0).getName());
    }

    @Test
    @Sql(scripts = {"/sql/cleargroup.sql", "/sql/insertgroup.sql"})
    public void test_should_find_all_recruitmenttype_not_exist() {
        GroupGetRequest groupGetRequest = new GroupGetRequest();
        groupGetRequest.setName("State1");
        groupGetRequest.setTenantId("default");
        List<Group> groups = groupRepository.findForCriteria(groupGetRequest);
        assertEquals(Boolean.TRUE,groups.isEmpty());
    }
}
