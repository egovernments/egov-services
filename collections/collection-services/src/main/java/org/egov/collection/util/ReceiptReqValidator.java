package org.egov.collection.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.egov.collection.config.CollectionServiceConstants;
import org.egov.collection.service.CollectionConfigService;
import org.egov.collection.web.contract.BillAccountDetail;
import org.egov.collection.web.contract.BillDetail;
import org.egov.collection.web.contract.CollectionConfigGetRequest;
import org.egov.collection.web.contract.Receipt;
import org.egov.collection.web.contract.ReceiptReq;
import org.egov.collection.web.contract.ReceiptSearchGetRequest;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ReceiptReqValidator {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(ReceiptReqValidator.class);

	@Autowired
	private CollectionConfigService collectionConfigService;

	public List<ErrorResponse> validatecreateReceiptRequest(
			final ReceiptReq receiptRequest) {
		final List<ErrorResponse> errorResponses = new ArrayList<>();
		final ErrorResponse errorResponse = new ErrorResponse();
		final Error error = getError(receiptRequest);
		errorResponse.setError(error);
		errorResponses.add(errorResponse);
		return errorResponses;
	}

	private Error getError(final ReceiptReq receiptRequest) {
		final List<ErrorField> errorFields = getErrorFields(receiptRequest);
		Error error = null;
		if (!errorFields.isEmpty())
			error = Error
					.builder()
					.code(HttpStatus.BAD_REQUEST.value())
					.message(CollectionServiceConstants.INVALID_RECEIPT_REQUEST)
					.fields(errorFields).build();
		return error;
	}

	private List<ErrorField> getErrorFields(final ReceiptReq receiptRequest) {
		final List<ErrorField> errorFields = new ArrayList<>();
		addServiceIdValidationErrors(receiptRequest, errorFields);
		return errorFields;
	}

	private void addServiceIdValidationErrors(final ReceiptReq receiptRequest,
			final List<ErrorField> errorFields) {
		RequestInfo requestInfo = receiptRequest.getRequestInfo();
		try {
			final List<Receipt> receipts = receiptRequest.getReceipt();
			for (Receipt receipt : receipts) {

				if (null == receipt.getTenantId()
						|| receipt.getTenantId().isEmpty()) {
					final ErrorField errorField = ErrorField
							.builder()
							.code(CollectionServiceConstants.TENANT_ID_MISSING_CODE)
							.message(
									CollectionServiceConstants.TENANT_ID_MISSING_MESSAGE)
							.field(CollectionServiceConstants.TENANT_ID_MISSING_FIELD)
							.build();
					errorFields.add(errorField);
				}

				ErrorField errorField;
				/*
				 * ErrorField .builder()
				 * .code(CollectionServiceConstants.PAYEE_NAME_MISSING_CODE)
				 * .message(
				 * CollectionServiceConstants.PAYEE_NAME_MISSING_MESSAGE)
				 * .field(CollectionServiceConstants.PAYEE_NAME_MISSING_FIELD)
				 * .build(); errorFields.add(errorField);
				 */

				if (null == receipt.getBill().get(0).getPaidBy()
						|| receipt.getBill().get(0).getPaidBy().isEmpty()) {
					errorField = ErrorField
							.builder()
							.code(CollectionServiceConstants.PAID_BY_MISSING_CODE)
							.message(
									CollectionServiceConstants.PAID_BY_MISSING_MESSAGE)
							.field(CollectionServiceConstants.PAID_BY_MISSING_FIELD)
							.build();
					errorFields.add(errorField);
				}

				for (BillDetail billDetails : receipt.getBill().get(0)
						.getBillDetails()) {
					List<BillAccountDetail> billAccountDetails = new ArrayList<>();

					if (StringUtils.isBlank(billDetails.getBillDescription())) {
						errorField = ErrorField
								.builder()
								.code(CollectionServiceConstants.COLL_DETAILS_DESCRIPTION_CODE)
								.message(
										CollectionServiceConstants.COLL_DETAILS_DESCRIPTION_MESSAGE)
								.field(CollectionServiceConstants.COLL_DETAILS_DESCRIPTION_FIELD)
								.build();
						errorFields.add(errorField);
					}

					if (null == billDetails.getBusinessService()
							|| billDetails.getBusinessService().isEmpty()) {
						errorField = ErrorField
								.builder()
								.code(CollectionServiceConstants.BD_CODE_MISSING_CODE)
								.message(
										CollectionServiceConstants.BD_CODE_MISSING_MESSAGE)
								.field(CollectionServiceConstants.BD_CODE_MISSING_FIELD)
								.build();
						errorFields.add(errorField);
					}
					CollectionConfigGetRequest collectionConfigGetRequest = new CollectionConfigGetRequest();
					collectionConfigGetRequest.setTenantId(receipt
							.getTenantId());
					collectionConfigGetRequest
							.setName(CollectionServiceConstants.MANUAL_RECEIPT_DETAILS_REQUIRED_CONFIG_KEY);

					Map<String, List<String>> manualReceiptRequiredConfiguration = collectionConfigService
							.getCollectionConfiguration(collectionConfigGetRequest);

					List<Role> roleList = requestInfo.getUserInfo().getRoles();
					if (!manualReceiptRequiredConfiguration.isEmpty()
							&& manualReceiptRequiredConfiguration
									.get(CollectionServiceConstants.MANUAL_RECEIPT_DETAILS_REQUIRED_CONFIG_KEY)
									.get(0).equalsIgnoreCase("Yes")
							&& roleList
									.stream()
									.anyMatch(
											role -> CollectionServiceConstants.COLLECTION_LEGACY_RECEIPT_CREATOR_ROLE
													.contains(role.getName()))
							&& StringUtils.isNotEmpty(billDetails
									.getManualReceiptNumber())
							&& billDetails.getReceiptDate() != null) {
						CollectionConfigGetRequest collectionConfigRequest = new CollectionConfigGetRequest();
						collectionConfigRequest.setTenantId(receipt
								.getTenantId());
						collectionConfigRequest
								.setName(CollectionServiceConstants.MANUAL_RECEIPT_DETAILS_CUTOFF_DATE_CONFIG_KEY);
						Map<String, List<String>> manualReceiptCutOffDateConfiguration = collectionConfigService
								.getCollectionConfiguration(collectionConfigRequest);
						if (!manualReceiptCutOffDateConfiguration.isEmpty()
								&& manualReceiptCutOffDateConfiguration
										.get(CollectionServiceConstants.MANUAL_RECEIPT_DETAILS_CUTOFF_DATE_CONFIG_KEY) != null) {
							SimpleDateFormat dateFormat = new SimpleDateFormat(
									"dd-MM-yyyy");
							Date cutOffDate = dateFormat
									.parse(manualReceiptCutOffDateConfiguration
											.get(CollectionServiceConstants.MANUAL_RECEIPT_DETAILS_CUTOFF_DATE_CONFIG_KEY)
											.get(0));
							Date manualReceiptDate = new Date(
									billDetails.getReceiptDate());
							if (manualReceiptDate.after(cutOffDate)) {
								errorField = ErrorField
										.builder()
										.code(CollectionServiceConstants.CUTT_OFF_DATE_CODE)
										.message(
												CollectionServiceConstants.CUTT_OFF_DATE_MESSAGE
														+ cutOffDate
														+ CollectionServiceConstants.CUTT_OFF_DATE_MESSAGE_DESC)
										.field(CollectionServiceConstants.CUTT_OFF_DATE_FIELD)
										.build();
								errorFields.add(errorField);
							}
						}

					}
					for (BillAccountDetail billAccountDetail : billAccountDetails) {
						if (null == billAccountDetail.getPurpose()) {
							errorField = ErrorField
									.builder()
									.code(CollectionServiceConstants.PURPOSE_MISSING_CODE)
									.message(
											CollectionServiceConstants.PURPOSE_MISSING_MESSAGE)
									.field(CollectionServiceConstants.PURPOSE_MISSING_FIELD)
									.build();
							errorFields.add(errorField);
						}

						if (null == billAccountDetail.getGlcode()
								|| billAccountDetail.getGlcode().isEmpty()) {
							errorField = ErrorField
									.builder()
									.code(CollectionServiceConstants.COA_MISSING_CODE)
									.message(
											CollectionServiceConstants.COA_MISSING_MESSAGE)
									.field(CollectionServiceConstants.COA_MISSING_FIELD)
									.build();
							errorFields.add(errorField);
						}
					}
				}
			}
		} catch (Exception e) {
			final ErrorField errorField = ErrorField
					.builder()
					.code(HttpStatus.BAD_REQUEST.toString())
					.message(CollectionServiceConstants.INVALID_RECEIPT_REQUEST)
					.field(CollectionServiceConstants.INVALID_RECEIPT_REQUEST)
					.build();
			errorFields.add(errorField);
			LOGGER.error("Exception in Receipt Request Validator" + e);
		}
	}

	public void validateSearchReceiptRequest(
			final ReceiptSearchGetRequest receiptGetRequest) {
		if (receiptGetRequest.getFromDate() > receiptGetRequest.getToDate()) {
			throw new ValidationException(
					CollectionServiceConstants.INVALID_DATE_EXCEPTION_MSG);
		}
	}
}
