package org.egov.egf.master.web.repository;

import org.egov.egf.master.web.contract.BankAccountContract;
import org.egov.egf.master.web.requests.BankAccountResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BankAccountContractRepository {

    private RestTemplate restTemplate;
    private String hostUrl;
    public static final String SEARCH_URL = "/egf-masters/bankaccounts/_search?";

    public BankAccountContractRepository(@Value("${egf.master.host.url}") String hostUrl, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.hostUrl = hostUrl;
    }

    public BankAccountContract findById(BankAccountContract bankAccountContract) {

        String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content = new StringBuffer();
        if (bankAccountContract.getId() != null) {
            content.append("id=" + bankAccountContract.getId());
        }

        if (bankAccountContract.getTenantId() != null) {
            content.append("&tenantId=" + bankAccountContract.getTenantId());
        }
        url = url + content.toString();
        BankAccountResponse result = restTemplate.postForObject(url, null, BankAccountResponse.class);

        if (result.getBankAccounts() != null && result.getBankAccounts().size() == 1) {
            return result.getBankAccounts().get(0);
        } else {
            return null;
        }

    }

    public BankAccountContract findByAccountNumber(BankAccountContract bankAccountContract) {

        String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content = new StringBuffer();
        if (bankAccountContract.getId() != null) {
            content.append("accountNumber=" + bankAccountContract.getAccountNumber());
        }

        if (bankAccountContract.getTenantId() != null) {
            content.append("&tenantId=" + bankAccountContract.getTenantId());
        }
        url = url + content.toString();
        BankAccountResponse result = restTemplate.postForObject(url, null, BankAccountResponse.class);

        if (result.getBankAccounts() != null && result.getBankAccounts().size() == 1) {
            return result.getBankAccounts().get(0);
        } else {
            return null;
        }

    }
}