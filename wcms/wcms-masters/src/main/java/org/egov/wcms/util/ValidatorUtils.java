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
package org.egov.wcms.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.response.ErrorField;
import org.egov.wcms.model.CategoryType;
import org.egov.wcms.model.DocumentType;
import org.egov.wcms.model.DocumentTypeApplicationType;
import org.egov.wcms.model.Donation;
import org.egov.wcms.model.PipeSize;
import org.egov.wcms.model.PropertyTypePipeSize;
import org.egov.wcms.model.PropertyTypeUsageType;
import org.egov.wcms.model.SourceType;
import org.egov.wcms.model.SupplyType;
import org.egov.wcms.model.enums.ApplicationType;
import org.egov.wcms.service.CategoryTypeService;
import org.egov.wcms.service.DocumentTypeApplicationTypeService;
import org.egov.wcms.service.DocumentTypeService;
import org.egov.wcms.service.PipeSizeService;
import org.egov.wcms.service.PropertyTypePipeSizeTypeService;
import org.egov.wcms.service.PropertyUsageTypeService;
import org.egov.wcms.service.SourceTypeService;
import org.egov.wcms.service.SupplyTypeService;
import org.egov.wcms.web.contract.CategoryTypeRequest;
import org.egov.wcms.web.contract.DocumentTypeApplicationTypeReq;
import org.egov.wcms.web.contract.DocumentTypeReq;
import org.egov.wcms.web.contract.DonationRequest;
import org.egov.wcms.web.contract.MeterCostRequest;
import org.egov.wcms.web.contract.PipeSizeRequest;
import org.egov.wcms.web.contract.PropertyTypeCategoryTypeReq;
import org.egov.wcms.web.contract.PropertyTypePipeSizeRequest;
import org.egov.wcms.web.contract.PropertyTypeUsageTypeReq;
import org.egov.wcms.web.contract.SourceTypeRequest;
import org.egov.wcms.web.contract.SupplyTypeRequest;
import org.egov.wcms.web.errorhandlers.Error;
import org.egov.wcms.web.errorhandlers.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class ValidatorUtils {

    @Autowired
    private CategoryTypeService categoryTypeService;

    @Autowired
    private PipeSizeService pipeSizeService;

    @Autowired
    private DocumentTypeApplicationTypeService documentApplicationService;

    @Autowired
    private DocumentTypeService documentTypeService;

    @Autowired
    private PropertyTypePipeSizeTypeService propertPipeSizeService;

    @Autowired
    private PropertyUsageTypeService propertyUsageTypeService;

    @Autowired
    private SourceTypeService sourceTypeService;

    @Autowired
    private SupplyTypeService supplyTypeService;

    public List<ErrorResponse> validateCategoryRequest(final CategoryTypeRequest categoryRequest) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(categoryRequest);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);
        return errorResponses;
    }

    private Error getError(final CategoryTypeRequest categoryRequest) {
        categoryRequest.getCategory();
        final List<ErrorField> errorFields = getErrorFields(categoryRequest);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_CATEGORY_REQUEST_MESSAGE)
                .errorFields(errorFields)
                .build();
    }

    private List<ErrorField> getErrorFields(final CategoryTypeRequest categoryRequest) {
        final List<ErrorField> errorFields = new ArrayList<>();
        addCategoryNameValidationErrors(categoryRequest, errorFields);
        addTenantIdValidationErrors(categoryRequest.getCategory().getTenantId(), errorFields);
        addActiveValidationErrors(categoryRequest.getCategory().getActive(), errorFields);
        return errorFields;
    }

    private void addCategoryNameValidationErrors(final CategoryTypeRequest categoryRequest,
            final List<ErrorField> errorFields) {
        final CategoryType category = categoryRequest.getCategory();
        if (category.getName() == null || category.getName().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.CATEGORY_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.CATEGORY_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.CATEGORY_NAME_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (!categoryTypeService.getCategoryByNameAndCode(category.getCode(), category.getName(),
                category.getTenantId())) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.CATEGORY_NAME_UNIQUE_CODE)
                    .message(WcmsConstants.CATEGORY_UNQ_ERROR_MESSAGE)
                    .field(WcmsConstants.CATEGORY_NAME_UNQ_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else
            return;
    }

    public List<ErrorResponse> validatePipeSizeRequest(final PipeSizeRequest pipeSizeRequest) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(pipeSizeRequest);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);

        return errorResponses;
    }

    private Error getError(final PipeSizeRequest pipeSizeRequest) {
        pipeSizeRequest.getPipeSize();
        final List<ErrorField> errorFields = getErrorFields(pipeSizeRequest);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_PIPESIZE_REQUEST_MESSAGE)
                .errorFields(errorFields)
                .build();
    }

    private List<ErrorField> getErrorFields(final PipeSizeRequest pipeSizeRequest) {
        final List<ErrorField> errorFields = new ArrayList<>();
        addPipeSizeInmmValidationErrors(pipeSizeRequest, errorFields);
        addTenantIdValidationErrors(pipeSizeRequest.getPipeSize().getTenantId(), errorFields);
        addActiveValidationErrors(pipeSizeRequest.getPipeSize().getActive(), errorFields);
        return errorFields;
    }

    private void addPipeSizeInmmValidationErrors(final PipeSizeRequest pipeSizeRequest, final List<ErrorField> errorFields) {
        final PipeSize pipeSize = pipeSizeRequest.getPipeSize();
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

    private void addTenantIdValidationErrors(final String tenantId, final List<ErrorField> errorFields) {

        if (StringUtils.isBlank(tenantId)) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.TENANTID_MANDATORY_CODE)
                    .message(WcmsConstants.TENANTID_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.TENANTID_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else
            return;
    }

    private void addActiveValidationErrors(final Boolean isActive, final List<ErrorField> errorFields) {
        if (isActive == null) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.ACTIVE_MANDATORY_CODE)
                    .message(WcmsConstants.ACTIVE_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.ACTIVE_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else
            return;
    }

    public ErrorResponse populateErrors(final BindingResult errors) {
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

    public List<ErrorResponse> validateDocumentNameRequest(final DocumentTypeApplicationTypeReq documentNameRequest) {
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
        addTenantIdValidationErrors(documentNameRequest.getDocumentTypeApplicationType().getTenantId(), errorFields);
        addActiveValidationErrors(documentNameRequest.getDocumentTypeApplicationType().getActive(), errorFields);
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

        if (!documentApplicationService.checkDocumentTypeApplicationTypeExist(docTypeAppType.getApplicationType(),
                docTypeAppType.getDocumentTypeId(), docTypeAppType.getTenantId())) {

            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.DOCTYPE_APPLICATIONTYPE_UNIQUE_CODE)
                    .message(WcmsConstants.DOCTYPE_APPLICATIONTYPE_UNQ_ERROR_MESSAGE)
                    .field(WcmsConstants.DOCTYPE_APPLICATIONTYPE_UNQ_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        }
    }

    public List<ErrorResponse> validateDocumentTypeRequest(final DocumentTypeReq documentTypeReq) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(documentTypeReq);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);

        return errorResponses;
    }

    private Error getError(final DocumentTypeReq documentTypeReq) {
        documentTypeReq.getDocumentType();
        final List<ErrorField> errorFields = getErrorFields(documentTypeReq);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_DOCUMENTTYPE_REQUEST_MESSAGE)
                .errorFields(errorFields)
                .build();
    }

    private List<ErrorField> getErrorFields(final DocumentTypeReq documentTypeReq) {
        final List<ErrorField> errorFields = new ArrayList<>();
        addDocumentTypeNameValidationErrors(documentTypeReq, errorFields);
        addTenantIdValidationErrors(documentTypeReq.getDocumentType().getTenantId(), errorFields);
        addActiveValidationErrors(documentTypeReq.getDocumentType().getActive(), errorFields);
        return errorFields;
    }

    private void addDocumentTypeNameValidationErrors(final DocumentTypeReq documentTypeRequest,
            final List<ErrorField> errorFields) {
        final DocumentType documentType = documentTypeRequest.getDocumentType();
        if (documentType.getName() == null || documentType.getName().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.DOCUMENTTYPE_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.DOCUMENTTYPE_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.DOCUMENTTYPE_NAME_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (!documentTypeService.getDocumentTypeByNameAndCode(documentType.getCode(), documentType.getName(),
                documentType.getTenantId())) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.DOCUMENTTYPE_NAME_UNIQUE_CODE)
                    .message(WcmsConstants.DOCUMENTTYPE_UNQ_ERROR_MESSAGE)
                    .field(WcmsConstants.DOCUMENTTYPE_NAME_UNQ_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else
            return;
    }

    public List<ErrorResponse> validateDonationRequest(final DonationRequest donationRequest) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(donationRequest);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);
        return errorResponses;
    }

    private Error getError(final DonationRequest donationRequest) {
        final List<ErrorField> errorFields = getErrorFields(donationRequest);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_DONATION_REQUEST_MESSAGE)
                .errorFields(errorFields)
                .build();
    }

    private List<ErrorField> getErrorFields(final DonationRequest donationRequest) {
        final Donation donation = donationRequest.getDonation();
        final List<ErrorField> errorFields = new ArrayList<>();
        checkPropertyTypeValue(errorFields, donation);
        checkUsageTypeValue(errorFields, donation);
        checkCategoryValue(errorFields, donation);
        checkPipeSizeValues(errorFields, donation);
        checkDonationAmountValues(errorFields, donation);
        checkFromToDateValues(errorFields, donation);
        return errorFields;
    }

    private void checkPropertyTypeValue(final List<ErrorField> errorFields, final Donation donation) {
        if (donation.getPropertyType() == null || donation.getPropertyType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.PROPERTYTYPE_MANDATORY_CODE)
                    .message(WcmsConstants.PROPERTYTYPE_MANDATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.PROPERTYTYPE_MANDATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        }
    }

    private void checkUsageTypeValue(final List<ErrorField> errorFields, final Donation donation) {
        if (donation.getUsageType() == null || donation.getUsageType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.USAGETYPE_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.USAGETYPE_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.USAGETYPE_NAME_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        }
    }

    private void checkCategoryValue(final List<ErrorField> errorFields, final Donation donation) {
        if (donation.getCategory() == null || donation.getCategory().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.CATEGORY_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.CATEGORY_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.CATEGORY_NAME_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        }
    }

    private void checkPipeSizeValues(final List<ErrorField> errorFields, final Donation donation) {
        if ((donation.getMaxHSCPipeSize() == null || donation.getMaxHSCPipeSize().isEmpty()) &&
                donation.getMinHSCPipeSize() == null || donation.getMinHSCPipeSize().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.PIPESIZE_SIZEINMM_MANDATORY_CODE)
                    .message(WcmsConstants.PIPESIZE_SIZEINMM__MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.PIPESIZE_SIZEINMM__MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        }
    }

    private void checkDonationAmountValues(final List<ErrorField> errorFields, final Donation donation) {
        if (donation.getDonationAmount() == null || donation.getDonationAmount().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.DONATION_MANDATORY_CODE)
                    .message(WcmsConstants.DONATION_MANDATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.DONATION_MANDATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        }
    }

    private void checkFromToDateValues(final List<ErrorField> errorFields, final Donation donation) {
        if (donation.getFromDate() == null || donation.getToDate() == null) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.FROMTO_MANDATORY_CODE)
                    .message(WcmsConstants.FROMTO_MANDATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.FROMTO_MANDATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        }
    }

    public List<ErrorResponse> validateMeterCostRequest(final MeterCostRequest meterCostRequest) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(meterCostRequest);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);

        return errorResponses;
    }

    private Error getError(final MeterCostRequest meterCostRequest) {
        final List<ErrorField> errorFields = getErrorFields(meterCostRequest);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_USAGETYPE_REQUEST_MESSAGE)
                .errorFields(errorFields)
                .build();
    }

    private List<ErrorField> getErrorFields(final MeterCostRequest meterCostRequest) {
        final List<ErrorField> errorFields = new ArrayList<>();
        addTenantIdValidationErrors(meterCostRequest.getMeterCost().getTenantId(), errorFields);
        addActiveValidationErrors(meterCostRequest.getMeterCost().getActive(), errorFields);
        return errorFields;
    }

    public List<ErrorResponse> validatePropertyPipeSizeRequest(final PropertyTypePipeSizeRequest propertyPipeSizeRequest) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(propertyPipeSizeRequest);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);

        return errorResponses;
    }

    private Error getError(final PropertyTypePipeSizeRequest propertyPipeSizeRequest) {
        propertyPipeSizeRequest.getPropertyPipeSize();
        final List<ErrorField> errorFields = getErrorFields(propertyPipeSizeRequest);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_PROPERTY_PIPESIZE_REQUEST_MESSAGE)
                .errorFields(errorFields)
                .build();
    }

    private List<ErrorField> getErrorFields(final PropertyTypePipeSizeRequest propertyPipeSizeRequest) {
        final List<ErrorField> errorFields = new ArrayList<>();
        addPropertyPipeSizeValidationErrors(propertyPipeSizeRequest, errorFields);
        addTenantIdValidationErrors(propertyPipeSizeRequest.getPropertyPipeSize().getTenantId(), errorFields);
        addActiveValidationErrors(propertyPipeSizeRequest.getPropertyPipeSize().getActive(), errorFields);
        return errorFields;
    }

    private void addPropertyPipeSizeValidationErrors(final PropertyTypePipeSizeRequest propertyPipeSizeRequest,
            final List<ErrorField> errorFields) {
        final PropertyTypePipeSize propertyPipeSize = propertyPipeSizeRequest.getPropertyPipeSize();
        if (propertyPipeSize.getPropertyTypeName() == null && !propertyPipeSize.getPropertyTypeName().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.PROPERTY_PIPESIZE_PROPERTYTYPE_MANDATORY_CODE)
                    .message(WcmsConstants.PROPERTY_PIPESIZE_PROPERTYTYPE_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.PROPERTY_PIPESIZE_PROPERTYTYPE_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (propertyPipeSize.getPipeSizeType() == null) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.PROPERTY_PIPESIZE_HSCSIZEINMM_MANDATORY_CODE)
                    .message(WcmsConstants.PROPERTY_PIPESIZE_HSCSIZEINMM_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.PROPERTY_PIPESIZE_HSCSIZEINMM_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (propertPipeSizeService.checkPipeSizeExists(propertyPipeSize.getPipeSizeType(),
                propertyPipeSize.getTenantId())) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.PROPERTY_PIPESIZE_HSCSIZEINMM_INVALID_CODE)
                    .message(WcmsConstants.PROPERTY_PIPESIZE_HSCSIZEINMM_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.PROPERTY_PIPESIZE_HSCSIZEINMM_INVALID_FIELD_NAME)
                    .build();
            errorFields.add(errorField);

        } else if (!propertPipeSizeService.checkPropertyByPipeSize(propertyPipeSize.getId(), propertyPipeSize.getPropertyTypeId(),
                propertyPipeSize.getPipeSizeId(), propertyPipeSize.getTenantId())) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.PROPERTY_PIPESIZE_SIZEINMM_UNIQUE_CODE)
                    .message(WcmsConstants.PROPERTY_PIPESIZE_SIZEINMM_UNQ_ERROR_MESSAGE)
                    .field(WcmsConstants.PROPERTY_PIPESIZE_SIZEINMM_UNQ_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else
            return;
    }

    public List<ErrorResponse> validatePropertyCategoryRequest(final PropertyTypeCategoryTypeReq propertyCategoryRequest) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(propertyCategoryRequest);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);
        return errorResponses;
    }

    private Error getError(final PropertyTypeCategoryTypeReq propertyCategoryRequest) {
        final List<ErrorField> errorFields = new ArrayList<>();
        if (propertyCategoryRequest.getPropertyTypeCategoryType().getCategoryTypeName() == null
                || propertyCategoryRequest.getPropertyTypeCategoryType().getCategoryTypeName().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.CATEGORY_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.CATEGORY_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.CATEGORY_NAME_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (propertyCategoryRequest.getPropertyTypeCategoryType().getPropertyTypeName() == null
                || propertyCategoryRequest.getPropertyTypeCategoryType().getPropertyTypeName().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.PROPERTY_TYPE_MANDATORY_CODE)
                    .message(WcmsConstants.PROPERTY_TYPE_MANDATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.PROPERTY_TYPE_MANDATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (propertyCategoryRequest.getPropertyTypeCategoryType().getTenantId() == null
                || propertyCategoryRequest.getPropertyTypeCategoryType().getTenantId().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.TENANTID_MANDATORY_CODE)
                    .message(WcmsConstants.TENANTID_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.TENANTID_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        }

        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_CATEGORY_REQUEST_MESSAGE)
                .errorFields(errorFields)
                .build();
    }

    public List<ErrorResponse> validateUsageTypeRequest(final PropertyTypeUsageTypeReq propUsageTypeRequest) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(propUsageTypeRequest);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);

        return errorResponses;
    }

    private Error getError(final PropertyTypeUsageTypeReq propUsageTypeRequest) {
        final PropertyTypeUsageType propUsageType = propUsageTypeRequest.getPropertyTypeUsageType();
        final List<ErrorField> errorFields = new ArrayList<>();
        validatePropertyTypeValue(errorFields, propUsageType);
        validateUsageTypeValue(errorFields, propUsageType);
        addTenantIdValidationErrors(propUsageTypeRequest.getPropertyTypeUsageType().getTenantId(), errorFields);
        if (errorFields.size() <= 0 && !(propUsageType.getTenantId() == null || propUsageType.getTenantId().isEmpty()))
            if (propertyUsageTypeService.checkPropertyUsageTypeExists(propUsageTypeRequest)) {
                final ErrorField errorField = ErrorField.builder()
                        .code(WcmsConstants.PROPERTYTYPE_USAGETYPE_UNIQUE_CODE)
                        .message(WcmsConstants.PROPERTYTYPE_USAGETYPE_UNQ_ERROR_MESSAGE)
                        .field(WcmsConstants.PROPERTYTYPE_USAGETYPE_UNQ_FIELD_NAME)
                        .build();
                errorFields.add(errorField);
            }
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_PROPERTYUSAGETYPE_REQUEST_MESSAGE)
                .errorFields(errorFields)
                .build();
    }

    private List<ErrorField> validatePropertyTypeValue(final List<ErrorField> errorFields,
            final PropertyTypeUsageType propUsageType) {
        if (propUsageType.getPropertyType() == null || propUsageType.getPropertyType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.PROPERTYTYPE_MANDATORY_CODE)
                    .message(WcmsConstants.PROPERTYTYPE_MANDATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.PROPERTYTYPE_MANDATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        }
        return errorFields;
    }

    private List<ErrorField> validateUsageTypeValue(final List<ErrorField> errorFields,
            final PropertyTypeUsageType propUsageType) {
        if (propUsageType.getUsageType() == null || propUsageType.getUsageType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.USAGETYPE_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.USAGETYPE_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.USAGETYPE_NAME_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        }
        return errorFields;
    }

    public List<ErrorResponse> validateWaterSourceRequest(final SourceTypeRequest waterSourceTypeRequest) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(waterSourceTypeRequest);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);
        return errorResponses;
    }

    private Error getError(final SourceTypeRequest waterSourceTypeRequest) {
        final List<ErrorField> errorFields = getErrorFields(waterSourceTypeRequest);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_WATERSOURCETYPE_REQUEST_MESSAGE)
                .errorFields(errorFields)
                .build();
    }

    private List<ErrorField> getErrorFields(final SourceTypeRequest waterSourceTypeRequest) {
        final List<ErrorField> errorFields = new ArrayList<>();
        addCategoryNameValidationErrors(waterSourceTypeRequest, errorFields);
        addTenantIdValidationErrors(waterSourceTypeRequest.getWaterSourceType().getTenantId(), errorFields);
        addActiveValidationErrors(waterSourceTypeRequest.getWaterSourceType().getActive(), errorFields);
        return errorFields;
    }

    private void addCategoryNameValidationErrors(final SourceTypeRequest waterSourceTypeRequest,
            final List<ErrorField> errorFields) {
        final SourceType waterSource = waterSourceTypeRequest.getWaterSourceType();
        if (waterSource.getName() == null || waterSource.getName().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.WATERSOURCETYPE_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.WATERSOURCETYPE_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.WATERSOURCETYPE_NAME_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (!sourceTypeService.getWaterSourceByNameAndCode(waterSource.getCode(), waterSource.getName(),
                waterSource.getTenantId())) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.WATERSOURCETYPE_NAME_UNIQUE_CODE)
                    .message(WcmsConstants.WATERSOURCETYPE_UNQ_ERROR_MESSAGE)
                    .field(WcmsConstants.WATERSOURCETYPE_NAME_UNQ_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else
            return;
    }

    public List<ErrorResponse> validateSupplyType(final SupplyTypeRequest supplyTypeRequest) {
        final ErrorResponse errorResponse = new ErrorResponse();
        final List<ErrorResponse> errorResponseList = new ArrayList<>();
        final Error error = getError(supplyTypeRequest);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponseList.add(errorResponse);

        return errorResponseList;
    }

    private List<ErrorField> getErrorFields(final SupplyTypeRequest supplyTypeRequest) {
        final List<ErrorField> errorFields = new ArrayList<>();
        addSupplyNameValidationErrors(supplyTypeRequest, errorFields);
        addTenantIdValidationErrors(supplyTypeRequest.getSupplyType().getTenantId(), errorFields);
        addActiveValidationErrors(supplyTypeRequest.getSupplyType().getActive(), errorFields);
        return errorFields;
    }

    private void addSupplyNameValidationErrors(final SupplyTypeRequest supplyTypeRequest,
            final List<ErrorField> errorFields) {
        final SupplyType supply = supplyTypeRequest.getSupplyType();
        if (supply.getName() == null || supply.getName().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.SUPPLYTYPE_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.SUPPLYTYPE_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.SUPPLYTYPE_NAME_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (supply.getCode() != null
                && !supplyTypeService.getSupplyTypeByNameAndCode(supply.getCode(), supply.getName(), supply.getTenantId())) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.SUPPLYTYPE_NAME_UNIQUE_CODE)
                    .message(WcmsConstants.SUPPLYTYPE_UNQ_ERROR_MESSAGE)
                    .field(WcmsConstants.SUPPLYTYPE_NAME_UNQ_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else
            return;
    }

    private Error getError(final SupplyTypeRequest supplyTypeRequest) {
        final List<ErrorField> errorFiled = getErrorFields(supplyTypeRequest);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_CATEGORY_REQUEST_MESSAGE)
                .errorFields(errorFiled).build();
    }

}
