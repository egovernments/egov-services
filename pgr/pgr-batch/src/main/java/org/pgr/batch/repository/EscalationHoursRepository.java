package org.pgr.batch.repository;

import org.pgr.batch.repository.contract.EscalationHoursRequest;
import org.pgr.batch.repository.contract.EscalationHoursResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
public class EscalationHoursRepository {
    private final RestTemplate restTemplate;
    private final String url;


    public EscalationHoursRepository(RestTemplate restTemplate,
                                     @Value("${egov.services.workflow.hostname}") String host,
                                     @Value("${egov.services.workflow.escalationhours}") String endpointUrl) {
        this.restTemplate = restTemplate;
        this.url = host + endpointUrl;
    }

    public int getEscalationHours(String tenantId, String complaintTypeId, String designationId) {
        final EscalationHoursRequest request = new EscalationHoursRequest(null);
        final HashMap<String, String> uriVariables = new HashMap<>();
        uriVariables.put("tenantId", tenantId);
        uriVariables.put("complaintTypeId", complaintTypeId);
        uriVariables.put("designationId", designationId);
        final EscalationHoursResponse escalationHoursResponse = this.restTemplate
            .postForObject(this.url, request, EscalationHoursResponse.class, uriVariables);
        return Math.toIntExact(escalationHoursResponse.getHours());
    }
}
