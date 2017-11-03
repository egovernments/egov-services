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
 * Controller class for opinion service
 * 
 * @author Veswanth
 *
 */
@RestController
@RequestMapping(path = "/legalcase")
public class OpinonController {

	@Autowired
	OpinionService opinionService;

	/**
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
	 * Update method for opinion service
	 * 
	 * @param opinionRequest
	 * @return opinionResponse
	 */
	@RequestMapping(path = "/opinion/_update", method = RequestMethod.POST)
	public ResponseEntity<?> updateOpinion(@RequestBody @Valid OpinionRequest opinionRequest) {
		return new ResponseEntity<>(opinionService.updateOpinion(opinionRequest), HttpStatus.OK);
	}

	/**
	 * Search method for opinion service.
	 * 
	 * @param requestInfoWrapper
	 * @param opinionSearchCriteria
	 * @return opinionResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/opinion/_search", method = RequestMethod.POST)
	public ResponseEntity<?> searchOpinion(@RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			@Valid @ModelAttribute OpinionSearchCriteria opinionSearchCriteria) throws Exception {
		return new ResponseEntity<>(
				opinionService.searchOpinion(requestInfoWrapper, opinionSearchCriteria),
				HttpStatus.OK);
	}
}