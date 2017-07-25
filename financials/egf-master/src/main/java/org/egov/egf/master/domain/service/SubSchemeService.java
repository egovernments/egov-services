package org.egov.egf.master.domain.service;

import java.util.List;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.Scheme;
import org.egov.egf.master.domain.model.SubScheme;
import org.egov.egf.master.domain.model.SubSchemeSearch;
import org.egov.egf.master.domain.repository.SchemeRepository;
import org.egov.egf.master.domain.repository.SubSchemeRepository;
import org.egov.egf.master.web.requests.SubSchemeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class SubSchemeService {

	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String ACTION_VIEW = "view";
	public static final String ACTION_EDIT = "edit";
	public static final String ACTION_SEARCH = "search";

	@Autowired
	private SubSchemeRepository subSchemeRepository;

	@Autowired
	private SmartValidator validator;
	@Autowired
	private SchemeRepository schemeRepository;

	private BindingResult validate(List<SubScheme> subschemes, String method, BindingResult errors) {

		try {
			switch (method) {
			case ACTION_VIEW:
				// validator.validate(subSchemeContractRequest.getSubScheme(),
				// errors);
				break;
			case ACTION_CREATE:
				Assert.notNull(subschemes, "SubSchemes to create must not be null");
				for (SubScheme subScheme : subschemes) {
					validator.validate(subScheme, errors);
				}
				break;
			case ACTION_UPDATE:
				Assert.notNull(subschemes, "SubSchemes to update must not be null");
				for (SubScheme subScheme : subschemes) {
					validator.validate(subScheme, errors);
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<SubScheme> fetchRelated(List<SubScheme> subschemes) {
		for (SubScheme subScheme : subschemes) {
			// fetch related items
			if (subScheme.getScheme() != null) {
				Scheme scheme = schemeRepository.findById(subScheme.getScheme());
				if (scheme == null) {
					throw new InvalidDataException("scheme", "scheme.invalid", " Invalid scheme");
				}
				subScheme.setScheme(scheme);
			}

		}

		return subschemes;
	}

	@Transactional
	public List<SubScheme> add(List<SubScheme> subschemes, BindingResult errors) {
		subschemes = fetchRelated(subschemes);
		validate(subschemes, ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return subschemes;

	}

	@Transactional
	public List<SubScheme> update(List<SubScheme> subschemes, BindingResult errors) {
		subschemes = fetchRelated(subschemes);
		validate(subschemes, ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return subschemes;

	}

	public void addToQue(SubSchemeRequest request) {
		subSchemeRepository.add(request);
	}

	public Pagination<SubScheme> search(SubSchemeSearch subSchemeSearch) {
		return subSchemeRepository.search(subSchemeSearch);
	}

	@Transactional
	public SubScheme save(SubScheme subScheme) {
		return subSchemeRepository.save(subScheme);
	}

	@Transactional
	public SubScheme update(SubScheme subScheme) {
		return subSchemeRepository.update(subScheme);
	}

}