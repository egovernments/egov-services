package org.egov.lams.services.web.controller;

import javax.validation.Valid;

import org.egov.lams.common.web.contract.LandPossessionRequest;
import org.egov.lams.common.web.contract.LandPossessionSearchCriteria;
import org.egov.lams.common.web.request.RequestInfoWrapper;
import org.egov.lams.services.service.LandPossessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/possession")
public class LandPossessionController {
	@Autowired
	private LandPossessionService landPossessionService;

	@PostMapping("_create")
	public ResponseEntity<?> create(@RequestBody @Valid final LandPossessionRequest landPossessionRequest) {
		
		return new ResponseEntity<>(landPossessionService.create(landPossessionRequest),HttpStatus.CREATED);
	}
	
	@PostMapping("_update")
	public ResponseEntity<?> update(@RequestBody @Valid final LandPossessionRequest landPossessionRequest) {

		return new ResponseEntity<>(landPossessionService.update(landPossessionRequest), HttpStatus.OK);
	}
	
	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid final LandPossessionSearchCriteria landPossessionSearchCriteria) {

		return new ResponseEntity<>(
				landPossessionService.search(landPossessionSearchCriteria, requestInfoWrapper.getRequestInfo()),
				HttpStatus.OK);
	}
}
