package org.egov.demand.web.controller;

import java.util.List;
import javax.validation.Valid;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.demand.model.Demand;
import org.egov.demand.model.DemandCriteria;
import org.egov.demand.model.DemandDetail;
import org.egov.demand.model.DemandDetailCriteria;
import org.egov.demand.service.DemandService;
import org.egov.demand.web.contract.DemandDetailResponse;
import org.egov.demand.web.contract.DemandRequest;
import org.egov.demand.web.contract.DemandResponse;
import org.egov.demand.web.contract.RequestInfoWrapper;
import org.egov.demand.web.contract.factory.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demand")
public class DemandController {

	@Autowired
	private DemandService demandService;
	
	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	
	@PostMapping("_create")
	public ResponseEntity<?> create(@RequestBody @Valid DemandRequest demandRequest, BindingResult bindingResult) {

		RequestInfo requestInfo = demandRequest.getRequestInfo();
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(getErrorResponse(bindingResult, requestInfo), HttpStatus.BAD_REQUEST);
		}
		
		/*Demand demand = new Demand();
		demand.setConsumerCode("consumer code");
		DemandDetail demandDetail = new DemandDetail();
		demandDetail.setTaxAmount(100d);
		DemandDetail demandDetail1 = new DemandDetail();
		demandDetail1.setTaxAmount(200d);
		List<DemandDetail> demandDetails = new ArrayList<>();
		demandDetails.add(demandDetail1);
		demandDetails.add(demandDetail);
		demand.setDemandDetails(demandDetails);
		List<Demand> demands = new ArrayList<>();
		demands.add(demand);
		demandRequest.setDemands(demands);*/
		
		List<Demand> demands = demandService.create(demandRequest);
		return new ResponseEntity<>(new DemandResponse(
				responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.CREATED), demands),
				HttpStatus.CREATED);
	}

	@PostMapping("_update")
	public ResponseEntity<?> update(@RequestBody @Valid DemandRequest demandRequest, BindingResult bindingResult) {

		RequestInfo requestInfo = demandRequest.getRequestInfo();
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(getErrorResponse(bindingResult, requestInfo), HttpStatus.BAD_REQUEST);
		}
		List<Demand> demands = demandService.updateAsync(demandRequest);
		return new ResponseEntity<>(new DemandResponse(
				responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.CREATED), demands),
				HttpStatus.CREATED);
	}

	@PostMapping("_search")
	public ResponseEntity<?> search(@RequestBody RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid DemandCriteria demandCriteria, BindingResult bindingResult) {
		
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(getErrorResponse(bindingResult, requestInfo), HttpStatus.BAD_REQUEST);
		}
		List<Demand> demandList = demandService.getDemands(demandCriteria);
		return new ResponseEntity<>(new DemandResponse(
				responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.OK), demandList),
				HttpStatus.OK);
	}

	@PostMapping("/demanddetail/_search")
	public ResponseEntity<?> demandDetailSearch(@RequestBody RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid DemandDetailCriteria demandDetailCriteria, BindingResult bindingResult) {
		
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(getErrorResponse(bindingResult, requestInfo), HttpStatus.BAD_REQUEST);
		}
		List<DemandDetail> demandDetails = demandService.getDemandDetails(demandDetailCriteria);
		return new ResponseEntity<>(new DemandDetailResponse(
				responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.OK), demandDetails),
				HttpStatus.OK);
	}
	
private ErrorResponse getErrorResponse(Errors bindingResult,RequestInfo requestInfo) {
		
		Error error = new Error();
		error.setCode(400);
		error.setMessage("Mandatory Fields Null");
		error.setDescription("exception occurred in DemandController");
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			ErrorField errorField = new ErrorField(fieldError.getCode(),fieldError.getDefaultMessage(), fieldError.getField());
			error.getFields().add(errorField);
		}
		return  new ErrorResponse(responseInfoFactory.getResponseInfo(requestInfo,HttpStatus.BAD_REQUEST), error);
	}
}
