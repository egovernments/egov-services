package org.egov.commons.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.commons.model.Uom;
import org.egov.commons.service.UomService;
import org.egov.commons.web.contract.RequestInfo;
import org.egov.commons.web.contract.RequestInfoWrapper;
import org.egov.commons.web.contract.ResponseInfo;
import org.egov.commons.web.contract.UomResponse;
import org.egov.commons.web.contract.factory.ResponseInfoFactory;
import org.egov.commons.web.errorhandlers.ErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("uom")
public class UomController {

	@Autowired
	private UomService uomService;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private ErrorHandler errHandler;

	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			BindingResult requestBodyBindingResult) {
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		if (requestBodyBindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);
		}

		List<Uom> uomList = uomService.search();
		return getSuccessResponse(uomList, requestInfo);
	}

	/**
	 * Populate Response object and returnreligionsList
	 * 
	 * @param religionsList
	 * @return
	 */
	private ResponseEntity<?> getSuccessResponse(List<Uom> uomList, RequestInfo requestInfo) {
		UomResponse uomResponse = new UomResponse();
		uomResponse.setUoms(uomList);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		uomResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<UomResponse>(uomResponse, HttpStatus.OK);

	}

}
