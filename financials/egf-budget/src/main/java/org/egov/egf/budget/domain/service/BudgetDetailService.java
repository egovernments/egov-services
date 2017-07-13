package org.egov.egf.budget.domain.service;

import java.util.List;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetDetailSearch;
import org.egov.egf.budget.domain.repository.BudgetDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class BudgetDetailService {

	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String ACTION_VIEW = "view";
	public static final String ACTION_EDIT = "edit";
	public static final String ACTION_SEARCH = "search";

	@Autowired
	private BudgetDetailRepository budgetDetailRepository;

	@Autowired
	private SmartValidator validator;

	/*
	 * @Autowired private SchemeContractRepository schemeContractRepository;
	 * 
	 * @Autowired private FunctionContractRepository functionContractRepository;
	 * 
	 * @Autowired private BudgetGroupContractRepository
	 * budgetGroupContractRepository;
	 * 
	 * @Autowired private DepartmentContractRepository
	 * departmentContractRepository;
	 * 
	 * @Autowired private BudgetRepository budgetRepository;
	 * 
	 * @Autowired private FundContractRepository fundContractRepository;
	 * 
	 * @Autowired private SubSchemeContractRepository
	 * subSchemeContractRepository;
	 * 
	 * @Autowired private EgfStatusRepository egfStatusRepository;
	 * 
	 * @Autowired private BoundaryContractRepository boundaryContractRepository;
	 */

	private BindingResult validate(List<BudgetDetail> budgetdetails, String method, BindingResult errors) {

		try {
			switch (method) {
			case ACTION_VIEW:
				// validator.validate(budgetDetailContractRequest.getBudgetDetail(),
				// errors);
				break;
			case ACTION_CREATE:
				Assert.notNull(budgetdetails, "BudgetDetails to create must not be null");
				for (BudgetDetail budgetDetail : budgetdetails) {
					validator.validate(budgetDetail, errors);
				}
				break;
			case ACTION_UPDATE:
				Assert.notNull(budgetdetails, "BudgetDetails to update must not be null");
				for (BudgetDetail budgetDetail : budgetdetails) {
					validator.validate(budgetDetail, errors);
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<BudgetDetail> fetchRelated(List<BudgetDetail> budgetdetails) {
		for (BudgetDetail budgetDetail : budgetdetails) {
			/*
			 * // fetch related items if (budgetDetail.getBudget() != null) {
			 * Budget budget =
			 * budgetRepository.findById(budgetDetail.getBudget()); if (budget
			 * == null) { throw new InvalidDataException("budget",
			 * "budget.invalid", " Invalid budget"); }
			 * budgetDetail.setBudget(budget); } if
			 * (budgetDetail.getBudgetGroup() != null) { BudgetGroupContract
			 * budgetGroup =
			 * budgetGroupContractRepository.findById(budgetDetail.
			 * getBudgetGroup()); if (budgetGroup == null) { throw new
			 * InvalidDataException("budgetGroup", "budgetGroup.invalid",
			 * " Invalid budgetGroup"); }
			 * budgetDetail.setBudgetGroup(budgetGroup); } if
			 * (budgetDetail.getUsingDepartment() != null) { DepartmentContract
			 * usingDepartment = departmentContractRepository
			 * .findById(budgetDetail.getUsingDepartment()); if (usingDepartment
			 * == null) { throw new InvalidDataException("usingDepartment",
			 * "usingDepartment.invalid", " Invalid usingDepartment"); }
			 * budgetDetail.setUsingDepartment(usingDepartment); } if
			 * (budgetDetail.getExecutingDepartment() != null) {
			 * DepartmentContract executingDepartment =
			 * departmentContractRepository
			 * .findById(budgetDetail.getExecutingDepartment()); if
			 * (executingDepartment == null) { throw new
			 * InvalidDataException("executingDepartment",
			 * "executingDepartment.invalid", " Invalid executingDepartment"); }
			 * budgetDetail.setExecutingDepartment(executingDepartment); } if
			 * (budgetDetail.getFund() != null) { FundContract fund =
			 * fundContractRepository.findById(budgetDetail.getFund()); if (fund
			 * == null) { throw new InvalidDataException("fund", "fund.invalid",
			 * " Invalid fund"); } budgetDetail.setFund(fund); } if
			 * (budgetDetail.getFunction() != null) { FunctionContract function
			 * =
			 * functionContractRepository.findById(budgetDetail.getFunction());
			 * if (function == null) { throw new
			 * InvalidDataException("function", "function.invalid",
			 * " Invalid function"); } budgetDetail.setFunction(function); } if
			 * (budgetDetail.getScheme() != null) { SchemeContract scheme =
			 * schemeContractRepository.findById(budgetDetail.getScheme()); if
			 * (scheme == null) { throw new InvalidDataException("scheme",
			 * "scheme.invalid", " Invalid scheme"); }
			 * budgetDetail.setScheme(scheme); } if (budgetDetail.getSubScheme()
			 * != null) { SubSchemeContract subScheme =
			 * subSchemeContractRepository.findById(budgetDetail.getSubScheme())
			 * ; if (subScheme == null) { throw new
			 * InvalidDataException("subScheme", "subScheme.invalid",
			 * " Invalid subScheme"); } budgetDetail.setSubScheme(subScheme); }
			 * if (budgetDetail.getFunctionary() != null) { FunctionContract
			 * functionary =
			 * functionContractRepository.findById(budgetDetail.getFunctionary()
			 * ); if (functionary == null) { throw new
			 * InvalidDataException("functionary", "functionary.invalid",
			 * " Invalid functionary"); }
			 * budgetDetail.setFunctionary(functionary); } if
			 * (budgetDetail.getBoundary() != null) { BoundaryContract boundary
			 * =
			 * boundaryContractRepository.findById(budgetDetail.getBoundary());
			 * if (boundary == null) { throw new
			 * InvalidDataException("boundary", "boundary.invalid",
			 * " Invalid boundary"); } budgetDetail.setBoundary(boundary); } if
			 * (budgetDetail.getStatus() != null) { EgfStatus status =
			 * egfStatusRepository.findById(budgetDetail.getStatus()); if
			 * (status == null) { throw new InvalidDataException("status",
			 * "status.invalid", " Invalid status"); }
			 * budgetDetail.setStatus(status); }
			 */
		}

		return budgetdetails;
	}

	@Transactional
	public List<BudgetDetail> save(List<BudgetDetail> budgetdetails, BindingResult errors, String action) {

		budgetdetails = fetchRelated(budgetdetails);

		validate(budgetdetails, action, errors);

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		return budgetdetails;

	}

	public Pagination<BudgetDetail> search(BudgetDetailSearch budgetDetailSearch) {
		return budgetDetailRepository.search(budgetDetailSearch);
	}

	@Transactional
	public BudgetDetail save(BudgetDetail budgetDetail) {
		return budgetDetailRepository.save(budgetDetail);
	}

	@Transactional
	public BudgetDetail update(BudgetDetail budgetDetail) {
		return budgetDetailRepository.update(budgetDetail);
	}

}