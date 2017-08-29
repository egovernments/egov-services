package org.egov.egf.master.domain.service;

import org.egov.common.constants.Constants;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.model.Pagination;
import org.egov.egf.master.domain.model.ChartOfAccount;
import org.egov.egf.master.domain.model.ChartOfAccountSearch;
import org.egov.egf.master.domain.model.Recovery;
import org.egov.egf.master.domain.model.RecoverySearch;
import org.egov.egf.master.domain.repository.ChartOfAccountRepository;
import org.egov.egf.master.domain.repository.RecoveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RecoveryService {

    @Autowired
    private RecoveryRepository recoveryRepository;

    @Autowired
    private SmartValidator validator;

    @Autowired
    private ChartOfAccountRepository chartOfAccountRepository;

    @Transactional
    public List<Recovery> create(List<Recovery> recoveries, BindingResult errors,
                                 RequestInfo requestInfo) {

        try {

            recoveries = fetchRelated(recoveries);

            validate(recoveries, Constants.ACTION_CREATE, errors);

            if (errors.hasErrors()) {
                throw new CustomBindException(errors);
            }
            for (Recovery b : recoveries) {
                b.setId(recoveryRepository.getNextSequence());
            }

        } catch (CustomBindException e) {

            throw new CustomBindException(errors);
        }

        return recoveryRepository.save(recoveries, requestInfo);

    }

    @Transactional
    public List<Recovery> update(List<Recovery> recoveries, BindingResult errors,
                                 RequestInfo requestInfo) {

        try {

            recoveries = fetchRelated(recoveries);

            validate(recoveries, Constants.ACTION_UPDATE, errors);

            if (errors.hasErrors()) {
                throw new CustomBindException(errors);
            }
            for (Recovery b : recoveries) {
            }

        } catch (CustomBindException e) {

            throw new CustomBindException(errors);
        }

        return recoveryRepository.update(recoveries, requestInfo);

    }

    private BindingResult validate(List<Recovery> recoveries, String method, BindingResult errors) {

        try {
            switch (method) {
                case Constants.ACTION_VIEW:
                    // validator.validate(fundContractRequest.getFund(), errors);
                    break;
                case Constants.ACTION_CREATE:
                    Assert.notNull(recoveries, "Recoverys to create must not be null");
                    for (Recovery recovery : recoveries) {
                        validator.validate(recovery, errors);
                    }
                    break;
                case Constants.ACTION_UPDATE:
                    Assert.notNull(recoveries, "Recoverys to update must not be null");
                    for (Recovery recovery : recoveries) {
                        Assert.notNull(recovery.getId(), "Recovery ID to update must not be null");
                        validator.validate(recovery, errors);
                    }
                    break;
                default:

            }
        } catch (IllegalArgumentException e) {
            errors.addError(new ObjectError("Missing data", e.getMessage()));
        }
        return errors;

    }

    public List<Recovery> fetchRelated(List<Recovery> recoveries) {
        for (Recovery recovery : recoveries) {
            // fetch related items
            if(recovery.getChartOfAccount() != null) {
                ChartOfAccountSearch chartOfAccountSearch = new ChartOfAccountSearch();
                chartOfAccountSearch.setGlcode(recovery.getChartOfAccount().getGlcode());
                Pagination<ChartOfAccount> response = chartOfAccountRepository.search(chartOfAccountSearch);
                if (response == null || response.getPagedData() == null || response.getPagedData().isEmpty()) {
                    throw new InvalidDataException("chartOfAccount", "chartOfAccount.invalid", " Invalid chartOfAccount");
                }else{
                   recovery.setChartOfAccount(response.getPagedData().get(0));
                }
            }
        }

        return recoveries;

    }

    public Pagination<Recovery> search(RecoverySearch recoverySearch) {
        Assert.notNull(recoverySearch.getTenantId(), "tenantId is mandatory for voucher search");
        return recoveryRepository.search(recoverySearch);
    }

    @Transactional
    public Recovery save(Recovery recovery) {
        return recoveryRepository.save(recovery);
    }

    @Transactional
    public Recovery update(Recovery recovery) {
        return recoveryRepository.update(recovery);
    }

}