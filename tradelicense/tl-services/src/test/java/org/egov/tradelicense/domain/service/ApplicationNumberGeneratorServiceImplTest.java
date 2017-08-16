package org.egov.tradelicense.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationNumberGeneratorServiceImplTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private IdGenService idGenService;

    @Mock
    private PropertiesManager propertiesManager;

    @InjectMocks
    private ApplicationNumberGeneratorService applicationNumberGeneratorService = new ApplicationNumberGeneratorServiceImpl();

    @Before
    public void before() throws SQLException {
        when(idGenService.generate(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.any(RequestInfo.class))).thenReturn("12345");
        when(propertiesManager.getIdApplicationNumberGenNameServiceTopic()).thenReturn("");
        when(propertiesManager.getIdApplicationNumberGenFormatServiceTopic()).thenReturn("");
    }

    @Test
    public void test_tl_number_generator_should_return_number() {
        final String applicationNumber = applicationNumberGeneratorService.generate("ap.kurnool", new RequestInfo());
        assertEquals("12345", applicationNumber);
    }
}
