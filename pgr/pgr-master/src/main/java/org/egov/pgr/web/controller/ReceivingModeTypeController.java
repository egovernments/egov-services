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
import org.egov.pgr.domain.model.ReceivingModeType;
import org.egov.pgr.domain.model.enums.ChannelType;
import org.egov.pgr.service.ReceivingModeTypeService;
import org.egov.pgr.util.CommonValidation;
import org.egov.pgr.util.PgrMasterConstants;
import org.egov.pgr.web.contract.ReceivingModeTypeGetReq;
import org.egov.pgr.web.contract.ReceivingModeTypeReq;
import org.egov.pgr.web.contract.ReceivingModeTypeRes;
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
@RequestMapping("/receivingmode")
public class ReceivingModeTypeController {

    private static final Logger logger = LoggerFactory.getLogger(ReceivingModeTypeController.class);
    public static final String CODE = "code";
    public static final String MESSAGE = "message";
    public static final String FIELD = "field";

    @Autowired
    private ReceivingModeTypeService modeTypeService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ErrorHandler errHandler;

    @Autowired
    private CommonValidation commonValidation;

    HashMap<String, String> receivingModeException = new HashMap<>();

    @PostMapping(value = "/v1/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final ReceivingModeTypeReq ModeTypeRequest,
                                    final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("ReceivingModeType Create : Request::" + ModeTypeRequest);

        validateReceivingModeRequest(ModeTypeRequest, "create");

        final ReceivingModeType ReceivingMode = modeTypeService.sendMessage(
                applicationProperties.getCreateReceivingModeTopicName(),
                applicationProperties.getCreateReceivingModeTopicKey(), ModeTypeRequest);
        final List<ReceivingModeType> ReceivingModes = new ArrayList<>();
        ReceivingModes.add(ReceivingMode);
        return getSuccessResponse(ReceivingModes, ModeTypeRequest.getRequestInfo());

    }

    @PostMapping(value = "/v1/_update")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody @Valid final ReceivingModeTypeReq modeTypeRequest,
                                    final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("ReceivingMode Update : Request::" + modeTypeRequest);

        validateReceivingModeRequest(modeTypeRequest, "update");

