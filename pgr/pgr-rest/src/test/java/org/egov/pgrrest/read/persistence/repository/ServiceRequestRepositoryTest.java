package org.egov.pgrrest.read.persistence.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pgrrest.common.contract.web.ServiceRequest;
import org.egov.pgrrest.common.contract.web.SevaRequest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ServiceRequestRepositoryTest {

    @Mock
    private ServiceRequestMessageQueueRepository serviceRequestMessageQueueRepository;

    @Mock
    private SubmissionRepository submissionRepository;

    private ServiceRequestESRepository serviceRequestESRepository;

    @Captor
    private ArgumentCaptor<SevaRequest> sevaRequestArgumentCaptor;

    private ServiceRequestRepository complaintRepository;

    private ServiceRequestTypeRepository serviceRequestTypeRepository;

    private SubmissionAttributeRepository submissionAttributeRepository;

    @Before
    public void setUp() throws Exception {
        complaintRepository =
            new ServiceRequestRepository(serviceRequestMessageQueueRepository, submissionRepository,
                serviceRequestESRepository, serviceRequestTypeRepository, submissionAttributeRepository);
    }

    @Test
    @Ignore
    public void test_complaint_repository_should_call_jpa_repository_to_save_the_complaint() {
        RequestInfo requestInfo = new RequestInfo();
        ServiceRequest serviceRequest = new ServiceRequest();
        SevaRequest sevaRequest = new SevaRequest(requestInfo, serviceRequest);

        complaintRepository.save(sevaRequest);

        verify(serviceRequestMessageQueueRepository).save(sevaRequestArgumentCaptor.capture());

        SevaRequest actual = sevaRequestArgumentCaptor.getValue();
        assertThat(actual.getRequestInfo().getAction()).isEqualTo("POST");
        assertThat(actual.getServiceRequest().getCreatedDate()).isNotNull();
    }

    @Test
    public void test_complaint_repository_should_call_jpa_repository_to_update_the_complaint() {
        RequestInfo requestInfo = new RequestInfo();
        ServiceRequest serviceRequest = new ServiceRequest();
        SevaRequest sevaRequest = new SevaRequest(requestInfo, serviceRequest);

        complaintRepository.update(sevaRequest);

        verify(serviceRequestMessageQueueRepository).save(sevaRequestArgumentCaptor.capture());
        SevaRequest actual = sevaRequestArgumentCaptor.getValue();
        assertThat(actual.getRequestInfo().getAction()).isEqualTo("PUT");
        assertThat(actual.getServiceRequest().getLastModifiedDate()).isNotNull();
    }

}
