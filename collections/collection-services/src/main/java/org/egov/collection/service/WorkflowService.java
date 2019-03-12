package org.egov.collection.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.model.ReceiptSearchCriteria;
import org.egov.collection.model.enums.InstrumentStatusEnum;
import org.egov.collection.model.enums.ReceiptStatus;
import org.egov.collection.producer.CollectionProducer;
import org.egov.collection.repository.CollectionRepository;
import org.egov.collection.util.WorkflowValidator;
import org.egov.collection.web.contract.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static java.util.Collections.reverseOrder;
import static java.util.Collections.singleton;
import static java.util.Objects.isNull;
import static org.egov.collection.config.CollectionServiceConstants.VOUCHER_HEADER_KEY;
import static org.egov.collection.model.enums.InstrumentStatusEnum.DEPOSITED;
import static org.egov.collection.model.enums.InstrumentTypesEnum.*;
import static org.egov.collection.util.Utils.jsonMerge;

@Service
@Slf4j
public class WorkflowService {
    private CollectionRepository collectionRepository;
    private WorkflowValidator workflowValidator;
    private CollectionProducer collectionProducer;
    private ApplicationProperties applicationProperties;

    @Autowired
    public WorkflowService(CollectionRepository collectionRepository, WorkflowValidator
            workflowValidator, CollectionProducer collectionProducer, ApplicationProperties applicationProperties) {
        this.collectionRepository = collectionRepository;
        this.workflowValidator = workflowValidator;
        this.collectionProducer = collectionProducer;
        this.applicationProperties = applicationProperties;

    }

    /**
     * Processes all workflow related requests
     *  - Verify that all workflow actions in the request are same
     *  - Handover processing of actions to sub methods
     *  - Persist the updated receipts
     *
     * @param receiptWorkflowRequest multiple actions
     * @return updated receipts
     */
    @Transactional
    public List<Receipt> performWorkflow(ReceiptWorkflowRequest receiptWorkflowRequest){

        ReceiptWorkflow.ReceiptAction action = receiptWorkflowRequest.getReceiptWorkflow().get(0).getAction();
        Set<String> consumerCodes = new HashSet<>();
        Map<String, ReceiptWorkflow> workflowRequestByReceiptNumber = new HashMap<>();

        for(ReceiptWorkflow workflow : receiptWorkflowRequest.getReceiptWorkflow()){
            if(!workflow.getAction().equals(action))
                throw new CustomException("RECEIPT_WORKFLOW_SINGLE_ACTION_ALLOWED", "All workflow requests should be " +
                        "of the same action");

            consumerCodes.add(workflow.getConsumerCode());
            workflowRequestByReceiptNumber.put(workflow.getReceiptNumber(), workflow);
        }

        List<Receipt> processedReceipts = new ArrayList<>();

        switch (action){
            case CANCEL:
                processedReceipts = cancel(workflowRequestByReceiptNumber, consumerCodes, receiptWorkflowRequest.getRequestInfo());
                break;
            case DISHONOUR:
                processedReceipts = dishonour(workflowRequestByReceiptNumber, consumerCodes, receiptWorkflowRequest.getRequestInfo());
                break;
            case REMIT:
                processedReceipts = remit(workflowRequestByReceiptNumber, consumerCodes, receiptWorkflowRequest.getRequestInfo());
                break;
        }

        // persist to db

        collectionRepository.updateStatus(processedReceipts);

        return processedReceipts;


    }

