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
import org.egov.pgr.domain.model.ReceivingCenterType;
import org.egov.pgr.service.ReceivingCenterTypeService;
import org.egov.pgr.util.CommonValidation;
import org.egov.pgr.util.PgrMasterConstants;
import org.egov.pgr.web.contract.ReceivingCenterTypeGetReq;
import org.egov.pgr.web.contract.ReceivingCenterTypeReq;
import org.egov.pgr.web.contract.ReceivingCenterTypeRes;
import org.egov.pgr.web.contract.RequestInfoWrapper;
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
@RequestMapping("/receivingcenter")
public class ReceivingCenterTypeController {

    private static final Logger logger = LoggerFactory.getLogger(ReceivingCenterTypeController.class);
    public static final String CODE = "code";
    public static final String MESSAGE = "message";
    public static final String FIELD = "field";

    @Autowired
    private ReceivingCenterTypeService receivingCenterService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ErrorHandler errHandler;

    @Autowired
    private CommonValidation commonValidation;

    HashMap<String, String> receivingCenterException = new HashMap<>();


    @PostMapping(value = "/v1/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final ReceivingCenterTypeReq centerTypeRequest,
                                    final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("ReceivingCenterType Create : Request::" + centerTypeRequest);

        validateReceivingCenterRequest(centerTypeRequest, true, "create");


        final ReceivingCenterType ReceivingCenter = receivingCenterService.sendMessage(applicationProperties.getCreateReceivingCenterTopicName(), applicationProperties.getCreateReceivingCenterTopicKey(), centerTypeRequest);
        final List<ReceivingCenterType> ReceivingCenters = new ArrayList<>();
        ReceivingCenters.add(ReceivingCenter);
        return getSuccessResponse(ReceivingCenters, centerTypeRequest.getRequestInfo());

    }

    @PostMapping(value = "/v1/_update")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody @Valid final ReceivingCenterTypeReq centerTypeRequest, final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("ReceivingCenterType Update : Request::" + centerTypeRequest);

        validateReceivingCenterRequest(centerTypeRequest, false, "update");


