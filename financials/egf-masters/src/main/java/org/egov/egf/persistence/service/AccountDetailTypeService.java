package org.egov.egf.persistence.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.json.ObjectMapperFactory;
import org.egov.egf.persistence.entity.AccountDetailType;
import org.egov.egf.persistence.queue.contract.AccountDetailTypeContract;
import org.egov.egf.persistence.queue.contract.AccountDetailTypeContractRequest;
import org.egov.egf.persistence.queue.contract.AccountDetailTypeContractResponse;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.repository.AccountDetailTypeJpaRepository;
import org.egov.egf.persistence.repository.AccountDetailTypeQueueRepository;
import org.egov.egf.persistence.specification.AccountDetailTypeSpecification;
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
public class AccountDetailTypeService {

	private final AccountDetailTypeJpaRepository accountDetailTypeJpaRepository;

	private final AccountDetailTypeQueueRepository accountDetailTypeQueueRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SmartValidator validator;

	@Autowired
	public AccountDetailTypeService(final AccountDetailTypeJpaRepository accountDetailTypeJpaRepository,
			final AccountDetailTypeQueueRepository accountDetailTypeQueueRepository) {
		this.accountDetailTypeJpaRepository = accountDetailTypeJpaRepository;
		this.accountDetailTypeQueueRepository = accountDetailTypeQueueRepository;
	}

	public void push(final AccountDetailTypeContractRequest accountDetailTypeContractRequest) {
		accountDetailTypeQueueRepository.push(accountDetailTypeContractRequest);
	}

	@Transactional
	public AccountDetailTypeContractResponse create(HashMap<String, Object> financialContractRequestMap) {
		final AccountDetailTypeContractRequest accountDetailTypeContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("AccountDetailTypeCreate"),
						AccountDetailTypeContractRequest.class);
		AccountDetailTypeContractResponse accountDetailTypeContractResponse = new AccountDetailTypeContractResponse();
		accountDetailTypeContractResponse.setAccountDetailTypes(new ArrayList<AccountDetailTypeContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (accountDetailTypeContractRequest.getAccountDetailTypes() != null
				&& !accountDetailTypeContractRequest.getAccountDetailTypes().isEmpty()) {
			for (AccountDetailTypeContract accountDetailTypeContract : accountDetailTypeContractRequest
					.getAccountDetailTypes()) {
				AccountDetailType accountDetailTypeEntity = new AccountDetailType(accountDetailTypeContract);
				accountDetailTypeJpaRepository.save(accountDetailTypeEntity);
				AccountDetailTypeContract resp = modelMapper.map(accountDetailTypeEntity,
						AccountDetailTypeContract.class);
				accountDetailTypeContractResponse.getAccountDetailTypes().add(resp);
			}
		} else if (accountDetailTypeContractRequest.getAccountDetailType() != null) {
			AccountDetailType accountDetailTypeEntity = new AccountDetailType(
					accountDetailTypeContractRequest.getAccountDetailType());
			accountDetailTypeJpaRepository.save(accountDetailTypeEntity);
			AccountDetailTypeContract resp = modelMapper.map(accountDetailTypeEntity, AccountDetailTypeContract.class);
			accountDetailTypeContractResponse.setAccountDetailType(resp);
		}
		accountDetailTypeContractResponse
				.setResponseInfo(getResponseInfo(accountDetailTypeContractRequest.getRequestInfo()));
		return accountDetailTypeContractResponse;
	}
	
