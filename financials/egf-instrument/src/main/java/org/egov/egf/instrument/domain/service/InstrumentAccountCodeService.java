package org.egov.egf.instrument.domain.service;

import java.util.List;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.instrument.domain.model.InstrumentAccountCode;
import org.egov.egf.instrument.domain.model.InstrumentAccountCodeSearch;
import org.egov.egf.instrument.domain.model.InstrumentType;
import org.egov.egf.instrument.domain.repository.InstrumentAccountCodeRepository;
import org.egov.egf.instrument.domain.repository.InstrumentTypeRepository;
import org.egov.egf.instrument.web.requests.InstrumentAccountCodeRequest;
import org.egov.egf.master.web.contract.ChartOfAccountContract;
import org.egov.egf.master.web.repository.ChartOfAccountContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class InstrumentAccountCodeService {

	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String ACTION_VIEW = "view";
	public static final String ACTION_EDIT = "edit";
	public static final String ACTION_SEARCH = "search";

	@Autowired
	private InstrumentAccountCodeRepository instrumentAccountCodeRepository;

	@Autowired
	private SmartValidator validator;
	@Autowired
	private ChartOfAccountContractRepository chartOfAccountContractRepository;
	@Autowired
	private InstrumentTypeRepository instrumentTypeRepository;

	private BindingResult validate(List<InstrumentAccountCode> instrumentaccountcodes, String method,
			BindingResult errors) {

		try {
			switch (method) {
			case ACTION_VIEW:
				// validator.validate(instrumentAccountCodeContractRequest.getInstrumentAccountCode(),
				// errors);
				break;
			case ACTION_CREATE:
				Assert.notNull(instrumentaccountcodes, "InstrumentAccountCodes to create must not be null");
				for (InstrumentAccountCode instrumentAccountCode : instrumentaccountcodes) {
					validator.validate(instrumentAccountCode, errors);
				}
				break;
			case ACTION_UPDATE:
				Assert.notNull(instrumentaccountcodes, "InstrumentAccountCodes to update must not be null");
				for (InstrumentAccountCode instrumentAccountCode : instrumentaccountcodes) {
					validator.validate(instrumentAccountCode, errors);
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<InstrumentAccountCode> fetchRelated(List<InstrumentAccountCode> instrumentaccountcodes) {
		for (InstrumentAccountCode instrumentAccountCode : instrumentaccountcodes) {
			// fetch related items
			if (instrumentAccountCode.getInstrumentType() != null) {
				InstrumentType instrumentType = instrumentTypeRepository
						.findById(instrumentAccountCode.getInstrumentType());
				if (instrumentType == null) {
					throw new InvalidDataException("instrumentType", "instrumentType.invalid",
							" Invalid instrumentType");
				}
				instrumentAccountCode.setInstrumentType(instrumentType);
			}
			if (instrumentAccountCode.getAccountCode() != null) {
				ChartOfAccountContract accountCode = chartOfAccountContractRepository
						.findById(instrumentAccountCode.getAccountCode());
				if (accountCode == null) {
					throw new InvalidDataException("accountCode", "accountCode.invalid", " Invalid accountCode");
				}
				instrumentAccountCode.setAccountCode(accountCode);
			}

		}

		return instrumentaccountcodes;
	}

	@Transactional
	public List<InstrumentAccountCode> add(List<InstrumentAccountCode> instrumentaccountcodes, BindingResult errors) {
		instrumentaccountcodes = fetchRelated(instrumentaccountcodes);
		validate(instrumentaccountcodes, ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return instrumentaccountcodes;

	}

	@Transactional
	public List<InstrumentAccountCode> update(List<InstrumentAccountCode> instrumentaccountcodes,
			BindingResult errors) {
		instrumentaccountcodes = fetchRelated(instrumentaccountcodes);
		validate(instrumentaccountcodes, ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return instrumentaccountcodes;

	}

	public void addToQue(InstrumentAccountCodeRequest request) {
		instrumentAccountCodeRepository.add(request);
	}

	public Pagination<InstrumentAccountCode> search(InstrumentAccountCodeSearch instrumentAccountCodeSearch) {
		return instrumentAccountCodeRepository.search(instrumentAccountCodeSearch);
	}

	@Transactional
	public InstrumentAccountCode save(InstrumentAccountCode instrumentAccountCode) {
		return instrumentAccountCodeRepository.save(instrumentAccountCode);
	}

	@Transactional
	public InstrumentAccountCode update(InstrumentAccountCode instrumentAccountCode) {
		return instrumentAccountCodeRepository.update(instrumentAccountCode);
	}

}