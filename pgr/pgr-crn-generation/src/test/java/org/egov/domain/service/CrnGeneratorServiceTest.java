package org.egov.domain.service;

import org.egov.persistence.util.DBSequenceGenerator;
import org.egov.persistence.util.SequenceNumberGenerator;
import org.egov.persistence.util.Utils;
import org.egov.persistence.util.repository.TenantRepository;
import org.egov.web.contract.City;
import org.egov.web.contract.Tenant;
import org.hibernate.exception.SQLGrammarException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CrnGeneratorServiceTest {

    @Mock
    private SequenceNumberGenerator sequenceNumberGenerator;

    @Mock
    private DBSequenceGenerator dbSequenceGenerator;

    @Mock
    private Utils utils;
    @Mock
    private TenantRepository tenantRepository;

    private CrnGeneratorService crnGeneratorService;

    private String ulbName = "ROHA";
    private String councilName = "COUNCIL";
    private String APP_NUMBER_SEQ_PREFIX = "SEQ_APPLICATION_NUMBER";
    private String YEAR = "2017";
    private String SEQUENCE_NAME = APP_NUMBER_SEQ_PREFIX;

    @Before
    public void setUp() {
        crnGeneratorService =
                new CrnGeneratorService(sequenceNumberGenerator, dbSequenceGenerator, utils, tenantRepository);
        when(utils.currentDateToYearFormat()).thenReturn(YEAR);
        when(utils.getRandomAlphabets()).thenReturn("AB");
    }

    @Test
    public void shouldGetNextSequenceNumber() throws Exception {
        when(sequenceNumberGenerator.getNextSequence(SEQUENCE_NAME)).thenReturn(1);
        when(tenantRepository.fetchTenantByCode(any())).thenReturn(getTenant());

        String crnNumber = crnGeneratorService.generate("default");

        assertEquals("ROHA/COUNCIL/00001/2017", crnNumber);
        verify(sequenceNumberGenerator).getNextSequence(SEQUENCE_NAME);
    }

    @Test
    public void shouldCreateAndGetNextSequenceNumberWhenSequenceDoesNotExist() throws Exception {
        when(sequenceNumberGenerator.getNextSequence(SEQUENCE_NAME))
                .thenThrow(new SQLGrammarException("", new SQLException()));
        when(dbSequenceGenerator.createAndGetNextSequence(SEQUENCE_NAME)).thenReturn(1);
        when(tenantRepository.fetchTenantByCode(any())).thenReturn(getTenant());

        String crnNumber = crnGeneratorService.generate("default");

        assertEquals("ROHA/COUNCIL/00001/2017", crnNumber);
        verify(sequenceNumberGenerator).getNextSequence(SEQUENCE_NAME);
        verify(dbSequenceGenerator).createAndGetNextSequence(SEQUENCE_NAME);
    }

    @Test(expected = RuntimeException.class)
    public void shouldHandleWhenSequenceCreationFail() throws Exception {
        when(sequenceNumberGenerator.getNextSequence(SEQUENCE_NAME))
                .thenThrow(new SQLGrammarException("", new SQLException()));
        when(dbSequenceGenerator.createAndGetNextSequence(SEQUENCE_NAME)).thenThrow(new SQLException());
        when(tenantRepository.fetchTenantByCode(any())).thenReturn(getTenant());

        crnGeneratorService.generate("default");

        verify(sequenceNumberGenerator).getNextSequence(SEQUENCE_NAME);
        verify(dbSequenceGenerator).createAndGetNextSequence(SEQUENCE_NAME);
    }

    private List<Tenant> getTenant(){
        Tenant tenant = Tenant.builder()
            .city(City.builder().code("ROHA").build())
            .type("COUNCIL")
            .build();

        return Collections.singletonList(tenant);
    }

}