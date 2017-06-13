package org.egov.workflow.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.workflow.domain.model.EscalationHoursSearchCriteria;
import org.egov.workflow.domain.model.EscalationTimeType;
import org.egov.workflow.domain.service.EscalationService;
import org.egov.workflow.domain.util.PgrMasterConstants;
import org.egov.workflow.web.contract.EscalationHoursRequest;
import org.egov.workflow.web.contract.EscalationHoursResponse;
import org.egov.workflow.web.contract.EscalationTimeTypeReq;
import org.egov.workflow.web.contract.EscalationTimeTypeRes;
import org.egov.workflow.web.contract.factory.ResponseInfoFactory;
import org.egov.workflow.web.errorhandlers.ErrorResponse;
import org.egov.workflow.web.errorhandlers.Error;
import org.egov.workflow.web.validation.EscalationTimeTypeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/escalation-hours")
public class EscalationHoursController {

	private static final Logger logger = LoggerFactory.getLogger(EscalationHoursController.class);

	
    private EscalationService escalationService;
    
	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	
	@Autowired 
	private EscalationTimeTypeValidator escalationTimeTypeValidator;

    public EscalationHoursController(EscalationService escalationService) {
        this.escalationService = escalationService;
    }

    @PostMapping("/_search")
    public EscalationHoursResponse getEscalationDate(@RequestParam(value="tenantId",required=true) String tenantId,
                                                     @RequestParam("designationId") Long designationId,
                                                     @RequestParam("complaintTypeId") Long complaintTypeId,
                                                     @RequestBody EscalationHoursRequest escalationHoursRequest) {
        final EscalationHoursSearchCriteria searchCriteria = EscalationHoursSearchCriteria.builder()
            .tenantId(tenantId)
            .designationId(designationId)
            .complaintTypeId(complaintTypeId)
            .build();
        final int escalationHours = escalationService.getEscalationHours(searchCriteria);
        return new EscalationHoursResponse(null, escalationHours,tenantId);
    }
    
	@PostMapping(value = "/_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid final EscalationTimeTypeReq escalationTimeTypeRequest,
			final BindingResult errors) {
		if (errors.hasErrors()) {
			final ErrorResponse errRes = escalationTimeTypeValidator.populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}
		logger.info("EscalationTimeTypeRequest::" + escalationTimeTypeRequest);

		final List<ErrorResponse> errorResponses = escalationTimeTypeValidator.validateServiceGroupRequest(escalationTimeTypeRequest);
		if (!errorResponses.isEmpty())
			return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
		
		final EscalationTimeTypeReq escalationTypeRequest = escalationService.create(escalationTimeTypeRequest);
		if(null == escalationTypeRequest){
			Error error = new Error();
			error.setMessage(PgrMasterConstants.INVALID_ESCALATIONTIMETYPE_REQUEST_MESSAGE);
			
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setError(error);
			
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
			
		}
		
		final EscalationTimeType escalationType = escalationTypeRequest.getEscalationTimeType();
		final List<EscalationTimeType> escalationTimeTypes = new ArrayList<>();
		escalationTimeTypes.add(escalationType);
		return getSuccessResponse(escalationTimeTypes, escalationTimeTypeRequest.getRequestInfo());

	}
	
	@PostMapping(value = "/_update/{id}")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody @Valid final EscalationTimeTypeReq escalationTimeTypeRequest,
			@PathVariable("id") final long id, final BindingResult errors) {
		if (errors.hasErrors() || id == 0L) {
			final ErrorResponse errRes = escalationTimeTypeValidator.populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}
		escalationTimeTypeRequest.getEscalationTimeType().setId(id);
		logger.info("EscalationTimeTypeRequest::" + escalationTimeTypeRequest);

		final List<ErrorResponse> errorResponses = escalationTimeTypeValidator.validateServiceGroupRequest(escalationTimeTypeRequest);
		if (!errorResponses.isEmpty())
			return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
		
		final EscalationTimeTypeReq escalationTypeRequest = escalationService.update(escalationTimeTypeRequest);
		
		if(null == escalationTypeRequest){
			Error error = new Error();
			error.setMessage(PgrMasterConstants.INVALID_ESCALATIONTIMETYPE_REQUEST_MESSAGE);
			
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setError(error);
			
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
			
		}
		
		final EscalationTimeType escalationType = escalationTypeRequest.getEscalationTimeType();
		
		final List<EscalationTimeType> escalationTimeTypes = new ArrayList<>();
		escalationTimeTypes.add(escalationType);
		return getSuccessResponse(escalationTimeTypes, escalationTimeTypeRequest.getRequestInfo());

	}
	
	private ResponseEntity<?> getSuccessResponse(final List<EscalationTimeType> escalationTimeTypeList, final RequestInfo requestInfo) {
		final EscalationTimeTypeRes escalationTimeTypeRes = new EscalationTimeTypeRes();
		final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		escalationTimeTypeRes.setResponseInfo(responseInfo);
		escalationTimeTypeRes.setEscalationTimeTypes(escalationTimeTypeList);
		return new ResponseEntity<>(escalationTimeTypeRes, HttpStatus.OK);

	}

}
