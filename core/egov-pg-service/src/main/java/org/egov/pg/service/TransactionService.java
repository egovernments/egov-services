package org.egov.pg.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.pg.config.AppProperties;
import org.egov.pg.models.GatewayStatus;
import org.egov.pg.models.Transaction;
import org.egov.pg.models.TransactionDump;
import org.egov.pg.models.TransactionDumpRequest;
import org.egov.pg.producer.Producer;
import org.egov.pg.repository.TransactionRepository;
import org.egov.pg.web.models.RequestInfo;
import org.egov.pg.web.models.TransactionCriteria;
import org.egov.pg.web.models.TransactionRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.util.*;

/**
 * Handles all transaction related requests
 */
@Service
@Slf4j
public class TransactionService {

    private List<Gateway> gateways;
    private Map<String, GatewayStatus> gatewayMap = new HashMap<>();
    private Set<String> transactionIdKeyset = new HashSet<>();
    private Producer producer;
    private IdGenService idGenService;
    private AppProperties appProperties;
    private TransactionRepository transactionRepository;


    @Autowired
    TransactionService(List<Gateway> gateways, Producer producer, TransactionRepository transactionRepository,
                              IdGenService idGenService,
                              AppProperties appProperties) {
        this.gateways = gateways;
        this.producer = producer;
        this.transactionRepository = transactionRepository;
        this.idGenService = idGenService;
        this.appProperties = appProperties;
        initialize();
    }

    /**
     * Initiates a transaction by generating a gateway redirect URI for the request
     * <p>
     * 1. Enriches the request by assigning a TxnID and a default status of PENDING
     * 2. Verifies if the selected gateway is valid & active
     * 3. If yes, calls the gateway's implementation to generate a redirect URI
     * 4. Persists the transaction and a transaction dump with the RAW requests asynchronously
     * 5. Returns the redirect URI
     *
     * @param transactionRequest Valid transaction request for which transaction needs to be initiated
     * @return Redirect URI to the gateway for the particular transaction
     */
    public URI initiateTransaction(TransactionRequest transactionRequest) {
        Transaction transaction = transactionRequest.getTransaction();

        // Check if chosen gateway is valid & active
        if (gatewayMap.containsKey(transaction.getGateway()) && gatewayMap.get(transaction.getGateway()).isActive()) {
            Gateway gateway = gatewayMap.get(transaction.getGateway()).getGateway();

            // Generate ID from ID Gen service and assign to txn object
            String txnId = idGenService.generateTxnId(transactionRequest);
            transaction.setTxnId(txnId);
            transaction.setTxnStatus(Transaction.TxnStatusEnum.PENDING);
            transaction.setCreatedTime(System.currentTimeMillis());

            URI uri = gateway.generateRedirectURI(transaction);

            TransactionDump dump = new TransactionDump(transaction.getTxnId(), uri.toString(), null, System
                    .currentTimeMillis
                            (), null);


            // Persist transaction and transaction dump objects
            producer.push(appProperties.getSaveTxnTopic(), new org.egov.pg.models.TransactionRequest
                    (transactionRequest.getRequestInfo(), transaction));
            producer.push(appProperties.getSaveTxnDumpTopic(), new TransactionDumpRequest(transactionRequest
                    .getRequestInfo(), dump));

            return uri;
        } else {
            throw new CustomException("INVALID_PAYMENT_GATEWAY", "Invalid or inactive payment gateway provided");
        }
    }


    /**
     * Fetches a list of transactions matching the current criteria
     * <p>
     * Currently has a hard limit of 5 records, configurable
     *
     * @param transactionCriteria Search Conditions that should be matched
     * @return List of transactions matching the conditions.
     */
    public List<Transaction> getTransactions(TransactionCriteria transactionCriteria) {
        log.info(transactionCriteria.toString());
        transactionCriteria.setOffset(0);
        transactionCriteria.setLimit(5);
        try {
            return transactionRepository.fetchTransactions(transactionCriteria);
        }catch (DataAccessException e){
            log.error("Unable to fetch data from the database for criteria: "+transactionCriteria.toString(), e);
            throw new CustomException("FETCH_TXNS_FAILED", "Unable to fetch transactions from store");
        }
    }

