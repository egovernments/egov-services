package org.egov.egf.master.domain.service;

import java.util.List;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.Functionary;
import org.egov.egf.master.domain.model.FunctionarySearch;
import org.egov.egf.master.domain.repository.FunctionaryRepository;
import org.egov.egf.master.web.contract.FunctionaryContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class FunctionaryService {

	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String ACTION_VIEW = "view";
	public static final String ACTION_EDIT = "edit";
	public static final String ACTION_SEARCH = "search";

	@Autowired
	private FunctionaryRepository functionaryRepository;

	@Autowired
	private SmartValidator validator;

	public BindingResult validate(List<Functionary> functionaries, String method, BindingResult errors) {

		try {
			switch (method) {
			case ACTION_VIEW:
				// validator.validate(functionaryContractRequest.getFunctionary(),
				// errors);
				break;
			case ACTION_CREATE:
				Assert.notNull(functionaries, "Functionaries to create must not be null");
				for (Functionary functionary : functionaries) {
					validator.validate(functionary, errors);
				}
				break;
			case ACTION_UPDATE:
				Assert.notNull(functionaries, "Functionaries to update must not be null");
				for (Functionary functionary : functionaries) {
					validator.validate(functionary, errors);
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<Functionary> fetchRelated(List<Functionary> functionaries) {
		for (Functionary functionary : functionaries) {
			// fetch related items

		}

		return functionaries;
	}

	public List<Functionary> add(List<Functionary> functionaries, BindingResult errors) {
		functionaries = fetchRelated(functionaries);
		validate(functionaries, ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return functionaries;

	}

	public List<Functionary> update(List<Functionary> functionaries, BindingResult errors) {
		functionaries = fetchRelated(functionaries);
		validate(functionaries, ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return functionaries;

	}

	public void addToQue(CommonRequest<FunctionaryContract> request) {
		functionaryRepository.add(request);
	}

	public Pagination<Functionary> search(FunctionarySearch functionarySearch) {
		return functionaryRepository.search(functionarySearch);
	}

	@Transactional
	public Functionary save(Functionary functionary) {
		return functionaryRepository.save(functionary);
	}

	@Transactional
	public Functionary update(Functionary functionary) {
		return functionaryRepository.update(functionary);
	}

}