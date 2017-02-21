package org.egov.pgr.persistence;

import org.egov.pgr.domain.model.ComplaintRegistrationNumber;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CrnRepositoryTest {

    @Mock
    private RestTemplate restTemplate;

    @Test
    public void shouldGetCrn() {
        final String CRN_SERVICE_URL = "http://localhost:8088/";
        final String CRN = "crn_number";
        final ComplaintRegistrationNumber expected = new ComplaintRegistrationNumber(CRN);
        when(restTemplate.getForObject(CRN_SERVICE_URL, ComplaintRegistrationNumber.class))
                .thenReturn(expected);

        CrnRepository crnRepository = new CrnRepository(restTemplate, CRN_SERVICE_URL);

        ComplaintRegistrationNumber actual = crnRepository.getCrn();

        assertEquals(expected, actual);

        verify(restTemplate).getForObject(CRN_SERVICE_URL, ComplaintRegistrationNumber.class);
    }
}