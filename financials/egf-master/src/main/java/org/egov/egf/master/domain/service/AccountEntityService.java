package org.egov.egf.master.domain.service;

import java.util.List;

import org.egov.common.constants.EgfConstants;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.AccountDetailType;
import org.egov.egf.master.domain.model.AccountEntity;
import org.egov.egf.master.domain.model.AccountEntitySearch;
import org.egov.egf.master.domain.repository.AccountDetailTypeRepository;
import org.egov.egf.master.domain.repository.AccountEntityRepository;
import org.egov.egf.master.web.requests.AccountEntityRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class AccountEntityService {

	@Autowired
	private AccountEntityRepository accountEntityRepository;

	@Autowired
	private SmartValidator validator;
	@Autowired
	private AccountDetailTypeRepository accountDetailTypeRepository;

	private BindingResult validate(List<AccountEntity> accountentities, String method, BindingResult errors) {

		try {
			switch (method) {
			case EgfConstants.ACTION_VIEW:
				// validator.validate(accountEntityContractRequest.getAccountEntity(),
				// errors);
				break;
			case EgfConstants.ACTION_CREATE:
				Assert.notNull(accountentities, "AccountEntities to create must not be null");
				for (AccountEntity accountEntity : accountentities) {
					validator.validate(accountEntity, errors);
				}
				break;
			case EgfConstants.ACTION_UPDATE:
				Assert.notNull(accountentities, "AccountEntities to update must not be null");
				for (AccountEntity accountEntity : accountentities) {
					validator.validate(accountEntity, errors);
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<AccountEntity> fetchRelated(List<AccountEntity> accountentities) {
		for (AccountEntity accountEntity : accountentities) {
			// fetch related items
			if (accountEntity.getAccountDetailType() != null) {
				AccountDetailType accountDetailType = accountDetailTypeRepository
						.findById(accountEntity.getAccountDetailType());
				if (accountDetailType == null) {
					throw new InvalidDataException("accountDetailType", "accountDetailType.invalid",
							" Invalid accountDetailType");
				}
				accountEntity.setAccountDetailType(accountDetailType);
			}

		}

		return accountentities;
	}

	@Transactional
	public List<AccountEntity> add(List<AccountEntity> accountentities, BindingResult errors) {
		accountentities = fetchRelated(accountentities);
		validate(accountentities, EgfConstants.ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return accountentities;

	}

	@Transactional
	public List<AccountEntity> update(List<AccountEntity> accountentities, BindingResult errors) {
		accountentities = fetchRelated(accountentities);
		validate(accountentities, EgfConstants.ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return accountentities;

	}

	public void addToQue(AccountEntityRequest request) {
		accountEntityRepository.add(request);
	}

	public Pagination<AccountEntity> search(AccountEntitySearch accountEntitySearch) {
		return accountEntityRepository.search(accountEntitySearch);
	}

	@Transactional
	public AccountEntity save(AccountEntity accountEntity) {
		return accountEntityRepository.save(accountEntity);
	}

	@Transactional
	public AccountEntity update(AccountEntity accountEntity) {
		return accountEntityRepository.update(accountEntity);
	}

}