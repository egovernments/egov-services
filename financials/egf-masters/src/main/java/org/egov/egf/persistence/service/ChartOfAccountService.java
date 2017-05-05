package org.egov.egf.persistence.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.egov.egf.domain.exception.InvalidDataException;
import org.egov.egf.persistence.entity.AccountCodePurpose;
import org.egov.egf.persistence.entity.ChartOfAccount;
import org.egov.egf.persistence.queue.contract.ChartOfAccountContract;
import org.egov.egf.persistence.queue.contract.ChartOfAccountContractRequest;
import org.egov.egf.persistence.repository.ChartOfAccountRepository;
import org.egov.egf.persistence.specification.ChartOfAccountSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

@Service
@Transactional(readOnly = true)
public class ChartOfAccountService {

    private final ChartOfAccountRepository chartOfAccountRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ChartOfAccountService(final ChartOfAccountRepository chartOfAccountRepository) {
        this.chartOfAccountRepository = chartOfAccountRepository;
    }

    @Autowired
    private SmartValidator validator;
    @Autowired
    private ChartOfAccountService chartOfAccountService;
    @Autowired
    private AccountCodePurposeService accountCodePurposeService;

    @Transactional
    public ChartOfAccount create(final ChartOfAccount chartOfAccount) {
        setChartOfAccount(chartOfAccount);
        return chartOfAccountRepository.save(chartOfAccount);
    }

    private void setChartOfAccount(final ChartOfAccount chartOfAccount) {
        if (chartOfAccount.getAccountCodePurpose() != null) {
            final AccountCodePurpose accountCodePurpose = accountCodePurposeService
                    .findOne(chartOfAccount.getAccountCodePurpose());
            if (accountCodePurpose == null)
                throw new InvalidDataException("accountCodePurpose", "accountCodePurpose.invalid",
                        " Invalid accountCodePurpose");
            chartOfAccount.setAccountCodePurpose(accountCodePurpose.getId());
        }
        if (chartOfAccount.getParentId() != null) {
            final ChartOfAccount parentId = chartOfAccountService.findOne(chartOfAccount.getParentId());
            if (parentId == null)
                throw new InvalidDataException("parentId", "parentId.invalid", " Invalid parentId");
            chartOfAccount.setParentId(parentId.getId());
        }
    }

    @Transactional
    public ChartOfAccount update(final ChartOfAccount chartOfAccount) {
        setChartOfAccount(chartOfAccount);
        return chartOfAccountRepository.save(chartOfAccount);
    }

