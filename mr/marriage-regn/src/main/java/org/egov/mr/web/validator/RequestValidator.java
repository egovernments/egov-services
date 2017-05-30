package org.egov.mr.web.validator;

import org.egov.mr.web.contract.RequestInfo;
import org.egov.mr.web.errorhandler.ErrorHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class RequestValidator {

	// Response being returned to controller
	public ResponseEntity<?> validateSearchRequest(RequestInfo requestInfo,
			BindingResult bindingResultsRegistrationUnit, BindingResult bindingResultsForSearchCriteria) {
		ErrorHandler errHandler = new ErrorHandler();
		if (bindingResultsRegistrationUnit != null && bindingResultsRegistrationUnit.hasErrors()) {
			return errHandler.getRegistrationUnitMissingParameterErrorResponse(bindingResultsRegistrationUnit,
					requestInfo);
		}
		if (bindingResultsForSearchCriteria != null && bindingResultsForSearchCriteria.hasErrors()) {
			return errHandler.getRegistrationUnitMissingParameterErrorResponse(bindingResultsForSearchCriteria,
					requestInfo);
		}
		return null;
	}

}
