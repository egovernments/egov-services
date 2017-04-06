package org.egov.workflow.domain.service;

import org.egov.workflow.domain.model.ComplaintStatus;
import org.egov.workflow.persistence.repository.ComplaintStatusRepository;
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
public class ComplaintStatusServiceTest {

    @Mock
    private ComplaintStatusRepository complaintStatusRepository;

    private ComplaintStatusService complaintStatusService;

    @Before
    public void setUp() {
        complaintStatusService = new ComplaintStatusService(complaintStatusRepository);
    }

    @Test
    public void should_find_all_statuses() {
        List<ComplaintStatus> complaintStatuses = getListOfComplaintStatusModels();

        when(complaintStatusRepository.findAll()).thenReturn(complaintStatuses);

        List<ComplaintStatus> all = complaintStatusService.findAll();

        assertThat(all).isEqualTo(complaintStatuses);
    }

    private List<ComplaintStatus> getListOfComplaintStatusModels() {
        return Arrays.asList(
                new ComplaintStatus(1L, "REGISTERED"),
                new ComplaintStatus(2L, "WITHDRAWN")
        );
    }
}