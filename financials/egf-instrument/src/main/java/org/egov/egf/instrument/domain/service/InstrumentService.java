package org.egov.egf.instrument.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.instrument.domain.model.Instrument;
import org.egov.egf.instrument.domain.model.InstrumentSearch;
import org.egov.egf.instrument.domain.repository.InstrumentRepository;
import org.egov.egf.instrument.domain.repository.InstrumentTypeRepository;
import org.egov.egf.instrument.domain.repository.SurrenderReasonRepository;
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
    public List<Instrument> save(List<Instrument> instruments, BindingResult errors) {

        List<Instrument> resultList = new ArrayList<Instrument>();

        try {

            instruments = fetchAndValidate(instruments, errors, ACTION_CREATE);

        } catch (CustomBindException e) {

            throw new CustomBindException(errors);
        }

        for (Instrument i : instruments) {

            resultList.add(save(i));

        }

        return resultList;
    }

    @Transactional
    public List<Instrument> update(List<Instrument> instruments, BindingResult errors) {

        List<Instrument> resultList = new ArrayList<Instrument>();

        try {

            instruments = fetchAndValidate(instruments, errors, ACTION_UPDATE);

        } catch (CustomBindException e) {

            throw new CustomBindException(errors);
        }

        for (Instrument i : instruments) {

            resultList.add(update(i));

        }

        return resultList;
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
   
    	//entity.setId(UUID.randomUUID().toString().replace("-", ""));
    	 if (instruments != null)
            for (Instrument instrument : instruments) {
            	
            	instrument.setId(UUID.randomUUID().toString().replace("-", ""));
                // fetch related items
            /*    if (instrument.getInstrumentType() != null) {
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
                }*/

            }

        return instruments;
    }

    @Transactional
    public List<Instrument> fetchAndValidate(List<Instrument> instruments, BindingResult errors, String action) {
        instruments = fetchRelated(instruments);
        validate(instruments, action, errors);
        if (errors.hasErrors()) {
            throw new CustomBindException(errors);
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

}