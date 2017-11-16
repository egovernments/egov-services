package org.egov.pgrrest.read.persistence;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pgrrest.read.domain.model.ServiceRequestRegistrationNumber;
import org.egov.pgrrest.read.persistence.repository.CrnRepository;
import org.egov.pgrrest.read.web.contract.RequestInfoBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CrnRepositoryTest {

    @Mock
    private RestTemplate restTemplate;

    @Test
    public void shouldGetCrn() {
        final String CRN_HOST = "http://localhost:8088/";
        final String EXPECTED_URL = "http://localhost:8088/crn-generation/crn/v1/_create?tenantId={tenantId}";
        final String CRN = "crn_number";
        final ServiceRequestRegistrationNumber expected = new ServiceRequestRegistrationNumber(CRN);
        when(restTemplate.postForObject(eq(EXPECTED_URL), any(), eq(ServiceRequestRegistrationNumber.class),anyString()))
            .thenReturn(expected);
        CrnRepository crnRepository = new CrnRepository(restTemplate, CRN_HOST);

        ServiceRequestRegistrationNumber actual = crnRepository.getCrn("tenantId");

        assertEquals(expected, actual);

    }

}