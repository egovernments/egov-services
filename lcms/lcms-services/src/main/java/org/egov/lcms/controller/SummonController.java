package org.egov.lcms.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lcms.factory.ResponseFactory;
import org.egov.lcms.models.CaseRequest;
import org.egov.lcms.models.CaseResponse;
import org.egov.lcms.models.CaseSearchCriteria;
import org.egov.lcms.models.SummonRequest;
import org.egov.lcms.models.SummonResponse;
import org.egov.lcms.service.SummonService;
import org.egov.lcms.util.UniqueCodeGeneration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;;

@RestController
@RequestMapping("/test/")
public class SummonController {

	@Autowired
	SummonService summonService;

	@Autowired
	ResponseFactory responseInfoFactory;

	@Autowired
	UniqueCodeGeneration UniqueCodeGeneration;

	@RequestMapping(path = "summmon/_create", method = RequestMethod.POST)
	public ResponseEntity<?> createSummon(@RequestBody @Valid SummonRequest summonRequest) throws Exception {
		SummonResponse summonResponse = summonService.createSummon(summonRequest);
		return new ResponseEntity<>(summonResponse, HttpStatus.CREATED);
	}

	@RequestMapping(path = "/_registration", method = RequestMethod.POST)
	public ResponseEntity<?> updateSummon(@RequestBody @Valid CaseRequest caseRequest) throws Exception {
		CaseResponse summonResponse = summonService.createCase(caseRequest);
		return new ResponseEntity<>(summonResponse, HttpStatus.CREATED);

	}

	@RequestMapping(path = "/_assignadvocate", method = RequestMethod.POST)
	public ResponseEntity<?> assignAdvocate(@RequestBody @Valid CaseRequest caseRequest) throws Exception {
		CaseResponse caseResponse = summonService.assignAdvocate(caseRequest);
		return new ResponseEntity<>(caseResponse, HttpStatus.CREATED);

	}

	@RequestMapping(path = "/case/_search", method = RequestMethod.POST)
	public ResponseEntity<?> assignAdvocate(@ModelAttribute @Valid CaseSearchCriteria caseSearchCriteria,
			@RequestBody RequestInfo requestInfo) throws Exception {
		CaseResponse caseResponse = summonService.caseSearch(caseSearchCriteria, requestInfo);
		return new ResponseEntity<>(caseResponse, HttpStatus.CREATED);

	}

	@RequestMapping(path = "/_Vakalatnamageneration", method = RequestMethod.POST)
	public ResponseEntity<?> createVakalatnama(@RequestBody @Valid CaseRequest caseRequest) throws Exception {
		CaseResponse caseResponse = summonService.createVakalatnama(caseRequest);
		return new ResponseEntity<>(caseResponse, HttpStatus.CREATED);

	}

	@RequestMapping(path = "/parawisecomment/_create", method = RequestMethod.POST)
	public ResponseEntity<?> createParaWiseComment(@RequestBody @Valid CaseRequest caseRequest) throws Exception {
		CaseResponse caseResponse = summonService.createParaWiseComment(caseRequest);
		return new ResponseEntity<>(caseResponse, HttpStatus.CREATED);

	}

	@RequestMapping(path = "/parawisecomment/_update", method = RequestMethod.POST)
	public ResponseEntity<?> updateParaWiseComment(@RequestBody @Valid CaseRequest caseRequest) throws Exception {
		CaseResponse caseResponse = summonService.updateParaWiseComment(caseRequest);
		return new ResponseEntity<>(caseResponse, HttpStatus.CREATED);

	}

	@RequestMapping(path = "/hearingdetails/_create", method = RequestMethod.POST)
	public ResponseEntity<?> createHearingDetails(@RequestBody @Valid CaseRequest caseRequest) throws Exception {
		CaseResponse caseResponse = summonService.createHearingDetails(caseRequest);
		return new ResponseEntity<>(caseResponse, HttpStatus.CREATED);

	}

	@RequestMapping(path = "/hearingdetails/_update", method = RequestMethod.POST)
	public ResponseEntity<?> updateHearingDetails(@RequestBody @Valid CaseRequest caseRequest) throws Exception {
		CaseResponse caseResponse = summonService.createHearingDetails(caseRequest);
		return new ResponseEntity<>(caseResponse, HttpStatus.CREATED);

	}

}
