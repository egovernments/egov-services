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
import org.egov.egf.master.domain.model.AccountCodePurpose;
import org.egov.egf.master.domain.model.AccountCodePurposeSearch;
import org.egov.egf.master.domain.service.AccountCodePurposeService;
import org.egov.egf.master.web.contract.AccountCodePurposeContract;
import org.egov.egf.master.web.contract.AccountCodePurposeSearchContract;
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
@RequestMapping("/accountcodepurposes")
public class AccountCodePurposeController {

	@Autowired
	private AccountCodePurposeService accountCodePurposeService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<AccountCodePurposeContract> create(
			@RequestBody @Valid CommonRequest<AccountCodePurposeContract> accountCodePurposeContractRequest,
			BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<AccountCodePurposeContract> accountCodePurposeResponse = new CommonResponse<>();
		List<AccountCodePurpose> accountcodepurposes = new ArrayList<>();
		AccountCodePurpose accountCodePurpose = null;
		List<AccountCodePurposeContract> accountCodePurposeContracts = new ArrayList<AccountCodePurposeContract>();
		AccountCodePurposeContract contract = null;

		accountCodePurposeContractRequest.getRequestInfo().setAction("create");

		for (AccountCodePurposeContract accountCodePurposeContract : accountCodePurposeContractRequest.getData()) {
			accountCodePurpose = new AccountCodePurpose();
			model.map(accountCodePurposeContract, accountCodePurpose);
			accountCodePurpose.setCreatedDate(new Date());
			accountCodePurpose.setCreatedBy(accountCodePurposeContractRequest.getRequestInfo().getUserInfo());
			accountCodePurpose.setLastModifiedBy(accountCodePurposeContractRequest.getRequestInfo().getUserInfo());
			accountcodepurposes.add(accountCodePurpose);
		}

		accountcodepurposes = accountCodePurposeService.add(accountcodepurposes, errors);

		for (AccountCodePurpose f : accountcodepurposes) {
			contract = new AccountCodePurposeContract();
			contract.setCreatedDate(new Date());
			model.map(f, contract);
			accountCodePurposeContracts.add(contract);
		}

		accountCodePurposeContractRequest.setData(accountCodePurposeContracts);
		accountCodePurposeService.addToQue(accountCodePurposeContractRequest);
		accountCodePurposeResponse.setData(accountCodePurposeContracts);

		return accountCodePurposeResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<AccountCodePurposeContract> update(
			@RequestBody @Valid CommonRequest<AccountCodePurposeContract> accountCodePurposeContractRequest,
			BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		accountCodePurposeContractRequest.getRequestInfo().setAction("update");

		ModelMapper model = new ModelMapper();
		CommonResponse<AccountCodePurposeContract> accountCodePurposeResponse = new CommonResponse<>();
		List<AccountCodePurpose> accountcodepurposes = new ArrayList<>();
		AccountCodePurpose accountCodePurpose;
		AccountCodePurposeContract contract;
		List<AccountCodePurposeContract> accountCodePurposeContracts = new ArrayList<>();

		for (AccountCodePurposeContract accountCodePurposeContract : accountCodePurposeContractRequest.getData()) {
			accountCodePurpose = new AccountCodePurpose();
			model.map(accountCodePurposeContract, accountCodePurpose);
			accountCodePurpose.setLastModifiedDate(new Date());
			accountCodePurpose.setLastModifiedBy(accountCodePurposeContractRequest.getRequestInfo().getUserInfo());
			accountcodepurposes.add(accountCodePurpose);
		}

		accountcodepurposes = accountCodePurposeService.update(accountcodepurposes, errors);

		for (AccountCodePurpose accountCodePurposeObj : accountcodepurposes) {
			contract = new AccountCodePurposeContract();
			contract.setLastModifiedDate(new Date());
			model.map(accountCodePurposeObj, contract);
			accountCodePurposeContracts.add(contract);
		}

		accountCodePurposeContractRequest.setData(accountCodePurposeContracts);
		accountCodePurposeService.addToQue(accountCodePurposeContractRequest);
		accountCodePurposeResponse.setData(accountCodePurposeContracts);

		return accountCodePurposeResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<AccountCodePurposeContract> search(
			@ModelAttribute AccountCodePurposeSearchContract accountCodePurposeSearchContract,
			RequestInfo requestInfo, BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		AccountCodePurposeSearch domain = new AccountCodePurposeSearch();
		mapper.map(accountCodePurposeSearchContract, domain);
		AccountCodePurposeContract contract = null;
		ModelMapper model = new ModelMapper();
		List<AccountCodePurposeContract> accountCodePurposeContracts = new ArrayList<AccountCodePurposeContract>();

		Pagination<AccountCodePurpose> accountcodepurposes = accountCodePurposeService.search(domain);

		for (AccountCodePurpose accountCodePurpose : accountcodepurposes.getPagedData()) {
			contract = new AccountCodePurposeContract();
			model.map(accountCodePurpose, contract);
			accountCodePurposeContracts.add(contract);
		}

		CommonResponse<AccountCodePurposeContract> response = new CommonResponse<>();
		response.setData(accountCodePurposeContracts);
		response.setPage(new PaginationContract(accountcodepurposes));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}