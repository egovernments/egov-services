package org.egov.boundary.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.boundary.domain.service.BoundaryService;
import org.egov.boundary.domain.service.BoundaryTypeService;
import org.egov.boundary.domain.service.CrossHierarchyService;
import org.egov.boundary.util.BoundaryConstants;
import org.egov.boundary.web.contract.CrossHierarchy;
import org.egov.boundary.web.contract.CrossHierarchyRequest;
import org.egov.boundary.web.contract.CrossHierarchyResponse;
import org.egov.boundary.web.contract.CrossHierarchySearchRequest;
import org.egov.boundary.web.errorhandlers.Error;
import org.egov.boundary.web.errorhandlers.ErrorResponse;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ResponseInfo;
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
@RequestMapping("/crosshierarchys")
public class CrossHierarchyController {
	@Autowired
	private CrossHierarchyService crossHierarchyService;
	
	@Autowired
	private BoundaryService boundaryService;
	
	@Autowired
	private BoundaryTypeService boundaryTypeService;


	@PostMapping
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid CrossHierarchyRequest crossHierarchyRequest,
									BindingResult errors) {

		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
		}
		final ErrorResponse errorResponses = validateCrossHierarchyRequest(crossHierarchyRequest);
		if (errorResponses.getError() != null && errorResponses.getError().getErrorFields().size() > 0)
			return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

		CrossHierarchyResponse crossHierarchyResponse = new CrossHierarchyResponse();
		if (crossHierarchyRequest.getCrossHierarchy() != null
				&& crossHierarchyRequest.getCrossHierarchy().getTenantId() != null
				&& !crossHierarchyRequest.getCrossHierarchy().getTenantId().isEmpty()) {
			RequestInfo requestInfo = crossHierarchyRequest.getRequestInfo();
			CrossHierarchy crossHierarchy = crossHierarchyService.create(crossHierarchyRequest.getCrossHierarchy());
			crossHierarchyResponse.getCrossHierarchys().add(crossHierarchy);
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setStatus(HttpStatus.CREATED.toString());
			responseInfo.setApiId(requestInfo.getApiId());
			crossHierarchyResponse.setResponseInfo(responseInfo);
		}
		return new ResponseEntity<>(crossHierarchyResponse, HttpStatus.CREATED);
	}

	@PutMapping(value = "/{code}")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody @Valid CrossHierarchyRequest crossHierarchyRequest,
									BindingResult errors, @PathVariable String code,
									@RequestParam(value = "tenantId", required = true) String tenantId) {
		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
		}
		CrossHierarchyResponse crossHierarchyResponse = new CrossHierarchyResponse();
		if (tenantId != null && !tenantId.isEmpty()) {
			RequestInfo requestInfo = crossHierarchyRequest.getRequestInfo();
			crossHierarchyRequest.getCrossHierarchy().setCode(code);
			crossHierarchyRequest.getCrossHierarchy().setTenantId(tenantId);
			CrossHierarchy crossHierarchy = crossHierarchyService.update(crossHierarchyRequest.getCrossHierarchy());
			crossHierarchyResponse.getCrossHierarchys().add(crossHierarchy);
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setStatus(HttpStatus.OK.toString());
			responseInfo.setApiId(requestInfo.getApiId());
			crossHierarchyResponse.setResponseInfo(responseInfo);
		}
		return new ResponseEntity<CrossHierarchyResponse>(crossHierarchyResponse, HttpStatus.CREATED);
	}

	@GetMapping
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute CrossHierarchySearchRequest crossHierarchyRequest) {

		CrossHierarchyResponse crossHierarchyResponse = new CrossHierarchyResponse();
		if (crossHierarchyRequest.getCrossHierarchy() != null
				&& crossHierarchyRequest.getCrossHierarchy().getTenantId() != null
				&& !crossHierarchyRequest.getCrossHierarchy().getTenantId().isEmpty()) {
			List<CrossHierarchy> allCrossHierarchys = crossHierarchyService
					.getAllCrossHierarchys(crossHierarchyRequest);
			crossHierarchyResponse.getCrossHierarchys().addAll(allCrossHierarchys);
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setStatus(HttpStatus.OK.toString());
			// responseInfo.setApi_id(body.getRequestInfo().getApi_id());
			crossHierarchyResponse.setResponseInfo(responseInfo);
		}
		return new ResponseEntity<CrossHierarchyResponse>(crossHierarchyResponse, HttpStatus.OK);
	}
	
	@PostMapping(value = "/_search")
	@ResponseBody
	public ResponseEntity<?> searchCrossHierarch(@RequestBody @Valid CrossHierarchySearchRequest crossHierarchyRequest) {

		CrossHierarchyResponse crossHierarchyResponse = new CrossHierarchyResponse();
		if (crossHierarchyRequest.getCrossHierarchy() != null
				&& crossHierarchyRequest.getCrossHierarchy().getTenantId() != null
				&& !crossHierarchyRequest.getCrossHierarchy().getTenantId().isEmpty()) {
			List<CrossHierarchy> allCrossHierarchys = crossHierarchyService
					.getAllCrossHierarchys(crossHierarchyRequest);
			crossHierarchyResponse.getCrossHierarchys().addAll(allCrossHierarchys);
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setStatus(HttpStatus.OK.toString());
			// responseInfo.setApi_id(body.getRequestInfo().getApi_id());
			crossHierarchyResponse.setResponseInfo(responseInfo);
		}
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
			for (final FieldError fieldError : errors.getFieldErrors())
				error.getFields().put(fieldError.getField(), fieldError.getRejectedValue());
		}
		errRes.setError(error);
		return errRes;
	}

	private ErrorResponse validateCrossHierarchyRequest(final CrossHierarchyRequest crossHierarchyRequest) {
		final ErrorResponse errorResponse = new ErrorResponse();
		final Error error = getError(crossHierarchyRequest);
		errorResponse.setError(error);
		return errorResponse;
	}

	private Error getError(final CrossHierarchyRequest crossHierarchyRequest) {
		final List<ErrorField> errorFields = getErrorFields(crossHierarchyRequest);
		return Error.builder().code(HttpStatus.BAD_REQUEST.value())
				.message(BoundaryConstants.INVALID_HIERARCHYtype_REQUEST_MESSAGE).errorFields(errorFields).build();
	}

	private List<ErrorField> getErrorFields(final CrossHierarchyRequest crossHierarchyRequest) {
		final List<ErrorField> errorFields = new ArrayList<>();
		addTenantIdValidationError(crossHierarchyRequest, errorFields);
		addCrossHierarchyParentValidationError(crossHierarchyRequest,errorFields);
		addCrossHierarchyChildValidationError(crossHierarchyRequest,errorFields);
		addCrossHierarchyParentTypeValidationError(crossHierarchyRequest,errorFields);
		addCrossHierarchyChildTypeValidationError(crossHierarchyRequest,errorFields);
		return errorFields;
	}

	private List<ErrorField> addTenantIdValidationError(final CrossHierarchyRequest crossHierarchyRequest,
			final List<ErrorField> errorFields) {
		if (crossHierarchyRequest.getCrossHierarchy().getTenantId() == null
				|| crossHierarchyRequest.getCrossHierarchy().getTenantId().isEmpty()) {
			final ErrorField errorField = ErrorField.builder().code(BoundaryConstants.TENANTID_MANDATORY_CODE)
					.message(BoundaryConstants.TENANTID_MANADATORY_ERROR_MESSAGE)
					.field(BoundaryConstants.TENANTID_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		}
		return errorFields;
	}
	
	private List<ErrorField> addCrossHierarchyParentValidationError(final CrossHierarchyRequest crossHierarchyRequest,
			final List<ErrorField> errorFields){
		if(crossHierarchyRequest.getCrossHierarchy().getParent() == null){
			final ErrorField errorField = ErrorField.builder().code(BoundaryConstants.CROSSHIERARCHY_PARENT_MANDATORY_CODE)
					.message(BoundaryConstants.CROSSHIERARCHY_PARENT_MANADATORY_ERROR_MESSAGE)
					.field(BoundaryConstants.CROSSHIERARCHY_PARENT_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		} else if (crossHierarchyRequest.getCrossHierarchy().getParent() != null && crossHierarchyRequest.getCrossHierarchy().getParent().getId()==null){
			final ErrorField errorField = ErrorField.builder().code(BoundaryConstants.CROSSHIERARCHY_PARENTID_MANDATORY_CODE)
					.message(BoundaryConstants.CROSSHIERARCHY_PARENTID_MANADATORY_ERROR_MESSAGE)
					.field(BoundaryConstants.CROSSHIERARCHY_PARENTID_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		} else if(crossHierarchyRequest.getCrossHierarchy().getParent() != null && crossHierarchyRequest.getCrossHierarchy().getParent().getId()!=null){
			if (!(boundaryService.findByTenantIdAndId(crossHierarchyRequest.getCrossHierarchy().getParent().getId(), crossHierarchyRequest.getCrossHierarchy().getTenantId())!=null)){
				final ErrorField errorField = ErrorField.builder().code(BoundaryConstants.CROSSHIERARCHY_PARENTID_INVALID_CODE)
						.message(BoundaryConstants.CROSSHIERARCHY_PARENTID_INVALID_ERROR_MESSAGE)
						.field(BoundaryConstants.CROSSHIERARCHY_PARENTID_INVALID_FIELD_NAME).build();
				errorFields.add(errorField);
			}
		}
		return errorFields;
	}
	
	private List<ErrorField> addCrossHierarchyChildValidationError(final CrossHierarchyRequest crossHierarchyRequest,
			final List<ErrorField> errorFields){
		if(crossHierarchyRequest.getCrossHierarchy().getChild() == null){
			final ErrorField errorField = ErrorField.builder().code(BoundaryConstants.CROSSHIERARCHY_CHILD_MANDATORY_CODE)
					.message(BoundaryConstants.CROSSHIERARCHY_CHILD_MANADATORY_ERROR_MESSAGE)
					.field(BoundaryConstants.CROSSHIERARCHY_CHILD_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		} else if (crossHierarchyRequest.getCrossHierarchy().getChild() != null && crossHierarchyRequest.getCrossHierarchy().getChild().getId()==null){
			final ErrorField errorField = ErrorField.builder().code(BoundaryConstants.CROSSHIERARCHY_CHILDID_MANDATORY_CODE)
					.message(BoundaryConstants.CROSSHIERARCHY_CHILDID_MANADATORY_ERROR_MESSAGE)
					.field(BoundaryConstants.CROSSHIERARCHY_CHILDID_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		} else if(crossHierarchyRequest.getCrossHierarchy().getChild() != null && crossHierarchyRequest.getCrossHierarchy().getChild().getId()!=null){
			if (!(boundaryService.findByTenantIdAndId(crossHierarchyRequest.getCrossHierarchy().getChild().getId(), crossHierarchyRequest.getCrossHierarchy().getTenantId())!=null)){
				final ErrorField errorField = ErrorField.builder().code(BoundaryConstants.CROSSHIERARCHY_CHILDID_INVALID_CODE)
						.message(BoundaryConstants.CROSSHIERARCHY_CHILDID_INVALID_ERROR_MESSAGE)
						.field(BoundaryConstants.CROSSHIERARCHY_CHILDID_INVALID_FIELD_NAME).build();
				errorFields.add(errorField);
			}
		}
		return errorFields;
	}

	private List<ErrorField> addCrossHierarchyParentTypeValidationError(final CrossHierarchyRequest crossHierarchyRequest,
			final List<ErrorField> errorFields){
         if(crossHierarchyRequest.getCrossHierarchy().getParentType() != null && crossHierarchyRequest.getCrossHierarchy().getParentType().getId()!=null){
			if (!(boundaryTypeService.findByIdAndTenantId(Long.valueOf(crossHierarchyRequest.getCrossHierarchy().getParentType().getId()), crossHierarchyRequest.getCrossHierarchy().getTenantId())!=null)){
				final ErrorField errorField = ErrorField.builder().code(BoundaryConstants.CROSSHIERARCHY_PARENTTYPEID_INVALID_CODE)
						.message(BoundaryConstants.CROSSHIERARCHY_PARENTTYPEID_INVALID_ERROR_MESSAGE)
						.field(BoundaryConstants.CROSSHIERARCHY_PARENTTYPEID_INVALID_FIELD_NAME).build();
				errorFields.add(errorField);
			}
		}
		return errorFields;
	}
	
	private List<ErrorField> addCrossHierarchyChildTypeValidationError(final CrossHierarchyRequest crossHierarchyRequest,
			final List<ErrorField> errorFields){
         if(crossHierarchyRequest.getCrossHierarchy().getChildType() != null && crossHierarchyRequest.getCrossHierarchy().getChildType().getId()!=null){
			if (!(boundaryTypeService.findByIdAndTenantId(Long.valueOf(crossHierarchyRequest.getCrossHierarchy().getChildType().getId()), crossHierarchyRequest.getCrossHierarchy().getTenantId())!=null)){
				final ErrorField errorField = ErrorField.builder().code(BoundaryConstants.CROSSHIERARCHY_CHILDTYPEID_INVALID_CODE)
						.message(BoundaryConstants.CROSSHIERARCHY_CHILDTYPEID_INVALID_ERROR_MESSAGE)
						.field(BoundaryConstants.CROSSHIERARCHY_CHILDTYPEID_INVALID_FIELD_NAME).build();
				errorFields.add(errorField);
			}
		}
		return errorFields;
	}
}