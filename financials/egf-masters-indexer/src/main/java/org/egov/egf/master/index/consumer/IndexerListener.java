package org.egov.egf.master.index.consumer;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.egov.egf.master.index.persistence.repository.ElasticSearchRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class IndexerListener {

    private static final String BANK_OBJECT_TYPE = "bank";
    private static final String BANKBRANCH_OBJECT_TYPE = "bankbranch";
    private static final String BANKACCOUNT_OBJECT_TYPE = "bankaccount";
    private static final String ACCOUNTCODEPURPOSE_OBJECT_TYPE = "accountcodepurpose";
    private static final String ACCOUNTDETAILKEY_OBJECT_TYPE = "accountdetailkey";
    private static final String ACCOUNTDETAILTYPE_OBJECT_TYPE = "accountdetailtype";
    private static final String ACCOUNTENTITY_OBJECT_TYPE = "accountentity";
    private static final String BUDGETGROUP_OBJECT_TYPE = "budgetgroup";
    private static final String CHARTOFACCOUNT_OBJECT_TYPE = "chartofaccount";
    private static final String CHARTOFACCOUNTDETAIL_OBJECT_TYPE = "chartofaccountdetail";
    private static final String FINANCIALYEAR_OBJECT_TYPE = "financialyear";
    private static final String FISCALPERIOD_OBJECT_TYPE = "fiscalperiod";
    private static final String FUNCTIONARY_OBJECT_TYPE = "functionary";
    private static final String FUNCTION_OBJECT_TYPE = "function";
    private static final String FUND_OBJECT_TYPE = "fund";
    private static final String FUNDSOURCE_OBJECT_TYPE = "fundsource";
    private static final String SCHEME_OBJECT_TYPE = "scheme";
    private static final String SUBSCHEME_OBJECT_TYPE = "subscheme";
    private static final String SUPPLIER_OBJECT_TYPE = "supplier";
    private static final String FINANCIALSTATUS_OBJECT_TYPE = "financialstatus";

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Autowired
    private ElasticSearchRepository elasticSearchRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(id = "${kafka.topics.egov.index.id}", topics = "${kafka.topics.egov.index.name}", group = "${kafka.topics.egov.index.group}")
    public void listen(final HashMap<String, Object> financialContractRequestMap) {

        if (financialContractRequestMap.get("fundcontract_persisted") != null) {

            final FundRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("fundcontract_persisted"),
                    FundRequest.class);

            if (request.getFunds() != null && !request.getFunds().isEmpty())
                for (final FundContract fundContract : request.getFunds()) {
                    final HashMap<String, Object> indexObj = getFundIndexObject(fundContract);
                    elasticSearchRepository.index(FUND_OBJECT_TYPE,
                            fundContract.getTenantId() + "-" + fundContract.getName(), indexObj);
                }
        }

        if (financialContractRequestMap.get("bankcontract_persisted") != null) {

            final BankRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("bankcontract_persisted"),
                    BankRequest.class);

            if (request.getBanks() != null && !request.getBanks().isEmpty())
                for (final BankContract bankContract : request.getBanks()) {
                    final HashMap<String, Object> indexObj = getBankIndexObject(bankContract);
                    elasticSearchRepository.index(BANK_OBJECT_TYPE,
                            bankContract.getTenantId() + "-" + bankContract.getName(), indexObj);
                }
        }

        if (financialContractRequestMap.get("functioncontract_persisted") != null) {

            final FunctionRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("functioncontract_persisted"),
                    FunctionRequest.class);

            if (request.getFunctions() != null && !request.getFunctions().isEmpty())
                for (final FunctionContract functionContract : request.getFunctions()) {
                    final HashMap<String, Object> indexObj = getFunctionIndexObject(functionContract);
                    elasticSearchRepository.index(FUNCTION_OBJECT_TYPE,
                            functionContract.getTenantId() + "-" + functionContract.getName(), indexObj);
                }
        }

        if (financialContractRequestMap.get("bankBranchcontract_persisted") != null) {

            final BankBranchRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("bankBranchcontract_persisted"),
                    BankBranchRequest.class);

            if (request.getBankBranches() != null && !request.getBankBranches().isEmpty())
                for (final BankBranchContract bankBranchContract : request.getBankBranches()) {
                    final HashMap<String, Object> indexObj = getBankBranchIndexObject(bankBranchContract);
                    elasticSearchRepository.index(BANKBRANCH_OBJECT_TYPE,
                            bankBranchContract.getTenantId() + "-" + bankBranchContract.getName(), indexObj);
                }
        }

        if (financialContractRequestMap.get("bankaccountcontract_persisted") != null) {

            final BankAccountRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("bankaccountcontract_persisted"),
                    BankAccountRequest.class);

            if (request.getBankAccounts() != null && !request.getBankAccounts().isEmpty())
                for (final BankAccountContract bankAccountContract : request.getBankAccounts()) {
                    final HashMap<String, Object> indexObj = getBankAccountIndexObject(bankAccountContract);
                    elasticSearchRepository.index(BANKACCOUNT_OBJECT_TYPE,
                            bankAccountContract.getTenantId() + "-" + bankAccountContract.getAccountNumber(),
                            indexObj);
                }
        }

        if (financialContractRequestMap.get("accountcodepurposecontract_persisted") != null) {

            final AccountCodePurposeRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("accountcodepurposecontract_persisted"),
                    AccountCodePurposeRequest.class);

            if (request.getAccountCodePurposes() != null && !request.getAccountCodePurposes().isEmpty())
                for (final AccountCodePurposeContract accountCodePurposeContract : request.getAccountCodePurposes()) {
                    final HashMap<String, Object> indexObj = getAccountCodePurposeContractIndexObject(accountCodePurposeContract);
                    elasticSearchRepository.index(ACCOUNTCODEPURPOSE_OBJECT_TYPE,
                            accountCodePurposeContract.getTenantId() + "-" + accountCodePurposeContract.getName(),
                            indexObj);
                }
        }

        if (financialContractRequestMap.get("accountdetailtypecontract_persisted") != null) {

            final AccountDetailTypeRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("accountdetailtypecontract_persisted"),
                    AccountDetailTypeRequest.class);

            if (request.getAccountDetailTypes() != null && !request.getAccountDetailTypes().isEmpty())
                for (final AccountDetailTypeContract accountDetailTypeContract : request.getAccountDetailTypes()) {
                    final HashMap<String, Object> indexObj = getAccountDetailTypeContractIndexObject(accountDetailTypeContract);
                    elasticSearchRepository.index(ACCOUNTDETAILTYPE_OBJECT_TYPE,
                            accountDetailTypeContract.getTenantId() + "-" + accountDetailTypeContract.getName(),
                            indexObj);
                }
        }

        if (financialContractRequestMap.get("accountdetailkeycontract_persisted") != null) {

            final AccountDetailKeyRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("accountdetailkeycontract_persisted"),
                    AccountDetailKeyRequest.class);

            if (request.getAccountDetailKeys() != null && !request.getAccountDetailKeys().isEmpty())
                for (final AccountDetailKeyContract accountDetailKeyContract : request.getAccountDetailKeys()) {
                    final HashMap<String, Object> indexObj = getAccountDetailKeyContractIndexObject(accountDetailKeyContract);
                    elasticSearchRepository.index(ACCOUNTDETAILKEY_OBJECT_TYPE,
                            accountDetailKeyContract.getTenantId() + "-" + accountDetailKeyContract.getKey(),
                            indexObj);
                }
        }

        if (financialContractRequestMap.get("accountentitycontract_persisted") != null) {

            final AccountEntityRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("accountentitycontract_persisted"),
                    AccountEntityRequest.class);

            if (request.getAccountEntities() != null && !request.getAccountEntities().isEmpty())
                for (final AccountEntityContract accountEntityContract : request.getAccountEntities()) {
                    final HashMap<String, Object> indexObj = getAccountEntityContractIndexObject(accountEntityContract);
                    elasticSearchRepository.index(ACCOUNTENTITY_OBJECT_TYPE,
                            accountEntityContract.getTenantId() + "-" + accountEntityContract.getName(),
                            indexObj);
                }
        }

        if (financialContractRequestMap.get("budgetgroupcontract_persisted") != null) {

            final BudgetGroupRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("budgetgroupcontract_persisted"),
                    BudgetGroupRequest.class);

            if (request.getBudgetGroups() != null && !request.getBudgetGroups().isEmpty())
                for (final BudgetGroupContract budgetGroupContract : request.getBudgetGroups()) {
                    final HashMap<String, Object> indexObj = getBudgetGroupContractIndexObject(budgetGroupContract);
                    elasticSearchRepository.index(BUDGETGROUP_OBJECT_TYPE,
                            budgetGroupContract.getTenantId() + "-" + budgetGroupContract.getName(),
                            indexObj);
                }
        }

        if (financialContractRequestMap.get("chartofaccountcontract_persisted") != null) {

            final ChartOfAccountRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("chartofaccountcontract_persisted"),
                    ChartOfAccountRequest.class);

            if (request.getChartOfAccounts() != null && !request.getChartOfAccounts().isEmpty())
                for (final ChartOfAccountContract chartOfAccountContract : request.getChartOfAccounts()) {
                    final HashMap<String, Object> indexObj = getChartOfAccountContractIndexObject(chartOfAccountContract);
                    elasticSearchRepository.index(CHARTOFACCOUNT_OBJECT_TYPE,
                            chartOfAccountContract.getTenantId() + "-" + chartOfAccountContract.getGlcode(),
                            indexObj);
                }
        }

        if (financialContractRequestMap.get("chartofaccountdetailcontract_persisted") != null) {

            final ChartOfAccountDetailRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("chartofaccountdetailcontract_persisted"),
                    ChartOfAccountDetailRequest.class);

            if (request.getChartOfAccountDetails() != null && !request.getChartOfAccountDetails().isEmpty())
                for (final ChartOfAccountDetailContract chartOfAccountDetailContract : request.getChartOfAccountDetails()) {
                    final HashMap<String, Object> indexObj = getChartOfAccountDetailContractIndexObject(
                            chartOfAccountDetailContract);
                    elasticSearchRepository.index(CHARTOFACCOUNTDETAIL_OBJECT_TYPE,
                            chartOfAccountDetailContract.getTenantId() + "-"
                                    + chartOfAccountDetailContract.getChartOfAccount().getGlcode(),
                            indexObj);
                }
        }

        if (financialContractRequestMap.get("financialyearcontract_persisted") != null) {

            final FinancialYearRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("financialyearcontract_persisted"),
                    FinancialYearRequest.class);

            if (request.getFinancialYears() != null && !request.getFinancialYears().isEmpty())
                for (final FinancialYearContract financialYearContract : request.getFinancialYears()) {
                    final HashMap<String, Object> indexObj = getFinancialYearContractIndexObject(financialYearContract);
                    elasticSearchRepository.index(FINANCIALYEAR_OBJECT_TYPE,
                            financialYearContract.getTenantId() + "-" + financialYearContract.getFinYearRange(),
                            indexObj);
                }
        }

        if (financialContractRequestMap.get("fiscalperiodcontract_persisted") != null) {

            final FiscalPeriodRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("fiscalperiodcontract_persisted"),
                    FiscalPeriodRequest.class);

            if (request.getFiscalPeriods() != null && !request.getFiscalPeriods().isEmpty())
                for (final FiscalPeriodContract fiscalPeriodContract : request.getFiscalPeriods()) {
                    final HashMap<String, Object> indexObj = getFiscalPeriodContractIndexObject(fiscalPeriodContract);
                    elasticSearchRepository.index(FISCALPERIOD_OBJECT_TYPE,
                            fiscalPeriodContract.getTenantId() + "-" + fiscalPeriodContract.getName(),
                            indexObj);
                }
        }

        if (financialContractRequestMap.get("functionarycontract_persisted") != null) {

            final FunctionaryRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("functionarycontract_persisted"),
                    FunctionaryRequest.class);

            if (request.getFunctionaries() != null && !request.getFunctionaries().isEmpty())
                for (final FunctionaryContract functionaryContract : request.getFunctionaries()) {
                    final HashMap<String, Object> indexObj = getFunctionaryContractIndexObject(functionaryContract);
                    elasticSearchRepository.index(FUNCTIONARY_OBJECT_TYPE,
                            functionaryContract.getTenantId() + "-" + functionaryContract.getName(),
                            indexObj);
                }
        }

        if (financialContractRequestMap.get("fundsourcecontract_persisted") != null) {

            final FundsourceRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("fundsourcecontract_persisted"),
                    FundsourceRequest.class);

            if (request.getFundsources() != null && !request.getFundsources().isEmpty())
                for (final FundsourceContract fundsourceContract : request.getFundsources()) {
                    final HashMap<String, Object> indexObj = getFundsourceContractIndexObject(fundsourceContract);
                    elasticSearchRepository.index(FUNDSOURCE_OBJECT_TYPE,
                            fundsourceContract.getTenantId() + "-" + fundsourceContract.getName(), indexObj);
                }
        }

        if (financialContractRequestMap.get("schemecontract_persisted") != null) {

            final SchemeRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("schemecontract_persisted"),
                    SchemeRequest.class);

            if (request.getSchemes() != null && !request.getSchemes().isEmpty())
                for (final SchemeContract schemeContract : request.getSchemes()) {
                    final HashMap<String, Object> indexObj = getSchemeContractIndexObject(schemeContract);
                    elasticSearchRepository.index(SCHEME_OBJECT_TYPE,
                            schemeContract.getTenantId() + "-" + schemeContract.getName(), indexObj);
                }
        }

        if (financialContractRequestMap.get("subschemecontract_persisted") != null) {

            final SubSchemeRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("subschemecontract_persisted"),
                    SubSchemeRequest.class);

            if (request.getSubSchemes() != null && !request.getSubSchemes().isEmpty())
                for (final SubSchemeContract subSchemeContract : request.getSubSchemes()) {
                    final HashMap<String, Object> indexObj = getSubSchemeContractIndexObject(subSchemeContract);
                    elasticSearchRepository.index(SUBSCHEME_OBJECT_TYPE,
                            subSchemeContract.getTenantId() + "-" + subSchemeContract.getName(), indexObj);
                }
        }

        if (financialContractRequestMap.get("suppliercontract_persisted") != null) {

            final SupplierRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("suppliercontract_persisted"),
                    SupplierRequest.class);

            if (request.getSuppliers() != null && !request.getSuppliers().isEmpty())
                for (final SupplierContract supplierContract : request.getSuppliers()) {
                    final HashMap<String, Object> indexObj = getSupplierContractIndexObject(supplierContract);
                    elasticSearchRepository.index(SUPPLIER_OBJECT_TYPE,
                            supplierContract.getTenantId() + "-" + supplierContract.getName(), indexObj);
                }
        }

        if (financialContractRequestMap.get("financialstatus_persisted") != null) {

            final FinancialStatusRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("financialstatus_persisted"),
                    FinancialStatusRequest.class);

            if (request.getFinancialStatuses() != null && !request.getFinancialStatuses().isEmpty())
                for (final FinancialStatusContract financialStatusContract : request.getFinancialStatuses()) {
                    final HashMap<String, Object> indexObj = getFinancialStatusContractIndexObject(financialStatusContract);
                    elasticSearchRepository.index(FINANCIALSTATUS_OBJECT_TYPE,
                            financialStatusContract.getTenantId() + "-" + financialStatusContract.getCode(), indexObj);
                }
        }

    }

    private HashMap<String, Object> getFundIndexObject(final FundContract fundContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", fundContract.getId());
        indexObj.put("name", fundContract.getName());
        indexObj.put("code", fundContract.getCode());
        indexObj.put("active", fundContract.getActive());
        indexObj.put("createdby", fundContract.getCreatedBy());
        indexObj.put("identifier", fundContract.getIdentifier());
        indexObj.put("isparent", fundContract.getIsParent());
        indexObj.put("lastmodifiedby", fundContract.getLastModifiedBy());
        indexObj.put("level", fundContract.getLevel());
        indexObj.put("parentid", fundContract.getParent());
        indexObj.put("tenantid", fundContract.getTenantId());

        if (fundContract.getCreatedDate() != null)
            indexObj.put("createddate", formatter.format(fundContract.getCreatedDate()));
        else
            indexObj.put("createddate", null);
        if (fundContract.getLastModifiedDate() != null)
            indexObj.put("lastmodifieddate", formatter.format(fundContract.getLastModifiedDate()));
        else
            indexObj.put("lastmodifieddate", null);

        return indexObj;
    }

    private HashMap<String, Object> getBankIndexObject(final BankContract bankContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", bankContract.getId());
        indexObj.put("name", bankContract.getName());
        indexObj.put("code", bankContract.getCode());
        indexObj.put("active", bankContract.getActive());
        indexObj.put("description", bankContract.getActive());
        indexObj.put("createdby", bankContract.getCreatedBy());
        indexObj.put("fund", bankContract.getFund());
        indexObj.put("type", bankContract.getType());
        indexObj.put("lastmodifiedby", bankContract.getLastModifiedBy());
        indexObj.put("tenantid", bankContract.getTenantId());

        if (bankContract.getCreatedDate() != null)
            indexObj.put("createddate", formatter.format(bankContract.getCreatedDate()));
        else
            indexObj.put("createddate", null);
        if (bankContract.getLastModifiedDate() != null)
            indexObj.put("lastmodifieddate", formatter.format(bankContract.getLastModifiedDate()));
        else
            indexObj.put("lastmodifieddate", null);

        return indexObj;
    }

    private HashMap<String, Object> getFunctionIndexObject(final FunctionContract functionContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", functionContract.getId());
        indexObj.put("name", functionContract.getName());
        indexObj.put("code", functionContract.getCode());
        indexObj.put("active", functionContract.getActive());
        indexObj.put("level", functionContract.getLevel());
        indexObj.put("createdby", functionContract.getCreatedBy());
        indexObj.put("parentid", functionContract.getParentId());
        indexObj.put("isparent", functionContract.getIsParent());
        indexObj.put("lastmodifiedby", functionContract.getLastModifiedBy());
        indexObj.put("tenantid", functionContract.getTenantId());

        if (functionContract.getCreatedDate() != null)
            indexObj.put("createddate", formatter.format(functionContract.getCreatedDate()));
        else
            indexObj.put("createddate", null);
        if (functionContract.getLastModifiedDate() != null)
            indexObj.put("lastmodifieddate", formatter.format(functionContract.getLastModifiedDate()));
        else
            indexObj.put("lastmodifieddate", null);

        return indexObj;
    }

    private HashMap<String, Object> getBankBranchIndexObject(final BankBranchContract bankBranchContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", bankBranchContract.getId());
        indexObj.put("name", bankBranchContract.getName());
        indexObj.put("code", bankBranchContract.getCode());
        indexObj.put("bank", bankBranchContract.getBank());
        indexObj.put("address", bankBranchContract.getAddress());
        indexObj.put("address2", bankBranchContract.getAddress2());
        indexObj.put("city", bankBranchContract.getCity());
        indexObj.put("state", bankBranchContract.getState());
        indexObj.put("pincode", bankBranchContract.getPincode());
        indexObj.put("micr", bankBranchContract.getMicr());
        indexObj.put("phone", bankBranchContract.getPhone());
        indexObj.put("fax", bankBranchContract.getFax());
        indexObj.put("contactperson", bankBranchContract.getContactPerson());
        indexObj.put("active", bankBranchContract.getActive());
        indexObj.put("description", bankBranchContract.getDescription());
        indexObj.put("createdby", bankBranchContract.getCreatedBy());
        indexObj.put("lastmodifiedby", bankBranchContract.getLastModifiedBy());
        indexObj.put("tenantid", bankBranchContract.getTenantId());

        if (bankBranchContract.getCreatedDate() != null)
            indexObj.put("createddate", formatter.format(bankBranchContract.getCreatedDate()));
        else
            indexObj.put("createddate", null);
        if (bankBranchContract.getLastModifiedDate() != null)
            indexObj.put("lastmodifieddate", formatter.format(bankBranchContract.getLastModifiedDate()));
        else
            indexObj.put("lastmodifieddate", null);

        return indexObj;
    }

    private HashMap<String, Object> getAccountCodePurposeContractIndexObject(
            final AccountCodePurposeContract accountCodePurposeContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", accountCodePurposeContract.getId());
        indexObj.put("name", accountCodePurposeContract.getName());
        indexObj.put("createdby", accountCodePurposeContract.getCreatedBy());
        indexObj.put("lastmodifiedby", accountCodePurposeContract.getLastModifiedBy());
        indexObj.put("tenantid", accountCodePurposeContract.getTenantId());

        if (accountCodePurposeContract.getCreatedDate() != null)
            indexObj.put("createddate", formatter.format(accountCodePurposeContract.getCreatedDate()));
        else
            indexObj.put("createddate", null);
        if (accountCodePurposeContract.getLastModifiedDate() != null)
            indexObj.put("lastmodifieddate", formatter.format(accountCodePurposeContract.getLastModifiedDate()));
        else
            indexObj.put("lastmodifieddate", null);

        return indexObj;
    }

    private HashMap<String, Object> getAccountDetailTypeContractIndexObject(
            final AccountDetailTypeContract accountDetailTypeContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", accountDetailTypeContract.getId());
        indexObj.put("name", accountDetailTypeContract.getName());
        indexObj.put("description", accountDetailTypeContract.getDescription());
        indexObj.put("tablename", accountDetailTypeContract.getTableName());
        indexObj.put("active", accountDetailTypeContract.getActive());
        indexObj.put("fullyqualifiedname", accountDetailTypeContract.getFullyQualifiedName());
        indexObj.put("createdby", accountDetailTypeContract.getCreatedBy());
        indexObj.put("lastmodifiedby", accountDetailTypeContract.getLastModifiedBy());
        indexObj.put("tenantid", accountDetailTypeContract.getTenantId());

        if (accountDetailTypeContract.getCreatedDate() != null)
            indexObj.put("createddate", formatter.format(accountDetailTypeContract.getCreatedDate()));
        else
            indexObj.put("createddate", null);
        if (accountDetailTypeContract.getLastModifiedDate() != null)
            indexObj.put("lastmodifieddate", formatter.format(accountDetailTypeContract.getLastModifiedDate()));
        else
            indexObj.put("lastmodifieddate", null);

        return indexObj;
    }

    private HashMap<String, Object> getAccountDetailKeyContractIndexObject(
            final AccountDetailKeyContract accountDetailKeyContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", accountDetailKeyContract.getId());
        indexObj.put("key", accountDetailKeyContract.getKey());
        indexObj.put("accountdetailtype", accountDetailKeyContract.getAccountDetailType());
        indexObj.put("createdby", accountDetailKeyContract.getCreatedBy());
        indexObj.put("lastmodifiedby", accountDetailKeyContract.getLastModifiedBy());
        indexObj.put("tenantid", accountDetailKeyContract.getTenantId());

        if (accountDetailKeyContract.getCreatedDate() != null)
            indexObj.put("createddate", formatter.format(accountDetailKeyContract.getCreatedDate()));
        else
            indexObj.put("createddate", null);
        if (accountDetailKeyContract.getLastModifiedDate() != null)
            indexObj.put("lastmodifieddate", formatter.format(accountDetailKeyContract.getLastModifiedDate()));
        else
            indexObj.put("lastmodifieddate", null);

        return indexObj;
    }

    private HashMap<String, Object> getAccountEntityContractIndexObject(final AccountEntityContract accountEntityContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", accountEntityContract.getId());
        indexObj.put("code", accountEntityContract.getCode());
        indexObj.put("name", accountEntityContract.getName());
        indexObj.put("description", accountEntityContract.getDescription());
        indexObj.put("accountdetailtype", accountEntityContract.getAccountDetailType());
        indexObj.put("active", accountEntityContract.getActive());
        indexObj.put("createdby", accountEntityContract.getCreatedBy());
        indexObj.put("lastmodifiedby", accountEntityContract.getLastModifiedBy());
        indexObj.put("tenantid", accountEntityContract.getTenantId());

        if (accountEntityContract.getCreatedDate() != null)
            indexObj.put("createddate", formatter.format(accountEntityContract.getCreatedDate()));
        else
            indexObj.put("createddate", null);
        if (accountEntityContract.getLastModifiedDate() != null)
            indexObj.put("lastmodifieddate", formatter.format(accountEntityContract.getLastModifiedDate()));
        else
            indexObj.put("lastmodifieddate", null);

        return indexObj;
    }

    private HashMap<String, Object> getBudgetGroupContractIndexObject(final BudgetGroupContract budgetGroupContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", budgetGroupContract.getId());
        indexObj.put("name", budgetGroupContract.getName());
        indexObj.put("description", budgetGroupContract.getDescription());
        indexObj.put("majorcode", budgetGroupContract.getMajorCode());
        indexObj.put("maxcode", budgetGroupContract.getMaxCode());
        indexObj.put("mincode", budgetGroupContract.getMinCode());
        indexObj.put("accounttype", budgetGroupContract.getAccountType());
        indexObj.put("active", budgetGroupContract.getActive());
        indexObj.put("budgetingtype", budgetGroupContract.getBudgetingType());

        indexObj.put("createdby", budgetGroupContract.getCreatedBy());
        indexObj.put("lastmodifiedby", budgetGroupContract.getLastModifiedBy());
        indexObj.put("tenantid", budgetGroupContract.getTenantId());

        if (budgetGroupContract.getCreatedDate() != null)
            indexObj.put("createddate", formatter.format(budgetGroupContract.getCreatedDate()));
        else
            indexObj.put("createddate", null);
        if (budgetGroupContract.getLastModifiedDate() != null)
            indexObj.put("lastmodifieddate", formatter.format(budgetGroupContract.getLastModifiedDate()));
        else
            indexObj.put("lastmodifieddate", null);

        return indexObj;
    }

    private HashMap<String, Object> getChartOfAccountContractIndexObject(final ChartOfAccountContract chartOfAccountContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", chartOfAccountContract.getId());
        indexObj.put("glcode", chartOfAccountContract.getGlcode());
        indexObj.put("name", chartOfAccountContract.getName());
        indexObj.put("accountcodepurpose", chartOfAccountContract.getAccountCodePurpose());
        indexObj.put("description", chartOfAccountContract.getDescription());
        indexObj.put("isactiveforposting", chartOfAccountContract.getIsActiveForPosting());
        indexObj.put("parentid", chartOfAccountContract.getParentId());
        indexObj.put("type", chartOfAccountContract.getType());
        indexObj.put("classification", chartOfAccountContract.getClassification());
        indexObj.put("functionrequired", chartOfAccountContract.getFunctionRequired());

        indexObj.put("budgetcheckrequired", chartOfAccountContract.getBudgetCheckRequired());
        indexObj.put("majorcode", chartOfAccountContract.getMajorCode());
        indexObj.put("issubledger", chartOfAccountContract.getIsSubLedger());

        indexObj.put("createdby", chartOfAccountContract.getCreatedBy());
        indexObj.put("lastmodifiedby", chartOfAccountContract.getLastModifiedBy());
        indexObj.put("tenantid", chartOfAccountContract.getTenantId());

        if (chartOfAccountContract.getCreatedDate() != null)
            indexObj.put("createddate", formatter.format(chartOfAccountContract.getCreatedDate()));
        else
            indexObj.put("createddate", null);
        if (chartOfAccountContract.getLastModifiedDate() != null)
            indexObj.put("lastmodifieddate", formatter.format(chartOfAccountContract.getLastModifiedDate()));
        else
            indexObj.put("lastmodifieddate", null);

        return indexObj;
    }

    private HashMap<String, Object> getChartOfAccountDetailContractIndexObject(
            final ChartOfAccountDetailContract chartOfAccountDetailContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", chartOfAccountDetailContract.getId());
        indexObj.put("accountdetailtype", chartOfAccountDetailContract.getAccountDetailType());
        indexObj.put("chartofaccount", chartOfAccountDetailContract.getChartOfAccount());
        indexObj.put("createdby", chartOfAccountDetailContract.getCreatedBy());
        indexObj.put("lastmodifiedby", chartOfAccountDetailContract.getLastModifiedBy());
        indexObj.put("tenantid", chartOfAccountDetailContract.getTenantId());

        if (chartOfAccountDetailContract.getCreatedDate() != null)
            indexObj.put("createddate", formatter.format(chartOfAccountDetailContract.getCreatedDate()));
        else
            indexObj.put("createddate", null);
        if (chartOfAccountDetailContract.getLastModifiedDate() != null)
            indexObj.put("lastmodifieddate", formatter.format(chartOfAccountDetailContract.getLastModifiedDate()));
        else
            indexObj.put("lastmodifieddate", null);

        return indexObj;
    }

    private HashMap<String, Object> getFinancialYearContractIndexObject(final FinancialYearContract financialYearContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", financialYearContract.getId());
        indexObj.put("finyearrange", financialYearContract.getFinYearRange());
        indexObj.put("startingdate", financialYearContract.getStartingDate());
        indexObj.put("endingdate", financialYearContract.getEndingDate());
        indexObj.put("isactiveforposting", financialYearContract.getIsActiveForPosting());
        indexObj.put("isclosed", financialYearContract.getIsClosed());
        indexObj.put("active", financialYearContract.getActive());
        indexObj.put("transferclosingbalance", financialYearContract.getTransferClosingBalance());
        indexObj.put("createdby", financialYearContract.getCreatedBy());
        indexObj.put("lastmodifiedby", financialYearContract.getLastModifiedBy());
        indexObj.put("tenantid", financialYearContract.getTenantId());

        if (financialYearContract.getCreatedDate() != null)
            indexObj.put("createddate", formatter.format(financialYearContract.getCreatedDate()));
        else
            indexObj.put("createddate", null);
        if (financialYearContract.getLastModifiedDate() != null)
            indexObj.put("lastmodifieddate", formatter.format(financialYearContract.getLastModifiedDate()));
        else
            indexObj.put("lastmodifieddate", null);

        return indexObj;
    }

    private HashMap<String, Object> getFiscalPeriodContractIndexObject(final FiscalPeriodContract fiscalPeriodContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", fiscalPeriodContract.getId());
        indexObj.put("name", fiscalPeriodContract.getName());
        indexObj.put("financialyear", fiscalPeriodContract.getFinancialYear());
        indexObj.put("isactiveforposting", fiscalPeriodContract.getIsActiveForPosting());
        indexObj.put("isclosed", fiscalPeriodContract.getIsClosed());
        indexObj.put("active", fiscalPeriodContract.getActive());
        indexObj.put("startingdate", fiscalPeriodContract.getStartingDate());
        indexObj.put("endingdate", fiscalPeriodContract.getEndingDate());
        indexObj.put("createdby", fiscalPeriodContract.getCreatedBy());
        indexObj.put("lastmodifiedby", fiscalPeriodContract.getLastModifiedBy());
        indexObj.put("tenantid", fiscalPeriodContract.getTenantId());

        if (fiscalPeriodContract.getCreatedDate() != null)
            indexObj.put("createddate", formatter.format(fiscalPeriodContract.getCreatedDate()));
        else
            indexObj.put("createddate", null);
        if (fiscalPeriodContract.getLastModifiedDate() != null)
            indexObj.put("lastmodifieddate", formatter.format(fiscalPeriodContract.getLastModifiedDate()));
        else
            indexObj.put("lastmodifieddate", null);

        return indexObj;
    }

    private HashMap<String, Object> getFunctionaryContractIndexObject(final FunctionaryContract functionaryContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", functionaryContract.getId());
        indexObj.put("code", functionaryContract.getCode());
        indexObj.put("name", functionaryContract.getName());
        indexObj.put("active", functionaryContract.getActive());
        indexObj.put("createdby", functionaryContract.getCreatedBy());
        indexObj.put("lastmodifiedby", functionaryContract.getLastModifiedBy());
        indexObj.put("tenantid", functionaryContract.getTenantId());

        if (functionaryContract.getCreatedDate() != null)
            indexObj.put("createddate", formatter.format(functionaryContract.getCreatedDate()));
        else
            indexObj.put("createddate", null);
        if (functionaryContract.getLastModifiedDate() != null)
            indexObj.put("lastmodifieddate", formatter.format(functionaryContract.getLastModifiedDate()));
        else
            indexObj.put("lastmodifieddate", null);

        return indexObj;
    }

    private HashMap<String, Object> getFundsourceContractIndexObject(final FundsourceContract fundsourceContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", fundsourceContract.getId());
        indexObj.put("code", fundsourceContract.getCode());
        indexObj.put("name", fundsourceContract.getName());
        indexObj.put("fundsource", fundsourceContract.getFundSource());
        indexObj.put("type", fundsourceContract.getType());
        indexObj.put("llevel", fundsourceContract.getLlevel());
        indexObj.put("active", fundsourceContract.getActive());
        indexObj.put("isparent", fundsourceContract.getIsParent());
        indexObj.put("createdby", fundsourceContract.getCreatedBy());
        indexObj.put("lastmodifiedby", fundsourceContract.getLastModifiedBy());
        indexObj.put("tenantid", fundsourceContract.getTenantId());

        if (fundsourceContract.getCreatedDate() != null)
            indexObj.put("createddate", formatter.format(fundsourceContract.getCreatedDate()));
        else
            indexObj.put("createddate", null);
        if (fundsourceContract.getLastModifiedDate() != null)
            indexObj.put("lastmodifieddate", formatter.format(fundsourceContract.getLastModifiedDate()));
        else
            indexObj.put("lastmodifieddate", null);

        return indexObj;
    }

    private HashMap<String, Object> getSchemeContractIndexObject(final SchemeContract schemeContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", schemeContract.getId());
        indexObj.put("code", schemeContract.getCode());
        indexObj.put("name", schemeContract.getName());
        indexObj.put("fund", schemeContract.getFund());
        indexObj.put("validfrom", schemeContract.getValidFrom());
        indexObj.put("validto", schemeContract.getValidTo());
        indexObj.put("boundary", schemeContract.getBoundary());
        indexObj.put("description", schemeContract.getDescription());
        indexObj.put("createdby", schemeContract.getCreatedBy());
        indexObj.put("lastmodifiedby", schemeContract.getLastModifiedBy());
        indexObj.put("tenantid", schemeContract.getTenantId());

        if (schemeContract.getCreatedDate() != null)
            indexObj.put("createddate", formatter.format(schemeContract.getCreatedDate()));
        else
            indexObj.put("createddate", null);
        if (schemeContract.getLastModifiedDate() != null)
            indexObj.put("lastmodifieddate", formatter.format(schemeContract.getLastModifiedDate()));
        else
            indexObj.put("lastmodifieddate", null);

        return indexObj;
    }

    private HashMap<String, Object> getSubSchemeContractIndexObject(final SubSchemeContract subSchemeContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", subSchemeContract.getId());
        indexObj.put("scheme", subSchemeContract.getScheme());
        indexObj.put("code", subSchemeContract.getCode());
        indexObj.put("name", subSchemeContract.getName());
        indexObj.put("departmentid", subSchemeContract.getDepartmentId());
        indexObj.put("validfrom", subSchemeContract.getValidFrom());
        indexObj.put("validto", subSchemeContract.getValidTo());
        indexObj.put("createdby", subSchemeContract.getCreatedBy());
        indexObj.put("lastmodifiedby", subSchemeContract.getLastModifiedBy());
        indexObj.put("tenantid", subSchemeContract.getTenantId());

        if (subSchemeContract.getCreatedDate() != null)
            indexObj.put("createddate", formatter.format(subSchemeContract.getCreatedDate()));
        else
            indexObj.put("createddate", null);
        if (subSchemeContract.getLastModifiedDate() != null)
            indexObj.put("lastmodifieddate", formatter.format(subSchemeContract.getLastModifiedDate()));
        else
            indexObj.put("lastmodifieddate", null);

        return indexObj;
    }

    private HashMap<String, Object> getSupplierContractIndexObject(final SupplierContract supplierContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", supplierContract.getId());
        indexObj.put("code", supplierContract.getCode());
        indexObj.put("name", supplierContract.getName());
        indexObj.put("address", supplierContract.getAddress());
        indexObj.put("mobile", supplierContract.getMobile());
        indexObj.put("email", supplierContract.getEmail());
        indexObj.put("active", supplierContract.getActive());
        indexObj.put("description", supplierContract.getDescription());
        indexObj.put("panno", supplierContract.getPanNo());
        indexObj.put("tinno", supplierContract.getTinNo());
        indexObj.put("registationno", supplierContract.getRegistationNo());
        indexObj.put("bankaccount", supplierContract.getBankAccount());
        indexObj.put("ifsccode", supplierContract.getIfscCode());
        indexObj.put("bank", supplierContract.getBank());

        indexObj.put("createdby", supplierContract.getCreatedBy());
        indexObj.put("lastmodifiedby", supplierContract.getLastModifiedBy());
        indexObj.put("tenantid", supplierContract.getTenantId());

        if (supplierContract.getCreatedDate() != null)
            indexObj.put("createddate", formatter.format(supplierContract.getCreatedDate()));
        else
            indexObj.put("createddate", null);
        if (supplierContract.getLastModifiedDate() != null)
            indexObj.put("lastmodifieddate", formatter.format(supplierContract.getLastModifiedDate()));
        else
            indexObj.put("lastmodifieddate", null);

        return indexObj;
    }

    private HashMap<String, Object> getFinancialStatusContractIndexObject(final FinancialStatusContract financialStatusContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", financialStatusContract.getId());
        indexObj.put("moduletype", financialStatusContract.getModuleType());
        indexObj.put("code", financialStatusContract.getCode());
        indexObj.put("description", financialStatusContract.getDescription());
        indexObj.put("createdby", financialStatusContract.getCreatedBy());
        indexObj.put("lastmodifiedby", financialStatusContract.getLastModifiedBy());
        indexObj.put("tenantid", financialStatusContract.getTenantId());

        if (financialStatusContract.getCreatedDate() != null)
            indexObj.put("createddate", formatter.format(financialStatusContract.getCreatedDate()));
        else
            indexObj.put("createddate", null);
        if (financialStatusContract.getLastModifiedDate() != null)
            indexObj.put("lastmodifieddate", formatter.format(financialStatusContract.getLastModifiedDate()));
        else
            indexObj.put("lastmodifieddate", null);

        return indexObj;
    }

    private HashMap<String, Object> getBankAccountIndexObject(final BankAccountContract bankAccountContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", bankAccountContract.getId());
        indexObj.put("bankbranch", bankAccountContract.getBankBranch());
        indexObj.put("chartofaccount", bankAccountContract.getChartOfAccount());
        indexObj.put("fund", bankAccountContract.getFund());
        indexObj.put("accountnumber", bankAccountContract.getAccountNumber());
        indexObj.put("accounttype", bankAccountContract.getAccountType());
        indexObj.put("active", bankAccountContract.getActive());
        indexObj.put("description", bankAccountContract.getDescription());
        indexObj.put("payto", bankAccountContract.getPayTo());
        indexObj.put("type", bankAccountContract.getType());
        indexObj.put("createdby", bankAccountContract.getCreatedBy());
        indexObj.put("lastmodifiedby", bankAccountContract.getLastModifiedBy());
        indexObj.put("tenantid", bankAccountContract.getTenantId());

        if (bankAccountContract.getCreatedDate() != null)
            indexObj.put("createddate", formatter.format(bankAccountContract.getCreatedDate()));
        else
            indexObj.put("createddate", null);
        if (bankAccountContract.getLastModifiedDate() != null)
            indexObj.put("lastmodifieddate", formatter.format(bankAccountContract.getLastModifiedDate()));
        else
            indexObj.put("lastmodifieddate", null);

        return indexObj;
    }

}