    /**
     * Updates the status of the transaction from the gateway
     * <p>
     * 1. Fetch TXN ID from the request params, if not found, exit!
     * 2. Fetch current transaction status from DB, if not found, exit!
     * 3. Fetch the current transaction status from the payment gateway
     * 4. Verify the amount returned from the gateway matches our records
     * 5. Persist the updated transaction status and raw gateway transaction response
     *
     * @param requestInfo
     * @param requestParams Response parameters posted by the gateway
     * @return Updated transaction
     */
    public Transaction updateTransaction(RequestInfo requestInfo, Map<String, String> requestParams) {

        Optional<String> transactionOptional = fetchTransactionIdFromRequest(requestParams);

        if (!transactionOptional.isPresent())
            throw new CustomException("MISSING_TXN_ID", "Cannot process request, missing transaction id");

        String txnId = transactionOptional.get();


        List<Transaction> statuses = getTransactions(TransactionCriteria.builder().txnId
                (txnId).tenantId(requestParams.get("tenantId")).build
                ());

        //TODO Add to error queue
        if (statuses.isEmpty()) {
            throw new CustomException("TXN_NOT_FOUND", "Transaction not found");
        }

        Transaction currentTxnStatus = statuses.get(0);

        if(!currentTxnStatus.getTxnStatus().equals(Transaction.TxnStatusEnum.PENDING))
            throw new CustomException("TXN_ALREADY_COMPLETED", "Transaction has already reached completion, check " +
                    "status");

        Gateway gateway = gatewayMap.get(currentTxnStatus.getGateway()).getGateway();

        log.info(currentTxnStatus.toString());
        log.info(requestParams.toString());

        Transaction newTxn = gateway.fetchStatus(currentTxnStatus, requestParams);

        if (new BigDecimal(currentTxnStatus.getTxnAmount()).compareTo(new BigDecimal(newTxn.getTxnAmount())) != 0) {
            log.error("Transaction Amount mismatch, expected {} got {}", currentTxnStatus.getTxnAmount(), newTxn.getTxnAmount());
            newTxn.setTxnStatus(Transaction.TxnStatusEnum.FAILURE);
        }

        populateBaseTxnInfo(currentTxnStatus, newTxn);

        //TODO Amount should match

        TransactionDump dump = TransactionDump.builder()
                .txnId(currentTxnStatus.getTxnId())
                .txnResponse(newTxn.getResponseJson())
                .lastModifiedTime(newTxn.getLastModifiedTime())
                .build();

        producer.push(appProperties.getUpdateTxnTopic(), new org.egov.pg.models.TransactionRequest(requestInfo, newTxn));
        producer.push(appProperties.getUpdateTxnDumpTopic(), new TransactionDumpRequest(requestInfo, dump));


        return newTxn;
    }

    /**
     * Returns the active payment gateways
     *
     * @return list of active gateways that can be used for payments
     */
    public Set<String> activeGateways() {
        return gatewayMap.keySet();
    }

    /**
     * Get the transaction id from the raw request
     * <p>
     * The TXNID key can be anything, as this is gateway specific,
     * *  ex., txnid, vpc_txnid etc,
     * check all payment gateway specific keys for txnid and returns txnid
     *
     * @param params Request parameters
     * @return Transaction id
     */
    private Optional<String> fetchTransactionIdFromRequest(Map<String, String> params) {

        Map<String, String> caseInsensitiveMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        caseInsensitiveMap.putAll(params);
        for (String txnId : transactionIdKeyset) {
            if (caseInsensitiveMap.containsKey(txnId))
                return Optional.of(caseInsensitiveMap.get(txnId));
        }

        return Optional.empty();
    }

    private void populateBaseTxnInfo(Transaction currentTxn, Transaction newTxn) {
        newTxn.setTxnId(currentTxn.getTxnId());
        newTxn.setGateway(currentTxn.getGateway());
        newTxn.setCreatedTime(currentTxn.getCreatedTime());
        newTxn.setModule(currentTxn.getModule());
        newTxn.setOrderId(currentTxn.getOrderId());
        newTxn.setProductInfo(currentTxn.getProductInfo());
        newTxn.setTenantId(currentTxn.getTenantId());
        newTxn.setUser(currentTxn.getUser());
    }


    private void initialize() {
        if (Objects.isNull(gateways))
            throw new IllegalStateException("No gateways found, spring initialization failed.");

        if (!gateways.isEmpty() && gatewayMap.isEmpty())
            gateways.forEach(gateway -> {
                gatewayMap.put(gateway.gatewayName().toUpperCase(), new GatewayStatus(gateway, gateway.isActive()));
                transactionIdKeyset.add(gateway.transactionIdKeyInResponse());
            });

        log.info(gatewayMap.toString());
        log.info(transactionIdKeyset.toString());
    }

}
