package org.egov.pg.service.jobs.dailyReconciliation;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.pg.models.Transaction;
import org.egov.pg.service.TransactionService;
import org.egov.pg.web.models.TransactionCriteria;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

/**
 * Daily Reconciliation of pending transactions
 */
@Slf4j
public class DailyReconciliationJob implements Job {

    private static final RequestInfo requestInfo;

    static {
        User userInfo = User.builder().uuid("DAILY_RECONC_JOB").roles(Collections.emptyList()).id(0L).build();
        requestInfo = new RequestInfo("", "", 0L, "", "", "", "", "", "", userInfo);
    }

    @Autowired
    private TransactionService transactionService;

    /**
     * Fetch live status for pending transactions
     *
     * @param jobExecutionContext execution context with optional job parameters
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        List<Transaction> pendingTxns = transactionService.getTransactions(TransactionCriteria.builder().txnStatus(Transaction
                .TxnStatusEnum.PENDING)
                .build());

        log.info(pendingTxns.toString());

        for (Transaction txn : pendingTxns) {
            log.info(transactionService.updateTransaction(requestInfo, Collections.singletonMap("transactionId", txn.getTxnId
                    ())).toString());
        }

    }
}
