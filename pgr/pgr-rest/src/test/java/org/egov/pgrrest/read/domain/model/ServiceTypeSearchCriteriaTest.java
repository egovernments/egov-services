package org.egov.pgrrest.read.domain.model;


import org.egov.pgrrest.read.domain.exception.InvalidServiceTypeSearchException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class ServiceTypeSearchCriteriaTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test_should_throw_exception_when_search_type_is_not_known() {
        final ServiceTypeSearchCriteria searchCriteria = ServiceTypeSearchCriteria.builder()
                .complaintTypeSearch("unknown")
                .categoryId(123L)
                .build();

        thrown.expect(InvalidServiceTypeSearchException.class);
        thrown.expectMessage("Invalid search type");

        searchCriteria.validate();
    }

    @Test
    public void test_should_throw_exception_when_category_id_is_not_present_for_category_search() {
        final ServiceTypeSearchCriteria searchCriteria = ServiceTypeSearchCriteria.builder()
                .complaintTypeSearch("category")
                .categoryId(null)
                .build();

        thrown.expect(InvalidServiceTypeSearchException.class);
        thrown.expectMessage("Category id is not present");

        searchCriteria.validate();
    }

    @Test
    public void test_should_default_count_to_5_when_frequency_is_not_present() {
        final ServiceTypeSearchCriteria searchCriteria = ServiceTypeSearchCriteria.builder()
                .complaintTypeSearch("frequency")
                .count(null)
                .build();

        assertEquals(5, searchCriteria.getCount());
    }

    @Test
    public void test_should_return_count_when_frequency_is_present() {
        final ServiceTypeSearchCriteria searchCriteria = ServiceTypeSearchCriteria.builder()
                .complaintTypeSearch("frequency")
                .count(10)
                .build();

        assertEquals(10, searchCriteria.getCount());
    }

    @Test
    public void test_should_not_thrown_exception_for_a_valid_category_search_type() {
        final ServiceTypeSearchCriteria searchCriteria = ServiceTypeSearchCriteria.builder()
                .complaintTypeSearch("CATegory")
                .categoryId(10L)
                .build();

        searchCriteria.validate();
    }

    @Test
    public void test_should_not_thrown_exception_for_a_valid_frequency_search_type() {
        final ServiceTypeSearchCriteria searchCriteria = ServiceTypeSearchCriteria.builder()
                .complaintTypeSearch("FREQUENCY")
                .count(10)
                .build();

        searchCriteria.validate();
    }


}