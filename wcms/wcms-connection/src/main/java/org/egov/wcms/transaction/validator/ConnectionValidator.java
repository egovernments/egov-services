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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class ConnectionValidator {

    @Autowired
    private RestConnectionService restConnectionService;
    
    @Autowired
    private WaterConnectionService waterConnectionService; 

    public static final Logger LOGGER = LoggerFactory.getLogger(ConnectionValidator.class);
    public static final String consumerNumberPrefix = "0000000000"; 
    
    @Autowired
    private ConfigurationManager  configurationManager;

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
        if (waterConnectionRequest.getConnection().getBillingType() == null
                || waterConnectionRequest.getConnection().getBillingType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConnectionConstants.BILLING_TYPE_INVALID_CODE)
                    .message(WcmsConnectionConstants.BILLING_TYPE_INVALID_ERROR_MESSAGE)
                    .field(WcmsConnectionConstants.BILLING_TYPE_INVALID_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (waterConnectionRequest.getConnection().getCategoryType() == null) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConnectionConstants.CATEGORY_NAME_MANDATORY_CODE)
                    .message(WcmsConnectionConstants.CATEGORY_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConnectionConstants.CATEGORY_NAME_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (waterConnectionRequest.getConnection().getApplicationType() == null) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConnectionConstants.APPLICATIONTYPE_MANDATORY_CODE)
                    .message(WcmsConnectionConstants.APPLICATIONTYPE_INVALID_ERROR_MESSAGE)
                    .field(WcmsConnectionConstants.APPLICATIONTYPE_INVALID_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (waterConnectionRequest.getConnection().getConnectionType() == null
                || waterConnectionRequest.getConnection().getConnectionType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConnectionConstants.CONNECTION_TYPE_INVALID_CODE)
                    .message(WcmsConnectionConstants.CONNECTION_INVALID_ERROR_MESSAGE)
                    .field(WcmsConnectionConstants.CONNECTION_TYPE_INVALID_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (waterConnectionRequest.getConnection().getHscPipeSizeType() == null) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConnectionConstants.PIPESIZE_SIZEINMM_MANDATORY_CODE)
                    .message(WcmsConnectionConstants.PIPESIZE_SIZEINMM__MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConnectionConstants.PIPESIZE_SIZEINMM__MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (waterConnectionRequest.getConnection().getProperty() == null
				|| waterConnectionRequest.getConnection().getProperty().getPropertyType().isEmpty()) {
			final ErrorField errorField = ErrorField.builder()
					.code(WcmsConnectionConstants.PROPERTY_TYPE_MANDATORY_CODE)
					.message(WcmsConnectionConstants.PROPERTY_TYPE_MANDATORY_ERROR_MESSAGE)
					.field(WcmsConnectionConstants.PROPERTY_TYPE_MANDATORY_FIELD_NAME).build();
			errorFields.add(errorField);
		} else if (waterConnectionRequest.getConnection().getProperty() == null
                || waterConnectionRequest.getConnection().getProperty().getUsageType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConnectionConstants.USAGETYPE_NAME_MANDATORY_CODE)
                    .message(WcmsConnectionConstants.USAGETYPE_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConnectionConstants.USAGETYPE_NAME_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (waterConnectionRequest.getConnection().getSourceType() == null) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConnectionConstants.SOURCE_TYPE_INVALID_CODE)
                    .message(WcmsConnectionConstants.SOURCE_TYPE_INVALID_ERROR_MESSAGE)
                    .field(WcmsConnectionConstants.SOURCE_TYPE_INVALID_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (waterConnectionRequest.getConnection().getSumpCapacity() == 0) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConnectionConstants.SUMP_CAPACITY_INVALID_CODE)
                    .message(WcmsConnectionConstants.SUMP_CAPACITY_INVALID_ERROR_MESSAGE)
                    .field(WcmsConnectionConstants.SUMP_CAPACITY_INVALID_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (waterConnectionRequest.getConnection().getSupplyType() == null) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConnectionConstants.SUPPLY_TYPE_INVALID_CODE)
                    .message(WcmsConnectionConstants.SUPPLY_TYPE_INVALID_ERROR_MESSAGE)
                    .field(WcmsConnectionConstants.SUPPLY_TYPE_INVALID_FIELD_NAME).build();
            errorFields.add(errorField);
        }
        if(waterConnectionRequest.getConnection().getIsLegacy() ){
        	if(waterConnectionRequest.getConnection().getBillingType() !=null  && waterConnectionRequest.getConnection().getBillingType().equals("METERED")){
        		LOGGER.info("Validating Connection Meter details");
        		if ( waterConnectionRequest.getConnection().getMeter()==null || waterConnectionRequest.getConnection().getMeter().isEmpty()){
        			final ErrorField errorField = ErrorField.builder().code(WcmsConnectionConstants.CONNECTION_METERED_INVALID_CODE)
        					.message(WcmsConnectionConstants.CONNECTION_METERED_INVALID_ERROR_MESSAGE)
        					.field(WcmsConnectionConstants.CONNECTION_METERED_INVALID_FIELD_NAME).build();
        			errorFields.add(errorField);
        		}else if(waterConnectionRequest.getConnection().getMeter().get(0).getMeterOwner() == null || waterConnectionRequest.getConnection().getMeter().get(0).getMeterOwner().isEmpty()){
        			final ErrorField errorField = ErrorField.builder().code(WcmsConnectionConstants.CONNECTION_METERED_OWNER_INVALID_CODE)
        					.message(WcmsConnectionConstants.CONNECTION_METERED_OWNER_INVALID_ERROR_MESSAGE)
        					.field(WcmsConnectionConstants.CONNECTION_METERED_OWNER_INVALID_FIELD_NAME).build();
        			errorFields.add(errorField);
        		}/*else if(waterConnectionRequest.getConnection().getMeter().get(0).getMeterModel() == null || waterConnectionRequest.getConnection().getMeter().get(0).getMeterModel().isEmpty()){
        			final ErrorField errorField = ErrorField.builder().code(WcmsConnectionConstants.CONNECTION_METERED_MODEL_INVALID_CODE)
        					.message(WcmsConnectionConstants.CONNECTION_METERED_MODEL_INVALID_ERROR_MESSAGE)
        					.field(WcmsConnectionConstants.CONNECTION_METERED_MODEL_INVALID_FIELD_NAME).build();
        			errorFields.add(errorField);
        		}else if (waterConnectionRequest.getConnection().getMeter().get(0).getMeterSlNo() == null || waterConnectionRequest.getConnection().getMeter().get(0).getMeterSlNo().isEmpty()){
        			final ErrorField errorField = ErrorField.builder().code(WcmsConnectionConstants.CONNECTION_METERED_NUMBER_INVALID_CODE)
        					.message(WcmsConnectionConstants.CONNECTION_METERED_NUMBER_INVALID_ERROR_MESSAGE)
        					.field(WcmsConnectionConstants.CONNECTION_METERED_NUMBER_INVALID_FIELD_NAME).build();
        			errorFields.add(errorField);
        		}*/else if (waterConnectionRequest.getConnection().getMeter().get(0).getMaximumMeterReading() == null || waterConnectionRequest.getConnection().getMeter().get(0).getMaximumMeterReading().isEmpty()){
        			final ErrorField errorField = ErrorField.builder().code(WcmsConnectionConstants.CONNECTION_METERED_MAXMETERREADING_INVALID_CODE)
        					.message(WcmsConnectionConstants.CONNECTION_METERED_MAXMETERREADING_INVALID_ERROR_MESSAGE)
        					.field(WcmsConnectionConstants.CONNECTION_METERED_MAXMETERREADING_INVALID_FIELD_NAME).build();
        			errorFields.add(errorField);
        		}else if (waterConnectionRequest.getConnection().getMeter().get(0).getMeterReadings() == null || waterConnectionRequest.getConnection().getMeter().get(0).getMeterReadings().isEmpty()){
        			final ErrorField errorField = ErrorField.builder().code(WcmsConnectionConstants.CONNECTION_METERED_METERREADINGDETAILS_INVALID_CODE)
        					.message(WcmsConnectionConstants.CONNECTION_METERED_METERREADINGDETAILS_INVALID_ERROR_MESSAGE)
        					.field(WcmsConnectionConstants.CONNECTION_METERED_METERREADINGDETAILS_INVALID_FIELD_NAME).build();
        			errorFields.add(errorField);
        		}else {
        			LOGGER.info("Validating Connection MeterReadings details");
        			List<MeterReading> meterReadingList = waterConnectionRequest.getConnection().getMeter().get(0).getMeterReadings();
        			for (MeterReading meterReading : meterReadingList){
        				if(meterReading.getReading() <= 0){
        					final ErrorField errorField = ErrorField.builder().code(WcmsConnectionConstants.CONNECTION_METERED_METERREADING_INVALID_CODE)
        							.message(WcmsConnectionConstants.CONNECTION_METERED_METERREADING_INVALID_ERROR_MESSAGE)
        							.field(WcmsConnectionConstants.CONNECTION_METERED_METERREADING_INVALID_FIELD_NAME).build();
        					errorFields.add(errorField);
        				}else if(meterReading.getReadingDate() <= 0){
        					final ErrorField errorField = ErrorField.builder().code(WcmsConnectionConstants.CONNECTION_METERED_METERREADINGDATE_INVALID_CODE)
        							.message(WcmsConnectionConstants.CONNECTION_METERED_METERREADINGDATE_INVALID_ERROR_MESSAGE)
        							.field(WcmsConnectionConstants.CONNECTION_METERED_METERREADINGDATE_INVALID_FIELD_NAME).build();
        					errorFields.add(errorField);
        				};
        				break;
        			}

        		}
        	}
        	if (waterConnectionRequest.getConnection().getExecutionDate() == null) {
        		final ErrorField errorField = ErrorField.builder().code(WcmsConnectionConstants.LEGACY_EXECUTIONDATE_INVALID_CODE)
        				.message(WcmsConnectionConstants.LEGACY_EXECUTIONDATE_INVALID_ERROR_MESSAGE)
        				.field(WcmsConnectionConstants.LEGACY_EXECUTIONDATE_INVALID_FIELD_NAME).build();
        		errorFields.add(errorField);
        	}

        }
        /*else if (waterConnectionRequest.getConnection().getProperty()!=null && 
                waterConnectionRequest.getConnection().getProperty().getPropertyidentifier()==null && 
                        ("").equals(waterConnectionRequest.getConnection().getProperty().getPropertyidentifier())
                ) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConnectionConstants.PROPERTYIDENTIFIER_MANDATORY_CODE)
                    .message(WcmsConnectionConstants.PROPERTYIDENTIFIER_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConnectionConstants.PROPERTYIDENTIFIER_MANADATORY_FIELD_NAME).build();
            errorFields.add(errorField);
        } */
		if (!waterConnectionRequest.getConnection().getIsLegacy()) {
			if (waterConnectionRequest.getConnection().getDocuments() == null
					|| waterConnectionRequest.getConnection().getDocuments().isEmpty()) {
				final ErrorField errorField = ErrorField.builder().code(WcmsConnectionConstants.DOCUMENTS_INVALID_CODE)
						.message(WcmsConnectionConstants.DOCUMENTS_INVALID_ERROR_MESSAGE)
						.field(WcmsConnectionConstants.DOCUMENTS_INVALID_FIELD_NAME).build();
				errorFields.add(errorField);

			} else {
				for (final DocumentOwner document : waterConnectionRequest.getConnection().getDocuments())
					if (null == document.getDocument()) {
						final ErrorField errorField = ErrorField.builder()
								.code(WcmsConnectionConstants.DOCUMENTS_INVALID_CODE)
								.message(WcmsConnectionConstants.DOCUMENTS_INVALID_ERROR_MESSAGE)
								.field(WcmsConnectionConstants.DOCUMENTS_INVALID_FIELD_NAME).build();
						errorFields.add(errorField);
					} 
			}
		}
		
		/*if(null != waterConnectionRequest.getConnection().getWithProperty()
				&& !waterConnectionRequest.getConnection().getWithProperty()) { 
			validateConnectionLocationDetails(waterConnectionRequest,errorFields);
		}*/

        if (errorFields.size() > 0)
            return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                    .message(WcmsConnectionConstants.INVALID_REQUEST_MESSAGE)
                    .errorFields(errorFields).build();

        final List<ErrorField> masterfielderrorList = getMasterValidation(waterConnectionRequest);
        errorFields.addAll(masterfielderrorList);

       final List<ErrorField> errorFieldList = validateNewConnectionBusinessRules(waterConnectionRequest);
        errorFields.addAll(errorFieldList);

        return Error.builder().code(HttpStatus.BAD_REQUEST.value()).message(WcmsConnectionConstants.INVALID_REQUEST_MESSAGE)
                .errorFields(errorFields).build();
    }
    
    private void validateConnectionLocationDetails(WaterConnectionReq waterConnectionReq, List<ErrorField> errorFields) { 
    	if (!waterConnectionService.getBoundaryByZone(waterConnectionReq)) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConnectionConstants.BOUNDARY_ZONE_INVALID_CODE)
                    .message(WcmsConnectionConstants.BOUNDARY_ZONE_INVALID_ERROR_MESSAGE)
                    .field(WcmsConnectionConstants.BOUNDARY_ZONE_INVALID_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (!waterConnectionService.getBoundaryByWard(waterConnectionReq)) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConnectionConstants.BOUNDARY_WARD_INVALID_CODE)
                    .message(WcmsConnectionConstants.BOUNDARY_WARD_INVALID_ERROR_MESSAGE)
                    .field(WcmsConnectionConstants.BOUNDARY_WARD_INVALID_FIELD_NAME).build();
            errorFields.add(errorField);
        } else if (!waterConnectionService.getBoundaryByLocation(waterConnectionReq)) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConnectionConstants.BOUNDARY_LOCATION_INVALID_CODE)
                    .message(WcmsConnectionConstants.BOUNDARY_LOCATION_INVALID_ERROR_MESSAGE)
                    .field(WcmsConnectionConstants.BOUNDARY_LOCATION_INVALID_FIELD_NAME).build();
            errorFields.add(errorField);
        }	
    }

    public List<ErrorField> validateNewConnectionBusinessRules(final WaterConnectionReq waterConnectionRequest) {
        boolean isRequestValid = false;
        final List<ErrorField> errorFields = new ArrayList<>();

		if (waterConnectionRequest.getConnection().getProperty() != null
				&& waterConnectionRequest.getConnection().getProperty().getPropertyidentifier() != null
				&& !waterConnectionRequest.getConnection().getProperty().getPropertyidentifier().equals("")) {
			PropertyResponse propResp = restConnectionService.getPropertyDetailsByUpicNo(waterConnectionRequest);
			if (propResp.getProperties() != null && propResp.getProperties().isEmpty()) {
				final ErrorField errorField = ErrorField.builder().code(WcmsConnectionConstants.PROPERTY_INVALID_CODE)
						.message(WcmsConnectionConstants.PROPERTY_INVALID_ERROR_MESSAGE)
						.field(WcmsConnectionConstants.PROPERTY_INVALID_FIELD_NAME).build();
				errorFields.add(errorField);
			}
		}
        isRequestValid = restConnectionService.validatePropertyCategoryMapping(waterConnectionRequest);
        if (!isRequestValid) {
            final ErrorField errorField = ErrorField.builder().code(WcmsConnectionConstants.PROPERTY_CATEGORY_INVALID_CODE)
                    .message(WcmsConnectionConstants.PROPERTY_CATEGORY_INVALID_ERROR_MESSAGE)
                    .field(WcmsConnectionConstants.PROPERTY_CATEGORY_INVALID_FIELD_NAME).build();
            errorFields.add(errorField);
        }
        isRequestValid = restConnectionService.validatePropertyUsageTypeMapping(waterConnectionRequest);

        if (!isRequestValid) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConnectionConstants.PROPERTY_USAGE_INVALID_CODE)
                    .message(WcmsConnectionConstants.PROPERTY_USAGE_INVALID_ERROR_MESSAGE)
                    .field(WcmsConnectionConstants.PROPERTY_USAGE_INVALID_FIELD_NAME).build();
            errorFields.add(errorField);
        }
        isRequestValid=restConnectionService.validateSubUsageType(waterConnectionRequest);
        if (!isRequestValid) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConnectionConstants.SUBUSAGETYPE_INVALID_CODE)
                    .message(WcmsConnectionConstants.SUBUSAGETYPE_INVALID_ERROR_MESSAGE)
                    .field(WcmsConnectionConstants.SUBUSAGETYPE_INVALID_FIELD_NAME).build();
            errorFields.add(errorField);
        }

        /*
         * if (waterConnectionRequest.getConnection().getLegacyConsumerNumber() == null) { isRequestValid =
         * validateDocumentApplicationType(waterConnectionRequest); if (!isRequestValid) { final ErrorField errorField =
         * ErrorField.builder() .code(WcmsTranasanctionConstants.DOCUMENT_APPLICATION_INVALID_CODE)
         * .message(WcmsTranasanctionConstants.DOCUMENT_APPLICATION_INVALID_ERROR_MESSAGE)
         * .field(WcmsTranasanctionConstants.DOCUMENT_APPLICATION_INVALID_FIELD_NAME) .build(); errorFields.add(errorField); } }
         */
        isRequestValid = validateStaticFields(waterConnectionRequest);
        if (!isRequestValid) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConnectionConstants.STATIC_INVALID_CODE)
                    .message(WcmsConnectionConstants.STATIC_INVALID_ERROR_MESSAGE)
                    .field(WcmsConnectionConstants.STATIC_INVALID_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        }

        if (waterConnectionRequest.getConnection().getCategoryType().equals("BPL"))
            if (waterConnectionRequest.getConnection().getBplCardHolderName() == null ||
                    waterConnectionRequest.getConnection().getBplCardHolderName().isEmpty()) {
                final ErrorField errorField = ErrorField.builder()
                        .code(WcmsConnectionConstants.BPL_INVALID_CODE)
                        .message(WcmsConnectionConstants.BPL_INVALID_ERROR_MESSAGE)
                        .field(WcmsConnectionConstants.BPL_INVALID_FIELD_NAME)
                        .build();
                errorFields.add(errorField);
            }
        if(!waterConnectionRequest.getConnection().getIsLegacy()){
        final DonationResponseInfo donationresInfo = restConnectionService.validateDonationAmount(waterConnectionRequest);
        if (donationresInfo == null) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConnectionConstants.DONATION_INVALID_CODE)
                    .message(WcmsConnectionConstants.DONATION_INVALID_ERROR_MESSAGE)
                    .field(WcmsConnectionConstants.DONATION_INVALID_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        }
        }

        return errorFields;
    }


    /*
     * private boolean validateDocumentApplicationType(final WaterConnectionReq waterConnectionRequest) { LOGGER.info(
     * "Validating Document - Application Mapping"); boolean isDocumentValid = true; int countOfDocs = 0; final List<Long>
     * mandatoryDocs = documentTypeService.getAllMandatoryDocs(ApplicationType.NEWCONNECTION.toString()); if
     * (waterConnectionRequest.getConnection().getLegacyConsumerNumber() == null) for (final DocumentOwner documentOwner :
     * waterConnectionRequest.getConnection().getDocuments()) if (mandatoryDocs.contains(documentOwner.getDocument().getTypeId()))
     * { countOfDocs++; if (documentOwner.getFileStoreId() == null || documentOwner.getFileStoreId().isEmpty()) { LOGGER.info(
     * "File Upload FAILED for the document: " + documentOwner.toString()); isDocumentValid = false; return isDocumentValid; } }
     * if (countOfDocs != mandatoryDocs.size()) isDocumentValid = false; return isDocumentValid; }
     */

    private boolean validateStaticFields(final WaterConnectionReq waterConnectionRequest) {
        LOGGER.info("Validating ConnectionType, BillingType, SupplyType, SourceType");

        boolean isRequestValid = false;

        if (!(waterConnectionRequest.getConnection().getConnectionType().equals("TEMPORARY") ||
                waterConnectionRequest.getConnection().getConnectionType().equals("PERMANENT"))) {
            LOGGER.info("ConnectionType is INVALID");
            return isRequestValid;
        } else if (!(waterConnectionRequest.getConnection().getBillingType().equals("METERED") ||
                waterConnectionRequest.getConnection().getBillingType().equals("NONMETERED"))) {
            LOGGER.info("BillingType is INVALID");
            return isRequestValid;
        }

        isRequestValid = true;
        return isRequestValid;

    }

    public List<ErrorField> getMasterValidation(final WaterConnectionReq waterConnectionRequest) {
        final List<ErrorField> errorFields = new ArrayList<>();

        if (restConnectionService.getCategoryTypeByName(waterConnectionRequest).getCategory().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConnectionConstants.CATEGORY_INVALID_CODE)
                    .message(WcmsConnectionConstants.CATEGORY_INVALID_FIELD_NAME)
                    .field(WcmsConnectionConstants.CATEGORY_INVALID_ERROR_MESSAGE)
                    .build();
            errorFields.add(errorField);
        }
        PipeSizeResponseInfo pipeinfo=restConnectionService.getPipesizeTypeByCode(waterConnectionRequest);
        if (pipeinfo.getPipeSize()!=null && pipeinfo.getPipeSize().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConnectionConstants.PIPESIZE_INVALID_CODE)
                    .message(WcmsConnectionConstants.PIPESIZE_INVALID_FIELD_NAME)
                    .field(WcmsConnectionConstants.PIPESIZE_INVALID_ERROR_MESSAGE)
                    .build();
            errorFields.add(errorField);
        }
        if (restConnectionService.getSourceTypeByName(waterConnectionRequest).getWaterSourceType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConnectionConstants.SOURCETYPE_INVALID_CODE)
                    .message(WcmsConnectionConstants.SOURCETYPE_INVALID_FIELD_NAME)
                    .field(WcmsConnectionConstants.SOURCETYPE_INVALID_ERROR_MESSAGE)
                    .build();
            errorFields.add(errorField);
        }
        if (restConnectionService.getTreateMentPlantName(waterConnectionRequest).getTreatmentPlants().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConnectionConstants.TREATPLANT_INVALID_CODE)
                    .message(WcmsConnectionConstants.TREATPLANT_INVALID_FIELD_NAME)
                    .field(WcmsConnectionConstants.TREATPLANT_INVALID_ERROR_MESSAGE)
                    .build();
            errorFields.add(errorField);
        }
       
        if (restConnectionService.getSupplyTypeByName(waterConnectionRequest) .getSupplytypes().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConnectionConstants.SUPPLYTYPE_INVALID_CODE)
                    .message(WcmsConnectionConstants.SUPPLYTYPE_INVALID_FIELD_NAME)
                    .field(WcmsConnectionConstants.SUPPLYTYPE_INVALID_ERROR_MESSAGE)
                    .build();
            errorFields.add(errorField);
        }
        
        return errorFields;
    }

	public String generateAcknowledgementNumber(final WaterConnectionReq waterConnectionRequest) {
		return restConnectionService.generateRequestedDocumentNumber(
				waterConnectionRequest.getConnection().getTenantId(), configurationManager.getIdGenNameServiceTopic(),
				configurationManager.getIdGenFormatServiceTopic(), waterConnectionRequest.getRequestInfo());
	}

	public String generateConsumerNumber(final WaterConnectionReq waterConnectionRequest) {
		Long nextConsumerNumber = waterConnectionService.generateNextConsumerNumber(); 
		Integer format = configurationManager.getHscNumberOfChar();
		String ulbName = restConnectionService.getULBNameFromTenant(waterConnectionRequest.getConnection().getTenantId(), waterConnectionRequest.getRequestInfo());
		String completeConsumerNumber = ulbName.concat(StringUtils.right(consumerNumberPrefix + String.valueOf(nextConsumerNumber), format));
		return completeConsumerNumber; 
	}
	
}

