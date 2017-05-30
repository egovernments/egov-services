package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.AccountEntity;
import org.egov.egf.persistence.queue.contract.AccountEntityContract;
import org.egov.egf.persistence.queue.contract.AccountEntityContractRequest;
import org.egov.egf.persistence.queue.contract.AccountEntityContractResponse;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.AccountEntityService;
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
@RequestMapping("/accountentities")
public class AccountEntityController {

	@Autowired
	private AccountEntityService accountEntityService;

	@PostMapping("/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public AccountEntityContractResponse create(
			@RequestBody @Valid AccountEntityContractRequest accountEntityContractRequest, BindingResult errors) {
		accountEntityService.validate(accountEntityContractRequest, "create", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		accountEntityContractRequest.getRequestInfo().setAction("create");
		accountEntityService.fetchRelatedContracts(accountEntityContractRequest);
		accountEntityService.push(accountEntityContractRequest);
		AccountEntityContractResponse accountEntityContractResponse = new AccountEntityContractResponse();
		accountEntityContractResponse.setAccountEntities(new ArrayList<AccountEntityContract>());
		if (accountEntityContractRequest.getAccountEntities() != null
				&& !accountEntityContractRequest.getAccountEntities().isEmpty()) {
			for (AccountEntityContract accountEntityContract : accountEntityContractRequest.getAccountEntities()) {

				accountEntityContractResponse.getAccountEntities().add(accountEntityContract);
			}
		} else if (accountEntityContractRequest.getAccountEntity() != null) {
			accountEntityContractResponse.setAccountEntity(accountEntityContractRequest.getAccountEntity());
		}
		accountEntityContractResponse.setResponseInfo(getResponseInfo(accountEntityContractRequest.getRequestInfo()));

		return accountEntityContractResponse;
	}

	@PostMapping(value = "/{uniqueId}/_update")
	@ResponseStatus(HttpStatus.OK)
	public AccountEntityContractResponse update(
			@RequestBody @Valid AccountEntityContractRequest accountEntityContractRequest, BindingResult errors,
			@PathVariable Long uniqueId) {

		accountEntityService.validate(accountEntityContractRequest, "update", errors);

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		accountEntityContractRequest.getRequestInfo().setAction("update");
		accountEntityService.fetchRelatedContracts(accountEntityContractRequest);
		accountEntityContractRequest.getAccountEntity().setId(uniqueId);
		accountEntityService.push(accountEntityContractRequest);
		AccountEntityContractResponse accountEntityContractResponse = new AccountEntityContractResponse();
		accountEntityContractResponse.setAccountEntity(accountEntityContractRequest.getAccountEntity());
		accountEntityContractResponse.setResponseInfo(getResponseInfo(accountEntityContractRequest.getRequestInfo()));
		accountEntityContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return accountEntityContractResponse;
	}

	@PostMapping("_update")
	@ResponseStatus(HttpStatus.OK)
	public AccountEntityContractResponse updateAll(
			@RequestBody @Valid AccountEntityContractRequest accountEntityContractRequest, BindingResult errors) {
		accountEntityService.validate(accountEntityContractRequest, "updateAll", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		accountEntityContractRequest.getRequestInfo().setAction("updateAll");
		accountEntityService.fetchRelatedContracts(accountEntityContractRequest);
		accountEntityService.push(accountEntityContractRequest);
		AccountEntityContractResponse accountEntityContractResponse = new AccountEntityContractResponse();
		accountEntityContractResponse.setAccountEntities(new ArrayList<AccountEntityContract>());
		for (AccountEntityContract accountEntityContract : accountEntityContractRequest.getAccountEntities()) {
			accountEntityContractResponse.getAccountEntities().add(accountEntityContract);
		}

		accountEntityContractResponse.setResponseInfo(getResponseInfo(accountEntityContractRequest.getRequestInfo()));
		accountEntityContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());

		return accountEntityContractResponse;
	}

	@PostMapping("/_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public AccountEntityContractResponse search(@ModelAttribute AccountEntityContract accountEntityContracts,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {
		AccountEntityContractRequest accountEntityContractRequest = new AccountEntityContractRequest();
		accountEntityContractRequest.setAccountEntity(accountEntityContracts);
		accountEntityContractRequest.setRequestInfo(requestInfo);
		accountEntityService.validate(accountEntityContractRequest, "search", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		accountEntityService.fetchRelatedContracts(accountEntityContractRequest);
		AccountEntityContractResponse accountEntityContractResponse = new AccountEntityContractResponse();
		accountEntityContractResponse.setAccountEntities(new ArrayList<AccountEntityContract>());
		accountEntityContractResponse.setPage(new Pagination());
		Page<AccountEntity> allAccountEntities;
		ModelMapper model = new ModelMapper();

		allAccountEntities = accountEntityService.search(accountEntityContractRequest);
		AccountEntityContract accountEntityContract = null;
		for (AccountEntity b : allAccountEntities) {
			accountEntityContract = new AccountEntityContract();
			model.map(b, accountEntityContract);
			accountEntityContractResponse.getAccountEntities().add(accountEntityContract);
		}
		accountEntityContractResponse.getPage().map(allAccountEntities);
		accountEntityContractResponse.setResponseInfo(getResponseInfo(accountEntityContractRequest.getRequestInfo()));
		accountEntityContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return accountEntityContractResponse;
	}

	@GetMapping(value = "/{uniqueId}")
	@ResponseStatus(HttpStatus.OK)
	public AccountEntityContractResponse view(@ModelAttribute AccountEntityContractRequest accountEntityContractRequest,
			BindingResult errors, @PathVariable Long uniqueId) {
		accountEntityService.validate(accountEntityContractRequest, "view", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		accountEntityService.fetchRelatedContracts(accountEntityContractRequest);
		AccountEntity accountEntityFromDb = accountEntityService.findOne(uniqueId);
		AccountEntityContract accountEntity = accountEntityContractRequest.getAccountEntity();

		ModelMapper model = new ModelMapper();
		model.map(accountEntityFromDb, accountEntity);

		AccountEntityContractResponse accountEntityContractResponse = new AccountEntityContractResponse();
		accountEntityContractResponse.setAccountEntity(accountEntity);
		accountEntityContractResponse.setResponseInfo(getResponseInfo(accountEntityContractRequest.getRequestInfo()));
		accountEntityContractResponse.getResponseInfo().setStatus(HttpStatus.CREATED.toString());
		return accountEntityContractResponse;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}