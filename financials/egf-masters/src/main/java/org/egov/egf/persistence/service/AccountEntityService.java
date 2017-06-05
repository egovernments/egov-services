package org.egov.egf.persistence.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.domain.exception.InvalidDataException;
import org.egov.egf.json.ObjectMapperFactory;
import org.egov.egf.persistence.entity.AccountDetailType;
import org.egov.egf.persistence.entity.AccountEntity;
import org.egov.egf.persistence.queue.contract.AccountEntityContract;
import org.egov.egf.persistence.queue.contract.AccountEntityContractRequest;
import org.egov.egf.persistence.queue.contract.AccountEntityContractResponse;
import org.egov.egf.persistence.queue.contract.RequestInfo;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
import org.egov.egf.persistence.repository.AccountEntityJpaRepository;
import org.egov.egf.persistence.repository.AccountEntityQueueRepository;
import org.egov.egf.persistence.specification.AccountEntitySpecification;
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
public class AccountEntityService {

	private final AccountEntityJpaRepository accountEntityJpaRepository;

	private final AccountEntityQueueRepository accountEntityQueueRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SmartValidator validator;

	@Autowired
	private AccountDetailTypeService accountDetailTypeService;

	@Autowired
	public AccountEntityService(final AccountEntityJpaRepository accountEntityJpaRepository,
			final AccountEntityQueueRepository accountEntityQueueRepository) {
		this.accountEntityJpaRepository = accountEntityJpaRepository;
		this.accountEntityQueueRepository = accountEntityQueueRepository;
	}

	public void push(final AccountEntityContractRequest accountEntityContractRequest) {
		accountEntityQueueRepository.push(accountEntityContractRequest);
	}

