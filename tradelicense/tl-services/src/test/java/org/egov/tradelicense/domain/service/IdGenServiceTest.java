package org.egov.tradelicense.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import org.egov.common.utils.RequestJsonReader;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.common.domain.exception.IdGenerationException;
import org.egov.tradelicense.web.contract.AckNoGenerationRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class IdGenServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @InjectMocks
    private IdGenService idGenService = new IdGenService();

    @Mock
    private PropertiesManager propertiesManager;

    @Mock
    private RestTemplate restTemplate;

    private RequestJsonReader resources = new RequestJsonReader();

    @Before
    public void before() throws SQLException {
        when(propertiesManager.getIdGenServiceBasePathTopic()).thenReturn("http://egov-idgen:8080");
        when(propertiesManager.getIdGenServiceCreatePathTopic()).thenReturn("/egov-idgen/id/_generate");
    }

    @Test
    public void test_tl_number_generator_should_return_number() {
        when(restTemplate.postForObject(Mockito.anyString(), Mockito.any(AckNoGenerationRequest.class), Mockito.any()))
                .thenReturn(resources.readResponse("idGenSuccessResponse.json"));
        final String generatedNumber = idGenService.generate("ap.kurnool", "", "", new RequestInfo());

        assertEquals("12345", generatedNumber);
    }

    @Test
    public void test_tl_number_generator_should_throw_id_generation_exception() {
        when(restTemplate.postForObject(Mockito.anyString(), Mockito.any(AckNoGenerationRequest.class), Mockito.any()))
                .thenReturn(resources.readResponse("idGenErrorResponse.json"));
        thrown.expect(IdGenerationException.class);
        idGenService.generate("ap.kurnool", "", "", new RequestInfo());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_tl_number_generator_should_throw_exception() {
        when(restTemplate.postForObject(Mockito.anyString(), Mockito.any(AckNoGenerationRequest.class), Mockito.any()))
                .thenThrow(Exception.class);
        thrown.expect(Exception.class);
        idGenService.generate("ap.kurnool", "", "", new RequestInfo());
    }
}
