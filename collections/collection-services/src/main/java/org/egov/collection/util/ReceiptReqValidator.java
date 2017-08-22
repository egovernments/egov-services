package org.egov.collection.util;

import org.apache.commons.lang3.StringUtils;
import org.egov.collection.config.CollectionServiceConstants;
import org.egov.collection.service.CollectionConfigService;
import org.egov.collection.web.contract.*;
import org.egov.collection.web.errorhandlers.Error;
import org.egov.collection.web.errorhandlers.ErrorResponse;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.response.ErrorField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ReceiptReqValidator {


    @Autowired
    private CollectionConfigService collectionConfigService;
	
	public List<ErrorResponse> validatecreateReceiptRequest(final ReceiptReq receiptRequest) {
		final List<ErrorResponse> errorResponses = new ArrayList<>();
		final ErrorResponse errorResponse = new ErrorResponse();
		final Error error = getError(receiptRequest);
		errorResponse.setError(error);
		if (!errorResponse.getErrorFields().isEmpty())
			errorResponses.add(errorResponse);
		return errorResponses;
	}

	private Error getError(final ReceiptReq receiptRequest) {
		final List<ErrorField> errorFields = getErrorFields(receiptRequest);
		return Error.builder().code(HttpStatus.BAD_REQUEST.value())
				.message(CollectionServiceConstants.INVALID_RECEIPT_REQUEST).errorFields(errorFields).build();
	}

	private List<ErrorField> getErrorFields(final ReceiptReq receiptRequest) {
		final List<ErrorField> errorFields = new ArrayList<>();
		addServiceIdValidationErrors(receiptRequest, errorFields);
		return errorFields;
	}

	private void addServiceIdValidationErrors(final ReceiptReq receiptRequest,
			final List<ErrorField> errorFields) {
        RequestInfo requestInfo = receiptRequest.getRequestInfo();
	try{	
		final List<Receipt> receipts = receiptRequest.getReceipt();
		for(Receipt receipt:receipts){
		
		if(null == receipt.getTenantId() || receipt.getTenantId().isEmpty()){
			final ErrorField errorField = ErrorField.builder().code(CollectionServiceConstants.TENANT_ID_MISSING_CODE)
					.message(CollectionServiceConstants.TENANT_ID_MISSING_MESSAGE)
					.field(CollectionServiceConstants.TENANT_ID_MISSING_FIELD).build();
			errorFields.add(errorField);
		}
		
		/*TODO: commenting because payee name is coming as null from billing service if(null == receipt.getBill().get(0).getPayeeName() || receipt.getBill().get(0).getPayeeName().isEmpty()){
			final ErrorField errorField = ErrorField.builder().code(CollectionServiceConstants.PAYEE_NAME_MISSING_CODE)
					.message(CollectionServiceConstants.PAYEE_NAME_MISSING_MESSAGE)
					.field(CollectionServiceConstants.PAYEE_NAME_MISSING_FIELD).build();
			errorFields.add(errorField);
		}*/
		
		if(null == receipt.getBill().get(0).getPaidBy() || receipt.getBill().get(0).getPaidBy().isEmpty()){
			final ErrorField errorField = ErrorField.builder().code(CollectionServiceConstants.PAID_BY_MISSING_CODE)
					.message(CollectionServiceConstants.PAID_BY_MISSING_MESSAGE)
					.field(CollectionServiceConstants.PAID_BY_MISSING_FIELD).build();
			errorFields.add(errorField);
		}
		
		for(BillDetail billDetails:  receipt.getBill().get(0).getBillDetails()){
			List<BillAccountDetail> billAccountDetails = new ArrayList<BillAccountDetail>();

            if(StringUtils.isBlank(billDetails.getBillDescription())) {
                final ErrorField errorField = ErrorField.builder().code(CollectionServiceConstants.COLL_DETAILS_DESCRIPTION_CODE)
                        .message(CollectionServiceConstants.COLL_DETAILS_DESCRIPTION_MESSAGE)
                        .field(CollectionServiceConstants.COLL_DETAILS_DESCRIPTION_FIELD).build();
                errorFields.add(errorField);
            }
            
            if(null == billDetails.getAmountPaid()) {
                final ErrorField errorField = ErrorField.builder().code(CollectionServiceConstants.AMT_PAID_NOT_NULL_CODE)
                        .message(CollectionServiceConstants.AMT_PAID_NOT_NULL_MESSAGE)
                        .field(CollectionServiceConstants.AMT_PAID_NOT_NULL_FIELD).build();
                errorFields.add(errorField);
            }
			
			if(null == billDetails.getBusinessService() || billDetails.getBusinessService().isEmpty()){
				final ErrorField errorField = ErrorField.builder().code(CollectionServiceConstants.BD_CODE_MISSING_CODE)
						.message(CollectionServiceConstants.BD_CODE_MISSING_MESSAGE)
						.field(CollectionServiceConstants.BD_CODE_MISSING_FIELD).build();
				errorFields.add(errorField);
			}
            CollectionConfigGetRequest collectionConfigGetRequest = new CollectionConfigGetRequest();
            collectionConfigGetRequest.setTenantId(receipt.getTenantId());
            collectionConfigGetRequest.setName(CollectionServiceConstants.MANUAL_RECEIPT_DETAILS_REQUIRED_CONFIG_KEY);

            Map<String, List<String>> manualReceiptRequiredConfiguration = collectionConfigService.getCollectionConfiguration(collectionConfigGetRequest);

            List<Role> roleList = requestInfo.getUserInfo().getRoles();
            if(!manualReceiptRequiredConfiguration.isEmpty() && manualReceiptRequiredConfiguration.get(CollectionServiceConstants.MANUAL_RECEIPT_DETAILS_REQUIRED_CONFIG_KEY).get(0).equalsIgnoreCase("Yes")
                    && roleList.stream().anyMatch(role -> CollectionServiceConstants.COLLECTION_LEGACY_RECEIPT_CREATOR_ROLE.contains(role.getName())) && StringUtils.isNotEmpty(billDetails.getManualReceiptNumber()) && billDetails.getReceiptDate() != null ) {
                CollectionConfigGetRequest collectionConfigRequest = new CollectionConfigGetRequest();
                collectionConfigRequest.setTenantId(receipt.getTenantId());
                collectionConfigRequest.setName(CollectionServiceConstants.MANUAL_RECEIPT_DETAILS_CUTOFF_DATE_CONFIG_KEY);
                Map<String, List<String>> manualReceiptCutOffDateConfiguration = collectionConfigService.getCollectionConfiguration(collectionConfigRequest);
                if(!manualReceiptCutOffDateConfiguration.isEmpty() && manualReceiptCutOffDateConfiguration.get(CollectionServiceConstants.MANUAL_RECEIPT_DETAILS_CUTOFF_DATE_CONFIG_KEY) != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Date cutOffDate = dateFormat.parse(manualReceiptCutOffDateConfiguration.get(CollectionServiceConstants.MANUAL_RECEIPT_DETAILS_CUTOFF_DATE_CONFIG_KEY).get(0));
                    Date manualReceiptDate = new Date(billDetails.getReceiptDate());
                    if(manualReceiptDate.after(cutOffDate)) {
                        final ErrorField errorField = ErrorField.builder().code(CollectionServiceConstants.CUTT_OFF_DATE_CODE)
                                .message(CollectionServiceConstants.CUTT_OFF_DATE_MESSAGE + cutOffDate + CollectionServiceConstants.CUTT_OFF_DATE_MESSAGE_DESC)
                                .field(CollectionServiceConstants.CUTT_OFF_DATE_FIELD).build();
                        errorFields.add(errorField);
                    }
                }

            }
			for(BillAccountDetail billAccountDetail: billAccountDetails){
				if(null == billAccountDetail.getPurpose()){
					final ErrorField errorField = ErrorField.builder().code(CollectionServiceConstants.PURPOSE_MISSING_CODE)
							.message(CollectionServiceConstants.PURPOSE_MISSING_MESSAGE)
							.field(CollectionServiceConstants.PURPOSE_MISSING_FIELD).build();
					errorFields.add(errorField);
				}
				
				if(null == billAccountDetail.getGlcode() || billAccountDetail.getGlcode().isEmpty()){
					final ErrorField errorField = ErrorField.builder().code(CollectionServiceConstants.COA_MISSING_CODE)
							.message(CollectionServiceConstants.COA_MISSING_MESSAGE)
							.field(CollectionServiceConstants.COA_MISSING_FIELD).build();
					errorFields.add(errorField);
				}
			}
			}
		}
	}catch(Exception e){
		e.printStackTrace();
		final ErrorField errorField = ErrorField.builder().code(HttpStatus.BAD_REQUEST.toString())
				.message(CollectionServiceConstants.INVALID_RECEIPT_REQUEST)
				.field(CollectionServiceConstants.INVALID_RECEIPT_REQUEST).build();
		errorFields.add(errorField);
	}
	}
	
	public void validateSearchReceiptRequest(final ReceiptSearchGetRequest receiptGetRequest){
		if(receiptGetRequest.getFromDate() > receiptGetRequest.getToDate()){
			throw new ValidationException(CollectionServiceConstants.INVALID_DATE_EXCEPTION_MSG);
		}
	}
}
