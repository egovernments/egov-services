package org.egov.boundary.web.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.egov.boundary.domain.service.BoundaryService;
import org.egov.boundary.persistence.entity.Boundary;
import org.egov.boundary.web.contract.BoundaryRequest;
import org.egov.boundary.web.contract.BoundaryResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boundarys")
public class BoundaryController {
	@Autowired
	private BoundaryService boundaryService;

	@PostMapping
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid BoundaryRequest boundaryRequest, BindingResult errors) {

		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
		}
		RequestInfo requestInfo = boundaryRequest.getRequestInfo();
		Boundary boundary = boundaryService.createBoundary(boundaryRequest.getBoundary());

		BoundaryResponse boundaryResponse = new BoundaryResponse();
		boundaryResponse.getBoundarys().add(boundary);

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.CREATED.toString());
		responseInfo.setApi_id(requestInfo.getApi_id());
		boundaryResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<BoundaryResponse>(boundaryResponse, HttpStatus.CREATED);
	}

	@PutMapping(value = "/{code}")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody @Valid BoundaryRequest boundaryRequest, BindingResult errors,
			@PathVariable String code) {

		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
		}
		RequestInfo requestInfo = boundaryRequest.getRequestInfo();
		Boundary boundaryFromDb = boundaryService.findByCode(code);
		Boundary boundary = boundaryRequest.getBoundary();
		boundary.setId(boundaryFromDb.getId());
		boundary.setVersion(boundaryFromDb.getVersion());
		boundary = boundaryService.updateBoundary(boundary);

		BoundaryResponse boundaryResponse = new BoundaryResponse();
		boundaryResponse.getBoundarys().add(boundary);

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.CREATED.toString());
		responseInfo.setApi_id(requestInfo.getApi_id());
		boundaryResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<BoundaryResponse>(boundaryResponse, HttpStatus.CREATED);
	}

	@GetMapping
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute BoundaryRequest boundaryRequest) {

		BoundaryResponse boundaryResponse = new BoundaryResponse();
		List<Boundary> allBoundarys = boundaryService.getAllBoundary(boundaryRequest);
		boundaryResponse.getBoundarys().addAll(allBoundarys);
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.CREATED.toString());
		boundaryResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<BoundaryResponse>(boundaryResponse, HttpStatus.OK);
	}
	
	
	@GetMapping("/getLocationByLocationName")
	@ResponseBody
	public ResponseEntity<?> getLocation(@RequestParam("locationName") final String locationName) {
		try {
			final List<Map<String, Object>> list = boundaryService.getBoundaryDataByNameLike(locationName);
			return new ResponseEntity<List<Map<String, Object>>>(list, HttpStatus.OK);
		} catch (final Exception e) {
			return new ResponseEntity<String>("error in request", HttpStatus.BAD_REQUEST);
		}
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