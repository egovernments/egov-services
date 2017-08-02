package org.egov.egf.master.domain.service;

import java.util.List;

import org.egov.common.constants.EgfConstants;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.BankAccount;
import org.egov.egf.master.domain.model.BankAccountSearch;
import org.egov.egf.master.domain.model.BankBranch;
import org.egov.egf.master.domain.model.ChartOfAccount;
import org.egov.egf.master.domain.model.Fund;
import org.egov.egf.master.domain.repository.BankAccountRepository;
import org.egov.egf.master.domain.repository.BankBranchRepository;
import org.egov.egf.master.domain.repository.ChartOfAccountRepository;
import org.egov.egf.master.domain.repository.FundRepository;
import org.egov.egf.master.web.requests.BankAccountRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class BankAccountService {

	@Autowired
	private BankAccountRepository bankAccountRepository;

	@Autowired
	private SmartValidator validator;
	@Autowired
	private ChartOfAccountRepository chartOfAccountRepository;
	@Autowired
	private BankBranchRepository bankBranchRepository;
	@Autowired
	private FundRepository fundRepository;

	private BindingResult validate(List<BankAccount> bankaccounts, String method, BindingResult errors) {

		try {
			switch (method) {
			case EgfConstants.ACTION_VIEW:
				// validator.validate(bankAccountContractRequest.getBankAccount(),
				// errors);
				break;
			case EgfConstants.ACTION_CREATE:
				Assert.notNull(bankaccounts, "BankAccounts to create must not be null");
				for (BankAccount bankAccount : bankaccounts) {
					validator.validate(bankAccount, errors);
				}
				break;
			case EgfConstants.ACTION_UPDATE:
				Assert.notNull(bankaccounts, "BankAccounts to update must not be null");
				for (BankAccount bankAccount : bankaccounts) {
					validator.validate(bankAccount, errors);
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<BankAccount> fetchRelated(List<BankAccount> bankaccounts) {
		for (BankAccount bankAccount : bankaccounts) {
			// fetch related items
			if (bankAccount.getBankBranch() != null) {
				BankBranch bankBranch = bankBranchRepository.findById(bankAccount.getBankBranch());
				if (bankBranch == null) {
					throw new InvalidDataException("bankBranch", "bankBranch.invalid", " Invalid bankBranch");
				}
				bankAccount.setBankBranch(bankBranch);
			}
			if (bankAccount.getChartOfAccount() != null) {
				ChartOfAccount chartOfAccount = chartOfAccountRepository.findById(bankAccount.getChartOfAccount());
				if (chartOfAccount == null) {
					throw new InvalidDataException("chartOfAccount", "chartOfAccount.invalid",
							" Invalid chartOfAccount");
				}
				bankAccount.setChartOfAccount(chartOfAccount);
			}
			if (bankAccount.getFund() != null) {
				Fund fund = fundRepository.findById(bankAccount.getFund());
				if (fund == null) {
					throw new InvalidDataException("fund", "fund.invalid", " Invalid fund");
				}
				bankAccount.setFund(fund);
			}

		}

		return bankaccounts;
	}

	@Transactional
	public List<BankAccount> add(List<BankAccount> bankaccounts, BindingResult errors) {
		bankaccounts = fetchRelated(bankaccounts);
		validate(bankaccounts, EgfConstants.ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return bankaccounts;

	}

	@Transactional
	public List<BankAccount> update(List<BankAccount> bankaccounts, BindingResult errors) {
		bankaccounts = fetchRelated(bankaccounts);
		validate(bankaccounts, EgfConstants.ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return bankaccounts;

	}

	public void addToQue(BankAccountRequest request) {
		bankAccountRepository.add(request);
	}

	public Pagination<BankAccount> search(BankAccountSearch bankAccountSearch) {
		return bankAccountRepository.search(bankAccountSearch);
	}

	@Transactional
	public BankAccount save(BankAccount bankAccount) {
		return bankAccountRepository.save(bankAccount);
	}

	@Transactional
	public BankAccount update(BankAccount bankAccount) {
		return bankAccountRepository.update(bankAccount);
	}

}