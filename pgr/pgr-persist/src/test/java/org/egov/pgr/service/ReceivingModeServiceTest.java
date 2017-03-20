package org.egov.pgr.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.egov.pgr.entity.ReceivingMode;
import org.egov.pgr.repository.ReceivingModeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReceivingModeServiceTest {

	@Mock
	private ReceivingModeRepository receivingModeRepository;

	@InjectMocks
	private ReceivingModeService receivingModeService;

	@Test
	public void testShouldFindReceivingModeByCode() {
		ReceivingMode expectedResult = ReceivingMode.builder().id(1L).name("Website").code("WEBSITE").build();
		when(receivingModeRepository.findByCode("WEBSITE")).thenReturn(expectedResult);
		final ReceivingMode receivingMode = receivingModeService.getReceivingModeByCode("WEBSITE");

		assertEquals(receivingMode, expectedResult);
	}

}