package org.egov.egf.instrument.domain.service;

import java.util.List;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.instrument.domain.model.Instrument;
import org.egov.egf.instrument.domain.model.InstrumentSearch;
import org.egov.egf.instrument.domain.model.InstrumentType;
import org.egov.egf.instrument.domain.model.SurrenderReason;
import org.egov.egf.instrument.domain.repository.InstrumentRepository;
import org.egov.egf.instrument.domain.repository.InstrumentTypeRepository;
import org.egov.egf.instrument.domain.repository.SurrenderReasonRepository;
import org.egov.egf.instrument.web.requests.InstrumentRequest;
import org.egov.egf.master.web.contract.BankAccountContract;
import org.egov.egf.master.web.contract.BankContract;
import org.egov.egf.master.web.contract.FinancialStatusContract;
import org.egov.egf.master.web.repository.BankAccountContractRepository;
import org.egov.egf.master.web.repository.BankContractRepository;
import org.egov.egf.master.web.repository.FinancialStatusContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class InstrumentService {

	public static final String ACTION_CREATE = "create";
	public static final String ACTION_UPDATE = "update";
	public static final String ACTION_VIEW = "view";
	public static final String ACTION_EDIT = "edit";
	public static final String ACTION_SEARCH = "search";

	@Autowired
	private InstrumentRepository instrumentRepository;

	@Autowired
	private SmartValidator validator;
	@Autowired
	private SurrenderReasonRepository surrenderReasonRepository;
	@Autowired
	private BankContractRepository bankContractRepository;
	@Autowired
	private FinancialStatusContractRepository financialStatusContractRepository;
	@Autowired
	private BankAccountContractRepository bankAccountContractRepository;
	@Autowired
	private InstrumentTypeRepository instrumentTypeRepository;

	private BindingResult validate(List<Instrument> instruments, String method, BindingResult errors) {

		try {
			switch (method) {
			case ACTION_VIEW:
				// validator.validate(instrumentContractRequest.getInstrument(),
				// errors);
				break;
			case ACTION_CREATE:
				Assert.notNull(instruments, "Instruments to create must not be null");
				for (Instrument instrument : instruments) {
					validator.validate(instrument, errors);
				}
				break;
			case ACTION_UPDATE:
				Assert.notNull(instruments, "Instruments to update must not be null");
				for (Instrument instrument : instruments) {
					validator.validate(instrument, errors);
				}
				break;
			default:

			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}
		return errors;

	}

	public List<Instrument> fetchRelated(List<Instrument> instruments) {
		for (Instrument instrument : instruments) {
			// fetch related items
			if (instrument.getInstrumentType() != null) {
				InstrumentType instrumentType = instrumentTypeRepository.findById(instrument.getInstrumentType());
				if (instrumentType == null) {
					throw new InvalidDataException("instrumentType", "instrumentType.invalid",
							" Invalid instrumentType");
				}
				instrument.setInstrumentType(instrumentType);
			}
			if (instrument.getBank() != null) {
				BankContract bank = bankContractRepository.findById(instrument.getBank());
				if (bank == null) {
					throw new InvalidDataException("bank", "bank.invalid", " Invalid bank");
				}
				instrument.setBank(bank);
			}
			if (instrument.getBankAccount() != null) {
				BankAccountContract bankAccount = bankAccountContractRepository.findById(instrument.getBankAccount());
				if (bankAccount == null) {
					throw new InvalidDataException("bankAccount", "bankAccount.invalid", " Invalid bankAccount");
				}
				instrument.setBankAccount(bankAccount);
			}
			if (instrument.getFinancialStatus() != null) {
				FinancialStatusContract financialStatus = financialStatusContractRepository
						.findById(instrument.getFinancialStatus());
				if (financialStatus == null) {
					throw new InvalidDataException("financialStatus", "financialStatus.invalid",
							" Invalid financialStatus");
				}
				instrument.setFinancialStatus(financialStatus);
			}
			if (instrument.getSurrendarReason() != null) {
				SurrenderReason surrendarReason = surrenderReasonRepository.findById(instrument.getSurrendarReason());
				if (surrendarReason == null) {
					throw new InvalidDataException("surrendarReason", "surrendarReason.invalid",
							" Invalid surrendarReason");
				}
				instrument.setSurrendarReason(surrendarReason);
			}

		}

		return instruments;
	}

	@Transactional
	public List<Instrument> add(List<Instrument> instruments, BindingResult errors) {
		instruments = fetchRelated(instruments);
		validate(instruments, ACTION_CREATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return instruments;

	}

	@Transactional
	public List<Instrument> update(List<Instrument> instruments, BindingResult errors) {
		instruments = fetchRelated(instruments);
		validate(instruments, ACTION_UPDATE, errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}
		return instruments;

	}

	public void addToQue(InstrumentRequest request) {
		instrumentRepository.add(request);
	}

	public Pagination<Instrument> search(InstrumentSearch instrumentSearch) {
		return instrumentRepository.search(instrumentSearch);
	}

	@Transactional
	public Instrument save(Instrument instrument) {
		return instrumentRepository.save(instrument);
	}

	@Transactional
	public Instrument update(Instrument instrument) {
		return instrumentRepository.update(instrument);
	}

}