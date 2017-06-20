package org.egov.pgrrest.common.model;


import org.junit.Test;

import static org.junit.Assert.assertNull;

public class RequesterTest {

    @Test
    public void test_for_maskmobile_and_emaildetails(){

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

}