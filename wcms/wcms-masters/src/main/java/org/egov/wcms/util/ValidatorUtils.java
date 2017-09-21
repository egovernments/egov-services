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
import org.egov.wcms.model.DocumentType;
import org.egov.wcms.model.DocumentTypeApplicationType;
import org.egov.wcms.model.Donation;
import org.egov.wcms.model.Gapcode;
import org.egov.wcms.model.MeterCost;
import org.egov.wcms.model.MeterStatus;
import org.egov.wcms.model.MeterWaterRates;
import org.egov.wcms.model.NonMeterWaterRates;
import org.egov.wcms.model.PipeSize;
import org.egov.wcms.model.ServiceCharge;
import org.egov.wcms.model.SourceType;
import org.egov.wcms.model.StorageReservoir;
import org.egov.wcms.model.SupplyType;
import org.egov.wcms.model.TreatmentPlant;
import org.egov.wcms.model.UsageType;
import org.egov.wcms.model.enums.ApplicationType;
import org.egov.wcms.model.enums.ConnectionType;
import org.egov.wcms.model.enums.PlantType;
import org.egov.wcms.model.enums.ReservoirType;
import org.egov.wcms.service.DocumentTypeApplicationTypeService;
import org.egov.wcms.service.DocumentTypeService;
import org.egov.wcms.service.DonationService;
import org.egov.wcms.service.MeterCostService;
import org.egov.wcms.service.MeterWaterRatesService;
import org.egov.wcms.service.NonMeterWaterRatesService;
import org.egov.wcms.service.PipeSizeService;
import org.egov.wcms.service.SourceTypeService;
import org.egov.wcms.service.StorageReservoirService;
import org.egov.wcms.service.SupplyTypeService;
import org.egov.wcms.service.TreatmentPlantService;
import org.egov.wcms.web.contract.DocumentTypeApplicationTypeReq;
import org.egov.wcms.web.contract.DocumentTypeReq;
import org.egov.wcms.web.contract.DonationRequest;
import org.egov.wcms.web.contract.GapcodeRequest;
import org.egov.wcms.web.contract.MeterCostReq;
import org.egov.wcms.web.contract.MeterStatusReq;
import org.egov.wcms.web.contract.MeterWaterRatesRequest;
import org.egov.wcms.web.contract.NonMeterWaterRatesReq;
import org.egov.wcms.web.contract.PipeSizeRequest;
import org.egov.wcms.web.contract.ServiceChargeReq;
import org.egov.wcms.web.contract.SourceTypeRequest;
import org.egov.wcms.web.contract.StorageReservoirRequest;
import org.egov.wcms.web.contract.SupplyTypeRequest;
import org.egov.wcms.web.contract.TreatmentPlantRequest;
import org.egov.wcms.web.contract.UsageTypeReq;
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
    private PipeSizeService pipeSizeService;

    @Autowired
    private DocumentTypeApplicationTypeService documentApplicationService;

    @Autowired
    private DocumentTypeService documentTypeService;

    @Autowired
    private SourceTypeService sourceTypeService;

    @Autowired
    private SupplyTypeService supplyTypeService;

    @Autowired
    private DonationService donationService;

    @Autowired
    private StorageReservoirService storageReservoirService;

    @Autowired
    private TreatmentPlantService treatmentPlantService;

    @Autowired
    private MeterWaterRatesService meterWaterRatesService;

    @Autowired
    private MeterCostService meterCostService;

    @Autowired
    private NonMeterWaterRatesService nonMeterWaterRatesService;

    public List<ErrorResponse> validatePipeSizeRequest(final PipeSizeRequest pipeSizeRequest, final Boolean isUpdate) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(pipeSizeRequest, isUpdate);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);

        return errorResponses;
    }

    private Error getError(final PipeSizeRequest pipeSizeRequest, final Boolean isUpdate) {
        final List<ErrorField> errorFields = getErrorFields(pipeSizeRequest, isUpdate);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_PIPESIZE_REQUEST_MESSAGE).errorFields(errorFields).build();
    }

    private List<ErrorField> getErrorFields(final PipeSizeRequest pipeSizeRequest, final Boolean isUpdate) {
        final List<ErrorField> errorFields = new ArrayList<>();
        for (final PipeSize pipeSize : pipeSizeRequest.getPipeSizes()) {
            addPipeSizeInmmValidationErrors(pipeSize, errorFields, isUpdate);
            addTenantIdValidationErrors(pipeSize.getTenantId(), errorFields);
            addActiveValidationErrors(pipeSize.getActive(), errorFields);
        }
        return errorFields;
    }

    private void addPipeSizeInmmValidationErrors(final PipeSize pipeSize,
            final List<ErrorField> errorFields, final Boolean isUpdate) {
        if (isUpdate)
            if (StringUtils.isBlank(pipeSize.getCode())) {
                final ErrorField errorField = ErrorField.builder().code(WcmsConstants.CODE_MANDATORY_CODE)
                        .message(WcmsConstants.CODE_MANDATORY_ERROR_MESSAGE)
                        .field(WcmsConstants.CODE_MANDATORY_FIELD_NAME).build();
                errorFields.add(errorField);
            }
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

    public List<ErrorResponse> validateDocumentApplicationRequest(final DocumentTypeApplicationTypeReq documentNameRequest,
            final Boolean isUpdate) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(documentNameRequest, isUpdate);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);

        return errorResponses;
    }

    private Error getError(final DocumentTypeApplicationTypeReq documentNameRequest, final Boolean isUpdate) {
        final List<ErrorField> errorFields = getErrorFields(documentNameRequest, isUpdate);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_DOCTYPE_APPLICATION_TYPE_REQUEST_MESSAGE).errorFields(errorFields)
                .build();
    }

    private List<ErrorField> getErrorFields(final DocumentTypeApplicationTypeReq documentApplicationReq, final Boolean isUpdate) {
        final List<ErrorField> errorFields = new ArrayList<>();
        for (final DocumentTypeApplicationType docTypeAppType : documentApplicationReq.getDocumentTypeApplicationTypes()) {
            addDocumentTypeValidationErrors(docTypeAppType, errorFields, isUpdate);
            addTenantIdValidationErrors(docTypeAppType.getTenantId(), errorFields);
            addActiveValidationErrors(docTypeAppType.getActive(), errorFields);
            addApplicationTypeValidationErrors(docTypeAppType, errorFields);
        }
        return errorFields;
    }

    private void addDocumentTypeValidationErrors(final DocumentTypeApplicationType docTypeAppType,
            final List<ErrorField> errorFields, final Boolean isUpdate) {
        if (isUpdate)
            if (StringUtils.isBlank(docTypeAppType.getCode())) {
                final ErrorField errorField = ErrorField.builder().code(WcmsConstants.CODE_MANDATORY_CODE)
                        .message(WcmsConstants.CODE_MANDATORY_ERROR_MESSAGE)
                        .field(WcmsConstants.CODE_MANDATORY_FIELD_NAME).build();
                errorFields.add(errorField);
            }
        if (StringUtils.isBlank(docTypeAppType.getDocumentType())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.DOCTYPE_MANDATORY_CODE)
                    .message(WcmsConstants.DOCTYPE_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.DOCTYPE_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);

        } else if (documentApplicationService.checkDocumentTypeExists(docTypeAppType.getDocumentType(),
                docTypeAppType.getTenantId())) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.DOCUMENTTYPE_INVALID_CODE)
                    .message(WcmsConstants.DOCUMENTTYPE_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.DOCUMENTTYPE_INVALID_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (!documentApplicationService.checkDocumentTypeApplicationTypeExist(docTypeAppType.getCode(),
                docTypeAppType.getApplicationType(),
                docTypeAppType.getDocumentType(), docTypeAppType.getTenantId())) {

            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.DOCTYPE_APPLICATIONTYPE_UNIQUE_CODE)
                    .message(WcmsConstants.DOCTYPE_APPLICATIONTYPE_UNQ_ERROR_MESSAGE)
                    .field(WcmsConstants.DOCTYPE_APPLICATIONTYPE_UNQ_FIELD_NAME).build();
            errorFields.add(errorField);
        }
        return;
    }

    private void addApplicationTypeValidationErrors(final DocumentTypeApplicationType docTypeAppType,
            final List<ErrorField> errorFields) {
        if (StringUtils.isBlank(docTypeAppType.getApplicationType())) {
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

    public List<ErrorResponse> validateDocumentTypeRequest(final DocumentTypeReq documentTypeReq, final Boolean isUpdate) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(documentTypeReq, isUpdate);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);

        return errorResponses;
    }

    private Error getError(final DocumentTypeReq documentTypeReq, final Boolean isUpdate) {
        final List<ErrorField> errorFields = getErrorFields(documentTypeReq, isUpdate);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_DOCUMENTTYPE_REQUEST_MESSAGE).errorFields(errorFields).build();
    }

    private List<ErrorField> getErrorFields(final DocumentTypeReq documentTypeReq, final Boolean isUpdate) {
        final List<ErrorField> errorFields = new ArrayList<>();
        for (final DocumentType documentType : documentTypeReq.getDocumentTypes()) {
            addDocumentTypeNameValidationErrors(documentType, errorFields, isUpdate);
            addTenantIdValidationErrors(documentType.getTenantId(), errorFields);
            addActiveValidationErrors(documentType.getActive(), errorFields);
        }
        return errorFields;
    }

    private void addDocumentTypeNameValidationErrors(final DocumentType documentType,
            final List<ErrorField> errorFields, final Boolean isUpdate) {
        if (isUpdate)
            if (StringUtils.isBlank(documentType.getCode())) {
                final ErrorField errorField = ErrorField.builder().code(WcmsConstants.CODE_MANDATORY_CODE)
                        .message(WcmsConstants.CODE_MANDATORY_ERROR_MESSAGE)
                        .field(WcmsConstants.CODE_MANDATORY_FIELD_NAME).build();
                errorFields.add(errorField);
            }
        if (StringUtils.isBlank(documentType.getName())) {
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

    public List<ErrorResponse> validateDonationRequest(final DonationRequest donationRequest, final Boolean isUpdate) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(donationRequest, isUpdate);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);
        return errorResponses;
    }

    private Error getError(final DonationRequest donationRequest, final Boolean isUpdate) {
        final List<ErrorField> errorFields = getErrorFields(donationRequest, isUpdate);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_DONATION_REQUEST_MESSAGE).errorFields(errorFields).build();
    }

    private List<ErrorField> getErrorFields(final DonationRequest donationRequest, final Boolean isUpdate) {
        final List<ErrorField> errorFields = new ArrayList<>();
        for (final Donation donation : donationRequest.getDonations()) {
            checkDonationCode(errorFields, donation, isUpdate);
            checkUsageTypeMandatoryAndExists(errorFields, donation);
            checkSubUsageTypeMandatoryAndExists(errorFields, donation);
            checkPipeSizeValues(errorFields, donation);
            checkDonationAmountValues(errorFields, donation);
            checkFromToDateValues(errorFields, donation);
            checkMaxPipesizeAndMinPipeSize(errorFields, donation);
            checkPipeSizeExist(errorFields, donation);
            checkDonationsExist(errorFields, donation);
            addTenantIdValidationErrors(donation.getTenantId(), errorFields);
        }
        return errorFields;
    }

    private void checkDonationCode(final List<ErrorField> errorFields, final Donation donation, final Boolean isUpdate) {
        if (isUpdate)
            if (StringUtils.isBlank(donation.getCode())) {
                final ErrorField errorField = ErrorField.builder().code(WcmsConstants.CODE_MANDATORY_CODE)
                        .message(WcmsConstants.CODE_MANDATORY_ERROR_MESSAGE)
                        .field(WcmsConstants.CODE_MANDATORY_FIELD_NAME).build();
                errorFields.add(errorField);
            }

    }

    private void checkUsageTypeMandatoryAndExists(final List<ErrorField> errorFields, final Donation donation) {
        if (StringUtils.isBlank(donation.getUsageTypeCode())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.USAGETYPE_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.USAGETYPE_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.USAGETYPE_NAME_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (donationService.checkUsageAndSubUsageExists(donation.getUsageTypeCode(), donation.getTenantId())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.USAGETYPE_INVALID_CODE)
                    .message(WcmsConstants.USAGETYPE_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.USAGETYPE_INVALID_FIELD_NAME).build();
            errorFields.add(errorField);
        }

    }

    private void checkPipeSizeValues(final List<ErrorField> errorFields, final Donation donation) {
        if (donation.getMaxPipeSize() == null
                || donation.getMinPipeSize() == null) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.PIPESIZE_SIZEINMM_MANDATORY_CODE)
                    .message(WcmsConstants.PIPESIZE_SIZEINMM__MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.PIPESIZE_SIZEINMM__MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        }
    }

    private void checkMaxPipesizeAndMinPipeSize(final List<ErrorField> errorFields, final Donation donation) {
        if (donation.getMinPipeSize() > 0
                && donation.getMaxPipeSize() > 0)
            if (donation.getMinPipeSize() > donation.getMaxPipeSize()) {
                final ErrorField errorField = ErrorField.builder().code(WcmsConstants.DONATION_MINPIPESIZE_MAXPIPESIZE_CODE)
                        .message(WcmsConstants.DONATION_MINPIPESIZE_MAXPIPESIZE_ERROR_MESSAGE)
                        .field(WcmsConstants.DONATION_MINPIPESIZE_MAXPIPESIZE_FIELD_NAME).build();
                errorFields.add(errorField);
            }

            else if (donation.getMinPipeSize().equals(donation.getMaxPipeSize())) {
                final ErrorField errorField = ErrorField.builder().code(WcmsConstants.DONATION_MINPIPESIZE_MAXPIPESIZE_EQUAL_CODE)
                        .message(WcmsConstants.DONATION_MINPIPESIZE_MAXPIPESIZE__EQUAL_ERROR_MESSAGE)
                        .field(WcmsConstants.DONATION_MINPIPESIZE_MAXPIPESIZE__EQUALFIELD_NAME).build();
                errorFields.add(errorField);

            }

    }

    private void checkDonationAmountValues(final List<ErrorField> errorFields, final Donation donation) {
        if (donation.getDonationAmount() == null) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.DONATION_MANDATORY_CODE)
                    .message(WcmsConstants.DONATION_MANDATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.DONATION_MANDATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        }
    }

    private void checkFromToDateValues(final List<ErrorField> errorFields, final Donation donation) {
        if (donation.getFromDate() == null || donation.getToDate() == null) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.FROMTO_MANDATORY_CODE)
                    .message(WcmsConstants.FROMTO_MANDATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.FROMTO_MANDATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (donation.getFromDate() > donation.getToDate()) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.FROMDATE_TODATE_CODE)
                    .message(WcmsConstants.FROMDATE_TODATE_ERROR_MESSAGE)
                    .field(WcmsConstants.FROMDATE_TODATE_FIELD_NAME).build();
            errorFields.add(errorField);
        }
    }

    private void checkSubUsageTypeMandatoryAndExists(final List<ErrorField> errorFields,
            final Donation donation) {
        if (StringUtils.isBlank(donation.getSubUsageTypeCode())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.USAGETYPE_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.USAGETYPE_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.USAGETYPE_NAME_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (donationService.checkUsageAndSubUsageExists(donation.getSubUsageTypeCode(), donation.getTenantId())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.SUB_USAGETYPE_INVALID_CODE)
                    .message(WcmsConstants.SUB_USAGETYPE_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.SUB_USAGETYPE_INVALID_FIELD_NAME).build();
            errorFields.add(errorField);
        }
    }

    private void checkDonationsExist(final List<ErrorField> errorFields,
            final Donation donation) {

        if (!donationService.checkDonationsExist(donation)) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.DONATION_UNIQUE_CODE)
                    .message(WcmsConstants.DONATION_UNQ_ERROR_MESSAGE)
                    .field(WcmsConstants.DONATION_UNQ_FIELD_NAME).build();
            errorFields.add(errorField);
        }
    }

    private void checkPipeSizeExist(final List<ErrorField> errorFields,
            final Donation donation) {
        if (donationService.checkPipeSizeExists(donation.getMaxPipeSize(),
                donation.getTenantId())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.DONATION_PIPESIZE_MAX_INVALID_CODE)
                    .message(WcmsConstants.DONATION_PIPESIZE_MAX_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.DONATION_PIPESIZE_MAX_INVALID_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (donationService.checkPipeSizeExists(donation.getMinPipeSize(),
                donation.getTenantId())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.DONATION_PIPESIZE_MIN_INVALID_CODE)
                    .message(WcmsConstants.DONATION_PIPESIZE_MIN_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.DONATION_PIPESIZE_MIN_INVALID_FIELD_NAME).build();
            errorFields.add(errorField);
        }
    }

    public List<ErrorResponse> validateMeterStatusRequest(final MeterStatusReq meterStatusRequest, final Boolean isUpdate) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(meterStatusRequest, isUpdate);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);
        return errorResponses;
    }

    private Error getError(final MeterStatusReq meterStatusRequest, final Boolean isUpdate) {
        final List<ErrorField> errorFields = getErrorFields(meterStatusRequest, isUpdate);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_METER_STATUS_REQUEST_MESSAGE).errorFields(errorFields).build();
    }

    private List<ErrorField> getErrorFields(final MeterStatusReq meterStatusRequest, final Boolean isUpdate) {
        final List<ErrorField> errorFields = new ArrayList<>();
        final List<MeterStatus> meterStatuses = meterStatusRequest.getMeterStatus();
        for (final MeterStatus meterStatus : meterStatuses)
            addMeterStatusValidationErrors(meterStatus, errorFields, isUpdate);
        return errorFields;
    }

    private void addMeterStatusValidationErrors(final MeterStatus meterStatus, final List<ErrorField> errorFields,
            final Boolean isUpdate) {
        if (isUpdate)
            if (StringUtils.isBlank(meterStatus.getCode())) {
                final ErrorField errorField = ErrorField.builder().code(WcmsConstants.CODE_MANDATORY_CODE)
                        .message(WcmsConstants.CODE_MANDATORY_ERROR_MESSAGE)
                        .field(WcmsConstants.CODE_MANDATORY_FIELD_NAME).build();
                errorFields.add(errorField);
            }
        if (StringUtils.isBlank(meterStatus.getTenantId())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.TENANTID_MANDATORY_CODE)
                    .message(WcmsConstants.TENANTID_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.TENANTID_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (meterStatus.getMeterStatus() == null) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.ACTIVE_MANDATORY_CODE)
                    .message(WcmsConstants.ACTIVE_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.ACTIVE_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else
            return;
    }

    public List<ErrorResponse> validateMeterCostRequest(final MeterCostReq meterCostRequest, final Boolean isUpdate) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(meterCostRequest, isUpdate);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);
        return errorResponses;
    }

    private Error getError(final MeterCostReq meterCostRequest, final Boolean isUpdate) {
        final List<ErrorField> errorFields = getErrorFields(meterCostRequest, isUpdate);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_METER_COST_REQUEST_MESSAGE).errorFields(errorFields).build();
    }

    private List<ErrorField> getErrorFields(final MeterCostReq meterCostRequest, final Boolean isUpdate) {
        final List<ErrorField> errorFields = new ArrayList<>();
        final List<MeterCost> meterCosts = meterCostRequest.getMeterCost();
        for (final MeterCost meterCost : meterCosts)
            addMeterCostValidationErrors(meterCost, errorFields, isUpdate);
        return errorFields;
    }

    private void addMeterCostValidationErrors(final MeterCost meterCost, final List<ErrorField> errorFields,
            final Boolean isUpdate) {
        if (isUpdate)
            if (StringUtils.isBlank(meterCost.getCode())) {
                final ErrorField errorField = ErrorField.builder().code(WcmsConstants.CODE_MANDATORY_CODE)
                        .message(WcmsConstants.CODE_MANDATORY_ERROR_MESSAGE)
                        .field(WcmsConstants.CODE_MANDATORY_FIELD_NAME).build();
                errorFields.add(errorField);
            }

        if (StringUtils.isBlank(meterCost.getTenantId())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.TENANTID_MANDATORY_CODE)
                    .message(WcmsConstants.TENANTID_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.TENANTID_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);

        } else if (meterCost.getAmount().toString() == null || meterCost.getAmount().toString().isEmpty()) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.AMOUNT_MANDATORY_CODE)
                    .message(WcmsConstants.AMOUNT_MANDATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.AMOUNT_MANDATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (meterCost.getMeterMake() == null || meterCost.getMeterMake().isEmpty()) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.METERMAKE_MANDATORY_CODE)
                    .message(WcmsConstants.METERMAKE_MANDATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.METERMAKE_MANDATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (!meterCostService.checkMeterMakeAndAmountAlreadyExists(meterCost)) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.NAMETENANTID_UNIQUE_CODE)
                    .message(WcmsConstants.NAMETENANTID_UNIQUE_ERROR_MESSAGE)
                    .field(WcmsConstants.NAMETENANTID_UNIQUE_FIELD_NAME).build();
            errorFields.add(errorField);
        } else
            return;

    }

    public List<ErrorResponse> validateWaterSourceRequest(final SourceTypeRequest waterSourceTypeRequest,
            final Boolean isUpdate) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(waterSourceTypeRequest, isUpdate);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);
        return errorResponses;
    }

    private Error getError(final SourceTypeRequest waterSourceTypeRequest, final Boolean isUpdate) {
        final List<ErrorField> errorFields = getErrorFields(waterSourceTypeRequest, isUpdate);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_WATERSOURCETYPE_REQUEST_MESSAGE).errorFields(errorFields).build();
    }

    private List<ErrorField> getErrorFields(final SourceTypeRequest waterSourceTypeRequest, final Boolean isUpdate) {
        final List<ErrorField> errorFields = new ArrayList<>();
        for (final SourceType sourcetype : waterSourceTypeRequest.getSourceTypes()) {
            addSupplyTypeNameValidationErrors(sourcetype, errorFields, isUpdate);
            addTenantIdValidationErrors(sourcetype.getTenantId(), errorFields);
            addActiveValidationErrors(sourcetype.getActive(), errorFields);
        }
        return errorFields;
    }

    private void addSupplyTypeNameValidationErrors(final SourceType sourceType,
            final List<ErrorField> errorFields, final Boolean isUpdate) {
        if (isUpdate)
            if (StringUtils.isBlank(sourceType.getCode())) {
                final ErrorField errorField = ErrorField.builder().code(WcmsConstants.CODE_MANDATORY_CODE)
                        .message(WcmsConstants.CODE_MANDATORY_ERROR_MESSAGE)
                        .field(WcmsConstants.CODE_MANDATORY_FIELD_NAME).build();
                errorFields.add(errorField);
            }

        if (StringUtils.isBlank(sourceType.getName())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.WATERSOURCETYPE_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.WATERSOURCETYPE_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.WATERSOURCETYPE_NAME_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (!sourceTypeService.getWaterSourceByNameAndCode(sourceType.getCode(), sourceType.getName(),
                sourceType.getTenantId())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.WATERSOURCETYPE_NAME_UNIQUE_CODE)
                    .message(WcmsConstants.WATERSOURCETYPE_UNQ_ERROR_MESSAGE)
                    .field(WcmsConstants.WATERSOURCETYPE_NAME_UNQ_FIELD_NAME).build();
            errorFields.add(errorField);
        } else
            return;
    }

    public List<ErrorResponse> validateSupplyType(final SupplyTypeRequest supplyTypeRequest, final Boolean isUpdate) {
        final ErrorResponse errorResponse = new ErrorResponse();
        final List<ErrorResponse> errorResponseList = new ArrayList<>();
        final Error error = getError(supplyTypeRequest, isUpdate);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponseList.add(errorResponse);

        return errorResponseList;
    }

    private Error getError(final SupplyTypeRequest supplyTypeRequest, final Boolean isUpdate) {
        final List<ErrorField> errorFiled = getErrorFields(supplyTypeRequest, isUpdate);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_SUPPLY_TYPE_REQUEST_MESSAGE).errorFields(errorFiled).build();
    }

    private List<ErrorField> getErrorFields(final SupplyTypeRequest supplyTypeRequest, final Boolean isUpdate) {
        final List<ErrorField> errorFields = new ArrayList<>();
        for (final SupplyType supplyType : supplyTypeRequest.getSupplyTypes()) {
            addSupplyNameValidationErrors(supplyType, errorFields, isUpdate);
            addTenantIdValidationErrors(supplyType.getTenantId(), errorFields);
        }
        return errorFields;
    }

    private void addSupplyNameValidationErrors(final SupplyType supplyType,
            final List<ErrorField> errorFields, final Boolean isUpdate) {
        if (isUpdate)
            if (StringUtils.isBlank(supplyType.getCode())) {
                final ErrorField errorField = ErrorField.builder().code(WcmsConstants.CODE_MANDATORY_CODE)
                        .message(WcmsConstants.CODE_MANDATORY_ERROR_MESSAGE)
                        .field(WcmsConstants.CODE_MANDATORY_FIELD_NAME).build();
                errorFields.add(errorField);
            }
        if (StringUtils.isBlank(supplyType.getName())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.SUPPLYTYPE_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.SUPPLYTYPE_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.SUPPLYTYPE_NAME_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (!supplyTypeService.getSupplyTypeByNameAndCode(supplyType.getCode(),
                supplyType.getName(), supplyType.getTenantId())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.SUPPLYTYPE_NAME_UNIQUE_CODE)
                    .message(WcmsConstants.SUPPLYTYPE_UNQ_ERROR_MESSAGE)
                    .field(WcmsConstants.SUPPLYTYPE_NAME_UNQ_FIELD_NAME).build();
            errorFields.add(errorField);
        } else
            return;
    }

    public List<ErrorResponse> validateStorageReservoirRequest(final StorageReservoirRequest storageReservoirRequest,
            final Boolean isUpdate) {
        final ErrorResponse errorResponse = new ErrorResponse();
        final List<ErrorResponse> errorResponseList = new ArrayList<>();
        final Error error = getError(storageReservoirRequest, isUpdate);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponseList.add(errorResponse);
        return errorResponseList;

    }

    private Error getError(final StorageReservoirRequest storageReservoirRequest, final Boolean isUpdate) {
        final List<ErrorField> errorFiled = getErrorFields(storageReservoirRequest, isUpdate);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_STORAGE_RESERVOIR_REQUEST_MESSAGE).errorFields(errorFiled).build();
    }

    private List<ErrorField> getErrorFields(final StorageReservoirRequest storageReservoirRequest, final Boolean isUpdate) {
        final List<ErrorField> errorFields = new ArrayList<>();
        for (final StorageReservoir storageReservoir : storageReservoirRequest.getStorageReservoirs()) {
            addStorageReservoirValidationErrors(storageReservoir, errorFields, isUpdate);
            addTenantIdValidationErrors(storageReservoir.getTenantId(), errorFields);
        }
        return errorFields;
    }

    private void addStorageReservoirValidationErrors(final StorageReservoir storageReservoir,
            final List<ErrorField> errorFields, final Boolean isUpdate) {
        if (isUpdate)
            if (StringUtils.isBlank(storageReservoir.getCode())) {
                final ErrorField errorField = ErrorField.builder().code(WcmsConstants.CODE_MANDATORY_CODE)
                        .message(WcmsConstants.CODE_MANDATORY_ERROR_MESSAGE)
                        .field(WcmsConstants.CODE_MANDATORY_FIELD_NAME).build();
                errorFields.add(errorField);
            }
        if (StringUtils.isBlank(storageReservoir.getName())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.STORAGE_RESERVOIR_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.STORAGE_RESERVOIR_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.STORAGE_RESERVOIR_NAME_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        }
        if (StringUtils.isBlank(storageReservoir.getLocation())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.LOCATION_MANDATORY_CODE)
                    .message(WcmsConstants.LOCATION_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.LOCATION_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        }
        if (storageReservoir.getCapacity() == 0) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.CAPACITY_MANDATORY_CODE)
                    .message(WcmsConstants.CAPACITY_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.CAPACITY_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        }
        if (StringUtils.isBlank(storageReservoir.getReservoirType())) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.STORAGE_RESERVOIR_RESERVOIR_TYPE_MANDATORY_CODE)
                    .message(WcmsConstants.STORAGE_RESERVOIR_RESERVOIR_TYPE_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.STORAGE_RESERVOIR_RESERVOIR_TYPE_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (ReservoirType.fromValue(storageReservoir.getReservoirType()) == null) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.RESERVOIR_TYPE_INVALID_CODE)
                    .message(WcmsConstants.RESERVOIR_TYPE_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.RESERVOIR_TYPE_INVALID_FIELD_NAME).build();
            errorFields.add(errorField);
        }
        if (!storageReservoirService.getStorageReservoirByNameAndCode(storageReservoir.getCode(),
                storageReservoir.getName(),
                storageReservoir.getTenantId())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.STORAGERESERVOIR_NAME_UNIQUE_CODE)
                    .message(WcmsConstants.STORAGERESERVOIR_UNQ_ERROR_MESSAGE)
                    .field(WcmsConstants.STORAGERESERVOIR_NAME_UNQ_FIELD_NAME).build();
            errorFields.add(errorField);
        }
        return;
    }

    public List<ErrorResponse> validateTreatmentPlantRequest(final TreatmentPlantRequest treatmentPlantRequest,
            final Boolean isUpdate) {
        final ErrorResponse errorResponse = new ErrorResponse();
        final List<ErrorResponse> errorResponseList = new ArrayList<>();
        final Error error = getError(treatmentPlantRequest, isUpdate);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponseList.add(errorResponse);
        return errorResponseList;

    }

    private Error getError(final TreatmentPlantRequest treatmentPlantRequest, final Boolean isUpdate) {
        final List<ErrorField> errorFiled = getErrorFields(treatmentPlantRequest, isUpdate);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_TREATMENT_PLANT_REQUEST_MESSAGE).errorFields(errorFiled).build();
    }

    private List<ErrorField> getErrorFields(final TreatmentPlantRequest treatmentPlantRequest, final Boolean isUpdate) {
        final List<ErrorField> errorFields = new ArrayList<>();
        for (final TreatmentPlant treatmentPlant : treatmentPlantRequest.getTreatmentPlants()) {
            addTreatmentPlantValidationErrors(treatmentPlant, errorFields, isUpdate);
            addTenantIdValidationErrors(treatmentPlant.getTenantId(), errorFields);
        }
        return errorFields;
    }

    private void addTreatmentPlantValidationErrors(final TreatmentPlant treatmentPlant,
            final List<ErrorField> errorFields, final Boolean isUpdate) {
        if (isUpdate)
            if (StringUtils.isBlank(treatmentPlant.getCode())) {
                final ErrorField errorField = ErrorField.builder().code(WcmsConstants.CODE_MANDATORY_CODE)
                        .message(WcmsConstants.CODE_MANDATORY_ERROR_MESSAGE)
                        .field(WcmsConstants.CODE_MANDATORY_FIELD_NAME).build();
                errorFields.add(errorField);
            }
        if (StringUtils.isBlank(treatmentPlant.getName())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.TREATMENT_PLANT_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.TREATMENT_PLANT_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.TREATMENT_PLANT_NAME_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        }
        if (StringUtils.isBlank(treatmentPlant.getLocation())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.LOCATION_MANDATORY_CODE)
                    .message(WcmsConstants.LOCATION_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.LOCATION_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);

        }
        if (treatmentPlant.getCapacity() == 0) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.CAPACITY_MANDATORY_CODE)
                    .message(WcmsConstants.CAPACITY_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.CAPACITY_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);

        }
        if (StringUtils.isBlank(treatmentPlant.getPlantType())) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.TREATMENT_PLANT_TYPE_MANDATORY_CODE)
                    .message(WcmsConstants.TREATMENT_PLANT_TYPE_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.TREATMENT_PLANT_TYPE_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (PlantType.fromValue(treatmentPlant.getPlantType()) == null) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.PLANT_TYPE_INVALID_CODE)
                    .message(WcmsConstants.PLANT_TYPE_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.PLANT_TYPE_INVALID_FIELD_NAME).build();
            errorFields.add(errorField);
        }
        if (StringUtils.isNotBlank(treatmentPlant.getStorageReservoirName()))
            if (treatmentPlantService.checkStorageReservoirExists(treatmentPlant.getStorageReservoirName(),
                    treatmentPlant.getTenantId())) {
                final ErrorField errorField = ErrorField.builder()
                        .code(WcmsConstants.STORAGE_RESERVOIR_NAME_INVALID_CODE)
                        .message(WcmsConstants.STORAGE_RESERVOIR_NAME_INVALID_ERROR_MESSAGE)
                        .field(WcmsConstants.STORAGE_RESERVOIR_NAME_INVALID_FIELD_NAME).build();
                errorFields.add(errorField);
            }
        if (!treatmentPlantService.getTreatmentPlantByNameAndCode(treatmentPlant.getCode(), treatmentPlant.getName(),
                treatmentPlant.getTenantId())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.TREATMENT_PLANT_NAME_UNIQUE_CODE)
                    .message(WcmsConstants.TREATMENTPLANT_UNQ_ERROR_MESSAGE)
                    .field(WcmsConstants.TREATMENT_PLANT_NAME_UNQ_FIELD_NAME).build();
            errorFields.add(errorField);
        }
        return;

    }

    public List<ErrorResponse> validateMeterWaterRatesRequest(final MeterWaterRatesRequest meterWaterRatesRequest,
            final Boolean isUpdate) {
        final ErrorResponse errorResponse = new ErrorResponse();
        final List<ErrorResponse> errorResponseList = new ArrayList<>();
        final Error error = getError(meterWaterRatesRequest, isUpdate);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponseList.add(errorResponse);
        return errorResponseList;

    }

    private Error getError(final MeterWaterRatesRequest meterWaterRatesRequest, final Boolean isUpdate) {
        final List<ErrorField> errorFiled = getErrorFields(meterWaterRatesRequest, isUpdate);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_METER_WATER_RATES_REQUEST_MESSAGE).errorFields(errorFiled).build();
    }

    private List<ErrorField> getErrorFields(final MeterWaterRatesRequest meterWaterRatesRequest, final Boolean isUpdate) {
        final List<ErrorField> errorFields = new ArrayList<>();
        for (final MeterWaterRates meterWaterRates : meterWaterRatesRequest.getMeterWaterRates()) {
            addMeterWaterRatesValidationErrors(meterWaterRates, errorFields, isUpdate);
            addTenantIdValidationErrors(meterWaterRates.getTenantId(), errorFields);
        }
        return errorFields;
    }

    private void addMeterWaterRatesValidationErrors(final MeterWaterRates meterWaterRates,
            final List<ErrorField> errorFields, final Boolean isUpdate) {
        if (isUpdate)
            if (StringUtils.isBlank(meterWaterRates.getCode())) {
                final ErrorField errorField = ErrorField.builder().code(WcmsConstants.CODE_MANDATORY_CODE)
                        .message(WcmsConstants.CODE_MANDATORY_ERROR_MESSAGE)
                        .field(WcmsConstants.CODE_MANDATORY_FIELD_NAME).build();
                errorFields.add(errorField);
            }
        if (StringUtils.isBlank(meterWaterRates.getUsageTypeCode())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.USAGETYPECODE_CODE_MANDATORY_CODE)
                    .message(WcmsConstants.USAGETYPE_CODE_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.USAGETYPE_CODE_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);

        }
        else if (!meterWaterRatesService.isUsageTypeExists(meterWaterRates)) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.USAGETYPE_INVALID_CODE)
                    .message(WcmsConstants.USAGETYPE_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.USAGETYPE_INVALID_FIELD_NAME).build();
            errorFields.add(errorField);
        }
        if (StringUtils.isBlank(meterWaterRates.getSubUsageTypeCode())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.SUBUSAGETYPE_CODE_MANDATORY_CODE)
                    .message(WcmsConstants.SUBUSAGETYPE_CODE_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.SUBUSAGETYPE_CODE_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        }
        else if (!meterWaterRatesService.isSubUsageTypeExists(meterWaterRates)) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.SUB_USAGETYPE_INVALID_CODE)
                    .message(WcmsConstants.SUB_USAGETYPE_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.SUB_USAGETYPE_INVALID_FIELD_NAME).build();
            errorFields.add(errorField);
        }
        if (StringUtils.isBlank(meterWaterRates.getSourceTypeName())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.SOURCETYPE_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.SOURCETYPE_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.SOURCETYPE_NAME_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        }
        else  if (meterWaterRatesService.checkSourceTypeExists(meterWaterRates.getSourceTypeName(),
                meterWaterRates.getTenantId())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.SOURCE_TYPE_NAME_INVALID_CODE)
                    .message(WcmsConstants.SOURCE_TYPE_NAME_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.SOURCE_TYPE_NAME_INVALID_FIELD_NAME).build();
            errorFields.add(errorField);
        }
        if (meterWaterRates.getFromDate() == null || meterWaterRates.getToDate() == null) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.FROMTO_MANDATORY_CODE)
                    .message(WcmsConstants.FROMTO_MANDATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.FROMTO_MANDATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        }

        else if (meterWaterRates.getFromDate() > meterWaterRates.getToDate()) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.FROMDATE_TODATE_CODE)
                    .message(WcmsConstants.FROMDATE_TODATE_ERROR_MESSAGE)
                    .field(WcmsConstants.FROMDATE_TODATE_FIELD_NAME).build();
            errorFields.add(errorField);
        }
        if (meterWaterRates.getPipeSize() == null) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.PIPESIZE_SIZEINMM_MANDATORY_CODE)
                    .message(WcmsConstants.PIPESIZE_SIZEINMM__MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.PIPESIZE_SIZEINMM__MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        }
        
        else if (meterWaterRatesService.checkPipeSizeExists(meterWaterRates.getPipeSize(),
                meterWaterRates.getTenantId())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.PIPESIZE_INMM_INVALID_CODE)
                    .message(WcmsConstants.PIPESIZE_INMM_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.PIPESIZE_INMM_FIELD_NAME).build();
            errorFields.add(errorField);
        }
       
        if (!meterWaterRatesService.checkMeterWaterRatesExists(meterWaterRates)) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.METER_WATER_RATES_UNIQUE_CODE)
                    .message(WcmsConstants.METER_WATER_RATES_UNQ_ERROR_MESSAGE)
                    .field(WcmsConstants.METER_WATER_RATES_UNQ_FIELD_NAME).build();
            errorFields.add(errorField);

        }
    }

    public List<ErrorResponse> validateNonMeterWaterRatesRequest(final NonMeterWaterRatesReq nonMeterWaterRatesReq,
            final Boolean isUpdate) {
        final ErrorResponse errorResponse = new ErrorResponse();
        final List<ErrorResponse> errorResponseList = new ArrayList<>();
        final Error error = getError(nonMeterWaterRatesReq, isUpdate);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponseList.add(errorResponse);
        return errorResponseList;

    }

    private Error getError(final NonMeterWaterRatesReq nonMeterWaterRatesReq, final Boolean isUpdate) {
        final List<ErrorField> errorFiled = getErrorFields(nonMeterWaterRatesReq, isUpdate);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_NON_METER_WATER_RATES_REQUEST_MESSAGE).errorFields(errorFiled).build();
    }

    private List<ErrorField> getErrorFields(final NonMeterWaterRatesReq nonMeterWaterRatesReq, final Boolean isUpdate) {
        final List<ErrorField> errorFields = new ArrayList<>();
        for (final NonMeterWaterRates nonMeterWaterRates : nonMeterWaterRatesReq.getNonMeterWaterRates()) {
            addNonMeterWaterRatesValidationErrors(nonMeterWaterRates, errorFields, isUpdate);
            addTenantIdValidationErrors(nonMeterWaterRates.getTenantId(), errorFields);
        }
        return errorFields;
    }

    private void addNonMeterWaterRatesValidationErrors(final NonMeterWaterRates nonMeterWaterRates,
            final List<ErrorField> errorFields, final Boolean isUpdate) {
        if (isUpdate)
            if (StringUtils.isBlank(nonMeterWaterRates.getCode())) {
                final ErrorField errorField = ErrorField.builder().code(WcmsConstants.CODE_MANDATORY_CODE)
                        .message(WcmsConstants.CODE_MANDATORY_ERROR_MESSAGE)
                        .field(WcmsConstants.CODE_MANDATORY_FIELD_NAME).build();
                errorFields.add(errorField);
            }

        if (nonMeterWaterRates.getFromDate() == null) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.FROMDATE_CODE)
                    .message(WcmsConstants.FROMDATE_ERROR_MESSAGE)
                    .field(WcmsConstants.FROMDATE_FIELD_NAME).build();
            errorFields.add(errorField);
        }
        if (StringUtils.isBlank(nonMeterWaterRates.getConnectionType())) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.CONNECTION_TYPE_MANDATORY_CODE)
                    .message(WcmsConstants.CONNECTION_TYPE_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.CONNECTION_TYPE_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (ConnectionType.fromValue(nonMeterWaterRates.getConnectionType()) == null) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.CONNECTION_TYPE_INVALID_CODE)
                    .message(WcmsConstants.CONNECTION_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.CONNECTION_TYPE_INVALID_FIELD_NAME).build();
            errorFields.add(errorField);
        }
        if (StringUtils.isBlank(nonMeterWaterRates.getUsageTypeCode())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.USAGETYPECODE_CODE_MANDATORY_CODE)
                    .message(WcmsConstants.USAGETYPE_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.USAGETYPE_NAME_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (!nonMeterWaterRatesService.isUsageTypeExists(nonMeterWaterRates)) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.USAGETYPE_INVALID_CODE)
                    .message(WcmsConstants.USAGETYPE_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.USAGETYPE_INVALID_FIELD_NAME).build();
            errorFields.add(errorField);
        }
        if (StringUtils.isBlank(nonMeterWaterRates.getSubUsageTypeCode())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.SUBUSAGETYPE_CODE_MANDATORY_CODE)
                    .message(WcmsConstants.SUBUSAGETYPE_CODE_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.SUBUSAGETYPE_CODE_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (!nonMeterWaterRatesService.isSubUsageTypeExists(nonMeterWaterRates)) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.SUB_USAGETYPE_INVALID_CODE)
                    .message(WcmsConstants.SUB_USAGETYPE_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.SUB_USAGETYPE_INVALID_FIELD_NAME).build();
            errorFields.add(errorField);
        }
        if (StringUtils.isBlank(nonMeterWaterRates.getSourceTypeName())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.SOURCETYPE_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.SOURCETYPE_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.SOURCETYPE_NAME_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (nonMeterWaterRatesService.checkSourceTypeExists(nonMeterWaterRates.getSourceTypeName(),
                nonMeterWaterRates.getTenantId())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.SOURCE_TYPE_NAME_INVALID_CODE)
                    .message(WcmsConstants.SOURCE_TYPE_NAME_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.SOURCE_TYPE_NAME_INVALID_FIELD_NAME).build();
            errorFields.add(errorField);
        }
        if (nonMeterWaterRates.getPipeSize() == null) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.PIPESIZE_SIZEINMM_MANDATORY_CODE)
                    .message(WcmsConstants.PIPESIZE_SIZEINMM__MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.PIPESIZE_SIZEINMM__MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (nonMeterWaterRatesService.checkPipeSizeExists(nonMeterWaterRates.getPipeSize(),
                nonMeterWaterRates.getTenantId())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.PIPESIZE_INMM_INVALID_CODE)
                    .message(WcmsConstants.PIPESIZE_INMM_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.PIPESIZE_INMM_FIELD_NAME).build();
            errorFields.add(errorField);
        }

        if (!nonMeterWaterRatesService.checkNonMeterWaterRatesExists(nonMeterWaterRates)) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.NON_METER_WATER_RATES_UNIQUE_CODE)
                    .message(WcmsConstants.NON_METER_WATER_RATES_UNQ_ERROR_MESSAGE)
                    .field(WcmsConstants.NON_METER_WATER_RATES_UNQ_FIELD_NAME).build();
            errorFields.add(errorField);

        }
    }

    public List<ErrorResponse> validateServiceChargeRequest(final ServiceChargeReq serviceChargeRequest, final boolean isUpdate) {
        final ErrorResponse errorResponse = new ErrorResponse();
        final List<ErrorResponse> errorResponseList = new ArrayList<>();
        final Error error = getError(serviceChargeRequest, isUpdate);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponseList.add(errorResponse);
        return errorResponseList;
    }

    private Error getError(final ServiceChargeReq serviceChargeRequest, final boolean isUpdate) {
        final List<ErrorField> errorFiled = getErrorFields(serviceChargeRequest, isUpdate);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_SERVICE_CHARGE_REQUEST_MESSAGE).errorFields(errorFiled).build();
    }

    private List<ErrorField> getErrorFields(final ServiceChargeReq serviceChargeRequest, final boolean isUpdate) {
        final List<ErrorField> errorFields = new ArrayList<>();
        for (final ServiceCharge serviceCharge : serviceChargeRequest.getServiceCharge())
            addServiceChargeValidationErrors(serviceCharge, errorFields, isUpdate);
        return errorFields;
    }

    private void addServiceChargeValidationErrors(final ServiceCharge serviceCharge, final List<ErrorField> errorFields,
            final boolean isUpdate) {
        if (isUpdate)
            if (serviceCharge.getCode() == null || serviceCharge.getCode().isEmpty()) {
                final ErrorField errorField = ErrorField.builder().code(WcmsConstants.CODE_MANDATORY_CODE)
                        .message(WcmsConstants.CODE_MANDATORY_ERROR_MESSAGE)
                        .field(WcmsConstants.CODE_MANDATORY_FIELD_NAME).build();
                errorFields.add(errorField);
            }
        if (StringUtils.isBlank(serviceCharge.getTenantId())) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.TENANTID_MANDATORY_CODE)
                    .message(WcmsConstants.TENANTID_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.TENANTID_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (serviceCharge.getServiceType() == null || serviceCharge.getServiceType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.SERVICETYPE_MANDATORY_CODE)
                    .message(WcmsConstants.SERVICETYPE_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.SERVICETYPE_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (serviceCharge.getServiceChargeApplicable() == null) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.SERVICECHARGEAPPLICABLE_MANDATORY_CODE)
                    .message(WcmsConstants.SERVICECHARGEAPPLICABLE_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.SERVICECHARGEAPPLICABLE_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        }

        else if (serviceCharge.getServiceChargeType() == null || serviceCharge.getServiceChargeType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.SERVICECHARGETYPE_MANDATORY_CODE)
                    .message(WcmsConstants.SERVICECHARGETYPE_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.SERVICECHARGETYPE_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (serviceCharge.getEffectiveFrom() == null) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.SERVICECHARGEEFFECTIVEFROM_MANDATORY_CODE)
                    .message(WcmsConstants.SERVICECHARGEEFFECTIVEFROM_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.SERVICECHARGEEFFECTIVEFROM_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (serviceCharge.getEffectiveTo() == null) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.SERVICECHARGEEFFECTIVETO_MANDATORY_CODE)
                    .message(WcmsConstants.SERVICECHARGEEFFECTIVETO_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.SERVICECHARGEEFFECTIVETO_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (serviceCharge.getActive() == null) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.ACTIVE_MANDATORY_CODE)
                    .message(WcmsConstants.ACTIVE_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.ACTIVE_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else
            return;

    }

    public List<ErrorResponse> validateGapcodeRequest(final GapcodeRequest categoryRequest, final Boolean isUpdate) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(categoryRequest, isUpdate);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);
        return errorResponses;
    }

    private Error getError(final GapcodeRequest gapcodeRequest, final Boolean isUpdate) {
        gapcodeRequest.getGapcode();
        final List<ErrorField> errorFields = getErrorFields(gapcodeRequest, isUpdate);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_GAPCODE_REQUEST_MESSAGE).errorFields(errorFields).build();
    }

    private List<ErrorField> getErrorFields(final GapcodeRequest gapcodeRequest, final Boolean isUpdate) {
        final List<ErrorField> errorFields = new ArrayList<>();
        for (final Gapcode gapcode : gapcodeRequest.getGapcode()) {
            addGapcodeNameValidationErrors(gapcode, errorFields, isUpdate);
            addTenantIdValidationErrors(gapcode.getTenantId(), errorFields);
            addNoOfMonthsValidationErrors(gapcode.getNoOfMonths(), errorFields);
            addLogicValidationErrors(gapcode.getLogic(), errorFields);
        }
        return errorFields;
    }

    private void addGapcodeNameValidationErrors(final Gapcode gapcode,
            final List<ErrorField> errorFields, final boolean isUpdate) {
        if (gapcode.getName() == null || gapcode.getName().isEmpty()) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.GAPCODE_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.GAPCODE_NAME_MANDATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.GAPCODE_NAME_MANDATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        }
    }

    private void addNoOfMonthsValidationErrors(final String noOfMonths, final List<ErrorField> errorFields) {
        if (noOfMonths == null || noOfMonths.isEmpty()) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.NO_OF_MONTHS_MANDATORY_CODE)
                    .message(WcmsConstants.NO_OF_MONTHS_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.NO_OF_MONTHS_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else
            return;
    }

    private void addLogicValidationErrors(final String logic, final List<ErrorField> errorFields) {
        if (logic == null || logic.isEmpty()) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConstants.LOGIC_MANDATORY_CODE)
                    .message(WcmsConstants.LOGIC_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.LOGIC_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else
            return;
    }

    public List<ErrorResponse> validateUsageTypeRequest(final UsageTypeReq usageTypeRequest, final boolean isUpdate) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(usageTypeRequest, isUpdate);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);
        return errorResponses;
    }

    private Error getError(final UsageTypeReq usageTypeRequest, final boolean isUpdate) {
        final List<ErrorField> errorFields = getErrorFields(usageTypeRequest, isUpdate);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_USAGETYPE_REQUEST_MESSAGE).errorFields(errorFields).build();
    }

    private List<ErrorField> getErrorFields(final UsageTypeReq usageTypeRequest, final boolean isUpdate) {
        final List<ErrorField> errorFields = new ArrayList<>();
        for (final UsageType usageType : usageTypeRequest.getUsageTypes()) {
            if (isUpdate)
                if (usageType.getCode() == null || usageType.getCode().isEmpty()) {
                    final ErrorField errorField = ErrorField.builder().code(WcmsConstants.CODE_MANDATORY_CODE)
                            .message(WcmsConstants.CODE_MANDATORY_ERROR_MESSAGE).field(WcmsConstants.CODE_MANDATORY_FIELD_NAME)
                            .build();
                    errorFields.add(errorField);
                }
            if (usageType.getTenantId() == null || usageType.getTenantId().isEmpty()) {
                final ErrorField errorField = ErrorField.builder().code(WcmsConstants.TENANTID_MANDATORY_CODE)
                        .message(WcmsConstants.TENANTID_MANADATORY_ERROR_MESSAGE)
                        .field(WcmsConstants.TENANTID_MANADATORY_FIELD_NAME).build();
                errorFields.add(errorField);
            } else if (usageType.getActive() == null) {
                final ErrorField errorField = ErrorField.builder().code(WcmsConstants.ACTIVE_MANDATORY_CODE)
                        .message(WcmsConstants.ACTIVE_MANADATORY_ERROR_MESSAGE).field(WcmsConstants.ACTIVE_MANADATORY_FIELD_NAME)
                        .build();
                errorFields.add(errorField);
            }
        }
        return errorFields;
    }
}
