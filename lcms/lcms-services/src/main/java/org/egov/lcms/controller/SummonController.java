package org.egov.lcms.controller;

import javax.validation.Valid;
import org.egov.lcms.factory.ResponseFactory;
import org.egov.lcms.models.CaseRequest;
import org.egov.lcms.models.CaseResponse;
import org.egov.lcms.models.SummonRequest;
import org.egov.lcms.models.SummonResponse;
import org.egov.lcms.service.SummonService;
import org.egov.lcms.util.UniqueCodeGeneration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;;

@RestController
@RequestMapping("/legalcase/")
public class SummonController {

	@Autowired
	SummonService summonService;

	@Autowired
	ResponseFactory responseInfoFactory;

	@Autowired
	UniqueCodeGeneration UniqueCodeGeneration;

	@RequestMapping(path = "summon/_create", method = RequestMethod.POST)
	public ResponseEntity<?> createSummon(@RequestBody @Valid SummonRequest summonRequest) throws Exception {
		SummonResponse summonResponse = summonService.createSummon(summonRequest);
		return new ResponseEntity<>(summonResponse, HttpStatus.CREATED);
	}
	
	@RequestMapping(path = "summon/_update", method = RequestMethod.POST)
	public ResponseEntity<?> updateSummon(@RequestBody @Valid CaseRequest caseRequest) throws Exception {
		CaseResponse caseResponse = summonService.updateSummon(caseRequest);
		return new ResponseEntity<>(caseResponse, HttpStatus.CREATED);
	}

	@RequestMapping(path = "/summon/_assignadvocate", method = RequestMethod.POST)
	public ResponseEntity<?> assignAdvocate(@RequestBody @Valid CaseRequest caseRequest) throws Exception {
		CaseResponse caseResponse = summonService.assignAdvocate(caseRequest);
		return new ResponseEntity<>(caseResponse, HttpStatus.CREATED);

	}

}
