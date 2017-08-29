package org.egov.egf.master.domain.service;

import java.util.List;

import org.egov.common.constants.Constants;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.Bank;
import org.egov.egf.master.domain.model.Fund;
import org.egov.egf.master.domain.model.Scheme;
import org.egov.egf.master.domain.model.SchemeSearch;
import org.egov.egf.master.domain.repository.FundRepository;
import org.egov.egf.master.domain.repository.SchemeRepository;
import org.egov.egf.master.web.requests.SchemeRequest;
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

	@Autowired
	private SchemeRepository schemeRepository;

	@Autowired
	private SmartValidator validator;
	@Autowired
	private FundRepository fundRepository;

	private BindingResult validate(List<Scheme> schemes, String method, BindingResult errors) {

		try {
			switch (method) {
			case Constants.ACTION_VIEW:
				// validator.validate(schemeContractRequest.getScheme(),
				// errors);
				break;
			case Constants.ACTION_CREATE:
				Assert.notNull(schemes, "Schemes to create must not be null");
				for (Scheme scheme : schemes) {
					validator.validate(scheme, errors);
				}
				break;
			case Constants.ACTION_UPDATE:
				Assert.notNull(schemes, "Schemes to update must not be null");
				for (Scheme scheme : schemes) {
				        Assert.notNull(scheme.getId(), "Scheme ID to update must not be null");
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

	@Transactional
	public List<Scheme> add(List<Scheme> schemes, BindingResult errors) {
		schemes = fetchRelated(schemes);
		validate(schemes, Constants.ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		for(Scheme b:schemes)b.setId(schemeRepository.getNextSequence());
		return schemes;

	}

	@Transactional
	public List<Scheme> update(List<Scheme> schemes, BindingResult errors) {
		schemes = fetchRelated(schemes);
		validate(schemes, Constants.ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return schemes;

	}

	public void addToQue(SchemeRequest request) {
		schemeRepository.add(request);
	}

	public Pagination<Scheme> search(SchemeSearch schemeSearch) {
	        Assert.notNull(schemeSearch.getTenantId(), "tenantId is mandatory for scheme search");
		return schemeRepository.search(schemeSearch);
	}

	@Transactional
	public Scheme save(Scheme scheme) {
		return schemeRepository.save(scheme);
	}

	@Transactional
	public Scheme update(Scheme scheme) {
		return schemeRepository.update(scheme);
	}

}