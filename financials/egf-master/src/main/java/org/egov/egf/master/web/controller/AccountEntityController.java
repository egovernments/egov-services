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
import org.egov.egf.master.domain.model.AccountEntity;
import org.egov.egf.master.domain.model.AccountEntitySearch;
import org.egov.egf.master.domain.service.AccountEntityService;
import org.egov.egf.master.web.contract.AccountEntityContract;
import org.egov.egf.master.web.contract.AccountEntitySearchContract;
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
@RequestMapping("/accountentities")
public class AccountEntityController {

	@Autowired
	private AccountEntityService accountEntityService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<AccountEntityContract> create(
			@RequestBody @Valid CommonRequest<AccountEntityContract> accountEntityContractRequest,
			BindingResult errors) {
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		CommonResponse<AccountEntityContract> accountEntityResponse = new CommonResponse<>();
		List<AccountEntity> accountentities = new ArrayList<>();
		AccountEntity accountEntity = null;
		List<AccountEntityContract> accountEntityContracts = new ArrayList<AccountEntityContract>();
		AccountEntityContract contract = null;

		accountEntityContractRequest.getRequestInfo().setAction("create");

		for (AccountEntityContract accountEntityContract : accountEntityContractRequest.getData()) {
			accountEntity = new AccountEntity();
			model.map(accountEntityContract, accountEntity);
			accountEntity.setCreatedBy(accountEntityContractRequest.getRequestInfo().getUserInfo());
			accountEntity.setLastModifiedBy(accountEntityContractRequest.getRequestInfo().getUserInfo());
			accountentities.add(accountEntity);
		}

		accountentities = accountEntityService.add(accountentities, errors);

		for (AccountEntity f : accountentities) {
			contract = new AccountEntityContract();
			model.map(f, contract);
			accountEntityContracts.add(contract);
		}

		accountEntityContractRequest.setData(accountEntityContracts);
		accountEntityService.addToQue(accountEntityContractRequest);
		accountEntityResponse.setData(accountEntityContracts);

		return accountEntityResponse;
	}

	@PostMapping("/_update")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<AccountEntityContract> update(
			@RequestBody @Valid CommonRequest<AccountEntityContract> accountEntityContractRequest,
			BindingResult errors) {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		accountEntityContractRequest.getRequestInfo().setAction("update");
		ModelMapper model = new ModelMapper();
		CommonResponse<AccountEntityContract> accountEntityResponse = new CommonResponse<>();
		List<AccountEntity> accountentities = new ArrayList<>();
		AccountEntity accountEntity;
		AccountEntityContract contract;
		List<AccountEntityContract> accountEntityContracts = new ArrayList<>();

		for (AccountEntityContract accountEntityContract : accountEntityContractRequest.getData()) {
			accountEntity = new AccountEntity();
			model.map(accountEntityContract, accountEntity);
			accountEntity.setLastModifiedBy(accountEntityContractRequest.getRequestInfo().getUserInfo());
			accountentities.add(accountEntity);
		}

		accountentities = accountEntityService.update(accountentities, errors);

		for (AccountEntity accountEntityObj : accountentities) {
			contract = new AccountEntityContract();
			model.map(accountEntityObj, contract);
			accountEntityContracts.add(contract);
		}

		accountEntityContractRequest.setData(accountEntityContracts);
		accountEntityService.addToQue(accountEntityContractRequest);
		accountEntityResponse.setData(accountEntityContracts);

		return accountEntityResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<AccountEntityContract> search(
			@ModelAttribute AccountEntitySearchContract accountEntitySearchContract,
			RequestInfo requestInfo, BindingResult errors) {

		ModelMapper mapper = new ModelMapper();
		AccountEntitySearch domain = new AccountEntitySearch();
		mapper.map(accountEntitySearchContract, domain);
		AccountEntityContract contract = null;
		ModelMapper model = new ModelMapper();
		List<AccountEntityContract> accountEntityContracts = new ArrayList<AccountEntityContract>();

		Pagination<AccountEntity> accountentities = accountEntityService.search(domain);

		for (AccountEntity accountEntity : accountentities.getPagedData()) {
			contract = new AccountEntityContract();
			model.map(accountEntity, contract);
			accountEntityContracts.add(contract);
		}

		CommonResponse<AccountEntityContract> response = new CommonResponse<>();
		response.setData(accountEntityContracts);
		response.setPage(new PaginationContract(accountentities));
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}