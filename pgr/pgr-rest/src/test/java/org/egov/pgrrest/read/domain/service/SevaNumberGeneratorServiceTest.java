package org.egov.pgrrest.read.domain.service;

import org.egov.pgrrest.read.domain.model.ServiceRequestRegistrationNumber;
import org.egov.pgrrest.read.persistence.repository.CrnRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SevaNumberGeneratorServiceTest {

    @Mock
    private CrnRepository crnRepository;

    @Test
    public void shouldGetCrn() {
        ServiceRequestRegistrationNumber expected = new ServiceRequestRegistrationNumber("crn");
        SevaNumberGeneratorService  sevaNumberGeneratorService = new SevaNumberGeneratorService(crnRepository);
        when(crnRepository.getCrn("default")).thenReturn(expected);

        String crn = sevaNumberGeneratorService.generate("default");

        assertEquals(expected.getValue(), crn);
        verify(crnRepository).getCrn("default");
    }
}