package org.egov.pgrrest.common.domain.model;


import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RequesterTest {

    @Test
    public void test_should_make_personal_details(){
        Requester requester = Requester.builder()
            .address("address")
            .email("test@abc.com")
            .firstName("test")
            .mobile("000000000")
            .userId("1")
            .build();

        requester.maskMobileAndEmailDetails();

        assertNull(requester.getEmail());
        assertNull(requester.getMobile());
    }

    @Test
    public void test_should_false_when_email_address_is_not_valid() {
        Requester requester = Requester.builder()
            .email("test@abc")
            .build();

        assertFalse(requester.isValidEmailAddress());
    }

    @Test
    public void test_should_true_when_email_address_is_valid() {
        Requester requester = Requester.builder()
            .email("test@abc.com")
            .build();

        assertTrue(requester.isValidEmailAddress());
    }

}