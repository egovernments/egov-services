package org.egov.workflow.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.workflow.domain.model.EscalationHoursSearchCriteria;
import org.egov.workflow.domain.model.EscalationTimeType;
import org.egov.workflow.domain.service.EscalationService;
import org.egov.workflow.util.PgrMasterConstants;
import org.egov.workflow.web.contract.EscalationHoursRequest;
import org.egov.workflow.web.contract.EscalationHoursResponse;
import org.egov.workflow.web.contract.EscalationTimeTypeGetReq;
import org.egov.workflow.web.contract.EscalationTimeTypeReq;
import org.egov.workflow.web.contract.EscalationTimeTypeRes;
import org.egov.workflow.web.contract.RequestInfoWrapper;
import org.egov.workflow.web.contract.factory.ResponseInfoFactory;
import org.egov.workflow.web.errorhandlers.Error;
import org.egov.workflow.web.errorhandlers.ErrorHandler;
import org.egov.workflow.web.errorhandlers.ErrorResponse;
import org.egov.workflow.web.validation.EscalationTimeTypeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/escalation-hours")
public class EscalationHoursController {

	private static final Logger logger = LoggerFactory.getLogger(EscalationHoursController.class);

	
    private EscalationService escalationService;
    
	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	
	@Autowired 
	private EscalationTimeTypeValidator escalationTimeTypeValidator;
	
	@Autowired
    private ErrorHandler errHandler;

    public EscalationHoursController(EscalationService escalationService) {
        this.escalationService = escalationService;
    }

    @PostMapping("/disabledAPI/_search")
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
    
	@PostMapping(value = "/v1/_create")
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
			error.setCode(Integer.parseInt(HttpStatus.BAD_REQUEST.toString()));
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setError(error);
			
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
			
		}
		
		final EscalationTimeType escalationType = escalationTypeRequest.getEscalationTimeType();
		final List<EscalationTimeType> escalationTimeTypes = new ArrayList<>();
		escalationTimeTypes.add(escalationType);
		return getSuccessResponse(escalationTimeTypes, escalationTimeTypeRequest.getRequestInfo());

	}
	
	@PostMapping(value = "/v1/_update/{id}")
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
	@PostMapping("/v1/_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid final EscalationTimeTypeGetReq escTimeTypeGetRequest,
            final BindingResult modelAttributeBindingResult, @RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult requestBodyBindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        // validate input params
        if (modelAttributeBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingParameters(modelAttributeBindingResult, requestInfo);

        // validate input params
        if (requestBodyBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);

        // Call service
        List<EscalationTimeType> escalationTypeList = null;
        try {
        	escalationTypeList = escalationService.getAllEscalationTimeTypes(escTimeTypeGetRequest);
        } catch (final Exception exception) {
            logger.error("Error while processing request " + escTimeTypeGetRequest, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return getSuccessResponse(escalationTypeList, requestInfo);

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
