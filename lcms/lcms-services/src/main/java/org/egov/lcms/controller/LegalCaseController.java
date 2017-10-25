package org.egov.lcms.controller;

import javax.validation.Valid;

import org.egov.lcms.models.LegalCaseRequest;
import org.egov.lcms.models.LegalCaseResponse;
import org.egov.lcms.service.LegalcaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/")
public class LegalCaseController {

	@Autowired
	LegalcaseService legalCaseService;

	@RequestMapping(path = "legalcase/_create")
	public LegalCaseResponse createLegalCase(@RequestBody @Valid LegalCaseRequest legalcaseReuqest) {
		return legalCaseService.createLegalCase(legalcaseReuqest);

	}

}
