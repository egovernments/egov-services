package org.egov.pgr.read.web.controller;

import org.egov.pgr.common.entity.ComplaintStatus;
import org.egov.pgr.read.domain.service.ComplaintStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ComplaintStatusController {
	@Autowired
	private ComplaintStatusService complaintStatusService;

	@PostMapping("/_statuses")
	public List<ComplaintStatus> getAllStatus(@RequestParam(value = "tenantId", required = true) final String tenantId) {
		if(tenantId!=null && !tenantId.isEmpty())
		    return complaintStatusService.getAllComplaintStatusByTenantId(tenantId);
		else
		    return new ArrayList<ComplaintStatus>();
	}

}
