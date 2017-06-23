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

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.PropertyTypePipeSize;
import org.egov.wcms.service.PropertyTypePipeSizeTypeService;
import org.egov.wcms.util.ValidatorUtils;
import org.egov.wcms.web.contract.PropertyTypePipeSizeGetRequest;
import org.egov.wcms.web.contract.PropertyTypePipeSizeRequest;
import org.egov.wcms.web.contract.PropertyTypePipeSizeResponse;
import org.egov.wcms.web.contract.RequestInfoWrapper;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/propertytype-pipesize")
public class PopertyTypePipeSizeController {

    private static final Logger logger = LoggerFactory.getLogger(PopertyTypePipeSizeController.class);

    @Autowired
    private PropertyTypePipeSizeTypeService propertPipeSizeService;

    @Autowired
    private ErrorHandler errHandler;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ValidatorUtils validatorUtils;

    @PostMapping(value = "/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final PropertyTypePipeSizeRequest propertyPipeSizeRequest,
            final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = validatorUtils.populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("propertyPipeSizeRequest::" + propertyPipeSizeRequest);

        final List<ErrorResponse> errorResponses = validatorUtils.validatePropertyPipeSizeRequest(propertyPipeSizeRequest);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

        final PropertyTypePipeSize propertyPipeSize = propertPipeSizeService.createPropertyPipeSize(
                applicationProperties.getCreatePropertyPipeSizeTopicName(), "propertypipesize-create", propertyPipeSizeRequest);
        final List<PropertyTypePipeSize> propertyPipeSizes = new ArrayList<>();
        propertyPipeSizes.add(propertyPipeSize);
        return getSuccessResponse(propertyPipeSizes, "created", propertyPipeSizeRequest.getRequestInfo());

    }

    @PostMapping(value = "/{propertyPipeSizeId}/_update")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody @Valid final PropertyTypePipeSizeRequest propertyPipeSizeRequest,
            final BindingResult errors,
            @PathVariable("propertyPipeSizeId") final Long propertyPipeSizeId) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = validatorUtils.populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("propertyPipeSizeRequest::" + propertyPipeSizeRequest);
        propertyPipeSizeRequest.getPropertyPipeSize().setId(propertyPipeSizeId);

        final List<ErrorResponse> errorResponses = validatorUtils.validatePropertyPipeSizeRequest(propertyPipeSizeRequest);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

        final PropertyTypePipeSize propertyPipeSize = propertPipeSizeService.createPropertyPipeSize(
                applicationProperties.getUpdatePropertyPipeSizeTopicName(), "propertypipesize-update", propertyPipeSizeRequest);
        final List<PropertyTypePipeSize> propertyPipeSizes = new ArrayList<>();
        propertyPipeSizes.add(propertyPipeSize);
        return getSuccessResponse(propertyPipeSizes, null, propertyPipeSizeRequest.getRequestInfo());
    }

    @PostMapping("_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid final PropertyTypePipeSizeGetRequest propertyPipeSizeGetRequest,
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
        List<PropertyTypePipeSize> propertyPipeSizeList = null;
        try {
            propertyPipeSizeList = propertPipeSizeService.getPropertyPipeSizes(propertyPipeSizeGetRequest);
        } catch (final Exception exception) {
            logger.error("Error while processing request " + propertyPipeSizeGetRequest, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return getSuccessResponse(propertyPipeSizeList, null, requestInfo);

    }

    private ResponseEntity<?> getSuccessResponse(final List<PropertyTypePipeSize> propertyPipeSizeList,
            final String mode, final RequestInfo requestInfo) {
        final PropertyTypePipeSizeResponse propertyPipeSizeResponse = new PropertyTypePipeSizeResponse();
        propertyPipeSizeResponse.setPropertyPipeSizes(propertyPipeSizeList);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        if (StringUtils.isNotBlank(mode))
            responseInfo.setStatus(HttpStatus.CREATED.toString());
        else
            responseInfo.setStatus(HttpStatus.OK.toString());
        propertyPipeSizeResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(propertyPipeSizeResponse, HttpStatus.OK);

    }

}
