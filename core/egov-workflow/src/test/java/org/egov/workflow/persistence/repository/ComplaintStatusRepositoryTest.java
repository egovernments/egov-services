package org.egov.workflow.persistence.repository;

import org.egov.workflow.persistence.entity.ComplaintStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ComplaintStatusRepositoryTest {

    @Mock
    private ComplaintStatusJpaRepository complaintStatusJpaRepository;

    private ComplaintStatusRepository complaintStatusRepository;

    @Before
    public void setUp() {
        complaintStatusRepository = new ComplaintStatusRepository(complaintStatusJpaRepository);
    }

    @Test
    public void test_should_find_all_statuses() {
        when(complaintStatusJpaRepository.findAll()).thenReturn(getListOfComplaintStatusEntities());

        List<org.egov.workflow.domain.model.ComplaintStatus> all = complaintStatusRepository.findAll();

        assertThat(all.size()).isEqualTo(2);
        assertThat(all.get(0).getId()).isEqualTo(1L);
        assertThat(all.get(1).getId()).isEqualTo(2L);

    }

    @Test
    public void test_should_find_status_by_name() {
        when(complaintStatusJpaRepository.findByName("REJECTED"))
                .thenReturn(new ComplaintStatus(1L, "REJECTED"));

        org.egov.workflow.domain.model.ComplaintStatus result = complaintStatusRepository.findByName("REJECTED");

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("REJECTED");
    }

    private List<ComplaintStatus> getListOfComplaintStatusEntities() {
        return Arrays.asList(
                new ComplaintStatus(1L, "FORWARDED"),
                new ComplaintStatus(2L, "WITHDRAWN")
        );
    }
}