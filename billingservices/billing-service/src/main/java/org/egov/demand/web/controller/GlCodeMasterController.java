package org.egov.demand.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.demand.model.GlCodeMasterCriteria;
import org.egov.demand.service.GlCodeMasterService;
import org.egov.demand.web.contract.GlCodeMasterResponse;
import org.egov.demand.web.contract.RequestInfoWrapper;
import org.egov.demand.web.contract.factory.ResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/glcodemasters")
public class GlCodeMasterController {

	@Autowired
	private ResponseFactory responseFactory;
	@Autowired
	private GlCodeMasterService glCodeMasterService;

	private static final Logger logger = LoggerFactory.getLogger(GlCodeMasterController.class);
	
	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid final GlCodeMasterCriteria glCodeMasterCriteria,
			final BindingResult bindingResult) {
		logger.info("glCodeMasterCriteria::" + glCodeMasterCriteria + "requestInfoWrapper::" + requestInfoWrapper);
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
		if (bindingResult.hasErrors()) {
			final ErrorResponse errorResponse = responseFactory.getErrorResponse(bindingResult, requestInfo);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		final GlCodeMasterResponse glCodeMasterResponse = glCodeMasterService.getGlCodes(glCodeMasterCriteria,
				requestInfoWrapper.getRequestInfo());
		return new ResponseEntity<>(glCodeMasterResponse, HttpStatus.OK);
	}

}
