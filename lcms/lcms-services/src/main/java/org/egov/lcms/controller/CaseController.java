package org.egov.lcms.controller;

import javax.validation.Valid;

import org.egov.lcms.models.CaseRequest;
import org.egov.lcms.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author Veswanth
 *
 */
@RestController
@RequestMapping(path = "/legalcase")
public class CaseController {

	@Autowired
	CaseService caseService;

	/**
	 * Create method for ParaWise API service
	 * 
	 * @param caseRequest
	 * @return caseResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/parawisecomment/_create", method = RequestMethod.POST)
	public ResponseEntity<?> createParaWiseComment(@RequestBody @Valid CaseRequest caseRequest) throws Exception {
		return new ResponseEntity<>(caseService.createParaWiseComment(caseRequest), HttpStatus.OK);
	}

	/**
	 * Update method for ParaWise API service
	 * 
	 * @param caseRequest
	 * @return caseResponse
	 */
	@RequestMapping(path = "/parawisecomment/_update", method = RequestMethod.POST)
	public ResponseEntity<?> updateOpinion(@RequestBody @Valid CaseRequest caseRequest) {
		return new ResponseEntity<>(caseService.updateParaWiseComment(caseRequest), HttpStatus.OK);
	}
}
