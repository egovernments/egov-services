package org.egov.egf.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.persistence.entity.AccountCodePurpose;
import org.egov.egf.persistence.queue.contract.AccountCodePurposeContract;
import org.egov.egf.persistence.queue.contract.AccountCodePurposeContractRequest;
import org.egov.egf.persistence.queue.contract.AccountCodePurposeContractResponse;
import org.egov.egf.persistence.queue.contract.AccountCodePurposeGetRequest;
import org.egov.egf.persistence.queue.contract.Pagination;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.service.AccountCodePurposeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
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
@RequestMapping("/accountcodepurposes")
public class AccountCodePurposeController {

	@Autowired
	private AccountCodePurposeService accountCodePurposeService;

	@PostMapping("_create")
	@ResponseStatus(HttpStatus.OK)
	public AccountCodePurposeContractResponse create(
			@RequestBody @Valid AccountCodePurposeContractRequest accountCodePurposeContractRequest,
			BindingResult errors) {
		accountCodePurposeService.validate(accountCodePurposeContractRequest, "create", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		accountCodePurposeContractRequest.getRequestInfo().setAction("create");
		accountCodePurposeService.fetchRelatedContracts(accountCodePurposeContractRequest);
		accountCodePurposeService.push(accountCodePurposeContractRequest);
		AccountCodePurposeContractResponse accountCodePurposeContractResponse = new AccountCodePurposeContractResponse();
		accountCodePurposeContractResponse.setAccountCodePurposes(new ArrayList<AccountCodePurposeContract>());
		if (accountCodePurposeContractRequest.getAccountCodePurposes() != null
				&& !accountCodePurposeContractRequest.getAccountCodePurposes().isEmpty()) {
			for (AccountCodePurposeContract accountCodePurposeContract : accountCodePurposeContractRequest
					.getAccountCodePurposes()) {

				accountCodePurposeContractResponse.getAccountCodePurposes().add(accountCodePurposeContract);
			}
		} else if (accountCodePurposeContractRequest.getAccountCodePurpose() != null) {
			accountCodePurposeContractResponse
					.setAccountCodePurpose(accountCodePurposeContractRequest.getAccountCodePurpose());
		}
		accountCodePurposeContractResponse
				.setResponseInfo(getResponseInfo(accountCodePurposeContractRequest.getRequestInfo()));
		accountCodePurposeContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return accountCodePurposeContractResponse;
	}

	@PostMapping(value = "/{uniqueId}/_update")
	@ResponseStatus(HttpStatus.OK)
	public AccountCodePurposeContractResponse update(
			@RequestBody @Valid AccountCodePurposeContractRequest accountCodePurposeContractRequest,
			BindingResult errors, @PathVariable Long uniqueId) {

		accountCodePurposeService.validate(accountCodePurposeContractRequest, "update", errors);

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		accountCodePurposeContractRequest.getRequestInfo().setAction("update");
		accountCodePurposeService.fetchRelatedContracts(accountCodePurposeContractRequest);
		accountCodePurposeContractRequest.getAccountCodePurpose().setId(uniqueId);
		accountCodePurposeService.push(accountCodePurposeContractRequest);
		AccountCodePurposeContractResponse accountCodePurposeContractResponse = new AccountCodePurposeContractResponse();
		accountCodePurposeContractResponse
				.setAccountCodePurpose(accountCodePurposeContractRequest.getAccountCodePurpose());
		accountCodePurposeContractResponse
				.setResponseInfo(getResponseInfo(accountCodePurposeContractRequest.getRequestInfo()));
		accountCodePurposeContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return accountCodePurposeContractResponse;
	}

	@PostMapping("_update")
	@ResponseStatus(HttpStatus.OK)
	public AccountCodePurposeContractResponse updateAll(
			@RequestBody @Valid AccountCodePurposeContractRequest accountCodePurposeContractRequest,
			BindingResult errors) {
		accountCodePurposeService.validate(accountCodePurposeContractRequest, "updateAll", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		accountCodePurposeContractRequest.getRequestInfo().setAction("updateAll");
		accountCodePurposeService.fetchRelatedContracts(accountCodePurposeContractRequest);
		accountCodePurposeService.push(accountCodePurposeContractRequest);
		AccountCodePurposeContractResponse accountCodePurposeContractResponse = new AccountCodePurposeContractResponse();
		accountCodePurposeContractResponse.setAccountCodePurposes(new ArrayList<AccountCodePurposeContract>());
		for (AccountCodePurposeContract accountCodePurposeContract : accountCodePurposeContractRequest
				.getAccountCodePurposes()) {
			accountCodePurposeContractResponse.getAccountCodePurposes().add(accountCodePurposeContract);
		}

		accountCodePurposeContractResponse
				.setResponseInfo(getResponseInfo(accountCodePurposeContractRequest.getRequestInfo()));
		accountCodePurposeContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());

		return accountCodePurposeContractResponse;
	}

	@PostMapping("_search")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public AccountCodePurposeContractResponse search(
			@ModelAttribute AccountCodePurposeGetRequest accountCodePurposeGetRequest,
			@RequestBody RequestInfo requestInfo, BindingResult errors) {
		AccountCodePurposeContractRequest accountCodePurposeContractRequest = new AccountCodePurposeContractRequest();
		accountCodePurposeContractRequest.setAccountCodePurposeGetRequest(accountCodePurposeGetRequest);
		accountCodePurposeContractRequest.setRequestInfo(requestInfo);
		accountCodePurposeService.validate(accountCodePurposeContractRequest, "search", errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		accountCodePurposeService.fetchRelatedContracts(accountCodePurposeContractRequest);
		AccountCodePurposeContractResponse accountCodePurposeContractResponse = new AccountCodePurposeContractResponse();
		accountCodePurposeContractResponse.setAccountCodePurposes(new ArrayList<AccountCodePurposeContract>());
		accountCodePurposeContractResponse.setPage(new Pagination());
		List<AccountCodePurpose> accountCodePurposesList = null;
		accountCodePurposesList = accountCodePurposeService.getAccountCodePurposes(accountCodePurposeGetRequest);

		for (AccountCodePurpose b : accountCodePurposesList) {
			accountCodePurposeContractResponse.getAccountCodePurposes().add(new AccountCodePurposeContract(b));
		}
		accountCodePurposeContractResponse.getPage().map(new PageImpl<AccountCodePurpose>(accountCodePurposesList));
		accountCodePurposeContractResponse
				.setResponseInfo(getResponseInfo(accountCodePurposeContractRequest.getRequestInfo()));
		accountCodePurposeContractResponse.getResponseInfo().setStatus(HttpStatus.OK.toString());
		return accountCodePurposeContractResponse;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

}