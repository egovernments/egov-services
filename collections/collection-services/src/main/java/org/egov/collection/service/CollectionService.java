package org.egov.collection.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.model.Instrument;
import org.egov.collection.model.ReceiptSearchCriteria;
import org.egov.collection.producer.CollectionProducer;
import org.egov.collection.repository.BillingServiceRepository;
import org.egov.collection.repository.CollectionRepository;
import org.egov.collection.repository.InstrumentRepository;
import org.egov.collection.util.ReceiptEnricher;
import org.egov.collection.util.ReceiptValidator;
import org.egov.collection.web.contract.*;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CollectionService {

    private CollectionRepository collectionRepository;
    private InstrumentRepository instrumentRepository;
    private BillingServiceRepository billingServiceRepository;
    private ReceiptEnricher receiptEnricher;
    private ReceiptValidator receiptValidator;
    private CollectionProducer collectionProducer;
    private ApplicationProperties applicationProperties;

    @Autowired
    public CollectionService(CollectionRepository collectionRepository, InstrumentRepository instrumentRepository,
                             BillingServiceRepository billingServiceRepository, ReceiptEnricher receiptEnricher,
                             ReceiptValidator receiptValidator, CollectionProducer collectionProducer,
                             ApplicationProperties applicationProperties) {
        this.collectionRepository = collectionRepository;
        this.instrumentRepository = instrumentRepository;
        this.billingServiceRepository = billingServiceRepository;
        this.receiptEnricher = receiptEnricher;
        this.receiptValidator = receiptValidator;
        this.collectionProducer = collectionProducer;
        this.applicationProperties = applicationProperties;
    }

    /**
     * Fetch all receipts matching the given criteria, enrich receipts with instruments
     *
     * @param requestInfo Request info of the search
     * @param receiptSearchCriteria Criteria against which search has to be performed
     * @return List of matching receipts
     */
    public List<Receipt> getReceipts(RequestInfo requestInfo, ReceiptSearchCriteria receiptSearchCriteria){
        receiptSearchCriteria.setOffset(0);
        receiptSearchCriteria.setLimit(25);

        List<Receipt> receipts = collectionRepository.fetchReceipts(receiptSearchCriteria);
//        if(!receipts.isEmpty())
//            receiptEnricher.enrichReceiptsWithInstruments(requestInfo, receipts);
        return receipts;
    }


    /**
     * Handles creation of a receipt, including multi-service, involves the following steps,
     *   - Enrich receipt from billing service using bill id
     *   - Validate the receipt object
     *   - Enrich receipt with receipt numbers, coll type etc
     *   - Apportion paid amount
     *   - Persist the receipt object
     *   - Create instrument
     *
     * @param receiptReq Receipt request for which receipt has to be created
     * @return Created receipt
     */
    @Transactional
    public Receipt createReceipt(ReceiptReq receiptReq) {

        receiptEnricher.enrichReceiptPreValidate(receiptReq);
        receiptValidator.validateReceiptForCreate(receiptReq);
        receiptEnricher.enrichReceiptPostValidate(receiptReq);

        Receipt receipt = receiptReq.getReceipt().get(0);
        Bill bill = receipt.getBill().get(0);
        List<BillDetail> billDetails = apportionPaidAmount(receiptReq.getRequestInfo(), receipt);
        bill.setBillDetails(billDetails);

        collectionRepository.saveReceipt(receipt);

//        if (receipt.getInstrument().getAmount().compareTo(BigDecimal.ZERO) > 0) {
//            Instrument instrument = instrumentRepository.createInstrument(receiptReq.getRequestInfo(), receipt
//                    .getInstrument());
//            receipt.getInstrument().setId(instrument.getId());
//            collectionRepository.saveInstrument(receipt);
//        }

        collectionProducer.producer(applicationProperties.getCreateReceiptTopicName(), applicationProperties
                .getCreateReceiptTopicKey(), receiptReq);

        return receipt;
    }

    
    
    @Transactional
    public Receipt updateReceipt(ReceiptReq receiptReq) {


        Receipt receipt = receiptReq.getReceipt().get(0);

        collectionRepository.updateReceipt(receipt);

        collectionProducer.producer(applicationProperties.getUpdateReceiptTopicName(), applicationProperties
                .getUpdateReceiptTopicKey(), receiptReq);

        return receipt;
    }

    public List<Receipt> cancelReceipt(RequestInfo requestInfo, String transactionNumber){
        ReceiptSearchCriteria receiptSearchCriteria = ReceiptSearchCriteria.builder().transactionId
                (transactionNumber).build();
        List<Receipt> receipts = getReceipts(requestInfo, receiptSearchCriteria);
        receiptValidator.validateReceiptsForCancellation(receipts);

        return receipts;
    }

    /**
     * Validates a provisional receipt,
     *   - Enriches receipt from billing service using bill id
     *   - Validates the receipt object
     *
     * @param receiptReq Receipt request for which receipt has to be validated
     * @return Validated receipt
     */
    public List<Receipt> validateReceipt(ReceiptReq receiptReq){
        receiptEnricher.enrichReceiptPreValidate(receiptReq);
        receiptValidator.validateReceiptForCreate(receiptReq);

        return receiptReq.getReceipt();
    }

    /**
     * Apportions the paid amount by,
     *   Calling the billing service OR by using inbuilt collection apportioner
     *
     * Adds a debit bill account detail against the paid amount     *
     *
     * @param requestInfo Request info of the search
     * @param receipt Receipt that has to be apportioned, looks for bill details & bill account details
     * @return List of bill details post apportioning
     */
    private List<BillDetail> apportionPaidAmount(RequestInfo requestInfo, Receipt receipt) {

        Bill bill = receipt.getBill().get(0);

        Map<Boolean, List<BillDetail>> billDetailsByApportionCallBack = bill.getBillDetails().stream().collect
                (Collectors.partitioningBy(BillDetail::getCallBackForApportioning));

        List<BillDetail> apportionedBillDetails = new ArrayList<>();

        if (!billDetailsByApportionCallBack.get(true).isEmpty()) {
            Bill apportionBill = new Bill(bill.getId(), bill.getPayeeName(),
                    bill.getPayeeAddress(), bill.getPayeeEmail(),
                    bill.getIsActive(), bill.getIsCancelled(), bill.getPaidBy(),
                    new ArrayList<>(), bill.getTenantId(), bill.getMobileNumber());

            BillResponse billResponse = billingServiceRepository.getApportionListFromBillingService(requestInfo, apportionBill);
            apportionedBillDetails.addAll(billResponse.getBill().get(0).getBillDetails());
        }

        if (!billDetailsByApportionCallBack.get(false).isEmpty()) {
            apportionedBillDetails.addAll(CollectionApportionerService.apportionPaidAmount(bill.getBillDetails()));
        }

        addDebitAccountHeadDetails(requestInfo, apportionedBillDetails, receipt.getInstrument());
        return apportionedBillDetails;
    }

    /**
     * For each bill detail representing a module,
     *  - Fetches module specific account code from financial service
     *  - Computes the total apportioned credit amounts & adds a debit
     *          bill account detail against the bill detail
     *
     * @param requestInfo Request info of the search
     * @param billDetails Bill Details
     * @param instrument Instrument for which debit detail should be added
     */
    //TODO Make sure bill details are all from the same tenant in one receipt create request
    private void addDebitAccountHeadDetails(RequestInfo requestInfo, List<BillDetail> billDetails,
                                                         Instrument instrument) {

        String glCode = instrumentRepository.getAccountCodeId(requestInfo, instrument, instrument.getTenantId());
        for(BillDetail billDetail : billDetails) {

            BigDecimal drAmount = BigDecimal.ZERO;

            for(BillAccountDetail billAccountDetail : billDetail.getBillAccountDetails())
                drAmount = drAmount.add(billAccountDetail.getCreditAmount());

            BillAccountDetail billAccountDetail = BillAccountDetail.builder()
                    .id(UUID.randomUUID().toString())
                    .glcode(glCode)
                    .debitAmount(drAmount)
                    .creditAmount(BigDecimal.ZERO)
                    .crAmountToBePaid(BigDecimal.ZERO)
                    .purpose(Purpose.OTHERS)
                    .isActualDemand(false)
                    .tenantId(billDetail.getTenantId())
                    .build();

            billDetail.getBillAccountDetails().add(billAccountDetail);

        }
    }


}
