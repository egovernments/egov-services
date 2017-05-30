package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.BankAccount;
import org.egov.egf.persistence.queue.contract.BankAccountContract;
import org.egov.egf.persistence.queue.contract.BankAccountContractRequest;
import org.egov.egf.persistence.queue.contract.BankAccountContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.BankAccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bankaccounts")
public class BankAccountController {
	@Autowired
	private BankAccountService bankAccountService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public BankAccountContractResponse create(@RequestBody @Valid BankAccountContractRequest bankAccountContractRequest,
			BindingResult errors) {
		bankAccountService.validate(bankAccountContractRequest, "create", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		bankAccountContractRequest.getRequestInfo().setAction("create");
		bankAccountService.fetchRelatedContracts(bankAccountContractRequest);
		bankAccountService.push(bankAccountContractRequest);
		BankAccountContractResponse bankAccountContractResponse = new BankAccountContractResponse();
		bankAccountContractResponse.setBankAccounts(new ArrayList<BankAccountContract>());
		if (bankAccountContractRequest.getBankAccounts() != null
				&& !bankAccountContractRequest.getBankAccounts().isEmpty()) {
			for (BankAccountContract bankAccountContract : bankAccountContractRequest.getBankAccounts()) {
				bankAccountContractResponse.getBankAccounts().add(bankAccountContract);
			}
		} else if (bankAccountContractRequest.getBankAccount() != null) {
			bankAccountContractResponse.setBankAccount(bankAccountContractRequest.getBankAccount());
		}
		bankAccountContractResponse.setResponseInfo(getResponseInfo(bankAccountContractRequest.getRequestInfo()));
		bankAccountContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return bankAccountContractResponse;
	}

	@PostMapping(value = "/{uniqueId}/_update")
	@ResponseStatus(HttpStatus.OK)
	public BankAccountContractResponse update(@RequestBody @Valid BankAccountContractRequest bankAccountContractRequest,
			BindingResult errors, @PathVariable Long uniqueId) {

		bankAccountService.validate(bankAccountContractRequest, "update", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		bankAccountContractRequest.getRequestInfo().setAction("update");
		bankAccountService.fetchRelatedContracts(bankAccountContractRequest);
		bankAccountContractRequest.getBankAccount().setId(uniqueId);
		bankAccountService.push(bankAccountContractRequest);
		BankAccountContractResponse bankAccountContractResponse = new BankAccountContractResponse();
		bankAccountContractResponse.setBankAccount(bankAccountContractRequest.getBankAccount());
		bankAccountContractResponse.setResponseInfo(getResponseInfo(bankAccountContractRequest.getRequestInfo()));
		bankAccountContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());

		return bankAccountContractResponse;

	}

	@PostMapping("_update")
	@ResponseStatus(HttpStatus.OK)
	public BankAccountContractResponse updateAll(
			@RequestBody @Valid BankAccountContractRequest bankAccountContractRequest, BindingResult errors) {
		bankAccountService.validate(bankAccountContractRequest, "updateAll", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		bankAccountContractRequest.getRequestInfo().setAction("updateAll");
		bankAccountService.fetchRelatedContracts(bankAccountContractRequest);
		bankAccountService.push(bankAccountContractRequest);
		BankAccountContractResponse bankAccountContractResponse = new BankAccountContractResponse();
		bankAccountContractResponse.setBankAccounts(new ArrayList<BankAccountContract>());
		for (BankAccountContract bankAccountContract : bankAccountContractRequest.getBankAccounts()) {
			bankAccountContractResponse.getBankAccounts().add(bankAccountContract);
		}

		bankAccountContractResponse.setResponseInfo(getResponseInfo(bankAccountContractRequest.getRequestInfo()));
		bankAccountContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());

		return bankAccountContractResponse;
	}

	@PostMapping("_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public BankAccountContractResponse search(@ModelAttribute BankAccountContract bankAccountContracts,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {
		BankAccountContractRequest bankAccountContractRequest = new BankAccountContractRequest();
		bankAccountContractRequest.setBankAccount(bankAccountContracts);
		bankAccountContractRequest.setRequestInfo(requestInfo);
		bankAccountService.validate(bankAccountContractRequest, "search", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		bankAccountService.fetchRelatedContracts(bankAccountContractRequest);
		BankAccountContractResponse bankAccountContractResponse = new BankAccountContractResponse();
		bankAccountContractResponse.setBankAccounts(new ArrayList<BankAccountContract>());
		bankAccountContractResponse.setPage(new Pagination());
		Page<BankAccount> allBankAccounts;
		ModelMapper model = new ModelMapper();

		allBankAccounts = bankAccountService.search(bankAccountContractRequest);
		BankAccountContract bankAccountContract = null;
		for (BankAccount b : allBankAccounts) {
			bankAccountContract = new BankAccountContract();
			model.map(b, bankAccountContract);
			bankAccountContractResponse.getBankAccounts().add(bankAccountContract);
		}
		bankAccountContractResponse.getPage().map(allBankAccounts);
		bankAccountContractResponse.setResponseInfo(getResponseInfo(bankAccountContractRequest.getRequestInfo()));
		bankAccountContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return bankAccountContractResponse;
	}

	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public BankAccountContractResponse view(@ModelAttribute BankAccountContractRequest bankAccountContractRequest,
			BindingResult errors, @PathVariable Long uniqueId) {
		bankAccountService.validate(bankAccountContractRequest, "view", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		bankAccountService.fetchRelatedContracts(bankAccountContractRequest);
		BankAccount bankAccountFromDb = bankAccountService.findOne(uniqueId);
		BankAccountContract bankAccount = bankAccountContractRequest.getBankAccount();

		ModelMapper model = new ModelMapper();
		model.map(bankAccountFromDb, bankAccount);

		BankAccountContractResponse bankAccountContractResponse = new BankAccountContractResponse();
		bankAccountContractResponse.setBankAccount(bankAccount);
		bankAccountContractResponse.setResponseInfo(getResponseInfo(bankAccountContractRequest.getRequestInfo()));
		bankAccountContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return bankAccountContractResponse;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}