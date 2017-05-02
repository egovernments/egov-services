package org.egov.pgr.read.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.egov.pgr.common.model.ReceivingCenter;
import org.egov.pgr.read.persistence.repository.ReceivingCenterRepository;
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
        List<ReceivingCenter> expectedResult = getReceivingCenters();

        when(receivingCenterRepository.findAllReceivingCentersByTenantId("ap.public")).thenReturn(expectedResult);

        final List<ReceivingCenter> receivingMode = receivingCenterService.getAllReceivingCenters("ap.public");

        assertEquals(expectedResult,receivingMode);
        assertEquals(expectedResult.get(0).getName(),receivingMode.get(0).getName());
        assertEquals(expectedResult.get(1).getName(),receivingMode.get(1).getName());

    }


    @Test
    public void testShouldFindReceivingCenterById() {
        ReceivingCenter expectedResult = ReceivingCenter.builder().id(1L).name("Complaint Cell").tenantId("ap.public")
                .crnRequired(true).orderNo(8L).build();
        when(receivingCenterRepository.findReceivingCenterByIdAndTenantId(1L, "ap.public")).thenReturn(expectedResult);
        final ReceivingCenter receivingCenter = receivingCenterService.getReceivingCenterById("ap.public", 1L);

        assertEquals(receivingCenter, expectedResult);
    }
    private List<ReceivingCenter> getReceivingCenters() {

        ReceivingCenter receivingCenter1= ReceivingCenter.builder().id(1L).name("Mayor/Chairperson Office").crnRequired(true).tenantId("ap.public").orderNo(2L).build();
        ReceivingCenter receivingCenter2= ReceivingCenter.builder().id(2L).name("CM Office").crnRequired(false).tenantId("ap.public").orderNo(3L).build();
        ReceivingCenter receivingCenter3= ReceivingCenter.builder().id(3L).name("Public Representatives").crnRequired(false).orderNo(4L).tenantId("ap.public").build();
        return Arrays.asList(receivingCenter1,receivingCenter2,receivingCenter3);

    }

}