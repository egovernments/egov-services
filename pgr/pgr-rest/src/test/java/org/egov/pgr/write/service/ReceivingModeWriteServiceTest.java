package org.egov.pgr.write.service;

import org.egov.pgr.common.entity.ReceivingMode;
import org.egov.pgr.common.repository.ReceivingModeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReceivingModeWriteServiceTest {
    @Mock
    private ReceivingModeRepository receivingModeRepository;

    @InjectMocks
    private ReceivingModeWriteService receivingModeService;

    @Test
    public void testShouldFindReceivingModeByCode() {
        ReceivingMode expectedResult = ReceivingMode.builder().id(1L).name("Website").code("WEBSITE").build();
        when(receivingModeRepository.findByCode("WEBSITE")).thenReturn(expectedResult);
        final ReceivingMode receivingMode = receivingModeService.getReceivingModeByCode("WEBSITE");

        assertEquals(receivingMode, expectedResult);
    }

}