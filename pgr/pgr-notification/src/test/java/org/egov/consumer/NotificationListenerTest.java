package org.egov.consumer;

import org.egov.domain.model.SevaRequest;
import org.egov.domain.service.NotificationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NotificationListenerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationListener notificationListener;

    @Test
    public void test_should_send_notifications() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();

        notificationListener.process(sevaRequestMap);

        verify(notificationService).notify(any(SevaRequest.class));
    }

}