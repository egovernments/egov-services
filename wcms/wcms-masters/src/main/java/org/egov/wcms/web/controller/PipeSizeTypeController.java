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

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.PipeSizeType;
import org.egov.wcms.service.PipeSizeTypeService;
import org.egov.wcms.util.WcmsConstants;
import org.egov.wcms.web.contract.PipeSizeTypeGetRequest;
import org.egov.wcms.web.contract.PipeSizeTypeRequest;
import org.egov.wcms.web.contract.PipeSizeTypeResponse;
import org.egov.wcms.web.contract.RequestInfoWrapper;
import org.egov.wcms.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.web.errorhandlers.Error;
import org.egov.wcms.web.errorhandlers.ErrorHandler;
import org.egov.wcms.web.errorhandlers.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pipesize")
public class PipeSizeTypeController {

    private static final Logger logger = LoggerFactory.getLogger(PipeSizeTypeController.class);

    @Autowired
    private PipeSizeTypeService pipeSizeService;

    @Autowired
    private ErrorHandler errHandler;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private ApplicationProperties applicationProperties;

    @PostMapping(value = "/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final PipeSizeTypeRequest pipeSizeRequest,
            final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("pipeSizeRequest::" + pipeSizeRequest);

        final List<ErrorResponse> errorResponses = validatePipeSizeRequest(pipeSizeRequest);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

        final PipeSizeType pipeSize = pipeSizeService.createPipeSize(applicationProperties.getCreatePipeSizetopicName(),
                "pipesize-create", pipeSizeRequest);
        final List<PipeSizeType> pipeSizes = new ArrayList<>();
        pipeSizes.add(pipeSize);
        return getSuccessResponse(pipeSizes, pipeSizeRequest.getRequestInfo());

    }

    @PostMapping(value = "/_update/{code}")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody @Valid final PipeSizeTypeRequest pipeSizeRequest, final BindingResult errors,
            @PathVariable("code") final String code) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("pipeSizeRequest::" + pipeSizeRequest);
        pipeSizeRequest.getPipeSize().setCode(code);

        final List<ErrorResponse> errorResponses = validatePipeSizeRequest(pipeSizeRequest);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

        final PipeSizeType pipeSize = pipeSizeService.updatePipeSize(applicationProperties.getUpdatePipeSizeTopicName(),
                "pipesize-update", pipeSizeRequest);
        final List<PipeSizeType> pipeSizes = new ArrayList<>();
        pipeSizes.add(pipeSize);
        return getSuccessResponse(pipeSizes, pipeSizeRequest.getRequestInfo());
    }

    @PostMapping("_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid final PipeSizeTypeGetRequest pipeSizeGetRequest,
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
        List<PipeSizeType> pipeSizeList = null;
        try {
            pipeSizeList = pipeSizeService.getPipeSizes(pipeSizeGetRequest);
        } catch (final Exception exception) {
            logger.error("Error while processing request " + pipeSizeGetRequest, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return getSuccessResponse(pipeSizeList, requestInfo);

    }

    private List<ErrorResponse> validatePipeSizeRequest(final PipeSizeTypeRequest pipeSizeRequest) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(pipeSizeRequest);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);

        return errorResponses;
    }

    private Error getError(final PipeSizeTypeRequest pipeSizeRequest) {
        pipeSizeRequest.getPipeSize();
        final List<ErrorField> errorFields = getErrorFields(pipeSizeRequest);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_PIPESIZE_REQUEST_MESSAGE)
                .errorFields(errorFields)
                .build();
    }

    private List<ErrorField> getErrorFields(final PipeSizeTypeRequest pipeSizeRequest) {
        final List<ErrorField> errorFields = new ArrayList<>();
        addPipeSizeInmmValidationErrors(pipeSizeRequest, errorFields);
        addTeanantIdValidationErrors(pipeSizeRequest, errorFields);
        addActiveValidationErrors(pipeSizeRequest, errorFields);
        return errorFields;
    }

    private void addPipeSizeInmmValidationErrors(final PipeSizeTypeRequest pipeSizeRequest, final List<ErrorField> errorFields) {
        final PipeSizeType pipeSize = pipeSizeRequest.getPipeSize();
        if (pipeSize.getSizeInMilimeter() == 0) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.PIPESIZE_SIZEINMM_MANDATORY_CODE)
                    .message(WcmsConstants.PIPESIZE_SIZEINMM__MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.PIPESIZE_SIZEINMM__MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (!pipeSizeService.getPipeSizeInmmAndCode(pipeSize.getCode(), pipeSize.getSizeInMilimeter(),
                pipeSize.getTenantId())) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.PIPESIZE_SIZEINMM_UNIQUE_CODE)
                    .message(WcmsConstants.PIPESIZE_SIZEINMM_UNQ_ERROR_MESSAGE)
                    .field(WcmsConstants.PIPESIZE_SIZEINMM__UNQ_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else
            return;
    }

    private void addTeanantIdValidationErrors(final PipeSizeTypeRequest pipeSizeRequest, final List<ErrorField> errorFields) {
        final PipeSizeType pipeSize = pipeSizeRequest.getPipeSize();
        if (pipeSize.getTenantId() == null || pipeSize.getTenantId().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.TENANTID_MANDATORY_CODE)
                    .message(WcmsConstants.TENANTID_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.TENANTID_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else
            return;
    }

    private void addActiveValidationErrors(final PipeSizeTypeRequest pipeSizeRequest, final List<ErrorField> errorFields) {
        final PipeSizeType pipeSize = pipeSizeRequest.getPipeSize();
        if (pipeSize.getActive() == null) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.ACTIVE_MANDATORY_CODE)
                    .message(WcmsConstants.ACTIVE_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.ACTIVE_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else
            return;
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

    private ResponseEntity<?> getSuccessResponse(final List<PipeSizeType> pipeSizeList, final RequestInfo requestInfo) {
        final PipeSizeTypeResponse pipeSizeResponse = new PipeSizeTypeResponse();
        pipeSizeResponse.setPipeSizes(pipeSizeList);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        pipeSizeResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(pipeSizeResponse, HttpStatus.OK);

    }

}
