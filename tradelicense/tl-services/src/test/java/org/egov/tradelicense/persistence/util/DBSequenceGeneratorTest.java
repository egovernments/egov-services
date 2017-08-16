package org.egov.tradelicense.persistence.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class DBSequenceGeneratorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @InjectMocks
    private DBSequenceGenerator dbSequenceGenerator;

    @Mock
    JdbcTemplate jdbcTemplate;

    private static final String LICENSE_NUMBER_SEQ_NAME = "egtl_license_number";

    @Before
    public void before() {
        when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.any(Object[].class), eq(Integer.class))).thenReturn(1);
    }

    @Test
    public void test_db_sequence_generator_should_return_value() throws SQLException {
        final Serializable value = dbSequenceGenerator.createAndGetNextSequence(LICENSE_NUMBER_SEQ_NAME);
        assertEquals(1, value);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_db_sequence_generator_should_throw_exception() throws SQLException {
        when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.any(Object[].class), eq(Integer.class)))
                .thenThrow(SQLException.class);
        thrown.expect(SQLException.class);
        dbSequenceGenerator.createAndGetNextSequence(LICENSE_NUMBER_SEQ_NAME);
    }
}
