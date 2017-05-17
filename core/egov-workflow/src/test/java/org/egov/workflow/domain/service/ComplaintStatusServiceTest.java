package org.egov.workflow.domain.service;

import org.egov.workflow.domain.exception.InvalidComplaintStatusException;
import org.egov.workflow.domain.exception.InvalidComplaintStatusSearchException;
import org.egov.workflow.domain.model.ComplaintStatus;
import org.egov.workflow.domain.model.ComplaintStatusSearchCriteria;
import org.egov.workflow.persistence.repository.ComplaintStatusMappingRepository;
import org.egov.workflow.persistence.repository.ComplaintStatusRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ComplaintStatusServiceTest {

    @Mock
    private ComplaintStatusRepository complaintStatusRepository;

    @Mock
    private ComplaintStatusMappingRepository complaintStatusMappingRepository;

    private ComplaintStatusService complaintStatusService;

    @Before
    public void setUp() {
        complaintStatusService =
                new ComplaintStatusService(complaintStatusRepository, complaintStatusMappingRepository);
    }

    @Test
    public void test_should_find_all_statuses() {
        List<ComplaintStatus> complaintStatuses = getListOfComplaintStatusModels();

        when(complaintStatusRepository.findAll()).thenReturn(complaintStatuses);

        List<ComplaintStatus> all = complaintStatusService.findAll();

        assertThat(all).isEqualTo(complaintStatuses);
    }

    @Test
    public void test_should_find_next_statuses_given_current_status_name_and_user_roles() throws Exception {
        final String CURRENT_STATUS_NAME = "REJECTED";
        final Long CURRENT_STATUS_ID = 1L;
        final String CURRENT_STATUS_TENANTID = "default";

        ComplaintStatus currentStatus = new ComplaintStatus(CURRENT_STATUS_ID, CURRENT_STATUS_NAME,CURRENT_STATUS_TENANTID,"0001");
        List<Long> listOfRoles = asList(1L, 2L);
        List<ComplaintStatus> complaintStatuses = getListOfComplaintStatusModels();

        when(complaintStatusRepository.findByCodeAndTenantId(CURRENT_STATUS_NAME,"default")).thenReturn(currentStatus);
        when(complaintStatusMappingRepository.getNextStatuses(CURRENT_STATUS_ID, listOfRoles, "default"))
                .thenReturn(complaintStatuses);

        ComplaintStatusSearchCriteria complaintStatusSearchCriteria =
                new ComplaintStatusSearchCriteria(CURRENT_STATUS_NAME, listOfRoles, "default");

        List<ComplaintStatus> nextStatuses =
                complaintStatusService.getNextStatuses(complaintStatusSearchCriteria);

        assertThat(nextStatuses).isEqualTo(complaintStatuses);
    }

    @Test(expected = InvalidComplaintStatusSearchException.class)
    public void test_should_validate_search_criteria_before_proceeding() {
        ComplaintStatusSearchCriteria complaintStatusSearchCriteria = mock(ComplaintStatusSearchCriteria.class);
        Mockito.doThrow(new InvalidComplaintStatusSearchException(complaintStatusSearchCriteria))
                .when(complaintStatusSearchCriteria).validate();

        complaintStatusService.getNextStatuses(complaintStatusSearchCriteria);

        verify(complaintStatusSearchCriteria).validate();
    }

    @Test(expected = InvalidComplaintStatusException.class)
    public void test_find_next_statuses_should_throw_exception_when_currentstatus_is_not_found() {
        final String CURRENT_STATUS_NAME = "REJECTED";
        List<Long> listOfRoles = asList(1L, 2L);
        when(complaintStatusRepository.findByName(CURRENT_STATUS_NAME)).thenReturn(null);

        ComplaintStatusSearchCriteria complaintStatusSearchCriteria =
                new ComplaintStatusSearchCriteria(CURRENT_STATUS_NAME, listOfRoles, "default");
        List<ComplaintStatus> nextStatuses = complaintStatusService.getNextStatuses(complaintStatusSearchCriteria);

        assertThat(nextStatuses.size()).isEqualTo(0);
    }

    private List<ComplaintStatus> getListOfComplaintStatusModels() {
        return asList(
                new ComplaintStatus(1L, "REGISTERED","default","0001"),
                new ComplaintStatus(2L, "WITHDRAWN","default","0002")
        );
    }
}