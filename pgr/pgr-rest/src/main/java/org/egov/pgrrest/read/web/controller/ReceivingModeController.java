package org.egov.pgrrest.read.web.controller;


import java.util.List;
import java.util.stream.Collectors;


import org.egov.pgrrest.read.domain.service.ReceivingModeService;
import org.egov.pgrrest.read.web.contract.ReceivingModeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/receivingmode")
public class ReceivingModeController {

    private ReceivingModeService receivingModeService;

    @Autowired
    public ReceivingModeController(ReceivingModeService receivingModeService) {
        this.receivingModeService = receivingModeService;
    }

	@PostMapping(value="/v1/_search")
	public ReceivingModeResponse getAll(@RequestParam(value = "tenantId",defaultValue="default") final String tenantId){
	
		List<org.egov.pgrrest.common.model.ReceivingMode> receivingModes  = receivingModeService
            .getAllReceivingModes(tenantId);
		List<org.egov.pgrrest.read.web.contract.ReceivingMode> receivingMode = receivingModes.stream()
				.map(org.egov.pgrrest.read.web.contract.ReceivingMode::new).collect(Collectors.toList());
        return new ReceivingModeResponse(null,receivingMode);

	}
}