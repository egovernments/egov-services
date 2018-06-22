package org.egov.pg.service.jobs.earlyReconciliation;

import lombok.extern.slf4j.Slf4j;
import org.egov.pg.models.Transaction;
import org.egov.pg.service.TransactionService;
import org.egov.pg.web.models.RequestInfo;
import org.egov.pg.web.models.TransactionCriteria;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Updates all transactions in pending state and created in the last 15 minutes
 */
@Slf4j
public class EarlyReconciliationJob implements Job {
    private static final RequestInfo requestInfo;

    static {
        requestInfo = new RequestInfo();
    }

    @Autowired
    private TransactionService transactionService;

    /**
     * Fetch live status for pending transactions
     *
     * @param jobExecutionContext execution context with optional job parameters
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        List<Transaction> pendingTxns = transactionService.getTransactions(TransactionCriteria.builder()
                .txnStatus(Transaction.TxnStatusEnum.PENDING)
                .createdTime(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(15))
                .build());

        log.info(pendingTxns.toString());

        for (Transaction txn : pendingTxns) {
            log.info(transactionService.updateTransaction(requestInfo, Collections.singletonMap("transactionId", txn.getTxnId
                    ())).toString());
        }

    }

}
