package org.egov.collection.util;

import java.util.ArrayList;
import java.util.List;

import org.egov.collection.config.CollectionServiceConstants;
import org.egov.collection.web.contract.BillAccountDetails;
import org.egov.collection.web.contract.BillDetails;
import org.egov.collection.web.contract.Receipt;
import org.egov.collection.web.contract.ReceiptReq;
import org.egov.collection.web.errorhandlers.ErrorResponse;
import org.egov.common.contract.response.ErrorField;
import org.egov.collection.web.errorhandlers.Error;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ReceiptReqValidator {
	
	public List<ErrorResponse> validateServiceGroupRequest(final ReceiptReq receiptRequest) {
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
		final Receipt receipt = receiptRequest.getReceipt();
		
		if(null == receipt.getTenantId() || receipt.getTenantId().isEmpty()){
			final ErrorField errorField = ErrorField.builder().code(CollectionServiceConstants.TENANT_ID_MISSING_CODE)
					.message(CollectionServiceConstants.TENANT_ID_MISSING_MESSAGE)
					.field(CollectionServiceConstants.TENANT_ID_MISSING_FIELD).build();
			errorFields.add(errorField);
		}
		
		if(null == receipt.getBillInfo().getPayeeName() || receipt.getBillInfo().getPayeeName().isEmpty()){
			final ErrorField errorField = ErrorField.builder().code(CollectionServiceConstants.PAYEE_NAME_MISSING_CODE)
					.message(CollectionServiceConstants.PAYEE_NAME_MISSING_MESSAGE)
					.field(CollectionServiceConstants.PAYEE_NAME_MISSING_FIELD).build();
			errorFields.add(errorField);
		}
		
		if(null == receipt.getBillInfo().getPaidBy() || receipt.getBillInfo().getPaidBy().isEmpty()){
			final ErrorField errorField = ErrorField.builder().code(CollectionServiceConstants.PAID_BY_MISSING_CODE)
					.message(CollectionServiceConstants.PAID_BY_MISSING_MESSAGE)
					.field(CollectionServiceConstants.PAID_BY_MISSING_FIELD).build();
			errorFields.add(errorField);
		}
		if(null == receipt.getBankAccount().getChartOfAccount().getName() || receipt.getBankAccount().getChartOfAccount().getName().isEmpty()){
			final ErrorField errorField = ErrorField.builder().code(CollectionServiceConstants.COA_MISSING_CODE)
					.message(CollectionServiceConstants.COA_MISSING_MESSAGE)
					.field(CollectionServiceConstants.COA_MISSING_FIELD).build();
			errorFields.add(errorField);
		}
		
		for(BillDetails billDetails:  receipt.getBillInfo().getBillDetails()){
			if(null == billDetails.getReceiptType()|| billDetails.getReceiptType().isEmpty()){
				final ErrorField errorField = ErrorField.builder().code(CollectionServiceConstants.RECEIPT_TYPE_MISSING_CODE)
						.message(CollectionServiceConstants.RECEIPT_TYPE_MISSING_MESSAGE)
						.field(CollectionServiceConstants.RECEIPT_TYPE_MISSING_FIELD).build();
				errorFields.add(errorField);
			}
			
			if(null == billDetails.getReceiptDate()){
				final ErrorField errorField = ErrorField.builder().code(CollectionServiceConstants.RECEIPT_DATE_MISSING_CODE)
						.message(CollectionServiceConstants.RECEIPT_DATE_MISSING_MESSAGE)
						.field(CollectionServiceConstants.RECEIPT_DATE_MISSING_FIELD).build();
				errorFields.add(errorField);
			}
			
			if(null == billDetails.getBusinessDetailsCode() || 0L == billDetails.getBusinessDetailsCode()){
				final ErrorField errorField = ErrorField.builder().code(CollectionServiceConstants.BD_CODE_MISSING_CODE)
						.message(CollectionServiceConstants.BD_CODE_MISSING_MESSAGE)
						.field(CollectionServiceConstants.BD_CODE_MISSING_FIELD).build();
				errorFields.add(errorField);
			}	
			
			for(BillAccountDetails billAccountDetails: billDetails.getBillAccountDetails()){
				
				if(null == billAccountDetails.getPurpose() || billAccountDetails.getPurpose().isEmpty()){
					final ErrorField errorField = ErrorField.builder().code(CollectionServiceConstants.PURPOSE_MISSING_CODE)
							.message(CollectionServiceConstants.PURPOSE_MISSING_MESSAGE)
							.field(CollectionServiceConstants.PURPOSE_MISSING_FIELD).build();
					errorFields.add(errorField);
				}
			}
		}
	}
}
