package org.egov.egf.instrument.domain.service;

import java.util.List;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.instrument.domain.model.InstrumentType;
import org.egov.egf.instrument.domain.model.InstrumentTypeSearch;
import org.egov.egf.instrument.domain.repository.InstrumentTypeRepository;
import org.egov.egf.instrument.web.contract.InstrumentTypeContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class InstrumentTypeService {

	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String ACTION_VIEW = "view";
	public static final String ACTION_EDIT = "edit";
	public static final String ACTION_SEARCH = "search";

	@Autowired
	private InstrumentTypeRepository instrumentTypeRepository;

	@Autowired
	private SmartValidator validator;


	private BindingResult validate(List<InstrumentType> instrumenttypes, String method, BindingResult errors) {

		try {
			switch (method) {
			case ACTION_VIEW:
				// validator.validate(instrumentTypeContractRequest.getInstrumentType(), errors);
				break;
			case ACTION_CREATE:
				Assert.notNull(instrumenttypes, "InstrumentTypes to create must not be null");
				for (InstrumentType instrumentType : instrumenttypes) {
					validator.validate(instrumentType, errors);
				}
				break;
			case ACTION_UPDATE:
				Assert.notNull(instrumenttypes, "InstrumentTypes to update must not be null");
				for (InstrumentType instrumentType : instrumenttypes) {
					validator.validate(instrumentType, errors);
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<InstrumentType> fetchRelated(List<InstrumentType> instrumenttypes) {
		for (InstrumentType instrumentType : instrumenttypes) {
			// fetch related items

		}

		return instrumenttypes;
	}

	@Transactional
	public List<InstrumentType> add(List<InstrumentType> instrumenttypes, BindingResult errors) {
		instrumenttypes = fetchRelated(instrumenttypes);
		validate(instrumenttypes, ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return instrumenttypes;

	}

	@Transactional
	public List<InstrumentType> update(List<InstrumentType> instrumenttypes, BindingResult errors) {
		instrumenttypes = fetchRelated(instrumenttypes);
		validate(instrumenttypes, ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return instrumenttypes;

	}

	public void addToQue(CommonRequest<InstrumentTypeContract> request) {
		instrumentTypeRepository.add(request);
	}

	public Pagination<InstrumentType> search(InstrumentTypeSearch instrumentTypeSearch) {
		return instrumentTypeRepository.search(instrumentTypeSearch);
	}

	@Transactional
	public InstrumentType save(InstrumentType instrumentType) {
		return instrumentTypeRepository.save(instrumentType);
	}

	@Transactional
	public InstrumentType update(InstrumentType instrumentType) {
		return instrumentTypeRepository.update(instrumentType);
	}

}