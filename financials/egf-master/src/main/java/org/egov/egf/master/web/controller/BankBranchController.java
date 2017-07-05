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
import org.egov.egf.master.domain.model.BankBranch;
import org.egov.egf.master.domain.model.BankBranchSearch;
import org.egov.egf.master.domain.service.BankBranchService;
import org.egov.egf.master.web.contract.BankBranchContract;
import org.egov.egf.master.web.contract.BankBranchSearchContract;
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
@RequestMapping("/bankbranches")
public class BankBranchController {

	@Autowired
	private BankBranchService bankBranchService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<BankBranchContract> create(
			@RequestBody @Valid CommonRequest<BankBranchContract> bankBranchContractRequest, BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<BankBranchContract> bankBranchResponse = new CommonResponse<>();
		List<BankBranch> bankbranches = new ArrayList<>();
		BankBranch bankBranch = null;
		List<BankBranchContract> bankBranchContracts = new ArrayList<BankBranchContract>();
		BankBranchContract contract = null;

		bankBranchContractRequest.getRequestInfo().setAction("create");

		for (BankBranchContract bankBranchContract : bankBranchContractRequest.getData()) {
			bankBranch = new BankBranch();
			model.map(bankBranchContract, bankBranch);
			bankBranch.setCreatedBy(bankBranchContractRequest.getRequestInfo().getUserInfo());
			bankBranch.setLastModifiedBy(bankBranchContractRequest.getRequestInfo().getUserInfo());
			bankbranches.add(bankBranch);
		}

		bankbranches = bankBranchService.add(bankbranches, errors);

		for (BankBranch f : bankbranches) {
			contract = new BankBranchContract();
			model.map(f, contract);
			bankBranchContracts.add(contract);
		}

		bankBranchContractRequest.setData(bankBranchContracts);
		bankBranchService.addToQue(bankBranchContractRequest);
		bankBranchResponse.setData(bankBranchContracts);

		return bankBranchResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<BankBranchContract> update(
			@RequestBody @Valid CommonRequest<BankBranchContract> bankBranchContractRequest, BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		bankBranchContractRequest.getRequestInfo().setAction("update");
		ModelMapper model = new ModelMapper();
		CommonResponse<BankBranchContract> bankBranchResponse = new CommonResponse<>();
		List<BankBranch> bankbranches = new ArrayList<>();
		BankBranch bankBranch = null;
		BankBranchContract contract = null;
		List<BankBranchContract> bankBranchContracts = new ArrayList<BankBranchContract>();

		for (BankBranchContract bankBranchContract : bankBranchContractRequest.getData()) {
			bankBranch = new BankBranch();
			model.map(bankBranchContract, bankBranch);
			bankBranch.setLastModifiedBy(bankBranchContractRequest.getRequestInfo().getUserInfo());
			bankbranches.add(bankBranch);
		}

		bankbranches = bankBranchService.update(bankbranches, errors);

		for (BankBranch bankBranchObj : bankbranches) {
			contract = new BankBranchContract();
			model.map(bankBranchObj, contract);
			bankBranchContracts.add(contract);
		}

		bankBranchContractRequest.setData(bankBranchContracts);
		bankBranchService.addToQue(bankBranchContractRequest);
		bankBranchResponse.setData(bankBranchContracts);

		return bankBranchResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<BankBranchContract> search(@ModelAttribute BankBranchSearchContract bankBranchSearchContract,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		BankBranchSearch domain = new BankBranchSearch();
		mapper.map(bankBranchSearchContract, domain);
		BankBranchContract contract = null;
		ModelMapper model = new ModelMapper();
		List<BankBranchContract> bankBranchContracts = new ArrayList<BankBranchContract>();

		Pagination<BankBranch> bankbranches = bankBranchService.search(domain);

		for (BankBranch bankBranch : bankbranches.getPagedData()) {
			contract = new BankBranchContract();
			model.map(bankBranch, contract);
			bankBranchContracts.add(contract);
		}

		CommonResponse<BankBranchContract> response = new CommonResponse<>();
		response.setData(bankBranchContracts);
		response.setPage(new PaginationContract(bankbranches));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}