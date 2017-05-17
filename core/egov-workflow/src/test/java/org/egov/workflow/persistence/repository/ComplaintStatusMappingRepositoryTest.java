package org.egov.workflow.persistence.repository;

import org.egov.workflow.persistence.entity.ComplaintStatus;
import org.egov.workflow.persistence.entity.ComplaintStatusMapping;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ComplaintStatusMappingRepositoryTest {

    @Mock
    ComplaintStatusMappingJpaRepository complaintStatusMappingJpaRepository;

    ComplaintStatusMappingRepository complaintStatusMappingRepository;

    @Before
    public void setUp() {
        complaintStatusMappingRepository = new ComplaintStatusMappingRepository(complaintStatusMappingJpaRepository);
    }

    @Test
    public void test_should_get_distinct_next_statuses() {
        Long currentStatusId = 1L;
        List<Long> roles = asList(1L, 2L);
        List<ComplaintStatusMapping> complaintStatusMappingEntities = getListOfComplaintStatusMappingEntities();

        when(
                complaintStatusMappingJpaRepository.
                    findByCurrentStatusIdAndRoleInAndTenantIdOrderByOrderNoAsc(currentStatusId, roles,"default")
        ).thenReturn(complaintStatusMappingEntities);

        List<org.egov.workflow.domain.model.ComplaintStatus> result =
                complaintStatusMappingRepository.getNextStatuses(currentStatusId, roles,"default");

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getName()).isEqualTo("PROCESSING");
        assertThat(result.get(1).getName()).isEqualTo("COMPLETED");
    }


    private List<ComplaintStatusMapping> getListOfComplaintStatusMappingEntities() {
        ComplaintStatus registered = new ComplaintStatus(1L, "REGISTERED","default","0001");
        ComplaintStatus processing = new ComplaintStatus(2L, "PROCESSING","default","0002");
        ComplaintStatus completed = new ComplaintStatus(3L, "COMPLETED","default","0003");

        ComplaintStatusMapping complaintStatusMapping1 =
                new ComplaintStatusMapping(1L, registered, processing, 1L, 1,"default");

        ComplaintStatusMapping complaintStatusMapping2 =
                new ComplaintStatusMapping(2L, processing, completed, 2L, 2,"default");

        ComplaintStatusMapping complaintStatusMapping3 =
                new ComplaintStatusMapping(3L, completed, processing, 2L, 3, "default");

        return asList(complaintStatusMapping1, complaintStatusMapping2, complaintStatusMapping3);
    }
}