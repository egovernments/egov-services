package org.egov.collection.util;

import lombok.extern.slf4j.Slf4j;
import org.egov.collection.model.*;
import org.egov.collection.repository.BillingServiceRepository;
import org.egov.collection.repository.ReceiptRepository;
import org.egov.collection.web.contract.*;
import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.tracer.model.CustomException;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.*;
import static org.egov.collection.config.CollectionServiceConstants.*;
import static org.egov.collection.model.enums.ReceiptStatus.APPROVALPENDING;
import static org.egov.collection.model.enums.ReceiptStatus.APPROVED;

@Slf4j
@Component
public class ReceiptValidator {

    private BillingServiceRepository billingRepository;
    private ReceiptRepository receiptRepository;

    ReceiptValidator(BillingServiceRepository billingRepository, ReceiptRepository receiptRepository) {
        this.billingRepository = billingRepository;
        this.receiptRepository = receiptRepository;
    }

    public void validatecreateReceiptRequest(final ReceiptReq receiptRequest) {
        Map<String, String> errorMap = new HashMap<>();

        validateBill(receiptRequest, errorMap);
        validatecreateReceiptRequest(receiptRequest, errorMap);
        validateInstrument(receiptRequest, errorMap);

        if (!errorMap.isEmpty())
            throw new CustomException(errorMap);
    }


    private void validateBill(final ReceiptReq receiptReq, Map<String, String> errorMap) {
        //Assume one element is always present, bean validation

        Receipt receipt = receiptReq.getReceipt().get(0);
        Bill billFromRequest = receipt.getBill().get(0);

        for (BillDetail billDetail : billFromRequest.getBillDetails()) {
            ReceiptSearchCriteria criteria = ReceiptSearchCriteria.builder()
                    .tenantId(billFromRequest.getTenantId())
                    .billIds(Collections.singletonList(billDetail.getId()))
                    .build();

            Pagination<ReceiptHeader> receiptHeaders = receiptRepository.findAllReceiptsByCriteria(criteria,
                    receiptReq.getRequestInfo());


            if (receiptHeaders.getPagedData().isEmpty()) {
                validateIfReceiptForBillAbsent(errorMap, billDetail);
            } else {
                validateIfReceiptForBillPresent(errorMap, receiptHeaders.getPagedData(), billDetail);
            }
        }

    }

    /**
     * Validations if no receipts exists for this bill
     * If part payment is allowed,
     * Amount being paid should not be greater than bill value
     * <p>
     * If part payment is not allowed,
     * Amount being paid should be equal to bill value
     *
     * @param errorMap   Map of errors occurred during validations
     * @param billDetail Bill detail for which payment is being made
     */

    private void validateIfReceiptForBillAbsent(Map<String, String> errorMap, BillDetail billDetail) {
        if (billDetail.getPartPaymentAllowed()) {
            if (billDetail.getTotalAmount().compareTo(billDetail.getAmountPaid()) < 0) {
                log.error("Amount paid of {} cannot be greater than bill amount of {} for bill detail {}", billDetail
                                .getAmountPaid()
                        , billDetail.getTotalAmount(), billDetail.getId());
                errorMap.put("AMOUNT_MISMATCH", "Amount paid cannot be greater than bill amount");
            }
        } else {
            if (!(billDetail.getTotalAmount().compareTo(billDetail.getAmountPaid()) == 0)) {
                log.error("Transaction Amount of {} has to be equal to bill amount of {} for bill detail {}",
                        billDetail.getAmountPaid(),
                        billDetail.getTotalAmount(), billDetail.getId());
                errorMap.put("AMOUNT_MISMATCH", "Amount paid has to be equal to bill amount");
            }
        }
    }

    /**
     * Validations if no transaction exists for this bill
     * No existing receipt should be in approved or pending status
     * <p>
     * If not, proceed with validateIfReceiptForBillAbsent validations
     * *
     *
     * @param errorMap       Map of errors occurred during validations
     * @param receiptHeaders List of receipt headers
     * @param billDetail     Bill detail for which payment is being made
     */
    private void validateIfReceiptForBillPresent(Map<String, String> errorMap, List<ReceiptHeader>
            receiptHeaders, BillDetail billDetail) {
        for (ReceiptHeader receiptHeader : receiptHeaders) {
            String receiptStatus = receiptHeader.getStatus();
            if (receiptStatus.equalsIgnoreCase(APPROVED.toString()) || receiptStatus
                    .equalsIgnoreCase(APPROVALPENDING.toString())) {
                errorMap.put("BILL_ALREADY_PAID", "Bill has already been paid or is in pending state");
                return;
            }
        }
        validateIfReceiptForBillAbsent(errorMap, billDetail);

    }

