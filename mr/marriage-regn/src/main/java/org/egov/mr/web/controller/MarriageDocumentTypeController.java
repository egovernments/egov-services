package org.egov.mr.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.mr.model.MarriageDocumentType;
import org.egov.mr.service.MarriageDocumentTypeService;
import org.egov.mr.web.contract.MarriageDocTypeResponse;
import org.egov.mr.web.contract.MarriageDocumentTypeSearchCriteria;
import org.egov.mr.web.contract.RequestInfo;
import org.egov.mr.web.contract.RequestInfoWrapper;
import org.egov.mr.web.contract.ResponseInfo;
import org.egov.mr.web.errorhandler.ErrorHandler;
import org.egov.mr.web.validator.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/marriageRegns/documents")
public class MarriageDocumentTypeController {

	public static final Logger LOGGER = LoggerFactory.getLogger(RegistrationUnitController.class);

	@Autowired
	private RequestValidator requestValidator;

	@Autowired
	private ErrorHandler errorHandler;

	@Autowired
	private MarriageDocumentTypeService marriageDocumentTypeService;

	@PostMapping
	@RequestMapping("/_search")
	public ResponseEntity<?> search(@RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			BindingResult bindingResultsForRequestInfoWrapper,
			@ModelAttribute @Valid MarriageDocumentTypeSearchCriteria marriageDocumentTypeSearchCriteria,
			BindingResult bindingResultForRegnDocumentTypeSearchCriteria) {

		LOGGER.info("requestInfoWrapper : " + requestInfoWrapper);
		LOGGER.info("regnDocumentTypeSearchCriteria : " + marriageDocumentTypeSearchCriteria);

		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
		// Validation
		ResponseEntity<?> errorResponseEntity = requestValidator.validateSearchRequest(requestInfo,
				bindingResultsForRequestInfoWrapper, bindingResultForRegnDocumentTypeSearchCriteria);
		if (errorResponseEntity != null)
			return errorResponseEntity;
		// Entering service method
		List<MarriageDocumentType> marriageDocTypesList = new ArrayList<>();
		try {
			marriageDocTypesList = marriageDocumentTypeService.search(marriageDocumentTypeSearchCriteria);
		} catch (Exception e) {
			LOGGER.info(" Error While Procssing the Request!");
			return errorHandler.getRegistrationUnitForUnExpectedErrorResponse(bindingResultsForRequestInfoWrapper,
					requestInfo, "Encountered : " + e.getMessage());
		}

		if (marriageDocTypesList.isEmpty()) {
			return errorHandler.getRegistrationUnitEmptyListErrorResponse(bindingResultsForRequestInfoWrapper,
					requestInfo);
		}
		return getSuccessResponse(marriageDocTypesList, requestInfo);
	}

	// returning the Response to method in the same class
	private ResponseEntity<?> getSuccessResponse(List<MarriageDocumentType> marriageDocTypesList,
			RequestInfo requestInfo) {
		// Setting ResponseInfo
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setApiId(requestInfo.getApiId());
		responseInfo.setKey(requestInfo.getKey());
		responseInfo.setResMsgId(requestInfo.getMsgId());
		responseInfo.setStatus(HttpStatus.OK.toString());
		responseInfo.setTenantId(requestInfo.getTenantId());
		responseInfo.setTs(requestInfo.getTs());
		responseInfo.setVer(requestInfo.getVer());

		// Setting regnUnitResponse responseInfo
		MarriageDocTypeResponse marriageDocTypeResponse = new MarriageDocTypeResponse();
		marriageDocTypeResponse.setResponseInfo(responseInfo);
		// Setting regnUnitResponse registrationUnitsList
		marriageDocTypeResponse.setMarriageDocTypes(marriageDocTypesList);
		return new ResponseEntity<MarriageDocTypeResponse>(marriageDocTypeResponse, HttpStatus.OK);
	}
}
