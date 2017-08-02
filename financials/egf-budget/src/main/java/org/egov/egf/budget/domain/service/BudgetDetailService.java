package org.egov.egf.budget.domain.service;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetDetailSearch;
import org.egov.egf.budget.domain.repository.BudgetDetailRepository;
import org.egov.egf.budget.domain.repository.BudgetRepository;
import org.egov.egf.budget.web.contract.Boundary;
import org.egov.egf.budget.web.contract.DepartmentRes;
import org.egov.egf.budget.web.repository.BoundaryRepository;
import org.egov.egf.budget.web.repository.DepartmentRepository;
import org.egov.egf.master.web.contract.BudgetGroupContract;
import org.egov.egf.master.web.contract.BudgetGroupSearchContract;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.contract.FunctionSearchContract;
import org.egov.egf.master.web.contract.FunctionaryContract;
import org.egov.egf.master.web.contract.FunctionarySearchContract;
import org.egov.egf.master.web.contract.FundContract;
import org.egov.egf.master.web.contract.FundSearchContract;
import org.egov.egf.master.web.contract.SchemeContract;
import org.egov.egf.master.web.contract.SchemeSearchContract;
import org.egov.egf.master.web.contract.SubSchemeContract;
import org.egov.egf.master.web.contract.SubSchemeSearchContract;
import org.egov.egf.master.web.repository.BudgetGroupContractRepository;
import org.egov.egf.master.web.repository.FunctionContractRepository;
import org.egov.egf.master.web.repository.FunctionaryContractRepository;
import org.egov.egf.master.web.repository.FundContractRepository;
import org.egov.egf.master.web.repository.SchemeContractRepository;
import org.egov.egf.master.web.repository.SubSchemeContractRepository;
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

	@Autowired
	private SchemeContractRepository schemeContractRepository;

	@Autowired
	private FunctionContractRepository functionContractRepository;

	@Autowired
	private FunctionaryContractRepository functionaryContractRepository;

	@Autowired
	private BudgetGroupContractRepository budgetGroupContractRepository;

	@Autowired
	private FundContractRepository fundContractRepository;

	@Autowired
	private SubSchemeContractRepository subSchemeContractRepository;

	@Autowired
	private BudgetRepository budgetRepository;

	@Autowired
	private BoundaryRepository boundaryRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Transactional
	public List<BudgetDetail> create(List<BudgetDetail> budgetDetails, BindingResult errors, RequestInfo requestInfo) {

		try {

			budgetDetails = fetchRelated(budgetDetails);

			validate(budgetDetails, ACTION_CREATE, errors);

			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}

		} catch (CustomBindException e) {

			throw new CustomBindException(errors);
		}

		return budgetDetailRepository.save(budgetDetails, requestInfo);

	}

	@Transactional
	public List<BudgetDetail> update(List<BudgetDetail> budgetDetails, BindingResult errors, RequestInfo requestInfo) {

		try {

			budgetDetails = fetchRelated(budgetDetails);

			validate(budgetDetails, ACTION_UPDATE, errors);

			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}

		} catch (CustomBindException e) {

			throw new CustomBindException(errors);
		}

		return budgetDetailRepository.update(budgetDetails, requestInfo);

	}

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
		if (budgetdetails != null)
			for (BudgetDetail budgetDetail : budgetdetails) {

				// fetch related items
				if (budgetDetail.getBudget() != null && budgetDetail.getBudget().getId() != null
						&& budgetDetail.getBudget().getTenantId() != null) {
					Budget budget = budgetRepository.findById(budgetDetail.getBudget());
					if (budget == null) {
						throw new InvalidDataException("budget", "budget.invalid", " Invalid budget");
					}
					budgetDetail.setBudget(budget);
				}

				if (budgetDetail.getFund() != null && budgetDetail.getFund().getId() != null
						&& budgetDetail.getFund().getTenantId() != null) {
					FundSearchContract contract = new FundSearchContract();
					contract.setId(budgetDetail.getFund().getId());
					contract.setTenantId(budgetDetail.getFund().getTenantId());
					FundContract fund = fundContractRepository.findById(contract);
					if (fund == null) {
						throw new InvalidDataException("fund", "fund.invalid", " Invalid fund");
					}
					budgetDetail.setFund(fund);
				}

				if (budgetDetail.getBudgetGroup() != null && budgetDetail.getBudgetGroup().getId() != null
						&& budgetDetail.getBudgetGroup().getTenantId() != null) {
					BudgetGroupSearchContract contract = new BudgetGroupSearchContract();
					contract.setId(budgetDetail.getBudgetGroup().getId());
					contract.setTenantId(budgetDetail.getBudgetGroup().getTenantId());
					BudgetGroupContract budgetGroup = budgetGroupContractRepository.findById(contract);
					if (budgetGroup == null) {
						throw new InvalidDataException("budgetGroup", "budgetGroup.invalid", " Invalid budgetGroup");
					}
					budgetDetail.setBudgetGroup(budgetGroup);
				}

				if (budgetDetail.getUsingDepartment() != null && budgetDetail.getUsingDepartment().getId() != null
						&& budgetDetail.getUsingDepartment().getTenantId() != null) {
					DepartmentRes usingDepartment = departmentRepository.getDepartmentById(
							budgetDetail.getUsingDepartment().getId().toString(), budgetDetail.getTenantId());
					if (usingDepartment == null || usingDepartment.getDepartment() == null
							|| usingDepartment.getDepartment().isEmpty()) {
						throw new InvalidDataException("usingDepartment", "usingDepartment.invalid",
								" Invalid usingDepartment");
					}
					budgetDetail.setUsingDepartment(usingDepartment.getDepartment().get(0));
				}

				if (budgetDetail.getExecutingDepartment() != null
						&& budgetDetail.getExecutingDepartment().getId() != null
						&& budgetDetail.getExecutingDepartment().getTenantId() != null) {
					DepartmentRes executingDepartment = departmentRepository.getDepartmentById(
							budgetDetail.getExecutingDepartment().getId().toString(), budgetDetail.getTenantId());
					if (executingDepartment == null) {
						throw new InvalidDataException("executingDepartment", "executingDepartment.invalid",
								" Invalid executingDepartment");
					}
					budgetDetail.setExecutingDepartment(executingDepartment.getDepartment().get(0));
				}

				if (budgetDetail.getFunction() != null && budgetDetail.getFunction().getId() != null
						&& budgetDetail.getFunction().getTenantId() != null) {
					FunctionSearchContract contract = new FunctionSearchContract();
					contract.setId(budgetDetail.getFunction().getId());
					contract.setTenantId(budgetDetail.getFunction().getTenantId());
					FunctionContract function = functionContractRepository.findById(contract);
					if (function == null) {
						throw new InvalidDataException("function", "function.invalid", " Invalid function");
					}
					budgetDetail.setFunction(function);
				}

				if (budgetDetail.getScheme() != null && budgetDetail.getScheme().getId() != null
						&& budgetDetail.getScheme().getTenantId() != null) {
					SchemeSearchContract contract = new SchemeSearchContract();
					contract.setId(budgetDetail.getScheme().getId());
					contract.setTenantId(budgetDetail.getScheme().getTenantId());
					SchemeContract scheme = schemeContractRepository.findById(contract);
					if (scheme == null) {
						throw new InvalidDataException("scheme", "scheme.invalid", " Invalid scheme");
					}
					budgetDetail.setScheme(scheme);
				}

				if (budgetDetail.getSubScheme() != null && budgetDetail.getSubScheme().getId() != null
						&& budgetDetail.getSubScheme().getTenantId() != null) {
					SubSchemeSearchContract contract = new SubSchemeSearchContract();
					contract.setId(budgetDetail.getSubScheme().getId());
					contract.setTenantId(budgetDetail.getSubScheme().getTenantId());
					SubSchemeContract subScheme = subSchemeContractRepository.findById(contract);
					if (subScheme == null) {
						throw new InvalidDataException("subScheme", "subScheme.invalid", " Invalid subScheme");
					}
					budgetDetail.setSubScheme(subScheme);
				}

				if (budgetDetail.getFunctionary() != null && budgetDetail.getFunctionary().getId() != null
						&& budgetDetail.getFunctionary().getTenantId() != null) {
					FunctionarySearchContract contract = new FunctionarySearchContract();
					contract.setId(budgetDetail.getFunctionary().getId());
					contract.setTenantId(budgetDetail.getFunctionary().getTenantId());
					FunctionaryContract functionary = functionaryContractRepository.findById(contract);
					if (functionary == null) {
						throw new InvalidDataException("functionary", "functionary.invalid", " Invalid functionary");
					}
					budgetDetail.setFunctionary(functionary);
				}

				if (budgetDetail.getBoundary() != null && budgetDetail.getBoundary().getId() != null
						&& budgetDetail.getBoundary().getTenantId() != null) {
					Boundary boundary = boundaryRepository.getBoundaryById(budgetDetail.getBoundary().getId(),
							budgetDetail.getBoundary().getTenantId());
					if (boundary == null) {
						throw new InvalidDataException("boundary", "boundary.invalid", " Invalid boundary");
					}
					budgetDetail.setBoundary(boundary);
				}

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