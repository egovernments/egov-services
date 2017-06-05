package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.Bank;
import org.egov.egf.persistence.entity.BankBranch;
import org.egov.egf.persistence.queue.contract.BankBranchContract;
import org.egov.egf.persistence.queue.contract.BankBranchContractRequest;
import org.egov.egf.persistence.queue.contract.BankBranchContractResponse;
import org.egov.egf.persistence.queue.contract.BankContract;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.BankBranchService;
import org.egov.egf.persistence.service.BankService;
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
@RequestMapping("/bankbranches")
public class BankBranchController {

	@Autowired
	private BankBranchService bankBranchService;

	@Autowired
	private BankService bankService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public BankBranchContractResponse create(@RequestBody @Valid BankBranchContractRequest bankBranchContractRequest,
			BindingResult errors) {
		bankBranchService.validate(bankBranchContractRequest, "create", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		bankBranchContractRequest.getRequestInfo().setAction("create");
		bankBranchService.fetchRelatedContracts(bankBranchContractRequest);
		bankBranchService.push(bankBranchContractRequest);
		BankBranchContractResponse bankBranchContractResponse = new BankBranchContractResponse();
		bankBranchContractResponse.setBankBranches(new ArrayList<BankBranchContract>());
		if (bankBranchContractRequest.getBankBranches() != null
				&& !bankBranchContractRequest.getBankBranches().isEmpty()) {
			for (BankBranchContract bankBranchContract : bankBranchContractRequest.getBankBranches()) {

				bankBranchContractResponse.getBankBranches().add(bankBranchContract);
			}
		} else if (bankBranchContractRequest.getBankBranch() != null) {
			bankBranchContractResponse.setBankBranch(bankBranchContractRequest.getBankBranch());
		}
		bankBranchContractResponse.setResponseInfo(getResponseInfo(bankBranchContractRequest.getRequestInfo()));

		return bankBranchContractResponse;
	}

	@PostMapping(value = "/{uniqueId}/_update")
	@ResponseStatus(HttpStatus.OK)
	public BankBranchContractResponse update(@RequestBody @Valid BankBranchContractRequest bankBranchContractRequest,
			BindingResult errors, @PathVariable Long uniqueId) {

		bankBranchService.validate(bankBranchContractRequest, "update", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		bankBranchContractRequest.getRequestInfo().setAction("update");
		bankBranchService.fetchRelatedContracts(bankBranchContractRequest);
		bankBranchContractRequest.getBankBranch().setId(uniqueId);
		bankBranchService.push(bankBranchContractRequest);
		BankBranchContractResponse bankBranchContractResponse = new BankBranchContractResponse();
		bankBranchContractResponse.setBankBranch(new BankBranchContract());

		bankBranchContractResponse.setBankBranch(bankBranchContractRequest.getBankBranch());

		bankBranchContractResponse.setResponseInfo(getResponseInfo(bankBranchContractRequest.getRequestInfo()));
		bankBranchContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return bankBranchContractResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.OK)
	public BankBranchContractResponse updateAll(@RequestBody @Valid BankBranchContractRequest bankBranchContractRequest,
			BindingResult errors) {
		bankBranchService.validate(bankBranchContractRequest, "updateAll", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		bankBranchContractRequest.getRequestInfo().setAction("updateAll");
		bankBranchService.fetchRelatedContracts(bankBranchContractRequest);
		bankBranchService.push(bankBranchContractRequest);
		BankBranchContractResponse bankBranchContractResponse = new BankBranchContractResponse();
		bankBranchContractResponse.setBankBranches(new ArrayList<BankBranchContract>());
		for (BankBranchContract bankBranchContract : bankBranchContractRequest.getBankBranches()) {
			bankBranchContractResponse.getBankBranches().add(bankBranchContract);
		}

		bankBranchContractResponse.setResponseInfo(getResponseInfo(bankBranchContractRequest.getRequestInfo()));
		bankBranchContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());

		return bankBranchContractResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public BankBranchContractResponse search(@ModelAttribute BankBranchContract bankBranchContracts,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {
		final BankBranchContractRequest bankBranchContractRequest = new BankBranchContractRequest();
		bankBranchContractRequest.setBankBranch(bankBranchContracts);
		bankBranchContractRequest.setRequestInfo(requestInfo);
		bankBranchService.validate(bankBranchContractRequest, "search", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		bankBranchService.fetchRelatedContracts(bankBranchContractRequest);
		BankBranchContractResponse bankBranchContractResponse = new BankBranchContractResponse();
		bankBranchContractResponse.setBankBranches(new ArrayList<BankBranchContract>());
		bankBranchContractResponse.setPage(new Pagination());
		Page<BankBranch> allBankBranches;

		allBankBranches = bankBranchService.search(bankBranchContractRequest);
		BankBranchContract bankBranchContract = null;
		for (BankBranch b : allBankBranches) {
			bankBranchContract = new BankBranchContract(b);
			if (b.getBank() != null) {
				Bank bank = bankService.findOne(b.getBank());
				bankBranchContract.setBank(new BankContract(bank));
			}
			bankBranchContractResponse.getBankBranches().add(bankBranchContract);
		}
		bankBranchContractResponse.getPage().map(allBankBranches);
		bankBranchContractResponse.setResponseInfo(getResponseInfo(bankBranchContractRequest.getRequestInfo()));
		bankBranchContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return bankBranchContractResponse;
	}

	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public BankBranchContractResponse view(@ModelAttribute BankBranchContractRequest bankBranchContractRequest,
			BindingResult errors, @PathVariable Long uniqueId) {
		bankBranchService.validate(bankBranchContractRequest, "view", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		bankBranchService.fetchRelatedContracts(bankBranchContractRequest);
		BankBranch bankBranchFromDb = bankBranchService.findOne(uniqueId);
		BankBranchContract bankBranch = bankBranchContractRequest.getBankBranch();

		ModelMapper model = new ModelMapper();
		model.map(bankBranchFromDb, bankBranch);

		BankBranchContractResponse bankBranchContractResponse = new BankBranchContractResponse();
		bankBranchContractResponse.setBankBranch(bankBranch);
		bankBranchContractResponse.setResponseInfo(getResponseInfo(bankBranchContractRequest.getRequestInfo()));
		bankBranchContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return bankBranchContractResponse;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}