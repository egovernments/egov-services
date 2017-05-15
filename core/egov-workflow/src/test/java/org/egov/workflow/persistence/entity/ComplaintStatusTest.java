package org.egov.workflow.persistence.entity;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ComplaintStatusTest {

    @Test
    public void test_should_map_from_entity_to_domain() {
        ComplaintStatus complaintStatus = new ComplaintStatus(1L, "name","default","0001");

        org.egov.workflow.domain.model.ComplaintStatus complaintStatusModel = complaintStatus.toDomain();

        assertThat(complaintStatusModel.getId()).isEqualTo(1);
        assertThat(complaintStatusModel.getName()).isEqualTo("name");
    }
}
