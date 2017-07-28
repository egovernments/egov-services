package org.egov.egf.master.domain.service;

import java.util.List;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.Bank;
import org.egov.egf.master.domain.model.BankAccount;
import org.egov.egf.master.domain.model.Supplier;
import org.egov.egf.master.domain.model.SupplierSearch;
import org.egov.egf.master.domain.repository.BankAccountRepository;
import org.egov.egf.master.domain.repository.BankRepository;
import org.egov.egf.master.domain.repository.SupplierRepository;
import org.egov.egf.master.web.requests.SupplierRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class SupplierService {

	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String ACTION_VIEW = "view";
	public static final String ACTION_EDIT = "edit";
	public static final String ACTION_SEARCH = "search";

	@Autowired
	private SupplierRepository supplierRepository;

	@Autowired
	private SmartValidator validator;
	@Autowired
	private BankAccountRepository bankAccountRepository;
	@Autowired
	private BankRepository bankRepository;

	private BindingResult validate(List<Supplier> suppliers, String method, BindingResult errors) {

		try {
			switch (method) {
			case ACTION_VIEW:
				// validator.validate(supplierContractRequest.getSupplier(),
				// errors);
				break;
			case ACTION_CREATE:
				Assert.notNull(suppliers, "Suppliers to create must not be null");
				for (Supplier supplier : suppliers) {
					validator.validate(supplier, errors);
				}
				break;
			case ACTION_UPDATE:
				Assert.notNull(suppliers, "Suppliers to update must not be null");
				for (Supplier supplier : suppliers) {
					validator.validate(supplier, errors);
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<Supplier> fetchRelated(List<Supplier> suppliers) {
		for (Supplier supplier : suppliers) {
			// fetch related items
			if (supplier.getBankAccount() != null) {
				BankAccount bankAccount = bankAccountRepository.findById(supplier.getBankAccount());
				if (bankAccount == null) {
					throw new InvalidDataException("bankAccount", "bankAccount.invalid", " Invalid bankAccount");
				}
				supplier.setBankAccount(bankAccount);
			}
			if (supplier.getBank() != null) {
				Bank bank = bankRepository.findById(supplier.getBank());
				if (bank == null) {
					throw new InvalidDataException("bank", "bank.invalid", " Invalid bank");
				}
				supplier.setBank(bank);
			}

		}

		return suppliers;
	}

	@Transactional
	public List<Supplier> add(List<Supplier> suppliers, BindingResult errors) {
		suppliers = fetchRelated(suppliers);
		validate(suppliers, ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return suppliers;

	}

	@Transactional
	public List<Supplier> update(List<Supplier> suppliers, BindingResult errors) {
		suppliers = fetchRelated(suppliers);
		validate(suppliers, ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return suppliers;

	}

	public void addToQue(SupplierRequest request) {
		supplierRepository.add(request);
	}

	public Pagination<Supplier> search(SupplierSearch supplierSearch) {
		return supplierRepository.search(supplierSearch);
	}

	@Transactional
	public Supplier save(Supplier supplier) {
		return supplierRepository.save(supplier);
	}

	@Transactional
	public Supplier update(Supplier supplier) {
		return supplierRepository.update(supplier);
	}

}