package org.egov.egf.persistence.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.json.ObjectMapperFactory;
import org.egov.egf.persistence.entity.AccountCodePurpose;
import org.egov.egf.persistence.queue.contract.AccountCodePurposeContract;
import org.egov.egf.persistence.queue.contract.AccountCodePurposeContractRequest;
import org.egov.egf.persistence.queue.contract.AccountCodePurposeContractResponse;
import org.egov.egf.persistence.queue.contract.AccountCodePurposeGetRequest;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.repository.AccountCodePurposeQueueRepository;
import org.egov.egf.persistence.repository.AccountCodePurposeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class AccountCodePurposeService {

	private final AccountCodePurposeRepository accountCodePurposeRepository;

	private final AccountCodePurposeQueueRepository accountCodePurposeQueueRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SmartValidator validator;

	@Autowired
	public AccountCodePurposeService(final AccountCodePurposeRepository accountCodePurposeRepository,
			final AccountCodePurposeQueueRepository accountCodePurposeQueueRepository) {
		this.accountCodePurposeRepository = accountCodePurposeRepository;
		this.accountCodePurposeQueueRepository = accountCodePurposeQueueRepository;
	}

	public void push(final AccountCodePurposeContractRequest accountCodePurposeContractRequest) {
		accountCodePurposeQueueRepository.push(accountCodePurposeContractRequest);
	}

	@Transactional
	public AccountCodePurposeContractResponse create(HashMap<String, Object> financialContractRequestMap) {
		AccountCodePurposeContractRequest accountCodePurposeContractRequest = ObjectMapperFactory.create().convertValue(
				financialContractRequestMap.get("AccountCodePurposeCreate"), AccountCodePurposeContractRequest.class);
		accountCodePurposeContractRequest = accountCodePurposeRepository.create(accountCodePurposeContractRequest);
		AccountCodePurposeContractResponse accountCodePurposeContractResponse = new AccountCodePurposeContractResponse();
		accountCodePurposeContractResponse.setAccountCodePurposes(new ArrayList<AccountCodePurposeContract>());
		accountCodePurposeContractResponse.getAccountCodePurposes()
				.addAll(accountCodePurposeContractRequest.getAccountCodePurposes());
		accountCodePurposeContractResponse
				.setAccountCodePurpose(accountCodePurposeContractRequest.getAccountCodePurpose());
		accountCodePurposeContractResponse
				.setResponseInfo(getResponseInfo(accountCodePurposeContractRequest.getRequestInfo()));
		return accountCodePurposeContractResponse;

	}

	@Transactional
	public AccountCodePurposeContractResponse update(HashMap<String, Object> financialContractRequestMap) {
		AccountCodePurposeContractRequest accountCodePurposeContractRequest = ObjectMapperFactory.create().convertValue(
				financialContractRequestMap.get("AccountCodePurposeUpdate"), AccountCodePurposeContractRequest.class);
		accountCodePurposeContractRequest = accountCodePurposeRepository.update(accountCodePurposeContractRequest);
		AccountCodePurposeContractResponse accountCodePurposeContractResponse = new AccountCodePurposeContractResponse();
		accountCodePurposeContractResponse.setAccountCodePurposes(new ArrayList<AccountCodePurposeContract>());
		accountCodePurposeContractResponse.getAccountCodePurposes()
				.addAll(accountCodePurposeContractRequest.getAccountCodePurposes());
		accountCodePurposeContractResponse
				.setAccountCodePurpose(accountCodePurposeContractRequest.getAccountCodePurpose());
		accountCodePurposeContractResponse
				.setResponseInfo(getResponseInfo(accountCodePurposeContractRequest.getRequestInfo()));
		return accountCodePurposeContractResponse;
	}

	public List<AccountCodePurpose> getAccountCodePurposes(AccountCodePurposeGetRequest accountCodePurposeGetRequest) {
		return accountCodePurposeRepository.findForCriteria(accountCodePurposeGetRequest);
	}

	public BindingResult validate(AccountCodePurposeContractRequest accountCodePurposeContractRequest, String method,
			BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(accountCodePurposeContractRequest.getAccountCodePurpose(),
						"AccountCodePurpose to edit must not be null");
				validator.validate(accountCodePurposeContractRequest.getAccountCodePurpose(), errors);
				break;
			case "search":
				validator.validate(accountCodePurposeContractRequest.getAccountCodePurposeGetRequest(), errors);
				break;
			case "create":
				Assert.notNull(accountCodePurposeContractRequest.getAccountCodePurposes(),
						"AccountCodePurposes to create must not be null");
				for (AccountCodePurposeContract b : accountCodePurposeContractRequest.getAccountCodePurposes()) {
					validator.validate(b, errors);
				}
				break;
			case "updateAll":
				Assert.notNull(accountCodePurposeContractRequest.getAccountCodePurposes(),
						"AccountCodePurposes to create must not be null");
				for (AccountCodePurposeContract b : accountCodePurposeContractRequest.getAccountCodePurposes()) {
					validator.validate(b, errors);
				}
				break;
			default:
				validator.validate(accountCodePurposeContractRequest.getRequestInfo(), errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public AccountCodePurposeContractRequest fetchRelatedContracts(
			AccountCodePurposeContractRequest accountCodePurposeContractRequest) {
		return accountCodePurposeContractRequest;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}
}