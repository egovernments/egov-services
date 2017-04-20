package org.egov.pgr.read.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.egov.pgr.read.domain.service.ComplaintStatusMappingService;
import org.egov.pgr.read.web.contract.ComplaintStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ComplaintStatusMappingController {

    private ComplaintStatusMappingService complaintStatusMappingService;

    public ComplaintStatusMappingController(ComplaintStatusMappingService complaintStatusMappingService) {
        this.complaintStatusMappingService = complaintStatusMappingService;
    }

    @PostMapping("/_getnextstatuses")
    public List<ComplaintStatus> getNextStatusesByCurrentStatusAndRole(@RequestParam final Long userId,
            @RequestParam final String currentStatus,
            @RequestParam(value = "tenantId", required = true) final String tenantId) {
        return complaintStatusMappingService.getStatusByRoleAndCurrentStatus(userId, currentStatus, tenantId).stream()
                .map(ComplaintStatus::new).collect(Collectors.toList());
    }

}
