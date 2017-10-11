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
package org.egov.collection.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.egov.collection.persistence.repository.BankDetailsRepository;
import org.egov.collection.persistence.repository.BusinessDetailsRepository;
import org.egov.collection.service.BankAccountMappingService;
import org.egov.collection.web.contract.*;
import org.egov.collection.web.contract.enums.BankAccountType;
import org.egov.collection.web.contract.factory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/bankAccountServiceMapping")
@Slf4j
public class CommonMastersController {

    @Autowired
    private BankDetailsRepository bankDetailsRepository;

    @Autowired
    private BusinessDetailsRepository businessDetailsRepository;

    @Autowired
    private BankAccountMappingService bankAccountMappingService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;


    @RequestMapping("/_searchBanks")
    @PostMapping
    public ResponseEntity<?> searchBanks(@ModelAttribute String tenantId, @RequestBody RequestInfo requestInfo,
                                                final BindingResult modelAttributeBindingResult) {
        List<Bank> banksList = bankDetailsRepository.getAllBankHavingBranchAndAccounts(tenantId, requestInfo);
        return getBanksSuccessResponse(banksList, requestInfo);
    }

    @RequestMapping("/_searchBranch")
    @PostMapping
    public ResponseEntity<?> searchBankBranches(@ModelAttribute BankBranchSearchRequest bankBranchSearchRequest, @RequestBody RequestInfo requestInfo,
                                                final BindingResult modelAttributeBindingResult) {

       List<String> accountTypes = Arrays.stream(BankAccountType.values()).map(Enum::name)
                    .collect(Collectors.toList());
        BankAccountSearchRequest bankAccountSearchRequest = BankAccountSearchRequest.builder()
                .tenantId(bankBranchSearchRequest.getTenantId()).active(true).build();

        List<BankAccount> bankAccounts = bankDetailsRepository.searchBankAccounts(bankAccountSearchRequest, null, accountTypes, requestInfo);
        List<Long> bankBranchIds = new ArrayList<>();
        for(BankAccount bankAccount:bankAccounts)
            bankBranchIds.add(Long.valueOf(bankAccount.getBankBranch().getId()));
        List<BankBranch> bankBranchList = bankDetailsRepository.getBankbranches(bankBranchIds, bankBranchSearchRequest.getBankId(), bankBranchSearchRequest.getTenantId(), true, requestInfo);
        return getBankBranchResponse(bankBranchList,requestInfo);
    }

    @RequestMapping("/_searchBankAccounts")
    @PostMapping
    public ResponseEntity<?> searchBankAccounts(@ModelAttribute final BankAccountSearchRequest bankAccountSearchRequest, @RequestBody RequestInfo requestInfo,
                                                final BindingResult modelAttributeBindingResult) {
        List<BusinessDetailsRequestInfo> businessDetails = businessDetailsRepository.getBusinessDetails(
                Arrays.asList(Long.valueOf(bankAccountSearchRequest.getBusinessDetailsId())),bankAccountSearchRequest.getTenantId(),requestInfo);
        List<String> accountTypes = Arrays.stream(BankAccountType.values()).map(Enum::name)
                .collect(Collectors.toList());
        List<BankAccount> bankAccounts = bankDetailsRepository.searchBankAccounts(bankAccountSearchRequest,businessDetails.get(0).getFund(),accountTypes,requestInfo);
        return getBankAccontResponse(bankAccounts, requestInfo);
    }

    @RequestMapping("/_searchBankBranches")
    @PostMapping
    public ResponseEntity<?> searchBankBranches(@RequestParam String tenantId, @RequestBody RequestInfo requestInfo,
                                         final BindingResult modelAttributeBindingResult) {
       List<Long> bankAccountIds = bankAccountMappingService.searchBankAccountsMappedToServices(tenantId);
       List<BankAccount> bankAccountList = bankDetailsRepository.getBankAccountsById(bankAccountIds,tenantId, requestInfo);
       List<BankBranch> bankBranchList = bankAccountList.stream().map(ba -> ba.getBankBranch()).collect(Collectors.toList());
        List<Long> bankBranchIds = new ArrayList<>();
        for(BankBranch bankBranch:bankBranchList)
            bankBranchIds.add(Long.valueOf(bankBranch.getId()));

      List<BankBranch> bankBranches = bankDetailsRepository.getBankbranches(bankBranchIds,null,tenantId,true,requestInfo);

      return getBankBranchResponse(bankBranches, requestInfo);
    }

    @RequestMapping("/_searchAccountNumbersByBankBranch")
    @PostMapping
    public ResponseEntity<?> searchBankAccountByBankBranch(@RequestParam String bankBranchId,@RequestParam String tenantId, @RequestBody RequestInfo requestInfo,
                                                final BindingResult modelAttributeBindingResult) {
        List<String> accountTypes = Arrays.stream(BankAccountType.values()).map(Enum::name)
                .collect(Collectors.toList());
        BankAccountSearchRequest bankAccountSearchRequest = BankAccountSearchRequest.builder().tenantId(tenantId).bankBranchId(bankBranchId).build();
        List<BankAccount> bankAccountsList = bankDetailsRepository.searchBankAccounts(bankAccountSearchRequest,null,accountTypes,requestInfo);
        return getBankAccontResponse(bankAccountsList, requestInfo);
    }


    private ResponseEntity<?> getBankBranchResponse(List<BankBranch> bankBranches, RequestInfo requestInfo) {
        log.info("Building Bank Branch success response.");
        BankBranchResponse bankBranchResponse = new BankBranchResponse();
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        bankBranchResponse.setBankBranches(bankBranches);
        bankBranchResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(bankBranchResponse, HttpStatus.OK);
    }

    private ResponseEntity<?> getBankAccontResponse(List<BankAccount> bankAccounts, RequestInfo requestInfo) {
        log.info("Building Bank Branch success response.");
        BankAccountResponse bankAccountResponse = new BankAccountResponse();
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        bankAccountResponse.setBankAccounts(bankAccounts);
        bankAccountResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(bankAccountResponse, HttpStatus.OK);
    }

    private ResponseEntity<?> getBanksSuccessResponse(List<Bank> banksList, RequestInfo requestInfo) {
        log.info("Building Bank search success response.");
        BankResponse bankResponse = new BankResponse();
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        bankResponse.setBankList(banksList);
        bankResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(bankResponse, HttpStatus.OK);
    }
}
