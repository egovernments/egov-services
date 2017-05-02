package org.egov.pgr.read.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.egov.pgr.read.domain.model.ReceivingMode;
import org.egov.pgr.read.persistence.repository.ReceivingModeRepository;
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
		List<ReceivingMode> expectedResult =getReceivingModes();

		when(receivingModeRepository.findAllByReceivingModeByTenantId("ap.public")).thenReturn(expectedResult);

		final List<ReceivingMode> receivingMode = receivingModeService.getAllReceivingModes("ap.public");

		assertEquals(expectedResult,receivingMode);
        assertEquals(expectedResult.get(0).getName(),receivingMode.get(0).getName());
        assertEquals(expectedResult.get(1).getName(),receivingMode.get(1).getName());
        assertEquals(expectedResult.get(2).getName(),receivingMode.get(2).getName());

	}

    private List<ReceivingMode> getReceivingModes() {
        ReceivingMode receivingMode1=ReceivingMode.builder().id(1L).name("Website").code("WEBSITE").visible(false).tenantId("ap.public").build();
        ReceivingMode receivingMode2=ReceivingMode.builder().id(2l).name("SMS").code("SMS").visible(false).tenantId("ap.public").build();
        ReceivingMode receivingMode3=ReceivingMode.builder().id(3L).name("Mobile").code("MOBILE").visible(false).tenantId("ap.public").build();
        return Arrays.asList(receivingMode1,receivingMode2,receivingMode3);
    }

}