    public List<ChartOfAccount> findAll() {
        return chartOfAccountRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public ChartOfAccount findByName(final String name) {
        return chartOfAccountRepository.findByName(name);
    }

    public ChartOfAccount findOne(final Long id) {
        return chartOfAccountRepository.findOne(id);
    }

    public Page<ChartOfAccount> search(final ChartOfAccountContractRequest chartOfAccountContractRequest) {
//        ChartOfAccountContract chartOfAccountContract = chartOfAccountContractRequest.getChartOfAccount();
//        
//        List<ChartOfAccount> chartOfAccounts = new ArrayList<ChartOfAccount>();
//        final StringBuilder queryStr = new StringBuilder();
//        
//        queryStr.append("select coa.glcode,coa.parentid,coa.tenantid from egf_chartofaccount as coa where coa.tenantid =: tenantId and coa.parentid.tenantid =: tenantId");
//        
//        if(chartOfAccountContract.getGlcode() != null)
//            queryStr.append("and coa.purposeId.id =: glcode and coa.purposeId.tenantid =: tenantId");
//        
//        final Query query = entityManager.createQuery(queryStr.toString());
//        query.setParameter("tenantId", chartOfAccountContract.getTenantId());
//
//        if(chartOfAccountContract.getGlcode() != null)
//            query.setParameter("glcode", chartOfAccountContract.getGlcode());
//        
//        chartOfAccounts = query.getResultList();
        
        
        final ChartOfAccountSpecification specification = new ChartOfAccountSpecification(
                chartOfAccountContractRequest.getChartOfAccount());
        final Pageable page = new PageRequest(chartOfAccountContractRequest.getPage().getOffSet(),
                chartOfAccountContractRequest.getPage().getPageSize());
        return chartOfAccountRepository.findAll(specification, page);
    }

    public BindingResult validate(final ChartOfAccountContractRequest chartOfAccountContractRequest, final String method,
            final BindingResult errors) {

        try {
            switch (method) {
            case "update":
                Assert.notNull(chartOfAccountContractRequest.getChartOfAccount(),
                        "ChartOfAccount to edit must not be null");
                validator.validate(chartOfAccountContractRequest.getChartOfAccount(), errors);
                break;
            case "view":
                // validator.validate(chartOfAccountContractRequest.getChartOfAccount(),
                // errors);
                break;
            case "create":
                Assert.notNull(chartOfAccountContractRequest.getChartOfAccounts(),
                        "ChartOfAccounts to create must not be null");
                for (final ChartOfAccountContract b : chartOfAccountContractRequest.getChartOfAccounts())
                    validator.validate(b, errors);
                break;
            case "updateAll":
                Assert.notNull(chartOfAccountContractRequest.getChartOfAccounts(),
                        "ChartOfAccounts to create must not be null");
                for (final ChartOfAccountContract b : chartOfAccountContractRequest.getChartOfAccounts())
                    validator.validate(b, errors);
                break;
            default:
                validator.validate(chartOfAccountContractRequest.getRequestInfo(), errors);
            }
        } catch (final IllegalArgumentException e) {
            errors.addError(new ObjectError("Missing data", e.getMessage()));
        }
        return errors;

    }

    public ChartOfAccountContractRequest fetchRelatedContracts(
            final ChartOfAccountContractRequest chartOfAccountContractRequest) {
        final ModelMapper model = new ModelMapper();
        for (final ChartOfAccountContract chartOfAccount : chartOfAccountContractRequest.getChartOfAccounts()) {
            if (chartOfAccount.getAccountCodePurpose() != null) {
                final AccountCodePurpose accountCodePurpose = accountCodePurposeService
                        .findOne(chartOfAccount.getAccountCodePurpose().getId());
                if (accountCodePurpose == null)
                    throw new InvalidDataException("accountCodePurpose", "accountCodePurpose.invalid",
                            " Invalid accountCodePurpose");
                model.map(accountCodePurpose, chartOfAccount.getAccountCodePurpose());
            }
            if (chartOfAccount.getParentId() != null) {
                final ChartOfAccount parentId = chartOfAccountService.findOne(chartOfAccount.getParentId().getId());
                if (parentId == null)
                    throw new InvalidDataException("parentId", "parentId.invalid", " Invalid parentId");
                model.map(parentId, chartOfAccount.getParentId());
            }
        }
        final ChartOfAccountContract chartOfAccount = chartOfAccountContractRequest.getChartOfAccount();
        if (chartOfAccount.getAccountCodePurpose() != null) {
            final AccountCodePurpose accountCodePurpose = accountCodePurposeService
                    .findOne(chartOfAccount.getAccountCodePurpose().getId());
            if (accountCodePurpose == null)
                throw new InvalidDataException("accountCodePurpose", "accountCodePurpose.invalid",
                        " Invalid accountCodePurpose");
            model.map(accountCodePurpose, chartOfAccount.getAccountCodePurpose());
        }
        if (chartOfAccount.getParentId() != null) {
            final ChartOfAccount parentId = chartOfAccountService.findOne(chartOfAccount.getParentId().getId());
            if (parentId == null)
                throw new InvalidDataException("parentId", "parentId.invalid", " Invalid parentId");
            model.map(parentId, chartOfAccount.getParentId());
        }
        return chartOfAccountContractRequest;
    }

}