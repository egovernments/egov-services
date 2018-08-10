package org.egov.collection.util;

import lombok.extern.slf4j.Slf4j;
import org.egov.collection.repository.BillingServiceRepository;
import org.egov.collection.web.contract.Bill;
import org.egov.collection.web.contract.Receipt;
import org.egov.collection.web.contract.ReceiptReq;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
@Slf4j
public class ReceiptEnricher {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private BillingServiceRepository billingRepository;

    ReceiptEnricher(BillingServiceRepository billingRepository) {
        this.billingRepository = billingRepository;
    }

    /**
     * Fetch bill from billing service for the provided bill id
     * Ensure bill exists and amount paid details exist for all bill details
     * Enrich the new bill object that's fetched
     *
     * @param receiptReq Receipt to be enriched
     */
    public void enrichReceipt(ReceiptReq receiptReq) {
        Receipt receipt = receiptReq.getReceipt().get(0);
        Bill billFromRequest = receipt.getBill().get(0);

        List<Bill> validatedBills = billingRepository.fetchBill(receiptReq.getRequestInfo(), receipt.getTenantId(), billFromRequest.getId
                ());

        if (validatedBills.isEmpty() || validatedBills.get(0).getBillDetails().isEmpty()) {
            log.error("Bill ID provided does not exist or is in an invalid state " + billFromRequest.getId());
            throw new CustomException("INVALID_BILL_ID", "Bill ID provided does not exist or is in an invalid state");
        }

        if (validatedBills.get(0).getBillDetails().size() != billFromRequest.getBillDetails().size()) {
            log.error("Mismatch in bill details records provided in request and actual bill. Expected {} billdetails " +
                    "found {} in request", billFromRequest.getBillDetails().size(), validatedBills.get(0)
                    .getBillDetails().size());
            throw new CustomException("INVALID_BILL_ID", "Mismatch in bill detail records provided in request and actual bill");

        }

        enrichValidatedBills(billFromRequest, validatedBills.get(0));
        receipt.setBill(validatedBills);


    }

    /**
     * Set paid by and amount paid for each bill detail
     *
     * @param billFromRequest Bill provided in request
     * @param validatedBill   Validated bill retrieved from billing service
     */
    private void enrichValidatedBills(Bill billFromRequest, Bill validatedBill) {
        validatedBill.setPaidBy(billFromRequest.getPaidBy());

        for (int i = 0; i < validatedBill.getBillDetails().size(); i++) {
            validatedBill.getBillDetails().get(i).setAmountPaid(billFromRequest.getBillDetails().get(i).getAmountPaid
                    ());
        }

    }

//    public void  enrichInstrument(Instrument instrument){
//        instrument.setTransactionType(TransactionType.Debit);
//        instrument.setTenantId(tenantId);
//
//        if (instrument.getInstrumentType().getName().equalsIgnoreCase(INSTRUMENT_TYPE_CASH)|| instrument
//                .getInstrumentType().getName().equalsIgnoreCase(INSTRUMENT_TYPE_ONLINE)) {
//
//            String transactionDate = simpleDateFormat.format(new Date());
//            instrument.setTransactionDate(simpleDateFormat.parse(transactionDate));
//            instrument.setTransactionNumber(transactionId);
//            if (onlinePayment == null) {
//                onlinePayment = new OnlinePayment();
//                onlinePayment.setTenantId(tenantId);
//                onlinePayment.setTransactionNumber(transactionId);
//                onlinePayment.setTransactionDate(simpleDateFormat.parse(transactionDate).getTime());
//                onlinePayment.setAuthorisationStatusCode(ONLINE_PAYMENT_AUTHORISATION_SUCCESS_CODE);
//                onlinePayment.setRemarks(ONLINE_PAYMENT_REMARKS);
//                onlinePayment.setStatus(OnlineStatusCode.SUCCESS.toString());
//                onlinePayment.setTransactionAmount(billDetail.getAmountPaid());
//            }
//        } else {
//            DateTime transactionDate = new DateTime(instrument.getTransactionDateInput());
//            instrument.setTransactionDate(simpleDateFormat.parse(transactionDate.toString("dd/MM/yyyy")));
//        }
//    }
}
