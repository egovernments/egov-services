package org.egov.lcms.controller;

import javax.validation.Valid;

import org.egov.lcms.models.OpinionRequest;
import org.egov.lcms.models.OpinionSearchCriteria;
import org.egov.lcms.models.RequestInfoWrapper;
import org.egov.lcms.service.OpinionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/** 
* 
* Author		Date			eGov-JIRA ticket	Commit message
* ---------------------------------------------------------------------------
* Veswanth		26th Oct 2017						Initial commit for Opinion Api 
* Veswanth		28th Oct 2017						Added opinion tag for RequestMapping path
* Veswanth		03rd Nov 2017						Changed requestInfo to requestInfoWrapper
*/
@RestController
@RequestMapping(path = "/legalcase")
public class OpinonController {

	@Autowired
	OpinionService opinionService;

	/**
	 * API for opinion create
	 * 
	 * @param opinionRequest
	 * @return opinionResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/opinion/_create", method = RequestMethod.POST)
	public ResponseEntity<?> createOpinion(@RequestBody @Valid OpinionRequest opinionRequest) throws Exception {
		return new ResponseEntity<>(opinionService.createOpinion(opinionRequest), HttpStatus.OK);
	}

	/**
	 * Update API for opinion service
	 * 
	 * @param opinionRequest
	 * @return opinionResponse
	 */
	@RequestMapping(path = "/opinion/_update", method = RequestMethod.POST)
	public ResponseEntity<?> updateOpinion(@RequestBody @Valid OpinionRequest opinionRequest) {
		return new ResponseEntity<>(opinionService.updateOpinion(opinionRequest), HttpStatus.OK);
	}

	/**
	 * Search API for opinion service.
	 * 
	 * @param requestInfoWrapper
	 * @param opinionSearchCriteria
	 * @return opinionResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/opinion/_search", method = RequestMethod.POST)
	public ResponseEntity<?> searchOpinion(@RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			@Valid @ModelAttribute OpinionSearchCriteria opinionSearchCriteria) throws Exception {
		return new ResponseEntity<>(opinionService.searchOpinion(requestInfoWrapper, opinionSearchCriteria),
				HttpStatus.OK);
	}
}