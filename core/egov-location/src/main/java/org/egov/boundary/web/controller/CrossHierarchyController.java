package org.egov.boundary.web.controller;

import org.egov.boundary.domain.service.CrossHierarchyService;
import org.egov.boundary.persistence.entity.CrossHierarchy;
import org.egov.boundary.web.contract.CrossHierarchyRequest;
import org.egov.boundary.web.contract.CrossHierarchyResponse;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
		responseInfo.setApiId(requestInfo.getApiId());
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
		responseInfo.setApiId(requestInfo.getApiId());
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
		responseInfo.setApiId("");
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();
		error.setCode(1);
		error.setDescription("Error while binding request");
		if (errors.hasFieldErrors()) {
			for (FieldError errs : errors.getFieldErrors()) {
				error.getFields().add(new ErrorField(errs.getCode(), errs.getDefaultMessage(), errs.getObjectName()));
			}
		}
		errRes.setError(error);
		return errRes;
	}

}