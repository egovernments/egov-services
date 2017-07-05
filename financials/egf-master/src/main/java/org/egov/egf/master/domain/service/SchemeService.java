package org.egov.egf.master.domain.service;

import java.util.List;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.master.domain.model.Fund;
import org.egov.egf.master.domain.model.Scheme;
import org.egov.egf.master.domain.model.SchemeSearch;
import org.egov.egf.master.domain.repository.FundRepository;
import org.egov.egf.master.domain.repository.SchemeRepository;
import org.egov.egf.master.web.contract.SchemeContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class SchemeService {

	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String ACTION_VIEW = "view";
	public static final String ACTION_EDIT = "edit";
	public static final String ACTION_SEARCH = "search";

	@Autowired
	private SchemeRepository schemeRepository;

	@Autowired
	private SmartValidator validator;
	@Autowired
	private FundRepository fundRepository;

	public BindingResult validate(List<Scheme> schemes, String method, BindingResult errors) {

		try {
			switch (method) {
			case ACTION_VIEW:
				// validator.validate(schemeContractRequest.getScheme(),
				// errors);
				break;
			case ACTION_CREATE:
				Assert.notNull(schemes, "Schemes to create must not be null");
				for (Scheme scheme : schemes) {
					validator.validate(scheme, errors);
				}
				break;
			case ACTION_UPDATE:
				Assert.notNull(schemes, "Schemes to update must not be null");
				for (Scheme scheme : schemes) {
					validator.validate(scheme, errors);
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<Scheme> fetchRelated(List<Scheme> schemes) {
		for (Scheme scheme : schemes) {
			// fetch related items
			if (scheme.getFund() != null) {
				Fund fund = fundRepository.findById(scheme.getFund());
				if (fund == null) {
					throw new InvalidDataException("fund", "fund.invalid", " Invalid fund");
				}
				scheme.setFund(fund);
			}

		}

		return schemes;
	}

	public List<Scheme> add(List<Scheme> schemes, BindingResult errors) {
		schemes = fetchRelated(schemes);
		validate(schemes, ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return schemes;

	}

	public List<Scheme> update(List<Scheme> schemes, BindingResult errors) {
		schemes = fetchRelated(schemes);
		validate(schemes, ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return schemes;

	}

	public void addToQue(CommonRequest<SchemeContract> request) {
		schemeRepository.add(request);
	}

	public Pagination<Scheme> search(SchemeSearch schemeSearch) {
		return schemeRepository.search(schemeSearch);
	}

}