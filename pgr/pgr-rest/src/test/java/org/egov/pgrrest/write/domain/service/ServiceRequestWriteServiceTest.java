package org.egov.pgrrest.write.domain.service;

import org.egov.pgrrest.write.domain.model.ServiceRequestRecord;
import org.egov.pgrrest.write.persistence.repository.ServiceRequestWriteRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ServiceRequestWriteServiceTest {

    @InjectMocks
    private ServiceRequestWriteService complaintWriteService;

    @Mock
    private ServiceRequestWriteRepository serviceRequestWriteRepository;

    @Test
    public void test_should_persist_complaint_record() {
        final ServiceRequestRecord serviceRequestRecord = ServiceRequestRecord.builder().build();

        complaintWriteService.updateOrInsert(serviceRequestRecord);

        verify(serviceRequestWriteRepository).updateOrInsert(serviceRequestRecord);
    }


}