package org.egov.egf.master.domain.service;

import java.util.List;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.AccountCodePurpose;
import org.egov.egf.master.domain.model.AccountCodePurposeSearch;
import org.egov.egf.master.domain.repository.AccountCodePurposeRepository;
import org.egov.egf.master.web.contract.AccountCodePurposeContract;
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

	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String ACTION_VIEW = "view";
	public static final String ACTION_EDIT = "edit";
	public static final String ACTION_SEARCH = "search";

	@Autowired
	private AccountCodePurposeRepository accountCodePurposeRepository;

	@Autowired
	private SmartValidator validator;

	public BindingResult validate(List<AccountCodePurpose> accountcodepurposes, String method, BindingResult errors) {

		try {
			switch (method) {
			case ACTION_VIEW:
				// validator.validate(accountCodePurposeContractRequest.getAccountCodePurpose(),
				// errors);
				break;
			case ACTION_CREATE:
				Assert.notNull(accountcodepurposes, "AccountCodePurposes to create must not be null");
				for (AccountCodePurpose accountCodePurpose : accountcodepurposes) {
					validator.validate(accountCodePurpose, errors);
				}
				break;
			case ACTION_UPDATE:
				Assert.notNull(accountcodepurposes, "AccountCodePurposes to update must not be null");
				for (AccountCodePurpose accountCodePurpose : accountcodepurposes) {
					validator.validate(accountCodePurpose, errors);
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<AccountCodePurpose> fetchRelated(List<AccountCodePurpose> accountcodepurposes) {
		for (AccountCodePurpose accountCodePurpose : accountcodepurposes) {
			// fetch related items

		}

		return accountcodepurposes;
	}

	public List<AccountCodePurpose> add(List<AccountCodePurpose> accountcodepurposes, BindingResult errors) {
		accountcodepurposes = fetchRelated(accountcodepurposes);
		validate(accountcodepurposes, ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return accountcodepurposes;

	}

	public List<AccountCodePurpose> update(List<AccountCodePurpose> accountcodepurposes, BindingResult errors) {
		accountcodepurposes = fetchRelated(accountcodepurposes);
		validate(accountcodepurposes, ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return accountcodepurposes;

	}

	public void addToQue(CommonRequest<AccountCodePurposeContract> request) {
		accountCodePurposeRepository.add(request);
	}

	public Pagination<AccountCodePurpose> search(AccountCodePurposeSearch accountCodePurposeSearch) {
		return accountCodePurposeRepository.search(accountCodePurposeSearch);
	}

}