package org.egov.web.controller;

import org.egov.web.contract.ComplaintRegistrationNumber;
import org.egov.domain.service.CrnGeneratorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crn")
public class CrnController {

    private CrnGeneratorService crnGeneratorService;

    public CrnController(CrnGeneratorService crnGeneratorService) {
        this.crnGeneratorService = crnGeneratorService;
    }

    @GetMapping()
    public ComplaintRegistrationNumber getCrn() {
        return new ComplaintRegistrationNumber(crnGeneratorService.generate());
    }

    @PostMapping("/v1/_create")
    public ComplaintRegistrationNumber createCrn() {
        return new ComplaintRegistrationNumber(crnGeneratorService.generate());
    }
}