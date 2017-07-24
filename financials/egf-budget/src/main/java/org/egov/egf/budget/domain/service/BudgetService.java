package org.egov.egf.budget.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetSearch;
import org.egov.egf.budget.domain.repository.BudgetRepository;
import org.egov.egf.master.web.contract.FinancialYearContract;
import org.egov.egf.master.web.contract.FinancialYearSearchContract;
import org.egov.egf.master.web.repository.FinancialYearContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class BudgetService {

	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String ACTION_VIEW = "view";
	public static final String ACTION_EDIT = "edit";
	public static final String ACTION_SEARCH = "search";

	private SmartValidator validator;

	private BudgetRepository budgetRepository;

	private FinancialYearContractRepository financialYearContractRepository;

	@Autowired
	public BudgetService(SmartValidator validator, BudgetRepository budgetRepository,
			FinancialYearContractRepository financialYearContractRepository) {
		this.validator = validator;
		this.budgetRepository = budgetRepository;
		this.financialYearContractRepository = financialYearContractRepository;
	}

	@Transactional
	public List<Budget> save(List<Budget> budgets, BindingResult errors) {

		List<Budget> resultList = new ArrayList<Budget>();

		try {

			budgets = save(budgets, errors, ACTION_CREATE);

		} catch (CustomBindException e) {

			throw new CustomBindException(errors);
		}

		for (Budget b : budgets) {

			resultList.add(save(b));

		}

		return resultList;
	}

	@Transactional
	public List<Budget> update(List<Budget> budgets, BindingResult errors) {

		List<Budget> resultList = new ArrayList<Budget>();

		try {

			budgets = save(budgets, errors, ACTION_UPDATE);

		} catch (CustomBindException e) {

			throw new CustomBindException(errors);
		}

		for (Budget b : budgets) {

			resultList.add(update(b));

		}

		return resultList;
	}

	public BindingResult validate(List<Budget> budgets, String method, BindingResult errors) {

		try {
			switch (method) {
			case ACTION_VIEW:
				// validator.validate(budgetContractRequest.getBudget(),
				// errors);
				break;
			case ACTION_CREATE:
				Assert.notNull(budgets, "Budgets to create must not be null");
				for (Budget budget : budgets) {
					validator.validate(budget, errors);
				}
				break;
			case ACTION_UPDATE:
				Assert.notNull(budgets, "Budgets to update must not be null");
				for (Budget budget : budgets) {
					validator.validate(budget, errors);
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<Budget> fetchRelated(List<Budget> budgets) {
		for (Budget budget : budgets) {
			// fetch related items

			if (budget.getFinancialYear() != null && budget.getFinancialYear().getId() != null
					&& budget.getFinancialYear().getTenantId() != null) {
				FinancialYearSearchContract contract = new FinancialYearSearchContract();
				contract.setId(budget.getFinancialYear().getId());
				contract.setTenantId(budget.getFinancialYear().getTenantId());
				FinancialYearContract financialYear = financialYearContractRepository.findById(contract);
				if (financialYear == null) {
					throw new InvalidDataException("financialYear", "financialYear.invalid", " Invalid financialYear");
				}
				budget.setFinancialYear(financialYear);
			}

			if (budget.getParent() != null && budget.getParent().getId() != null
					&& !budget.getParent().getId().isEmpty()) {
				Budget parent = budgetRepository.findById(budget.getParent());
				if (parent == null) {
					throw new InvalidDataException("parent", "parent.invalid", " Invalid parent");
				}
				budget.setParent(parent);
			}
			if (budget.getReferenceBudget() != null && budget.getReferenceBudget().getId() != null
					&& !budget.getReferenceBudget().getId().isEmpty()) {
				Budget referenceBudget = budgetRepository.findById(budget.getReferenceBudget());
				if (referenceBudget == null) {
					throw new InvalidDataException("referenceBudget", "referenceBudget.invalid",
							" Invalid referenceBudget");
				}
				budget.setReferenceBudget(referenceBudget);
			}
		}

		return budgets;
	}

	@Transactional
	public List<Budget> save(List<Budget> budgets, BindingResult errors, String action) {

		budgets = fetchRelated(budgets);

		validate(budgets, action, errors);

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		return budgets;

	}

	public Pagination<Budget> search(BudgetSearch budgetSearch) {
		return budgetRepository.search(budgetSearch);
	}

	@Transactional
	public Budget save(Budget budget) {
		return budgetRepository.save(budget);
	}

	@Transactional
	public Budget update(Budget budget) {
		return budgetRepository.update(budget);
	}

}