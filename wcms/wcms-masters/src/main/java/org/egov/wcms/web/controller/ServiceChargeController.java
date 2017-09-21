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
package org.egov.wcms.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.model.ServiceCharge;
import org.egov.wcms.service.ServiceChargeService;
import org.egov.wcms.util.ValidatorUtils;
import org.egov.wcms.web.contract.RequestInfoWrapper;
import org.egov.wcms.web.contract.ServiceChargeGetRequest;
import org.egov.wcms.web.contract.ServiceChargeReq;
import org.egov.wcms.web.contract.ServiceChargeRes;
import org.egov.wcms.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.web.errorhandlers.ErrorHandler;
import org.egov.wcms.web.errorhandlers.ErrorResponse;
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
@RequestMapping("/serviceCharges")
public class ServiceChargeController {
    public static final Logger logger = LoggerFactory.getLogger(ServiceChargeController.class);

    @Autowired
    private ValidatorUtils validatorUtils;

    @Autowired
    private ServiceChargeService serviceChargeService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private ErrorHandler errHandler;

    @PostMapping(value = "/_create")
    @ResponseBody
    public ResponseEntity<?> createServiceCharges(@RequestBody final ServiceChargeReq serviceChargeRequest,
            final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = validatorUtils.populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("ServiceChargeReq::" + serviceChargeRequest);

        final List<ErrorResponse> errorResponses = validatorUtils.validateServiceChargeRequest(serviceChargeRequest, false);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        final List<ServiceCharge> listOfServiceCharges = serviceChargeService
                .pushCreateToQueue(serviceChargeRequest);
        return getSuccessResponse(listOfServiceCharges, "Created", serviceChargeRequest.getRequestInfo());
    }

    @PostMapping(value = "/_update")
    @ResponseBody
    public ResponseEntity<?> updateServiceCharges(@RequestBody final ServiceChargeReq serviceChargeRequest,
            final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = validatorUtils.populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("ServiceChargeReq::" + serviceChargeRequest);

        final List<ErrorResponse> errorResponses = validatorUtils.validateServiceChargeRequest(serviceChargeRequest, true);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        final List<ServiceCharge> listOfServiceCharges = serviceChargeService
                .pushUpdateToQueue(serviceChargeRequest);
        return getSuccessResponse(listOfServiceCharges, null, serviceChargeRequest.getRequestInfo());

    }

    @PostMapping(value = "/_search")
    @ResponseBody
    public ResponseEntity<?> searchServiceCharges(@ModelAttribute @Valid final ServiceChargeGetRequest serviceChargeGetRequest,
            final BindingResult modelAttributeBindingResult, @RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult requestBodyBindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
        if (modelAttributeBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingParameters(modelAttributeBindingResult, requestInfo);
        if (requestBodyBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);
        final List<ServiceCharge> listOfServiceCharge = serviceChargeService.getServiceChargesByCriteria(serviceChargeGetRequest);
        return getSuccessResponse(listOfServiceCharge, null, requestInfo);

    }

    private ResponseEntity<?> getSuccessResponse(final List<ServiceCharge> serviceCharges,
            final String mode, final RequestInfo requestInfo) {
        final ServiceChargeRes serviceChargeResponse = new ServiceChargeRes();
        serviceChargeResponse.setServiceCharge(serviceCharges);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        if (StringUtils.isNotBlank(mode))
            responseInfo.setStatus(HttpStatus.CREATED.toString());
        else
            responseInfo.setStatus(HttpStatus.OK.toString());
        serviceChargeResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(serviceChargeResponse, HttpStatus.OK);
    }

}
