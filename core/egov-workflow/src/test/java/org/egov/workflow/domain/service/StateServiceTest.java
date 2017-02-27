package org.egov.workflow.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.egov.workflow.persistence.repository.StateRepository;
import org.egov.workflow.persistence.entity.State;
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
		when(stateRepository.findOne(1L)).thenReturn(expectedState);

		final State actualState = stateService.getStateById(1L);

		assertEquals(1, actualState.getId().intValue());
		assertEquals(expectedState, actualState);

	}

}