	@Transactional
	public AccountEntityContractResponse create(HashMap<String, Object> financialContractRequestMap) {
		final AccountEntityContractRequest accountEntityContractRequest = ObjectMapperFactory.create().convertValue(
				financialContractRequestMap.get("AccountEntityCreate"), AccountEntityContractRequest.class);
		AccountEntityContractResponse accountEntityContractResponse = new AccountEntityContractResponse();
		accountEntityContractResponse.setAccountEntities(new ArrayList<AccountEntityContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (accountEntityContractRequest.getAccountEntities() != null
				&& !accountEntityContractRequest.getAccountEntities().isEmpty()) {
			for (AccountEntityContract accountEntityContract : accountEntityContractRequest.getAccountEntities()) {
				AccountEntity accountEntityEntity = new AccountEntity(accountEntityContract);
				accountEntityJpaRepository.save(accountEntityEntity);
				AccountEntityContract resp = modelMapper.map(accountEntityEntity, AccountEntityContract.class);
				accountEntityContractResponse.getAccountEntities().add(resp);
			}
		} else if (accountEntityContractRequest.getAccountEntity() != null) {
			AccountEntity accountEntityEntity = new AccountEntity(accountEntityContractRequest.getAccountEntity());
			accountEntityJpaRepository.save(accountEntityEntity);
			AccountEntityContract resp = modelMapper.map(accountEntityEntity, AccountEntityContract.class);
			accountEntityContractResponse.setAccountEntity(resp);
		}
		accountEntityContractResponse.setResponseInfo(getResponseInfo(accountEntityContractRequest.getRequestInfo()));
		return accountEntityContractResponse;
	}

	@Transactional
	public AccountEntityContractResponse updateAll(HashMap<String, Object> financialContractRequestMap) {
		final AccountEntityContractRequest accountEntityContractRequest = ObjectMapperFactory.create().convertValue(
				financialContractRequestMap.get("AccountEntityUpdateAll"), AccountEntityContractRequest.class);
		AccountEntityContractResponse accountEntityContractResponse = new AccountEntityContractResponse();
		accountEntityContractResponse.setAccountEntities(new ArrayList<AccountEntityContract>());
		ModelMapper modelMapper = new ModelMapper();
		if (accountEntityContractRequest.getAccountEntities() != null
				&& !accountEntityContractRequest.getAccountEntities().isEmpty()) {
			for (AccountEntityContract accountEntityContract : accountEntityContractRequest.getAccountEntities()) {
				AccountEntity accountEntityEntity = new AccountEntity(accountEntityContract);
				accountEntityEntity.setVersion(findOne(accountEntityEntity.getId()).getVersion());
				accountEntityJpaRepository.save(accountEntityEntity);
				AccountEntityContract resp = modelMapper.map(accountEntityEntity, AccountEntityContract.class);
				accountEntityContractResponse.getAccountEntities().add(resp);
			}
		}
		accountEntityContractResponse.setResponseInfo(getResponseInfo(accountEntityContractRequest.getRequestInfo()));
		return accountEntityContractResponse;
	}

	@Transactional
	public AccountEntityContractResponse update(HashMap<String, Object> financialContractRequestMap) {
		final AccountEntityContractRequest accountEntityContractRequest = ObjectMapperFactory.create().convertValue(
				financialContractRequestMap.get("AccountEntityUpdate"), AccountEntityContractRequest.class);
		AccountEntityContractResponse accountEntityContractResponse = new AccountEntityContractResponse();
		accountEntityContractResponse.setAccountEntities(new ArrayList<AccountEntityContract>());
		ModelMapper modelMapper = new ModelMapper();
		AccountEntity accountEntityEntity = new AccountEntity(accountEntityContractRequest.getAccountEntity());
		accountEntityEntity.setVersion(accountEntityJpaRepository.findOne(accountEntityEntity.getId()).getVersion());
		accountEntityJpaRepository.save(accountEntityEntity);
		AccountEntityContract resp = modelMapper.map(accountEntityEntity, AccountEntityContract.class);
		accountEntityContractResponse.setAccountEntity(resp);
		accountEntityContractResponse.setResponseInfo(getResponseInfo(accountEntityContractRequest.getRequestInfo()));
		return accountEntityContractResponse;
	}

	@Transactional
	public AccountEntity create(final AccountEntity accountEntity) {
		setAccountEntity(accountEntity);
		return accountEntityJpaRepository.save(accountEntity);
	}

	@Transactional
	public AccountEntity update(final AccountEntity accountEntity) {
		setAccountEntity(accountEntity);
		return accountEntityJpaRepository.save(accountEntity);
	}

	private void setAccountEntity(final AccountEntity accountEntity) {
		if (accountEntity.getAccountDetailType() != null) {
			final AccountDetailType accountDetailType = accountDetailTypeService
					.findOne(accountEntity.getAccountDetailType());
			if (accountDetailType == null)
				throw new InvalidDataException("accountDetailType", "accountDetailType.invalid",
						" Invalid accountDetailType");
			accountEntity.setAccountDetailType(accountDetailType.getId());
		}
	}

	public List<AccountEntity> findAll() {
		return accountEntityJpaRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public AccountEntity findByName(final String name) {
		return accountEntityJpaRepository.findByName(name);
	}

	public AccountEntity findByCode(final String code) {
		return accountEntityJpaRepository.findByCode(code);
	}

	public AccountEntity findOne(final Long id) {
		return accountEntityJpaRepository.findOne(id);
	}

	public Page<AccountEntity> search(final AccountEntityContractRequest accountEntityContractRequest) {
		final AccountEntitySpecification specification = new AccountEntitySpecification(
				accountEntityContractRequest.getAccountEntity());
		final Pageable page = new PageRequest(accountEntityContractRequest.getPage().getOffSet(),
				accountEntityContractRequest.getPage().getPageSize());
		return accountEntityJpaRepository.findAll(specification, page);
	}

	public BindingResult validate(final AccountEntityContractRequest accountEntityContractRequest, final String method,
			final BindingResult errors) {

		try {
			switch (method) {
			case "update":
				Assert.notNull(accountEntityContractRequest.getAccountEntity(),
						"AccountEntity to edit must not be null");
				validator.validate(accountEntityContractRequest.getAccountEntity(), errors);
				break;
			case "view":
				// validator.validate(accountEntityContractRequest.getAccountEntity(),
				// errors);
				break;
			case "create":
				Assert.notNull(accountEntityContractRequest.getAccountEntities(),
						"AccountEntities to create must not be null");
				for (final AccountEntityContract b : accountEntityContractRequest.getAccountEntities())
					validator.validate(b, errors);
				break;
			case "updateAll":
				Assert.notNull(accountEntityContractRequest.getAccountEntities(),
						"AccountEntities to create must not be null");
				for (final AccountEntityContract b : accountEntityContractRequest.getAccountEntities())
					validator.validate(b, errors);
				break;
			default:
				validator.validate(accountEntityContractRequest.getRequestInfo(), errors);
			}
		} catch (final IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public AccountEntityContractRequest fetchRelatedContracts(
			final AccountEntityContractRequest accountEntityContractRequest) {
		final ModelMapper model = new ModelMapper();
		for (final AccountEntityContract accountEntity : accountEntityContractRequest.getAccountEntities())
			if (accountEntity.getAccountDetailType() != null) {
				final AccountDetailType accountDetailType = accountDetailTypeService
						.findOne(accountEntity.getAccountDetailType().getId());
				if (accountDetailType == null)
					throw new InvalidDataException("accountDetailType", "accountDetailType.invalid",
							" Invalid accountDetailType");
				model.map(accountDetailType, accountEntity.getAccountDetailType());
			}
		final AccountEntityContract accountEntity = accountEntityContractRequest.getAccountEntity();
		if (accountEntity.getAccountDetailType() != null) {
			final AccountDetailType accountDetailType = accountDetailTypeService
					.findOne(accountEntity.getAccountDetailType().getId());
			if (accountDetailType == null)
				throw new InvalidDataException("accountDetailType", "accountDetailType.invalid",
						" Invalid accountDetailType");
			model.map(accountDetailType, accountEntity.getAccountDetailType());
		}
		return accountEntityContractRequest;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).ts(new Date())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}
}