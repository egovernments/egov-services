package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.AccountDetailType;
import org.egov.egf.persistence.queue.contract.AccountDetailTypeContract;
import org.egov.egf.persistence.queue.contract.AccountDetailTypeContractRequest;
import org.egov.egf.persistence.queue.contract.AccountDetailTypeContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.AccountDetailTypeService;
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
@RequestMapping("/accountdetailtypes")
public class AccountDetailTypeController {
	@Autowired
	private AccountDetailTypeService accountDetailTypeService;

	@PostMapping("_create")
	@ResponseStatus(HttpStatus.CREATED)
	public AccountDetailTypeContractResponse create(
			@RequestBody @Valid AccountDetailTypeContractRequest accountDetailTypeContractRequest,
			BindingResult errors) {
		accountDetailTypeService.validate(accountDetailTypeContractRequest, "create", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		accountDetailTypeContractRequest.getRequestInfo().setAction("create");
		accountDetailTypeService.fetchRelatedContracts(accountDetailTypeContractRequest);
		accountDetailTypeService.push(accountDetailTypeContractRequest);
		AccountDetailTypeContractResponse accountDetailTypeContractResponse = new AccountDetailTypeContractResponse();
		accountDetailTypeContractResponse.setAccountDetailTypes(new ArrayList<AccountDetailTypeContract>());
		if (accountDetailTypeContractRequest.getAccountDetailTypes() != null
				&& !accountDetailTypeContractRequest.getAccountDetailTypes().isEmpty()) {
			for (AccountDetailTypeContract accountDetailTypeContract : accountDetailTypeContractRequest
					.getAccountDetailTypes()) {
				accountDetailTypeContractResponse.getAccountDetailTypes().add(accountDetailTypeContract);
			}
		} else if (accountDetailTypeContractRequest.getAccountDetailType() != null) {
			accountDetailTypeContractResponse
					.setAccountDetailType(accountDetailTypeContractRequest.getAccountDetailType());
		}

		accountDetailTypeContractResponse
				.setResponseInfo(getResponseInfo(accountDetailTypeContractRequest.getRequestInfo()));

		return accountDetailTypeContractResponse;
	}

	@PostMapping(value = "/{uniqueId}/_update")
	@ResponseStatus(HttpStatus.OK)
	public AccountDetailTypeContractResponse update(
			@RequestBody @Valid AccountDetailTypeContractRequest accountDetailTypeContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {

		accountDetailTypeService.validate(accountDetailTypeContractRequest, "update", errors);

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		accountDetailTypeContractRequest.getRequestInfo().setAction("update");
		accountDetailTypeService.fetchRelatedContracts(accountDetailTypeContractRequest);
		accountDetailTypeContractRequest.getAccountDetailType().setId(uniqueId);
		accountDetailTypeService.push(accountDetailTypeContractRequest);
		accountDetailTypeContractRequest.getAccountDetailType().setId(uniqueId);
		AccountDetailTypeContractResponse accountDetailTypeContractResponse = new AccountDetailTypeContractResponse();
		accountDetailTypeContractResponse.setAccountDetailType(accountDetailTypeContractRequest.getAccountDetailType());
		accountDetailTypeContractResponse
				.setResponseInfo(getResponseInfo(accountDetailTypeContractRequest.getRequestInfo()));
		accountDetailTypeContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return accountDetailTypeContractResponse;
	}

	@PostMapping("_update")
	@ResponseStatus(HttpStatus.OK)
	public AccountDetailTypeContractResponse updateAll(
			@RequestBody @Valid AccountDetailTypeContractRequest accountDetailTypeContractRequest,
			BindingResult errors) {
		accountDetailTypeService.validate(accountDetailTypeContractRequest, "updateAll", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		accountDetailTypeContractRequest.getRequestInfo().setAction("updateAll");
		accountDetailTypeService.fetchRelatedContracts(accountDetailTypeContractRequest);
		accountDetailTypeService.push(accountDetailTypeContractRequest);
		AccountDetailTypeContractResponse accountDetailTypeContractResponse = new AccountDetailTypeContractResponse();
		accountDetailTypeContractResponse.setAccountDetailTypes(new ArrayList<AccountDetailTypeContract>());
		for (AccountDetailTypeContract accountDetailTypeContract : accountDetailTypeContractRequest
				.getAccountDetailTypes()) {
			accountDetailTypeContractResponse.getAccountDetailTypes().add(accountDetailTypeContract);
		}

		accountDetailTypeContractResponse
				.setResponseInfo(getResponseInfo(accountDetailTypeContractRequest.getRequestInfo()));
		accountDetailTypeContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());

		return accountDetailTypeContractResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public AccountDetailTypeContractResponse search(
			@ModelAttribute AccountDetailTypeContract accountDetailTypeContracts, @RequestBody RequestInfo requestInfo,
			BindingResult errors) {

		AccountDetailTypeContractRequest accountDetailTypeContractRequest = new AccountDetailTypeContractRequest();
		accountDetailTypeContractRequest.setAccountDetailType(accountDetailTypeContracts);
		accountDetailTypeContractRequest.setRequestInfo(requestInfo);
		accountDetailTypeService.validate(accountDetailTypeContractRequest, "search", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		accountDetailTypeService.fetchRelatedContracts(accountDetailTypeContractRequest);
		AccountDetailTypeContractResponse accountDetailTypeContractResponse = new AccountDetailTypeContractResponse();
		accountDetailTypeContractResponse.setAccountDetailTypes(new ArrayList<AccountDetailTypeContract>());
		accountDetailTypeContractResponse.setPage(new Pagination());
		Page<AccountDetailType> allAccountDetailTypes;
		ModelMapper model = new ModelMapper();

		allAccountDetailTypes = accountDetailTypeService.search(accountDetailTypeContractRequest);
		AccountDetailTypeContract accountDetailTypeContract = null;
		for (AccountDetailType b : allAccountDetailTypes) {
			accountDetailTypeContract = new AccountDetailTypeContract();
			model.map(b, accountDetailTypeContract);
			accountDetailTypeContractResponse.getAccountDetailTypes().add(accountDetailTypeContract);
		}
		accountDetailTypeContractResponse.getPage().map(allAccountDetailTypes);
		accountDetailTypeContractResponse
				.setResponseInfo(getResponseInfo(accountDetailTypeContractRequest.getRequestInfo()));
		accountDetailTypeContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return accountDetailTypeContractResponse;
	}

	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public AccountDetailTypeContractResponse view(
			@ModelAttribute AccountDetailTypeContractRequest accountDetailTypeContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {
		accountDetailTypeService.validate(accountDetailTypeContractRequest, "view", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		accountDetailTypeService.fetchRelatedContracts(accountDetailTypeContractRequest);
		AccountDetailType accountDetailTypeFromDb = accountDetailTypeService.findOne(uniqueId);
		AccountDetailTypeContract accountDetailType = accountDetailTypeContractRequest.getAccountDetailType();

		ModelMapper model = new ModelMapper();
		model.map(accountDetailTypeFromDb, accountDetailType);

		AccountDetailTypeContractResponse accountDetailTypeContractResponse = new AccountDetailTypeContractResponse();
		accountDetailTypeContractResponse.setAccountDetailType(accountDetailType);
		accountDetailTypeContractResponse
				.setResponseInfo(getResponseInfo(accountDetailTypeContractRequest.getRequestInfo()));
		accountDetailTypeContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return accountDetailTypeContractResponse;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}