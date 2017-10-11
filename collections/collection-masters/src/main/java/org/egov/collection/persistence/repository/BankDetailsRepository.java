/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.collection.persistence.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.collection.web.contract.*;
import org.egov.collection.web.contract.factory.RequestInfoWrapper;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BankDetailsRepository {

    private RestTemplate restTemplate;

    private String bankBranchUrl;

    private String bankAccountByBankBranchUrl;

    private String bankAccountByIdUrl;

    private String getAllBankAccountsUrl;

    public BankDetailsRepository(final RestTemplate restTemplate,@Value("${egov.egf.masters.hostname}") final String egfMastersHost,
                                 @Value("${egov.egf.masters.searchallbankAccounts}") final String getAllBankAccountsUrl,
                                 @Value("${egov.egf.masters.searchbankaccountbyidurl}") final String bankAccountByIdUrl,
                                 @Value("${egov.egf.masters.searchbankaccounbybankbranchturl}") final String bankAccountByBankBranchUrl,
                                 @Value("${egov.egf.masters.searchbankbranch}") final String bankBranchUrl) {
        this.restTemplate = restTemplate;
        this.getAllBankAccountsUrl = egfMastersHost + getAllBankAccountsUrl;
        this.bankAccountByBankBranchUrl = egfMastersHost + bankAccountByBankBranchUrl;
        this.bankBranchUrl = egfMastersHost + bankBranchUrl;
        this.bankAccountByIdUrl = egfMastersHost + bankAccountByIdUrl;
    }

    public List<Bank> getAllBankHavingBranchAndAccounts(final String tenantId,final RequestInfo requestInfo) {
        List<BankAccount> bankAccounts = getAllBankAccounts(tenantId,requestInfo);
        List<BankBranch> bankBranchList = bankAccounts.stream().map(ba -> ba.getBankBranch()).collect(Collectors.toList());
        List<Long> bankBranchIds = new ArrayList<>();
        for(BankBranch bankBranch : bankBranchList)
            bankBranchIds.add(Long.valueOf(bankBranch.getId()));
        List<BankBranch> bankBranches = getBankbranches(bankBranchIds,null,tenantId,true,requestInfo);
        return bankBranches.stream().filter(b -> b.getBank().getId() != null).map(bb -> bb.getBank()).collect(Collectors.toList());
    }

    public List<BankAccount> getAllBankAccounts(final String tenantId,final RequestInfo requestInfo) {
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);
        log.info("All bankaccounts: "+getAllBankAccountsUrl);
        log.info("tenantid: "+tenantId);
        return restTemplate.postForObject(getAllBankAccountsUrl, requestInfoWrapper,
                BankAccountResponse.class,tenantId).getBankAccounts();
    }

    public List<BankAccount> getBankAccountsById(final List<Long> bankAccountIds,final String tenantId,final RequestInfo requestInfo) {
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);
        log.info("bankAccount by id: "+bankAccountByIdUrl);
        log.info("tenantid: "+tenantId);
        return restTemplate.postForObject(bankAccountByIdUrl, requestInfoWrapper,
                BankAccountResponse.class,tenantId,bankAccountIds,bankAccountByIdUrl).getBankAccounts();
    }

    public List<BankBranch> getBankbranches(final List<Long> bankBranchIds,final String bankId,final String tenantId,final boolean active,final RequestInfo requestInfo) {
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);
        Bank bank = Bank.builder().id(bankId).build();
        log.info("bankbranch by id Url: "+bankBranchUrl);
        log.info("tenantid: "+tenantId);
        return restTemplate.postForObject(bankBranchUrl, requestInfoWrapper,
                BankBranchResponse.class,tenantId,bankBranchIds,bank,active).getBankBranches();
    }

    public List<BankAccount> searchBankAccounts(final BankAccountSearchRequest bankAccountSearchRequest,final String fundId,final List<String> accountTypes,final RequestInfo requestInfo) {
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);
        String bankBranchId = bankAccountSearchRequest.getBankBranchId();
        String tenantId = bankAccountSearchRequest.getTenantId();
        Long fund = Long.valueOf(fundId);
        boolean isActive = bankAccountSearchRequest.isActive();
        BankBranch bankBranch = BankBranch.builder().id(bankBranchId).build();
        String bankAccountTypes = String.join(",", accountTypes.get(0));
        log.info("Bank Account by branch: "+bankAccountByBankBranchUrl);
        log.info("tenantid: "+tenantId);
        return restTemplate.postForObject(bankAccountByBankBranchUrl, requestInfoWrapper,
                BankAccountResponse.class,tenantId,bankBranch,fund,bankAccountTypes,isActive).getBankAccounts();
    }


}
