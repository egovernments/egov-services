package org.egov.domain.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class EmailRequestTest {

    @Test
    public void test_equality_should_return_true_when_body_and_subject_are_same_for_two_instances() {
        final EmailRequest emailRequest1 = new EmailRequest("subject", "body");
        final EmailRequest emailRequest2 = new EmailRequest("subject", "body");

        assertTrue(emailRequest1.equals(emailRequest2));
    }

    @Test
    public void test_hashcode_should_match_when_body_and_subject_are_same_for_two_instances() {
        final EmailRequest emailRequest1 = new EmailRequest("subject", "body");
        final EmailRequest emailRequest2 = new EmailRequest("subject", "body");

        assertEquals(emailRequest1.hashCode(), emailRequest2.hashCode());
    }

    @Test
    public void test_hashcode_should_not_match_when_body_and_subject_are_different_for_two_instances() {
        final EmailRequest emailRequest1 = new EmailRequest("subject1", "body1");
        final EmailRequest emailRequest2 = new EmailRequest("subject2", "body2");

        assertNotEquals(emailRequest1.hashCode(), emailRequest2.hashCode());
    }

    @Test
    public void test_equality_should_return_false_when_body_is_different_for_two_instances() {
        final EmailRequest emailRequest1 = new EmailRequest("subject", "body1");
        final EmailRequest emailRequest2 = new EmailRequest("subject", "body2");

        assertFalse(emailRequest1.equals(emailRequest2));
    }

    @Test
    public void test_equality_should_return_false_when_subject_is_different_for_two_instances() {
        final EmailRequest emailRequest1 = new EmailRequest("subject1", "body");
        final EmailRequest emailRequest2 = new EmailRequest("subject2", "body");

        assertFalse(emailRequest1.equals(emailRequest2));
    }

}