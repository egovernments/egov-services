package org.egov.egf.budget.domain.service;

import java.util.List;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;
import org.egov.egf.budget.domain.repository.BudgetDetailRepository;
import org.egov.egf.budget.domain.repository.BudgetReAppropriationRepository;
import org.egov.egf.budget.web.contract.BudgetReAppropriationContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class BudgetReAppropriationService {

	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String ACTION_VIEW = "view";
	public static final String ACTION_EDIT = "edit";
	public static final String ACTION_SEARCH = "search";

	@Autowired
	private BudgetReAppropriationRepository budgetReAppropriationRepository;

	@Autowired
	private SmartValidator validator;

	@Autowired
	private BudgetDetailRepository budgetDetailRepository;

	public BindingResult validate(List<BudgetReAppropriation> budgetreappropriations, String method,
			BindingResult errors) {

		try {
			switch (method) {
			case ACTION_VIEW:
				// validator.validate(budgetReAppropriationContractRequest.getBudgetReAppropriation(),
				// errors);
				break;
			case ACTION_CREATE:
				Assert.notNull(budgetreappropriations, "BudgetReAppropriations to create must not be null");
				for (BudgetReAppropriation budgetReAppropriation : budgetreappropriations) {
					validator.validate(budgetReAppropriation, errors);
				}
				break;
			case ACTION_UPDATE:
				Assert.notNull(budgetreappropriations, "BudgetReAppropriations to update must not be null");
				for (BudgetReAppropriation budgetReAppropriation : budgetreappropriations) {
					validator.validate(budgetReAppropriation, errors);
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<BudgetReAppropriation> fetchRelated(List<BudgetReAppropriation> budgetreappropriations) {
		for (BudgetReAppropriation budgetReAppropriation : budgetreappropriations) {
			// fetch related items
			if (budgetReAppropriation.getBudgetDetail() != null) {
				BudgetDetail budgetDetail = budgetDetailRepository.findById(budgetReAppropriation.getBudgetDetail());
				if (budgetDetail == null) {
					throw new InvalidDataException("budgetDetail", "budgetDetail.invalid", " Invalid budgetDetail");
				}
				budgetReAppropriation.setBudgetDetail(budgetDetail);
			}

		}

		return budgetreappropriations;
	}

	public List<BudgetReAppropriation> add(List<BudgetReAppropriation> budgetreappropriations, BindingResult errors) {
		budgetreappropriations = fetchRelated(budgetreappropriations);
		validate(budgetreappropriations, ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return budgetreappropriations;

	}

	public List<BudgetReAppropriation> update(List<BudgetReAppropriation> budgetreappropriations,
			BindingResult errors) {
		budgetreappropriations = fetchRelated(budgetreappropriations);
		validate(budgetreappropriations, ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return budgetreappropriations;

	}

	public void addToQue(CommonRequest<BudgetReAppropriationContract> request) {
		budgetReAppropriationRepository.add(request);
	}

	public Pagination<BudgetReAppropriation> search(BudgetReAppropriationSearch budgetReAppropriationSearch) {
		return budgetReAppropriationRepository.search(budgetReAppropriationSearch);
	}

}