package org.egov.pgrrest.common.persistence.repository;

import org.egov.pgrrest.TestConfiguration;
import org.egov.pgrrest.common.persistence.entity.SubmissionAttribute;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(TestConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SubmissionAttributeJpaRepositoryTest {

    @Autowired
    private SubmissionAttributeJpaRepository submissionAttributeJpaRepository;

    @Test
    @Sql(scripts = {
        "/sql/clearSubmissionAttributes.sql",
        "/sql/insertSubmissionAttributes.sql"
    })
    public void test_should_retrieve_submissions_for_given_crn() {
        final List<SubmissionAttribute> submissionAttributes = submissionAttributeJpaRepository
            .findByCrnAndTenantId("crn1", "tenant1");

        assertNotNull(submissionAttributes);
        assertEquals(3, submissionAttributes.size());
        assertEquals("code1", submissionAttributes.get(0).getId().getCode());
        assertEquals("crn1", submissionAttributes.get(0).getId().getCrn());
        assertEquals("key1", submissionAttributes.get(0).getId().getKey());
        assertEquals("tenant1", submissionAttributes.get(0).getId().getTenantId());
    }

}