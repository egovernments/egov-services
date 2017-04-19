package org.egov.access.persistence.repository;

import org.egov.access.domain.model.Action;
import org.egov.access.domain.model.ActionSearchCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ActionRepositoryTest {

    @Autowired
    private ActionRepository actionRepository;

    @Test
    @Sql(scripts = {"/sql/clearAction.sql", "/sql/insertActionData.sql" })
    public void testShouldReturnActionForUserRole() throws Exception {
        List<String> roleCodesList = new ArrayList<String>();
        roleCodesList.add("CITIZEN");
        roleCodesList.add("SUPERUSER");
        ActionSearchCriteria actionSearchCriteria = ActionSearchCriteria.builder().tenantId("ap.public").roleCodes(roleCodesList).build();
        List<Action> actions = actionRepository.findForCriteria(actionSearchCriteria);
        assertEquals(8,actions.size());
        assertEquals(Long.valueOf(3),actions.get(0).getId());
        assertEquals("Get ComplaintType by type,count and tenantId",actions.get(0).getName());
        assertEquals("/pgr/services",actions.get(0).getUrl());
        assertEquals("Get ComplaintType by type,count and tenantId",actions.get(0).getDisplayName());
        assertEquals("ap.public",actions.get(0).getTenantId());
        assertEquals(Integer.valueOf(5),actions.get(0).getOrderNumber());
        assertEquals(false,actions.get(0).isEnabled());
        assertEquals(Long.valueOf(1),actions.get(0).getCreatedBy());
        assertEquals(Long.valueOf(1),actions.get(0).getLastModifiedBy());
        String currDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        assertEquals(currDate,actions.get(0).getCreatedDate().toString());
        assertEquals(currDate,actions.get(0).getLastModifiedDate().toString());
        assertEquals("PgrComp",actions.get(0).getParentModule());
        assertEquals("PGR",actions.get(0).getServiceCode());

    }

}
