package org.egov.workflow.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.egov.workflow.persistence.entity.State;
import org.egov.workflow.persistence.entity.State.StateStatus;
import org.egov.workflow.persistence.entity.StateHistory;
import org.egov.workflow.persistence.repository.StateRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StateServiceTest {

    @Mock
    private StateRepository stateRepository;

    @InjectMocks
    private StateService stateService;

    @Test
    public void testShouldReturnStateWhenStateIdIsSpecified() {
        final State expectedState = new State();
        expectedState.setId(1L);
        when(stateRepository.findByIdAndTenantId(1L,"tenantId")).thenReturn(expectedState);

        final State actualState = stateService.getStateByIdAndTenantId(1L,"tenantId");

        assertEquals(1, actualState.getId().intValue());
        assertEquals(expectedState, actualState);

    }

    @Test
    public void testShouldCreateStateWhenStateIsProvided() {
        final State state = getState();
        final State expectedState = new State();

        when(stateRepository.save(state)).thenReturn(expectedState);

        final State actualState = stateService.create(state);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void testShouldUpdateStateWhenStateIsProvided() {
        final State state = getState();
        final State expectedState = new State();

        when(stateRepository.save(state)).thenReturn(expectedState);

        final State actualState = stateService.update(state);
        assertEquals(expectedState, actualState);
    }

    @Test
    public void testShouldGetAssignedWorkFlowTypeNamesWhenWorkFlowTypeAndOwnerIdIsProvided() {
        final List<Long> ownerIds = new ArrayList<>();
        ownerIds.add(34L);
        ownerIds.add(35L);
        final List<String> types = new ArrayList<>();
        types.add("type1");
        types.add("type2");
        final List<String> expectedAssignedWorkFlowTypes = new ArrayList<>();

        when(stateRepository.findAllTypeByOwnerAndStatus(ownerIds, types)).thenReturn(expectedAssignedWorkFlowTypes);
        final List<String> actualAssignedWorkFlowTypes = stateService.getAssignedWorkflowTypeNames(ownerIds, types);
        assertEquals(expectedAssignedWorkFlowTypes, actualAssignedWorkFlowTypes);

    }

    @Test
    public void testShouldVerifyWhetherThePositionExistsUnderWorkflow() {
        final Boolean expectedResult = true;
        final Long count = 2L;
        final Date date = new Date();
        when(stateRepository.countByOwnerPositionAndCreatedDateGreaterThanEqual(34L, date)).thenReturn(count);
        final Boolean actualResult = stateService.isPositionUnderWorkflow(34L, date);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testShouldVerifyPositionUnderWorkflow() {
        final Boolean expectedResult = false;
        final Long count = 0L;
        final Date date = new Date();
        when(stateRepository.countByOwnerPositionAndCreatedDateGreaterThanEqual(34L, date)).thenReturn(count);
        final Boolean actualResult = stateService.isPositionUnderWorkflow(34L, date);
        assertEquals(expectedResult, actualResult);
    }

    public State getState() {
        return State.builder().id(1L).type(null).value("value").ownerPosition(34L).ownerUser(45L)
                .senderName("senderName").nextAction("nextAction").comments("comments").natureOfTask("natureOfTask")
                .extraInfo("extraInfo").dateInfo(null).extraDateInfo(null).status(StateStatus.INPROGRESS)
                .initiatorPosition(45L).history(Collections.singleton(StateHistory.builder().build())).build();
    }
}
