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

package org.egov.pgr.web.controller;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.pgr.config.ApplicationProperties;
import org.egov.pgr.domain.exception.PGRMasterException;
import org.egov.pgr.domain.model.ServiceGroup;
import org.egov.pgr.service.ServiceGroupService;
import org.egov.pgr.util.CommonValidation;
import org.egov.pgr.util.PgrMasterConstants;
import org.egov.pgr.web.contract.RequestInfoWrapper;
import org.egov.pgr.web.contract.ServiceGroupGetRequest;
import org.egov.pgr.web.contract.ServiceGroupRequest;
import org.egov.pgr.web.contract.ServiceGroupResponse;
import org.egov.pgr.web.contract.factory.ResponseInfoFactory;
import org.egov.pgr.web.errorhandlers.Error;
import org.egov.pgr.web.errorhandlers.ErrorHandler;
import org.egov.pgr.web.errorhandlers.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/serviceGroup")
public class ServiceGroupController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceGroupController.class);
    public static final String CODE = "code";
    public static final String MESSAGE = "message";
    public static final String FIELD = "field";

    @Autowired
    private ServiceGroupService serviceGroupService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private ApplicationProperties applicationProperties;

    private static final String[] taskAction = {"create", "update"};

    @Autowired
    private ErrorHandler errHandler;

    @Autowired
    private CommonValidation commonValidation;

    HashMap<String, String> serviceGroupException = new HashMap<>();

    @PostMapping(value = "/v1/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final ServiceGroupRequest serviceGroupRequest,
                                    final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("serviceGroup Create : Request::" + serviceGroupRequest);

        validateServiceGroupRequest(serviceGroupRequest, taskAction[0]);

        final ServiceGroup serviceGroup = serviceGroupService.createCategory(
                applicationProperties.getCreateServiceGroupTopicName(),
                applicationProperties.getCreateServiceGroupTopicKey(), serviceGroupRequest);
        final List<ServiceGroup> serviceGroups = new ArrayList<>();
        serviceGroups.add(serviceGroup);
        return getSuccessResponse(serviceGroups, serviceGroupRequest.getRequestInfo());

    }

    @PostMapping(value = "/v1/_update")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody @Valid final ServiceGroupRequest serviceGroupRequest
            , final BindingResult errors) {
        if (errors.hasErrors() || (null == serviceGroupRequest.getServiceGroup().getCode() || serviceGroupRequest.getServiceGroup().getCode().isEmpty())) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("serviceGroup Update : Request::" + serviceGroupRequest);
        serviceGroupRequest.getServiceGroup().setCode(serviceGroupRequest.getServiceGroup().getCode());
        validateServiceGroupRequest(serviceGroupRequest, taskAction[1]);

        final ServiceGroup category = serviceGroupService.updateCategory(
                applicationProperties.getUpdateServiceGroupTopicName(),
                applicationProperties.getUpdateServiceGroupTopicKey(), serviceGroupRequest);
        final List<ServiceGroup> categories = new ArrayList<>();
        categories.add(category);
        return getSuccessResponse(categories, serviceGroupRequest.getRequestInfo());

    }

    @PostMapping("/v1/_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid final ServiceGroupGetRequest serviceGroupGetRequest,
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
        List<ServiceGroup> serviceGroupList = null;
        try {
            serviceGroupList = serviceGroupService.getAllServiceGroup(serviceGroupGetRequest);
        } catch (final Exception exception) {
            logger.error("Error while processing request " + serviceGroupGetRequest, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }
        return getSuccessResponse(serviceGroupList, requestInfo);
    }

    private void validateServiceGroupRequest(final ServiceGroupRequest serviceGroupRequest, String action) {
        addServiceGroupNameValidationErrors(serviceGroupRequest);
        addTenantIdValidation(serviceGroupRequest);
        verifyRequestUniqueness(serviceGroupRequest, action);
        verifyIfNameAlreadyExists(serviceGroupRequest, action);
        verifyIfCodeAlreadyExists(serviceGroupRequest, action);
        commonValidation(serviceGroupRequest.getServiceGroup());
    }

    private void commonValidation(ServiceGroup serviceGroup) {
        commonValidation.validateCode(serviceGroup.getCode());
        commonValidation.validateCodeLength(serviceGroup.getCode());
        commonValidation.validateName(serviceGroup.getName());
        commonValidation.validateNameLength(serviceGroup.getName());
        commonValidation.validateDescriptionLength(serviceGroup.getDescription());
    }

    private void addServiceGroupNameValidationErrors(final ServiceGroupRequest serviceGroupRequest) {
        final ServiceGroup serviceGroup = serviceGroupRequest.getServiceGroup();
        if (serviceGroup.getName() == null || serviceGroup.getName().isEmpty()) {
            serviceGroupException.put(CODE, PgrMasterConstants.SERVICEGROUP_NAME_MANDATORY_CODE);
            serviceGroupException.put(MESSAGE, PgrMasterConstants.SERVICEGROUP_NAME_MANADATORY_ERROR_MESSAGE);
            serviceGroupException.put(FIELD, PgrMasterConstants.SERVICEGROUP_NAME_MANADATORY_FIELD_NAME);
            throw new PGRMasterException(serviceGroupException);
        }
        if (serviceGroup.getCode() == null || serviceGroup.getCode().isEmpty()) {
            serviceGroupException.put(CODE, PgrMasterConstants.SERVICEGROUP_CODE_MANDATORY_CODE);
            serviceGroupException.put(MESSAGE, PgrMasterConstants.SERVICEGROUP_CODE_MANADATORY_ERROR_MESSAGE);
            serviceGroupException.put(FIELD, PgrMasterConstants.SERVICEGROUP_CODE_MANADATORY_FIELD_NAME);
            throw new PGRMasterException(serviceGroupException);
        }
    }

    private void addTenantIdValidation(final ServiceGroupRequest serviceGroupRequest) {
        final ServiceGroup serviceGroup = serviceGroupRequest.getServiceGroup();
        if (serviceGroup.getTenantId() == null || serviceGroup.getTenantId().isEmpty()) {
            serviceGroupException.put(CODE, PgrMasterConstants.TENANTID_MANDATORY_CODE);
            serviceGroupException.put(MESSAGE, PgrMasterConstants.TENANTID_MANADATORY_ERROR_MESSAGE);
            serviceGroupException.put(FIELD, PgrMasterConstants.TENANTID_MANADATORY_FIELD_NAME);
            throw new PGRMasterException(serviceGroupException);
        }
    }

    private void verifyRequestUniqueness(final ServiceGroupRequest serviceGroupRequest, String action) {
        if (serviceGroupService.verifyRequestUniqueness(serviceGroupRequest, action)) {
            serviceGroupException.put(CODE, PgrMasterConstants.CODENAME_UNIQUE_CODE);
            serviceGroupException.put(MESSAGE, PgrMasterConstants.CODENAME_UNIQUE_ERROR_MESSAGE);
            serviceGroupException.put(FIELD, PgrMasterConstants.CODENAME_UNIQUE_FIELD_NAME);
            throw new PGRMasterException(serviceGroupException);
        }
    }

    private void verifyIfNameAlreadyExists(final ServiceGroupRequest serviceGroupRequest, String action) {
        if (serviceGroupService.verifyIfNameAlreadyExists(serviceGroupRequest, action)) {
            serviceGroupException.put(CODE, PgrMasterConstants.SERVICEGROUP_NAME_UNIQUE_CODE);
            serviceGroupException.put(MESSAGE, PgrMasterConstants.SERVICEGROUP_NAME_ERROR_MESSAGE);
            serviceGroupException.put(FIELD, PgrMasterConstants.SERVICEGROUP_NAME_FIELD_NAME);
            throw new PGRMasterException(serviceGroupException);
        }
    }

    private void verifyIfCodeAlreadyExists(final ServiceGroupRequest serviceGroupRequest, String action) {
        if (serviceGroupService.verifyIfCodeAlreadyExists(serviceGroupRequest, action)) {
            serviceGroupException.put(CODE, PgrMasterConstants.SERVICEGROUP_CODE_UNIQUE_CODE);
            serviceGroupException.put(MESSAGE, PgrMasterConstants.SERVICEGROUP_CODE_ERROR_MESSAGE);
            serviceGroupException.put(FIELD, PgrMasterConstants.SERVICEGROUP_CODE_FIELD_NAME);
            throw new PGRMasterException(serviceGroupException);
        }
    }

    private ErrorResponse populateErrors(final BindingResult errors) {
        final ErrorResponse errRes = new ErrorResponse();

        final Error error = new Error();
        error.setCode(1);
        error.setDescription("Error while binding request. Ensure id is passed if you're updating a record.");
        if (errors.hasFieldErrors())
            for (final FieldError fieldError : errors.getFieldErrors())
                error.getFields().put(fieldError.getField(), fieldError.getRejectedValue());
        errRes.setError(error);
        return errRes;
    }

    private ResponseEntity<?> getSuccessResponse(final List<ServiceGroup> serviceGroupList, final RequestInfo requestInfo) {
        final ServiceGroupResponse serviceGroupResponse = new ServiceGroupResponse();
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        serviceGroupResponse.setResponseInfo(responseInfo);
        serviceGroupResponse.setServiceGroups(serviceGroupList);
        return new ResponseEntity<>(serviceGroupResponse, HttpStatus.OK);

    }

}