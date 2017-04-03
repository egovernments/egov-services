package org.egov.pgr.read.web.controller;

import java.util.List;

import org.egov.pgr.common.entity.ComplaintStatus;
import org.egov.pgr.read.domain.service.ComplaintStatusMappingService;
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
                                                                       @RequestParam final String tenantId) {
        return complaintStatusMappingService.getStatusByRoleAndCurrentStatus(userId, currentStatus, tenantId);
    }

}
