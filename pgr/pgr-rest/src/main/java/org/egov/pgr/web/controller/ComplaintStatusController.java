package org.egov.pgr.web.controller;

import java.util.List;

import org.egov.pgr.domain.service.ComplaintStatusService;
import org.egov.pgr.persistence.entity.ComplaintStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ComplaintStatusController {
	@Autowired
	private ComplaintStatusService complaintStatusService;

	@PostMapping("/_statuses")
	public List<ComplaintStatus> getAllStatus(@RequestParam final String tenantId) {
		return complaintStatusService.getAllComplaintStatus();
	}

}
