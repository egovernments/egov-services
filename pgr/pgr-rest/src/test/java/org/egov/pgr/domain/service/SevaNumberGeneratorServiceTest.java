package org.egov.pgr.domain.service;

import org.egov.pgr.domain.model.ComplaintRegistrationNumber;
import org.egov.pgr.persistence.CrnRepository;
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
        ComplaintRegistrationNumber expected = new ComplaintRegistrationNumber("crn");
        SevaNumberGeneratorService  sevaNumberGeneratorService = new SevaNumberGeneratorService(crnRepository);
        when(crnRepository.getCrn()).thenReturn(expected);

        String crn = sevaNumberGeneratorService.generate();

        assertEquals(expected.getValue(), crn);
        verify(crnRepository).getCrn();
    }
}