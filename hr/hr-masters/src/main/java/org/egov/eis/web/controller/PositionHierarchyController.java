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

package org.egov.eis.web.controller;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.model.PositionHierarchy;
import org.egov.eis.service.PositionHierarchyService;
import org.egov.eis.web.contract.PositionHierarchyGetRequest;
import org.egov.eis.web.contract.PositionHierarchyResponse;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.egov.eis.web.errorhandlers.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/positionhierarchies")
public class PositionHierarchyController {

	private static final Logger logger = LoggerFactory.getLogger(PositionHierarchyController.class);

	@Autowired
	private PositionHierarchyService positionHierarchyService;

	@Autowired
	private ErrorHandler errHandler;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute @Valid PositionHierarchyGetRequest positionHierarchyGetRequest,
			BindingResult modelAttributeBindingResult, @RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			BindingResult requestBodyBindingResult) {
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		// validate input params
		if (modelAttributeBindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForMissingParameters(modelAttributeBindingResult, requestInfo);
		}

		// validate input params
		if (requestBodyBindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);
		}

		// Call service
		List<PositionHierarchy> positionHierarchiesList = null;
		try {
			positionHierarchiesList = positionHierarchyService.getPositionHierarchies(positionHierarchyGetRequest);
		} catch (Exception exception) {
			logger.error("Error while processing request " + positionHierarchyGetRequest, exception);
			return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
		}

		return getSuccessResponse(positionHierarchiesList, requestInfo);
	}

	/**
	 * Populate Response object and returnpositionHierarchiesList
	 * 
	 * @param positionHierarchiesList
	 * @return
	 */
	private ResponseEntity<?> getSuccessResponse(List<PositionHierarchy> positionHierarchiesList, RequestInfo requestInfo) {
		PositionHierarchyResponse positionHierarchyRes = new PositionHierarchyResponse();
		positionHierarchyRes.setPositionHierarchy(positionHierarchiesList);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		positionHierarchyRes.setResponseInfo(responseInfo);
		return new ResponseEntity<PositionHierarchyResponse>(positionHierarchyRes, HttpStatus.OK);

	}

}
