package org.egov.pgrrest.common.persistence.repository;

import org.egov.pgr.common.date.Date;
import org.egov.pgrrest.TestConfiguration;
import org.egov.pgrrest.common.persistence.entity.Submission;
import org.egov.pgrrest.common.persistence.entity.SubmissionKey;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(TestConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SubmissionJpaRepositoryTest {

    @Autowired
    private SubmissionJpaRepository submissionJpaRepository;

    @Before
    public void before() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Calcutta"));
    }

    @Test
    @Sql(scripts = {
        "/sql/clearSubmissions.sql",
        "/sql/insertSubmissions.sql"
    })
    public void test_should_retrieve_submissions_for_given_crn() {
        final SubmissionKey key = new SubmissionKey("crn1", "tenant1");
        final Submission actualSubmission = submissionJpaRepository.findOne(key);

        assertNotNull(actualSubmission);
        assertEquals("crn1", actualSubmission.getId().getCrn());
        assertEquals("tenant1", actualSubmission.getId().getTenantId());
        final Date expectedEscalationDate =
            new Date(LocalDateTime.of(1994, 11, 29, 0, 0));
        assertEquals(expectedEscalationDate.toDate(), actualSubmission.getEscalationDate());
        assertEquals("landmark1", actualSubmission.getLandmarkDetails());
        assertEquals(Long.valueOf(26), actualSubmission.getDepartment());
        assertEquals(Long.valueOf(3), actualSubmission.getPosition());
        assertEquals(1.2d, actualSubmission.getLatitude(), 0);
        assertEquals(4.5d, actualSubmission.getLongitude(), 0);
        assertEquals("REGISTERED", actualSubmission.getStatus());
        assertEquals("details1", actualSubmission.getDetails());
        assertEquals("servicecode1", actualSubmission.getServiceCode());
        assertEquals("foo@bar.com", actualSubmission.getEmail());
        assertEquals("123123122", actualSubmission.getMobile());
        assertEquals("name1", actualSubmission.getName());
        assertEquals(Long.valueOf(2), actualSubmission.getLoggedInRequester());
        assertEquals("requester add", actualSubmission.getRequesterAddress());
    }

    @Test
    @Sql(scripts = {
        "/sql/clearSubmissions.sql",
        "/sql/insertSubmissions.sql"
    })
    public void test_should_retrieve_assigneeid_for_given_crn() {
        final Long assigneeId = submissionJpaRepository.findPosition("crn1", "tenant1");

        assertEquals(Long.valueOf(3), assigneeId);
    }


}