	@Transactional
	public AccountDetailTypeContractResponse updateAll(HashMap<String, Object> financialContractRequestMap) {
		final AccountDetailTypeContractRequest accountDetailTypeContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("AccountDetailTypeUpdateAll"),
						AccountDetailTypeContractRequest.class);
		AccountDetailTypeContractResponse accountDetailTypeContractResponse = new AccountDetailTypeContractResponse();
		accountDetailTypeContractResponse.setAccountDetailTypes(new ArrayList<AccountDetailTypeContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (accountDetailTypeContractRequest.getAccountDetailTypes() != null
				&& !accountDetailTypeContractRequest.getAccountDetailTypes().isEmpty()) {
			for (AccountDetailTypeContract accountDetailTypeContract : accountDetailTypeContractRequest
					.getAccountDetailTypes()) {
				AccountDetailType accountDetailTypeEntity = new AccountDetailType(accountDetailTypeContract);
				accountDetailTypeEntity.setVersion(findOne(accountDetailTypeEntity.getId()).getVersion());
				accountDetailTypeJpaRepository.save(accountDetailTypeEntity);
				AccountDetailTypeContract resp = modelMapper.map(accountDetailTypeEntity,
						AccountDetailTypeContract.class);
				accountDetailTypeContractResponse.getAccountDetailTypes().add(resp);
			}
		} 
		accountDetailTypeContractResponse
				.setResponseInfo(getResponseInfo(accountDetailTypeContractRequest.getRequestInfo()));
		return accountDetailTypeContractResponse;
	}
	
	
	@Transactional
	public AccountDetailTypeContractResponse update(HashMap<String, Object> financialContractRequestMap) {
		final AccountDetailTypeContractRequest accountDetailTypeContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("AccountDetailTypeUpdate"),
						AccountDetailTypeContractRequest.class);
		AccountDetailTypeContractResponse accountDetailTypeContractResponse = new AccountDetailTypeContractResponse();
		accountDetailTypeContractResponse.setAccountDetailTypes(new ArrayList<AccountDetailTypeContract>());
		ModelMapper modelMapper = new ModelMapper();
		AccountDetailType accountDetailTypeEntity = new AccountDetailType(
				accountDetailTypeContractRequest.getAccountDetailType());
		accountDetailTypeEntity
				.setVersion(accountDetailTypeJpaRepository.findOne(accountDetailTypeEntity.getId()).getVersion());
		accountDetailTypeJpaRepository.save(accountDetailTypeEntity);
		AccountDetailTypeContract resp = modelMapper.map(accountDetailTypeEntity, AccountDetailTypeContract.class);
		accountDetailTypeContractResponse.setAccountDetailType(resp);
		accountDetailTypeContractResponse
				.setResponseInfo(getResponseInfo(accountDetailTypeContractRequest.getRequestInfo()));
		return accountDetailTypeContractResponse;
	}

	@Transactional
	public AccountDetailType create(final AccountDetailType accountDetailType) {
		return accountDetailTypeJpaRepository.save(accountDetailType);
	}

	@Transactional
	public AccountDetailType update(final AccountDetailType accountDetailType) {
		return accountDetailTypeJpaRepository.save(accountDetailType);
	}

	public List<AccountDetailType> findAll() {
		return accountDetailTypeJpaRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public AccountDetailType findByName(String name) {
		return accountDetailTypeJpaRepository.findByName(name);
	}

	public AccountDetailType findOne(Long id) {
		return accountDetailTypeJpaRepository.findOne(id);
	}

	public Page<AccountDetailType> search(AccountDetailTypeContractRequest accountDetailTypeContractRequest) {
		final AccountDetailTypeSpecification specification = new AccountDetailTypeSpecification(
				accountDetailTypeContractRequest.getAccountDetailType());
		Pageable page = new PageRequest(accountDetailTypeContractRequest.getPage().getOffSet(),
				accountDetailTypeContractRequest.getPage().getPageSize());
		return accountDetailTypeJpaRepository.findAll(specification, page);
	}

	public BindingResult validate(AccountDetailTypeContractRequest accountDetailTypeContractRequest, String method,
			BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(accountDetailTypeContractRequest.getAccountDetailType(),
						"AccountDetailType to edit must not be null");
				validator.validate(accountDetailTypeContractRequest.getAccountDetailType(), errors);
				break;
			case "view":
				// validator.validate(accountDetailTypeContractRequest.getAccountDetailType(),
				// errors);
				break;
			case "create":
				Assert.notNull(accountDetailTypeContractRequest.getAccountDetailTypes(),
						"AccountDetailTypes to create must not be null");
				for (AccountDetailTypeContract b : accountDetailTypeContractRequest.getAccountDetailTypes()) {
					validator.validate(b, errors);
				}
				break;
			case "updateAll":
				Assert.notNull(accountDetailTypeContractRequest.getAccountDetailTypes(),
						"AccountDetailTypes to create must not be null");
				for (AccountDetailTypeContract b : accountDetailTypeContractRequest.getAccountDetailTypes()) {
					validator.validate(b, errors);
				}
				break;
			default:
				validator.validate(accountDetailTypeContractRequest.getRequestInfo(), errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public AccountDetailTypeContractRequest fetchRelatedContracts(
			AccountDetailTypeContractRequest accountDetailTypeContractRequest) {
		return accountDetailTypeContractRequest;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}
}