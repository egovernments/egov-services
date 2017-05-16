package org.egov.workflow.web.controller;

import org.egov.common.contract.request.Role;
import org.egov.workflow.domain.model.ComplaintStatusSearchCriteria;
import org.egov.workflow.domain.model.KeywordStatusMappingSearchCriteria;
import org.egov.workflow.domain.service.ComplaintStatusService;
import org.egov.workflow.domain.service.KeywordStatusMappingService;
import org.egov.workflow.web.contract.ComplaintStatusRequest;
import org.egov.workflow.web.contract.ServiceStatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
public class ServiceStatusController {
    private ComplaintStatusService complaintStatusService;
    private KeywordStatusMappingService keywordStatusMappingService;

    @Autowired
    public ServiceStatusController(ComplaintStatusService complaintStatusService, KeywordStatusMappingService keywordStatusMappingService) {
        this.complaintStatusService = complaintStatusService;
        this.keywordStatusMappingService = keywordStatusMappingService;
    }

    @PostMapping("/statuses/_search")
    public ServiceStatusResponse getAllStatus(@RequestBody final ComplaintStatusRequest request,
                                              @RequestParam(defaultValue = "Complaint") final String keyword,
                                              @RequestParam(defaultValue = "default") final String tenantId) {

        KeywordStatusMappingSearchCriteria searchCriteria = KeywordStatusMappingSearchCriteria.builder()
            .keyword(keyword)
            .tenantId(tenantId)
            .build();

        List<org.egov.workflow.domain.model.ComplaintStatus> domainStatuses = keywordStatusMappingService
            .getStatusForKeyword(searchCriteria);

        return new ServiceStatusResponse(null, domainStatuses);
    }

    @PostMapping("/nextstatuses/_search")
    public ServiceStatusResponse getNextStatuses(@RequestBody final ComplaintStatusRequest request,
                                                 @RequestParam final String currentStatusCode,
                                                 @RequestParam(defaultValue = "default") final String tenantId) {
        List<Long> roles = request.getRequestInfo().getUserInfo().getRoles().stream()
            .map(Role::getId)
            .collect(Collectors.toList());

        ComplaintStatusSearchCriteria complaintStatusSearchCriteria =
            new ComplaintStatusSearchCriteria(currentStatusCode, roles,tenantId);

        List<org.egov.workflow.domain.model.ComplaintStatus> domainStatuses =  complaintStatusService
            .getNextStatuses(complaintStatusSearchCriteria);

        return new ServiceStatusResponse(null, domainStatuses);
    }
}
