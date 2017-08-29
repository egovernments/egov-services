package org.egov.egf.master.domain.service;

import java.util.List;

import org.egov.common.constants.Constants;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.AccountDetailType;
import org.egov.egf.master.domain.model.AccountDetailTypeSearch;
import org.egov.egf.master.domain.model.Bank;
import org.egov.egf.master.domain.repository.AccountDetailTypeRepository;
import org.egov.egf.master.web.requests.AccountDetailTypeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class AccountDetailTypeService {

	@Autowired
	private AccountDetailTypeRepository accountDetailTypeRepository;

	@Autowired
	private SmartValidator validator;

	private BindingResult validate(List<AccountDetailType> accountdetailtypes, String method, BindingResult errors) {

		try {
			switch (method) {
			case Constants.ACTION_VIEW:
				// validator.validate(accountDetailTypeContractRequest.getAccountDetailType(),
				// errors);
				break;
			case Constants.ACTION_CREATE:
				Assert.notNull(accountdetailtypes, "AccountDetailTypes to create must not be null");
				for (AccountDetailType accountDetailType : accountdetailtypes) {
					validator.validate(accountDetailType, errors);
				}
				break;
			case Constants.ACTION_UPDATE:
				Assert.notNull(accountdetailtypes, "AccountDetailTypes to update must not be null");
				for (AccountDetailType accountDetailType : accountdetailtypes) {
				        Assert.notNull(accountDetailType.getId(), "Account Detail Type ID to update must not be null");
					validator.validate(accountDetailType, errors);
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<AccountDetailType> fetchRelated(List<AccountDetailType> accountdetailtypes) {
		for (AccountDetailType accountDetailType : accountdetailtypes) {
			// fetch related items

		}

		return accountdetailtypes;
	}

	@Transactional
	public List<AccountDetailType> add(List<AccountDetailType> accountdetailtypes, BindingResult errors) {
		accountdetailtypes = fetchRelated(accountdetailtypes);
		validate(accountdetailtypes, Constants.ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		for(AccountDetailType b:accountdetailtypes)b.setId(accountDetailTypeRepository.getNextSequence());
		return accountdetailtypes;

	}

	@Transactional
	public List<AccountDetailType> update(List<AccountDetailType> accountdetailtypes, BindingResult errors) {
		accountdetailtypes = fetchRelated(accountdetailtypes);
		validate(accountdetailtypes, Constants.ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return accountdetailtypes;

	}

	public void addToQue(AccountDetailTypeRequest request) {
		accountDetailTypeRepository.add(request);
	}

	public Pagination<AccountDetailType> search(AccountDetailTypeSearch accountDetailTypeSearch) {
	        Assert.notNull(accountDetailTypeSearch.getTenantId(), "tenantId is mandatory for accountDetailType search");
		return accountDetailTypeRepository.search(accountDetailTypeSearch);
	}

	@Transactional
	public AccountDetailType save(AccountDetailType accountDetailType) {
		return accountDetailTypeRepository.save(accountDetailType);
	}

	@Transactional
	public AccountDetailType update(AccountDetailType accountDetailType) {
		return accountDetailTypeRepository.update(accountDetailType);
	}

}