    private void validatecreateReceiptRequest(final ReceiptReq receiptRequest, Map<String, String> errorMap) {

        Receipt receipt = receiptRequest.getReceipt().get(0);

        if (receipt.getBill().isEmpty())
            return;

        if (isEmpty(receipt.getBill().get(0).getPaidBy()))
            errorMap.put(PAID_BY_MISSING_CODE, PAID_BY_MISSING_MESSAGE);


        for (BillDetail billDetails : receipt.getBill().get(0).getBillDetails()) {
            BigDecimal amountPaid = billDetails.getAmountPaid();
            BigDecimal totalAmount = billDetails.getTotalAmount();

            List<BillAccountDetail> billAccountDetails = billDetails.getBillAccountDetails();

            if (isBlank(billDetails.getBillDescription()))
                errorMap.put(COLL_DETAILS_DESCRIPTION_CODE, COLL_DETAILS_DESCRIPTION_MESSAGE);

            if (isNull(amountPaid) || !isIntegerValue(amountPaid)) {
                errorMap.put(AMOUNT_PAID_CODE, AMOUNT_PAID_MESSAGE);
            }

            if (!isNull(amountPaid) && (amountPaid.compareTo(BigDecimal.ZERO) == 0 && totalAmount.compareTo
                    (BigDecimal.ZERO) != 0)) {
                errorMap.put("INVALID_AMOUNT", "Invalid amount paid, amount paid can only be 0 if bill amount is also" +
                        " 0");
            }


            if (isNull(totalAmount) || !isIntegerValue(totalAmount)) {
                errorMap.put("INVALID_BILL_AMOUNT", "Invalid bill amount! Amount should be  greater than 0 and " +
                        "without fractions");
            }

            if (isEmpty(billDetails.getBusinessService())) {
                errorMap.put(BD_CODE_MISSING_CODE, BD_CODE_MISSING_MESSAGE);
            }


            for (BillAccountDetail billAccountDetail : billAccountDetails) {
                if (isNull(billAccountDetail.getPurpose())) {
                    throw new CustomException(PURPOSE_MISSING_CODE, PURPOSE_MISSING_MESSAGE);
                }

                if (isEmpty(billAccountDetail.getGlcode())) {
                    throw new CustomException(COA_MISSING_CODE, COA_MISSING_MESSAGE);
                }
            }

            String instrumentType = receipt.getInstrument().getInstrumentType().getName();

            if (instrumentType.equalsIgnoreCase(INSTRUMENT_TYPE_CHEQUE) || instrumentType.equalsIgnoreCase
                    (INSTRUMENT_TYPE_DD)) {
                DateTime instrumentDate = new DateTime(receipt.getInstrument().getTransactionDateInput());

                if (billDetails.getReceiptDate() != null && isNotEmpty(billDetails.getManualReceiptNumber())) {
                    if (instrumentDate.isAfter(billDetails.getReceiptDate())) {
                        throw new CustomException(RECEIPT_CHEQUE_OR_DD_DATE_CODE, RECEIPT_CHEQUE_OR_DD_DATE_MESSAGE);
                    }

                    Days daysDiff = Days.daysBetween(instrumentDate, new DateTime(billDetails.getReceiptDate()));
                    if (daysDiff.getDays() > Integer.valueOf(INSTRUMENT_DATE_DAYS)) {
                        throw new CustomException(CHEQUE_DD_DATE_WITH_MANUAL_RECEIPT_DATE_CODE,
                                CHEQUE_DD_DATE_WITH_MANUAL_RECEIPT_DATE_MESSAGE);
                    }

                } else {
                    Days daysDiff = Days.daysBetween(instrumentDate, new DateTime());
                    if (daysDiff.getDays() > Integer.valueOf(INSTRUMENT_DATE_DAYS)) {
                        throw new CustomException(CHEQUE_DD_DATE_WITH_RECEIPT_DATE_CODE,
                                CHEQUE_DD_DATE_WITH_RECEIPT_DATE_MESSAGE);
                    }
                    if (instrumentDate.isAfter(new DateTime().getMillis())) {
                        throw new CustomException(CHEQUE_DD_DATE_WITH_FUTURE_DATE_CODE,
                                CHEQUE_DD_DATE_WITH_FUTURE_DATE_MESSAGE);
                    }
                }
            }

        }

    }

    /**
     * Validation to ensure,
     * Sum of amount paid on all bill details should be equal to the instrument amount
     *
     * @param receiptRequest Receipt request to be validated
     * @param errorMap       Map of errors occurred during validations
     */
    private void validateInstrument(final ReceiptReq receiptRequest, Map<String, String> errorMap) {
        //bean validation to take care that there exists at least one bill

        Receipt receipt = receiptRequest.getReceipt().get(0);
        Bill bill = receipt.getBill().get(0);
        Instrument instrument = receipt.getInstrument();
        BigDecimal totalAmountPaid = BigDecimal.ZERO;

        for (BillDetail billDetail : bill.getBillDetails()) {
            totalAmountPaid = totalAmountPaid.add(billDetail.getAmountPaid());
        }

        if (instrument.getAmount().compareTo(totalAmountPaid) != 0)
            errorMap.put("INSTRUMENT_AMOUNT_MISMATCH", "Sum of amount paid of all bill details should be equal to " +
                    "instrument amount");
    }

