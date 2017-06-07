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
package org.egov.wcms.validator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.egov.common.contract.response.ErrorField;
import org.egov.wcms.model.DocumentOwner;
import org.egov.wcms.model.Donation;
import org.egov.wcms.model.Property;
import org.egov.wcms.model.PropertyTypeUsageType;
import org.egov.wcms.repository.DonationRepository;
import org.egov.wcms.service.DocumentTypeApplicationTypeService;
import org.egov.wcms.service.DonationService;
import org.egov.wcms.service.PropertyCategoryService;
import org.egov.wcms.service.PropertyTypePipeSizeTypeService;
import org.egov.wcms.service.PropertyUsageTypeService;
import org.egov.wcms.util.WcmsConstants;
import org.egov.wcms.web.contract.DonationGetRequest;
import org.egov.wcms.web.contract.PropertyTypeUsageTypeReq;
import org.egov.wcms.web.contract.WaterConnectionReq;
import org.egov.wcms.web.errorhandlers.Error;
import org.egov.wcms.web.errorhandlers.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class NewWaterConnectionValidator {
	
	@Autowired
	private DonationService donationService; 
	
	@Autowired
	private PropertyUsageTypeService propertyUsageTypeService;
	
	@Autowired
	private PropertyCategoryService propertyCategoryService;
	
	@Autowired
	private DocumentTypeApplicationTypeService documentTypeApplicationTypeService;
	
	@Autowired
	private PropertyTypePipeSizeTypeService propertyPipeSizeService;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(DonationRepository.class);
	
	
	
	public ErrorResponse populateErrors(final BindingResult errors) {
        final ErrorResponse errRes = new ErrorResponse();
        final Error error = new Error();
        error.setCode(1);
        error.setDescription("Error while binding request");
        if (errors.hasFieldErrors())
            for (final FieldError fieldError : errors.getFieldErrors()) {
                error.getFields().put(fieldError.getField(), fieldError.getRejectedValue());
            }
        errRes.setError(error);
        return errRes;
    }
    
    public List<ErrorResponse> validateWaterConnectionRequest(final WaterConnectionReq waterConnectionRequest) {
    	// As we are creating a stub of Property Tax Module, we are creating objects and 
    	// filling data manually
    	// These lines of code can be removed once PTax Module gets integrated
    	waterConnectionRequest.getConnection().getProperty().setPropertyTypeId(1L);
    	waterConnectionRequest.getConnection().getProperty().setUsageTypeId(1L);

    	final List<ErrorResponse> errorResponses = new ArrayList<>();
        ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(waterConnectionRequest);
        errorResponse.setError(error);
        if(!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);
        
        return errorResponses;
    }
    
    public Error getError(final WaterConnectionReq waterConnectionRequest) {
        List<ErrorField> errorFields = new ArrayList<>();
        if (waterConnectionRequest.getConnection().getBillingType() == null || 
        		waterConnectionRequest.getConnection().getBillingType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.BILLING_TYPE_INVALID_CODE)
                    .message(WcmsConstants.BILLING_TYPE_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.BILLING_TYPE_INVALID_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (waterConnectionRequest.getConnection().getCategoryType() == null || 
        		waterConnectionRequest.getConnection().getCategoryType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.CATEGORY_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.CATEGORY_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.CATEGORY_NAME_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (waterConnectionRequest.getConnection().getConnectionType() == null || 
        		waterConnectionRequest.getConnection().getConnectionType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.CONNECTION_TYPE_INVALID_CODE)
                    .message(WcmsConstants.CONNECTION_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.CONNECTION_TYPE_INVALID_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (waterConnectionRequest.getConnection().getDocuments() == null || 
        		waterConnectionRequest.getConnection().getDocuments().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.DOCUMENTS_INVALID_CODE)
                    .message(WcmsConstants.DOCUMENTS_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.DOCUMENTS_INVALID_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (waterConnectionRequest.getConnection().getHscPipeSizeType() == 0L) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.PIPESIZE_SIZEINMM_MANDATORY_CODE)
                    .message(WcmsConstants.PIPESIZE_SIZEINMM__MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.PIPESIZE_SIZEINMM__MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (waterConnectionRequest.getConnection().getProperty() == null || 
        		waterConnectionRequest.getConnection().getProperty().getPropertyType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.PROPERTY_TYPE_MANDATORY_CODE)
                    .message(WcmsConstants.PROPERTY_TYPE_MANDATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.PROPERTY_TYPE_MANDATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (waterConnectionRequest.getConnection().getProperty() == null || 
        		waterConnectionRequest.getConnection().getProperty().getUsageType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.USAGETYPE_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.USAGETYPE_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.USAGETYPE_NAME_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (waterConnectionRequest.getConnection().getSourceType() == null || 
        		waterConnectionRequest.getConnection().getSourceType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.SOURCE_TYPE_INVALID_CODE)
                    .message(WcmsConstants.SOURCE_TYPE_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.SOURCE_TYPE_INVALID_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (waterConnectionRequest.getConnection().getSumpCapacity() == 0L) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.SUMP_CAPACITY_INVALID_CODE)
                    .message(WcmsConstants.SUMP_CAPACITY_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.SUMP_CAPACITY_INVALID_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        } else if (waterConnectionRequest.getConnection().getSupplyType() == null || 
        		waterConnectionRequest.getConnection().getSupplyType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.SUPPLY_TYPE_INVALID_CODE)
                    .message(WcmsConstants.SUPPLY_TYPE_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.SUPPLY_TYPE_INVALID_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        }
       
        List<ErrorField> errorFieldList = validateNewConnectionBusinessRules(waterConnectionRequest);
        errorFields.addAll(errorFieldList);

        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_REQUEST_MESSAGE)
                .errorFields(errorFields)
                .build();
    }
	
	
	public List<ErrorField> validateNewConnectionBusinessRules(WaterConnectionReq waterConnectionRequest){
		boolean isRequestValid = false;
        List<ErrorField> errorFields = new ArrayList<>();

		isRequestValid = validatePropertyUsageMapping(waterConnectionRequest);
		if(!isRequestValid){			
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.PROPERTY_USAGE_INVALID_CODE)
                    .message(WcmsConstants.PROPERTY_USAGE_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.PROPERTY_USAGE_INVALID_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
		}
		
		isRequestValid = validatePropertyCategoryMapping(waterConnectionRequest);
		if(!isRequestValid){
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.PROPERTY_CATEGORY_INVALID_CODE)
                    .message(WcmsConstants.PROPERTY_CATEGORY_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.PROPERTY_CATEGORY_INVALID_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
		}
		
		isRequestValid = validateDocumentApplicationType(waterConnectionRequest);
		if(!isRequestValid){
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.DOCUMENT_APPLICATION_INVALID_CODE)
                    .message(WcmsConstants.DOCUMENT_APPLICATION_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.DOCUMENT_APPLICATION_INVALID_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
		}
		
		isRequestValid = validateStaticFields(waterConnectionRequest);
		if(!isRequestValid){
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.STATIC_INVALID_CODE)
                    .message(WcmsConstants.STATIC_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.STATIC_INVALID_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
		}
		
		if(waterConnectionRequest.getConnection().getCategoryType().equals("BPL")){
			if(waterConnectionRequest.getConnection().getBplCardHolderName() == null ||
					waterConnectionRequest.getConnection().getBplCardHolderName().isEmpty()){
	            final ErrorField errorField = ErrorField.builder()
	                    .code(WcmsConstants.BPL_INVALID_CODE)
	                    .message(WcmsConstants.BPL_INVALID_ERROR_MESSAGE)
	                    .field(WcmsConstants.BPL_INVALID_FIELD_NAME)
	                    .build();
	            errorFields.add(errorField);
			}
		}
	
		isRequestValid =  validateDonationAmount(waterConnectionRequest);
		if(!isRequestValid){
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.DONATION_INVALID_CODE)
                    .message(WcmsConstants.DONATION_INVALID_ERROR_MESSAGE)
                    .field(WcmsConstants.DONATION_INVALID_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
		}
		
		
		return errorFields;
	}
	
	@SuppressWarnings("rawtypes")
	private boolean validateDonationAmount(WaterConnectionReq waterConnectionRequest){
		List<Donation> donationList = donationService.getDonationList(prepareDonationGetRequest(waterConnectionRequest));
		Iterator itr = donationList.iterator();
		while(itr.hasNext()){
			Donation donation = (Donation) itr.next();
			if(null == donation.getDonationAmount() || donation.getDonationAmount().isEmpty()){
				return false;
			}
		}
		return true;
		
	}
	
	private boolean validatePropertyUsageMapping(WaterConnectionReq waterConnectionRequest){
		LOGGER.info("Validating Property - Usage Mapping");
		boolean result = false;

		PropertyTypeUsageTypeReq propUsageTypeRequest = new PropertyTypeUsageTypeReq();
		PropertyTypeUsageType propertyTypeUsageType = new PropertyTypeUsageType();
		
		propertyTypeUsageType.setPropertyType(waterConnectionRequest.getConnection().getProperty().getPropertyType());
		propertyTypeUsageType.setUsageType(waterConnectionRequest.getConnection().getProperty().getUsageType());
		propertyTypeUsageType.setTenantId(waterConnectionRequest.getConnection().getTenantId());
		
		propUsageTypeRequest.setPropertyTypeUsageType(propertyTypeUsageType);
		
		try{
			result = propertyUsageTypeService.checkPropertyUsageTypeExists(propUsageTypeRequest);
		}catch(Exception e){
			LOGGER.info("Validating Property - Usage Mapping FAILED!");
		}
		if(!result){
			LOGGER.info("Validating Property - Usage Mapping FAILED!");
		}
		
		return result;
		
	}
	
	private boolean validatePropertyCategoryMapping(WaterConnectionReq waterConnectionRequest){
		LOGGER.info("Validating Property - Category Mapping");
		boolean result = false;
		try{
			result =  propertyCategoryService.checkIfMappingExists(waterConnectionRequest.getConnection().getProperty().getPropertyType(),
				waterConnectionRequest.getConnection().getCategoryType(), waterConnectionRequest.getConnection().getTenantId());
		}catch(Exception e){
			LOGGER.info("Validating Property - Category Mapping FAILED!");
		}
		return result;
		
	}
	
	private boolean validateDocumentApplicationType(WaterConnectionReq waterConnectionRequest){
		LOGGER.info("Validating Document - Application Mapping");
		
		boolean isDocumentValid = true;
		for(DocumentOwner documentOwner: waterConnectionRequest.getConnection().getDocuments()){
			if(documentOwner.getFileStoreId() == null || documentOwner.getFileStoreId().isEmpty()){
				LOGGER.info("File Upload FAILED for the document: "+documentOwner.toString());
				isDocumentValid = false; //This flow should get activated only when the document is mandatory, revisit the logic.
				return isDocumentValid;
			}			
		}
		return isDocumentValid;
	}
	
	private boolean validateStaticFields(WaterConnectionReq waterConnectionRequest){
		LOGGER.info("Validating ConnectionType, BillingType, SupplyType, SourceType");

		boolean isRequestValid = false;
		
		if(!(waterConnectionRequest.getConnection().getConnectionType().equals("TEMPORARY") || 
				waterConnectionRequest.getConnection().getConnectionType().equals("PERMANENT"))){
			LOGGER.info("ConnectionType is INVALID");
			return isRequestValid;
		}else if(!(waterConnectionRequest.getConnection().getBillingType().equals("METERED") || 
				waterConnectionRequest.getConnection().getBillingType().equals("NON-METERED"))){
			LOGGER.info("BillingType is INVALID");
			return isRequestValid;
		}else if(!(waterConnectionRequest.getConnection().getSupplyType().equals("REGULAR") || 
				waterConnectionRequest.getConnection().getSupplyType().equals("BULK") || 
				waterConnectionRequest.getConnection().getSupplyType().equals("SEMIBULK"))){
			LOGGER.info("SupplyType is INVALID");
			return isRequestValid;
		}else if(!(waterConnectionRequest.getConnection().getSourceType().equals("GROUNDWATER") || 
				waterConnectionRequest.getConnection().getSourceType().equals("SURFACEWATER"))){
			LOGGER.info("SourceType is INVALID");
			return isRequestValid;
		}
		
		isRequestValid = true;
		return isRequestValid;
		
		//Refactoring needed to reduce the if-else ladder and other optimization.
	}
	
		
	
	private DonationGetRequest prepareDonationGetRequest(WaterConnectionReq waterConnectionRequest){
		// Receive new connection request as a parameter for this method
		// Then using the values in the New Connection Request, prepare a Donation Get Request Object
		// Pass this Object to Get Method of Donation Service
		DonationGetRequest donationGetRequest = new DonationGetRequest();
		donationGetRequest.setPropertyType(waterConnectionRequest.getConnection().getProperty().getPropertyType());
		donationGetRequest.setUsageType(waterConnectionRequest.getConnection().getProperty().getUsageType());
		donationGetRequest.setCategoryType(waterConnectionRequest.getConnection().getCategoryType());
		donationGetRequest.setMaxHSCPipeSize(waterConnectionRequest.getConnection().getHscPipeSizeType());
		donationGetRequest.setMinHSCPipeSize(waterConnectionRequest.getConnection().getHscPipeSizeType());
		donationGetRequest.setTenantId(waterConnectionRequest.getConnection().getTenantId());
		return donationGetRequest; 
	}
	
	
	
}
