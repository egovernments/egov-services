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
package org.egov.wcms.transaction.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.transaction.config.ApplicationProperties;
import org.egov.wcms.transaction.model.Connection;
import org.egov.wcms.transaction.model.EstimationNotice;
import org.egov.wcms.transaction.model.WorkOrderFormat;
import org.egov.wcms.transaction.service.ConnectionNoticeService;
import org.egov.wcms.transaction.service.WaterConnectionService;
import org.egov.wcms.transaction.util.WcmsConnectionConstants;
import org.egov.wcms.transaction.validator.ConnectionValidator;
import org.egov.wcms.transaction.web.contract.EstimationNoticeRes;
import org.egov.wcms.transaction.web.contract.RequestInfoWrapper;
import org.egov.wcms.transaction.web.contract.WaterConnectionGetReq;
import org.egov.wcms.transaction.web.contract.WaterConnectionReq;
import org.egov.wcms.transaction.web.contract.WaterConnectionRes;
import org.egov.wcms.transaction.web.contract.WorkOrderRes;
import org.egov.wcms.transaction.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.transaction.web.errorhandler.Error;
import org.egov.wcms.transaction.web.errorhandler.ErrorHandler;
import org.egov.wcms.transaction.web.errorhandler.ErrorResponse;
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

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/connection")
@Slf4j
public class WaterConnectionController {

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ErrorHandler errHandler;

    @Autowired
    private ConnectionValidator connectionValidator;
    
    @Autowired
    private ConnectionNoticeService  connectionNoticeService;

    @Autowired
    private WaterConnectionService waterConnectionService;

    @PostMapping(value = "/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final WaterConnectionReq waterConnectionRequest,
            final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = connectionValidator.populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        log.info("WaterConnectionRequest ::" + waterConnectionRequest);
        waterConnectionService.beforePersistTasks(waterConnectionRequest);
        final List<ErrorResponse> errorResponses = connectionValidator
                .validateWaterConnectionRequest(waterConnectionRequest);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

        waterConnectionService.generateIdsForWaterConnectionRequest(waterConnectionRequest);
        waterConnectionService.persistBeforeKafkaPush(waterConnectionRequest);
        final List<Connection> connectionList = new ArrayList<>();
        if(waterConnectionRequest.getConnection().getId() > 0) { 
            Connection	connection = waterConnectionService.pushConnectionToKafka(
                    applicationProperties.getCreateNewConnectionTopicName(), "newconnection-create",
                    waterConnectionRequest);
            connectionList.add(waterConnectionService.afterPersistTasks(waterConnectionRequest, connection));
        }
        // Return success or error response based on the status of persistence
        return errorMessageOnConnectionSuccessAndFailure(waterConnectionRequest, connectionList);
    }



    private ResponseEntity<?> errorMessageOnConnectionSuccessAndFailure(final WaterConnectionReq waterConnectionRequest,
            final List<Connection> connectionList) {
        if (waterConnectionRequest.getConnection().getId() > 0)
            return getSuccessResponse(connectionList, waterConnectionRequest.getRequestInfo());
        else {
            final ErrorResponse errorResponse = new ErrorResponse();
            final Error error = new Error();
            error.setDescription(WcmsConnectionConstants.CONNECTION_PERSIST_FAILURE);
            errorResponse.setError(error);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

   

    @PostMapping(value = "/_update")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody @Valid final WaterConnectionReq waterConnectionRequest,
            final BindingResult errors) {
       /* if (errors.hasErrors()) {

            throw new CustomBindException(errors, waterConnectionRequest.getRequestInfo());
        }
        return updateConnection(waterConnectionRequest, errors);*/
        return null;
    }

    
    @PostMapping("/_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid final WaterConnectionGetReq waterConnectionGetReq,
            final BindingResult modelAttributeBindingResult, @RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult requestBodyBindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        // validate input params
        if (modelAttributeBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingParameters(modelAttributeBindingResult, requestInfo);

        // validate input params
        if (requestBodyBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);

        log.info("Request Received for Search Water Connection : " + waterConnectionGetReq);

        // Call service
        List<Connection> connectionList = null;
        try {
            connectionList = waterConnectionService.getConnectionDetails(waterConnectionGetReq, requestInfo);
        } catch (final Exception exception) {
            log.error("Error while processing request " + waterConnectionGetReq, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }
        return getSuccessResponse(connectionList, requestInfo);
    }

    @PostMapping("/_getEstimationNotice")
    @ResponseBody
    public ResponseEntity<?> getEstimationNotice(@ModelAttribute @Valid final WaterConnectionGetReq waterConnectionGetReq,
            final BindingResult modelAttributeBindingResult, @RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult requestBodyBindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        // validate input params
        if (modelAttributeBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingParameters(modelAttributeBindingResult, requestInfo);

        // validate input params
        if (requestBodyBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);

        // Call service
        EstimationNotice estimationNotice = new EstimationNotice();
        try {
            estimationNotice = connectionNoticeService.getEstimationNotice(applicationProperties.getEstimationNoticeTopicName(),
                    applicationProperties.getEstimationNoticeTopicKey(), waterConnectionGetReq, requestInfo);
        } catch (final Exception exception) {
            log.error("Error while processing request " + waterConnectionGetReq, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }
        return getSuccessResponseForEstimationNotice(estimationNotice, requestInfo);
    }

    @PostMapping("/_getWorkOrder")
    @ResponseBody
    public ResponseEntity<?> getWorkOrder(@ModelAttribute @Valid final WaterConnectionGetReq waterConnectionGetReq,
            final BindingResult modelAttributeBindingResult, @RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult requestBodyBindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        // validate input params
        if (modelAttributeBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingParameters(modelAttributeBindingResult, requestInfo);

        // validate input params
        if (requestBodyBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);

        // Call service
        WorkOrderFormat workOrder = new WorkOrderFormat();
        try {
            workOrder = connectionNoticeService.getWorkOrder(applicationProperties.getWorkOrderTopicName(),
                    applicationProperties.getWorkOrderTopicKey(), waterConnectionGetReq, requestInfo);
        } catch (final Exception exception) {
            log.error("Error while processing request " + waterConnectionGetReq, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }
        return getSuccessResponseForWorkOrder(workOrder, requestInfo);
    }

    private ResponseEntity<?> getSuccessResponse(final List<Connection> connectionList,
            final RequestInfo requestInfo) {
        final WaterConnectionRes waterConnectionRes = new WaterConnectionRes();
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        waterConnectionRes.setResponseInfo(responseInfo);
        waterConnectionRes.setConnections(connectionList);
        return new ResponseEntity<>(waterConnectionRes, HttpStatus.OK);

    }

    private ResponseEntity<?> getSuccessResponseForEstimationNotice(final EstimationNotice estimationNotice,
            final RequestInfo requestInfo) {
        final EstimationNoticeRes estimationNoticeRes = new EstimationNoticeRes();
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        estimationNoticeRes.setResponseInfo(responseInfo);
        estimationNoticeRes.setEstimationNotice(estimationNotice);
        return new ResponseEntity<>(estimationNoticeRes, HttpStatus.OK);

    }

    private ResponseEntity<?> getSuccessResponseForWorkOrder(final WorkOrderFormat workOrder,
            final RequestInfo requestInfo) {
        final WorkOrderRes workOrderRes = new WorkOrderRes();
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        workOrderRes.setResponseInfo(responseInfo);
        workOrderRes.setWorkOrder(workOrder);
        return new ResponseEntity<>(workOrderRes, HttpStatus.OK);

    }

}
