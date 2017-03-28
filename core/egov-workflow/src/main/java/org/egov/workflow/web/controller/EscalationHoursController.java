package org.egov.workflow.web.controller;

import org.egov.workflow.domain.model.EscalationHoursSearchCriteria;
import org.egov.workflow.domain.service.EscalationService;
import org.egov.workflow.web.contract.EscalationHoursResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EscalationHoursController {

    private EscalationService escalationService;

    public EscalationHoursController(EscalationService escalationService) {
        this.escalationService = escalationService;
    }

    @PostMapping("/escalation-hours/_search")
    public EscalationHoursResponse getEscalationDate(@RequestParam("tenantId") String tenantId,
                                                     @RequestParam("designationId") Long designationId,
                                                     @RequestParam("complaintTypeId") Long complaintTypeId) {
        final EscalationHoursSearchCriteria searchCriteria = EscalationHoursSearchCriteria.builder()
            .tenantId(tenantId)
            .designationId(designationId)
            .complaintTypeId(complaintTypeId)
            .build();
        final int escalationHours = escalationService.getEscalationHours(searchCriteria);
        return new EscalationHoursResponse(escalationHours);
    }
}
