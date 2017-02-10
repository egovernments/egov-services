package org.egov.boundary.web.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import javax.validation.Valid;

import org.egov.boundary.model.BoundaryType;
import org.egov.boundary.service.BoundaryTypeService;
import org.egov.boundary.web.wrapper.BoundaryTypeRequest;
import org.egov.boundary.web.wrapper.BoundaryTypeResponse;
import org.egov.boundary.web.wrapper.Error;
import org.egov.boundary.web.wrapper.ErrorResponse;
import org.egov.boundary.web.wrapper.RequestInfo;
import org.egov.boundary.web.wrapper.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/boundarytypes")
public class BoundaryTypeController {
	@Autowired
	private BoundaryTypeService boundaryTypeService;

	@PostMapping()
	public ResponseEntity<?> create(@RequestBody BoundaryTypeRequest boundaryTypeRequest, BindingResult errors) {
		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
		}
		RequestInfo requestInfo = boundaryTypeRequest.getRequestInfo();
		BoundaryType boundaryType = boundaryTypeService.createBoundaryType(boundaryTypeRequest.getBoundaryType());

		BoundaryTypeResponse boundaryTypeResponse = new BoundaryTypeResponse();
		boundaryTypeResponse.getBoundaryTypes().add(boundaryType);

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.CREATED.toString());
		responseInfo.setApi_id(requestInfo.getApi_id());
		boundaryTypeResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<BoundaryTypeResponse>(boundaryTypeResponse, HttpStatus.CREATED);
	}

	@PutMapping(value = "/{code}")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody @Valid BoundaryTypeRequest boundaryTypeRequest, BindingResult errors,
			@PathVariable String code) {

		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
		}
		RequestInfo requestInfo = boundaryTypeRequest.getRequestInfo();
		BoundaryType boundaryTypeFromDb = boundaryTypeService.findByCode(code);
		BoundaryType boundaryType = boundaryTypeRequest.getBoundaryType();
		boundaryType.setId(boundaryTypeFromDb.getId());
		boundaryType.setVersion(boundaryTypeFromDb.getVersion());
		boundaryType = boundaryTypeService.updateBoundaryType(boundaryType);

		BoundaryTypeResponse boundaryTypeResponse = new BoundaryTypeResponse();
		boundaryTypeResponse.getBoundaryTypes().add(boundaryType);

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.CREATED.toString());
		responseInfo.setApi_id(requestInfo.getApi_id());
		boundaryTypeResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<BoundaryTypeResponse>(boundaryTypeResponse, HttpStatus.CREATED);
	}

	@GetMapping
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute BoundaryTypeRequest boundaryTypeRequest) {

		BoundaryTypeResponse boundaryTypeResponse = new BoundaryTypeResponse();
		List<BoundaryType> allBoundaryTypes = boundaryTypeService.getAllBoundaryTypes(boundaryTypeRequest);
		boundaryTypeResponse.getBoundaryTypes().addAll(allBoundaryTypes);
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.CREATED.toString());
		// responseInfo.setApi_id(body.getRequestInfo().getApi_id());
		boundaryTypeResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<BoundaryTypeResponse>(boundaryTypeResponse, HttpStatus.OK);
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