package org.egov.egf.persistence.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.domain.exception.InvalidDataException;
import org.egov.egf.json.ObjectMapperFactory;
import org.egov.egf.persistence.entity.AccountDetailKey;
import org.egov.egf.persistence.entity.AccountDetailType;
import org.egov.egf.persistence.queue.contract.AccountDetailKeyContract;
import org.egov.egf.persistence.queue.contract.AccountDetailKeyContractRequest;
import org.egov.egf.persistence.queue.contract.AccountDetailKeyContractResponse;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.repository.AccountDetailKeyJpaRepository;
import org.egov.egf.persistence.repository.AccountDetailKeyQueueRepository;
import org.egov.egf.persistence.specification.AccountDetailKeySpecification;
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
public class AccountDetailKeyService {

	private final AccountDetailKeyJpaRepository accountDetailKeyJpaRepository;

	private final AccountDetailKeyQueueRepository accountDetailKeyQueueRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SmartValidator validator;

	@Autowired
	private AccountDetailTypeService accountDetailTypeService;

	@Autowired
	public AccountDetailKeyService(final AccountDetailKeyJpaRepository accountDetailKeyJpaRepository,
			final AccountDetailKeyQueueRepository accountDetailKeyQueueRepository) {
		this.accountDetailKeyJpaRepository = accountDetailKeyJpaRepository;
		this.accountDetailKeyQueueRepository = accountDetailKeyQueueRepository;
	}

	public void push(final AccountDetailKeyContractRequest accountDetailKeyContractRequest) {
		accountDetailKeyQueueRepository.push(accountDetailKeyContractRequest);
	}

	@Transactional
	public AccountDetailKeyContractResponse create(HashMap<String, Object> financialContractRequestMap) {
		final AccountDetailKeyContractRequest accountDetailKeyContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("AccountDetailKeyCreate"),
						AccountDetailKeyContractRequest.class);
		AccountDetailKeyContractResponse accountDetailKeyContractResponse = new AccountDetailKeyContractResponse();
		accountDetailKeyContractResponse.setAccountDetailKeys(new ArrayList<AccountDetailKeyContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (accountDetailKeyContractRequest.getAccountDetailKeys() != null
				&& !accountDetailKeyContractRequest.getAccountDetailKeys().isEmpty()) {
			for (AccountDetailKeyContract accountDetailKeyContract : accountDetailKeyContractRequest
					.getAccountDetailKeys()) {
				AccountDetailKey accountDetailKeyEntity = new AccountDetailKey(accountDetailKeyContract);
				accountDetailKeyJpaRepository.save(accountDetailKeyEntity);
				AccountDetailKeyContract resp = modelMapper.map(accountDetailKeyEntity, AccountDetailKeyContract.class);
				accountDetailKeyContractResponse.getAccountDetailKeys().add(resp);
			}
		} else if (accountDetailKeyContractRequest.getAccountDetailKey() != null) {
			AccountDetailKey accountDetailKeyEntity = new AccountDetailKey(
					accountDetailKeyContractRequest.getAccountDetailKey());
			accountDetailKeyJpaRepository.save(accountDetailKeyEntity);
			AccountDetailKeyContract resp = modelMapper.map(accountDetailKeyEntity, AccountDetailKeyContract.class);
			accountDetailKeyContractResponse.setAccountDetailKey(resp);
		}
		accountDetailKeyContractResponse
				.setResponseInfo(getResponseInfo(accountDetailKeyContractRequest.getRequestInfo()));
		return accountDetailKeyContractResponse;
	}

	@Transactional
	public AccountDetailKeyContractResponse updateAll(HashMap<String, Object> financialContractRequestMap) {
		final AccountDetailKeyContractRequest accountDetailKeyContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("AccountDetailKeyUpdateAll"),
						AccountDetailKeyContractRequest.class);
		AccountDetailKeyContractResponse accountDetailKeyContractResponse = new AccountDetailKeyContractResponse();
		accountDetailKeyContractResponse.setAccountDetailKeys(new ArrayList<AccountDetailKeyContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (accountDetailKeyContractRequest.getAccountDetailKeys() != null
				&& !accountDetailKeyContractRequest.getAccountDetailKeys().isEmpty()) {
			for (AccountDetailKeyContract accountDetailKeyContract : accountDetailKeyContractRequest
					.getAccountDetailKeys()) {
				AccountDetailKey accountDetailKeyEntity = new AccountDetailKey(accountDetailKeyContract);
				accountDetailKeyEntity.setVersion(findOne(accountDetailKeyEntity.getId()).getVersion());
				accountDetailKeyJpaRepository.save(accountDetailKeyEntity);
				AccountDetailKeyContract resp = modelMapper.map(accountDetailKeyEntity, AccountDetailKeyContract.class);
				accountDetailKeyContractResponse.getAccountDetailKeys().add(resp);
			}
		}
		accountDetailKeyContractResponse
				.setResponseInfo(getResponseInfo(accountDetailKeyContractRequest.getRequestInfo()));
		return accountDetailKeyContractResponse;
	}

