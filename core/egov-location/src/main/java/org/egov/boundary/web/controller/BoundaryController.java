package org.egov.boundary.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.egov.boundary.domain.service.BoundaryService;
import org.egov.boundary.domain.service.BoundaryTypeService;
import org.egov.boundary.domain.service.CrossHierarchyService;
import org.egov.boundary.persistence.entity.BoundaryType;
import org.egov.boundary.util.BoundaryConstants;
import org.egov.boundary.web.contract.Boundary;
import org.egov.boundary.web.contract.BoundaryRequest;
import org.egov.boundary.web.contract.BoundaryResponse;
import org.egov.boundary.web.contract.RequestInfoWrapper;
import org.egov.boundary.web.contract.ShapeFile;
import org.egov.boundary.web.contract.ShapeFileResponse;
import org.egov.boundary.web.contract.factory.ResponseInfoFactory;
import org.egov.boundary.web.errorhandlers.Error;
import org.egov.boundary.web.errorhandlers.ErrorResponse;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.location.util.EgovLocationConstants;
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

	@Autowired
	private BoundaryTypeService boundaryTypeService;
	@Autowired
	private CrossHierarchyService crossHierarchyService;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@PostMapping
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid BoundaryRequest boundaryRequest, BindingResult errors) {

		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
		}
		RequestInfo requestInfo = boundaryRequest.getRequestInfo();

		final ErrorResponse errorResponses = validateBoundaryRequest(boundaryRequest);

		if (errorResponses.getError() != null && errorResponses.getError().getErrorFields().size() > 0)
			return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

		Boundary boundary = mapToContractBoundary(boundaryService.createBoundary(boundaryRequest.getBoundary()));

		BoundaryResponse boundaryResponse = new BoundaryResponse();
		if (boundaryRequest.getBoundary() != null && boundaryRequest.getBoundary().getTenantId() != null
				&& !boundaryRequest.getBoundary().getTenantId().isEmpty()) {
			boundaryResponse.getBoundarys().add(boundary);
			final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
			responseInfo.setStatus(HttpStatus.OK.toString());
			boundaryResponse.setResponseInfo(responseInfo);
		}
		return new ResponseEntity<BoundaryResponse>(boundaryResponse, HttpStatus.CREATED);
	}

	@PutMapping(value = "/{code}")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody @Valid BoundaryRequest boundaryRequest, BindingResult errors,
			@PathVariable String code, @RequestParam(value = "tenantId", required = true) String tenantId) {

		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
		}

		final ErrorResponse errorResponses = validateBoundaryRequest(boundaryRequest);

		if (errorResponses.getError() != null && errorResponses.getError().getErrorFields().size() > 0)
			return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

		BoundaryResponse boundaryResponse = new BoundaryResponse();
		if (tenantId != null && !tenantId.isEmpty()) {
			RequestInfo requestInfo = boundaryRequest.getRequestInfo();
			org.egov.boundary.persistence.entity.Boundary boundary = boundaryRequest.getBoundary();
			org.egov.boundary.persistence.entity.Boundary boundaryFromDb = boundaryService
					.findByTenantIdAndCode(tenantId, code);
			boundary.setId(boundaryFromDb.getId());
			boundary.setVersion(boundaryFromDb.getVersion());
			Boundary contractBoundary = mapToContractBoundary(boundaryService.updateBoundary(boundary));
			boundaryResponse.getBoundarys().add(contractBoundary);

			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setStatus(HttpStatus.CREATED.toString());
			responseInfo.setApiId(requestInfo.getApiId());
			boundaryResponse.setResponseInfo(responseInfo);
		}
		return new ResponseEntity<BoundaryResponse>(boundaryResponse, HttpStatus.CREATED);
	}

	@GetMapping
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute BoundaryRequest boundaryRequest) {

		BoundaryResponse boundaryResponse = new BoundaryResponse();
		if (boundaryRequest.getBoundary() != null && boundaryRequest.getBoundary().getTenantId() != null
				&& !boundaryRequest.getBoundary().getTenantId().isEmpty()) {
			List<Boundary> allBoundarys = mapToContractBoundaryList(boundaryService.getAllBoundary(boundaryRequest));
			boundaryResponse.getBoundarys().addAll(allBoundarys);
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setStatus(HttpStatus.CREATED.toString());
			boundaryResponse.setResponseInfo(responseInfo);
		}
		return new ResponseEntity<BoundaryResponse>(boundaryResponse, HttpStatus.OK);
	}

	@GetMapping("/getLocationByLocationName")
	@ResponseBody
	public ResponseEntity<?> getLocation(@RequestParam(value = "tenantId", required = true) String tenantId,
			@RequestParam("locationName") final String locationName) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			if (tenantId != null && !tenantId.isEmpty()) {
				list = boundaryService.getBoundaryDataByTenantIdAndNameLike(tenantId, locationName);
			}
			return new ResponseEntity<List<Map<String, Object>>>(list, HttpStatus.OK);
		} catch (final Exception e) {
			return new ResponseEntity<String>("error in request", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/childLocationsByBoundaryId")
	@ResponseBody
	public ResponseEntity<?> getChildLocationsByBoundaryId(
			@RequestParam(value = "tenantId", required = true) String tenantId,
			@RequestParam(value = "boundaryId", required = true) final String boundaryId) {
		BoundaryResponse boundaryResponse = new BoundaryResponse();
		if (tenantId != null && !tenantId.isEmpty() && boundaryId != null && !boundaryId.isEmpty()) {
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setStatus(HttpStatus.OK.toString());

			boundaryResponse.setResponseInfo(responseInfo);
			List<Boundary> boundaries = getChildBoundaryByBoundaryIdAndTenantId(boundaryId, tenantId);
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
			List<Boundary> boundaries = mapToContractBoundaryList(boundaryService
					.getAllBoundariesByBoundaryTypeIdAndTenantId(Long.valueOf(boundaryTypeId), tenantId));
			boundaryResponse.setBoundarys(boundaries);
			return new ResponseEntity<BoundaryResponse>(boundaryResponse, HttpStatus.OK);
		} else
			return new ResponseEntity<>(boundaryResponse, HttpStatus.BAD_REQUEST);

	}

	private List<Boundary> getChildBoundaryByBoundaryIdAndTenantId(String boundaryId, String tenantId) {
		return mapToContractBoundaryList(crossHierarchyService
				.getActiveChildBoundariesByBoundaryIdAndTenantId(Long.valueOf(boundaryId), tenantId));
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
			@RequestParam(value = "tenantId", required = true) String tenantId,
			@RequestParam(value = "boundaryTypeName", required = true) final String boundaryTypeName,
			@RequestParam(value = "hierarchyTypeName", required = true) final String hierarchyTypeName) {
		BoundaryResponse boundaryResponse = new BoundaryResponse();
		if (tenantId != null && !tenantId.isEmpty() && boundaryTypeName != null && !boundaryTypeName.isEmpty()
				&& hierarchyTypeName != null && !hierarchyTypeName.isEmpty()) {
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setStatus(HttpStatus.OK.toString());
			boundaryResponse.setResponseInfo(responseInfo);
			List<org.egov.boundary.persistence.entity.Boundary> boundaryList = boundaryService
					.getBoundariesByBndryTypeNameAndHierarchyTypeNameAndTenantId(boundaryTypeName, hierarchyTypeName,
							tenantId);
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
			for (final FieldError fieldError : errors.getFieldErrors())
				error.getFields().put(fieldError.getField(), fieldError.getRejectedValue());
		}
		errRes.setError(error);
		return errRes;
	}

	@PostMapping("/isshapefileexist")
	@ResponseBody
	public ResponseEntity<?> isShapeFileExist(@RequestParam(value = "tenantId", required = true) String tenantId,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {

		final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		boolean exist = false;
		try {
			if (tenantId != null && !tenantId.isEmpty()) {
				exist = boundaryService.checkTenantShapeFileExistOrNot(tenantId);
				return getSuccessResponse(requestInfo, exist);
			}

			return getFailureResponse(requestInfo);

		} catch (final Exception e) {
			return new ResponseEntity<String>("error in request", HttpStatus.BAD_REQUEST);
		}
	}

	private ResponseEntity<?> getFailureResponse(final RequestInfo requestInfo) {

		final org.egov.common.contract.response.ErrorResponse errorResponse = new org.egov.common.contract.response.ErrorResponse();
		final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, false);
		errorResponse.setResponseInfo(responseInfo);
		final List<ErrorField> errorFields = new ArrayList<>();
		final ErrorField errorField = ErrorField.builder().code(EgovLocationConstants.TENANTID_MANDATORY_CODE)
				.message(EgovLocationConstants.TENANTID_MANADATORY_ERROR_MESSAGE)
				.field(EgovLocationConstants.TENANTID_MANADATORY_FIELD_NAME).build();
		errorFields.add(errorField);
		org.egov.common.contract.response.Error error = new org.egov.common.contract.response.Error();
		error.setFields(errorFields);
		errorResponse.setError(error);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

	}

	private ResponseEntity<?> getSuccessResponse(final RequestInfo requestInfo, final boolean fileExist) {
		ShapeFileResponse shapeFileExist = new ShapeFileResponse();
		final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		shapeFileExist.setResponseInfo(responseInfo);
		ShapeFile shapeFile = new ShapeFile();
		shapeFile.setFileExist(fileExist);
		shapeFileExist.setShapeFile(shapeFile);
		return new ResponseEntity<ShapeFileResponse>(shapeFileExist, HttpStatus.OK);

	}

	@PostMapping(value = "/_search")
	@ResponseBody
	public ResponseEntity<?> boundarySearch(@RequestParam(value = "tenantId", required = true) String tenantId,
			@RequestParam(value = "codes", required = false) final List<String> codes,
			@RequestParam(value = "boundaryIds", required = false) final List<Long> boundaryIds,
			@RequestParam(value = "boundaryNum", required = false) final List<Long> boundaryNum,
			@RequestParam(value = "boundaryType", required = false) final String boundaryType) {
		BoundaryResponse boundaryResponse = new BoundaryResponse();
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.OK.toString());
		boundaryResponse.setResponseInfo(responseInfo);
		List<Long> boundaryTypeList = null;
		
		if(tenantId!=null && !tenantId.isEmpty() && codes!=null && !codes.isEmpty()){
			List<Boundary> allBoundarys = mapToContractBoundaryList(boundaryService.getAllBoundariesByTenantAndCodes(tenantId, codes));
			return getBoundarySearchSuccessResponse(boundaryResponse, allBoundarys);
		}

		if (tenantId != null && tenantId != "" && boundaryType != null && boundaryType != "") {
			boundaryTypeList = getBoundaryTypeList(tenantId, boundaryType);
			if (boundaryTypeList.isEmpty()) {
				ErrorResponse errorResponses = addBoundaryTypeValidationError(boundaryResponse);
				return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
			}
		}

		if (tenantId != null && tenantId != "" && boundaryNum != null && boundaryNum.size() != 0 && boundaryType != null
				&& boundaryType != "" && boundaryIds != null && !boundaryIds.isEmpty() && boundaryIds.size() > 0) {
			List<Boundary> allBoundarys = mapToContractBoundaryList(
					boundaryService.getAllBoundaryByTenantAndNumAndTypeAndTypeIds(tenantId, boundaryNum, boundaryIds,
							boundaryTypeList));
			return getBoundarySearchSuccessResponse(boundaryResponse, allBoundarys);
			
		} else if (tenantId != null && tenantId != "" && boundaryNum != null && boundaryNum.size() != 0 && boundaryType != null
				&& boundaryType != "") {
			List<Boundary> allBoundarys = mapToContractBoundaryList(
					boundaryService.getAllBoundariesByNumberAndType(tenantId, boundaryNum, boundaryTypeList));
			return getBoundarySearchSuccessResponse(boundaryResponse, allBoundarys);

		} else if (tenantId != null && tenantId != "" && boundaryIds != null && !boundaryIds.isEmpty()
				&& boundaryIds.size() > 0) {
			List<Boundary> allBoundarys = mapToContractBoundaryList(
					boundaryService.getAllBoundariesByBoundaryIdsAndTenant(tenantId, boundaryIds));
			return getBoundarySearchSuccessResponse(boundaryResponse, allBoundarys);

		} else if (tenantId != null && tenantId != "" && boundaryNum != null && boundaryNum.size() != 0) {
			List<Boundary> allBoundarys = mapToContractBoundaryList(
					boundaryService.getAllBoundaryByTenantIdAndNumber(tenantId, boundaryNum));
			return getBoundarySearchSuccessResponse(boundaryResponse, allBoundarys);

		} else if (tenantId != null && tenantId != "" && boundaryType != null && boundaryType != "") {
			List<Boundary> allBoundarys = mapToContractBoundaryList(
					boundaryService.getAllBoundaryByTenantIdAndTypeIds(tenantId, boundaryTypeList));
			return getBoundarySearchSuccessResponse(boundaryResponse, allBoundarys);

		} else if (tenantId != null && tenantId != "") {
			List<Boundary> allBoundarys = mapToContractBoundaryList(boundaryService.getAllBoundaryByTenantId(tenantId));
			return getBoundarySearchSuccessResponse(boundaryResponse, allBoundarys);
		
		} else {
			responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
			boundaryResponse.setResponseInfo(responseInfo);
			return new ResponseEntity<>(boundaryResponse, HttpStatus.BAD_REQUEST);
		}
	}

	private ResponseEntity<?> getBoundarySearchSuccessResponse(BoundaryResponse boundaryResponse,
			List<Boundary> allBoundarys) {
		boundaryResponse.setBoundarys(allBoundarys);
		return new ResponseEntity<BoundaryResponse>(boundaryResponse, HttpStatus.OK);
	}

	private ErrorResponse addBoundaryTypeValidationError(BoundaryResponse boundaryResponse) {
		boundaryResponse.getResponseInfo().setStatus(HttpStatus.BAD_REQUEST.toString());
		final ErrorResponse errorResponse = new ErrorResponse();
		final List<ErrorField> errorFields = new ArrayList<>();
		final ErrorField errorField = ErrorField.builder().code(BoundaryConstants.BOUNDARY_TYPE_INVALID_CODE)
				.message(BoundaryConstants.BOUNDARY_TYPE_INVALID_ERROR_MESSAGE)
				.field(BoundaryConstants.BOUNDARY_TYPE_INVALID_FIELD_NAME).build();
		final Error error = Error.builder().code(HttpStatus.BAD_REQUEST.value())
				.message(BoundaryConstants.INVALID_BOUNDARY_REQUEST_MESSAGE).errorFields(errorFields).build();
		errorResponse.setError(error);
		errorFields.add(errorField);
		return errorResponse;
	}

	private List<Long> getBoundaryTypeList(String tenantId, String boundaryType) {
		List<Long> list = new ArrayList<Long>();
		List<BoundaryType> boundaryTypeList = boundaryTypeService.getAllBoundarytypesByNameAndTenant(boundaryType,
				tenantId);
		for (BoundaryType bndryType : boundaryTypeList) {
			list.add(bndryType.getId());
		}
		return list;
	}

	private ErrorResponse validateBoundaryRequest(final BoundaryRequest boundaryRequest) {

		final ErrorResponse errorResponse = new ErrorResponse();
		final Error error = getError(boundaryRequest);
		errorResponse.setError(error);
		return errorResponse;
	}

	private Error getError(final BoundaryRequest boundaryRequest) {
		final List<ErrorField> errorFields = getErrorFields(boundaryRequest);
		return Error.builder().code(HttpStatus.BAD_REQUEST.value())
				.message(BoundaryConstants.INVALID_BOUNDARY_REQUEST_MESSAGE).errorFields(errorFields).build();
	}

	private List<ErrorField> getErrorFields(final BoundaryRequest boundaryRequest) {
		final List<ErrorField> errorFields = new ArrayList<>();

		addTenantIdValidationError(boundaryRequest, errorFields);
		addBoundaryNameNotNullValidationError(boundaryRequest, errorFields);
		addBoundaryCodeNotNullValidationError(boundaryRequest, errorFields);
		addBoundaryTypeNotNullValidationError(boundaryRequest, errorFields);
		addBoundaryNumberNotNullValidationError(boundaryRequest, errorFields);
		addBoundaryInvalidTypeIdValidationError(boundaryRequest, errorFields);

		return errorFields;
	}

	private List<ErrorField> addTenantIdValidationError(final BoundaryRequest boundaryRequest,
			final List<ErrorField> errorFields) {

		if (boundaryRequest.getBoundary().getTenantId() == null
				|| boundaryRequest.getBoundary().getTenantId().isEmpty()) {
			final ErrorField errorField = ErrorField.builder().code(BoundaryConstants.TENANTID_MANDATORY_CODE)
					.message(BoundaryConstants.TENANTID_MANADATORY_ERROR_MESSAGE)
					.field(BoundaryConstants.TENANTID_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		}

		return errorFields;
	}

	private List<ErrorField> addBoundaryNameNotNullValidationError(final BoundaryRequest boundaryRequest,
			final List<ErrorField> errorFields) {

		if (boundaryRequest.getBoundary().getName() == null || boundaryRequest.getBoundary().getName().isEmpty()) {
			final ErrorField errorField = ErrorField.builder().code(BoundaryConstants.BOUNDARY_NAME_MANDATORY_CODE)
					.message(BoundaryConstants.BOUNDARY_NAME_MANADATORY_ERROR_MESSAGE)
					.field(BoundaryConstants.BOUNDARY_NAME_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		}

		return errorFields;
	}
	
	private List<ErrorField> addBoundaryCodeNotNullValidationError(final BoundaryRequest boundaryRequest,
			final List<ErrorField> errorFields) {

		if (boundaryRequest.getBoundary().getCode() == null || boundaryRequest.getBoundary().getCode().isEmpty()) {
			final ErrorField errorField = ErrorField.builder().code(BoundaryConstants.BOUNDARY_CODE_MANDATORY_CODE)
					.message(BoundaryConstants.BOUNDARY_CODE_MANADATORY_ERROR_MESSAGE)
					.field(BoundaryConstants.BOUNDARY_CODE_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		}

		return errorFields;
	}

	private List<ErrorField> addBoundaryTypeNotNullValidationError(final BoundaryRequest boundaryRequest,
			final List<ErrorField> errorFields) {

		if (boundaryRequest.getBoundary().getBoundaryType() == null) {
			final ErrorField errorField = ErrorField.builder().code(BoundaryConstants.BOUNDARY_TYPE_MANDATORY_CODE)
					.message(BoundaryConstants.BOUNDARY_TYPE_MANADATORY_ERROR_MESSAGE)
					.field(BoundaryConstants.BOUNDARY_TYPE_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		}

		return errorFields;
	}

	private List<ErrorField> addBoundaryNumberNotNullValidationError(final BoundaryRequest boundaryRequest,
			final List<ErrorField> errorFields) {

		if (boundaryRequest.getBoundary() != null && boundaryRequest.getBoundary().getBoundaryNum() == null) {

			final ErrorField errorField = ErrorField.builder().code(BoundaryConstants.BOUNDARY_NUMBER__MANDATORY_CODE)
					.message(BoundaryConstants.BOUNDARY_NUMBER_MANADATORY_ERROR_MESSAGE)
					.field(BoundaryConstants.BOUNDARY_NUMBER_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		}

		return errorFields;
	}
	
	private List<ErrorField> addBoundaryInvalidTypeIdValidationError(final BoundaryRequest boundaryRequest,
			final List<ErrorField> errorFields) {

		if (boundaryRequest.getBoundary() != null && boundaryRequest.getBoundary().getBoundaryType() != null
				&& boundaryRequest.getBoundary().getBoundaryType().getCode() != null) {

			if ((boundaryTypeService.findByTenantIdAndCode(boundaryRequest.getBoundary().getTenantId(),boundaryRequest.getBoundary().getBoundaryType().getCode()) == null)) {

				final ErrorField errorField = ErrorField.builder().code(BoundaryConstants.BOUNDARY_TYPE_CODE_INVALID_CODE)
						.message(BoundaryConstants.BOUNDARY_TYPE_CODE_INVALID_ERROR_MESSAGE)
						.field(BoundaryConstants.BOUNDARY_TYPE_CODE_INVALID_FIELD_NAME).build();
				errorFields.add(errorField);

			}

		} else if (boundaryRequest.getBoundary() != null && boundaryRequest.getBoundary().getBoundaryType() != null
				&& boundaryRequest.getBoundary().getBoundaryType().getCode() == null) {

			final ErrorField errorField = ErrorField.builder().code(BoundaryConstants.BOUNDARY_TYPE_CODE_MANDATORY_CODE)
					.message(BoundaryConstants.BOUNDARY_TYPE_CODE_MANDATORY_ERROR_MESSAGE)
					.field(BoundaryConstants.BOUNDARY_TYPE_CODE_MANDATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		}

		return errorFields;
	}
}