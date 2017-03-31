package org.egov.pgr.write.service;

import org.egov.pgr.write.entity.ReceivingCenter;
import org.egov.pgr.write.repository.ReceivingCenterWriteRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReceivingCenterWriteServiceTest {

    @Mock
    private ReceivingCenterWriteRepository receivingCenterRepository;

    @InjectMocks
    private ReceivingCenterWriteService receivingCenterService;

    @Test
    public void testShouldFindReceivingCenterById() {
        ReceivingCenter expectedResult = ReceivingCenter.builder()
            .id(1L)
            .name("Complaint Cell")
            .crnRequired(true)
            .orderNo(8L)
            .build();
        when(receivingCenterRepository.findById(1L)).thenReturn(expectedResult);
        final ReceivingCenter receivingCenter = receivingCenterService.getReceivingCenterById(1L);

        assertEquals(receivingCenter, expectedResult);
    }

}