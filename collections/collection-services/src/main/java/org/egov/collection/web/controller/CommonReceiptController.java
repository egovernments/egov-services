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

import org.egov.collection.service.ReceiptService;
import org.egov.collection.web.contract.*;
import org.egov.collection.web.contract.factory.RequestInfoWrapper;
import org.egov.collection.web.contract.factory.ResponseInfoFactory;
import org.egov.collection.web.errorhandlers.ErrorHandler;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/receipts")
public class CommonReceiptController {

    public static final Logger LOGGER = LoggerFactory.getLogger(CommonReceiptController.class);

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private ErrorHandler errHandler;

    @Autowired
    private ReceiptService receiptService;

    @RequestMapping("/_getDistinctCollectedBy")
    public ResponseEntity<?> searchDistinctCreators(@RequestParam final String tenantId,@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,final BindingResult bindingResult) {

        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
        if (bindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(bindingResult, requestInfo);

        List<User> receiptCreators = new ArrayList<User>();
        try {
            receiptCreators = receiptService.getReceiptCreators(requestInfo,tenantId);
        } catch (final Exception exception) {
            LOGGER.error("Error while processing request " + receiptCreators, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return getSuccessResponse(receiptCreators, requestInfo);

    }

    @RequestMapping("/_status")
    public ResponseEntity<?> searchStatus(@RequestParam final String tenantId,@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,final BindingResult bindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
        if (bindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(bindingResult, requestInfo);

        Map<String, String> statusMap = new HashMap<>();
        try {
            List<String> statusList = receiptService.getReceiptStatus(tenantId);
            statusMap = statusList.stream().distinct().collect(
                    Collectors.toMap(s -> s, s -> s));
        } catch (final Exception exception) {
            LOGGER.error("Error while processing request " + statusMap, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return  getStatusSuccessResponse(statusMap, requestInfo);
    }

    @RequestMapping("/_getDistinctBusinessDetails")
    public ResponseEntity<?> getDistinctBusinessDetails(@RequestParam final String tenantId,@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,final BindingResult bindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
        if (bindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(bindingResult, requestInfo);

        List<BusinessDetailsRequestInfo> businessDetails = new ArrayList<BusinessDetailsRequestInfo>();
        try {
            businessDetails = receiptService.getBusinessDetails(tenantId,requestInfo);
        } catch (final Exception exception) {
            LOGGER.error("Error while processing request " + businessDetails, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return  getBusinessDetailsSuccessResponse(businessDetails, requestInfo);
    }

    @RequestMapping("/_getChartOfAccounts")
    public ResponseEntity<?> getChartOfAccounts(@RequestParam final String tenantId,@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,final BindingResult bindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
        if (bindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(bindingResult, requestInfo);

        List<ChartOfAccount> chartOfAccounts = new ArrayList<ChartOfAccount>();
        try {
            chartOfAccounts = receiptService.getChartOfAccountsForByGlCodes(tenantId,requestInfo);
        } catch(final Exception e) {
            LOGGER.error("Error while processing request " + chartOfAccounts, e);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }
        return getChartOfAccountsResponse(chartOfAccounts,requestInfo);
    }

    private ResponseEntity<?> getChartOfAccountsResponse(List<ChartOfAccount> chartOfAccounts, RequestInfo requestInfo) {
        LOGGER.info("Building success response.");
        ChartOfAccountsResponse chartOfAccountsResponse = new ChartOfAccountsResponse();
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        chartOfAccountsResponse.setChartOfAccounts(chartOfAccounts);
        chartOfAccountsResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(chartOfAccountsResponse, HttpStatus.OK);
    }

    private ResponseEntity<?> getBusinessDetailsSuccessResponse(List<BusinessDetailsRequestInfo> businessDetailsList, RequestInfo requestInfo) {
        LOGGER.info("Building success response.");
        BusinessDetailsResponse businessDetailsResponse = new BusinessDetailsResponse();
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        businessDetailsResponse.setBusinessDetails(businessDetailsList);
        businessDetailsResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(businessDetailsResponse, HttpStatus.OK);
    }

    private ResponseEntity<?> getStatusSuccessResponse(Map<String,String> statusMap, RequestInfo requestInfo) {
        LOGGER.info("Building success response.");
        StatusResponse statusResponse = new StatusResponse();
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        statusResponse.setStatus(statusMap);
        statusResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    private ResponseEntity<?> getSuccessResponse(List<User> users, RequestInfo requestInfo) {
        LOGGER.info("Building success response.");
        UserResponse userResponse = new UserResponse();
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        userResponse.setReceiptCreators(users);
        userResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

}
