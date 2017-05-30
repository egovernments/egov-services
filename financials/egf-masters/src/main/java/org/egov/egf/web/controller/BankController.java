package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.Bank;
import org.egov.egf.persistence.queue.contract.BankContract;
import org.egov.egf.persistence.queue.contract.BankContractRequest;
import org.egov.egf.persistence.queue.contract.BankContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.BankService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
	public BankContractResponse create(@RequestBody @Valid BankContractRequest bankContractRequest,
			BindingResult errors) {
		bankService.validate(bankContractRequest, "create", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		bankContractRequest.getRequestInfo().setAction("create");
		bankService.fetchRelatedContracts(bankContractRequest);
		bankService.push(bankContractRequest);
		BankContractResponse bankContractResponse = new BankContractResponse();
		bankContractResponse.setBanks(new ArrayList<BankContract>());
		if (bankContractRequest.getBanks() != null && !bankContractRequest.getBanks().isEmpty()) {
			for (BankContract bankContract : bankContractRequest.getBanks()) {
				bankContractResponse.getBanks().add(bankContract);
			}
		} else if (bankContractRequest.getBank() != null) {
			bankContractResponse.setBank(bankContractRequest.getBank());
		}

		bankContractResponse.setResponseInfo(getResponseInfo(bankContractRequest.getRequestInfo()));
		bankContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return bankContractResponse;
	}

	@PostMapping(value = "/{uniqueId}/_update")
	@ResponseStatus(HttpStatus.OK)
	public BankContractResponse update(@RequestBody @Valid BankContractRequest bankContractRequest,
			BindingResult errors, @PathVariable Long uniqueId) {
		bankService.validate(bankContractRequest, "update", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		bankContractRequest.getRequestInfo().setAction("update");
		bankService.fetchRelatedContracts(bankContractRequest);
		bankContractRequest.getBank().setId(uniqueId);
		bankService.push(bankContractRequest);
		BankContractResponse bankContractResponse = new BankContractResponse();

		bankContractResponse.setBank(bankContractRequest.getBank());

		bankContractResponse.setResponseInfo(getResponseInfo(bankContractRequest.getRequestInfo()));
		bankContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return bankContractResponse;
	}

	@PostMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public BankContractResponse view(@ModelAttribute BankContractRequest bankContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {
		bankService.validate(bankContractRequest, "view", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		bankService.fetchRelatedContracts(bankContractRequest);
		Bank bankFromDb = bankService.findOne(uniqueId);
		BankContract bank = bankContractRequest.getBank();

		ModelMapper model = new ModelMapper();
		model.map(bankFromDb, bank);

		BankContractResponse bankContractResponse = new BankContractResponse();
		bankContractResponse.setBank(bank);
		bankContractResponse.setResponseInfo(getResponseInfo(bankContractRequest.getRequestInfo()));
		bankContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return bankContractResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.OK)
	public BankContractResponse updateAll(@RequestBody @Valid BankContractRequest bankContractRequest,
			BindingResult errors) {
		bankService.validate(bankContractRequest, "updateAll", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		bankContractRequest.getRequestInfo().setAction("updateAll");
		bankService.fetchRelatedContracts(bankContractRequest);
		bankService.push(bankContractRequest);
		BankContractResponse bankContractResponse = new BankContractResponse();
		bankContractResponse.setBanks(new ArrayList<BankContract>());
		for (BankContract bankContract : bankContractRequest.getBanks()) {
			bankContractResponse.getBanks().add(bankContract);
		}

		bankContractResponse.setResponseInfo(getResponseInfo(bankContractRequest.getRequestInfo()));
		bankContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());

		return bankContractResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public BankContractResponse search(@ModelAttribute BankContract bankContracts, @RequestBody RequestInfo requestInfo,
			BindingResult errors) {
		BankContractRequest bankContractRequest = new BankContractRequest();
		bankContractRequest.setBank(bankContracts);
		bankContractRequest.setRequestInfo(requestInfo);

		bankService.validate(bankContractRequest, "search", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		bankService.fetchRelatedContracts(bankContractRequest);
		BankContractResponse bankContractResponse = new BankContractResponse();
		bankContractResponse.setBanks(new ArrayList<BankContract>());
		bankContractResponse.setPage(new Pagination());
		Page<Bank> allBanks;
		ModelMapper model = new ModelMapper();

		allBanks = bankService.search(bankContractRequest);
		BankContract bankContract = null;
		for (Bank b : allBanks) {
			bankContract = new BankContract();
			model.map(b, bankContract);
			bankContractResponse.getBanks().add(bankContract);
		}
		bankContractResponse.getPage().map(allBanks);
		bankContractResponse.setResponseInfo(getResponseInfo(bankContractRequest.getRequestInfo()));
		bankContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return bankContractResponse;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}