	@Transactional
	public AccountDetailKeyContractResponse update(HashMap<String, Object> financialContractRequestMap) {
		final AccountDetailKeyContractRequest accountDetailKeyContractRequest = ObjectMapperFactory.create()
				.convertValue(financialContractRequestMap.get("AccountDetailKeyUpdate"),
						AccountDetailKeyContractRequest.class);
		AccountDetailKeyContractResponse accountDetailKeyContractResponse = new AccountDetailKeyContractResponse();
		accountDetailKeyContractResponse.setAccountDetailKeys(new ArrayList<AccountDetailKeyContract>());
		ModelMapper modelMapper = new ModelMapper();
		AccountDetailKey accountDetailKeyEntity = new AccountDetailKey(
				accountDetailKeyContractRequest.getAccountDetailKey());
		accountDetailKeyEntity
				.setVersion(accountDetailKeyJpaRepository.findOne(accountDetailKeyEntity.getId()).getVersion());
		accountDetailKeyJpaRepository.save(accountDetailKeyEntity);
		AccountDetailKeyContract resp = modelMapper.map(accountDetailKeyEntity, AccountDetailKeyContract.class);
		accountDetailKeyContractResponse.setAccountDetailKey(resp);
		accountDetailKeyContractResponse
				.setResponseInfo(getResponseInfo(accountDetailKeyContractRequest.getRequestInfo()));
		return accountDetailKeyContractResponse;
	}

	@Transactional
	public AccountDetailKey create(final AccountDetailKey accountDetailKey) {
		// setAccountDetailKey(accountDetailKey);
		return accountDetailKeyJpaRepository.save(accountDetailKey);
	}

	private void setAccountDetailKey(final AccountDetailKey accountDetailKey) {
		if (accountDetailKey.getAccountDetailType() != null) {
			final AccountDetailType accountDetailType = accountDetailTypeService
					.findOne(accountDetailKey.getAccountDetailType());
			if (accountDetailType == null)
				throw new InvalidDataException("accountDetailType", "accountDetailType.invalid",
						" Invalid accountDetailType");
			accountDetailKey.setAccountDetailType(accountDetailType.getId());
		}
	}

	@Transactional
	public AccountDetailKey update(final AccountDetailKey accountDetailKey) {
		setAccountDetailKey(accountDetailKey);
		return accountDetailKeyJpaRepository.save(accountDetailKey);
	}

	public List<AccountDetailKey> findAll() {
		return accountDetailKeyJpaRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public AccountDetailKey findOne(final Long id) {
		return accountDetailKeyJpaRepository.findOne(id);
	}

	public Page<AccountDetailKey> search(final AccountDetailKeyContractRequest accountDetailKeyContractRequest) {
		final AccountDetailKeySpecification specification = new AccountDetailKeySpecification(
				accountDetailKeyContractRequest.getAccountDetailKey());
		final Pageable page = new PageRequest(accountDetailKeyContractRequest.getPage().getOffSet(),
				accountDetailKeyContractRequest.getPage().getPageSize());
		return accountDetailKeyJpaRepository.findAll(specification, page);
	}

	public BindingResult validate(final AccountDetailKeyContractRequest accountDetailKeyContractRequest,
			final String method, final BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(accountDetailKeyContractRequest.getAccountDetailKey(),
						"AccountDetailKey to edit must not be null");
				validator.validate(accountDetailKeyContractRequest.getAccountDetailKey(), errors);
				break;
			case "view":
				// validator.validate(accountDetailKeyContractRequest.getAccountDetailKey(),
				// errors);
				break;
			case "create":
				Assert.notNull(accountDetailKeyContractRequest.getAccountDetailKeys(),
						"AccountDetailKeys to create must not be null");
				for (final AccountDetailKeyContract b : accountDetailKeyContractRequest.getAccountDetailKeys())
					validator.validate(b, errors);
				break;
			case "updateAll":
				Assert.notNull(accountDetailKeyContractRequest.getAccountDetailKeys(),
						"AccountDetailKeys to create must not be null");
				for (final AccountDetailKeyContract b : accountDetailKeyContractRequest.getAccountDetailKeys())
					validator.validate(b, errors);
				break;
			default:
				validator.validate(accountDetailKeyContractRequest.getRequestInfo(), errors);
			}
		} catch (final IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public AccountDetailKeyContractRequest fetchRelatedContracts(
			final AccountDetailKeyContractRequest accountDetailKeyContractRequest) {
		final ModelMapper model = new ModelMapper();
		for (final AccountDetailKeyContract accountDetailKey : accountDetailKeyContractRequest.getAccountDetailKeys())
			if (accountDetailKey.getAccountDetailType() != null) {
				final AccountDetailType accountDetailType = accountDetailTypeService
						.findOne(accountDetailKey.getAccountDetailType().getId());
				if (accountDetailType == null)
					throw new InvalidDataException("accountDetailType", "accountDetailType.invalid",
							" Invalid accountDetailType");
				model.map(accountDetailType, accountDetailKey.getAccountDetailType());
			}
		final AccountDetailKeyContract accountDetailKey = accountDetailKeyContractRequest.getAccountDetailKey();
		if (accountDetailKey.getAccountDetailType() != null) {
			final AccountDetailType accountDetailType = accountDetailTypeService
					.findOne(accountDetailKey.getAccountDetailType().getId());
			if (accountDetailType == null)
				throw new InvalidDataException("accountDetailType", "accountDetailType.invalid",
						" Invalid accountDetailType");
			model.map(accountDetailType, accountDetailKey.getAccountDetailType());
		}
		return accountDetailKeyContractRequest;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}
}