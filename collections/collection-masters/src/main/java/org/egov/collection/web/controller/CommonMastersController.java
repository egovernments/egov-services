package org.egov.collection.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.egov.collection.persistence.repository.BankDetailsRepository;
import org.egov.collection.persistence.repository.BusinessDetailsRepository;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
    private ResponseInfoFactory responseInfoFactory;

    @RequestMapping("/_searchBranch")
    @PostMapping
    public ResponseEntity<?> searchBankBranches(@ModelAttribute BankBranchSearchRequest bankBranchSearchRequest, @RequestBody RequestInfo requestInfo,
                                                final BindingResult modelAttributeBindingResult) {

       List<String> accountTypes = Arrays.stream(BankAccountType.values()).map(Enum::name)
                    .collect(Collectors.toList());
        List<BankAccount> bankAccounts = bankDetailsRepository.getBankAccounts(accountTypes,bankBranchSearchRequest.getTenantId(),requestInfo);
        List<Long> bankAccountIds = bankAccounts.stream()
                .map(BankAccount::getId).collect(Collectors.toList());
        List<BankBranch> bankBranches = bankDetailsRepository.getBankbranches(bankAccountIds,bankBranchSearchRequest.getBankId(), bankBranchSearchRequest.getTenantId(),bankBranchSearchRequest.isActive(), requestInfo);
        return getBankBranchResponse(bankBranches,requestInfo);
    }

    @RequestMapping("/_searchBankAccounts")
    @PostMapping
    public ResponseEntity<?> searchBankAccounts(@ModelAttribute final BankAccountSearchRequest bankAccountSearchRequest, @RequestBody RequestInfo requestInfo,
                                                final BindingResult modelAttributeBindingResult) {
        List<BusinessDetailsRequestInfo> businessDetails = businessDetailsRepository.getBusinessDetails(
                Arrays.asList(Long.valueOf(bankAccountSearchRequest.getBusinessDetailsId())),bankAccountSearchRequest.getTenantId(),requestInfo);
        List<String> accountTypes = Arrays.stream(BankAccountType.values()).map(Enum::name)
                .collect(Collectors.toList());
        List<BankAccount> bankAccounts = bankDetailsRepository.searchBankAccounts(bankAccountSearchRequest.getBankBranchId(),businessDetails.get(0).getFund(),bankAccountSearchRequest.isActive(),accountTypes,bankAccountSearchRequest.getTenantId(),requestInfo);
        return getBankAccontResponse(bankAccounts, requestInfo);
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
}
