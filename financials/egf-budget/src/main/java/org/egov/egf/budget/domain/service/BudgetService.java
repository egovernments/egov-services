package org.egov.egf.budget.domain.service;

import java.util.List;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.common.web.contract.CommonResponse;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetSearch;
import org.egov.egf.budget.domain.repository.BudgetRepository;
import org.egov.egf.budget.web.contract.BudgetContract;
import org.egov.egf.master.web.contract.FinancialYearContract;
import org.egov.egf.master.web.contract.repository.FinancialYearContractRepository;
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

	@Autowired
	private FinancialYearContractRepository financialYearContractRepository;

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

			if (budget.getFinancialYearId() != null) {
				CommonResponse<FinancialYearContract> result = financialYearContractRepository
						.getFinancialYearById(budget.getFinancialYearId().getId(), "500", "0", null);
				if (result == null || result.getData() == null || result.getData().isEmpty()) {
					throw new InvalidDataException("financialYear", "financialYear.invalid", " Invalid financialYear");
				}
				budget.setFinancialYearId(result.getData().get(0));
			}

			if (budget.getParentId() != null && budget.getParentId().getId() != null
					&& !budget.getParentId().getId().isEmpty() && budget.getParentId().getTenantId() != null
					&& !budget.getParentId().getTenantId().isEmpty()) {
				Budget parent = budgetRepository.findById(budget.getParentId());
				if (parent == null) {
					throw new InvalidDataException("parent", "parent.invalid", " Invalid parent");
				}
				budget.setParentId(parent);
			}
			if (budget.getReferenceBudgetId() != null && budget.getReferenceBudgetId().getId() != null
					&& !budget.getReferenceBudgetId().getId().isEmpty()
					&& budget.getReferenceBudgetId().getTenantId() != null
					&& !budget.getReferenceBudgetId().getTenantId().isEmpty()) {
				Budget referenceBudget = budgetRepository.findById(budget.getReferenceBudgetId());
				if (referenceBudget == null) {
					throw new InvalidDataException("referenceBudget", "referenceBudget.invalid",
							" Invalid referenceBudget");
				}
				budget.setReferenceBudgetId(referenceBudget);
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