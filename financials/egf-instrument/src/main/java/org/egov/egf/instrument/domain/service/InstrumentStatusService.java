package org.egov.egf.instrument.domain.service;

import java.util.List;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.common.web.contract.CommonRequest;
import org.egov.egf.instrument.domain.model.InstrumentStatus;
import org.egov.egf.instrument.domain.model.InstrumentStatusSearch;
import org.egov.egf.instrument.domain.repository.InstrumentStatusRepository;
import org.egov.egf.instrument.web.contract.InstrumentStatusContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class InstrumentStatusService {

	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String ACTION_VIEW = "view";
	public static final String ACTION_EDIT = "edit";
	public static final String ACTION_SEARCH = "search";

	@Autowired
	private InstrumentStatusRepository instrumentStatusRepository;

	@Autowired
	private SmartValidator validator;


	private BindingResult validate(List<InstrumentStatus> instrumentstatuses, String method, BindingResult errors) {

		try {
			switch (method) {
			case ACTION_VIEW:
				// validator.validate(instrumentStatusContractRequest.getInstrumentStatus(), errors);
				break;
			case ACTION_CREATE:
				Assert.notNull(instrumentstatuses, "InstrumentStatuses to create must not be null");
				for (InstrumentStatus instrumentStatus : instrumentstatuses) {
					validator.validate(instrumentStatus, errors);
				}
				break;
			case ACTION_UPDATE:
				Assert.notNull(instrumentstatuses, "InstrumentStatuses to update must not be null");
				for (InstrumentStatus instrumentStatus : instrumentstatuses) {
					validator.validate(instrumentStatus, errors);
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<InstrumentStatus> fetchRelated(List<InstrumentStatus> instrumentstatuses) {
		for (InstrumentStatus instrumentStatus : instrumentstatuses) {
			// fetch related items

		}

		return instrumentstatuses;
	}

	@Transactional
	public List<InstrumentStatus> add(List<InstrumentStatus> instrumentstatuses, BindingResult errors) {
		instrumentstatuses = fetchRelated(instrumentstatuses);
		validate(instrumentstatuses, ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return instrumentstatuses;

	}

	@Transactional
	public List<InstrumentStatus> update(List<InstrumentStatus> instrumentstatuses, BindingResult errors) {
		instrumentstatuses = fetchRelated(instrumentstatuses);
		validate(instrumentstatuses, ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return instrumentstatuses;

	}

	public void addToQue(CommonRequest<InstrumentStatusContract> request) {
		instrumentStatusRepository.add(request);
	}

	public Pagination<InstrumentStatus> search(InstrumentStatusSearch instrumentStatusSearch) {
		return instrumentStatusRepository.search(instrumentStatusSearch);
	}

	@Transactional
	public InstrumentStatus save(InstrumentStatus instrumentStatus) {
		return instrumentStatusRepository.save(instrumentStatus);
	}

	@Transactional
	public InstrumentStatus update(InstrumentStatus instrumentStatus) {
		return instrumentStatusRepository.update(instrumentStatus);
	}

}