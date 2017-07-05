package org.egov.egf.master.domain.service;

import java.util.List;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.AccountDetailKey;
import org.egov.egf.master.domain.model.AccountDetailKeySearch;
import org.egov.egf.master.domain.model.AccountDetailType;
import org.egov.egf.master.domain.repository.AccountDetailKeyRepository;
import org.egov.egf.master.domain.repository.AccountDetailTypeRepository;
import org.egov.egf.master.web.contract.AccountDetailKeyContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class AccountDetailKeyService {

	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String ACTION_VIEW = "view";
	public static final String ACTION_EDIT = "edit";
	public static final String ACTION_SEARCH = "search";

	@Autowired
	private AccountDetailKeyRepository accountDetailKeyRepository;

	@Autowired
	private SmartValidator validator;
	@Autowired
	private AccountDetailTypeRepository accountDetailTypeRepository;

	public BindingResult validate(List<AccountDetailKey> accountdetailkeys, String method, BindingResult errors) {

		try {
			switch (method) {
			case ACTION_VIEW:
				// validator.validate(accountDetailKeyContractRequest.getAccountDetailKey(),
				// errors);
				break;
			case ACTION_CREATE:
				Assert.notNull(accountdetailkeys, "AccountDetailKeys to create must not be null");
				for (AccountDetailKey accountDetailKey : accountdetailkeys) {
					validator.validate(accountDetailKey, errors);
				}
				break;
			case ACTION_UPDATE:
				Assert.notNull(accountdetailkeys, "AccountDetailKeys to update must not be null");
				for (AccountDetailKey accountDetailKey : accountdetailkeys) {
					validator.validate(accountDetailKey, errors);
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<AccountDetailKey> fetchRelated(List<AccountDetailKey> accountdetailkeys) {
		for (AccountDetailKey accountDetailKey : accountdetailkeys) {
			// fetch related items
			if (accountDetailKey.getAccountDetailType() != null) {
				AccountDetailType accountDetailType = accountDetailTypeRepository
						.findById(accountDetailKey.getAccountDetailType());
				if (accountDetailType == null) {
					throw new InvalidDataException("accountDetailType", "accountDetailType.invalid",
							" Invalid accountDetailType");
				}
				accountDetailKey.setAccountDetailType(accountDetailType);
			}

		}

		return accountdetailkeys;
	}

	public List<AccountDetailKey> add(List<AccountDetailKey> accountdetailkeys, BindingResult errors) {
		accountdetailkeys = fetchRelated(accountdetailkeys);
		validate(accountdetailkeys, ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return accountdetailkeys;

	}

	public List<AccountDetailKey> update(List<AccountDetailKey> accountdetailkeys, BindingResult errors) {
		accountdetailkeys = fetchRelated(accountdetailkeys);
		validate(accountdetailkeys, ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return accountdetailkeys;

	}

	public void addToQue(CommonRequest<AccountDetailKeyContract> request) {
		accountDetailKeyRepository.add(request);
	}

	public Pagination<AccountDetailKey> search(AccountDetailKeySearch accountDetailKeySearch) {
		return accountDetailKeyRepository.search(accountDetailKeySearch);
	}

}