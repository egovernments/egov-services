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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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

    @RequestMapping("/_creators")
    public ResponseEntity<?> searchUsers(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,final BindingResult bindingResult) {

        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
        if (bindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(bindingResult, requestInfo);

        List<User> receiptCreators = new ArrayList<User>();
        try {
            receiptCreators = receiptService.getReceiptCreators();
        } catch (final Exception exception) {
            LOGGER.error("Error while processing request " + receiptCreators, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return getSuccessResponse(receiptCreators, requestInfo);

    }

    @RequestMapping("/_status")
    public ResponseEntity<?> searchStatus(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,final BindingResult bindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
        if (bindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(bindingResult, requestInfo);

        List<String> statusList = new ArrayList<String>();
        try {
            statusList = receiptService.getReceiptStatus();
        } catch (final Exception exception) {
            LOGGER.error("Error while processing request " + statusList, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return  getStatusSuccessResponse(statusList, requestInfo);
    }

    private ResponseEntity<?> getStatusSuccessResponse(List<String> statusList, RequestInfo requestInfo) {
        LOGGER.info("Building success response.");
        StatusResponse statusResponse = new StatusResponse();
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        statusResponse.setStatus(statusList);
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
