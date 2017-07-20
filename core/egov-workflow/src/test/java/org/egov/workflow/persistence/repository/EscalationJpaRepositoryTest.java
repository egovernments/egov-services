package org.egov.workflow.persistence.repository;

import org.egov.workflow.TestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfiguration.class)
public class EscalationJpaRepositoryTest {

    @Autowired
    private EscalationJpaRepository repository;

    @Test
    @Sql(scripts = {"/sql/clearEscalationHours.sql", "/sql/insertEscalationHours.sql"})
    public void test_should_retrieve_escalation_hours_for_given_search_criteria() {
        final Integer escalationHours = repository.findBy(2, 1, "tenant1");

        assertEquals(Integer.valueOf(13), escalationHours);
    }

    @Test
    @Sql(scripts = {"/sql/clearEscalationHours.sql", "/sql/insertEscalationHours.sql"})
    public void test_should_return_null_when_no_escalation_hours_configured_for_given_search_criteria() {
        final Integer escalationHours = repository.findBy(2, 1, "unknown");

        assertNull(escalationHours);
    }

}