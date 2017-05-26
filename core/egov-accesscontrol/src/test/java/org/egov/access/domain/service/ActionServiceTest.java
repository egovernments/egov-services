package org.egov.access.domain.service;

import org.egov.access.domain.criteria.ValidateActionCriteria;
import org.egov.access.domain.model.Action;
import org.egov.access.domain.criteria.ActionSearchCriteria;
import org.egov.access.domain.model.ActionValidation;
import org.egov.access.persistence.repository.BaseRepository;
import org.egov.access.persistence.repository.querybuilder.ActionFinderQueryBuilder;
import org.egov.access.persistence.repository.querybuilder.ValidateActionQueryBuilder;
import org.egov.access.persistence.repository.rowmapper.ActionRowMapper;
import org.egov.access.persistence.repository.rowmapper.ActionValidationRowMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ActionServiceTest {

    @Mock
    private BaseRepository repository;

    @InjectMocks
    private ActionService actionService;

    @Test
    public void testShouldReturnActionsForUserRole() throws Exception {

        ActionSearchCriteria actionSearchCriteria = ActionSearchCriteria.builder().build();
        List<Object> actionsExpected = getActions();
        ActionFinderQueryBuilder queryBuilder = new ActionFinderQueryBuilder(actionSearchCriteria);
        when(repository.run(Mockito.any(ActionFinderQueryBuilder.class), Mockito.any(ActionRowMapper.class))).thenReturn(actionsExpected);

        List<Action> actualActions = actionService.getActions(actionSearchCriteria);
        assertEquals(actionsExpected, actualActions);
    }

    @Test
    public void testValidateQueriesRepositoryToValidateTheCriteria() {
        ActionValidation expectedValidation = ActionValidation.builder().allowed(true).build();
        when(repository.run(Mockito.any(ValidateActionQueryBuilder.class), Mockito.any(ActionValidationRowMapper.class))).thenReturn(Arrays.asList(expectedValidation));

        assert (actionService.validate(ValidateActionCriteria.builder().build()).isAllowed());
    }

    private List<Object> getActions() {
        List<Object> actions = new ArrayList<>();
        Action action1 = Action.builder().id(1L).name("Create Complaint").displayName("Create Complaint").createdBy(1L).lastModifiedBy(1L)
                .url("/createcomplaint").build();
        Action action2 = Action.builder().id(2L).name("Update Complaint").displayName("Update Complaint").createdBy(1L).lastModifiedBy(1L)
                .url("/updatecomplaint").build();
        actions.add(action1);
        actions.add(action2);
        return actions;
    }
}
