package org.egov.pgr.read.persistence.repository;

import org.egov.pgr.common.entity.ReceivingCenter;
import org.egov.pgr.common.repository.ReceivingCenterJpaRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ReceivingCenterRepositoryTest {

    @Mock
    private ReceivingCenterJpaRepository receivingCenterJpaRepository;

    @InjectMocks
    private  ReceivingCenterRepository receivingCenterRepository;
    @Test
    public void test_should_find_receivingCenter_by_id_and_tenantId()
    {
        ReceivingCenter receivingCenter=getReceivingCenter();
        when(receivingCenterJpaRepository.findByIdAndTenantId(1L,"ap.public")).thenReturn(receivingCenter);
        org.egov.pgr.common.model.ReceivingCenter receivingCenter1=receivingCenterRepository.findReceivingCenterByIdAndTenantId(1L,"ap.public");
        assertEquals(receivingCenter.getName(),receivingCenter1.getName());
        assertEquals(receivingCenter.getId(),receivingCenter1.getId());
        assertEquals(receivingCenter.getOrderNo(),receivingCenter1.getOrderNo());
    }


    @Test
    public void test_should_return_all_receivingcenters_by_tenantId()
    {
       List<ReceivingCenter> receivingCenters=getReceivingCenters();
        when(receivingCenterJpaRepository.findAllByTenantId("ap.public")).thenReturn(receivingCenters);
       List<org.egov.pgr.common.model.ReceivingCenter> receivingCenters1= receivingCenterRepository.findAllReceivingCentersByTenantId("ap.public");
        assertEquals(receivingCenters.size(),receivingCenters1.size());
        assertEquals(receivingCenters.get(0).getId(),receivingCenters1.get(0).getId());
        assertEquals(receivingCenters.get(1).getId(),receivingCenters1.get(1).getId());
        assertEquals(receivingCenters.get(0).getName(),receivingCenters1.get(0).getName());
        assertEquals(receivingCenters.get(1).getName(),receivingCenters1.get(1).getName());
        assertEquals(receivingCenters.get(2).getOrderNo(),receivingCenters1.get(2).getOrderNo());
    }

    private List<ReceivingCenter> getReceivingCenters() {
        ReceivingCenter receivingCenter1=ReceivingCenter.builder().id(1L).name("Mayor/Chairperson Office").crnRequired(true).tenantId("ap.public").orderNo(2L).build();
        ReceivingCenter receivingCenter2=ReceivingCenter.builder().id(2L).name("CM Office'").crnRequired(false).tenantId("ap.public").orderNo(3L).build();
        ReceivingCenter receivingCenter3=ReceivingCenter.builder().id(3L).name("Public Representatives").crnRequired(true).tenantId("ap.public").orderNo(4L).build();
         return Arrays.asList(receivingCenter1,receivingCenter2,receivingCenter3);

    }

    private ReceivingCenter getReceivingCenter() {
       return ReceivingCenter.builder().id(1L).name("Mayor/Chairperson Office").crnRequired(true).orderNo(2L).tenantId("ap.public").build();
    }
}
