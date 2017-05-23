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
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.repository.AccountCodePurposeJpaRepository;
import org.egov.egf.persistence.repository.AccountCodePurposeQueueRepository;
import org.egov.egf.persistence.specification.AccountCodePurposeSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class AccountCodePurposeService {

	private final AccountCodePurposeJpaRepository accountCodePurposeJpaRepository;

	private final AccountCodePurposeQueueRepository accountCodePurposeQueueRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public AccountCodePurposeService(final AccountCodePurposeJpaRepository accountCodePurposeJpaRepository,
			final AccountCodePurposeQueueRepository accountCodePurposeQueueRepository) {
		this.accountCodePurposeJpaRepository = accountCodePurposeJpaRepository;
		this.accountCodePurposeQueueRepository = accountCodePurposeQueueRepository;
	}

	@Autowired
	private SmartValidator validator;

	public void push(final AccountCodePurposeContractRequest accountCodePurposeContractRequest) {
		accountCodePurposeQueueRepository.push(accountCodePurposeContractRequest);
	}

	@Transactional
	public AccountCodePurposeContractResponse create(HashMap<String, Object> financialContractRequestMap) {
		final AccountCodePurposeContractRequest accountCodePurposeContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("AccountCodePurposeCreate"),
						AccountCodePurposeContractRequest.class);
		AccountCodePurposeContractResponse accountCodePurposeContractResponse = new AccountCodePurposeContractResponse();
		accountCodePurposeContractResponse.setAccountCodePurposes(new ArrayList<AccountCodePurposeContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (accountCodePurposeContractRequest.getAccountCodePurposes() != null
				&& !accountCodePurposeContractRequest.getAccountCodePurposes().isEmpty()) {
			for (AccountCodePurposeContract accountCodePurposeContract : accountCodePurposeContractRequest
					.getAccountCodePurposes()) {
				AccountCodePurpose accountCodePurposeEntity = new AccountCodePurpose(accountCodePurposeContract);
				accountCodePurposeJpaRepository.save(accountCodePurposeEntity);
				AccountCodePurposeContract resp = modelMapper.map(accountCodePurposeEntity,
						AccountCodePurposeContract.class);
				accountCodePurposeContractResponse.getAccountCodePurposes().add(resp);
			}
		} else if (accountCodePurposeContractRequest.getAccountCodePurpose() != null) {
			AccountCodePurpose accountCodePurposeEntity = new AccountCodePurpose(
					accountCodePurposeContractRequest.getAccountCodePurpose());
			accountCodePurposeJpaRepository.save(accountCodePurposeEntity);
			AccountCodePurposeContract resp = modelMapper.map(accountCodePurposeEntity,
					AccountCodePurposeContract.class);
			accountCodePurposeContractResponse.setAccountCodePurpose(resp);
		}
		accountCodePurposeContractResponse
				.setResponseInfo(getResponseInfo(accountCodePurposeContractRequest.getRequestInfo()));
		return accountCodePurposeContractResponse;
	}

	@Transactional
	public AccountCodePurposeContractResponse update(HashMap<String, Object> financialContractRequestMap) {
		final AccountCodePurposeContractRequest accountCodePurposeContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("AccountCodePurposeUpdate"),
						AccountCodePurposeContractRequest.class);
		AccountCodePurposeContractResponse accountCodePurposeContractResponse = new AccountCodePurposeContractResponse();
		accountCodePurposeContractResponse.setAccountCodePurposes(new ArrayList<AccountCodePurposeContract>());
		ModelMapper modelMapper = new ModelMapper();
		AccountCodePurpose accountCodePurposeEntity = new AccountCodePurpose(
				accountCodePurposeContractRequest.getAccountCodePurpose());
		accountCodePurposeEntity
				.setVersion(accountCodePurposeJpaRepository.findOne(accountCodePurposeEntity.getId()).getVersion());
		accountCodePurposeJpaRepository.save(accountCodePurposeEntity);
		AccountCodePurposeContract resp = modelMapper.map(accountCodePurposeEntity, AccountCodePurposeContract.class);
		accountCodePurposeContractResponse.setAccountCodePurpose(resp);
		accountCodePurposeContractResponse
				.setResponseInfo(getResponseInfo(accountCodePurposeContractRequest.getRequestInfo()));
		return accountCodePurposeContractResponse;
	}

	@Transactional
	public AccountCodePurpose create(final AccountCodePurpose accountCodePurpose) {
		return accountCodePurposeJpaRepository.save(accountCodePurpose);
	}

	@Transactional
	public AccountCodePurpose update(final AccountCodePurpose accountCodePurpose) {
		return accountCodePurposeJpaRepository.save(accountCodePurpose);
	}

	public List<AccountCodePurpose> findAll() {
		return accountCodePurposeJpaRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public AccountCodePurpose findByName(String name) {
		return accountCodePurposeJpaRepository.findByName(name);
	}

	public AccountCodePurpose findOne(Long id) {
		return accountCodePurposeJpaRepository.findOne(id);
	}

	public Page<AccountCodePurpose> search(AccountCodePurposeContractRequest accountCodePurposeContractRequest) {
		final AccountCodePurposeSpecification specification = new AccountCodePurposeSpecification(
				accountCodePurposeContractRequest.getAccountCodePurpose());
		Pageable page = new PageRequest(accountCodePurposeContractRequest.getPage().getOffSet(),
				accountCodePurposeContractRequest.getPage().getPageSize());
		return accountCodePurposeJpaRepository.findAll(specification, page);
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
			case "view":
				// validator.validate(accountCodePurposeContractRequest.getAccountCodePurpose(),
				// errors);
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