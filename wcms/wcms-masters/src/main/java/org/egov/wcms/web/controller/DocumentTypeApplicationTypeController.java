/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.wcms.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.DocumentTypeApplicationType;
import org.egov.wcms.model.enums.ApplicationType;
import org.egov.wcms.service.DocumentTypeApplicationTypeService;
import org.egov.wcms.util.WcmsConstants;
import org.egov.wcms.web.contract.DocumentTypeApplicationTypeGetRequest;
import org.egov.wcms.web.contract.DocumentTypeApplicationTypeReq;
import org.egov.wcms.web.contract.DocumentTypeApplicationTypeRes;
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
@RequestMapping("/documenttype-applicationtype")
public class DocumentTypeApplicationTypeController {

    private static final Logger logger = LoggerFactory.getLogger(DocumentTypeApplicationTypeController.class);

    @Autowired
    private DocumentTypeApplicationTypeService docTypeAppTypeService;

    @Autowired
    private ErrorHandler errHandler;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private ApplicationProperties applicationProperties;

    @PostMapping(value = "/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final DocumentTypeApplicationTypeReq documentNameRequest,
            final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("documentNameRequest::" + documentNameRequest);

        final List<ErrorResponse> errorResponses = validateDocumentNameRequest(documentNameRequest);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

        final DocumentTypeApplicationType docTypeAppType = docTypeAppTypeService.sendMessage(
                applicationProperties.getCreateDocumentTypeApplicationTypeTopicName(), "documenttypeapplicationtype-create",
                documentNameRequest);
        final List<DocumentTypeApplicationType> docTypesAppTypes = new ArrayList<>();
        docTypesAppTypes.add(docTypeAppType);
        return getSuccessResponse(docTypesAppTypes, documentNameRequest.getRequestInfo());

    }

