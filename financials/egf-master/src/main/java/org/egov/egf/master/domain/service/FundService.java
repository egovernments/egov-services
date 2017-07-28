package org.egov.egf.master.domain.service;

import java.util.List;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.Fund;
import org.egov.egf.master.domain.model.FundSearch;
import org.egov.egf.master.domain.repository.FundRepository;
import org.egov.egf.master.web.requests.FundRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class FundService {

	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String ACTION_VIEW = "view";
	public static final String ACTION_EDIT = "edit";
	public static final String ACTION_SEARCH = "search";

	@Autowired
	private FundRepository fundRepository;

	@Autowired
	private SmartValidator validator;

	private BindingResult validate(List<Fund> funds, String method, BindingResult errors) {

		try {
			switch (method) {
			case ACTION_VIEW:
				// validator.validate(fundContractRequest.getFund(), errors);
				break;
			case ACTION_CREATE:
				Assert.notNull(funds, "Funds to create must not be null");
				for (Fund fund : funds) {
					validator.validate(fund, errors);
				}
				break;
			case ACTION_UPDATE:
				Assert.notNull(funds, "Funds to update must not be null");
				for (Fund fund : funds) {
					validator.validate(fund, errors);
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<Fund> fetchRelated(List<Fund> funds) {
		for (Fund fund : funds) {
			// fetch related items
		}

		return funds;
	}

	@Transactional
	public List<Fund> add(List<Fund> funds, BindingResult errors) {
		funds = fetchRelated(funds);
		validate(funds, ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return funds;

	}

	@Transactional
	public List<Fund> update(List<Fund> funds, BindingResult errors) {
		funds = fetchRelated(funds);
		validate(funds, ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return funds;

	}

	public void addToQue(FundRequest request) {
		fundRepository.add(request);
	}

	public Pagination<Fund> search(FundSearch fundSearch) {
		return fundRepository.search(fundSearch);
	}

	@Transactional
	public Fund save(Fund fund) {
		return fundRepository.save(fund);
	}

	@Transactional
	public Fund update(Fund fund) {
		return fundRepository.update(fund);
	}

}