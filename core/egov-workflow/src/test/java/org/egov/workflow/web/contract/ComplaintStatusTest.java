package org.egov.workflow.web.contract;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ComplaintStatusTest {

    @Test
    public void test_should_convert_model_to_contract() {
        org.egov.workflow.domain.model.ComplaintStatus complaintStatus =
                new org.egov.workflow.domain.model.ComplaintStatus(1L, "REGISTERED","default","0001");

        ComplaintStatus complaintStatusContract = new ComplaintStatus(complaintStatus);

        assertThat(complaintStatusContract.getId()).isEqualTo(1L);
        assertThat(complaintStatusContract.getName()).isEqualTo("REGISTERED");
    }
}