package org.egov.pgr.read.web.controller;


import java.util.List;
import java.util.stream.Collectors;
import org.egov.pgr.common.entity.ReceivingMode;
import org.egov.pgr.read.domain.service.ReceivingModeService;
import org.egov.pgr.read.web.contract.ReceivingModeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/receivingmode")
public class ReceivingModeController {

	@Autowired
	private ReceivingModeService receivingModeService;

	@PostMapping
	public ReceivingModeResponse getAll(@RequestParam(value = "tenantId",defaultValue="default") final String tenantId) {
	
		List<ReceivingMode> receivingModes  = receivingModeService.getAllReceivingModes(tenantId);
		List<org.egov.pgr.read.web.contract.ReceivingMode> receivingMode = receivingModes.stream()
				.map(org.egov.pgr.read.web.contract.ReceivingMode::new).collect(Collectors.toList());

		return new org.egov.pgr.read.web.contract.ReceivingModeResponse(null,receivingMode);

	}
}