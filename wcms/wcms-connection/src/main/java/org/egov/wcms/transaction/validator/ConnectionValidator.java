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
package org.egov.wcms.transaction.validator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.response.ErrorField;
import org.egov.wcms.transaction.config.ConfigurationManager;
import org.egov.wcms.transaction.model.Connection;
import org.egov.wcms.transaction.model.DocumentOwner;
import org.egov.wcms.transaction.model.MeterReading;
import org.egov.wcms.transaction.service.WaterConnectionService;
import org.egov.wcms.transaction.util.WcmsConnectionConstants;
import org.egov.wcms.transaction.web.contract.DonationResponseInfo;
import org.egov.wcms.transaction.web.contract.PipeSizeResponseInfo;
import org.egov.wcms.transaction.web.contract.PropertyResponse;
import org.egov.wcms.transaction.web.contract.WaterConnectionReq;
import org.egov.wcms.transaction.web.errorhandler.Error;
import org.egov.wcms.transaction.web.errorhandler.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConnectionValidator {

	public static final String consumerNumberPrefix = "0000000000";

	@Autowired
	private RestConnectionService restConnectionService;

	@Autowired
	private WaterConnectionService waterConnectionService;

	@Autowired
	private ConfigurationManager configurationManager;

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

	public List<ErrorResponse> validateWaterConnectionRequest(final WaterConnectionReq waterConnectionRequest) {
		final List<ErrorResponse> errorResponses = new ArrayList<>();
		final ErrorResponse errorResponse = new ErrorResponse();
		final Error error = getError(waterConnectionRequest);
		errorResponse.setError(error);
		if (!errorResponse.getErrorFields().isEmpty())
			errorResponses.add(errorResponse);

		return errorResponses;
	}

	public Error getError(final WaterConnectionReq waterConnectionRequest) {
		final List<ErrorField> errorFields = new ArrayList<>();

		if (errorFields.size() > 0)
			return Error.builder().code(HttpStatus.BAD_REQUEST.value())
					.message(WcmsConnectionConstants.INVALID_REQUEST_MESSAGE).errorFields(errorFields).build();

		checkMandatoryFields(waterConnectionRequest, errorFields);

		if (waterConnectionRequest.getConnection().getIsLegacy()) {
			checkLegacyMasterFields(waterConnectionRequest, errorFields);
		}

		final List<ErrorField> masterfielderrorList = getMasterValidation(waterConnectionRequest);
		errorFields.addAll(masterfielderrorList);

		final List<ErrorField> errorFieldList = validateNewConnectionBusinessRules(waterConnectionRequest);
		errorFields.addAll(errorFieldList);

		return Error.builder().code(HttpStatus.BAD_REQUEST.value())
				.message(WcmsConnectionConstants.INVALID_REQUEST_MESSAGE).errorFields(errorFields).build();
	}
	
	/**
	 * This method validates the mandatory fields for their null and blank values
	 * @param waterConnectionRequest
	 * @param errorFields
	 */
	public void checkMandatoryFields(WaterConnectionReq waterConnectionRequest, List<ErrorField> errorFields) {
		if (StringUtils.isNotBlank(waterConnectionRequest.getConnection().getBillingType())) {
			errorFields.add(buildErrorField(WcmsConnectionConstants.BILLING_TYPE_INVALID_CODE,
					WcmsConnectionConstants.BILLING_TYPE_INVALID_ERROR_MESSAGE,
					WcmsConnectionConstants.BILLING_TYPE_INVALID_FIELD_NAME));
		}
		if (StringUtils.isNotBlank(waterConnectionRequest.getConnection().getApplicationType())) {
			errorFields.add(buildErrorField(WcmsConnectionConstants.APPLICATIONTYPE_MANDATORY_CODE,
					WcmsConnectionConstants.APPLICATIONTYPE_INVALID_ERROR_MESSAGE,
					WcmsConnectionConstants.APPLICATIONTYPE_INVALID_FIELD_NAME));
		}
		if (StringUtils.isNotBlank(waterConnectionRequest.getConnection().getConnectionType())) {
			errorFields.add(buildErrorField(WcmsConnectionConstants.CONNECTION_TYPE_INVALID_CODE,
					WcmsConnectionConstants.CONNECTION_INVALID_ERROR_MESSAGE,
					WcmsConnectionConstants.CONNECTION_TYPE_INVALID_FIELD_NAME));
		}
		if (StringUtils.isNotBlank(waterConnectionRequest.getConnection().getHscPipeSizeType())) {
			errorFields.add(buildErrorField(WcmsConnectionConstants.PIPESIZE_SIZEINMM_MANDATORY_CODE,
					WcmsConnectionConstants.PIPESIZE_SIZEINMM__MANADATORY_ERROR_MESSAGE,
					WcmsConnectionConstants.PIPESIZE_SIZEINMM__MANADATORY_FIELD_NAME));
		}
		if (StringUtils.isNotBlank(waterConnectionRequest.getConnection().getUsageType())) {
			errorFields.add(buildErrorField(WcmsConnectionConstants.USAGETYPE_NAME_MANDATORY_CODE,
					WcmsConnectionConstants.USAGETYPE_NAME_MANADATORY_ERROR_MESSAGE,
					WcmsConnectionConstants.USAGETYPE_NAME_MANADATORY_FIELD_NAME));
		}

		if (StringUtils.isNotBlank(waterConnectionRequest.getConnection().getSourceType())) {
			errorFields.add(buildErrorField(WcmsConnectionConstants.SOURCE_TYPE_INVALID_CODE,
					WcmsConnectionConstants.SOURCE_TYPE_INVALID_ERROR_MESSAGE,
					WcmsConnectionConstants.SOURCE_TYPE_INVALID_FIELD_NAME));
		}

		if (waterConnectionRequest.getConnection().getSumpCapacity() == 0) {
			errorFields.add(buildErrorField(WcmsConnectionConstants.SUMP_CAPACITY_INVALID_CODE,
					WcmsConnectionConstants.SUMP_CAPACITY_INVALID_ERROR_MESSAGE,
					WcmsConnectionConstants.SUMP_CAPACITY_INVALID_FIELD_NAME));
		}

		if (StringUtils.isNotBlank(waterConnectionRequest.getConnection().getSupplyType())) {
			errorFields.add(buildErrorField(WcmsConnectionConstants.SUPPLY_TYPE_INVALID_CODE,
					WcmsConnectionConstants.SUPPLY_TYPE_INVALID_ERROR_MESSAGE,
					WcmsConnectionConstants.SUPPLY_TYPE_INVALID_FIELD_NAME));
		}
		if (StringUtils.isNotBlank(waterConnectionRequest.getConnection().getLegacyConsumerNumber())) {
			Connection waterConn = waterConnectionService.getWaterConnectionByConsumerNumber(null,
					waterConnectionRequest.getConnection().getLegacyConsumerNumber(),
					waterConnectionRequest.getConnection().getTenantId());
			if (waterConn != null) {
				errorFields.add(buildErrorField(WcmsConnectionConstants.LEGACY_CONNECTION_INVALID_CODE,
						WcmsConnectionConstants.LEGACY_CONNECTION_INVALID_ERROR_MESSAGE,
						WcmsConnectionConstants.LEGACY_CONNECTION_INVALID_FIELD_NAME));
			}
		}

		if (!waterConnectionRequest.getConnection().getIsLegacy()) {
			if (waterConnectionRequest.getConnection().getDocuments() == null
					|| waterConnectionRequest.getConnection().getDocuments().isEmpty()) {
				errorFields.add(buildErrorField(WcmsConnectionConstants.DOCUMENTS_INVALID_CODE,
						WcmsConnectionConstants.DOCUMENTS_INVALID_ERROR_MESSAGE,
						WcmsConnectionConstants.DOCUMENTS_INVALID_FIELD_NAME));
			} else {
				for (final DocumentOwner document : waterConnectionRequest.getConnection().getDocuments())
					if (null == document.getDocument()) {
						errorFields.add(buildErrorField(WcmsConnectionConstants.DOCUMENTS_INVALID_CODE,
								WcmsConnectionConstants.DOCUMENTS_INVALID_ERROR_MESSAGE,
								WcmsConnectionConstants.DOCUMENTS_INVALID_FIELD_NAME));
					}
			}
		}
	}

	/**
	 * This method checks the Legacy Fields for thier null and blank values
	 * @param waterConnectionRequest
	 * @param errorFields
	 */
	public void checkLegacyMasterFields(WaterConnectionReq waterConnectionRequest, List<ErrorField> errorFields) {
		if (restConnectionService
				.getWaterChargeConfigValuesForAadhar(waterConnectionRequest.getConnection().getTenantId())) {
			errorFields.add(buildErrorField(WcmsConnectionConstants.AADHRA_MANDATORY_CODE,
					WcmsConnectionConstants.AADHRA_MANADATORY_ERROR_MESSAGE,
					WcmsConnectionConstants.AADHRA_MANADATORY_FIELD_NAME));
		}

		if (waterConnectionRequest.getConnection().getExecutionDate() == null) {
			final ErrorField errorField = ErrorField.builder()
					.code(WcmsConnectionConstants.LEGACY_EXECUTIONDATE_INVALID_CODE)
					.message(WcmsConnectionConstants.LEGACY_EXECUTIONDATE_INVALID_ERROR_MESSAGE)
					.field(WcmsConnectionConstants.LEGACY_EXECUTIONDATE_INVALID_FIELD_NAME).build();
			errorFields.add(errorField);
		}
		if (waterConnectionRequest.getConnection().getBillingType() != null
				&& waterConnectionRequest.getConnection().getBillingType().equals("METERED")) {
			log.info("Validating Connection Meter details");
			if (waterConnectionRequest.getConnection().getMeter() == null
					|| waterConnectionRequest.getConnection().getMeter().isEmpty()) {
				errorFields.add(buildErrorField(WcmsConnectionConstants.CONNECTION_METERED_INVALID_CODE,
						WcmsConnectionConstants.CONNECTION_METERED_INVALID_ERROR_MESSAGE,
						WcmsConnectionConstants.CONNECTION_METERED_INVALID_FIELD_NAME));
			}
			if (StringUtils.isNotBlank(waterConnectionRequest.getConnection().getMeter().get(0).getMeterOwner())) {
				errorFields.add(buildErrorField(WcmsConnectionConstants.CONNECTION_METERED_OWNER_INVALID_CODE,
						WcmsConnectionConstants.CONNECTION_METERED_OWNER_INVALID_ERROR_MESSAGE,
						WcmsConnectionConstants.CONNECTION_METERED_OWNER_INVALID_FIELD_NAME));
			}
			if (StringUtils.isNotBlank(waterConnectionRequest.getConnection().getMeter().get(0).getMeterModel())) {
				errorFields.add(buildErrorField(WcmsConnectionConstants.CONNECTION_METERED_MODEL_INVALID_CODE,
						WcmsConnectionConstants.CONNECTION_METERED_MODEL_INVALID_ERROR_MESSAGE,
						WcmsConnectionConstants.CONNECTION_METERED_MODEL_INVALID_FIELD_NAME));
			}
			if (StringUtils.isNotBlank(waterConnectionRequest.getConnection().getMeter().get(0).getMeterSlNo())) {
				errorFields.add(buildErrorField(WcmsConnectionConstants.CONNECTION_METERED_NUMBER_INVALID_CODE,
						WcmsConnectionConstants.CONNECTION_METERED_NUMBER_INVALID_ERROR_MESSAGE,
						WcmsConnectionConstants.CONNECTION_METERED_NUMBER_INVALID_FIELD_NAME));
			}
			if (StringUtils
					.isNotBlank(waterConnectionRequest.getConnection().getMeter().get(0).getMaximumMeterReading())) {
				errorFields.add(buildErrorField(WcmsConnectionConstants.CONNECTION_METERED_MAXMETERREADING_INVALID_CODE,
						WcmsConnectionConstants.CONNECTION_METERED_MAXMETERREADING_INVALID_ERROR_MESSAGE,
						WcmsConnectionConstants.CONNECTION_METERED_MAXMETERREADING_INVALID_FIELD_NAME));
			}
			if (waterConnectionRequest.getConnection().getMeter().get(0).getMeterReadings() == null
					|| waterConnectionRequest.getConnection().getMeter().get(0).getMeterReadings().isEmpty()) {
				errorFields.add(
						buildErrorField(WcmsConnectionConstants.CONNECTION_METERED_METERREADINGDETAILS_INVALID_CODE,
								WcmsConnectionConstants.CONNECTION_METERED_METERREADINGDETAILS_INVALID_ERROR_MESSAGE,
								WcmsConnectionConstants.CONNECTION_METERED_METERREADINGDETAILS_INVALID_FIELD_NAME));
			} else {
				log.info("Validating Connection MeterReadings details");
				List<MeterReading> meterReadingList = waterConnectionRequest.getConnection().getMeter().get(0)
						.getMeterReadings();
				for (MeterReading meterReading : meterReadingList) {
					if (meterReading.getReading() <= 0) {
						errorFields.add(
								buildErrorField(WcmsConnectionConstants.CONNECTION_METERED_METERREADING_INVALID_CODE,
										WcmsConnectionConstants.CONNECTION_METERED_METERREADING_INVALID_ERROR_MESSAGE,
										WcmsConnectionConstants.CONNECTION_METERED_METERREADING_INVALID_FIELD_NAME));
					}

					if (meterReading.getReadingDate() <= 0) {
						errorFields.add(buildErrorField(
								WcmsConnectionConstants.CONNECTION_METERED_METERREADINGDATE_INVALID_CODE,
								WcmsConnectionConstants.CONNECTION_METERED_METERREADINGDATE_INVALID_ERROR_MESSAGE,
								WcmsConnectionConstants.CONNECTION_METERED_METERREADINGDATE_INVALID_FIELD_NAME));
					}
					;
					break;
				}

			}
		}
	}

	public List<ErrorField> validateNewConnectionBusinessRules(final WaterConnectionReq waterConnectionRequest) {
		boolean isRequestValid = false;
		final List<ErrorField> errorFields = new ArrayList<>();

		if (waterConnectionRequest.getConnection().getProperty() != null
				&& waterConnectionRequest.getConnection().getProperty().getPropertyidentifier() != null
				&& !waterConnectionRequest.getConnection().getProperty().getPropertyidentifier().equals("")) {
			final PropertyResponse propResp = restConnectionService.getPropertyDetailsByUpicNo(waterConnectionRequest);
			if (propResp.getProperties() != null && propResp.getProperties().isEmpty()) {
				errorFields.add(buildErrorField(WcmsConnectionConstants.PROPERTY_INVALID_CODE,
						WcmsConnectionConstants.PROPERTY_INVALID_ERROR_MESSAGE,
						WcmsConnectionConstants.PROPERTY_INVALID_FIELD_NAME));
			}
		}

		isRequestValid = validateStaticFields(waterConnectionRequest);
		if (!isRequestValid) {
			errorFields.add(buildErrorField(WcmsConnectionConstants.STATIC_INVALID_CODE,
					WcmsConnectionConstants.STATIC_INVALID_ERROR_MESSAGE,
					WcmsConnectionConstants.STATIC_INVALID_FIELD_NAME));
		}

		if (!waterConnectionRequest.getConnection().getIsLegacy()) {
			final DonationResponseInfo donationresInfo = restConnectionService
					.validateDonationAmount(waterConnectionRequest);
			if (donationresInfo == null) {
				errorFields.add(buildErrorField(WcmsConnectionConstants.DONATION_INVALID_CODE,
						WcmsConnectionConstants.DONATION_INVALID_ERROR_MESSAGE,
						WcmsConnectionConstants.DONATION_INVALID_FIELD_NAME));
			}
		}

		return errorFields;
	}

	private boolean validateStaticFields(final WaterConnectionReq waterConnectionRequest) {
		log.info("Validating ConnectionType, BillingType, SupplyType, SourceType");

		boolean isRequestValid = false;

		if (!(waterConnectionRequest.getConnection().getConnectionType().equals("TEMPORARY")
				|| waterConnectionRequest.getConnection().getConnectionType().equals("PERMANENT"))) {
			log.info("ConnectionType is INVALID");
			return isRequestValid;
		} else if (!(waterConnectionRequest.getConnection().getBillingType().equals("METERED")
				|| waterConnectionRequest.getConnection().getBillingType().equals("NONMETERED"))) {
			log.info("BillingType is INVALID");
			return isRequestValid;
		}

		isRequestValid = true;
		return isRequestValid;
	}

	/**
	 * This method validates the Master Data fields for their availability in WCMS Masters
	 * @param waterConnectionRequest
	 * @return
	 */
	public List<ErrorField> getMasterValidation(final WaterConnectionReq waterConnectionRequest) {
		final List<ErrorField> errorFields = new ArrayList<>();

		final PipeSizeResponseInfo pipeinfo = restConnectionService.getPipesizeTypeByCode(waterConnectionRequest);
		if (pipeinfo.getPipeSize() != null && pipeinfo.getPipeSize().isEmpty()) {
			errorFields.add(buildErrorField(WcmsConnectionConstants.PIPESIZE_INVALID_CODE,
					WcmsConnectionConstants.PIPESIZE_INVALID_FIELD_NAME,
					WcmsConnectionConstants.PIPESIZE_INVALID_ERROR_MESSAGE));
		}
		if (restConnectionService.getSourceTypeByName(waterConnectionRequest).getWaterSourceType().isEmpty()) {
			errorFields.add(buildErrorField(WcmsConnectionConstants.SOURCETYPE_INVALID_CODE,
					WcmsConnectionConstants.SOURCETYPE_INVALID_FIELD_NAME,
					WcmsConnectionConstants.SOURCETYPE_INVALID_ERROR_MESSAGE));
		}
		if (restConnectionService.getTreateMentPlantName(waterConnectionRequest).getTreatmentPlants().isEmpty()) {
			errorFields.add(buildErrorField(WcmsConnectionConstants.TREATPLANT_INVALID_CODE,
					WcmsConnectionConstants.TREATPLANT_INVALID_FIELD_NAME,
					WcmsConnectionConstants.TREATPLANT_INVALID_ERROR_MESSAGE));
		}
		if (restConnectionService.getSupplyTypeByName(waterConnectionRequest).getSupplytypes().isEmpty()) {
			errorFields.add(buildErrorField(WcmsConnectionConstants.SUPPLYTYPE_INVALID_CODE,
					WcmsConnectionConstants.SUPPLYTYPE_INVALID_FIELD_NAME,
					WcmsConnectionConstants.SUPPLYTYPE_INVALID_ERROR_MESSAGE));
		}
		if (restConnectionService.getUsageTypeName(waterConnectionRequest).getUsageTypes().isEmpty()) {
			errorFields.add(buildErrorField(WcmsConnectionConstants.SUBUSAGETYPE_INVALID_CODE,
					WcmsConnectionConstants.SUBUSAGETYPE_INVALID_ERROR_MESSAGE,
					WcmsConnectionConstants.SUBUSAGETYPE_INVALID_FIELD_NAME));
		}
		if (restConnectionService.getSubUsageTypeName(waterConnectionRequest).getUsageTypes().isEmpty()) {
			errorFields.add(buildErrorField(WcmsConnectionConstants.SUBUSAGETYPE_INVALID_CODE,
					WcmsConnectionConstants.SUBUSAGETYPE_INVALID_ERROR_MESSAGE,
					WcmsConnectionConstants.SUBUSAGETYPE_INVALID_FIELD_NAME));
		}
		if (restConnectionService.getStorageReservoirName(waterConnectionRequest).getStorageReservoirs().isEmpty()) {
			errorFields.add(buildErrorField(WcmsConnectionConstants.STORAGERESERVOIR_MANDATORY_CODE,
					WcmsConnectionConstants.STORAGERESERVOIR_MANDATORY_ERROR_MESSAGE,
					WcmsConnectionConstants.STORAGERESERVOIR_MANDATORY_FIELD_NAME));
		}
		return errorFields;
	}

	/**
	 * This method returns ErrorField object for the Code, Message and Field
	 * Params
	 * @param code
	 * @param message
	 * @param field
	 * @return
	 */
	private ErrorField buildErrorField(String code, String message, String field) {
		return ErrorField.builder().code(code).message(message).field(field).build();

	}

	public String generateAcknowledgementNumber(final WaterConnectionReq waterConnectionRequest) {
		return restConnectionService.generateRequestedDocumentNumber(
				waterConnectionRequest.getConnection().getTenantId(), configurationManager.getIdGenNameServiceTopic(),
				configurationManager.getIdGenFormatServiceTopic(), waterConnectionRequest.getRequestInfo());
	}

	public String generateConsumerNumber(final WaterConnectionReq waterConnectionRequest) {
		final Long nextConsumerNumber = waterConnectionService.generateNextConsumerNumber();
		final Integer format = configurationManager.getHscNumberOfChar();
		final String ulbName = restConnectionService.getULBNameFromTenant(
				waterConnectionRequest.getConnection().getTenantId(), waterConnectionRequest.getRequestInfo());
		final String completeConsumerNumber = ulbName
				.concat(StringUtils.right(consumerNumberPrefix + String.valueOf(nextConsumerNumber), format));
		return completeConsumerNumber;
	}

}