package org.egov.boundary.web.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.egov.boundary.model.HierarchyType;
import org.egov.boundary.service.HierarchyTypeService;
import org.egov.boundary.web.wrapper.Error;
import org.egov.boundary.web.wrapper.ErrorResponse;
import org.egov.boundary.web.wrapper.HierarchyTypeRequest;
import org.egov.boundary.web.wrapper.HierarchyTypeResponse;
import org.egov.boundary.web.wrapper.RequestInfo;
import org.egov.boundary.web.wrapper.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hierarchytypes")
public class HierarchyTypeController {
	@Autowired
	private HierarchyTypeService hierarchyTypeService;

	@PostMapping
	@ResponseBody
	public ResponseEntity<?> create(@Valid @RequestBody HierarchyTypeRequest hierarchyTypeRequest,
			BindingResult errors) {

		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
		}
		RequestInfo requestInfo = hierarchyTypeRequest.getRequestInfo();
		HierarchyType hierarchyType = hierarchyTypeService.createHierarchyType(hierarchyTypeRequest.getHierarchyType());

		HierarchyTypeResponse hierarchyTypeResponse = new HierarchyTypeResponse();
		hierarchyTypeResponse.getHierarchyTypes().add(hierarchyType);

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.CREATED.toString());
		responseInfo.setApi_id(requestInfo.getApi_id());
		hierarchyTypeResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<HierarchyTypeResponse>(hierarchyTypeResponse, HttpStatus.CREATED);
	}

	@PutMapping(value = "/{code}")
	@ResponseBody
	public ResponseEntity<?> update(@Valid @RequestBody HierarchyTypeRequest hierarchyTypeRequest, BindingResult errors,
			@PathVariable String code) {

		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
		}
		RequestInfo requestInfo = hierarchyTypeRequest.getRequestInfo();
		HierarchyType hierarchyTypeFromDb = hierarchyTypeService.findByCode(code);
		HierarchyType hierarchyType = hierarchyTypeRequest.getHierarchyType();
		hierarchyType.setId(hierarchyTypeFromDb.getId());
		hierarchyType.setVersion(hierarchyTypeFromDb.getVersion());
		hierarchyType = hierarchyTypeService.updateHierarchyType(hierarchyType);

		HierarchyTypeResponse hierarchyTypeResponse = new HierarchyTypeResponse();
		hierarchyTypeResponse.getHierarchyTypes().add(hierarchyType);

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.CREATED.toString());
		responseInfo.setApi_id(requestInfo.getApi_id());
		hierarchyTypeResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<HierarchyTypeResponse>(hierarchyTypeResponse, HttpStatus.CREATED);
	}

	@GetMapping
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute HierarchyTypeRequest hierarchyTypeRequest) {

		HierarchyTypeResponse hierarchyTypeResponse = new HierarchyTypeResponse();
		List<HierarchyType> allHierarchyTypes = hierarchyTypeService.getAllHierarchyTypes(hierarchyTypeRequest);
		hierarchyTypeResponse.getHierarchyTypes().addAll(allHierarchyTypes);
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.CREATED.toString());
		// responseInfo.setApi_id(body.getRequestInfo().getApi_id());
		hierarchyTypeResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<HierarchyTypeResponse>(hierarchyTypeResponse, HttpStatus.OK);
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