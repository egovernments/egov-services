package org.egov.boundary.web.controller;

import org.egov.boundary.domain.service.BoundaryTypeService;
import org.egov.boundary.web.contract.BoundaryType;
import org.egov.boundary.web.contract.BoundaryTypeRequest;
import org.egov.boundary.web.contract.BoundaryTypeResponse;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/boundarytypes")
public class BoundaryTypeController {
	@Autowired
	private BoundaryTypeService boundaryTypeService;

	@PostMapping()
	public ResponseEntity<?> create(@RequestBody BoundaryTypeRequest boundaryTypeRequest, BindingResult errors) {
		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}
		RequestInfo requestInfo = boundaryTypeRequest.getRequestInfo();
		BoundaryType contractBoundaryType = mapToContractBoundaryType(
				boundaryTypeService.createBoundaryType(boundaryTypeRequest.getBoundaryType()));
		BoundaryTypeResponse boundaryTypeResponse = new BoundaryTypeResponse();
		boundaryTypeResponse.getBoundaryTypes().add(contractBoundaryType);

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.CREATED.toString());
		responseInfo.setApiId(requestInfo.getApiId());
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
		org.egov.boundary.persistence.entity.BoundaryType boundaryTypeFromDb = boundaryTypeService.findByCode(code);
		org.egov.boundary.persistence.entity.BoundaryType boundaryType = boundaryTypeRequest.getBoundaryType();
		boundaryType.setId(boundaryTypeFromDb.getId());
		boundaryType.setVersion(boundaryTypeFromDb.getVersion());
		boundaryType = boundaryTypeService.updateBoundaryType(boundaryType);
		BoundaryType contractBoundaryType = mapToContractBoundaryType(boundaryType);
		BoundaryTypeResponse boundaryTypeResponse = new BoundaryTypeResponse();
		boundaryTypeResponse.getBoundaryTypes().add(contractBoundaryType);

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.CREATED.toString());
		responseInfo.setApiId(requestInfo.getApiId());
		boundaryTypeResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<BoundaryTypeResponse>(boundaryTypeResponse, HttpStatus.CREATED);
	}

	@GetMapping
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute BoundaryTypeRequest boundaryTypeRequest) {

		BoundaryTypeResponse boundaryTypeResponse = new BoundaryTypeResponse();
		List<org.egov.boundary.persistence.entity.BoundaryType> allBoundaryTypes = boundaryTypeService
				.getAllBoundaryTypes(boundaryTypeRequest);
		boundaryTypeResponse.getBoundaryTypes().addAll(mapToContractBoundaryTypeList(allBoundaryTypes));
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.CREATED.toString());
		// responseInfo.setApi_id(body.getRequestInfo().getApi_id());
		boundaryTypeResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<BoundaryTypeResponse>(boundaryTypeResponse, HttpStatus.OK);
	}

	@PostMapping(value = "/getByHierarchyType")
	@ResponseBody
	public ResponseEntity<?> getBoundaryTypesByHierarchyTypeId(
			@RequestParam(value = "hierarchyTypeId", required = true) final String hierarchyTypeId,
			@RequestParam(value = "tenantId", required = true) final String tenantId) {
		BoundaryTypeResponse boundaryTypeResponse = new BoundaryTypeResponse();
		if (hierarchyTypeId != null && !hierarchyTypeId.isEmpty()) {
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setStatus(HttpStatus.OK.toString());
			boundaryTypeResponse.setResponseInfo(responseInfo);
			List<BoundaryType> boundaryTypes = mapToContractBoundaryTypeList(boundaryTypeService
					.getAllBoundarTypesByHierarchyTypeIdAndTenantId(Long.valueOf(hierarchyTypeId), tenantId));
			boundaryTypeResponse.setBoundaryTypes(boundaryTypes);
			return new ResponseEntity<>(boundaryTypeResponse, HttpStatus.OK);
		} else
			return new ResponseEntity<>(boundaryTypeResponse, HttpStatus.BAD_REQUEST);

	}

	private List<BoundaryType> mapToContractBoundaryTypeList(
			List<org.egov.boundary.persistence.entity.BoundaryType> boundaryTypeEntity) {
		return boundaryTypeEntity.stream().map(BoundaryType::new).collect(Collectors.toList());
	}

	private BoundaryType mapToContractBoundaryType(
			org.egov.boundary.persistence.entity.BoundaryType boundaryTypeEntity) {
		BoundaryType boundaryType = new BoundaryType();
		if (boundaryTypeEntity != null) {
			boundaryType = new BoundaryType(boundaryTypeEntity);
		}
		return boundaryType;
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
		error.setFields(new ArrayList<>());
		if (errors.hasFieldErrors()) {
			for (FieldError errs : errors.getFieldErrors()) {
				error.getFields().add(new ErrorField(errs.getCode(), errs.getDefaultMessage(), errs.getObjectName()));
			}
		}
		errRes.setError(error);
		return errRes;
	}

}