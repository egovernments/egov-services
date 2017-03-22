package org.egov.eis.web.validator;

import org.egov.eis.web.contract.RequestInfo;
import org.egov.eis.web.errorhandler.ErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class RequestValidator {

	@Autowired
	private ErrorHandler errHandler;

	/**
	 * Validate RequestInfo object & returns ErrorResponseEntity if there are
	 * any errors or else returns null
	 * 
	 * @param requestInfo
	 * @param headers
	 * @param bindingResult
	 * @return ResponseEntity<?>
	 */
	public ResponseEntity<?> validateSearchRequest(RequestInfo requestInfo, HttpHeaders headers,
			BindingResult bindingResult) {
		// validate header
		if (requestInfo.getApiId() == null || requestInfo.getVer() == null || requestInfo.getTs() == null) {
			return errHandler.getErrorResponseEntityForMissingRequestInfo(requestInfo, headers);
		}

		// validate input params
		if (bindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForMissingParameters(bindingResult, requestInfo, headers);
		}
		return null;
	}
}
