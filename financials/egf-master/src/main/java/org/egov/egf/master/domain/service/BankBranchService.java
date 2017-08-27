package org.egov.egf.master.domain.service;

import java.util.List;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.Bank;
import org.egov.egf.master.domain.model.BankBranch;
import org.egov.egf.master.domain.model.BankBranchSearch;
import org.egov.egf.master.domain.repository.BankBranchRepository;
import org.egov.egf.master.domain.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class BankBranchService {

	@Autowired
	private BankBranchRepository bankBranchRepository;

	@Autowired
	private SmartValidator validator;
	@Autowired
	private BankRepository bankRepository;

	@Transactional
	public List<BankBranch> create(List<BankBranch> bankBranches, BindingResult errors, RequestInfo requestInfo) {

		try {

			bankBranches = fetchRelated(bankBranches);

			validate(bankBranches, Constants.ACTION_CREATE, errors);

			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}
			for (BankBranch b : bankBranches) {
				b.setId(bankBranchRepository.getNextSequence());
			}

		} catch (CustomBindException e) {

			throw new CustomBindException(errors);
		}

		return bankBranchRepository.save(bankBranches, requestInfo);

	}

	@Transactional
	public List<BankBranch> update(List<BankBranch> bankBranches, BindingResult errors, RequestInfo requestInfo) {

		try {

			bankBranches = fetchRelated(bankBranches);

			validate(bankBranches, Constants.ACTION_UPDATE, errors);

			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}

		} catch (CustomBindException e) {

			throw new CustomBindException(errors);
		}

		return bankBranchRepository.update(bankBranches, requestInfo);

	}

	private BindingResult validate(List<BankBranch> bankbranches, String method, BindingResult errors) {

		try {
			switch (method) {
			case Constants.ACTION_VIEW:
				// validator.validate(bankBranchContractRequest.getBankBranch(),
				// errors);
				break;
			case Constants.ACTION_CREATE:
				Assert.notNull(bankbranches, "BankBranches to create must not be null");
				for (BankBranch bankBranch : bankbranches) {
					validator.validate(bankBranch, errors);
				}
				break;
			case Constants.ACTION_UPDATE:
				Assert.notNull(bankbranches, "BankBranches to update must not be null");
				for (BankBranch bankBranch : bankbranches) {
					Assert.notNull(bankBranch.getId(), "Bank Branch ID to update must not be null");
					validator.validate(bankBranch, errors);
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<BankBranch> fetchRelated(List<BankBranch> bankbranches) {
		for (BankBranch bankBranch : bankbranches) {
			// fetch related items
			if (bankBranch.getTenantId() != null)
				if (bankBranch.getBank() != null && bankBranch.getBank().getId() != null) {
					bankBranch.getBank().setTenantId(bankBranch.getTenantId());
					Bank bank = bankRepository.findById(bankBranch.getBank());
					if (bank == null) {
						throw new InvalidDataException("bank", "bank.invalid", " Invalid bank");
					}
					bankBranch.setBank(bank);
				}

		}

		return bankbranches;
	}

	@Transactional
	public List<BankBranch> update(List<BankBranch> bankbranches, BindingResult errors) {
		bankbranches = fetchRelated(bankbranches);
		validate(bankbranches, Constants.ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return bankbranches;

	}

	public Pagination<BankBranch> search(BankBranchSearch bankBranchSearch) {
		return bankBranchRepository.search(bankBranchSearch);
	}

	@Transactional
	public BankBranch save(BankBranch bankBranch) {
		return bankBranchRepository.save(bankBranch);
	}

	@Transactional
	public BankBranch update(BankBranch bankBranch) {
		return bankBranchRepository.update(bankBranch);
	}

}