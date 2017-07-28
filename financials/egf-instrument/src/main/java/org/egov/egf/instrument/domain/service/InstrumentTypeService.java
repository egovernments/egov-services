package org.egov.egf.instrument.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.instrument.domain.model.InstrumentType;
import org.egov.egf.instrument.domain.model.InstrumentTypeSearch;
import org.egov.egf.instrument.domain.repository.InstrumentTypeRepository;
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

    private InstrumentTypeRepository instrumentTypeRepository;

    private SmartValidator validator;

    @Autowired
    public InstrumentTypeService(SmartValidator validator, InstrumentTypeRepository instrumentTypeRepository) {
        this.validator = validator;
        this.instrumentTypeRepository = instrumentTypeRepository;
    }

    @Transactional
    public List<InstrumentType> save(List<InstrumentType> instrumentTypes, BindingResult errors) {

        List<InstrumentType> resultList = new ArrayList<InstrumentType>();

        try {

            instrumentTypes = fetchAndValidate(instrumentTypes, errors, ACTION_CREATE);

        } catch (CustomBindException e) {

            throw new CustomBindException(errors);
        }

        for (InstrumentType it : instrumentTypes) {

            resultList.add(save(it));

        }

        return resultList;
    }

    @Transactional
    public List<InstrumentType> update(List<InstrumentType> instrumentTypes, BindingResult errors) {

        List<InstrumentType> resultList = new ArrayList<InstrumentType>();

        try {

            instrumentTypes = fetchAndValidate(instrumentTypes, errors, ACTION_UPDATE);

        } catch (CustomBindException e) {

            throw new CustomBindException(errors);
        }

        for (InstrumentType it : instrumentTypes) {

            resultList.add(update(it));

        }

        return resultList;
    }

    private BindingResult validate(List<InstrumentType> instrumenttypes, String method, BindingResult errors) {

        try {
            switch (method) {
                case ACTION_VIEW:
                    // validator.validate(instrumentTypeContractRequest.getInstrumentType(),
                    // errors);
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
        if (instrumenttypes != null)
            for (InstrumentType instrumentType : instrumenttypes) {


            }

        return instrumenttypes;
    }

    @Transactional
    public List<InstrumentType> fetchAndValidate(List<InstrumentType> instrumenttypes, BindingResult errors,
                                                 String action) {
        instrumenttypes = fetchRelated(instrumenttypes);
        validate(instrumenttypes, action, errors);
        if (errors.hasErrors()) {
            throw new CustomBindException(errors);
        }
        return instrumenttypes;

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