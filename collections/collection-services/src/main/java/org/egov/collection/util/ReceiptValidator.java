package org.egov.collection.util;

import lombok.extern.slf4j.Slf4j;
import org.egov.collection.model.Instrument;
import org.egov.collection.model.LegacyReceiptHeader;
import org.egov.collection.model.ReceiptSearchCriteria;
import org.egov.collection.model.enums.InstrumentTypesEnum;
import org.egov.collection.repository.BusinessDetailsRepository;
import org.egov.collection.repository.CollectionRepository;
import org.egov.collection.web.contract.*;
import org.egov.common.contract.request.RequestInfo;
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

    private BusinessDetailsRepository businessDetailsRepository;
    private CollectionRepository collectionRepository;

    ReceiptValidator(BusinessDetailsRepository businessDetailsRepository, CollectionRepository collectionRepository) {
        this.businessDetailsRepository = businessDetailsRepository;
        this.collectionRepository = collectionRepository;
    }

    /**
     * Validate to ensure,
     *  - Instrument type provided is valid
     *  - Amount Paid & total amount are not null and are integer values, no fractions allowed!
     *  - Allow zero amount ONLY IF the total bill amount is also equal to zero
     *  - Sum of amount paid on all bill details should be equal to the instrument amount
     *  - The bill is not being repaid, if a receipt is already created and is in completed / pending
     *      state do not allow creation of another receipt.
     *  - Business service code provided is valid, call is being made in a loop as the tenant id could be different for
     *      each bill detail
     *  - Bill account details are valid, checks for purpose and GL Codes
     *  - Cheque and DD dates are correct
     *
     * @param receiptRequest Receipt request to be validated
     */
    public void validateReceiptForCreate(final ReceiptReq receiptRequest) {

        Map<String, String> errorMap = new HashMap<>();
        Receipt receipt = receiptRequest.getReceipt().get(0);

        if (receipt.getBill().isEmpty())
            return;

        if (isEmpty(receipt.getBill().get(0).getPaidBy()))
            errorMap.put(PAID_BY_MISSING_CODE, PAID_BY_MISSING_MESSAGE);

        String instrumentType = receipt.getInstrument().getInstrumentType().getName();
        if(!InstrumentTypesEnum.contains(instrumentType)){
            throw new CustomException("INVALID_INSTRUMENT_TYPE", "Invalid instrument type provided");
        }


//      Compute total amount paid by summing all billDetail amounts and compare with instrument amount
        BigDecimal totalAmountPaid = BigDecimal.ZERO;

//      Loop through all bill details [one for each service], and perform various validations
        for (BillDetail billDetails : receipt.getBill().get(0).getBillDetails()) {
            BigDecimal amountPaid = billDetails.getAmountPaid();
            BigDecimal totalAmount = billDetails.getTotalAmount();

            if (isNull(amountPaid) || !isIntegerValue(amountPaid)) {
                errorMap.put(AMOUNT_PAID_CODE, AMOUNT_PAID_MESSAGE);
            }

            if (isNull(totalAmount) || !isIntegerValue(totalAmount)) {
                errorMap.put("INVALID_BILL_AMOUNT", "Invalid bill amount! Amount should be  greater than 0 and " +
                        "without fractions");
            }
            if (!isNull(amountPaid) && (amountPaid.compareTo(BigDecimal.ZERO) == 0 && totalAmount.compareTo
                    (BigDecimal.ZERO) != 0)) {
                errorMap.put("INVALID_AMOUNT", "Invalid amount paid, amount paid can only be 0 if bill amount is also" +
                        " 0");
            }


            ReceiptSearchCriteria criteria = ReceiptSearchCriteria.builder()
                    .tenantId(billDetails.getTenantId())
                    .billIds(Collections.singletonList(billDetails.getId()))
                    .build();

            List<Receipt> receipts = collectionRepository.fetchReceipts(criteria);

            if (receipts.isEmpty()) {
                validateIfReceiptForBillAbsent(errorMap, billDetails);
            } else {
                validateIfReceiptForBillPresent(errorMap, receipts, billDetails);
            }

            validateBusinessServiceCode(receiptRequest.getRequestInfo(), billDetails, errorMap);

            validateBillAccountDetails(billDetails.getBillAccountDetails(), errorMap);

            totalAmountPaid = totalAmountPaid.add(amountPaid);


            if (isBlank(billDetails.getBillDescription()))
                errorMap.put(COLL_DETAILS_DESCRIPTION_CODE, COLL_DETAILS_DESCRIPTION_MESSAGE);

            if (isEmpty(billDetails.getBusinessService())) {
                errorMap.put(BD_CODE_MISSING_CODE, BD_CODE_MISSING_MESSAGE);
            }

            if (instrumentType.equalsIgnoreCase(InstrumentTypesEnum.CHEQUE.name()) || instrumentType.equalsIgnoreCase
                    (InstrumentTypesEnum.DD.name())) {
                validateChequeDD(billDetails, receipt.getInstrument(), errorMap);
            }
        }

//      Validation to ensure, Sum of amount paid on all bill details should be equal to the instrument amount

        Instrument instrument = receipt.getInstrument();
        if (instrument.getAmount().compareTo(totalAmountPaid) != 0)
            errorMap.put("INSTRUMENT_AMOUNT_MISMATCH", "Sum of amount paid of all bill details should be equal to " +
                    "instrument amount");

        if (!errorMap.isEmpty())
            throw new CustomException(errorMap);
    }


    private void validateChequeDD(BillDetail billDetails, Instrument instrument, Map<String, String> errorMap) {

        DateTime instrumentDate = new DateTime(instrument.getTransactionDateInput());

        if (!Objects.isNull(billDetails.getReceiptDate()) && isNotEmpty(billDetails.getManualReceiptNumber())) {
            if (instrumentDate.isAfter(billDetails.getReceiptDate())) {
                errorMap.put(RECEIPT_CHEQUE_OR_DD_DATE_CODE, RECEIPT_CHEQUE_OR_DD_DATE_MESSAGE);
            }

            Days daysDiff = Days.daysBetween(instrumentDate, new DateTime(billDetails.getReceiptDate()));
            if (daysDiff.getDays() > Integer.valueOf(INSTRUMENT_DATE_DAYS)) {
                errorMap.put(CHEQUE_DD_DATE_WITH_MANUAL_RECEIPT_DATE_CODE,CHEQUE_DD_DATE_WITH_MANUAL_RECEIPT_DATE_MESSAGE);
            }

        } else {
            Days daysDiff = Days.daysBetween(instrumentDate, new DateTime());
            if (daysDiff.getDays() > Integer.valueOf(INSTRUMENT_DATE_DAYS)) {
                errorMap.put(CHEQUE_DD_DATE_WITH_RECEIPT_DATE_CODE,CHEQUE_DD_DATE_WITH_RECEIPT_DATE_MESSAGE);
            }
            if (instrumentDate.isAfter(new DateTime().getMillis())) {
                errorMap.put(CHEQUE_DD_DATE_WITH_FUTURE_DATE_CODE,CHEQUE_DD_DATE_WITH_FUTURE_DATE_MESSAGE);
            }
        }

    }

    private void validateBillAccountDetails(List<BillAccountDetail> billAccountDetails, Map<String, String> errorMap) {
        for (BillAccountDetail billAccountDetail : billAccountDetails) {
            if (isNull(billAccountDetail.getPurpose())) {
                throw new CustomException(PURPOSE_MISSING_CODE, PURPOSE_MISSING_MESSAGE);
            }

            if (isEmpty(billAccountDetail.getGlcode())) {
                throw new CustomException(COA_MISSING_CODE, COA_MISSING_MESSAGE);
            }
        }

    }

    /**
     * Validations if no receipts exists for this bill
     * - If part payment is allowed,
     *   - Amount being paid should not be lower than minimum payable amount
     *   - Amount being paid should not be greater than bill value
     * <p>
     * - If part payment is not allowed,
     *   - Amount being paid should be equal to bill value
     *
     * @param errorMap   Map of errors occurred during validations
     * @param billDetail Bill detail for which payment is being made
     */

    private void validateIfReceiptForBillAbsent(Map<String, String> errorMap, BillDetail billDetail) {
        if (billDetail.getPartPaymentAllowed()) {

            if(billDetail.getAmountPaid().compareTo(billDetail.getMinimumAmount()) < 0 ){
                log.error("Amount paid of {} cannot be lesser than minimum payable amount of {} for bill detail " +
                                "{}", billDetail.getAmountPaid(), billDetail.getMinimumAmount(), billDetail.getId());
                errorMap.put("AMOUNT_MISMATCH", "Amount paid cannot be greater than bill amount");
            }

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
     * @param receipts List of receipt headers
     * @param billDetail     Bill detail for which payment is being made
     */
    private void validateIfReceiptForBillPresent(Map<String, String> errorMap, List<Receipt> receipts,
                                                 BillDetail billDetail) {
        for (Receipt receipt : receipts) {
            String receiptStatus = receipt.getBill().get(0).getBillDetails().get(0).getStatus();
            if (receiptStatus.equalsIgnoreCase(APPROVED.toString()) || receiptStatus
                    .equalsIgnoreCase(APPROVALPENDING.toString())) {
                errorMap.put("BILL_ALREADY_PAID", "Bill has already been paid or is in pending state");
                return;
            }
        }
        validateIfReceiptForBillAbsent(errorMap, billDetail);

    }


    /**
     * @param billDetail
     * @param errorMap
     */
    private void validateBusinessServiceCode(RequestInfo requestInfo, BillDetail billDetail, Map<String, String>
            errorMap) {
        BusinessDetailsResponse businessDetailsResponse = businessDetailsRepository.getBusinessDetails(Collections.singletonList(billDetail.getBusinessService()),
                        billDetail.getTenantId(), requestInfo);

        if (Objects.isNull(businessDetailsResponse.getBusinessDetails()) || businessDetailsResponse
                .getBusinessDetails().isEmpty()) {
            log.error("Business detail not found for {} and tenant {}", billDetail.getBusinessService(), billDetail
                    .getTenantId());
            errorMap.put(BUSINESSDETAILS_EXCEPTION_MSG, BUSINESSDETAILS_EXCEPTION_DESC);
        }
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