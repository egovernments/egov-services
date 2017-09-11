package org.egov.collection.persistence.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.collection.web.contract.*;
import org.egov.collection.web.contract.factory.RequestInfoWrapper;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class BankDetailsRepository {

    private RestTemplate restTemplate;

    private String bankAccountUrl;

    private String branchUrl;

    public BankDetailsRepository(final RestTemplate restTemplate,@Value("${egov.egf.masters.hostname}") final String
            egfMastersHost,@Value("${egov.egf.masters.searchbankaccounturl}") final String bankAccountUrl,
                                 @Value("${egov.egf.masters.branchurl}") final String branchUrl) {
        this.restTemplate = restTemplate;
        this.bankAccountUrl = egfMastersHost + bankAccountUrl;
        this.branchUrl = egfMastersHost + branchUrl;
    }

    public List<BankAccount> getBankAccounts(final List<String> accountTypes,final String tenantId,final RequestInfo requestInfo) {
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);
        String bankAccountTypes = String.join(",", accountTypes.get(0));
        log.info("bankAccountUrl: "+bankAccountUrl);
        log.info("tenantid: "+tenantId);
        return restTemplate.postForObject(bankAccountUrl, requestInfoWrapper,
                BankAccountResponse.class,tenantId,bankAccountTypes).getBankAccounts();
    }

    public List<BankBranch> getBankbranches(final List<Long> bankBranchIds,final String bankId,final String tenantId,final boolean active,final RequestInfo requestInfo) {
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);
        Bank bank = Bank.builder().id(bankId).build();
        log.info("branchUrl: "+branchUrl);
        log.info("tenantid: "+tenantId);
        return restTemplate.postForObject(branchUrl, requestInfoWrapper,
                BankBranchResponse.class,tenantId,bankBranchIds,bank,active).getBankBranches();
    }

    public List<BankAccount> searchBankAccounts(final String bankBranchId,final String fundId,final boolean active,final List<String> accountTypes,final String tenantId,final RequestInfo requestInfo) {
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);
        BankBranch bankBranch = BankBranch.builder().id(bankBranchId).build();
        String bankAccountTypes = String.join(",", accountTypes.get(0));
        Fund fund = Fund.builder().id(fundId).build();
        log.info("branchUrl: "+branchUrl);
        log.info("tenantid: "+tenantId);
        return restTemplate.postForObject(branchUrl, requestInfoWrapper,
                BankAccountResponse.class,tenantId,bankBranch,fund,bankAccountTypes,active).getBankAccounts();
    }
}
