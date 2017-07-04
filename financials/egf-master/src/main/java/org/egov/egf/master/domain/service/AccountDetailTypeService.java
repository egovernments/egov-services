package org.egov.egf.master.domain.service;

import java.util.List;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.AccountDetailType;
import org.egov.egf.master.domain.model.AccountDetailTypeSearch;
import org.egov.egf.master.domain.repository.AccountDetailTypeRepository;
import org.egov.egf.master.web.contract.AccountDetailTypeContract;
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

	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String ACTION_VIEW = "view";
	public static final String ACTION_EDIT = "edit";
	public static final String ACTION_SEARCH = "search";

	@Autowired
	private AccountDetailTypeRepository accountDetailTypeRepository;

	@Autowired
	private SmartValidator validator;

	public BindingResult validate(List<AccountDetailType> accountdetailtypes, String method, BindingResult errors) {

		try {
			switch (method) {
			case ACTION_VIEW:
				// validator.validate(accountDetailTypeContractRequest.getAccountDetailType(),
				// errors);
				break;
			case ACTION_CREATE:
				Assert.notNull(accountdetailtypes, "AccountDetailTypes to create must not be null");
				for (AccountDetailType accountDetailType : accountdetailtypes) {
					validator.validate(accountDetailType, errors);
				}
				break;
			case ACTION_UPDATE:
				Assert.notNull(accountdetailtypes, "AccountDetailTypes to update must not be null");
				for (AccountDetailType accountDetailType : accountdetailtypes) {
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

	public List<AccountDetailType> add(List<AccountDetailType> accountdetailtypes, BindingResult errors) {
		accountdetailtypes = fetchRelated(accountdetailtypes);
		validate(accountdetailtypes, ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return accountdetailtypes;

	}

	public List<AccountDetailType> update(List<AccountDetailType> accountdetailtypes, BindingResult errors) {
		accountdetailtypes = fetchRelated(accountdetailtypes);
		validate(accountdetailtypes, ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return accountdetailtypes;

	}

	public void addToQue(CommonRequest<AccountDetailTypeContract> request) {
		accountDetailTypeRepository.add(request);
	}

	public Pagination<AccountDetailType> search(AccountDetailTypeSearch accountDetailTypeSearch) {
		return accountDetailTypeRepository.search(accountDetailTypeSearch);
	}

}