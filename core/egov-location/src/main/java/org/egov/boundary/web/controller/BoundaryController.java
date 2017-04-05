package org.egov.boundary.web.controller;

import org.egov.boundary.domain.service.BoundaryService;
import org.egov.boundary.domain.service.CrossHierarchyService;
import org.egov.boundary.web.contract.Boundary;
import org.egov.boundary.web.contract.BoundaryRequest;
import org.egov.boundary.web.contract.BoundaryResponse;
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
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/boundarys")
public class BoundaryController {
	@Autowired
	private BoundaryService boundaryService;

	@Autowired
	private CrossHierarchyService crossHierarchyService;

	@PostMapping
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid BoundaryRequest boundaryRequest, BindingResult errors) {

		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
		}
		RequestInfo requestInfo = boundaryRequest.getRequestInfo();
		Boundary boundary = mapToContractBoundary(boundaryService.createBoundary(boundaryRequest.getBoundary()));

		BoundaryResponse boundaryResponse = new BoundaryResponse();
		boundaryResponse.getBoundarys().add(boundary);

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.CREATED.toString());
		responseInfo.setApiId(requestInfo.getApiId());
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
		org.egov.boundary.persistence.entity.Boundary boundaryFromDb = boundaryService.findByCode(code);
		org.egov.boundary.persistence.entity.Boundary boundary = boundaryRequest.getBoundary();
		boundary.setId(boundaryFromDb.getId());
		boundary.setVersion(boundaryFromDb.getVersion());
		Boundary contractBoundary = mapToContractBoundary(boundaryService.updateBoundary(boundary));

		BoundaryResponse boundaryResponse = new BoundaryResponse();
		boundaryResponse.getBoundarys().add(contractBoundary);

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.CREATED.toString());
		responseInfo.setApiId(requestInfo.getApiId());
		boundaryResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<BoundaryResponse>(boundaryResponse, HttpStatus.CREATED);
	}

	@GetMapping
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute BoundaryRequest boundaryRequest) {

		BoundaryResponse boundaryResponse = new BoundaryResponse();
		List<Boundary> allBoundarys = mapToContractBoundaryList(boundaryService.getAllBoundary(boundaryRequest));
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

	@PostMapping(value = "/childLocationsByBoundaryId")
	@ResponseBody
	public ResponseEntity<?> getChildLocationsByBoundaryId(
			@RequestParam(value = "boundaryId", required = true) final String boundaryId) {
		BoundaryResponse boundaryResponse = new BoundaryResponse();
		if (boundaryId != null && !boundaryId.isEmpty()) {
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setStatus(HttpStatus.OK.toString());
			boundaryResponse.setResponseInfo(responseInfo);
			List<Boundary> boundaries = getChildBoundaryByBoundaryId(boundaryId);
			boundaryResponse.setBoundarys(boundaries);
			return new ResponseEntity<BoundaryResponse>(boundaryResponse, HttpStatus.OK);
		} else
			return new ResponseEntity<BoundaryResponse>(boundaryResponse, HttpStatus.BAD_REQUEST);

	}

	@PostMapping(value = "/getByBoundaryType")
	@ResponseBody
	public ResponseEntity<?> getBoundaryByBoundaryTypeId(
			@RequestParam(value = "boundaryTypeId", required = true) final String boundaryTypeId,
			@RequestParam(value = "tenantId", required = true) final String tenantId) {
		BoundaryResponse boundaryResponse = new BoundaryResponse();
		if (boundaryTypeId != null && !boundaryTypeId.isEmpty()) {
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setStatus(HttpStatus.OK.toString());
			boundaryResponse.setResponseInfo(responseInfo);
			List<Boundary> boundaries = mapToContractBoundaryList(
					boundaryService.getAllBoundariesByBoundaryTypeIdAndTenantId(Long.valueOf(boundaryTypeId),
							tenantId));
			boundaryResponse.setBoundarys(boundaries);
			return new ResponseEntity<BoundaryResponse>(boundaryResponse, HttpStatus.OK);
		} else
			return new ResponseEntity<>(boundaryResponse, HttpStatus.BAD_REQUEST);

	}

	private List<Boundary> getChildBoundaryByBoundaryId(String boundaryId) {
		return mapToContractBoundaryList(
				crossHierarchyService.getActiveChildBoundariesByBoundaryId(Long.valueOf(boundaryId)));
	}

	private List<Boundary> mapToContractBoundaryList(
			List<org.egov.boundary.persistence.entity.Boundary> boundaryEntity) {
		return boundaryEntity.stream().map(Boundary::new).collect(Collectors.toList());
	}

	private Boundary mapToContractBoundary(org.egov.boundary.persistence.entity.Boundary boundaryEntity) {
		Boundary boundary = new Boundary();
		if (boundaryEntity != null) {
			boundary = new Boundary(boundaryEntity);
		}
		return boundary;
	}

	@PostMapping(value = "/boundariesByBndryTypeNameAndHierarchyTypeName")
	@ResponseBody
	public ResponseEntity<?> getBoundariesByBndryTypeNameAndHierarchyTypeName(
			@RequestParam(value = "boundaryTypeName", required = true) final String boundaryTypeName,
			@RequestParam(value = "hierarchyTypeName", required = true) final String hierarchyTypeName) {
		BoundaryResponse boundaryResponse = new BoundaryResponse();
		if (boundaryTypeName != null && !boundaryTypeName.isEmpty() && hierarchyTypeName != null
				&& !hierarchyTypeName.isEmpty()) {
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setStatus(HttpStatus.OK.toString());
			boundaryResponse.setResponseInfo(responseInfo);
			List<org.egov.boundary.persistence.entity.Boundary> boundaryList = boundaryService
					.getBoundariesByBndryTypeNameAndHierarchyTypeName(boundaryTypeName, hierarchyTypeName);
			boundaryResponse.setBoundarys(boundaryList.stream().map(Boundary::new).collect(Collectors.toList()));
			return new ResponseEntity<BoundaryResponse>(boundaryResponse, HttpStatus.OK);
		} else
			return new ResponseEntity<BoundaryResponse>(boundaryResponse, HttpStatus.BAD_REQUEST);

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