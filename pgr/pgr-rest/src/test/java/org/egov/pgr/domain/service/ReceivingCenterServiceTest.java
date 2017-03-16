package org.egov.pgr.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.egov.pgr.persistence.entity.ReceivingCenter;
import org.egov.pgr.persistence.entity.ReceivingMode;
import org.egov.pgr.persistence.repository.ReceivingCenterRepository;
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
	public void test_should_find_all_receiving_center() {
		List<ReceivingCenter> expectedResult = new ArrayList<ReceivingCenter>();
		expectedResult.add(new ReceivingCenter());
		when(receivingCenterRepository.findAll()).thenReturn(expectedResult);

		final List<ReceivingCenter> receivingMode = receivingCenterService.getAllReceivingCenters("ap.public");

		assertEquals(1, receivingMode.size());
		assertEquals(expectedResult, receivingMode);

	}

	@Test
	public void testShouldFindReceivingCenterById() {
		ReceivingCenter expectedResult = ReceivingCenter.builder().id(1L).name("Complaint Cell").isCrnRequired(true)
				.orderNo(8L).build();
		when(receivingCenterRepository.findById(1L)).thenReturn(expectedResult);
		final ReceivingCenter receivingCenter = receivingCenterService.getReceivingCenterById("ap.public", 1L);

		assertEquals(receivingCenter, expectedResult);
	}
}