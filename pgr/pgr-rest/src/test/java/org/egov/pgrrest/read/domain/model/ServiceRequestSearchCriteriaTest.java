package org.egov.pgrrest.read.domain.model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ServiceRequestSearchCriteriaTest {

    @Test
    public void test_should_return_true_when_pagination_criteria_is_present() {
        final ServiceRequestSearchCriteria searchCriteria = ServiceRequestSearchCriteria.builder()
            .pageSize(10)
            .fromIndex(2)
            .build();

        assertTrue(searchCriteria.isPaginationCriteriaPresent());
    }

    @Test
    public void test_should_return_false_when_page_size_is_not_present() {
        final ServiceRequestSearchCriteria searchCriteria = ServiceRequestSearchCriteria.builder()
            .pageSize(null)
            .fromIndex(2)
            .build();

        assertFalse(searchCriteria.isPaginationCriteriaPresent());
    }

    @Test
    public void test_should_return_false_when_page_number_is_not_present() {
        final ServiceRequestSearchCriteria searchCriteria = ServiceRequestSearchCriteria.builder()
            .pageSize(1)
            .fromIndex(null)
            .build();

        assertFalse(searchCriteria.isPaginationCriteriaPresent());
    }

}