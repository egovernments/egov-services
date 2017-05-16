package org.egov.workflow.domain.model;


import org.egov.workflow.domain.exception.InvalidComplaintStatusSearchException;
import org.junit.Test;

import java.util.Collections;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class ComplaintStatusSearchCriteriaTest {

    @Test
    public void test_equals() {
        ComplaintStatusSearchCriteria complaintStatusSearchCriteria1 =
                new ComplaintStatusSearchCriteria("REGISTERED", asList(1L, 2L), "default");

        ComplaintStatusSearchCriteria complaintStatusSearchCriteria2 =
                new ComplaintStatusSearchCriteria("REGISTERED", asList(1L, 2L), "default");

        ComplaintStatusSearchCriteria complaintStatusSearchCriteria3 =
                new ComplaintStatusSearchCriteria("CREATED", asList(1L, 2L), "default");

        ComplaintStatusSearchCriteria complaintStatusSearchCriteria4 =
                new ComplaintStatusSearchCriteria("CREATED", asList(1L), "default");

        assertThat(complaintStatusSearchCriteria1).isEqualTo(complaintStatusSearchCriteria2);
        assertThat(complaintStatusSearchCriteria1).isNotEqualTo(complaintStatusSearchCriteria3);
        assertThat(complaintStatusSearchCriteria3).isNotEqualTo(complaintStatusSearchCriteria4);
    }

    @Test(expected = InvalidComplaintStatusSearchException.class)
    public void test_should_fail_validation_when_name_is_empty() {
        ComplaintStatusSearchCriteria complaintStatusSearchCriteria =
                new ComplaintStatusSearchCriteria("", asList(1L, 2L), "default");

        complaintStatusSearchCriteria.validate();
    }

    @Test(expected = InvalidComplaintStatusSearchException.class)
    public void test_should_fail_validation_when_name_is_null() {
        ComplaintStatusSearchCriteria complaintStatusSearchCriteria =
                new ComplaintStatusSearchCriteria(null, asList(1L, 2L), "default");

        complaintStatusSearchCriteria.validate();
    }

    @Test(expected = InvalidComplaintStatusSearchException.class)
    public void test_should_fail_validation_when_roles_is_empty() {
        ComplaintStatusSearchCriteria complaintStatusSearchCriteria =
                new ComplaintStatusSearchCriteria("NAME", Collections.emptyList(), "default");

        complaintStatusSearchCriteria.validate();
    }

    @Test(expected = InvalidComplaintStatusSearchException.class)
    public void test_should_fail_validation_when_roles_is_null() {
        ComplaintStatusSearchCriteria complaintStatusSearchCriteria =
                new ComplaintStatusSearchCriteria("NAME", null, "default");

        complaintStatusSearchCriteria.validate();
    }
}