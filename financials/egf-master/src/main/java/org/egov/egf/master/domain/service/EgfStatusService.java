package org.egov.egf.master.domain.service;

import java.util.List;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.EgfStatus;
import org.egov.egf.master.domain.model.EgfStatusSearch;
import org.egov.egf.master.domain.repository.EgfStatusRepository;
import org.egov.egf.master.web.contract.EgfStatusContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class EgfStatusService {

	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String ACTION_VIEW = "view";
	public static final String ACTION_EDIT = "edit";
	public static final String ACTION_SEARCH = "search";

	@Autowired
	private EgfStatusRepository egfStatusRepository;

	@Autowired
	private SmartValidator validator;

	public BindingResult validate(List<EgfStatus> egfstatuses, String method, BindingResult errors) {

		try {
			switch (method) {
			case ACTION_VIEW:
				// validator.validate(egfStatusContractRequest.getEgfStatus(),
				// errors);
				break;
			case ACTION_CREATE:
				Assert.notNull(egfstatuses, "EgfStatuses to create must not be null");
				for (EgfStatus egfStatus : egfstatuses) {
					validator.validate(egfStatus, errors);
				}
				break;
			case ACTION_UPDATE:
				Assert.notNull(egfstatuses, "EgfStatuses to update must not be null");
				for (EgfStatus egfStatus : egfstatuses) {
					validator.validate(egfStatus, errors);
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<EgfStatus> fetchRelated(List<EgfStatus> egfstatuses) {
		for (EgfStatus egfStatus : egfstatuses) {
			// fetch related items

		}

		return egfstatuses;
	}

	public List<EgfStatus> add(List<EgfStatus> egfstatuses, BindingResult errors) {
		egfstatuses = fetchRelated(egfstatuses);
		validate(egfstatuses, ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return egfstatuses;

	}

	public List<EgfStatus> update(List<EgfStatus> egfstatuses, BindingResult errors) {
		egfstatuses = fetchRelated(egfstatuses);
		validate(egfstatuses, ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return egfstatuses;

	}

	public void addToQue(CommonRequest<EgfStatusContract> request) {
		egfStatusRepository.add(request);
	}

	public Pagination<EgfStatus> search(EgfStatusSearch egfStatusSearch) {
		return egfStatusRepository.search(egfStatusSearch);
	}

	@Transactional
	public EgfStatus save(EgfStatus egfStatus) {
		return egfStatusRepository.save(egfStatus);
	}

	@Transactional
	public EgfStatus update(EgfStatus egfStatus) {
		return egfStatusRepository.update(egfStatus);
	}

}