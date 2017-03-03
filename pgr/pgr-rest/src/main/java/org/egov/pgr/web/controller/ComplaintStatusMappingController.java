package org.egov.pgr.web.controller;

import java.util.List;

import org.egov.pgr.domain.service.ComplaintStatusMappingService;
import org.egov.pgr.persistence.entity.ComplaintStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ComplaintStatusMappingController {

    @Autowired
    private ComplaintStatusMappingService complaintStatusMappingService;

    @PostMapping("/_getnextstatuses")
    public List<ComplaintStatus> getNextStatusesByCurrentStatusAndRole(@RequestParam final Long userId,
            @RequestParam final String currentStatus, @RequestParam final String tenantId) {
        return complaintStatusMappingService.getStatusByRoleAndCurrentStatus(userId, currentStatus, tenantId);
    }

}
