package org.egov.pg.validator;

import lombok.extern.slf4j.Slf4j;
import org.egov.pg.models.Transaction;
import org.egov.pg.repository.TransactionRepository;
import org.egov.pg.service.GatewayService;
import org.egov.pg.web.models.TransactionCriteria;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class TransactionValidator {

    private GatewayService gatewayService;

    private TransactionRepository transactionRepository;

    @Autowired
    public TransactionValidator(GatewayService gatewayService, TransactionRepository transactionRepository){
        this.gatewayService = gatewayService;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Validate the transaction,
     *      Check if gateway is available and active
     *      Check if module specific order id is unique
     *
     * @param transaction txn object to be validated
     */
    public void validateCreateTxn(Transaction transaction){
        Map<String , String > errorMap = new HashMap<>();
        isGatewayActive(transaction, errorMap);
        doesOrderExist(transaction, errorMap);

        if(!errorMap.isEmpty())
            throw new CustomException(errorMap);

    }

    public Transaction validateUpdateTxn(Map<String, String> requestParams){

        Optional<String> optional = gatewayService.getTxnId(requestParams);

        if(!optional.isPresent())
            throw new CustomException("MISSING_TXN_ID", "Cannot process request, missing transaction id");

        TransactionCriteria criteria = TransactionCriteria.builder()
                .txnId(optional.get())
                .build();

        List<Transaction> statuses = transactionRepository.fetchTransactions(criteria);

        //TODO Add to error queue
        if (statuses.isEmpty()) {
            throw new CustomException("TXN_NOT_FOUND", "Transaction not found");
        }

        Transaction currentTxnStatus = statuses.get(0);

        if (!currentTxnStatus.getTxnStatus().equals(Transaction.TxnStatusEnum.PENDING))
            throw new CustomException("TXN_ALREADY_COMPLETED", "Transaction has already reached completion, check " +
                    "status");

        return currentTxnStatus;
    }


    private void isGatewayActive(Transaction transaction, Map<String, String> errorMap){
        if(!gatewayService.isGatewayActive(transaction.getGateway()))
            errorMap.put("INVALID_PAYMENT_GATEWAY", "Invalid or inactive payment gateway provided");
    }

    private void doesOrderExist(Transaction transaction, Map<String, String> errorMap){
        TransactionCriteria criteria = TransactionCriteria.builder()
                .orderId(transaction.getOrderId())
                .module(transaction.getModule())
                .build();

        if(!transactionRepository.fetchTransactions(criteria).isEmpty())
            errorMap.put("ORDER_EXISTS", "Combination Order ID and module must be unique");
    }

}
