package org.egov.egf.instrument.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;
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
import org.egov.egf.instrument.web.requests.InstrumentDepositRequest;
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

	private InstrumentRepository instrumentRepository;

	private SmartValidator validator;

	private SurrenderReasonRepository surrenderReasonRepository;

	private BankContractRepository bankContractRepository;

	private FinancialStatusContractRepository financialStatusContractRepository;

	private BankAccountContractRepository bankAccountContractRepository;

	private InstrumentTypeRepository instrumentTypeRepository;
	

	@Autowired
	public InstrumentService(SmartValidator validator, InstrumentRepository instrumentRepository,
			SurrenderReasonRepository surrenderReasonRepository, BankContractRepository bankContractRepository,
			FinancialStatusContractRepository financialStatusContractRepository,
			BankAccountContractRepository bankAccountContractRepository,
			InstrumentTypeRepository instrumentTypeRepository) {
		this.validator = validator;
		this.instrumentRepository = instrumentRepository;
		this.surrenderReasonRepository = surrenderReasonRepository;
		this.bankContractRepository = bankContractRepository;
		this.financialStatusContractRepository = financialStatusContractRepository;
		this.bankAccountContractRepository = bankAccountContractRepository;
		this.instrumentTypeRepository = instrumentTypeRepository;
	}

	@Transactional
	public List<Instrument> create(List<Instrument> instruments, BindingResult errors, RequestInfo requestInfo) {

		try {

			instruments = fetchRelated(instruments);

			validate(instruments, ACTION_CREATE, errors);

			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}

		} catch (CustomBindException e) {

			throw new CustomBindException(errors);
		}

		return instrumentRepository.save(instruments, requestInfo);

	}

	@Transactional
	public List<Instrument> update(List<Instrument> instruments, BindingResult errors, RequestInfo requestInfo) {

		try {

			instruments = fetchRelated(instruments);

			validate(instruments, ACTION_UPDATE, errors);

			if (errors.hasErrors()) {
				throw new CustomBindException(errors);
			}

		} catch (CustomBindException e) {

			throw new CustomBindException(errors);
		}

		return instrumentRepository.update(instruments, requestInfo);

	}

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

		if (instruments != null)
			for (Instrument instrument : instruments) {

				instrument.setId(UUID.randomUUID().toString().replace("-", ""));

				// fetch related items

				if (instrument.getInstrumentType() != null && instrument.getInstrumentType().getName() != null) {
					InstrumentType instrumentType = instrumentTypeRepository.findById(instrument.getInstrumentType());
					if (instrumentType == null) {
						throw new InvalidDataException("instrumentType", "instrumentType.invalid",
								" Invalid instrumentType");
					}
					instrument.setInstrumentType(instrumentType);
				}
				if (instrument.getBank() != null && instrument.getBank().getCode() != null) {
					BankContract bank = bankContractRepository.findByCode(instrument.getBank());
					if (bank == null) {
						throw new InvalidDataException("bank", "bank.invalid", " Invalid bank");
					}
					instrument.setBank(bank);
				}
				if (instrument.getBankAccount() != null && instrument.getBankAccount().getAccountNumber() != null) {
					BankAccountContract bankAccount = bankAccountContractRepository
							.findByAccountNumber(instrument.getBankAccount());
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
				if (instrument.getSurrenderReason() != null) {
					SurrenderReason surrenderReason = surrenderReasonRepository
							.findById(instrument.getSurrenderReason());
					if (surrenderReason == null) {
						throw new InvalidDataException("surrenderReason", "surrenderReason.invalid",
								" Invalid surrenderReason");
					}
					instrument.setSurrenderReason(surrenderReason);
				}

			}

		return instruments;
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

	public List<Instrument> deposit(
			InstrumentDepositRequest instrumentDepositRequest,
			BindingResult errors, RequestInfo requestInfo) {
		Instrument instrument = new Instrument();
		instrument.setId(instrumentDepositRequest.getInstrumentDepositId());
		instrument.setTenantId(instrumentDepositRequest.getTenantId());
		
		FinancialStatusContract financialStatusContract = new FinancialStatusContract();
		financialStatusContract.setCode("Deposited");
		financialStatusContract.setModuleType("Instrument");
		
		instrument = instrumentRepository.findById(instrument);
		FinancialStatusContract financialStatusContract1 = new FinancialStatusContract();
		financialStatusContract1 = financialStatusContractRepository.findByModuleCode(financialStatusContract);
		instrument.setFinancialStatus(financialStatusContract1);
		List<Instrument> instrumentsToUpdate = new ArrayList<>();
		instrumentsToUpdate.add(instrument);
		return instrumentRepository.update(instrumentsToUpdate, requestInfo);
	}

}