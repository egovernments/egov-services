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

import org.egov.collection.config.CollectionServiceConstants;
import org.egov.collection.exception.CustomException;
import org.egov.collection.model.*;
import org.egov.collection.service.ReceiptService;
import org.egov.collection.util.ReceiptReqValidator;
import org.egov.collection.web.contract.*;
import org.egov.collection.web.contract.factory.RequestInfoWrapper;
import org.egov.collection.web.contract.factory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.common.contract.response.ResponseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {
    public static final Logger LOGGER = LoggerFactory
            .getLogger(ReceiptController.class);

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private ReceiptReqValidator receiptReqValidator;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @PostMapping("/_search")
    @ResponseBody
    public ResponseEntity<?> search(
            @ModelAttribute ReceiptSearchGetRequest receiptGetRequest,
            final BindingResult modelAttributeBindingResult,
            @RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult requestBodyBindingResult) {

        LOGGER.info("Request: " + receiptGetRequest.toString());
        LOGGER.info("RequestInfo: " + requestInfoWrapper.toString());

        ReceiptSearchCriteria searchCriteria = ReceiptSearchCriteria.builder()
                .businessCode(receiptGetRequest.getBusinessCode())
                .classification(receiptGetRequest.getClassification())
                .collectedBy(receiptGetRequest.getCollectedBy())
                .consumerCode(receiptGetRequest.getConsumerCode())
                .fromDate(receiptGetRequest.getFromDate())
                .toDate(receiptGetRequest.getToDate())
                .paymentType(receiptGetRequest.getPaymentType())
                .receiptNumbers(receiptGetRequest.getReceiptNumbers())
                .receiptDetailsRequired(receiptGetRequest.isReceiptDetailsRequired())
                .status(receiptGetRequest.getStatus()).pageSize(receiptGetRequest.getPageSize())
                .tenantId(receiptGetRequest.getTenantId()).offset(receiptGetRequest.getOffset())
                .sortBy(receiptGetRequest.getSortBy()).billIds(receiptGetRequest.getBillIds())
                .sortOrder(receiptGetRequest.getSortOrder()).manualReceiptNumbers(receiptGetRequest.getManualReceiptNumbers())
                .transactionId(receiptGetRequest.getTransactionId()).build();

        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
        final ErrorResponse errorResponse = receiptReqValidator
                .validateSearchReceiptRequest(receiptGetRequest);

        if (errorResponse != null)
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

        Pagination<ReceiptHeader> modelReceiptHeaders = receiptService.getReceipts(searchCriteria,
                requestInfo);
        ReceiptRes receiptResponse = new ReceiptRes();
        final ResponseInfo responseInfo = responseInfoFactory
                .createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        receiptResponse.setReceipts(modelReceiptHeaders != null ? new ReceiptCommonModel(modelReceiptHeaders.getPagedData()).toDomainContract() : Collections.EMPTY_LIST);
        receiptResponse.setPage(new PaginationContract(modelReceiptHeaders));
        receiptResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(receiptResponse, HttpStatus.OK);
    }

    @PostMapping("/_view")
    @ResponseBody
    public List<Receipt> view(
            @ModelAttribute ReceiptSearchGetRequest receiptGetRequest) {

        ReceiptSearchCriteria searchCriteria = ReceiptSearchCriteria.builder()
                .businessCode(receiptGetRequest.getBusinessCode())
                .classification(receiptGetRequest.getClassification())
                .collectedBy(receiptGetRequest.getCollectedBy())
                .consumerCode(receiptGetRequest.getConsumerCode())
                .fromDate(receiptGetRequest.getFromDate())
                .toDate(receiptGetRequest.getToDate())
                .paymentType(receiptGetRequest.getPaymentType())
                .receiptNumbers(receiptGetRequest.getReceiptNumbers())
                .status(receiptGetRequest.getStatus())
                .tenantId(receiptGetRequest.getTenantId())
                .sortBy(receiptGetRequest.getSortBy())
                .sortOrder(receiptGetRequest.getSortOrder()).build();

        List<Receipt> receipts = new ArrayList<>();
        try {
            receipts = (List<Receipt>) receiptService.getReceipts(searchCriteria, null);
        } catch (final Exception exception) {
            LOGGER.error("Error while processing request " + receiptGetRequest,
                    exception);
        }
        return receipts;
    }

    @PostMapping("/_cancel")
    @ResponseBody
    public ResponseEntity<?> cancelReceipt(
            @RequestBody ReceiptReq receiptRequest, BindingResult errors) {
        final ErrorResponse errorResponse = receiptReqValidator
                .validatecreateReceiptRequest(receiptRequest);
        if (errorResponse != null)
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

        List<Receipt> receipt = receiptService
                .cancelReceiptPushToQueue(receiptRequest);
        return getSuccessResponse(receipt, receiptRequest.getRequestInfo());
    }

    @PostMapping("/_create")
    @ResponseBody
    public ResponseEntity<?> create(
            @RequestBody ReceiptReq receiptRequest, BindingResult errors) {
        LOGGER.info("Request: " + receiptRequest.toString());
        ErrorResponse errorResponse = receiptReqValidator
                .validatecreateReceiptRequest(receiptRequest);
        if (errorResponse != null)
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        Receipt receiptInfo = null;
        try {
            receiptInfo = receiptService
                    .apportionAndCreateReceipt(receiptRequest);
        } catch (CustomException e) {
            LOGGER.error("Exception Message: " , e);
            Error error = new Error();
            final ResponseInfo responseInfo = responseInfoFactory
                    .createResponseInfoFromRequestInfo(
                            receiptRequest.getRequestInfo(), true);
            error.setCode(Integer.valueOf(e.getCode().toString()));
            error.setMessage(e.getCustomMessage());
            error.setDescription(e.getDescription());
            errorResponse = new ErrorResponse();
            errorResponse.setError(error);
            errorResponse.setResponseInfo(responseInfo);

            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (ValidationException e) {
            LOGGER.error("Exception Message: " , e);
            Error error = new Error();
            final ResponseInfo responseInfo = responseInfoFactory
                    .createResponseInfoFromRequestInfo(
                            receiptRequest.getRequestInfo(), true);
            error.setCode(Integer.valueOf(HttpStatus.BAD_REQUEST.toString()));
            error.setMessage(e.getMessage());
            error.setDescription("Validation Exception");
            errorResponse = new ErrorResponse();
            errorResponse.setError(error);
            errorResponse.setResponseInfo(responseInfo);

            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        List<Receipt> receipts = new ArrayList<>();
        receipts.add(receiptInfo);

        return getSuccessResponse(receipts, receiptRequest.getRequestInfo());
    }

    @PostMapping("/_update")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody @Valid ReceiptReq receiptReq, BindingResult errors) {

        Receipt receipt = receiptReq.getReceipt().get(0);
            for (Receipt receipts : receiptReq.getReceipt()) {
                WorkflowDetailsRequest workFlowRequest = receiptReq.getReceipt().get(0)
                        .getWorkflowDetails();
                workFlowRequest.setReceiptHeaderId(Long.valueOf(receipts.getId()));
                workFlowRequest.setTenantId(receipt.getTenantId());
                workFlowRequest.setRequestInfo(receiptReq.getRequestInfo());
                if (!validator(workFlowRequest.getTenantId(),
                        workFlowRequest.getReceiptHeaderId())) {
                    LOGGER.info("Invalid TenantId");
                    Error error = new Error();
                    error.setCode(Integer.parseInt(HttpStatus.BAD_REQUEST.toString()));
                    error.setMessage(CollectionServiceConstants.TENANT_ID_REQUIRED_MESSAGE);
                    ErrorResponse errorResponse = new ErrorResponse();
                    errorResponse.setError(error);

                    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
                }
                /*WorkflowDetailsRequest workFlowDetailsRequest = workFlowService
                        .update(workFlowRequest);*/
              //  receipts.setWorkflowDetails(workFlowDetailsRequest);

        }

        return getSuccessResponse(receiptReq.getReceipt(), receiptReq.getRequestInfo());
    }

    @PostMapping(value = "/_legacycreate")
    @ResponseBody
    public ResponseEntity<?> createLegacyReceipt(@RequestBody @Valid final LegacyReceiptReq legacyReceiptRequest,
            final BindingResult errors) {
        final List<ErrorResponse> errorResponses = receiptReqValidator
                .validateCreateLegacyReceiptRequest(legacyReceiptRequest);
        if (null != errorResponses && !errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        LOGGER.info("LegacyReceiptReq :" + legacyReceiptRequest.toString());
        List<LegacyReceiptHeader> listOfLegacyReceipts = receiptService.persistAndPushToQueue(legacyReceiptRequest);
        return getSuccessResponseForLegacy(listOfLegacyReceipts, legacyReceiptRequest.getRequestInfo());
    }

    @PostMapping(value = "/_legacysearch")
    @ResponseBody
    public ResponseEntity<?> searchLegacyReceipt(@ModelAttribute @Valid final LegacyReceiptGetReq legacyReceiptGetReq,
            final BindingResult modelAttributeBindingResult, @RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult requestInfoBindingResult) {
        List<LegacyReceiptHeader> legacyReceiptHeaders = receiptService.getLegacyReceiptsByCriteria(legacyReceiptGetReq);
        return getSuccessResponseForLegacy(legacyReceiptHeaders, requestInfoWrapper.getRequestInfo());
    }

    private ResponseEntity<?> getSuccessResponseForLegacy(List<LegacyReceiptHeader> listOfLegacyReceipts,
            RequestInfo requestInfo) {
        LOGGER.info("Building success response.");
        LegacyReceiptRes legacyReceiptResponse = new LegacyReceiptRes();
        final ResponseInfo responseInfo = responseInfoFactory
                .createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        legacyReceiptResponse.setLegacyReceipts(listOfLegacyReceipts);
        legacyReceiptResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(legacyReceiptResponse, HttpStatus.OK);
    }

    private boolean validator(String tenantId, long receiptHeaderId) {
        boolean isTenantValid = true;
        if (null == tenantId || tenantId.isEmpty() || receiptHeaderId == 0L)
            isTenantValid = false;
        return isTenantValid;
    }

    private ResponseEntity<?> getSuccessResponse(List<Receipt> receipts,
            RequestInfo requestInfo) {
        LOGGER.info("Building success response.");
        ReceiptRes receiptResponse = new ReceiptRes();
        final ResponseInfo responseInfo = responseInfoFactory
                .createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        receiptResponse.setReceipts(receipts);
        receiptResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(receiptResponse, HttpStatus.OK);
    }
}