package org.egov.pgr.read.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.egov.pgr.common.entity.ReceivingMode;
import org.egov.pgr.read.domain.service.ReceivingModeService;
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
	public List<ReceivingMode> getAll(@RequestParam(value = "tenantId", required = true) final String tenantId) {
	    if (tenantId != null && !tenantId.isEmpty())
	        return receivingModeService.getAllReceivingModes(tenantId);
	    else
	        return new ArrayList<ReceivingMode>();
	}

}
