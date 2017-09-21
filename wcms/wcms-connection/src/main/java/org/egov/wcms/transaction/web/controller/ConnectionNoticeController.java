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

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wcms.transaction.config.ApplicationProperties;
import org.egov.wcms.transaction.model.EstimationNotice;
import org.egov.wcms.transaction.model.WorkOrderFormat;
import org.egov.wcms.transaction.service.ConnectionNoticeService;
import org.egov.wcms.transaction.utils.ConnectionUtils;
import org.egov.wcms.transaction.web.contract.RequestInfoWrapper;
import org.egov.wcms.transaction.web.contract.WaterConnectionGetReq;
import org.egov.wcms.transaction.web.errorhandler.ErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ConnectionNoticeController {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ErrorHandler errHandler;

    @Autowired
    private ConnectionUtils connectionUtils;

    @Autowired
    private ConnectionNoticeService connectionNoticeService;

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
        return connectionUtils.getSuccessResponseForEstimationNotice(estimationNotice, requestInfo);
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
        return connectionUtils.getSuccessResponseForWorkOrder(workOrder, requestInfo);
    }

}