        final ReceivingModeType modeType = modeTypeService.sendMessage(
                applicationProperties.getUpdateReceivingModeTopicName(),
                applicationProperties.getUpdateReceivingModeTopicKey(), modeTypeRequest);
        final List<ReceivingModeType> modeTypes = new ArrayList<>();
        modeTypes.add(modeType);
        return getSuccessResponse(modeTypes, modeTypeRequest.getRequestInfo());
    }

    @PostMapping("/v1/_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid final ReceivingModeTypeGetReq modeTypeGetRequest,
                                    final BindingResult modelAttributeBindingResult,
                                    @RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
                                    final BindingResult requestBodyBindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        // validate input params
        if (modelAttributeBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingParameters(modelAttributeBindingResult, requestInfo);

        // validate input params
        if (requestBodyBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);

        // Call service
        List<ReceivingModeType> centerTypeList = null;
        try {
            centerTypeList = modeTypeService.getAllReceivingModeTypes(modeTypeGetRequest);
        } catch (final Exception exception) {
            logger.error("Error while processing request " + modeTypeGetRequest, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return getSuccessResponse(centerTypeList, requestInfo);

    }

    private ResponseEntity<?> getSuccessResponse(final List<ReceivingModeType> modeList,
                                                 final RequestInfo requestInfo) {
        final ReceivingModeTypeRes receivingModeResponse = new ReceivingModeTypeRes();
        receivingModeResponse.setModeTypes(modeList);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        receivingModeResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(receivingModeResponse, HttpStatus.OK);

    }

    private void validateReceivingModeRequest(final ReceivingModeTypeReq receivingModeRequest,
                                              String mode) {
        validate(receivingModeRequest, mode);
        addTenantIdValidationErrors(receivingModeRequest);
        addChannelValidationErrors(receivingModeRequest);
    }

    private void commonValidation(ReceivingModeType receivingModeRequest) {
        commonValidation.validateCode(receivingModeRequest.getCode());
        commonValidation.validateCodeLength(receivingModeRequest.getCode());
        commonValidation.validateName(receivingModeRequest.getName());
        commonValidation.validateNameLength(receivingModeRequest.getName());
        commonValidation.validateDescriptionLength(receivingModeRequest.getDescription());
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

    private void validate(final ReceivingModeTypeReq receivingModeRequest, String mode) {
        final ReceivingModeType receivingMode = receivingModeRequest.getModeType();
        if (receivingMode.getCode() == null || receivingMode.getCode().isEmpty()) {
            receivingModeException.put(CODE, PgrMasterConstants.RECEIVINGMODE_CODE_MANDATORY_CODE);
            receivingModeException.put(MESSAGE, PgrMasterConstants.RECEIVINGMODE_CODE_MANADATORY_ERROR_MESSAGE);
            receivingModeException.put(FIELD, PgrMasterConstants.RECEIVINGMODE_CODE_MANADATORY_FIELD_NAME);
            throw new PGRMasterException(receivingModeException);
        }
        if (receivingMode.getName() == null || receivingMode.getName().isEmpty()) {
            receivingModeException.put(CODE, PgrMasterConstants.RECEIVINGMODE_NAME_MANDATORY_CODE);
            receivingModeException.put(MESSAGE, PgrMasterConstants.RECEIVINGMODE_NAME_MANADATORY_ERROR_MESSAGE);
            receivingModeException.put(FIELD, PgrMasterConstants.RECEIVINGMODE_NAME_MANADATORY_FIELD_NAME);
            throw new PGRMasterException(receivingModeException);
        }

        if (modeTypeService.checkReceivingModeTypeByNameAndCode(receivingMode.getCode(),
                receivingMode.getName(), receivingMode.getTenantId(), mode)) {
            receivingModeException.put(CODE, PgrMasterConstants.CODENAME_UNIQUE_CODE);
            receivingModeException.put(MESSAGE, PgrMasterConstants.CODENAME_UNIQUE_ERROR_MESSAGE);
            receivingModeException.put(FIELD, PgrMasterConstants.CODENAME_UNIQUE_FIELD_NAME);
            throw new PGRMasterException(receivingModeException);
        }

        if (modeTypeService.checkReceivingModeTypeByName(receivingMode.getCode(), receivingMode.getName(),
                receivingMode.getTenantId(), mode)) {
            receivingModeException.put(CODE, PgrMasterConstants.RECEIVINGMODE_NAME_UNIQUE_CODE);
            receivingModeException.put(MESSAGE, PgrMasterConstants.RECEIVINGMODE_NAME_UNIQUE_ERROR_MESSAGE);
            receivingModeException.put(FIELD, PgrMasterConstants.RECEIVINGMODE_NAME_UNIQUE_FIELD_NAME);
            throw new PGRMasterException(receivingModeException);
        }

        if (modeTypeService.checkReceivingModeTypeByCode(receivingMode.getCode(), receivingMode.getTenantId(), mode)) {
            receivingModeException.put(CODE, PgrMasterConstants.RECEIVINGMODE_CODE_UNIQUE_CODE);
            receivingModeException.put(MESSAGE, PgrMasterConstants.RECEIVINGMODE_UNQ_ERROR_MESSAGE);
            receivingModeException.put(FIELD, PgrMasterConstants.RECEIVINGMODE_CODE_UNQ_FIELD_NAME);
            throw new PGRMasterException(receivingModeException);
        }

        if (receivingMode.getDescription() != null && !receivingMode.getDescription().isEmpty() && !(receivingMode.getDescription().length() > 0 && receivingMode.getDescription().length() <= 250)) {
            receivingModeException.put(CODE, PgrMasterConstants.SERVICETYPE_DESCRIPTION_LENGTH_CODE);
            receivingModeException.put(FIELD, PgrMasterConstants.SERVICETYPE_DESCRIPTION_LENGTH_FIELD_NAME);
            receivingModeException.put(MESSAGE, PgrMasterConstants.SERVICETYPE_DESCRIPTION_LENGTH_ERROR_MESSAGE);
            throw new PGRMasterException(receivingModeException);
        }
        commonValidation(receivingModeRequest.getModeType());
    }


    private void addChannelValidationErrors(final ReceivingModeTypeReq receivingModeRequest) {

        final ReceivingModeType receivingMode = receivingModeRequest.getModeType();

        if (receivingMode.getChannels().isEmpty() || receivingMode.getChannels().size() == 0) {
            receivingModeException.put(CODE, PgrMasterConstants.RECEIVINGMODE_CHANNEL_MANDATORY_CODE);
            receivingModeException.put(MESSAGE, PgrMasterConstants.RECEIVINGMODE_CHANNEL_MANADATORY_ERROR_MESSAGE);
            receivingModeException.put(FIELD, PgrMasterConstants.RECEIVINGMODE_CHANNEL_MANADATORY_FIELD_NAME);
            throw new PGRMasterException(receivingModeException);
        }
    }

    private void addTenantIdValidationErrors(final ReceivingModeTypeReq receivingModeRequest) {
        final ReceivingModeType receivingMode = receivingModeRequest.getModeType();
        if (receivingMode.getTenantId() == null || receivingMode.getTenantId().isEmpty()) {
            receivingModeException.put(CODE, PgrMasterConstants.TENANTID_MANDATORY_CODE);
            receivingModeException.put(MESSAGE, PgrMasterConstants.TENANTID_MANADATORY_ERROR_MESSAGE);
            receivingModeException.put(FIELD, PgrMasterConstants.TENANTID_MANADATORY_FIELD_NAME);
            throw new PGRMasterException(receivingModeException);
        } else if (receivingMode.getChannels().size() > 0) {

            for (String chanel : receivingMode.getChannels()) {

                ChannelType chaType = ChannelType.fromValue(chanel);

                if (chaType == null) {
                    receivingModeException.put(CODE, PgrMasterConstants.RECEIVINGMODE_CHANNEL_VALID_CODE);
                    receivingModeException.put(MESSAGE, PgrMasterConstants.RECEIVINGMODE_CHANNEL_VALID_ERROR_MESSAGE);
                    receivingModeException.put(FIELD, PgrMasterConstants.RECEIVINGMODE_CHANNEL_VALID__FIELD_NAME);
                    throw new PGRMasterException(receivingModeException);
                }

            }

        }
    }
}
