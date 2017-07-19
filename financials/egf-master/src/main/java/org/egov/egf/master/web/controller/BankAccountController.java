package org.egov.egf.master.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.common.web.contract.CommonResponse;
import org.egov.common.web.contract.PaginationContract;
import org.egov.common.web.contract.RequestInfo;
import org.egov.common.web.contract.ResponseInfo;
import org.egov.egf.master.domain.model.BankAccount;
import org.egov.egf.master.domain.model.BankAccountSearch;
import org.egov.egf.master.domain.service.BankAccountService;
import org.egov.egf.master.web.contract.BankAccountContract;
import org.egov.egf.master.web.contract.BankAccountSearchContract;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	public CommonResponse<BankAccountContract> create(
			@RequestBody @Valid CommonRequest<BankAccountContract> bankAccountContractRequest, BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<BankAccountContract> bankAccountResponse = new CommonResponse<>();
		List<BankAccount> bankaccounts = new ArrayList<>();
		BankAccount bankAccount;
		List<BankAccountContract> bankAccountContracts = new ArrayList<BankAccountContract>();
		BankAccountContract contract;

		bankAccountContractRequest.getRequestInfo().setAction("create");

		for (BankAccountContract bankAccountContract : bankAccountContractRequest.getData()) {
			bankAccount = new BankAccount();
			model.map(bankAccountContract, bankAccount);
			bankAccount.setCreatedDate(new Date());
			bankAccount.setCreatedBy(bankAccountContractRequest.getRequestInfo().getUserInfo());
			bankAccount.setLastModifiedBy(bankAccountContractRequest.getRequestInfo().getUserInfo());
			bankaccounts.add(bankAccount);
		}

		bankaccounts = bankAccountService.add(bankaccounts, errors);

		for (BankAccount f : bankaccounts) {
			contract = new BankAccountContract();
			contract.setCreatedDate(new Date());
			model.map(f, contract);
			bankAccountContracts.add(contract);
		}

		bankAccountContractRequest.setData(bankAccountContracts);
		bankAccountService.addToQue(bankAccountContractRequest);
		bankAccountResponse.setData(bankAccountContracts);

		return bankAccountResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<BankAccountContract> update(
			@RequestBody @Valid CommonRequest<BankAccountContract> bankAccountContractRequest, BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		bankAccountContractRequest.getRequestInfo().setAction("update");
		ModelMapper model = new ModelMapper();
		CommonResponse<BankAccountContract> bankAccountResponse = new CommonResponse<>();
		List<BankAccount> bankaccounts = new ArrayList<>();
		BankAccount bankAccount = null;
		BankAccountContract contract = null;
		List<BankAccountContract> bankAccountContracts = new ArrayList<BankAccountContract>();

		for (BankAccountContract bankAccountContract : bankAccountContractRequest.getData()) {
			bankAccount = new BankAccount();
			model.map(bankAccountContract, bankAccount);
			bankAccount.setLastModifiedDate(new Date());
			bankAccount.setLastModifiedBy(bankAccountContractRequest.getRequestInfo().getUserInfo());
			bankaccounts.add(bankAccount);
		}

		bankaccounts = bankAccountService.update(bankaccounts, errors);

		for (BankAccount bankAccountObj : bankaccounts) {
			contract = new BankAccountContract();
			model.map(bankAccountObj, contract);
			contract.setLastModifiedDate(new Date());
			bankAccountContracts.add(contract);
		}

		bankAccountContractRequest.setData(bankAccountContracts);
		bankAccountService.addToQue(bankAccountContractRequest);
		bankAccountResponse.setData(bankAccountContracts);

		return bankAccountResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<BankAccountContract> search(
			@ModelAttribute BankAccountSearchContract bankAccountSearchContract, RequestInfo requestInfo,
			BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		BankAccountSearch domain = new BankAccountSearch();
		mapper.map(bankAccountSearchContract, domain);
		BankAccountContract contract = null;
		ModelMapper model = new ModelMapper();
		List<BankAccountContract> bankAccountContracts = new ArrayList<BankAccountContract>();

		Pagination<BankAccount> bankaccounts = bankAccountService.search(domain);

		for (BankAccount bankAccount : bankaccounts.getPagedData()) {
			contract = new BankAccountContract();
			model.map(bankAccount, contract);
			bankAccountContracts.add(contract);
		}

		CommonResponse<BankAccountContract> response = new CommonResponse<>();
		response.setData(bankAccountContracts);
		response.setPage(new PaginationContract(bankaccounts));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}