    public void validateSearchReceiptRequest(final ReceiptSearchCriteria receiptSearchCriteria) {

        Map<String, String> errorMap = new HashMap<>();

        if (isBlank(receiptSearchCriteria.getTenantId())) {
            errorMap.put(TENANT_ID_REQUIRED_CODE, TENANT_ID_REQUIRED_MESSAGE);
        }

        if (!isNull(receiptSearchCriteria.getFromDate()) && !isNull(receiptSearchCriteria.getToDate()) &&
                receiptSearchCriteria.getFromDate() > receiptSearchCriteria.getToDate()) {
            errorMap.put(FROM_DATE_GREATER_CODE, FROM_DATE_GREATER_MESSAGE);
        }

        if (!isNull(receiptSearchCriteria.getBillIds()) && !receiptSearchCriteria.getBillIds().isEmpty() && isBlank
                (receiptSearchCriteria.getBusinessCode())) {
            errorMap.put(BUSINESS_CODE_REQUIRED_CODE, BUSINESS_CODE_REQUIRED_MESSAGE);
        }

        if (!errorMap.isEmpty())
            throw new CustomException(errorMap);
    }


    public List<ErrorResponse> validateCreateLegacyReceiptRequest(
            LegacyReceiptReq legacyReceiptRequest) {
        List<ErrorResponse> errorResponses = null;
        final Error error = getError(legacyReceiptRequest);
        if (error != null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponses = new ArrayList<>();
            errorResponse.setError(error);
            errorResponses.add(errorResponse);
        }
        return errorResponses;
    }

    private Error getError(LegacyReceiptReq legacyReceiptRequest) {
        final List<ErrorField> errorFields = getErrorFields(legacyReceiptRequest);
        Error error = null;
        if (null != errorFields && !errorFields.isEmpty())
            error = Error
                    .builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(
                            INVALID_LEGACY_RECEIPT_REQUEST)
                    .fields(errorFields).build();
        return error;
    }

    private List<ErrorField> getErrorFields(
            LegacyReceiptReq legacyReceiptRequest) {
        final List<ErrorField> errorFields = null;
        addLegacyReceiptValidationErrors(legacyReceiptRequest, errorFields);
        return errorFields;
    }

    private void addLegacyReceiptValidationErrors(
            LegacyReceiptReq legacyReceiptRequest, List<ErrorField> errorFields) {
        for (LegacyReceiptHeader legacyReceiptHeader : legacyReceiptRequest
                .getLegacyReceipts()) {
            if (legacyReceiptHeader.getReceiptNo() == null
                    || legacyReceiptHeader.getReceiptNo().isEmpty()) {
                ErrorField errorField = ErrorField
                        .builder()
                        .code(RCPTNO_MISSING_CODE)
                        .message(
                                RCPTNO_MISSING_MESSAGE)
                        .field(RCPTNO_FIELD_NAME)
                        .build();
                errorFields.add(errorField);
            } else if (legacyReceiptHeader.getReceiptDate() == null) {
                ErrorField
                        .builder()
                        .code(RCPTDATE_MISSING_CODE)
                        .message(
                                RCPTDATE_MISSING_MESSAGE)
                        .field(RCPTDATE_FIELD_NAME)
                        .build();
            }
        }
    }

    private boolean isIntegerValue(BigDecimal bd) {
        return bd.signum() == 0 || bd.scale() <= 0 || bd.stripTrailingZeros().scale() <= 0;
    }
}

//    private void validateWorkFlowDetails(final ReceiptReq receiptRequest,
//			List<ErrorField> errorFields) {
//		String tenantId = receiptRequest.getReceipt().get(0).getTenantId();
//		CollectionConfigGetRequest collectionConfigGetRequest = new CollectionConfigGetRequest();
//		collectionConfigGetRequest.setTenantId(tenantId);
//		collectionConfigGetRequest
//				.setName(RECEIPT_PREAPPROVED_OR_APPROVED_CONFIG_KEY);
//
//		Map<String, List<String>> workFlowConfigValues = collectionConfigService
//				.getCollectionConfiguration(collectionConfigGetRequest);
//		if (!workFlowConfigValues.isEmpty()
//				&& workFlowConfigValues
//						.get(RECEIPT_PREAPPROVED_OR_APPROVED_CONFIG_KEY)
//						.get(0)
//						.equalsIgnoreCase(
//								PREAPPROVED_CONFIG_VALUE)) {
//			List<Employee> employees = employeeRepository
//					.getPositionsForEmployee(receiptRequest.getRequestInfo(),
//							receiptRequest.getRequestInfo().getUserInfo()
//									.getId(), tenantId);
//			if (employees.isEmpty()) {
//				final ErrorField errorField = ErrorField
//						.builder()
//						.code(RECEIPT_WORKFLOW_ASSIGNEE_MISSING_CODE)
//						.message(
//								RECEIPT_WORKFLOW_ASSIGNEE_MISSING_MESSAGE)
//						.field(RECEIPT_WORKFLOW_ASSIGNEE_MISSING_FIELD)
//						.build();
//				errorFields.add(errorField);
//			}
//		}
//
//	}