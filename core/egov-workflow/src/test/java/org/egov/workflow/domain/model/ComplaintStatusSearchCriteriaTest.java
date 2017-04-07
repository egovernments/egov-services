package org.egov.workflow.domain.model;


import org.junit.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class ComplaintStatusSearchCriteriaTest {

    @Test
    public void test_equals() {
        ComplaintStatusSearchCriteria complaintStatusSearchCriteria1 =
                new ComplaintStatusSearchCriteria("REGISTERED", asList(1L, 2L));

        ComplaintStatusSearchCriteria complaintStatusSearchCriteria2 =
                new ComplaintStatusSearchCriteria("REGISTERED", asList(1L, 2L));

        ComplaintStatusSearchCriteria complaintStatusSearchCriteria3 =
                new ComplaintStatusSearchCriteria("CREATED", asList(1L, 2L));

        ComplaintStatusSearchCriteria complaintStatusSearchCriteria4 =
                new ComplaintStatusSearchCriteria("CREATED", asList(1L));

        assertThat(complaintStatusSearchCriteria1).isEqualTo(complaintStatusSearchCriteria2);
        assertThat(complaintStatusSearchCriteria1).isNotEqualTo(complaintStatusSearchCriteria3);
        assertThat(complaintStatusSearchCriteria3).isNotEqualTo(complaintStatusSearchCriteria4);
    }
}