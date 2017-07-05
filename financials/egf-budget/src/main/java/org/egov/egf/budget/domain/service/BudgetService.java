package org.egov.egf.budget.domain.service;

import java.util.List;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetSearch;
import org.egov.egf.budget.domain.repository.BudgetRepository;
import org.egov.egf.budget.web.contract.BudgetContract;
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

	@Autowired
	private SmartValidator validator;
	@Autowired
	private BudgetRepository budgetRepository;
	/*
	 * @Autowired private FinancialYearContractRepository
	 * financialYearContractRepository;
	 */

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
			/*
			 * if (budget.getFinancialYear() != null) { FinancialYearContract
			 * financialYear = financialYearContractRepository
			 * .findById(budget.getFinancialYear()); if (financialYear == null)
			 * { throw new InvalidDataException("financialYear",
			 * "financialYear.invalid", " Invalid financialYear"); }
			 * budget.setFinancialYear(financialYear); }
			 */
			if (budget.getParent() != null) {
				Budget parent = budgetRepository.findById(budget.getParent());
				if (parent == null) {
					throw new InvalidDataException("parent", "parent.invalid", " Invalid parent");
				}
				budget.setParent(parent);
			}
			if (budget.getReferenceBudget() != null) {
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

	public List<Budget> add(List<Budget> budgets, BindingResult errors) {
		budgets = fetchRelated(budgets);
		validate(budgets, ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return budgets;

	}

	public List<Budget> update(List<Budget> budgets, BindingResult errors) {
		budgets = fetchRelated(budgets);
		validate(budgets, ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return budgets;

	}

	public void addToQue(CommonRequest<BudgetContract> request) {
		budgetRepository.add(request);
	}

	public Pagination<Budget> search(BudgetSearch budgetSearch) {
		return budgetRepository.search(budgetSearch);
	}

}