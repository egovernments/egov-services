package org.egov.lcms.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lcms.models.OpinionRequest;
import org.egov.lcms.models.OpinionSearchCriteria;
import org.egov.lcms.service.OpinionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for opinion service
 * 
 * @author Veswanth
 *
 */
@RestController
@RequestMapping(path = "/opinion/")
public class OpinonController {

	@Autowired
	OpinionService opinionService;

	/**
	 * create method for opinion service
	 * 
	 * @param opinionRequest
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path = "_create", method = RequestMethod.POST)
	public ResponseEntity<?> createOpinion(@RequestBody @Valid OpinionRequest opinionRequest) throws Exception {
		return new ResponseEntity<>(opinionService.createOpinion(opinionRequest), HttpStatus.OK);

	}

	/**
	 * Update method for opinion service
	 * 
	 * @param opinionRequest
	 * @return
	 */
	@RequestMapping(path = "_update", method = RequestMethod.POST)
	public ResponseEntity<?> updateOpinion(@RequestBody @Valid OpinionRequest opinionRequest) {
		return new ResponseEntity<>(opinionService.updateOpinion(opinionRequest), HttpStatus.OK);
	}

	/**
	 * Search method for opinion service.
	 * 
	 * @param requestInfo
	 * @param opinionSearchCriteria
	 * @return
	 */
	@RequestMapping(path = "_search", method = RequestMethod.POST)
	public ResponseEntity<?> searchOpinion(@RequestBody @Valid RequestInfo requestInfo,
			@RequestBody @Valid OpinionSearchCriteria opinionSearchCriteria) {
		return new ResponseEntity<>(opinionService.searchOpinion(requestInfo, opinionSearchCriteria), HttpStatus.OK);
	}
}