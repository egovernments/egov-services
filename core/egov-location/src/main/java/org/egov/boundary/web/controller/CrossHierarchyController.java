package org.egov.boundary.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.boundary.domain.service.CrossHierarchyService;
import org.egov.boundary.persistence.entity.CrossHierarchy;
import org.egov.boundary.web.contract.CrossHierarchyRequest;
import org.egov.boundary.web.contract.CrossHierarchyResponse;
import org.egov.boundary.web.contract.Error;
import org.egov.boundary.web.contract.ErrorResponse;
import org.egov.boundary.web.contract.RequestInfo;
import org.egov.boundary.web.contract.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crosshierarchys")
public class CrossHierarchyController {
	@Autowired
	private CrossHierarchyService crossHierarchyService;

	@PostMapping
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid CrossHierarchyRequest crossHierarchyRequest,
			BindingResult errors) {

		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
		}
		RequestInfo requestInfo = crossHierarchyRequest.getRequestInfo();
		CrossHierarchy crossHierarchy = crossHierarchyService.create(crossHierarchyRequest.getCrossHierarchy());

		CrossHierarchyResponse crossHierarchyResponse = new CrossHierarchyResponse();
		crossHierarchyResponse.getCrossHierarchys().add(crossHierarchy);

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.CREATED.toString());
		responseInfo.setApi_id(requestInfo.getApi_id());
		crossHierarchyResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<CrossHierarchyResponse>(crossHierarchyResponse, HttpStatus.CREATED);
	}

	@PutMapping(value = "/{code}")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody @Valid CrossHierarchyRequest crossHierarchyRequest,
			BindingResult errors, @PathVariable String code) {

		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
		}
		RequestInfo requestInfo = crossHierarchyRequest.getRequestInfo();
		CrossHierarchy crossHierarchyFromDb = crossHierarchyService.findByCode(code);
		CrossHierarchy crossHierarchy = crossHierarchyRequest.getCrossHierarchy();
		crossHierarchy.setId(crossHierarchyFromDb.getId());
		crossHierarchy.setVersion(crossHierarchyFromDb.getVersion());
		crossHierarchy = crossHierarchyService.update(crossHierarchy);

		CrossHierarchyResponse crossHierarchyResponse = new CrossHierarchyResponse();
		crossHierarchyResponse.getCrossHierarchys().add(crossHierarchy);

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.CREATED.toString());
		responseInfo.setApi_id(requestInfo.getApi_id());
		crossHierarchyResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<CrossHierarchyResponse>(crossHierarchyResponse, HttpStatus.CREATED);
	}

	@GetMapping
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute CrossHierarchyRequest crossHierarchyRequest) {

		CrossHierarchyResponse crossHierarchyResponse = new CrossHierarchyResponse();
		List<CrossHierarchy> allCrossHierarchys = crossHierarchyService.getAllCrossHierarchys(crossHierarchyRequest);
		crossHierarchyResponse.getCrossHierarchys().addAll(allCrossHierarchys);
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.OK.toString());
		// responseInfo.setApi_id(body.getRequestInfo().getApi_id());
		crossHierarchyResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<CrossHierarchyResponse>(crossHierarchyResponse, HttpStatus.OK);
	}

	private ErrorResponse populateErrors(BindingResult errors) {
		ErrorResponse errRes = new ErrorResponse();

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		responseInfo.setApi_id("");
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();
		error.setCode(1);
		error.setDescription("Error while binding request");
		if (errors.hasFieldErrors()) {
			for (FieldError errs : errors.getFieldErrors()) {
				error.getFilelds().add(errs.getField());
				error.getFilelds().add(errs.getRejectedValue());
			}
		}
		errRes.setError(error);
		return errRes;
	}

}