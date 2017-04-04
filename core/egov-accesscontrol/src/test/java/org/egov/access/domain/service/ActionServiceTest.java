package org.egov.access.domain.service;

import org.egov.access.domain.model.Action;
import org.egov.access.domain.model.ActionSearchCriteria;
import org.egov.access.persistence.repository.ActionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ActionServiceTest {

    @Mock
    private ActionRepository actionRepository;

    @InjectMocks
    private ActionService actionService;

    @Test
    public void testShouldReturnActionsForUserRole() throws Exception {

        final ActionSearchCriteria actionSearchCriteria = ActionSearchCriteria.builder().build();
        final List<Action> actionsExpected = getActions();
        when(actionRepository.findForCriteria(actionSearchCriteria)).thenReturn(actionsExpected);

        List<Action> actualActions = actionService.getActions(actionSearchCriteria);
        assertEquals(actionsExpected,actualActions);
    }

    private List<Action> getActions() {
        List<Action> actions = new ArrayList<Action>();
        Action action1 = Action.builder().id(1L).name("Create Complaint").displayName("Create Complaint").createdBy(1L).lastModifiedBy(1L)
                .url("/createcomplaint").build();
        Action action2 = Action.builder().id(2L).name("Update Complaint").displayName("Update Complaint").createdBy(1L).lastModifiedBy(1L)
                .url("/updatecomplaint").build();
        actions.add(action1);
        actions.add(action2);
        return actions;
    }
}
