package org.egov.lams.services.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.response.ErrorResponse;
import org.egov.lams.common.web.contract.LandPossessionResponse;
import org.egov.lams.common.web.contract.LandTransferRequest;
import org.egov.lams.common.web.contract.LandTransferResponse;
import org.egov.lams.common.web.contract.LandTransferSearchCriteria;
import org.egov.lams.common.web.request.RequestInfoWrapper;
import org.egov.lams.services.factory.ResponseFactory;
import org.egov.lams.services.service.LandTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/transfer")
@Slf4j
@Component
public class LandTransferController {
	@Autowired
	private LandTransferService landTransferService;

	@PostMapping("_create")
	public ResponseEntity<?> create(@RequestBody @Valid final LandTransferRequest landTransferRequest) {
		
		return new ResponseEntity<>(landTransferService.create(landTransferRequest),HttpStatus.CREATED);
	}
	
	@PostMapping("_update")
	public ResponseEntity<?> update(@RequestBody @Valid final LandTransferRequest landTransferRequest) {

		return new ResponseEntity<>(landTransferService.update(landTransferRequest), HttpStatus.OK);
	}
	
	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid final LandTransferSearchCriteria landTransferSearchCriteria) {

		final LandTransferResponse landAcquisitionResponse = landTransferService
				.search(landTransferSearchCriteria, requestInfoWrapper.getRequestInfo());
		return new ResponseEntity<>(landAcquisitionResponse, HttpStatus.OK);
	}

}
