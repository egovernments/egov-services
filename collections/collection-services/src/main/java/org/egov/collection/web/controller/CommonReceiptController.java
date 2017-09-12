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

import org.egov.collection.model.EnumData;
import org.egov.collection.service.CollectionConfigService;
import org.egov.collection.service.ReceiptService;
import org.egov.collection.web.contract.*;
import org.egov.collection.web.contract.factory.RequestInfoWrapper;
import org.egov.collection.web.contract.factory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
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
import java.util.Map;

@RestController
@RequestMapping("/receipts")
public class CommonReceiptController {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(CommonReceiptController.class);

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private ReceiptService receiptService;

	@Autowired
	private CollectionConfigService collectionConfigService;

	@RequestMapping("/_getDistinctCollectedBy")
	public ResponseEntity<?> searchDistinctCreators(
			@RequestParam final String tenantId,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
			final BindingResult bindingResult) {

		final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
		List<User> receiptCreators = receiptService.getReceiptCreators(requestInfo,
				tenantId);

		return getSuccessResponse(receiptCreators, requestInfo);

	}

	@RequestMapping("/_status")
	public ResponseEntity<?> searchStatus(@RequestParam final String tenantId,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
			final BindingResult bindingResult) {
		final List<EnumData> modelList = new ArrayList<>();
			List<String> statusList = receiptService.getReceiptStatus(tenantId);
			for (final String name : statusList)
				modelList.add(new EnumData(name, name));
		return getStatusSuccessResponse(modelList,
				requestInfoWrapper.getRequestInfo());
	}

	@RequestMapping("/_getDistinctBusinessDetails")
	public ResponseEntity<?> getDistinctBusinessDetails(
			@RequestParam final String tenantId,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
			final BindingResult bindingResult) {
		final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
		List<BusinessDetailsRequestInfo> businessDetails = receiptService.getBusinessDetails(tenantId,
					requestInfo);
		return getBusinessDetailsSuccessResponse(businessDetails, requestInfo);
	}

	@RequestMapping("/_getChartOfAccounts")
	public ResponseEntity<?> getChartOfAccounts(
			@RequestParam final String tenantId,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
			final BindingResult bindingResult) {
		final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
		List<ChartOfAccount> chartOfAccounts = receiptService.getChartOfAccountsForByGlCodes(
					tenantId, requestInfo);
		return getChartOfAccountsResponse(chartOfAccounts, requestInfo);
	}

	@RequestMapping("/_manualReceiptDetailsRequiredOrNot")
	public ResponseEntity<?> checkIfManualReceiptDetailsRequiredOrNot(
			@ModelAttribute final CollectionConfigGetRequest collectionConfigGetRequest,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
			final BindingResult bindingResult) {

		final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
		Map<String, List<String>> collectionConfigKeyValMap = collectionConfigService
					.getCollectionConfiguration(collectionConfigGetRequest);
		return getManualReceiptResponse(collectionConfigKeyValMap, requestInfo);
	}

	private ResponseEntity<?> getManualReceiptResponse(
			Map<String, List<String>> collectionConfigKeyValMap,
			RequestInfo requestInfo) {
		LOGGER.info("Building success response.");
		CollectionConfigResponse collectionConfigResponse = new CollectionConfigResponse();
		final ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		collectionConfigResponse
				.setCollectionConfiguration(collectionConfigKeyValMap);
		collectionConfigResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<>(collectionConfigResponse, HttpStatus.OK);
	}

	private ResponseEntity<?> getChartOfAccountsResponse(
			List<ChartOfAccount> chartOfAccounts, RequestInfo requestInfo) {
		LOGGER.info("Building success response.");
		ChartOfAccountsResponse chartOfAccountsResponse = new ChartOfAccountsResponse();
		final ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		chartOfAccountsResponse.setChartOfAccounts(chartOfAccounts);
		chartOfAccountsResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<>(chartOfAccountsResponse, HttpStatus.OK);
	}

	private ResponseEntity<?> getBusinessDetailsSuccessResponse(
			List<BusinessDetailsRequestInfo> businessDetailsList,
			RequestInfo requestInfo) {
		LOGGER.info("Building success response.");
		BusinessDetailsResponse businessDetailsResponse = new BusinessDetailsResponse();
		final ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		businessDetailsResponse.setBusinessDetails(businessDetailsList);
		businessDetailsResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<>(businessDetailsResponse, HttpStatus.OK);
	}

	private ResponseEntity<?> getStatusSuccessResponse(
			final List<EnumData> statusList, final RequestInfo requestInfo) {
		final StatusResponse response = new StatusResponse();
		final ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		response.setResponseInfo(responseInfo);
		response.setStatus(statusList);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	private ResponseEntity<?> getSuccessResponse(List<User> users,
			RequestInfo requestInfo) {
		LOGGER.info("Building success response.");
		UserResponse userResponse = new UserResponse();
		final ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		userResponse.setReceiptCreators(users);
		userResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<>(userResponse, HttpStatus.OK);
	}

}
