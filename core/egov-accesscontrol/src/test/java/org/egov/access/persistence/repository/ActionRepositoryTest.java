package org.egov.access.persistence.repository;

import org.egov.access.domain.model.Action;
import org.egov.access.domain.model.ActionSearchCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        ActionSearchCriteria actionSearchCriteria = ActionSearchCriteria.builder().roleCodes(roleCodesList).build();
        List<Action> actions = actionRepository.findForCriteria(actionSearchCriteria);
        assertFalse(actions.isEmpty());

    }

    private List<Action> getActions() {
        List<Action> actions = new ArrayList<Action>();
        Action action1 = Action.builder().id(1L).name("Create Complaint").displayName("Create Complaint").url("/createcomplaint")
                .createdDate(new Date()).createdBy(1L).lastModifiedBy(1L).lastModifiedDate(new Date()).build();

        Action action2 = Action.builder().id(1L).name("Update Complaint").displayName("Update Complaint").url("/updatecomplaint")
                .createdDate(new Date()).createdBy(2L).lastModifiedBy(2L).lastModifiedDate(new Date()).build();
        return actions;
    }

}
