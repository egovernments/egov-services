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
import org.egov.egf.master.domain.model.Bank;
import org.egov.egf.master.domain.model.BankSearch;
import org.egov.egf.master.domain.service.BankService;
import org.egov.egf.master.web.contract.BankContract;
import org.egov.egf.master.web.contract.BankSearchContract;
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
@RequestMapping("/banks")
public class BankController {

	@Autowired
	private BankService bankService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<BankContract> create(@RequestBody @Valid CommonRequest<BankContract> bankContractRequest,
			BindingResult errors) {
		ModelMapper model = new ModelMapper();
		CommonResponse<BankContract> bankResponse = new CommonResponse<>();
		bankContractRequest.getRequestInfo().setAction("create");
		List<Bank> banks = new ArrayList<>();
		Bank bank = null;

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		for (BankContract bankContract : bankContractRequest.getData()) {

			bank = new Bank();
			model.map(bankContract, bank);
			bank.setCreatedBy(bankContractRequest.getRequestInfo().getUserInfo());
			bank.setLastModifiedBy(bankContractRequest.getRequestInfo().getUserInfo());
			banks.add(bank);

		}

		banks = bankService.add(banks, errors);
		BankContract contract = null;

		List<BankContract> bankContracts = new ArrayList<BankContract>();
		for (Bank bankDomain : banks) {

			contract = new BankContract();
			model.map(bankDomain, contract);
			bankContracts.add(contract);
		}
		bankContractRequest.setData(bankContracts);
		bankService.addToQue(bankContractRequest);
		bankResponse.setData(bankContracts);
		return bankResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<BankContract> update(@RequestBody @Valid CommonRequest<BankContract> bankContractRequest,
			BindingResult errors) {
		ModelMapper model = new ModelMapper();
		CommonResponse<BankContract> bankResponse = new CommonResponse<>();
		List<Bank> banks = new ArrayList<>();
		Bank bank = null;
		bankContractRequest.getRequestInfo().setAction("update");
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		for (BankContract bankContract : bankContractRequest.getData()) {

			bank = new Bank();
			model.map(bankContract, bank);
			bank.setLastModifiedBy(bankContractRequest.getRequestInfo().getUserInfo());
			banks.add(bank);

		}
		banks = bankService.update(banks, errors);
		BankContract contract = null;

		List<BankContract> bankContracts = new ArrayList<BankContract>();
		for (Bank bankObj : banks) {

			contract = new BankContract();
			model.map(bankObj, contract);
			bankContracts.add(contract);
		}
		bankContractRequest.setData(bankContracts);
		bankService.addToQue(bankContractRequest);
		bankResponse.setData(bankContracts);
		return bankResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<BankContract> search(@ModelAttribute BankSearchContract bankSearchContract,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		BankSearch domain = new BankSearch();
		mapper.map(bankSearchContract, domain);

		Pagination<Bank> banks = bankService.search(domain);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		BankContract contract;
		ModelMapper model = new ModelMapper();
		List<BankContract> bankContracts = new ArrayList<>();
		for (Bank bank : banks.getPagedData()) {

			contract = new BankContract();
			model.map(bank, contract);
			bankContracts.add(contract);
		}

		CommonResponse<BankContract> response = new CommonResponse<>();
		response.setData(bankContracts);
		response.setPage(new PaginationContract(banks));
		response.setResponseInfo(getResponseInfo(requestInfo));
		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}