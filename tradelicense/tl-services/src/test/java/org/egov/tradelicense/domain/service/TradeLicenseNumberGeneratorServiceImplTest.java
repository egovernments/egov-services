package org.egov.tradelicense.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import org.egov.tradelicense.persistence.util.DBSequenceGenerator;
import org.egov.tradelicense.persistence.util.SequenceNumberGenerator;
import org.egov.tradelicense.persistence.util.Utils;
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
public class TradeLicenseNumberGeneratorServiceImplTest {
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @InjectMocks
    private TradeLicenseNumberGeneratorService tradeLicenseNumberGeneratorService = new TradeLicenseNumberGeneratorServiceImpl();

    @Mock
    private SequenceNumberGenerator sequenceNumberGenerator;

    @Mock
    private DBSequenceGenerator dbSequenceGenerator;
    
    @Mock
    private Utils utils;

    @Before
    public void before() throws SQLException {
        when(sequenceNumberGenerator.getNextSequence(Mockito.anyString())).thenReturn(001);
        when(dbSequenceGenerator.createAndGetNextSequence(Mockito.anyString())).thenReturn(001);
        when(utils.currentDateToYearFormat()).thenReturn("2017");
    }

    @Test
    public void test_tl_number_generator_should_return_number() {
        final String tlNumber = tradeLicenseNumberGeneratorService.generate();

        assertEquals("TL/00001/2017", tlNumber);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void test_tl_number_generator_should_throw_error() throws SQLException {
        when(sequenceNumberGenerator.getNextSequence(Mockito.anyString())).thenThrow(SQLException.class);
        when(dbSequenceGenerator.createAndGetNextSequence(Mockito.anyString())).thenThrow(SQLException.class);
        thrown.expect(RuntimeException.class);
        tradeLicenseNumberGeneratorService.generate();
    }
}
