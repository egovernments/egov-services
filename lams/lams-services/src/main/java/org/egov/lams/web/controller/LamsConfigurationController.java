/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.lams.web.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.egov.lams.service.LamsConfigurationService;
import org.egov.lams.web.contract.LamsConfigurationGetRequest;
import org.egov.lams.web.contract.LamsConfigurationResponse;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.RequestInfoWrapper;
import org.egov.lams.web.contract.ResponseInfo;
import org.egov.lams.web.contract.factory.ResponseInfoFactory;
import org.egov.lams.web.errorhandlers.ErrorHandler;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lamsconfigurations")
public class LamsConfigurationController {

	private static final Logger logger = LoggerFactory.getLogger(LamsConfigurationController.class);

	@Autowired
	private LamsConfigurationService lamsConfigurationService;

	@Autowired
	private ErrorHandler errHandler;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute @Valid LamsConfigurationGetRequest lamsConfigurationGetRequest,
			BindingResult bindingResult, @RequestBody RequestInfoWrapper requestInfoWrapper) {
		
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
		// validate header
		if(requestInfo.getApiId() == null || requestInfo.getVer() == null || requestInfo.getTs() == null ) {
			return errHandler.getErrorResponseEntityForMissingRequestInfo(requestInfo);
		}
		// validate input params
		if (bindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForMissingParameters(bindingResult, requestInfo);
		}
		// Call service
		Map<String, List<String>> lamsConfigurationKeyValuesList = null;
		try {
			lamsConfigurationKeyValuesList = lamsConfigurationService.getLamsConfigurations(lamsConfigurationGetRequest);
		} catch (Exception exception) {
			logger.error("Error while processing request " + lamsConfigurationGetRequest, exception);
			return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
		}

		return getSuccessResponse(lamsConfigurationKeyValuesList, requestInfo);
	}

	/**
	 * Populate Response object and returnlamsConfigurationsList
	 * 
	 * @param lamsConfigurationsList
	 * @return
	 */
	private ResponseEntity<?> getSuccessResponse(Map<String, List<String>> lamsConfigurationKeyValuesList, RequestInfo requestInfo) {
		LamsConfigurationResponse lamsConfigurationRes = new LamsConfigurationResponse();
		lamsConfigurationRes.setLamsConfiguration(lamsConfigurationKeyValuesList);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		lamsConfigurationRes.setResponseInfo(responseInfo);
		System.err.println(lamsConfigurationRes);
		return new ResponseEntity<>(lamsConfigurationRes, HttpStatus.OK);

	}

}
