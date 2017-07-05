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
import org.egov.wcms.model.PipeSize;
import org.egov.wcms.model.PropertyTypeCategoryType;
import org.egov.wcms.model.PropertyTypePipeSize;
import org.egov.wcms.model.SourceType;
import org.egov.wcms.model.SupplyType;
import org.egov.wcms.model.enums.ApplicationType;
import org.egov.wcms.service.CategoryTypeService;
import org.egov.wcms.service.DocumentTypeApplicationTypeService;
import org.egov.wcms.service.DocumentTypeService;
import org.egov.wcms.service.DonationService;
import org.egov.wcms.service.PipeSizeService;
import org.egov.wcms.service.PropertyCategoryService;
import org.egov.wcms.service.PropertyTypePipeSizeService;
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
	private PropertyTypePipeSizeService propertPipeSizeService;

	@Autowired
	private PropertyUsageTypeService propertyUsageTypeService;

	@Autowired
	private SourceTypeService sourceTypeService;

	@Autowired
	private SupplyTypeService supplyTypeService;

	@Autowired
	private PropertyCategoryService propertyCategoryService;

	@Autowired
	private DonationService donationService;

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
				.message(WcmsConstants.INVALID_CATEGORY_REQUEST_MESSAGE).errorFields(errorFields).build();
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
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.CATEGORY_NAME_MANDATORY_CODE)
					.message(WcmsConstants.CATEGORY_NAME_MANADATORY_ERROR_MESSAGE)
					.field(WcmsConstants.CATEGORY_NAME_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		} else if (!categoryTypeService.getCategoryByNameAndCode(category.getCode(), category.getName(),
				category.getTenantId())) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.CATEGORY_NAME_UNIQUE_CODE)
					.message(WcmsConstants.CATEGORY_UNQ_ERROR_MESSAGE).field(WcmsConstants.CATEGORY_NAME_UNQ_FIELD_NAME)
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
				.message(WcmsConstants.INVALID_PIPESIZE_REQUEST_MESSAGE).errorFields(errorFields).build();
	}

	private List<ErrorField> getErrorFields(final PipeSizeRequest pipeSizeRequest) {
		final List<ErrorField> errorFields = new ArrayList<>();
		addPipeSizeInmmValidationErrors(pipeSizeRequest, errorFields);
		addTenantIdValidationErrors(pipeSizeRequest.getPipeSize().getTenantId(), errorFields);
		addActiveValidationErrors(pipeSizeRequest.getPipeSize().getActive(), errorFields);
		return errorFields;
	}

	private void addPipeSizeInmmValidationErrors(final PipeSizeRequest pipeSizeRequest,
			final List<ErrorField> errorFields) {
		final PipeSize pipeSize = pipeSizeRequest.getPipeSize();
		if (pipeSize.getSizeInMilimeter() == 0) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.PIPESIZE_SIZEINMM_MANDATORY_CODE)
					.message(WcmsConstants.PIPESIZE_SIZEINMM__MANADATORY_ERROR_MESSAGE)
					.field(WcmsConstants.PIPESIZE_SIZEINMM__MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		} else if (!pipeSizeService.getPipeSizeInmmAndCode(pipeSize.getCode(), pipeSize.getSizeInMilimeter(),
				pipeSize.getTenantId())) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.PIPESIZE_SIZEINMM_UNIQUE_CODE)
					.message(WcmsConstants.PIPESIZE_SIZEINMM_UNQ_ERROR_MESSAGE)
					.field(WcmsConstants.PIPESIZE_SIZEINMM__UNQ_FIELD_NAME).build();
			errorFields.add(errorField);
		} else
			return;
	}

	private void addTenantIdValidationErrors(final String tenantId, final List<ErrorField> errorFields) {

		if (StringUtils.isBlank(tenantId)) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.TENANTID_MANDATORY_CODE)
					.message(WcmsConstants.TENANTID_MANADATORY_ERROR_MESSAGE)
					.field(WcmsConstants.TENANTID_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		} else
			return;
	}

	private void addActiveValidationErrors(final Boolean isActive, final List<ErrorField> errorFields) {
		if (isActive == null) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.ACTIVE_MANDATORY_CODE)
					.message(WcmsConstants.ACTIVE_MANADATORY_ERROR_MESSAGE)
					.field(WcmsConstants.ACTIVE_MANADATORY_FIELD_NAME).build();
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
				.message(WcmsConstants.INVALID_DOCTYPE_APPLICATION_TYPE_REQUEST_MESSAGE).errorFields(errorFields)
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

		if (docTypeAppType.getDocumentType() == null) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.DOCTYPE_MANDATORY_CODE)
					.message(WcmsConstants.DOCTYPE_MANADATORY_ERROR_MESSAGE)
					.field(WcmsConstants.DOCTYPE_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);

		} else
			return;
	}

	private void addApplicationTypeValidationErrors(final DocumentTypeApplicationType docTypeAppType,
			final List<ErrorField> errorFields) {
		if (docTypeAppType.getApplicationType() == null || docTypeAppType.getApplicationType().isEmpty()) {

			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.APPLICATION_TYPE_MANDATORY_CODE)
					.message(WcmsConstants.APPLICATION_TYPE_ERROR_MESSAGE)
					.field(WcmsConstants.APPLICATION_TYPE_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);

		} else if (ApplicationType.fromValue(docTypeAppType.getApplicationType()) == null) {

			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.VALID_APPLICATION_TYPE_MANDATORY_CODE)
					.message(WcmsConstants.VALID_APPLICATION_TYPE_ERROR_MESSAGE)
					.field(WcmsConstants.VALID_APPLICATION_TYPE_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);

		} else
			return;

	}

	private void addDocumentApplicationTypeUniqueValidation(final DocumentTypeApplicationType docTypeAppType,
			final List<ErrorField> errorFields) {

		if (!documentApplicationService.checkDocumentTypeApplicationTypeExist(docTypeAppType.getId(),docTypeAppType.getApplicationType(),
				docTypeAppType.getDocumentType(), docTypeAppType.getTenantId())) {

			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.DOCTYPE_APPLICATIONTYPE_UNIQUE_CODE)
					.message(WcmsConstants.DOCTYPE_APPLICATIONTYPE_UNQ_ERROR_MESSAGE)
					.field(WcmsConstants.DOCTYPE_APPLICATIONTYPE_UNQ_FIELD_NAME).build();
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
				.message(WcmsConstants.INVALID_DOCUMENTTYPE_REQUEST_MESSAGE).errorFields(errorFields).build();
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
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.DOCUMENTTYPE_NAME_MANDATORY_CODE)
					.message(WcmsConstants.DOCUMENTTYPE_NAME_MANADATORY_ERROR_MESSAGE)
					.field(WcmsConstants.DOCUMENTTYPE_NAME_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		} else if (!documentTypeService.getDocumentTypeByNameAndCode(documentType.getCode(), documentType.getName(),
				documentType.getTenantId())) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.DOCUMENTTYPE_NAME_UNIQUE_CODE)
					.message(WcmsConstants.DOCUMENTTYPE_UNQ_ERROR_MESSAGE)
					.field(WcmsConstants.DOCUMENTTYPE_NAME_UNQ_FIELD_NAME).build();
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
				.message(WcmsConstants.INVALID_DONATION_REQUEST_MESSAGE).errorFields(errorFields).build();
	}

	private List<ErrorField> getErrorFields(final DonationRequest donationRequest) {
		final List<ErrorField> errorFields = new ArrayList<>();
		checkPropertyTypeValue(errorFields, donationRequest);
		checkUsageTypeValue(errorFields, donationRequest);
		checkCategoryValue(errorFields, donationRequest);
		checkPipeSizeValues(errorFields, donationRequest);
		checkDonationAmountValues(errorFields, donationRequest);
		checkFromToDateValues(errorFields, donationRequest);
		checkPropertyTypeAndUsageTypeExist(errorFields, donationRequest);
		checkCategoryTypeAndPipeSizeExist(errorFields, donationRequest);
		addTenantIdValidationErrors(donationRequest.getDonation().getTenantId(), errorFields);
		return errorFields;
	}

	private void checkPropertyTypeValue(final List<ErrorField> errorFields, final DonationRequest donationRequest) {
		if (donationRequest.getDonation().getPropertyType() == null
				|| donationRequest.getDonation().getPropertyType().isEmpty()) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.PROPERTYTYPE_MANDATORY_CODE)
					.message(WcmsConstants.PROPERTYTYPE_MANDATORY_ERROR_MESSAGE)
					.field(WcmsConstants.PROPERTYTYPE_MANDATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		}
	}

	private void checkUsageTypeValue(final List<ErrorField> errorFields, final DonationRequest donationRequest) {
		if (donationRequest.getDonation().getUsageType() == null
				|| donationRequest.getDonation().getUsageType().isEmpty()) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.USAGETYPE_NAME_MANDATORY_CODE)
					.message(WcmsConstants.USAGETYPE_NAME_MANADATORY_ERROR_MESSAGE)
					.field(WcmsConstants.USAGETYPE_NAME_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		}
	}

	private void checkCategoryValue(final List<ErrorField> errorFields, final DonationRequest donationRequest) {
		if (donationRequest.getDonation().getCategory() == null
				|| donationRequest.getDonation().getCategory().isEmpty()) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.CATEGORY_NAME_MANDATORY_CODE)
					.message(WcmsConstants.CATEGORY_NAME_MANADATORY_ERROR_MESSAGE)
					.field(WcmsConstants.CATEGORY_NAME_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		}
	}

	private void checkPipeSizeValues(final List<ErrorField> errorFields, final DonationRequest donationRequest) {
		if ((donationRequest.getDonation().getMaxPipeSize() == null)
				|| donationRequest.getDonation().getMinPipeSize() == null) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.PIPESIZE_SIZEINMM_MANDATORY_CODE)
					.message(WcmsConstants.PIPESIZE_SIZEINMM__MANADATORY_ERROR_MESSAGE)
					.field(WcmsConstants.PIPESIZE_SIZEINMM__MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		}
	}

	private void checkDonationAmountValues(final List<ErrorField> errorFields, final DonationRequest donationRequest) {
		if (donationRequest.getDonation().getDonationAmount() == null) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.DONATION_MANDATORY_CODE)
					.message(WcmsConstants.DONATION_MANDATORY_ERROR_MESSAGE)
					.field(WcmsConstants.DONATION_MANDATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		}
	}

	private void checkFromToDateValues(final List<ErrorField> errorFields, final DonationRequest donationRequest) {
		if (donationRequest.getDonation().getFromDate() == null || donationRequest.getDonation().getToDate() == null) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.FROMTO_MANDATORY_CODE)
					.message(WcmsConstants.FROMTO_MANDATORY_ERROR_MESSAGE)
					.field(WcmsConstants.FROMTO_MANDATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		}
	}

	private void checkPropertyTypeAndUsageTypeExist(final List<ErrorField> errorFields,
			final DonationRequest donationRequest) {
		if (!donationService.getPropertyTypeByName(donationRequest)) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.PROPERTY_PROPERTYTYPE_INVALID_CODE)
					.message(WcmsConstants.PROPERTY_PROPERTYTYPE_INVALID_FIELD_NAME)
					.field(WcmsConstants.PROPERTY_PROPERTYTYPE_INVALID_ERROR_MESSAGE).build();
			errorFields.add(errorField);
		} else if (!donationService.getUsageTypeByName(donationRequest)) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.PROPERTY_USAGETYPE_INVALID_CODE)
					.message(WcmsConstants.PROPERTY_USAGETYPE_INVALID_FIELD_NAME)
					.field(WcmsConstants.PROPERTY_USAGETYPE_INVALID_ERROR_MESSAGE).build();
			errorFields.add(errorField);
		}
	}

	private void checkCategoryTypeAndPipeSizeExist(final List<ErrorField> errorFields,
			final DonationRequest donationRequest) {
		if (propertPipeSizeService.checkPipeSizeExists(donationRequest.getDonation().getMaxPipeSize(),
				donationRequest.getDonation().getTenantId())) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.DONATION_PIPESIZE_MAX_INVALID_CODE)
					.message(WcmsConstants.DONATION_PIPESIZE_MAX_INVALID_ERROR_MESSAGE)
					.field(WcmsConstants.DONATION_PIPESIZE_MAX_INVALID_FIELD_NAME).build();
			errorFields.add(errorField);
		} else if (propertPipeSizeService.checkPipeSizeExists(donationRequest.getDonation().getMinPipeSize(),
				donationRequest.getDonation().getTenantId())) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.DONATION_PIPESIZE_MIN_INVALID_CODE)
					.message(WcmsConstants.DONATION_PIPESIZE_MIN_INVALID_ERROR_MESSAGE)
					.field(WcmsConstants.DONATION_PIPESIZE_MIN_INVALID_FIELD_NAME).build();
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
				.message(WcmsConstants.INVALID_USAGETYPE_REQUEST_MESSAGE).errorFields(errorFields).build();
	}

	private List<ErrorField> getErrorFields(final MeterCostRequest meterCostRequest) {
		final List<ErrorField> errorFields = new ArrayList<>();
		addTenantIdValidationErrors(meterCostRequest.getMeterCost().getTenantId(), errorFields);
		addActiveValidationErrors(meterCostRequest.getMeterCost().getActive(), errorFields);
		return errorFields;
	}

	public List<ErrorResponse> validatePropertyPipeSizeRequest(
			final PropertyTypePipeSizeRequest propertyPipeSizeRequest) {
		final List<ErrorResponse> errorResponses = new ArrayList<>();
		final ErrorResponse errorResponse = new ErrorResponse();
		final Error error = getError(propertyPipeSizeRequest);
		errorResponse.setError(error);
		if (!errorResponse.getErrorFields().isEmpty())
			errorResponses.add(errorResponse);

		return errorResponses;
	}

	private Error getError(final PropertyTypePipeSizeRequest propertyPipeSizeRequest) {
		final List<ErrorField> errorFields = getErrorFields(propertyPipeSizeRequest);
		return Error.builder().code(HttpStatus.BAD_REQUEST.value())
				.message(WcmsConstants.INVALID_PROPERTY_PIPESIZE_REQUEST_MESSAGE).errorFields(errorFields).build();
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
					.field(WcmsConstants.PROPERTY_PIPESIZE_PROPERTYTYPE_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		} else if (propertyPipeSize.getPipeSize() == null) {
			final ErrorField errorField = ErrorField.builder()
					.code(WcmsConstants.PROPERTY_PIPESIZE_HSCSIZEINMM_MANDATORY_CODE)
					.message(WcmsConstants.PROPERTY_PIPESIZE_HSCSIZEINMM_MANADATORY_ERROR_MESSAGE)
					.field(WcmsConstants.PROPERTY_PIPESIZE_HSCSIZEINMM_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		} else if (!propertPipeSizeService.getPropertyTypeByName(propertyPipeSizeRequest)) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.PROPERTY_PROPERTYTYPE_INVALID_CODE)
					.message(WcmsConstants.PROPERTY_PROPERTYTYPE_INVALID_FIELD_NAME)
					.field(WcmsConstants.PROPERTY_PROPERTYTYPE_INVALID_ERROR_MESSAGE).build();
			errorFields.add(errorField);

		} else if (propertPipeSizeService.checkPipeSizeExists(propertyPipeSize.getPipeSize(),
				propertyPipeSize.getTenantId())) {
			final ErrorField errorField = ErrorField.builder()
					.code(WcmsConstants.PROPERTY_PIPESIZE_HSCSIZEINMM_INVALID_CODE)
					.message(WcmsConstants.PROPERTY_PIPESIZE_HSCSIZEINMM_INVALID_ERROR_MESSAGE)
					.field(WcmsConstants.PROPERTY_PIPESIZE_HSCSIZEINMM_INVALID_FIELD_NAME).build();
			errorFields.add(errorField);

		} else if (!propertPipeSizeService.checkPropertyByPipeSize(propertyPipeSizeRequest)) {
			final ErrorField errorField = ErrorField.builder()
					.code(WcmsConstants.PROPERTY_PIPESIZE_SIZEINMM_UNIQUE_CODE)
					.message(WcmsConstants.PROPERTY_PIPESIZE_SIZEINMM_UNQ_ERROR_MESSAGE)
					.field(WcmsConstants.PROPERTY_PIPESIZE_SIZEINMM_UNQ_FIELD_NAME).build();
			errorFields.add(errorField);
		} else
			return;
	}

	public List<ErrorResponse> validatePropertyCategoryRequest(
			final PropertyTypeCategoryTypeReq propertyCategoryRequest) {
		final List<ErrorResponse> errorResponses = new ArrayList<>();
		final ErrorResponse errorResponse = new ErrorResponse();
		final Error error = getError(propertyCategoryRequest);
		errorResponse.setError(error);
		if (!errorResponse.getErrorFields().isEmpty())
			errorResponses.add(errorResponse);
		return errorResponses;
	}

	private Error getError(final PropertyTypeCategoryTypeReq propertyCategoryRequest) {
		final List<ErrorField> errorFields = getErrorFields(propertyCategoryRequest);
		return Error.builder().code(HttpStatus.BAD_REQUEST.value())
				.message(WcmsConstants.INVALID_PROPERTY_CATEGORY_REQUEST_MESSAGE).errorFields(errorFields).build();
	}

	private List<ErrorField> getErrorFields(final PropertyTypeCategoryTypeReq propertyCategoryRequest) {
		final List<ErrorField> errorFields = new ArrayList<>();
		addPropertyCategoryValidationErrors(propertyCategoryRequest, errorFields);
		addTenantIdValidationErrors(propertyCategoryRequest.getPropertyTypeCategoryType().getTenantId(), errorFields);
		addActiveValidationErrors(propertyCategoryRequest.getPropertyTypeCategoryType().getActive(), errorFields);
		return errorFields;
	}

	private void addPropertyCategoryValidationErrors(final PropertyTypeCategoryTypeReq propertyCategoryRequest,
			final List<ErrorField> errorFields) {
		final PropertyTypeCategoryType propertyCategory = propertyCategoryRequest.getPropertyTypeCategoryType();
		if (propertyCategory.getCategoryTypeName() == null || propertyCategory.getCategoryTypeName().isEmpty()) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.CATEGORY_NAME_MANDATORY_CODE)
					.message(WcmsConstants.CATEGORY_NAME_MANADATORY_ERROR_MESSAGE)
					.field(WcmsConstants.CATEGORY_NAME_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		} else if (propertyCategory.getPropertyTypeName() == null || propertyCategory.getPropertyTypeName().isEmpty()) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.PROPERTYTYPE_MANDATORY_CODE)
					.message(WcmsConstants.PROPERTYTYPE_MANDATORY_ERROR_MESSAGE)
					.field(WcmsConstants.PROPERTYTYPE_MANDATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		} else if (!propertyCategoryService.getPropertyTypeByName(propertyCategoryRequest)) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.PROPERTY_PROPERTYTYPE_INVALID_CODE)
					.message(WcmsConstants.PROPERTY_PROPERTYTYPE_INVALID_FIELD_NAME)
					.field(WcmsConstants.PROPERTY_PROPERTYTYPE_INVALID_ERROR_MESSAGE).build();
			errorFields.add(errorField);

		} else if (!propertyCategoryService.checkIfMappingExists(propertyCategoryRequest)) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.PROPERTY_CATEGORY_INVALID_CODE)
					.message(WcmsConstants.PROPERTY_CATEGORY_INVALID_ERROR_MESSAGE)
					.field(WcmsConstants.PROPERTY_CATEGORY_INVALID_FIELD_NAME).build();
			errorFields.add(errorField);
		}

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
		final List<ErrorField> errorFields = getErrorFields(propUsageTypeRequest);
		return Error.builder().code(HttpStatus.BAD_REQUEST.value())
				.message(WcmsConstants.INVALID_PROPERTYUSAGETYPE_REQUEST_MESSAGE).errorFields(errorFields).build();
	}

	private List<ErrorField> getErrorFields(final PropertyTypeUsageTypeReq propUsageTypeRequest) {
		final List<ErrorField> errorFields = new ArrayList<>();
		addPropertyUsageValidationErrors(propUsageTypeRequest, errorFields);
		addTenantIdValidationErrors(propUsageTypeRequest.getPropertyTypeUsageType().getTenantId(), errorFields);
		addActiveValidationErrors(propUsageTypeRequest.getPropertyTypeUsageType().getActive(), errorFields);
		return errorFields;
	}

	private void addPropertyUsageValidationErrors(final PropertyTypeUsageTypeReq propUsageTypeRequest,
			final List<ErrorField> errorFields) {

		if (propUsageTypeRequest.getPropertyTypeUsageType().getPropertyType() == null
				|| propUsageTypeRequest.getPropertyTypeUsageType().getPropertyType().isEmpty()) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.PROPERTYTYPE_MANDATORY_CODE)
					.message(WcmsConstants.PROPERTYTYPE_MANDATORY_ERROR_MESSAGE)
					.field(WcmsConstants.PROPERTYTYPE_MANDATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		} else if (propUsageTypeRequest.getPropertyTypeUsageType().getUsageType() == null
				|| propUsageTypeRequest.getPropertyTypeUsageType().getUsageType().isEmpty()) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.USAGETYPE_NAME_MANDATORY_CODE)
					.message(WcmsConstants.USAGETYPE_NAME_MANADATORY_ERROR_MESSAGE)
					.field(WcmsConstants.USAGETYPE_NAME_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		} else if (!propertyUsageTypeService.getPropertyTypeByName(propUsageTypeRequest)) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.PROPERTY_PROPERTYTYPE_INVALID_CODE)
					.message(WcmsConstants.PROPERTY_PROPERTYTYPE_INVALID_FIELD_NAME)
					.field(WcmsConstants.PROPERTY_PROPERTYTYPE_INVALID_ERROR_MESSAGE).build();
			errorFields.add(errorField);
		} else if (!propertyUsageTypeService.getUsageTypeByName(propUsageTypeRequest)) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.PROPERTY_USAGETYPE_INVALID_CODE)
					.message(WcmsConstants.PROPERTY_USAGETYPE_INVALID_FIELD_NAME)
					.field(WcmsConstants.PROPERTY_USAGETYPE_INVALID_ERROR_MESSAGE).build();
			errorFields.add(errorField);
		} else if (!propertyUsageTypeService.checkPropertyUsageTypeExists(propUsageTypeRequest)) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.PROPERTYTYPE_USAGETYPE_UNIQUE_CODE)
					.message(WcmsConstants.PROPERTYTYPE_USAGETYPE_UNQ_ERROR_MESSAGE)
					.field(WcmsConstants.PROPERTYTYPE_USAGETYPE_UNQ_FIELD_NAME).build();
			errorFields.add(errorField);
		}

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
				.message(WcmsConstants.INVALID_WATERSOURCETYPE_REQUEST_MESSAGE).errorFields(errorFields).build();
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
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.WATERSOURCETYPE_NAME_MANDATORY_CODE)
					.message(WcmsConstants.WATERSOURCETYPE_NAME_MANADATORY_ERROR_MESSAGE)
					.field(WcmsConstants.WATERSOURCETYPE_NAME_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		} else if (!sourceTypeService.getWaterSourceByNameAndCode(waterSource.getCode(), waterSource.getName(),
				waterSource.getTenantId())) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.WATERSOURCETYPE_NAME_UNIQUE_CODE)
					.message(WcmsConstants.WATERSOURCETYPE_UNQ_ERROR_MESSAGE)
					.field(WcmsConstants.WATERSOURCETYPE_NAME_UNQ_FIELD_NAME).build();
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
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.SUPPLYTYPE_NAME_MANDATORY_CODE)
					.message(WcmsConstants.SUPPLYTYPE_NAME_MANADATORY_ERROR_MESSAGE)
					.field(WcmsConstants.SUPPLYTYPE_NAME_MANADATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		} else if (supply.getCode() != null && !supplyTypeService.getSupplyTypeByNameAndCode(supply.getCode(),
				supply.getName(), supply.getTenantId())) {
			final ErrorField errorField = ErrorField.builder().code(WcmsConstants.SUPPLYTYPE_NAME_UNIQUE_CODE)
					.message(WcmsConstants.SUPPLYTYPE_UNQ_ERROR_MESSAGE)
					.field(WcmsConstants.SUPPLYTYPE_NAME_UNQ_FIELD_NAME).build();
			errorFields.add(errorField);
		} else
			return;
	}

	private Error getError(final SupplyTypeRequest supplyTypeRequest) {
		final List<ErrorField> errorFiled = getErrorFields(supplyTypeRequest);
		return Error.builder().code(HttpStatus.BAD_REQUEST.value())
				.message(WcmsConstants.INVALID_CATEGORY_REQUEST_MESSAGE).errorFields(errorFiled).build();
	}

}
