package org.egov.pg.web.controllers;


import com.google.common.net.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.egov.pg.models.Transaction;
import org.egov.pg.service.GatewayService;
import org.egov.pg.service.TransactionService;
import org.egov.pg.utils.ResponseInfoFactory;
import org.egov.pg.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Endpoints to deal with all payment related operations
 */

@Slf4j
@Controller
public class TransactionsApiController {

    private final TransactionService transactionService;
    private final GatewayService gatewayService;


    @Autowired
    public TransactionsApiController(TransactionService transactionService, GatewayService
            gatewayService) {
        this.transactionService = transactionService;
        this.gatewayService = gatewayService;
    }


    /**
     * Initiates a new payment transaction, on successful validation, a redirect is issued to the payment gateway.
     *
     * @param transactionRequest Request containing all information necessary for initiating payment
     * @return 302 Redirect to the payment gateway
     */
    @RequestMapping(value = "/transactions/v1/_create", method = RequestMethod.POST)
    public ResponseEntity<Void> transactionsV1CreatePost(@Valid @RequestBody TransactionRequest transactionRequest) {

        URI uri = transactionService.initiateTransaction(transactionRequest);
        return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, uri.toString()).build();
    }


    /**
     * Returns the current status of a transaction in our systems;
     * This does not guarantee live payment gateway status.
     *
     * @param requestInfoWrapper  Request Info
     * @param transactionCriteria Search Conditions that should be matched
     * @return List of transactions matching the search criteria
     */
    @RequestMapping(value = "/transactions/v1/_search", method = RequestMethod.POST)
    public ResponseEntity<TransactionResponse> transactionsV1SearchPost(@Valid @RequestBody RequestInfoWrapper
                                                                                requestInfoWrapper, @Valid
                                                                        @ModelAttribute TransactionCriteria transactionCriteria) {
        List<Transaction> transactions = transactionService.getTransactions(transactionCriteria);
        ResponseInfo responseInfo = ResponseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper
                .getRequestInfo(), true);
        TransactionResponse response = new TransactionResponse(responseInfo, transactions);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Updates the status of the transaction from the gateway
     *
     * @param params             Parameters posted by the gateway
     * @return The current transaction status of the tranasction
     */
    @RequestMapping(value = "/transactions/v1/_update", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<TransactionResponse> transactionsV1UpdatePost( @RequestParam Map<String, String> params) {

        RequestInfo requestInfo = new RequestInfo();
        Transaction status = transactionService.updateTransaction(requestInfo, params);

        return new ResponseEntity<>(new TransactionResponse(ResponseInfoFactory
                .createResponseInfoFromRequestInfo(requestInfo, true), Collections
                .singletonList(status)), HttpStatus.OK);
    }


    /**
     * Active payment gateways that can be used for payments
     *
     * @return list of active gateways that can be used for payments
     */
    @RequestMapping(value = "/gateways/v1/_search", method = RequestMethod.POST)
    public ResponseEntity<Set<String>> transactionsV1AvailableGatewaysPost() {

        Set<String> gateways = gatewayService.getActiveGateways();
        log.debug("Available gateways : " + gateways);
        return new ResponseEntity<>(gateways, HttpStatus.OK);
    }

}
