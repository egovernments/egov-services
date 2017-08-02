package org.egov.egf.master.domain.service;

import java.util.List;

import org.egov.common.constants.EgfConstants;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.BudgetGroup;
import org.egov.egf.master.domain.model.BudgetGroupSearch;
import org.egov.egf.master.domain.model.ChartOfAccount;
import org.egov.egf.master.domain.repository.BudgetGroupRepository;
import org.egov.egf.master.domain.repository.ChartOfAccountRepository;
import org.egov.egf.master.web.requests.BudgetGroupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class BudgetGroupService {

	@Autowired
	private BudgetGroupRepository budgetGroupRepository;

	@Autowired
	private SmartValidator validator;
	@Autowired
	private ChartOfAccountRepository chartOfAccountRepository;

	private BindingResult validate(List<BudgetGroup> budgetgroups, String method, BindingResult errors) {

		try {
			switch (method) {
			case EgfConstants.ACTION_VIEW:
				// validator.validate(budgetGroupContractRequest.getBudgetGroup(),
				// errors);
				break;
			case EgfConstants.ACTION_CREATE:
				Assert.notNull(budgetgroups, "BudgetGroups to create must not be null");
				for (BudgetGroup budgetGroup : budgetgroups) {
					validator.validate(budgetGroup, errors);
				}
				break;
			case EgfConstants.ACTION_UPDATE:
				Assert.notNull(budgetgroups, "BudgetGroups to update must not be null");
				for (BudgetGroup budgetGroup : budgetgroups) {
					validator.validate(budgetGroup, errors);
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<BudgetGroup> fetchRelated(List<BudgetGroup> budgetgroups) {
		for (BudgetGroup budgetGroup : budgetgroups) {
			// fetch related items
			if (budgetGroup.getMajorCode() != null) {
				ChartOfAccount majorCode = chartOfAccountRepository.findById(budgetGroup.getMajorCode());
				if (majorCode == null) {
					throw new InvalidDataException("majorCode", "majorCode.invalid", " Invalid majorCode");
				}
				budgetGroup.setMajorCode(majorCode);
			}
			if (budgetGroup.getMaxCode() != null) {
				ChartOfAccount maxCode = chartOfAccountRepository.findById(budgetGroup.getMaxCode());
				if (maxCode == null) {
					throw new InvalidDataException("maxCode", "maxCode.invalid", " Invalid maxCode");
				}
				budgetGroup.setMaxCode(maxCode);
			}
			if (budgetGroup.getMinCode() != null) {
				ChartOfAccount minCode = chartOfAccountRepository.findById(budgetGroup.getMinCode());
				if (minCode == null) {
					throw new InvalidDataException("minCode", "minCode.invalid", " Invalid minCode");
				}
				budgetGroup.setMinCode(minCode);
			}

		}

		return budgetgroups;
	}

	@Transactional
	public List<BudgetGroup> add(List<BudgetGroup> budgetgroups, BindingResult errors) {
		budgetgroups = fetchRelated(budgetgroups);
		validate(budgetgroups, EgfConstants.ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return budgetgroups;

	}

	@Transactional
	public List<BudgetGroup> update(List<BudgetGroup> budgetgroups, BindingResult errors) {
		budgetgroups = fetchRelated(budgetgroups);
		validate(budgetgroups, EgfConstants.ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return budgetgroups;

	}

	public void addToQue(BudgetGroupRequest request) {
		budgetGroupRepository.add(request);
	}

	public Pagination<BudgetGroup> search(BudgetGroupSearch budgetGroupSearch) {
		return budgetGroupRepository.search(budgetGroupSearch);
	}

	@Transactional
	public BudgetGroup save(BudgetGroup budgetGroup) {
		return budgetGroupRepository.save(budgetGroup);
	}

	@Transactional
	public BudgetGroup update(BudgetGroup budgetGroup) {
		return budgetGroupRepository.update(budgetGroup);
	}

}