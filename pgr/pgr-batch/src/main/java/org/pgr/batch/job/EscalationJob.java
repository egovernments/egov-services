package org.pgr.batch.job;

import org.pgr.batch.service.EscalationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class EscalationJob {

    private EscalationService escalationService;

    public EscalationJob(EscalationService escalationService) {
        this.escalationService = escalationService;
    }

    @Scheduled(fixedDelayString = "${escalation.interval}")
    public void escalate() {
        escalationService.escalateComplaintForAllTenants();
    }
}
