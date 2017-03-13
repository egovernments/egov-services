package org.egov.user.domain.model;

import org.egov.user.domain.exception.InvalidUserSearchException;
import org.junit.Test;

public class UserSearchTest {

    @Test(expected = InvalidUserSearchException.class)
    public void test_validation_failure_when_no_fields_are_specified() throws Exception {
        UserSearch userSearch = new UserSearch();
        userSearch.validate();
    }
}