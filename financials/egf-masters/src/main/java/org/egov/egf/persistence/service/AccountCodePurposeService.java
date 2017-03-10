package org.egov.egf.persistence.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.persistence.entity.AccountCodePurpose;
import org.egov.egf.persistence.queue.contract.AccountCodePurposeContract;
import org.egov.egf.persistence.queue.contract.AccountCodePurposeContractRequest;
import org.egov.egf.persistence.repository.AccountCodePurposeRepository;
import org.egov.egf.persistence.specification.AccountCodePurposeSpecification;
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

	private final AccountCodePurposeRepository accountCodePurposeRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public AccountCodePurposeService(final AccountCodePurposeRepository accountCodePurposeRepository) {
		this.accountCodePurposeRepository = accountCodePurposeRepository;
	}

	@Autowired
	private SmartValidator validator;

	@Transactional
	public AccountCodePurpose create(final AccountCodePurpose accountCodePurpose) {
		return accountCodePurposeRepository.save(accountCodePurpose);
	}

	@Transactional
	public AccountCodePurpose update(final AccountCodePurpose accountCodePurpose) {
		return accountCodePurposeRepository.save(accountCodePurpose);
	}

	public List<AccountCodePurpose> findAll() {
		return accountCodePurposeRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public AccountCodePurpose findByName(String name) {
		return accountCodePurposeRepository.findByName(name);
	}

	public AccountCodePurpose findOne(Long id) {
		return accountCodePurposeRepository.findOne(id);
	}

	public Page<AccountCodePurpose> search(AccountCodePurposeContractRequest accountCodePurposeContractRequest) {
		final AccountCodePurposeSpecification specification = new AccountCodePurposeSpecification(
				accountCodePurposeContractRequest.getAccountCodePurpose());
		Pageable page = new PageRequest(accountCodePurposeContractRequest.getPage().getOffSet(),
				accountCodePurposeContractRequest.getPage().getPageSize());
		return accountCodePurposeRepository.findAll(specification, page);
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
}