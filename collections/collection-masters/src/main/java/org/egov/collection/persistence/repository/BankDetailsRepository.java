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
        log.info("bankAccountUrl: "+bankAccountUrl);
        log.info("tenantid: "+tenantId);
        return restTemplate.postForObject(branchUrl, requestInfoWrapper,
                BankAccountResponse.class,tenantId,bankBranch,fund,bankAccountTypes,active).getBankAccounts();
    }
}
