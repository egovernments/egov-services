package org.egov.workflow.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.egov.workflow.persistence.repository.StateRepository;
import org.egov.workflow.persistence.entity.State;
import org.egov.workflow.persistence.entity.State.StateStatus;
import org.egov.workflow.persistence.entity.StateHistory;
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
		;
		expectedState.setId(1L);
		when(stateRepository.findOne(1L)).thenReturn(expectedState);

		final State actualState = stateService.getStateById(1L);

		assertEquals(1, actualState.getId().intValue());
		assertEquals(expectedState, actualState);

	}

	@Test
	public void testShouldCreateStateWhenStateIsProvided() {
		State state = getState();
		State expectedState = new State();

		when(stateRepository.save(state)).thenReturn(expectedState);

		State actualState = stateService.create(state);
		assertEquals(expectedState, actualState);
	}

	@Test
	public void testShouldUpdateStateWhenStateIsProvided() {
		State state = getState();
		State expectedState = new State();

		when(stateRepository.save(state)).thenReturn(expectedState);

		State actualState = stateService.create(state);
		assertEquals(expectedState, actualState);
	}

	@Test
	public void testShouldGetAssignedWorkFlowTypeNamesWhenWorkFlowTypeAndOwnerIdIsProvided() {
		List<Long> ownerIds = new ArrayList<>();
		ownerIds.add(34L);
		ownerIds.add(35L);
		List<String> types = new ArrayList<>();
		types.add("type1");
		types.add("type2");
		List<String> expectedAssignedWorkFlowTypes = new ArrayList<>();

		when(stateRepository.findAllTypeByOwnerAndStatus(ownerIds, types)).thenReturn(expectedAssignedWorkFlowTypes);
		List<String> actualAssignedWorkFlowTypes = stateService.getAssignedWorkflowTypeNames(ownerIds, types);
		assertEquals(expectedAssignedWorkFlowTypes, actualAssignedWorkFlowTypes);

	}

	@Test
	public void testShouldVerifyWhetherThePositionExistsUnderWorkflow() {
		Boolean expectedResult = true;
		Long count = 2L;
		Date date = new Date();
		when(stateRepository.countByOwnerPositionAndCreatedDateGreaterThanEqual(34L, date)).thenReturn(count);
		Boolean actualResult = stateService.isPositionUnderWorkflow(34L, date);
		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void testShouldVerifyPositionUnderWorkflow() {
		Boolean expectedResult = false;
		Long count = 0L;
		Date date = new Date();
		when(stateRepository.countByOwnerPositionAndCreatedDateGreaterThanEqual(34L, date)).thenReturn(count);
		Boolean actualResult = stateService.isPositionUnderWorkflow(34L, date);
		assertEquals(expectedResult, actualResult);
	}

	public State getState() {
		return State.builder().id(1L).type(null).value("value").ownerPosition(34L).ownerUser(45L)
				.senderName("senderName").nextAction("nextAction").comments("comments").natureOfTask("natureOfTask")
				.extraInfo("extraInfo").dateInfo(null).extraDateInfo(null).status(StateStatus.INPROGRESS)
				.initiatorPosition(45L).history(Collections.singleton(StateHistory.builder().build())).build();
	}
}
