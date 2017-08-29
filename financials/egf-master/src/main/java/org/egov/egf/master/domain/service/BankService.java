package org.egov.egf.master.domain.service;

import java.util.List;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.Bank;
import org.egov.egf.master.domain.model.BankSearch;
import org.egov.egf.master.domain.repository.BankRepository;
import org.egov.egf.master.domain.repository.FundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class BankService {

	@Autowired
	private BankRepository bankRepository;

	@Autowired
	private SmartValidator validator;
	@Autowired
	private FundRepository fundRepository;

	@Transactional
	public List<Bank> create(List<Bank> banks, BindingResult errors, RequestInfo requestInfo) {

		try {

			banks = fetchRelated(banks);

			validate(banks, Constants.ACTION_CREATE, errors);

			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}
			for (Bank b : banks) {
				b.setId(bankRepository.getNextSequence());
			}

		} catch (CustomBindException e) {

			throw new CustomBindException(errors);
		}

		return bankRepository.save(banks, requestInfo);

	}

	@Transactional
	public List<Bank> update(List<Bank> banks, BindingResult errors, RequestInfo requestInfo) {

		try {

			banks = fetchRelated(banks);

			validate(banks, Constants.ACTION_UPDATE, errors);

			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}

		} catch (CustomBindException e) {

			throw new CustomBindException(errors);
		}

		return bankRepository.update(banks, requestInfo);

	}

	private BindingResult validate(List<Bank> banks, String method, BindingResult errors) {

		try {
			switch (method) {
			case Constants.ACTION_VIEW:
				// validator.validate(bankContractRequest.getBank(), errors);
				break;
			case Constants.ACTION_CREATE:
				Assert.notNull(banks, "Banks to create must not be null");
				for (Bank bank : banks) {
					validator.validate(bank, errors);
				}
				break;
			case Constants.ACTION_UPDATE:
				Assert.notNull(banks, "Banks to update must not be null");
				for (Bank bank : banks) {
					Assert.notNull(bank.getId(), "Bank ID to update must not be null");
					validator.validate(bank, errors);
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<Bank> fetchRelated(List<Bank> banks) {

		return banks;
	}

	@Transactional
	public List<Bank> update(List<Bank> banks, BindingResult errors) {
		banks = fetchRelated(banks);
		validate(banks, Constants.ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return banks;

	}

	public Pagination<Bank> search(BankSearch bankSearch) {
	        Assert.notNull(bankSearch.getTenantId(), "tenantId is mandatory for bank search");
		return bankRepository.search(bankSearch);
	}

	@Transactional
	public Bank save(Bank bank) {
		return bankRepository.save(bank);
	}

	@Transactional
	public Bank update(Bank bank) {
		return bankRepository.update(bank);
	}

}