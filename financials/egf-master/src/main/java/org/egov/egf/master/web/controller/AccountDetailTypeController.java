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
import org.egov.egf.master.domain.model.AccountDetailType;
import org.egov.egf.master.domain.model.AccountDetailTypeSearch;
import org.egov.egf.master.domain.service.AccountDetailTypeService;
import org.egov.egf.master.web.contract.AccountDetailTypeContract;
import org.egov.egf.master.web.contract.AccountDetailTypeSearchContract;
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
@RequestMapping("/accountdetailtypes")
public class AccountDetailTypeController {

	@Autowired
	private AccountDetailTypeService accountDetailTypeService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<AccountDetailTypeContract> create(
			@RequestBody @Valid CommonRequest<AccountDetailTypeContract> accountDetailTypeContractRequest,
			BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<AccountDetailTypeContract> accountDetailTypeResponse = new CommonResponse<>();
		List<AccountDetailType> accountdetailtypes = new ArrayList<>();
		AccountDetailType accountDetailType = null;
		List<AccountDetailTypeContract> accountDetailTypeContracts = new ArrayList<AccountDetailTypeContract>();
		AccountDetailTypeContract contract = null;

		accountDetailTypeContractRequest.getRequestInfo().setAction("create");

		for (AccountDetailTypeContract accountDetailTypeContract : accountDetailTypeContractRequest.getData()) {
			accountDetailType = new AccountDetailType();
			model.map(accountDetailTypeContract, accountDetailType);
			accountDetailType.setCreatedBy(accountDetailTypeContractRequest.getRequestInfo().getUserInfo());
			accountDetailType.setLastModifiedBy(accountDetailTypeContractRequest.getRequestInfo().getUserInfo());
			accountdetailtypes.add(accountDetailType);
		}

		accountdetailtypes = accountDetailTypeService.add(accountdetailtypes, errors);

		for (AccountDetailType f : accountdetailtypes) {
			contract = new AccountDetailTypeContract();
			model.map(f, contract);
			accountDetailTypeContracts.add(contract);
		}

		accountDetailTypeContractRequest.setData(accountDetailTypeContracts);
		accountDetailTypeService.addToQue(accountDetailTypeContractRequest);
		accountDetailTypeResponse.setData(accountDetailTypeContracts);

		return accountDetailTypeResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<AccountDetailTypeContract> update(
			@RequestBody @Valid CommonRequest<AccountDetailTypeContract> accountDetailTypeContractRequest,
			BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		accountDetailTypeContractRequest.getRequestInfo().setAction("update");
		ModelMapper model = new ModelMapper();
		CommonResponse<AccountDetailTypeContract> accountDetailTypeResponse = new CommonResponse<>();
		List<AccountDetailType> accountdetailtypes = new ArrayList<>();
		AccountDetailType accountDetailType = null;
		AccountDetailTypeContract contract = null;
		List<AccountDetailTypeContract> accountDetailTypeContracts = new ArrayList<AccountDetailTypeContract>();

		for (AccountDetailTypeContract accountDetailTypeContract : accountDetailTypeContractRequest.getData()) {
			accountDetailType = new AccountDetailType();
			model.map(accountDetailTypeContract, accountDetailType);
			accountDetailType.setLastModifiedBy(accountDetailTypeContractRequest.getRequestInfo().getUserInfo());
			accountdetailtypes.add(accountDetailType);
		}

		accountdetailtypes = accountDetailTypeService.update(accountdetailtypes, errors);

		for (AccountDetailType accountDetailTypeObj : accountdetailtypes) {
			contract = new AccountDetailTypeContract();
			model.map(accountDetailTypeObj, contract);
			accountDetailTypeContracts.add(contract);
		}

		accountDetailTypeContractRequest.setData(accountDetailTypeContracts);
		accountDetailTypeService.addToQue(accountDetailTypeContractRequest);
		accountDetailTypeResponse.setData(accountDetailTypeContracts);

		return accountDetailTypeResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<AccountDetailTypeContract> search(
			@ModelAttribute AccountDetailTypeSearchContract accountDetailTypeSearchContract,
			RequestInfo requestInfo, BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		AccountDetailTypeSearch domain = new AccountDetailTypeSearch();
		mapper.map(accountDetailTypeSearchContract, domain);
		AccountDetailTypeContract contract ;
		ModelMapper model = new ModelMapper();
		List<AccountDetailTypeContract> accountDetailTypeContracts = new ArrayList<>();

		Pagination<AccountDetailType> accountdetailtypes = accountDetailTypeService.search(domain);

		for (AccountDetailType accountDetailType : accountdetailtypes.getPagedData()) {
			contract = new AccountDetailTypeContract();
			model.map(accountDetailType, contract);
			accountDetailTypeContracts.add(contract);
		}

		CommonResponse<AccountDetailTypeContract> response = new CommonResponse<>();
		response.setData(accountDetailTypeContracts);
		response.setPage(new PaginationContract(accountdetailtypes));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}