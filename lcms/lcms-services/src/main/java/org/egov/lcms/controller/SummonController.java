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

/** 
* 
* Author		Date			eGov-JIRA ticket	Commit message
* ---------------------------------------------------------------------------
* Prasad		26th Oct 2107						Initial commit for Summon Controller Api 
* Prasad		26th Oct 2017						Added hearing details, parawise comments, vakaltanama endpoints
* prasad		01st Dec 2017						Summon update implementation
* Narendra		02nd Dec 2017						Added case request and case response for update summon
* 
*/
@RestController
@RequestMapping("/legalcase/")
public class SummonController {

	@Autowired
	SummonService summonService;

	@Autowired
	ResponseFactory responseInfoFactory;

	@Autowired
	UniqueCodeGeneration UniqueCodeGeneration;
	
	/**
	 * API for summon create
	 * 
	 * @param summonRequest
	 * @return SummonResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "summon/_create", method = RequestMethod.POST)
	public ResponseEntity<?> createSummon(@RequestBody @Valid SummonRequest summonRequest) throws Exception {
		SummonResponse summonResponse = summonService.createSummon(summonRequest);
		return new ResponseEntity<>(summonResponse, HttpStatus.CREATED);
	}
	
	/**
	 * API for summon update
	 * 
	 * @param caseRequest
	 * @return CaseResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "summon/_update", method = RequestMethod.POST)
	public ResponseEntity<?> updateSummon(@RequestBody @Valid CaseRequest caseRequest) throws Exception {
		CaseResponse caseResponse = summonService.updateSummon(caseRequest);
		return new ResponseEntity<>(caseResponse, HttpStatus.CREATED);
	}

	/**
	 * API for assign advocate
	 * @param caseRequest
	 * @return CaseResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/summon/_assignadvocate", method = RequestMethod.POST)
	public ResponseEntity<?> assignAdvocate(@RequestBody @Valid CaseRequest caseRequest) throws Exception {
		CaseResponse caseResponse = summonService.assignAdvocate(caseRequest);
		return new ResponseEntity<>(caseResponse, HttpStatus.CREATED);

	}

}
