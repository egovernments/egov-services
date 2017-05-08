package org.egov.pgrrest.read.web.contract;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ComplaintStatusTest {

    @Test
    public void test_should_convert_model_to_contract() {
        org.egov.pgrrest.read.domain.model.ComplaintStatus complaintStatus =
                new org.egov.pgrrest.read.domain.model.ComplaintStatus(1L, "REGISTERED");

        org.egov.pgrrest.read.web.contract.ComplaintStatus complaintStatusContract =
                new org.egov.pgrrest.read.web.contract.ComplaintStatus(complaintStatus);

        assertThat(complaintStatusContract.getId()).isEqualTo(1L);
        assertThat(complaintStatusContract.getName()).isEqualTo("REGISTERED");
    }
}
