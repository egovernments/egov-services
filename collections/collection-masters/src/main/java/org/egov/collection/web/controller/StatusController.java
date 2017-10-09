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
package org.egov.collection.web.controller;

import org.egov.collection.domain.model.StatusCriteria;
import org.egov.collection.domain.service.StatusService;
import org.egov.collection.web.contract.RequestInfoWrapper;
import org.egov.collection.web.contract.StatusGetRequest;
import org.egov.collection.web.contract.StatusResponse;
import org.egov.collection.web.contract.factory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/collectionStatus")
public class StatusController {
	private static final Logger logger = LoggerFactory.getLogger(StatusController.class);

	@Autowired
	StatusService statusService;
	@Autowired
	ResponseInfoFactory responseInfoFactory;

	@PostMapping(value = "/_search")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> getCollectionStatusesByCriteria(
			@ModelAttribute @Valid final StatusGetRequest statusRequest,
			final BindingResult modelAttributeBindingResult,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
			final BindingResult requestBodyBindingResult) {
		StatusCriteria criteria = StatusCriteria.builder().code(statusRequest.getCode())
				.objectType(statusRequest.getObjectType()).tenantId(statusRequest.getTenantId()).build();
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		List<org.egov.collection.web.contract.Status> contractStatus = new ArrayList<>();
		try {
			contractStatus = statusService.getStatuses(criteria).stream()
					.map(org.egov.collection.web.contract.Status::new).collect(Collectors.toList());
		} catch (final Exception exception) {
			logger.error("Error while processing request " + statusRequest, exception);
		}

		return getSuccessResponse(requestInfo, contractStatus);
	}

	private ResponseEntity<?> getSuccessResponse(RequestInfo requestInfo,
			List<org.egov.collection.web.contract.Status> contractStatus) {
		StatusResponse response = new StatusResponse();
		response.setStatus(contractStatus);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		response.setResponseInfo(responseInfo);
		return new ResponseEntity<StatusResponse>(response, HttpStatus.OK);
	}

}
