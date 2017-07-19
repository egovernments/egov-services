package org.egov.egf.budget.domain.service;

import java.util.List;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;
import org.egov.egf.budget.domain.repository.BudgetDetailRepository;
import org.egov.egf.budget.domain.repository.BudgetReAppropriationRepository;
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

	private BudgetReAppropriationRepository budgetReAppropriationRepository;

	private SmartValidator validator;

	private BudgetDetailRepository budgetDetailRepository;

	@Autowired
	public BudgetReAppropriationService(BudgetReAppropriationRepository budgetReAppropriationRepository,
			SmartValidator validator, BudgetDetailRepository budgetDetailRepository) {

		this.budgetReAppropriationRepository = budgetReAppropriationRepository;
		this.validator = validator;
		this.budgetDetailRepository = budgetDetailRepository;

	}

	private BindingResult validate(List<BudgetReAppropriation> budgetreappropriations, String method,
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
		if (budgetreappropriations != null)
			for (BudgetReAppropriation budgetReAppropriation : budgetreappropriations) {
				// fetch related items
				if (budgetReAppropriation.getBudgetDetail() != null
						&& budgetReAppropriation.getBudgetDetail().getId() != null
						&& budgetReAppropriation.getBudgetDetail().getTenantId() != null) {
					BudgetDetail budgetDetail = budgetDetailRepository
							.findById(budgetReAppropriation.getBudgetDetail());
					if (budgetDetail == null) {
						throw new InvalidDataException("budgetDetail", "budgetDetail.invalid", " Invalid budgetDetail");
					}
					budgetReAppropriation.setBudgetDetail(budgetDetail);
				}

			}

		return budgetreappropriations;
	}

	@Transactional
	public List<BudgetReAppropriation> save(List<BudgetReAppropriation> budgetreappropriations, BindingResult errors,
			String action) {

		budgetreappropriations = fetchRelated(budgetreappropriations);

		validate(budgetreappropriations, action, errors);

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		return budgetreappropriations;

	}

	public Pagination<BudgetReAppropriation> search(BudgetReAppropriationSearch budgetReAppropriationSearch) {
		return budgetReAppropriationRepository.search(budgetReAppropriationSearch);
	}

	@Transactional
	public BudgetReAppropriation save(BudgetReAppropriation budgetReAppropriation) {
		return budgetReAppropriationRepository.save(budgetReAppropriation);
	}

	@Transactional
	public BudgetReAppropriation update(BudgetReAppropriation budgetReAppropriation) {
		return budgetReAppropriationRepository.update(budgetReAppropriation);
	}

}