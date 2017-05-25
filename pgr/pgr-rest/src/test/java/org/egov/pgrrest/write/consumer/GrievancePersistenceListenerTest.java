package org.egov.pgrrest.write.consumer;

import org.egov.pgrrest.write.contracts.grievance.SevaRequestMapFactory;
import org.egov.pgrrest.write.model.ServiceRequestRecord;
import org.egov.pgrrest.write.service.ServiceRequestWriteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GrievancePersistenceListenerTest {

    @Mock
    private ServiceRequestWriteService complaintWriteService;

    @InjectMocks
    private GrievancePersistenceListener listener;

    @Test
    public void test_should_persist_compliant() {
        final HashMap<String, Object> sevaRequestMap = SevaRequestMapFactory.create();

        listener.processMessage(sevaRequestMap);

        verify(complaintWriteService).updateOrInsert(any(ServiceRequestRecord.class));
    }

}