    /**
     * Performs cancellation of eligible receipts
     * Eligibility is defined by current status of the receipt
     * Latest receipt of a consumer code with status of APPROVED [or initial state] can be cancelled
     *
     *  - Fetch receipts eligible for cancellation by consumer codes & order by receipt date desc
     *  - Validate workflow requests for cancellation
     *  - If validated, update receipts as per the request
     *      else, throws exception and exit
     *
     * @param workflowRequestByReceiptNumber map workflow request to receipt number
     * @param consumerCodes Consumer codes of all the requests
     * @param requestInfo request info of the incoming request
     * @return updated receipts
     */
    private List<Receipt> cancel(Map<String, ReceiptWorkflow> workflowRequestByReceiptNumber, Set<String> consumerCodes,
                                 RequestInfo requestInfo){
        ReceiptSearchCriteria receiptSearchCriteria = ReceiptSearchCriteria
                .builder()
                .consumerCode(consumerCodes)
                .status(singleton(ReceiptStatus.APPROVED.toString()))
                .build();

        List<Receipt> receipts = collectionRepository.fetchReceipts(receiptSearchCriteria);
        receipts.sort(reverseOrder(Comparator.comparingLong(Receipt::getReceiptDate)));

        List<Receipt> validatedReceipts = workflowValidator.validateForCancel(new ArrayList<>
                        (workflowRequestByReceiptNumber.values()), receipts);

        for(Receipt receipt : validatedReceipts) {
            BillDetail billDetail = receipt.getBill().get(0).getBillDetails().get(0);
            billDetail.setStatus(ReceiptStatus.CANCELLED.toString());
            billDetail.setReasonForCancellation(workflowRequestByReceiptNumber.get(receipt.getReceiptNumber()).getReason());
            billDetail.setAdditionalDetails(jsonMerge(billDetail.getAdditionalDetails(),
                    workflowRequestByReceiptNumber.get(receipt.getReceiptNumber()).getAdditionalDetails()));
            receipt.getInstrument().setInstrumentStatus(InstrumentStatusEnum.CANCELLED);

            updateAuditDetails(receipt, requestInfo);
        }

        collectionRepository.updateStatus(validatedReceipts);
        collectionProducer.producer(applicationProperties.getCancelReceiptTopicName(), applicationProperties
                .getCancelReceiptTopicKey(), new ReceiptReq(requestInfo, validatedReceipts));

        return validatedReceipts;
    }

    /**
     * Performs dishonoring of eligible receipts
     * Eligibility is defined by current status of the receipt & mode of payment
     * Latest receipt of a consumer code with status of APPROVED [or initial state], REMITTED & payment mode CHEQUE, DD can be dishonored
     *
     *  - Fetch receipts eligible for dishonoring by consumer codes & order by receipt date desc
     *  - Validate requests for dishonoring
     *  - If validated, update receipts as per the request
     *      else, throws exception and exit
     *
     * @param workflowRequestByReceiptNumber map workflow request to receipt number
     * @param consumerCodes Consumer codes of all the requests
     * @param requestInfo request info of the incoming request
     * @return updated receipts
     */
    private List<Receipt> dishonour(Map<String, ReceiptWorkflow> workflowRequestByReceiptNumber, Set<String> consumerCodes,
                                    RequestInfo requestInfo){
        Set<String> status = new HashSet<>();
        status.add(ReceiptStatus.APPROVED.toString());
        status.add(ReceiptStatus.REMITTED.toString());

        Set<String> instrumentTypes = new HashSet<>();
        instrumentTypes.add(CHEQUE.toString());
        instrumentTypes.add(DD.toString());

        ReceiptSearchCriteria receiptSearchCriteria = ReceiptSearchCriteria
                .builder()
                .consumerCode(consumerCodes)
                .instrumentType(instrumentTypes)
                .status(status)
                .build();

        List<Receipt> receipts = collectionRepository.fetchReceipts(receiptSearchCriteria);
        receipts.sort(reverseOrder(Comparator.comparingLong(Receipt::getReceiptDate)));

        List<Receipt> validatedReceipts = workflowValidator.validateForCancel(new ArrayList<>(workflowRequestByReceiptNumber.values()), receipts);

        for(Receipt receipt : validatedReceipts) {
            BillDetail billDetail = receipt.getBill().get(0).getBillDetails().get(0);
            billDetail.setStatus(ReceiptStatus.REJECTED.toString());
            billDetail.setReasonForCancellation(workflowRequestByReceiptNumber.get(receipt.getReceiptNumber()).getReason());
            billDetail.setAdditionalDetails(jsonMerge(billDetail.getAdditionalDetails(),
                    workflowRequestByReceiptNumber.get(receipt.getReceiptNumber()).getAdditionalDetails()));
            receipt.getInstrument().setInstrumentStatus(InstrumentStatusEnum.DISHONOURED);

            updateAuditDetails(receipt, requestInfo);
        }

        collectionRepository.updateStatus(validatedReceipts);
        collectionProducer.producer(applicationProperties.getCancelReceiptTopicName(), applicationProperties
                .getCancelReceiptTopicKey(), new ReceiptReq(requestInfo, validatedReceipts));
        return validatedReceipts;
    }


