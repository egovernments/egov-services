package org.egov.egf.persistence.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.egf.domain.exception.InvalidDataException;
import org.egov.egf.persistence.entity.BankAccount;
import org.egov.egf.persistence.entity.BankBranch;
import org.egov.egf.persistence.entity.ChartOfAccount;
import org.egov.egf.persistence.entity.Fund;
import org.egov.egf.persistence.queue.contract.BankAccountContract;
import org.egov.egf.persistence.queue.contract.BankAccountContractRequest;
import org.egov.egf.persistence.repository.BankAccountRepository;
import org.egov.egf.persistence.specification.BankAccountSpecification;
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
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public BankAccountService(final BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Autowired
    private SmartValidator validator;
    @Autowired
    private ChartOfAccountService chartOfAccountService;

    @Autowired
    private BankBranchService bankBranchService;
    @Autowired
    private FundService fundService;

    @Transactional
    public BankAccount create(final BankAccount bankAccount) {
        setBankAccount(bankAccount);
        return bankAccountRepository.save(bankAccount);
    }

    @Transactional
    public BankAccount update(final BankAccount bankAccount) {
        setBankAccount(bankAccount);
        return bankAccountRepository.save(bankAccount);
    }

    private void setBankAccount(final BankAccount bankAccount) {
        if (bankAccount.getBankBranch() != null) {
            final BankBranch bankBranch = bankBranchService.findOne(bankAccount.getBankBranch().getId());
            if (bankBranch == null)
                throw new InvalidDataException("bankBranch", "bankBranch.invalid", " Invalid bankBranch");
            bankAccount.setBankBranch(bankBranch);
        }
        if (bankAccount.getChartOfAccount() != null) {
            final ChartOfAccount chartOfAccount = chartOfAccountService.findOne(bankAccount.getChartOfAccount().getId());
            if (chartOfAccount == null)
                throw new InvalidDataException("chartOfAccount", "chartOfAccount.invalid",
                        " Invalid chartOfAccount");
            bankAccount.setChartOfAccount(chartOfAccount);
        }
        if (bankAccount.getFund() != null) {
            final Fund fund = fundService.findOne(bankAccount.getFund().getId());
            if (fund == null)
                throw new InvalidDataException("fund", "fund.invalid", " Invalid fund");
            bankAccount.setFund(fund);
        }
    }

    public List<BankAccount> findAll() {
        return bankAccountRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public BankAccount findOne(final Long id) {
        return bankAccountRepository.findOne(id);
    }

    public Page<BankAccount> search(final BankAccountContractRequest bankAccountContractRequest) {
        final BankAccountSpecification specification = new BankAccountSpecification(
                bankAccountContractRequest.getBankAccount());
        final Pageable page = new PageRequest(bankAccountContractRequest.getPage().getOffSet(),
                bankAccountContractRequest.getPage().getPageSize());
        return bankAccountRepository.findAll(specification, page);
    }

    public BindingResult validate(final BankAccountContractRequest bankAccountContractRequest, final String method,
            final BindingResult errors) {

        try {
            switch (method) {
            case "update":
                Assert.notNull(bankAccountContractRequest.getBankAccount(), "BankAccount to edit must not be null");
                validator.validate(bankAccountContractRequest.getBankAccount(), errors);
                break;
            case "view":
                // validator.validate(bankAccountContractRequest.getBankAccount(),
                // errors);
                break;
            case "create":
                Assert.notNull(bankAccountContractRequest.getBankAccounts(), "BankAccounts to create must not be null");
                for (final BankAccountContract b : bankAccountContractRequest.getBankAccounts())
                    validator.validate(b, errors);
                break;
            case "updateAll":
                Assert.notNull(bankAccountContractRequest.getBankAccounts(), "BankAccounts to create must not be null");
                for (final BankAccountContract b : bankAccountContractRequest.getBankAccounts())
                    validator.validate(b, errors);
                break;
            default:
                validator.validate(bankAccountContractRequest.getRequestInfo(), errors);
            }
        } catch (final IllegalArgumentException e) {
            errors.addError(new ObjectError("Missing data", e.getMessage()));
        }
        return errors;

    }

    public BankAccountContractRequest fetchRelatedContracts(final BankAccountContractRequest bankAccountContractRequest) {
        final ModelMapper model = new ModelMapper();
        for (final BankAccountContract bankAccount : bankAccountContractRequest.getBankAccounts()) {
            if (bankAccount.getBankBranch() != null) {
                final BankBranch bankBranch = bankBranchService.findOne(bankAccount.getBankBranch().getId());
                if (bankBranch == null)
                    throw new InvalidDataException("bankBranch", "bankBranch.invalid", " Invalid bankBranch");
                model.map(bankBranch, bankAccount.getBankBranch());
            }
            if (bankAccount.getChartOfAccount() != null) {
                final ChartOfAccount chartOfAccount = chartOfAccountService.findOne(bankAccount.getChartOfAccount().getId());
                if (chartOfAccount == null)
                    throw new InvalidDataException("chartOfAccount", "chartOfAccount.invalid",
                            " Invalid chartOfAccount");
                model.map(chartOfAccount, bankAccount.getChartOfAccount());
            }
            if (bankAccount.getFund() != null) {
                final Fund fund = fundService.findOne(bankAccount.getFund().getId());
                if (fund == null)
                    throw new InvalidDataException("fund", "fund.invalid", " Invalid fund");
                model.map(fund, bankAccount.getFund());
            }

        }
        final BankAccountContract bankAccount = bankAccountContractRequest.getBankAccount();
        if (bankAccount.getBankBranch() != null) {
            final BankBranch bankBranch = bankBranchService.findOne(bankAccount.getBankBranch().getId());
            if (bankBranch == null)
                throw new InvalidDataException("bankBranch", "bankBranch.invalid", " Invalid bankBranch");
            model.map(bankBranch, bankAccount.getBankBranch());
        }
        if (bankAccount.getChartOfAccount() != null) {
            final ChartOfAccount chartOfAccount = chartOfAccountService.findOne(bankAccount.getChartOfAccount().getId());
            if (chartOfAccount == null)
                throw new InvalidDataException("chartOfAccount", "chartOfAccount.invalid", " Invalid chartOfAccount");
            model.map(chartOfAccount, bankAccount.getChartOfAccount());
        }
        if (bankAccount.getFund() != null) {
            final Fund fund = fundService.findOne(bankAccount.getFund().getId());
            if (fund == null)
                throw new InvalidDataException("fund", "fund.invalid", " Invalid fund");
            model.map(fund, bankAccount.getFund());
        }

        return bankAccountContractRequest;
    }
}