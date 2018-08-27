package org.egov.pg.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.pg.constants.PgConstants;
import org.egov.pg.constants.TransactionAdditionalFields;
import org.egov.pg.models.AuditDetails;
import org.egov.pg.models.BankAccount;
import org.egov.pg.models.Transaction;
import org.egov.pg.repository.BankAccountRepository;
import org.egov.pg.web.models.TransactionRequest;
import org.egov.pg.web.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.Collections.singletonMap;

@Slf4j
@Service
public class EnrichmentService {

    private IdGenService idGenService;
    private BankAccountRepository bankAccountRepository;

    @Autowired
    EnrichmentService(IdGenService idGenService, BankAccountRepository bankAccountRepository) {
        this.idGenService = idGenService;
        this.bankAccountRepository = bankAccountRepository;
    }

    void enrichCreateTransaction(TransactionRequest transactionRequest) {
        Transaction transaction = transactionRequest.getTransaction();
        RequestInfo requestInfo = transactionRequest.getRequestInfo();

        BankAccount bankAccount = bankAccountRepository.getBankAccountsById(requestInfo, transaction.getTenantId());
        transaction.setAdditionalFields(singletonMap(TransactionAdditionalFields.BANK_ACCOUNT_NUMBER, bankAccount.getAccountNumber()));

        // Generate ID from ID Gen service and assign to txn object
        String txnId = idGenService.generateTxnId(transactionRequest);
        transaction.setTxnId(txnId);
        transaction.setUser(new User(requestInfo.getUserInfo()));
        transaction.setTxnStatus(Transaction.TxnStatusEnum.PENDING);
        transaction.setTxnStatusMsg(PgConstants.TXN_INITIATED);

        // Move callback url to Gateways for now

//        String uri = UriComponentsBuilder
//                .fromHttpUrl(transaction.getCallbackUrl())
//                .queryParams(new LinkedMultiValueMap<>(singletonMap(PgConstants.PG_TXN_IN_LABEL,
//                        Collections.singletonList(txnId))))
//                .build()
//                .toUriString();
//        transaction.setCallbackUrl(uri);

        AuditDetails auditDetails = AuditDetails.builder()
                .createdBy(requestInfo.getUserInfo() != null ? requestInfo.getUserInfo().getUuid() : null)
                .createdTime(System.currentTimeMillis())
                .build();
        transaction.setAuditDetails(auditDetails);
    }

    void enrichUpdateTransaction(TransactionRequest transactionRequest, Transaction newTxn) {
        RequestInfo requestInfo = transactionRequest.getRequestInfo();
        Transaction currentTxnStatus = transactionRequest.getTransaction();

        AuditDetails auditDetails = AuditDetails.builder()
                .createdBy(currentTxnStatus.getAuditDetails().getCreatedBy())
                .createdTime(currentTxnStatus.getAuditDetails().getCreatedTime())
                .lastModifiedBy(requestInfo.getUserInfo() != null ? requestInfo.getUserInfo().getUuid() : null)
                .lastModifiedTime(System.currentTimeMillis()).build();
        newTxn.setAuditDetails(auditDetails);

        newTxn.setTxnId(currentTxnStatus.getTxnId());
        newTxn.setGateway(currentTxnStatus.getGateway());
        newTxn.setModule(currentTxnStatus.getModule());
        newTxn.setModuleId(currentTxnStatus.getModuleId());
        newTxn.setBillId(currentTxnStatus.getBillId());
        newTxn.setProductInfo(currentTxnStatus.getProductInfo());
        newTxn.setTenantId(currentTxnStatus.getTenantId());
        newTxn.setUser(currentTxnStatus.getUser());

    }

}
