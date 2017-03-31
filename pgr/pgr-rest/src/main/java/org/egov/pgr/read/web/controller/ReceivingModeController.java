package org.egov.pgr.read.web.controller;

import java.util.List;

import org.egov.pgr.read.domain.service.ReceivingModeService;
import org.egov.pgr.common.entity.ReceivingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/receivingmode")
public class ReceivingModeController {

	@Autowired
	private ReceivingModeService receivingModeService;

	@GetMapping
	public List<ReceivingMode> getAll(@RequestParam String tenantId) {
		return receivingModeService.getAllReceivingModes(tenantId);
	}

}
