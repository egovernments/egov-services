package org.egov.boundary.web.controller;

import org.egov.boundary.domain.service.HierarchyTypeService;
import org.egov.boundary.persistence.entity.HierarchyType;
import org.egov.boundary.web.contract.HierarchyTypeRequest;
import org.egov.boundary.web.contract.HierarchyTypeResponse;
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
		responseInfo.setApiId(requestInfo.getApiId());
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
		responseInfo.setApiId(requestInfo.getApiId());
		hierarchyTypeResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<>(hierarchyTypeResponse, HttpStatus.CREATED);
	}

	@GetMapping
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute HierarchyTypeRequest hierarchyTypeRequest) {

		HierarchyTypeResponse hierarchyTypeResponse = new HierarchyTypeResponse();
		List<HierarchyType> allHierarchyTypes = hierarchyTypeService.getAllHierarchyTypes(hierarchyTypeRequest);
		hierarchyTypeResponse.getHierarchyTypes().addAll(allHierarchyTypes);
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.CREATED.toString());
		hierarchyTypeResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<>(hierarchyTypeResponse, HttpStatus.OK);
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