        final ReceivingCenterType documentType = receivingCenterService.sendMessage(applicationProperties.getUpdateReceivingCenterTopicName(),
                applicationProperties.getUpdateReceivingCenterTopicKey(), centerTypeRequest);
        final List<ReceivingCenterType> centerTypes = new ArrayList<>();
        centerTypes.add(documentType);
        return getSuccessResponse(centerTypes, centerTypeRequest.getRequestInfo());
    }

    @PostMapping("/v1/_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid final ReceivingCenterTypeGetReq centerTypeGetRequest,
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
        List<ReceivingCenterType> centerTypeList = null;
        try {
            centerTypeList = receivingCenterService.getAllReceivingCenterTypes(centerTypeGetRequest);
        } catch (final Exception exception) {
            logger.error("Error while processing request " + centerTypeGetRequest, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return getSuccessResponse(centerTypeList, requestInfo);

    }


    private void validateReceivingCenterRequest(final ReceivingCenterTypeReq receivingCenterRequest, boolean flag, String mode) {
        addReceivingCenterNameAndCodeValidationErrors(receivingCenterRequest, flag, mode);
        checkReceivingCenterNameUniqueness(receivingCenterRequest, mode);
        addTenantIdValidationErrors(receivingCenterRequest);
        commonValidation(receivingCenterRequest);
    }

    private ErrorResponse populateErrors(final BindingResult errors) {
        final ErrorResponse errRes = new ErrorResponse();

        final Error error = new Error();
        error.setCode(1);
        error.setDescription("Error while binding request");
        if (errors.hasFieldErrors())
            for (final FieldError fieldError : errors.getFieldErrors())
                error.getFields().put(fieldError.getField(), fieldError.getRejectedValue());
        errRes.setError(error);
        return errRes;
    }

    private void addReceivingCenterNameAndCodeValidationErrors(final ReceivingCenterTypeReq receivingCenterRequest, boolean flag, String mode) {
        final ReceivingCenterType receivingCenter = receivingCenterRequest.getCenterType();
        if (receivingCenter.getName() == null || receivingCenter.getName().isEmpty()) {
            receivingCenterException.put(CODE, PgrMasterConstants.RECEIVINGCENTER_NAME_MANDATORY_CODE);
            receivingCenterException.put(MESSAGE, PgrMasterConstants.RECEIVINGCENTER_NAME_MANADATORY_ERROR_MESSAGE);
            receivingCenterException.put(FIELD, PgrMasterConstants.RECEIVINGCENTER_NAME_MANADATORY_FIELD_NAME);
            throw new PGRMasterException(receivingCenterException);
        }
        if (receivingCenter.getCode() == null || receivingCenter.getCode().isEmpty()) {
            receivingCenterException.put(CODE, PgrMasterConstants.RECEIVINGCENTER_CODE_MANDATORY_CODE);
            receivingCenterException.put(MESSAGE, PgrMasterConstants.RECEIVINGCENTER_CODE_MANADATORY_ERROR_MESSAGE);
            receivingCenterException.put(FIELD, PgrMasterConstants.RECEIVINGCENTER_CODE_MANADATORY_FIELD_NAME);
            throw new PGRMasterException(receivingCenterException);
        }

        if (receivingCenterService.checkReceivingCenterCodeName(receivingCenter.getCode(), receivingCenter.getName(), receivingCenter.getTenantId(), mode)) {
            receivingCenterException.put(CODE, PgrMasterConstants.CODENAME_UNIQUE_CODE);
            receivingCenterException.put(MESSAGE, PgrMasterConstants.CODENAME_UNIQUE_ERROR_MESSAGE);
            receivingCenterException.put(FIELD, PgrMasterConstants.CODENAME_UNIQUE_FIELD_NAME);
            throw new PGRMasterException(receivingCenterException);
        }

        if (flag && !receivingCenterService.checkReceivingCenterTypeByCode(receivingCenter.getCode(), receivingCenter.getTenantId())) {
            receivingCenterException.put(CODE, PgrMasterConstants.RECEIVINGCENTER_CODE_EXISTS_CODE);
            receivingCenterException.put(MESSAGE, PgrMasterConstants.RECEIVINGCENTER_CODE_EXISTS_ERROR_MESSAGE);
            receivingCenterException.put(FIELD, PgrMasterConstants.RECEIVINGCENTER_CODE_EXISTS_FIELD_NAME);
            throw new PGRMasterException(receivingCenterException);
        }

    }

    private void checkReceivingCenterNameUniqueness(final ReceivingCenterTypeReq receivingCenterRequest, String mode) {
        final ReceivingCenterType receivingCenter = receivingCenterRequest.getCenterType();
        if (receivingCenterService.checkReceivingCenterNameExists(receivingCenter, mode)) {
            receivingCenterException.put(CODE, PgrMasterConstants.RECEIVINGCENTER_NAME_UNIQUE_CODE);
            receivingCenterException.put(MESSAGE, PgrMasterConstants.RECEIVINGCENTER_UNQ_ERROR_MESSAGE);
            receivingCenterException.put(FIELD, PgrMasterConstants.RECEIVINGCENTER_NAME_UNQ_FIELD_NAME);
            throw new PGRMasterException(receivingCenterException);
        }
    }

    private void addTenantIdValidationErrors(final ReceivingCenterTypeReq receivingCenterRequest) {
        final ReceivingCenterType receivingCenter = receivingCenterRequest.getCenterType();
        if (receivingCenter.getTenantId() == null || receivingCenter.getTenantId().isEmpty()) {
            receivingCenterException.put(CODE, PgrMasterConstants.TENANTID_MANDATORY_CODE);
            receivingCenterException.put(MESSAGE, PgrMasterConstants.TENANTID_MANADATORY_ERROR_MESSAGE);
            receivingCenterException.put(FIELD, PgrMasterConstants.TENANTID_MANADATORY_FIELD_NAME);
            throw new PGRMasterException(receivingCenterException);
        }

    }

    private void commonValidation(ReceivingCenterTypeReq receivingCenterRequest) {
        commonValidation.validateCode(receivingCenterRequest.getCenterType().getCode());
        commonValidation.validateCodeLength(receivingCenterRequest.getCenterType().getCode());
        commonValidation.validateName(receivingCenterRequest.getCenterType().getName());
        commonValidation.validateNameLength(receivingCenterRequest.getCenterType().getName());
        commonValidation.validateDescriptionLength(receivingCenterRequest.getCenterType().getDescription());
    }


    private ResponseEntity<?> getSuccessResponse(final List<ReceivingCenterType> centerList,
                                                 final RequestInfo requestInfo) {
        final ReceivingCenterTypeRes receivingCenterResponse = new ReceivingCenterTypeRes();
        receivingCenterResponse.setCenterTypes(centerList);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        receivingCenterResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(receivingCenterResponse, HttpStatus.OK);

    }

}
