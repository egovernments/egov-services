package org.egov.pgr.notification.domain.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class EmailRequestTest {

    @Test
    public void test_equality_should_return_true_when_fields_are_same_for_two_instances() {
        final EmailRequest emailRequest1 = new EmailRequest("subject", "body", "foo@bar.com");
        final EmailRequest emailRequest2 = new EmailRequest("subject", "body", "foo@bar.com");

        assertTrue(emailRequest1.equals(emailRequest2));
    }

    @Test
    public void test_hashcode_should_match_when_fields_are_same_for_two_instances() {
        final EmailRequest emailRequest1 = new EmailRequest("subject", "body", "foo@bar.com");
        final EmailRequest emailRequest2 = new EmailRequest("subject", "body", "foo@bar.com");

        assertEquals(emailRequest1.hashCode(), emailRequest2.hashCode());
    }

    @Test
    public void test_hashcode_should_not_match_when_fields_are_different_for_two_instances() {
        final EmailRequest emailRequest1 = new EmailRequest("subject1", "body1", "foo@bar.com");
        final EmailRequest emailRequest2 = new EmailRequest("subject2", "body2", "foo2@bar.com");

        assertNotEquals(emailRequest1.hashCode(), emailRequest2.hashCode());
    }

    @Test
    public void test_equality_should_return_false_when_body_is_different_for_two_instances() {
        final EmailRequest emailRequest1 = new EmailRequest("subject", "body1", "foo@bar.com");
        final EmailRequest emailRequest2 = new EmailRequest("subject", "body2", "foo@bar.com");

        assertFalse(emailRequest1.equals(emailRequest2));
    }

    @Test
    public void test_equality_should_return_false_when_subject_is_different_for_two_instances() {
        final EmailRequest emailRequest1 = new EmailRequest("subject1", "body", "foo@bar.com");
        final EmailRequest emailRequest2 = new EmailRequest("subject2", "body", "foo@bar.com");

        assertFalse(emailRequest1.equals(emailRequest2));
    }

}