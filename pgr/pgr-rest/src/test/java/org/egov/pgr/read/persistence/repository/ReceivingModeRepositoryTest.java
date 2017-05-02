package org.egov.pgr.read.persistence.repository;

import org.egov.pgr.common.entity.ReceivingMode;
import org.egov.pgr.common.repository.ReceivingModeJpaRepository;
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
public class ReceivingModeRepositoryTest {

    @Mock
    private ReceivingModeJpaRepository receivingModeJpaRepository;

    @InjectMocks
    private ReceivingModeRepository receivingModeRepository;

    @Test
    public void test_should_find_all_receivingmodes_by_tenantId()
    {
        List<ReceivingMode> receivingMode=getReceivingModes();
        when(receivingModeJpaRepository.findAllByTenantId("ap.public")).thenReturn(receivingMode);
        List<org.egov.pgr.read.domain.model.ReceivingMode> receivingModes=receivingModeRepository.findAllByReceivingModeByTenantId("ap.public");
        assertEquals(receivingMode.get(0).getName(),receivingModes.get(0).getName());
        assertEquals(receivingMode.get(1).getName(),receivingModes.get(1).getName());
        assertEquals(receivingMode.get(0).getId(),receivingModes.get(0).getId());
        assertEquals(receivingMode.get(1).getId(),receivingModes.get(1).getId());
    }

    public List<ReceivingMode>  getReceivingModes() {
        ReceivingMode receivingMode1=ReceivingMode.builder().id(1L).name("Website").code("WEBSITE").visible(false).tenantId("ap.public").build();
        ReceivingMode receivingMode2=ReceivingMode.builder().id(2l).name("SMS").code("SMS").visible(false).tenantId("ap.public").build();
        return Arrays.asList(receivingMode1,receivingMode2);
    }
}

