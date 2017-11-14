package org.egov.web.controller;

import org.egov.web.contract.ComplaintRegistrationNumber;
import org.egov.domain.service.CrnGeneratorService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crn")
public class CrnController {

    private CrnGeneratorService crnGeneratorService;

    public CrnController(CrnGeneratorService crnGeneratorService) {
        this.crnGeneratorService = crnGeneratorService;
    }

    @GetMapping()
    public ComplaintRegistrationNumber getCrn(@RequestParam(value = "tenantId") final String tenantId) {
        return new ComplaintRegistrationNumber(crnGeneratorService.generate(tenantId));
    }

    @PostMapping("/v1/_create")
    public ComplaintRegistrationNumber createCrn(@RequestParam(value = "tenantId")  final String tenantId) {
        return new ComplaintRegistrationNumber(crnGeneratorService.generate(tenantId));
    }
}