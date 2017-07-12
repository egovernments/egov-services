package org.egov.pgrrest.write.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.pgrrest.write.consumer.contracts.request.SevaRequestMapFactory;
import org.egov.pgrrest.write.domain.model.ServiceRequestRecord;
import org.egov.pgrrest.write.domain.service.ServiceRequestWriteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.TimeZone;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GrievancePersistenceListenerTest {

    private static final String IST = "Asia/Calcutta";
    @Mock
    private ServiceRequestWriteService complaintWriteService;

    @Test
    public void test_should_persist_compliant() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setTimeZone(TimeZone.getTimeZone(IST));
        final GrievancePersistenceListener listener =
            new GrievancePersistenceListener(complaintWriteService, objectMapper);
        final HashMap<String, Object> sevaRequestMap = SevaRequestMapFactory.create();

        listener.processMessage(sevaRequestMap);

        verify(complaintWriteService).updateOrInsert(any(ServiceRequestRecord.class));
    }

}