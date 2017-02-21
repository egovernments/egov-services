package org.egov.pgr.web.controller;

import java.util.List;

import org.egov.pgr.domain.service.ReceivingCenterService;
import org.egov.pgr.persistence.entity.ReceivingCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/receivingcenter")
public class ReceivingCenterController {

	@Autowired
	private ReceivingCenterService receivingCenterService;

	@GetMapping
	public List<ReceivingCenter> getAllReceivingCenters(@RequestParam String tenantId) {
		return receivingCenterService.getAllReceivingCenters();
	}

}
