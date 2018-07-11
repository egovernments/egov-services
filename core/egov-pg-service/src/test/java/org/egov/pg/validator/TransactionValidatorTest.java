package org.egov.pg.validator;

import org.egov.pg.models.Transaction;
import org.egov.pg.repository.TransactionRepository;
import org.egov.pg.service.GatewayService;
import org.egov.pg.web.models.TransactionCriteria;
import org.egov.tracer.model.CustomException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionValidatorTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private GatewayService gatewayService;

    private TransactionValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new TransactionValidator(gatewayService, transactionRepository);
    }

    @Test
    public void validateCreateTxnSuccess() {
        Transaction txn = Transaction.builder().txnAmount("100")
                .orderId("ORDER0012")
                .module("PT")
                .productInfo("Property Tax Payment")
                .gateway("ABCD123")
                .build();

        when(transactionRepository.fetchTransactions(any(TransactionCriteria.class))).thenReturn(Collections.emptyList());
        when(gatewayService.isGatewayActive(txn.getGateway())).thenReturn(true);

        validator.validateCreateTxn(txn);

    }

    /**
     * Duplicatet order id and module combination
     */
    @Test(expected = CustomException.class)
    public void validateCreateTxnDuplicateOrder() {
        Transaction txn = Transaction.builder().txnAmount("100")
                .orderId("ORDER0012")
                .module("PT")
                .productInfo("Property Tax Payment")
                .gateway("ABCD123")
                .build();

        when(transactionRepository.fetchTransactions(any(TransactionCriteria.class))).thenReturn(Collections.singletonList(txn));
        when(gatewayService.isGatewayActive(txn.getGateway())).thenReturn(true);

        validator.validateCreateTxn(txn);

    }

    /**
     * Invalid Gateway, inactive or not available
     */
    @Test(expected = CustomException.class)
    public void validateCreateTxnInvalidGateway() {
        Transaction txn = Transaction.builder().txnAmount("100")
                .orderId("ORDER0012")
                .productInfo("Property Tax Payment")
                .gateway("ABCD123")
                .build();

        when(gatewayService.isGatewayActive(txn.getGateway())).thenReturn(false);

        validator.validateCreateTxn(txn);

    }

    @Test
    public void validateUpdateTxnSuccess() {
        Transaction txnStatus = Transaction.builder().txnId("PT_001")
                .txnAmount("100")
                .orderId("ORDER0012")
                .txnStatus(Transaction.TxnStatusEnum.PENDING)
                .productInfo("Property Tax Payment")
                .gateway("PAYTM")
                .build();

        when(gatewayService.getTxnId(any(Map.class))).thenReturn(Optional.of("PB_PG_001"));
        when(transactionRepository.fetchTransactions(any(TransactionCriteria.class))).thenReturn(Collections.singletonList
                (txnStatus));

        validator.validateUpdateTxn(Collections.singletonMap("transactionId", "PB_PG_001"));

    }

    /**
     * Transaction Id not found in the request params
     */
    @Test(expected = CustomException.class)
    public void validateUpdateTxnIdNotFound() {

        when(gatewayService.getTxnId(any(Map.class))).thenReturn(Optional.empty());

        validator.validateUpdateTxn(Collections.singletonMap("transactionId", "PB_PG_001"));
    }

    /**
     * Invalid Transaction id, not available in our store
     */
    @Test(expected = CustomException.class)
    public void validateUpdateTxnInvalidId() {

        when(gatewayService.getTxnId(any(Map.class))).thenReturn(Optional.of("PB_PG_001"));
        when(transactionRepository.fetchTransactions(any(TransactionCriteria.class))).thenReturn(Collections.emptyList());

        validator.validateUpdateTxn(Collections.singletonMap("transactionId", "PB_PG_001"));
    }

    /**
     * Transaction status already set to completed
     */
    @Test(expected = CustomException.class)
    public void validateUpdateTxnStatusCompleted() {

        when(gatewayService.getTxnId(any(Map.class))).thenReturn(Optional.of("PB_PG_001"));
        when(transactionRepository.fetchTransactions(any(TransactionCriteria.class))).thenReturn(Collections.emptyList());

        validator.validateUpdateTxn(Collections.singletonMap("transactionId", "PB_PG_001"));
    }
}