    @PostMapping(value = "/{docTypeAppliTypeId}/_update")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody @Valid final DocumentTypeApplicationTypeReq documentTypeApplicationTypeReq,
            final BindingResult errors, @PathVariable("docTypeAppliTypeId") final Long docTypeAppliTypeId) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("documentTypeApplicationTypeRequest::" + documentTypeApplicationTypeReq);
        documentTypeApplicationTypeReq.getDocumentTypeApplicationType().setId(docTypeAppliTypeId);

        final List<ErrorResponse> errorResponses = validateDocumentNameRequest(documentTypeApplicationTypeReq);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

        final DocumentTypeApplicationType documentTypeAppliType = docTypeAppTypeService.sendMessage(
                applicationProperties.getUpdateDocumentTypeApplicationTypeTopicName(), "documenttypeapplicationtype-update",
                documentTypeApplicationTypeReq);
        final List<DocumentTypeApplicationType> documentTypeApplicaTypes = new ArrayList<>();
        documentTypeApplicaTypes.add(documentTypeAppliType);
        return getSuccessResponse(documentTypeApplicaTypes, documentTypeApplicationTypeReq.getRequestInfo());
    }

    @PostMapping("_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid final DocumentTypeApplicationTypeGetRequest docTypeAppTypeGetRequest,
            final BindingResult modelAttributeBindingResult, @RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult requestBodyBindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        if (modelAttributeBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingParameters(modelAttributeBindingResult, requestInfo);

        if (requestBodyBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);

        List<DocumentTypeApplicationType> docNameList = null;
        try {
            docNameList = docTypeAppTypeService.getDocumentAndApplicationTypes(docTypeAppTypeGetRequest);
        } catch (final Exception exception) {
            logger.error("Error while processing request " + docTypeAppTypeGetRequest, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return getSuccessResponse(docNameList, requestInfo);

    }

    private ResponseEntity<?> getSuccessResponse(final List<DocumentTypeApplicationType> docTypeAppTypeList,
            final RequestInfo requestInfo) {
        final DocumentTypeApplicationTypeRes docNameResponse = new DocumentTypeApplicationTypeRes();
        docNameResponse.setDocumentTypeApplicationTypes(docTypeAppTypeList);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        docNameResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(docNameResponse, HttpStatus.OK);

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

    private List<ErrorResponse> validateDocumentNameRequest(final DocumentTypeApplicationTypeReq documentNameRequest) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(documentNameRequest);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);

        return errorResponses;
    }

    private Error getError(final DocumentTypeApplicationTypeReq documentNameRequest) {
        final List<ErrorField> errorFields = getErrorFields(documentNameRequest);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_DOCTYPE_APPLICATION_TYPE_REQUEST_MESSAGE)
                .errorFields(errorFields)
                .build();
    }

    private List<ErrorField> getErrorFields(final DocumentTypeApplicationTypeReq documentNameRequest) {
        final List<ErrorField> errorFields = new ArrayList<>();
        final DocumentTypeApplicationType docTypeAppType = documentNameRequest.getDocumentTypeApplicationType();
        addDocumentTypeValidationErrors(docTypeAppType, errorFields);
        addTeanantIdValidationErrors(docTypeAppType, errorFields);
        addActiveValidationErrors(docTypeAppType, errorFields);
        addApplicationTypeValidationErrors(docTypeAppType, errorFields);
        addDocumentApplicationTypeUniqueValidation(docTypeAppType, errorFields);
        return errorFields;
    }

    private void addDocumentTypeValidationErrors(final DocumentTypeApplicationType docTypeAppType,
            final List<ErrorField> errorFields) {

        if (docTypeAppType.getDocumentTypeId() == 0) {

            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.DOCTYPE_MANDATORY_CODE)
                    .message(WcmsConstants.DOCTYPE_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.DOCTYPE_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);

        } else
            return;
    }

    private void addApplicationTypeValidationErrors(final DocumentTypeApplicationType docTypeAppType,
            final List<ErrorField> errorFields) {
        if (docTypeAppType.getApplicationType() == null || docTypeAppType.getApplicationType().isEmpty()) {

            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.APPLICATION_TYPE_MANDATORY_CODE)
                    .message(WcmsConstants.APPLICATION_TYPE_ERROR_MESSAGE)
                    .field(WcmsConstants.APPLICATION_TYPE_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);

        } else if (ApplicationType.fromValue(docTypeAppType.getApplicationType()) == null) {

            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.VALID_APPLICATION_TYPE_MANDATORY_CODE)
                    .message(WcmsConstants.VALID_APPLICATION_TYPE_ERROR_MESSAGE)
                    .field(WcmsConstants.VALID_APPLICATION_TYPE_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);

        } else
            return;

    }

    private void addDocumentApplicationTypeUniqueValidation(final DocumentTypeApplicationType docTypeAppType,
            final List<ErrorField> errorFields) {

        if (!docTypeAppTypeService.checkDocumentTypeApplicationTypeExist(docTypeAppType.getApplicationType(),
                docTypeAppType.getDocumentTypeId(), docTypeAppType.getTenantId())) {

            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.DOCTYPE_APPLICATIONTYPE_UNIQUE_CODE)
                    .message(WcmsConstants.DOCTYPE_APPLICATIONTYPE_UNQ_ERROR_MESSAGE)
                    .field(WcmsConstants.DOCTYPE_APPLICATIONTYPE_UNQ_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        }
    }

    private void addTeanantIdValidationErrors(final DocumentTypeApplicationType docTypeAppType,
            final List<ErrorField> errorFields) {
        if (docTypeAppType.getTenantId() == null || docTypeAppType.getTenantId().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.TENANTID_MANDATORY_CODE)
                    .message(WcmsConstants.TENANTID_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.TENANTID_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else
            return;
    }

    private void addActiveValidationErrors(final DocumentTypeApplicationType docTypeAppType, final List<ErrorField> errorFields) {
        if (docTypeAppType.getActive() == null) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.ACTIVE_MANDATORY_CODE)
                    .message(WcmsConstants.ACTIVE_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.ACTIVE_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else
            return;
    }

}
