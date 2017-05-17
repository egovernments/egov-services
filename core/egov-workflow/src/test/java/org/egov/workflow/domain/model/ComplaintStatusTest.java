package org.egov.workflow.domain.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ComplaintStatusTest {

    @Test
    public void test_equals() throws Exception {
        ComplaintStatus complaintStatus1 = new ComplaintStatus(1L, "REGISTERED","default","0001");
        ComplaintStatus complaintStatus2 = new ComplaintStatus(1L, "REGISTERED","default","0001");
        ComplaintStatus complaintStatus3 = new ComplaintStatus(2L, "DISPATCHED","default","0003");
        ComplaintStatus complaintStatus4 = new ComplaintStatus(2L, "WITHDRAWN","default","0003");
        ComplaintStatus complaintStatus5 = new ComplaintStatus(5L, "WITHDRAWN","default","0005");

        assertThat(complaintStatus1).isEqualTo(complaintStatus2);
        assertThat(complaintStatus2).isNotEqualTo(complaintStatus3);
        assertThat(complaintStatus4).isNotEqualTo(complaintStatus3);
        assertThat(complaintStatus5).isNotEqualTo(complaintStatus4);
    }
}