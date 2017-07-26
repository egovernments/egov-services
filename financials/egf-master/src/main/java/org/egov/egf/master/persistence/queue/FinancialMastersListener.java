package org.egov.egf.master.persistence.queue;

import java.util.Map;

import org.egov.egf.master.domain.model.AccountCodePurpose;
import org.egov.egf.master.domain.model.AccountDetailKey;
import org.egov.egf.master.domain.model.AccountDetailType;
import org.egov.egf.master.domain.model.AccountEntity;
import org.egov.egf.master.domain.model.Bank;
import org.egov.egf.master.domain.model.BankAccount;
import org.egov.egf.master.domain.model.BankBranch;
import org.egov.egf.master.domain.model.BudgetGroup;
import org.egov.egf.master.domain.model.ChartOfAccount;
import org.egov.egf.master.domain.model.ChartOfAccountDetail;
import org.egov.egf.master.domain.model.FinancialStatus;
import org.egov.egf.master.domain.model.FinancialYear;
import org.egov.egf.master.domain.model.FiscalPeriod;
import org.egov.egf.master.domain.model.Function;
import org.egov.egf.master.domain.model.Functionary;
import org.egov.egf.master.domain.model.Fund;
import org.egov.egf.master.domain.model.Fundsource;
import org.egov.egf.master.domain.model.Scheme;
import org.egov.egf.master.domain.model.SubScheme;
import org.egov.egf.master.domain.model.Supplier;
import org.egov.egf.master.domain.service.AccountCodePurposeService;
import org.egov.egf.master.domain.service.AccountDetailKeyService;
import org.egov.egf.master.domain.service.AccountDetailTypeService;
import org.egov.egf.master.domain.service.AccountEntityService;
import org.egov.egf.master.domain.service.BankAccountService;
import org.egov.egf.master.domain.service.BankBranchService;
import org.egov.egf.master.domain.service.BankService;
import org.egov.egf.master.domain.service.BudgetGroupService;
import org.egov.egf.master.domain.service.ChartOfAccountDetailService;
import org.egov.egf.master.domain.service.ChartOfAccountService;
import org.egov.egf.master.domain.service.FinancialStatusService;
import org.egov.egf.master.domain.service.FinancialYearService;
import org.egov.egf.master.domain.service.FiscalPeriodService;
import org.egov.egf.master.domain.service.FunctionService;
import org.egov.egf.master.domain.service.FunctionaryService;
import org.egov.egf.master.domain.service.FundService;
import org.egov.egf.master.domain.service.FundsourceService;
import org.egov.egf.master.domain.service.SchemeService;
import org.egov.egf.master.domain.service.SubSchemeService;
import org.egov.egf.master.domain.service.SupplierService;
import org.egov.egf.master.web.contract.AccountCodePurposeContract;
import org.egov.egf.master.web.contract.AccountDetailKeyContract;
import org.egov.egf.master.web.contract.AccountDetailTypeContract;
import org.egov.egf.master.web.contract.AccountEntityContract;
import org.egov.egf.master.web.contract.BankAccountContract;
import org.egov.egf.master.web.contract.BankBranchContract;
import org.egov.egf.master.web.contract.BankContract;
import org.egov.egf.master.web.contract.BudgetGroupContract;
import org.egov.egf.master.web.contract.ChartOfAccountContract;
import org.egov.egf.master.web.contract.ChartOfAccountDetailContract;
import org.egov.egf.master.web.contract.FinancialStatusContract;
import org.egov.egf.master.web.contract.FinancialYearContract;
import org.egov.egf.master.web.contract.FiscalPeriodContract;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.contract.FunctionaryContract;
import org.egov.egf.master.web.contract.FundContract;
import org.egov.egf.master.web.contract.FundsourceContract;
import org.egov.egf.master.web.contract.SchemeContract;
import org.egov.egf.master.web.contract.SubSchemeContract;
import org.egov.egf.master.web.contract.SupplierContract;
import org.egov.egf.master.web.requests.AccountCodePurposeRequest;
import org.egov.egf.master.web.requests.AccountDetailKeyRequest;
import org.egov.egf.master.web.requests.AccountDetailTypeRequest;
import org.egov.egf.master.web.requests.AccountEntityRequest;
import org.egov.egf.master.web.requests.BankAccountRequest;
import org.egov.egf.master.web.requests.BankBranchRequest;
import org.egov.egf.master.web.requests.BankRequest;
import org.egov.egf.master.web.requests.BudgetGroupRequest;
import org.egov.egf.master.web.requests.ChartOfAccountDetailRequest;
import org.egov.egf.master.web.requests.ChartOfAccountRequest;
import org.egov.egf.master.web.requests.FinancialStatusRequest;
import org.egov.egf.master.web.requests.FinancialYearRequest;
import org.egov.egf.master.web.requests.FiscalPeriodRequest;
import org.egov.egf.master.web.requests.FunctionRequest;
import org.egov.egf.master.web.requests.FunctionaryRequest;
import org.egov.egf.master.web.requests.FundRequest;
import org.egov.egf.master.web.requests.FundsourceRequest;
import org.egov.egf.master.web.requests.SchemeRequest;
import org.egov.egf.master.web.requests.SubSchemeRequest;
import org.egov.egf.master.web.requests.SupplierRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FinancialMastersListener {

    @Value("${kafka.topics.egf.masters.completed.topic}")
    private String completedTopic;

    @Value("${kafka.topics.egf.masters.fund.completed.key}")
    private String fundCompletedKey;

    @Value("${kafka.topics.egf.masters.bank.completed.key}")
    private String bankCompletedKey;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private FinancialProducer financialProducer;

    @Autowired
    private FundService fundService;

    @Autowired
    private BankService bankService;

    @Autowired
    private FunctionService functionService;

    @Autowired
    private BankBranchService bankBranchService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private AccountCodePurposeService accountCodePurposeService;

    @Autowired
    private AccountDetailTypeService accountDetailTypeService;

    @Autowired
    private AccountDetailKeyService accountDetailKeyService;

    @Autowired
    private AccountEntityService accountEntityService;

    @Autowired
    private BudgetGroupService budgetGroupService;

    @Autowired
    private ChartOfAccountService chartOfAccountService;

    @Autowired
    private ChartOfAccountDetailService chartOfAccountDetailService;

    @Autowired
    private FinancialYearService financialYearService;

    @Autowired
    private FiscalPeriodService fiscalPeriodService;

    @Autowired
    private FunctionaryService functionaryService;

    @Autowired
    private FundsourceService fundsourceService;

    @Autowired
    private SchemeService schemeService;

    @Autowired
    private SubSchemeService subSchemeService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private FinancialStatusService financialStatusService;

    @KafkaListener(id = "${kafka.topics.egf.masters.validated.id}", topics = "${kafka.topics.egf.masters.validated.topic}", group = "${kafka.topics.egf.masters.validated.group}")
    public void process(Map<String, Object> mastersMap) {
        // implement the details here

        if (mastersMap.get("fund_create") != null) {

            FundRequest request = objectMapper.convertValue(mastersMap.get("fund_create"),
                    FundRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (FundContract fundContract : request.getFunds()) {
                Fund domain = mapper.map(fundContract, Fund.class);
                fundService.save(domain);
            }

            mastersMap.clear();
            mastersMap.put("fundcontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
        if (mastersMap.get("fund_update") != null) {

            FundRequest request = objectMapper.convertValue(mastersMap.get("fund_create"),
                    FundRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (FundContract fundContract : request.getFunds()) {
                Fund domain = mapper.map(fundContract, Fund.class);
                fundService.update(domain);
            }

            mastersMap.clear();
            mastersMap.put("fundcontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }

        if (mastersMap.get("bank_create") != null) {
            BankRequest request = objectMapper.convertValue(mastersMap.get("bank_create"), BankRequest.class);
            ModelMapper mapper = new ModelMapper();
            for (BankContract bankContract : request.getBanks()) {
                Bank domain = mapper.map(bankContract, Bank.class);
                bankService.save(domain);
            }

            mastersMap.clear();
            mastersMap.put("bank_persisted", request);
            financialProducer.sendMessage(completedTopic, bankCompletedKey, mastersMap);
        }
        if (mastersMap.get("bank_update") != null) {

            BankRequest request = objectMapper.convertValue(mastersMap.get("bank_update"), BankRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (BankContract bankContract : request.getBanks()) {
                Bank domain = mapper.map(bankContract, Bank.class);
                bankService.update(domain);
            }
            mastersMap.clear();
            mastersMap.put("bank_persisted", request);
            financialProducer.sendMessage(completedTopic, bankCompletedKey, mastersMap);
        }

        if (mastersMap.get("bank_create") != null) {

            BankRequest request = objectMapper.convertValue(mastersMap.get("bank_create"),
                    BankRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (BankContract bankContract : request.getBanks()) {
                Bank domain = mapper.map(bankContract, Bank.class);
                bankService.save(domain);
            }

            mastersMap.clear();
            mastersMap.put("bankcontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
        if (mastersMap.get("bank_update") != null)

        {

            BankRequest request = objectMapper.convertValue(mastersMap.get("bank_update"),
                    BankRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (BankContract bankContract : request.getBanks()) {
                Bank domain = mapper.map(bankContract, Bank.class);
                bankService.update(domain);
            }

            mastersMap.clear();
            mastersMap.put("bankcontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }

        if (mastersMap.get("function_create") != null) {

            FunctionRequest request = objectMapper.convertValue(
                    mastersMap.get("function_create"), FunctionRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (FunctionContract functionContract : request.getFunctions()) {
                Function domain = mapper.map(functionContract, Function.class);
                functionService.save(domain);
            }

            mastersMap.clear();
            mastersMap.put("functioncontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
        if (mastersMap.get("function_update") != null) {

            FunctionRequest request = objectMapper.convertValue(
                    mastersMap.get("function_update"), FunctionRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (FunctionContract functionContract : request.getFunctions()) {
                Function domain = mapper.map(functionContract, Function.class);
                functionService.update(domain);
            }

            mastersMap.clear();
            mastersMap.put("functioncontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }

        if (mastersMap.get("bankbranch_create") != null) {

            BankBranchRequest request = objectMapper.convertValue(
                    mastersMap.get("bankbranch_create"),
                    BankBranchRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (BankBranchContract bankBranchContract : request.getBankBranches()) {
                BankBranch domain = mapper.map(bankBranchContract, BankBranch.class);
                bankBranchService.save(domain);
            }

            mastersMap.clear();
            mastersMap.put("bankbranchcontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
        if (mastersMap.get("bankbranch_update") != null) {

            BankBranchRequest request = objectMapper.convertValue(
                    mastersMap.get("bankbranch_update"),
                    BankBranchRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (BankBranchContract bankBranchContract : request.getBankBranches()) {
                BankBranch domain = mapper.map(bankBranchContract, BankBranch.class);
                bankBranchService.update(domain);
            }

            mastersMap.clear();
            mastersMap.put("bankbranchcontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }

        if (mastersMap.get("bankaccount_create") != null) {

            BankAccountRequest request = objectMapper.convertValue(
                    mastersMap.get("bankaccount_create"),
                    BankAccountRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (BankAccountContract bankAccountContract : request.getBankAccounts()) {
                BankAccount domain = mapper.map(bankAccountContract, BankAccount.class);
                bankAccountService.save(domain);
            }

            mastersMap.clear();
            mastersMap.put("bankaccountcontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
        if (mastersMap.get("bankaccount_update") != null) {

            BankAccountRequest request = objectMapper.convertValue(
                    mastersMap.get("bankaccount_update"),
                    BankAccountRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (BankAccountContract bankAccountContract : request.getBankAccounts()) {
                BankAccount domain = mapper.map(bankAccountContract, BankAccount.class);
                bankAccountService.update(domain);
            }

            mastersMap.clear();
            mastersMap.put("bankaccountcontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
        if (mastersMap.get("accountcodepurpose_create") != null) {

            AccountCodePurposeRequest request = objectMapper.convertValue(
                    mastersMap.get("accountcodepurpose_create"),
                    AccountCodePurposeRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (AccountCodePurposeContract accountCodePurposeContract : request.getAccountCodePurposes()) {
                AccountCodePurpose domain = mapper.map(accountCodePurposeContract, AccountCodePurpose.class);
                accountCodePurposeService.save(domain);
            }

            mastersMap.clear();
            mastersMap.put("accountcodepurposecontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
        if (mastersMap.get("accountcodepurpose_update") != null) {

            AccountCodePurposeRequest request = objectMapper.convertValue(
                    mastersMap.get("accountcodepurpose_update"),
                    AccountCodePurposeRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (AccountCodePurposeContract accountCodePurposeContract : request.getAccountCodePurposes()) {
                AccountCodePurpose domain = mapper.map(accountCodePurposeContract, AccountCodePurpose.class);
                accountCodePurposeService.update(domain);
            }

            mastersMap.clear();
            mastersMap.put("accountcodepurposecontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
        if (mastersMap.get("accountdetailtype_create") != null) {

            AccountDetailTypeRequest request = objectMapper.convertValue(
                    mastersMap.get("accountdetailtype_create"),
                    AccountDetailTypeRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (AccountDetailTypeContract accountDetailTypeContract : request.getAccountDetailTypes()) {
                AccountDetailType domain = mapper.map(accountDetailTypeContract, AccountDetailType.class);
                accountDetailTypeService.save(domain);
            }

            mastersMap.clear();
            mastersMap.put("accountdetailtypecontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
        if (mastersMap.get("accountdetailtype_update") != null) {

            AccountDetailTypeRequest request = objectMapper.convertValue(
                    mastersMap.get("accountdetailtype_update"),
                    AccountDetailTypeRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (AccountDetailTypeContract accountDetailTypeContract : request.getAccountDetailTypes()) {
                AccountDetailType domain = mapper.map(accountDetailTypeContract, AccountDetailType.class);
                accountDetailTypeService.update(domain);
            }

            mastersMap.clear();
            mastersMap.put("accountdetailtypecontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
        if (mastersMap.get("accountdetailkey_create") != null) {

            AccountDetailKeyRequest request = objectMapper.convertValue(
                    mastersMap.get("accountdetailkey_create"),
                    AccountDetailKeyRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (AccountDetailKeyContract accountDetailKeyContract : request.getAccountDetailKeys()) {
                AccountDetailKey domain = mapper.map(accountDetailKeyContract, AccountDetailKey.class);
                accountDetailKeyService.save(domain);
            }

            mastersMap.clear();
            mastersMap.put("accountdetailkeycontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
        if (mastersMap.get("accountdetailkey_update") != null) {

            AccountDetailKeyRequest request = objectMapper.convertValue(
                    mastersMap.get("accountdetailkey_update"),
                    AccountDetailKeyRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (AccountDetailKeyContract accountDetailKeyContract : request.getAccountDetailKeys()) {
                AccountDetailKey domain = mapper.map(accountDetailKeyContract, AccountDetailKey.class);
                accountDetailKeyService.update(domain);
            }

            mastersMap.clear();
            mastersMap.put("accountdetailkeycontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
        if (mastersMap.get("accountentity_create") != null) {

            AccountEntityRequest request = objectMapper.convertValue(
                    mastersMap.get("accountentity_create"),
                    AccountEntityRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (AccountEntityContract accountEntityContract : request.getAccountEntities()) {
                AccountEntity domain = mapper.map(accountEntityContract, AccountEntity.class);
                accountEntityService.save(domain);
            }

            mastersMap.clear();
            mastersMap.put("accountentitycontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
        if (mastersMap.get("accountentity_update") != null) {

            AccountEntityRequest request = objectMapper.convertValue(
                    mastersMap.get("accountentity_update"),
                    AccountEntityRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (AccountEntityContract accountEntityContract : request.getAccountEntities()) {
                AccountEntity domain = mapper.map(accountEntityContract, AccountEntity.class);
                accountEntityService.update(domain);
            }

            mastersMap.clear();
            mastersMap.put("accountentitycontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
        if (mastersMap.get("budgetgroup_create") != null) {

            BudgetGroupRequest request = objectMapper.convertValue(
                    mastersMap.get("budgetgroup_create"),
                    BudgetGroupRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (BudgetGroupContract budgetGroupContract : request.getBudgetGroups()) {
                BudgetGroup domain = mapper.map(budgetGroupContract, BudgetGroup.class);
                budgetGroupService.save(domain);
            }

            mastersMap.clear();
            mastersMap.put("budgetgroupcontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
        if (mastersMap.get("budgetgroup_update") != null) {

            BudgetGroupRequest request = objectMapper.convertValue(
                    mastersMap.get("budgetgroup_update"),
                    BudgetGroupRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (BudgetGroupContract budgetGroupContract : request.getBudgetGroups()) {
                BudgetGroup domain = mapper.map(budgetGroupContract, BudgetGroup.class);
                budgetGroupService.update(domain);
            }

            mastersMap.clear();
            mastersMap.put("budgetgroupcontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }

        if (mastersMap.get("chartofaccount_create") != null) {

            ChartOfAccountRequest request = objectMapper.convertValue(
                    mastersMap.get("chartofaccount_create"),
                    ChartOfAccountRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (ChartOfAccountContract chartOfAccountContract : request.getChartOfAccounts()) {
                ChartOfAccount domain = mapper.map(chartOfAccountContract, ChartOfAccount.class);
                chartOfAccountService.save(domain);
            }

            mastersMap.clear();
            mastersMap.put("chartofaccountcontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
        if (mastersMap.get("chartofaccount_update") != null) {

            ChartOfAccountRequest request = objectMapper.convertValue(
                    mastersMap.get("chartofaccount_update"),
                    ChartOfAccountRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (ChartOfAccountContract chartOfAccountContract : request.getChartOfAccounts()) {
                ChartOfAccount domain = mapper.map(chartOfAccountContract, ChartOfAccount.class);
                chartOfAccountService.update(domain);
            }

            mastersMap.clear();
            mastersMap.put("chartofaccountcontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }

        if (mastersMap.get("chartofaccountdetail_create") != null) {

            ChartOfAccountDetailRequest request = objectMapper.convertValue(
                    mastersMap.get("chartofaccountdetail_create"),
                    ChartOfAccountDetailRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (ChartOfAccountDetailContract chartOfAccountDetailContract : request.getChartOfAccountDetails()) {
                ChartOfAccountDetail domain = mapper.map(chartOfAccountDetailContract, ChartOfAccountDetail.class);
                chartOfAccountDetailService.save(domain);
            }

            mastersMap.clear();
            mastersMap.put("chartofaccountdetailcontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
        if (mastersMap.get("chartofaccountdetail_update") != null) {

            ChartOfAccountDetailRequest request = objectMapper.convertValue(
                    mastersMap.get("chartofaccountdetail_update"),
                    ChartOfAccountDetailRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (ChartOfAccountDetailContract chartOfAccountDetailContract : request.getChartOfAccountDetails()) {
                ChartOfAccountDetail domain = mapper.map(chartOfAccountDetailContract, ChartOfAccountDetail.class);
                chartOfAccountDetailService.update(domain);
            }

            mastersMap.clear();
            mastersMap.put("chartofaccountdetailcontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }

        if (mastersMap.get("financialyear_create") != null) {

            FinancialYearRequest request = objectMapper.convertValue(
                    mastersMap.get("financialyear_create"),
                    FinancialYearRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (FinancialYearContract financialYearContract : request.getFinancialYears()) {
                FinancialYear domain = mapper.map(financialYearContract, FinancialYear.class);
                financialYearService.save(domain);
            }

            mastersMap.clear();
            mastersMap.put("financialyearcontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
        if (mastersMap.get("financialyear_update") != null) {

            FinancialYearRequest request = objectMapper.convertValue(
                    mastersMap.get("financialyear_update"),
                    FinancialYearRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (FinancialYearContract financialYearContract : request.getFinancialYears()) {
                FinancialYear domain = mapper.map(financialYearContract, FinancialYear.class);
                financialYearService.update(domain);
            }

            mastersMap.clear();
            mastersMap.put("financialyearcontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }

        if (mastersMap.get("fiscalperiod_create") != null) {

            FiscalPeriodRequest request = objectMapper.convertValue(
                    mastersMap.get("fiscalperiod_create"),
                    FiscalPeriodRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (FiscalPeriodContract fiscalPeriodContract : request.getFiscalPeriods()) {
                FiscalPeriod domain = mapper.map(fiscalPeriodContract, FiscalPeriod.class);
                fiscalPeriodService.save(domain);
            }

            mastersMap.clear();
            mastersMap.put("fiscalperiodcontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
        if (mastersMap.get("fiscalperiod_update") != null) {

            FiscalPeriodRequest request = objectMapper.convertValue(
                    mastersMap.get("fiscalperiod_update"),
                    FiscalPeriodRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (FiscalPeriodContract fiscalPeriodContract : request.getFiscalPeriods()) {
                FiscalPeriod domain = mapper.map(fiscalPeriodContract, FiscalPeriod.class);
                fiscalPeriodService.update(domain);
            }

            mastersMap.clear();
            mastersMap.put("fiscalperiodcontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }

        if (mastersMap.get("functionary_create") != null) {

            FunctionaryRequest request = objectMapper.convertValue(
                    mastersMap.get("functionary_create"),
                    FunctionaryRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (FunctionaryContract functionaryContract : request.getFunctionaries()) {
                Functionary domain = mapper.map(functionaryContract, Functionary.class);
                functionaryService.save(domain);
            }

            mastersMap.clear();
            mastersMap.put("functionarycontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
        if (mastersMap.get("functionary_update") != null) {

            FunctionaryRequest request = objectMapper.convertValue(
                    mastersMap.get("functionary_update"),
                    FunctionaryRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (FunctionaryContract functionaryContract : request.getFunctionaries()) {
                Functionary domain = mapper.map(functionaryContract, Functionary.class);
                functionaryService.update(domain);
            }

            mastersMap.clear();
            mastersMap.put("functionarycontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }

        if (mastersMap.get("fundsource_create") != null) {

            FundsourceRequest request = objectMapper.convertValue(
                    mastersMap.get("fundsource_create"),
                    FundsourceRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (FundsourceContract fundsourceContract : request.getFundsources()) {
                Fundsource domain = mapper.map(fundsourceContract, Fundsource.class);
                fundsourceService.save(domain);
            }

            mastersMap.clear();
            mastersMap.put("fundsourcecontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
        if (mastersMap.get("fundsource_update") != null) {

            FundsourceRequest request = objectMapper.convertValue(
                    mastersMap.get("fundsource_update"),
                    FundsourceRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (FundsourceContract fundsourceContract : request.getFundsources()) {
                Fundsource domain = mapper.map(fundsourceContract, Fundsource.class);
                fundsourceService.update(domain);
            }

            mastersMap.clear();
            mastersMap.put("fundsourcecontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }

        if (mastersMap.get("scheme_create") != null) {

            SchemeRequest request = objectMapper.convertValue(mastersMap.get("scheme_create"),
                    SchemeRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (SchemeContract schemeContract : request.getSchemes()) {
                Scheme domain = mapper.map(schemeContract, Scheme.class);
                schemeService.save(domain);
            }

            mastersMap.clear();
            mastersMap.put("schemecontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
        if (mastersMap.get("scheme_update") != null) {

            SchemeRequest request = objectMapper.convertValue(mastersMap.get("scheme_update"),
                    SchemeRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (SchemeContract schemeContract : request.getSchemes()) {
                Scheme domain = mapper.map(schemeContract, Scheme.class);
                schemeService.update(domain);
            }

            mastersMap.clear();
            mastersMap.put("schemecontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }

        if (mastersMap.get("subscheme_create") != null) {

            SubSchemeRequest request = objectMapper.convertValue(
                    mastersMap.get("subscheme_create"), SubSchemeRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (SubSchemeContract subSchemeContract : request.getSubSchemes()) {
                SubScheme domain = mapper.map(subSchemeContract, SubScheme.class);
                subSchemeService.save(domain);
            }

            mastersMap.clear();
            mastersMap.put("subschemecontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
        if (mastersMap.get("subscheme_update") != null) {

            SubSchemeRequest request = objectMapper.convertValue(
                    mastersMap.get("subscheme_update"), SubSchemeRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (SubSchemeContract subSchemeContract : request.getSubSchemes()) {
                SubScheme domain = mapper.map(subSchemeContract, SubScheme.class);
                subSchemeService.update(domain);
            }

            mastersMap.clear();
            mastersMap.put("subsubschemecontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
        if (mastersMap.get("supplier_create") != null) {

            SupplierRequest request = objectMapper.convertValue(
                    mastersMap.get("supplier_create"), SupplierRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (SupplierContract supplierContract : request.getSuppliers()) {
                Supplier domain = mapper.map(supplierContract, Supplier.class);
                supplierService.save(domain);
            }

            mastersMap.clear();
            mastersMap.put("suppliercontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
        if (mastersMap.get("supplier_update") != null) {

            SupplierRequest request = objectMapper.convertValue(
                    mastersMap.get("supplier_update"), SupplierRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (SupplierContract supplierContract : request.getSuppliers()) {
                Supplier domain = mapper.map(supplierContract, Supplier.class);
                supplierService.update(domain);
            }

            mastersMap.clear();
            mastersMap.put("suppliercontract_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }

        if (mastersMap.get("financialstatus_create") != null) {

            FinancialStatusRequest request = objectMapper.convertValue(
                    mastersMap.get("financialstatus_create"), FinancialStatusRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (FinancialStatusContract egfStatusContract : request.getFinancialStatuses()) {
                FinancialStatus domain = mapper.map(egfStatusContract, FinancialStatus.class);
                financialStatusService.save(domain);
            }

            mastersMap.clear();
            mastersMap.put("financialstatus_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
        if (mastersMap.get("financialstatus_update") != null) {

            FinancialStatusRequest request = objectMapper.convertValue(
                    mastersMap.get("financialstatus_update"), FinancialStatusRequest.class);

            ModelMapper mapper = new ModelMapper();
            for (FinancialStatusContract egfStatusContract : request.getFinancialStatuses()) {
                FinancialStatus domain = mapper.map(egfStatusContract, FinancialStatus.class);
                financialStatusService.update(domain);
            }

            mastersMap.clear();
            mastersMap.put("financialstatus_persisted", request);
            financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
        }
    }

}
