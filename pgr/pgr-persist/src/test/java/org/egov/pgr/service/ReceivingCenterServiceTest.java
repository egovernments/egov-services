package org.egov.pgr.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.egov.pgr.entity.ReceivingCenter;
import org.egov.pgr.repository.ReceivingCenterRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReceivingCenterServiceTest {

	@Mock
	private ReceivingCenterRepository receivingCenterRepository;

	@InjectMocks
	private ReceivingCenterService receivingCenterService;

	@Test
	public void testShouldFindReceivingCenterById() {
		ReceivingCenter expectedResult = ReceivingCenter.builder().id(1L).name("Complaint Cell").isCrnRequired(true)
				.orderNo(8L).build();
		when(receivingCenterRepository.findById(1L)).thenReturn(expectedResult);
		final ReceivingCenter receivingCenter = receivingCenterService.getReceivingCenterById(1L);

		assertEquals(receivingCenter, expectedResult);
	}

}