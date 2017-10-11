package org.egov.mr.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.mr.model.MarriageRegn;
import org.egov.mr.model.Page;
import org.egov.mr.service.MarriageRegnService;
import org.egov.mr.web.contract.MarriageRegnCriteria;
import org.egov.mr.web.contract.MarriageRegnRequest;
import org.egov.mr.web.contract.MarriageRegnResponse;
import org.egov.mr.web.contract.RequestInfoWrapper;
import org.egov.mr.web.contract.ResponseInfoFactory;
import org.egov.mr.web.errorhandler.ErrorHandler;
import org.egov.mr.web.validator.MarriageRegnValidator;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/marriageRegns/appl/")
public class MarriageRegnController {

	@Autowired
	private MarriageRegnService marriageRegnService;
	
	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	
	@Autowired
	private ErrorHandler errorHandler;
	
	@Autowired
	private MarriageRegnValidator marriageRegnValidator;
	
	@PostMapping("/_search")
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute @Valid MarriageRegnCriteria marriageRegnCriteria,
			BindingResult modelAttributeBindingResult, @RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			BindingResult requestBodyBindingResult) {
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		if (modelAttributeBindingResult.hasErrors())
			return new ResponseEntity<>(errorHandler.getErrorResponse(modelAttributeBindingResult, requestInfo),
					HttpStatus.BAD_REQUEST);
		
		List<MarriageRegn> marriageRegnList = null;
		try {
			marriageRegnList = marriageRegnService.getMarriageRegns(marriageRegnCriteria, requestInfo);
			
		} catch (Exception exception) {
			log.error("Error while processing request " + marriageRegnCriteria, exception);
			//return errorHandler.getResponseEntityForUnexpectedErrors(requestInfo);
		}

		return getSuccessResponseForSearch(marriageRegnList, requestInfo);
	}
	
	@PostMapping(value = "/_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid MarriageRegnRequest marriageRegnRequest,
			BindingResult bindingResult) {
		log.info("marriageRegnRequest::" + marriageRegnRequest);
		RequestInfo requestInfo = marriageRegnRequest.getRequestInfo();

		//marriageRegnValidator.validate(marriageRegnRequest, bindingResult);
		if (bindingResult.hasErrors())
			return new ResponseEntity<>(errorHandler.getErrorResponse(bindingResult, requestInfo),
					HttpStatus.BAD_REQUEST);

		MarriageRegn marriageRegns = null;
		try {
			marriageRegns = marriageRegnService.createAsync(marriageRegnRequest);
		} catch (Exception exception) {
			log.error("Error while processing request ", exception);
		}
		return getSuccessResponseForCreate(marriageRegns, marriageRegnRequest.getRequestInfo());
	}

	@PostMapping(value = "/_update")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody @Valid MarriageRegnRequest marriageRegnRequest, BindingResult bindingResult) {
		log.debug("marriageRegnRequest::" + marriageRegnRequest);

		//esponseEntity<?> errorResponseEntity = validateMarriageRegnRequest(marriageRegnRequest, bindingResult, false);
		RequestInfo requestInfo = marriageRegnRequest.getRequestInfo();
		
		ResponseEntity<?> errorResponseEntity = errorHandler.handleBindingErrorsForCreate(requestInfo,
				bindingResult);

		MarriageRegn marriageRegns = null;
		try {
			marriageRegns = marriageRegnService.updateAsync(marriageRegnRequest);
		} catch (Exception exception) {
			log.error("Error while processing request ", exception);
			//return errorHandler.getResponseEntityForUnexpectedErrors(marriageRegnRequest.getRequestInfo());
		}
		return getSuccessResponseForUpdate(marriageRegns, marriageRegnRequest.getRequestInfo());
	}

	private ResponseEntity<?> getSuccessResponseForUpdate(MarriageRegn marriageRegns, RequestInfo requestInfo) {
		return getSuccessResponseForCreate(marriageRegns, requestInfo);
	}

	private ResponseEntity<?> getSuccessResponseForCreate(MarriageRegn marriageRegn, RequestInfo requestInfo) {
			MarriageRegnResponse marriageRegnResponse = new MarriageRegnResponse();
			List<MarriageRegn> marriageRegns = new ArrayList<MarriageRegn>();
			marriageRegns.add(marriageRegn);
			marriageRegnResponse.setMarriageRegns(marriageRegns);
			Page page=new Page();
			marriageRegnResponse.setPage(page);
		
			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
			System.err.println("::::::responseInfo:::::"+responseInfo);
			responseInfo.setStatus(HttpStatus.OK.toString());
			marriageRegnResponse.setResponseInfo(responseInfo);
			return new ResponseEntity<MarriageRegnResponse>(marriageRegnResponse, HttpStatus.OK);
		}
		

	private ResponseEntity<?> getSuccessResponseForSearch(List<MarriageRegn> marriageRegnList, RequestInfo requestInfo) {
		MarriageRegnResponse marriageRegnResponse = new MarriageRegnResponse();
		marriageRegnResponse.setMarriageRegns(marriageRegnList);
		System.out.println("marriageRegnList=" + marriageRegnList);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		marriageRegnResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<MarriageRegnResponse>(marriageRegnResponse, HttpStatus.OK);
	}
}
