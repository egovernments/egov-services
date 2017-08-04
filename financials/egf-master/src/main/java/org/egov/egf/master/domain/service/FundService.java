package org.egov.egf.master.domain.service;

import java.util.List;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.Bank;
import org.egov.egf.master.domain.model.Fund;
import org.egov.egf.master.domain.model.FundSearch;
import org.egov.egf.master.domain.repository.FundRepository;
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

    @Autowired
    private FundRepository fundRepository;

    @Autowired
    private SmartValidator validator;

    @Transactional
    public List<Fund> create(List<Fund> funds, BindingResult errors,
            RequestInfo requestInfo) {

        try {

            funds = fetchRelated(funds);

            validate(funds, Constants.ACTION_CREATE, errors);

            if (errors.hasErrors()) {
                throw new CustomBindException(errors);
            }
            for(Fund b:funds)b.setId(fundRepository.getNextSequence());

        } catch (CustomBindException e) {

            throw new CustomBindException(errors);
        }

        return fundRepository.save(funds, requestInfo);

    }

    @Transactional
    public List<Fund> update(List<Fund> funds, BindingResult errors,
            RequestInfo requestInfo) {

        try {

            funds = fetchRelated(funds);

            validate(funds, Constants.ACTION_UPDATE, errors);

            if (errors.hasErrors()) {
                throw new CustomBindException(errors);
            }

        } catch (CustomBindException e) {

            throw new CustomBindException(errors);
        }

        return fundRepository.update(funds, requestInfo);

    }

    private BindingResult validate(List<Fund> funds, String method, BindingResult errors) {

        try {
            switch (method) {
            case Constants.ACTION_VIEW:
                // validator.validate(fundContractRequest.getFund(), errors);
                break;
            case Constants.ACTION_CREATE:
                Assert.notNull(funds, "Funds to create must not be null");
                for (Fund fund : funds) {
                    validator.validate(fund, errors);
                }
                break;
            case Constants.ACTION_UPDATE:
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