    /**
     * Performs remittance of eligible receipts
     * Eligibility is defined by current status of the receipt & mode of payment
     * Receipts with status of APPROVED [or initial state] & payment mode CHEQUE, DD, CASH can be remitted
     *
     *
     *  - Fetch receipts eligible for remittance by consumer codes
     *  - Validate requests for remittance
     *  - If validated, update receipts as per the request
     *      else, throws exception and exit
     *
     * @param workflowRequestByReceiptNumber map workflow request to receipt number
     * @param consumerCodes Consumer codes of all the requests
     * @param requestInfo request info of the incoming request
     * @return updated receipts
     */
    private List<Receipt> remit(Map<String, ReceiptWorkflow> workflowRequestByReceiptNumber, Set<String> consumerCodes,
                                RequestInfo requestInfo){

        Set<String> instrumentTypes = new HashSet<>();
        instrumentTypes.add(CASH.toString());
        instrumentTypes.add(CHEQUE.toString());
        instrumentTypes.add(DD.toString());

        ReceiptSearchCriteria receiptSearchCriteria = ReceiptSearchCriteria
                .builder()
                .consumerCode(consumerCodes)
                .status(singleton(ReceiptStatus.APPROVED.toString()))
                .instrumentType(instrumentTypes)
                .build();

        List<Receipt> receipts = collectionRepository.fetchReceipts(receiptSearchCriteria);

        List<Receipt> validatedReceipts = workflowValidator.validateForRemit(new ArrayList<>(workflowRequestByReceiptNumber.values()), receipts);

        for(Receipt receipt : validatedReceipts) {
            JsonNode additionalDetails = workflowRequestByReceiptNumber.get(receipt.getReceiptNumber())
                    .getAdditionalDetails();
            BillDetail billDetail = receipt.getBill().get(0).getBillDetails().get(0);
            billDetail.setStatus(ReceiptStatus.REMITTED.toString());
            billDetail.setAdditionalDetails(jsonMerge(billDetail.getAdditionalDetails(),
                    additionalDetails));

            if( ! isNull(additionalDetails) && ! additionalDetails.isNull() && additionalDetails.has(VOUCHER_HEADER_KEY))
                billDetail.setVoucherHeader(additionalDetails.get(VOUCHER_HEADER_KEY).asText());

            receipt.getInstrument().setInstrumentStatus(DEPOSITED);

            updateAuditDetails(receipt, requestInfo);
        }

        collectionRepository.updateStatus(validatedReceipts);
        collectionProducer.producer(applicationProperties.getUpdateReceiptTopicName(), applicationProperties
                .getUpdateReceiptTopicKey(), new ReceiptReq(requestInfo, validatedReceipts));
        return validatedReceipts;
    }

    /**
     * Update audit details as per request
     *
     * @param receipt which is being modified
     * @param requestInfo info about the request
     */
    public static void updateAuditDetails(Receipt receipt, RequestInfo requestInfo){
        receipt.getAuditDetails().setLastModifiedBy(requestInfo.getUserInfo().getId().toString());
        receipt.getAuditDetails().setLastModifiedDate(System.currentTimeMillis());

        receipt.getInstrument().getAuditDetails().setLastModifiedBy(requestInfo.getUserInfo().getId().toString());
        receipt.getInstrument().getAuditDetails().setLastModifiedDate(System.currentTimeMillis());
    }

}
