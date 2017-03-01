package org.egov.pgr.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.pgr.persistence.entity.ReceivingCenter;
import org.egov.pgr.persistence.entity.ReceivingMode;
import org.egov.pgr.persistence.repository.ReceivingModeRepository;
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
	public void test_should_find_all_receiving_mode() {
		List<ReceivingMode> expectedResult = new ArrayList<ReceivingMode>();
		expectedResult.add(new ReceivingMode());
		when(receivingModeRepository.findAll()).thenReturn(expectedResult);

		final List<ReceivingMode> receivingMode = receivingModeService.getAllReceivingModes("ap.public");

		assertEquals(1, receivingMode.size());

	}

}