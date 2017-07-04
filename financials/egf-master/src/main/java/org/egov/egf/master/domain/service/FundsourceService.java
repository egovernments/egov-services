package org.egov.egf.master.domain.service;

import java.util.List;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.Fundsource;
import org.egov.egf.master.domain.model.FundsourceSearch;
import org.egov.egf.master.domain.repository.FundsourceRepository;
import org.egov.egf.master.web.contract.FundsourceContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class FundsourceService {

	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String ACTION_VIEW = "view";
	public static final String ACTION_EDIT = "edit";
	public static final String ACTION_SEARCH = "search";

	 

	@Autowired
	private SmartValidator validator;
	@Autowired
	private FundsourceRepository fundsourceRepository;

	public BindingResult validate(List<Fundsource> fundsources, String method, BindingResult errors) {

		try {
			switch (method) {
			case ACTION_VIEW:
				// validator.validate(fundsourceContractRequest.getFundsource(),
				// errors);
				break;
			case ACTION_CREATE:
				Assert.notNull(fundsources, "Fundsources to create must not be null");
				for (Fundsource fundsource : fundsources) {
					validator.validate(fundsource, errors);
				}
				break;
			case ACTION_UPDATE:
				Assert.notNull(fundsources, "Fundsources to update must not be null");
				for (Fundsource fundsource : fundsources) {
					validator.validate(fundsource, errors);
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<Fundsource> fetchRelated(List<Fundsource> fundsources) {
		for (Fundsource fundsource : fundsources) {
			// fetch related items
			if (fundsource.getFundSource() != null) {
				Fundsource fundSource = fundsourceRepository.findById(fundsource.getFundSource());
				if (fundSource == null) {
					throw new InvalidDataException("fundSource", "fundSource.invalid", " Invalid fundSource");
				}
				fundsource.setFundSource(fundSource);
			}

		}

		return fundsources;
	}

	public List<Fundsource> add(List<Fundsource> fundsources, BindingResult errors) {
		fundsources = fetchRelated(fundsources);
		validate(fundsources, ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return fundsources;

	}

	public List<Fundsource> update(List<Fundsource> fundsources, BindingResult errors) {
		fundsources = fetchRelated(fundsources);
		validate(fundsources, ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return fundsources;

	}

	public void addToQue(CommonRequest<FundsourceContract> request) {
		fundsourceRepository.add(request);
	}

	public Pagination<Fundsource> search(FundsourceSearch fundsourceSearch) {
		return fundsourceRepository.search(fundsourceSearch);
	}

}