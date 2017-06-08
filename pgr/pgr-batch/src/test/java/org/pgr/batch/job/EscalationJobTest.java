package org.pgr.batch.job;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.pgr.batch.service.EscalationService;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class EscalationJobTest {

    @Mock
    private EscalationService escalationService;

    @Test
    public void test_that_escalation_service_is_called() throws Exception {
        EscalationJob escalationJob = new EscalationJob(escalationService);

        escalationJob.escalate();

        verify(escalationService).escalateComplaintForAllTenants();;
    }
}