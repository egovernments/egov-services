package org.egov.persistence.util;

import org.egov.persistence.util.repository.TenantRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class DBSequenceGeneratorTest {

    @Autowired
    DBSequenceGenerator dbSequenceGenerator;

    @Autowired
    TenantRepository tenantRepository;

    @Test
    @Sql(scripts = {"/dropSequence.sql"})
    public void shouldCreateSequenceAndGetNextValue() throws Exception {
        Serializable firstSequenceValue =
                dbSequenceGenerator.createAndGetNextSequence("SEQ_APPLICATION_NUMBER");
        assertEquals(BigInteger.valueOf(1), firstSequenceValue);
    }
}