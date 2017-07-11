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
import org.egov.egf.master.domain.model.AccountDetailKey;
import org.egov.egf.master.domain.model.AccountDetailKeySearch;
import org.egov.egf.master.domain.service.AccountDetailKeyService;
import org.egov.egf.master.web.contract.AccountDetailKeyContract;
import org.egov.egf.master.web.contract.AccountDetailKeySearchContract;
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
@RequestMapping("/accountdetailkeys")
public class AccountDetailKeyController {

	@Autowired
	private AccountDetailKeyService accountDetailKeyService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<AccountDetailKeyContract> create(
			@RequestBody @Valid CommonRequest<AccountDetailKeyContract> accountDetailKeyContractRequest,
			BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<AccountDetailKeyContract> accountDetailKeyResponse = new CommonResponse<>();
		List<AccountDetailKey> accountdetailkeys = new ArrayList<>();
		AccountDetailKey accountDetailKey = null;
		List<AccountDetailKeyContract> accountDetailKeyContracts = new ArrayList<AccountDetailKeyContract>();
		AccountDetailKeyContract contract = null;

		accountDetailKeyContractRequest.getRequestInfo().setAction("create");

		for (AccountDetailKeyContract accountDetailKeyContract : accountDetailKeyContractRequest.getData()) {
			accountDetailKey = new AccountDetailKey();
			model.map(accountDetailKeyContract, accountDetailKey);
			accountDetailKey.setCreatedBy(accountDetailKeyContractRequest.getRequestInfo().getUserInfo());
			accountDetailKey.setLastModifiedBy(accountDetailKeyContractRequest.getRequestInfo().getUserInfo());
			accountdetailkeys.add(accountDetailKey);
		}

		accountdetailkeys = accountDetailKeyService.add(accountdetailkeys, errors);

		for (AccountDetailKey f : accountdetailkeys) {
			contract = new AccountDetailKeyContract();
			model.map(f, contract);
			accountDetailKeyContracts.add(contract);
		}

		accountDetailKeyContractRequest.setData(accountDetailKeyContracts);
		accountDetailKeyService.addToQue(accountDetailKeyContractRequest);
		accountDetailKeyResponse.setData(accountDetailKeyContracts);

		return accountDetailKeyResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<AccountDetailKeyContract> update(
			@RequestBody @Valid CommonRequest<AccountDetailKeyContract> accountDetailKeyContractRequest,
			BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		accountDetailKeyContractRequest.getRequestInfo().setAction("update");

		ModelMapper model = new ModelMapper();
		CommonResponse<AccountDetailKeyContract> accountDetailKeyResponse = new CommonResponse<>();
		List<AccountDetailKey> accountdetailkeys = new ArrayList<>();
		AccountDetailKey accountDetailKey;
		AccountDetailKeyContract contract;
		List<AccountDetailKeyContract> accountDetailKeyContracts = new ArrayList<>();

		for (AccountDetailKeyContract accountDetailKeyContract : accountDetailKeyContractRequest.getData()) {
			accountDetailKey = new AccountDetailKey();
			model.map(accountDetailKeyContract, accountDetailKey);
			accountDetailKey.setLastModifiedBy(accountDetailKeyContractRequest.getRequestInfo().getUserInfo());
			accountdetailkeys.add(accountDetailKey);
		}

		accountdetailkeys = accountDetailKeyService.update(accountdetailkeys, errors);

		for (AccountDetailKey accountDetailKeyObj : accountdetailkeys) {
			contract = new AccountDetailKeyContract();
			model.map(accountDetailKeyObj, contract);
			accountDetailKeyContracts.add(contract);
		}

		accountDetailKeyContractRequest.setData(accountDetailKeyContracts);
		accountDetailKeyService.addToQue(accountDetailKeyContractRequest);
		accountDetailKeyResponse.setData(accountDetailKeyContracts);

		return accountDetailKeyResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<AccountDetailKeyContract> search(
			@ModelAttribute AccountDetailKeySearchContract accountDetailKeySearchContract,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		AccountDetailKeySearch domain = new AccountDetailKeySearch();
		mapper.map(accountDetailKeySearchContract, domain);
		AccountDetailKeyContract contract ;
		ModelMapper model = new ModelMapper();
		List<AccountDetailKeyContract> accountDetailKeyContracts = new ArrayList<>();

		Pagination<AccountDetailKey> accountdetailkeys = accountDetailKeyService.search(domain);

		for (AccountDetailKey accountDetailKey : accountdetailkeys.getPagedData()) {
			contract = new AccountDetailKeyContract();
			model.map(accountDetailKey, contract);
			accountDetailKeyContracts.add(contract);
		}

		CommonResponse<AccountDetailKeyContract> response = new CommonResponse<>();
		response.setData(accountDetailKeyContracts);
		response.setPage(new PaginationContract(accountdetailkeys));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}