package org.egov.egf.master.index.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.egf.master.index.persistence.repository.ElasticSearchRepository;
import org.egov.egf.master.web.contract.*;
import org.egov.egf.master.web.requests.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;

@Service
public class MasterIndexerListener {

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
    private static final String RECOVERYCONTRACT_OBJECT_TYPE = "recovery";

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Autowired
    private ElasticSearchRepository elasticSearchRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(id = "${kafka.topics.egov.index.id}", topics = {
            "${kafka.topics.egov.index.name}"}, group = "${kafka.topics.egov.index.group}")
    public void listen(final HashMap<String, Object> financialContractRequestMap) {

        if (financialContractRequestMap.get("fund_persisted") != null) {

            final FundRequest request = objectMapper.convertValue(financialContractRequestMap.get("fund_persisted"),
                    FundRequest.class);

            if (request.getFunds() != null && !request.getFunds().isEmpty())
                for (final FundContract fundContract : request.getFunds()) {
                    final HashMap<String, Object> indexObj = getFundIndexObject(fundContract);
                    elasticSearchRepository.index(FUND_OBJECT_TYPE,
                            fundContract.getCode() + "-" + fundContract.getTenantId(), indexObj);
                }
        }

        if (financialContractRequestMap.get("bank_persisted") != null) {

            final BankRequest request = objectMapper.convertValue(financialContractRequestMap.get("bank_persisted"),
                    BankRequest.class);

            if (request.getBanks() != null && !request.getBanks().isEmpty())
                for (final BankContract bankContract : request.getBanks()) {
                    final HashMap<String, Object> indexObj = getBankIndexObject(bankContract);
                    elasticSearchRepository.index(BANK_OBJECT_TYPE,
                            bankContract.getCode() + "-" + bankContract.getTenantId(), indexObj);
                }
        }

        if (financialContractRequestMap.get("function_persisted") != null) {

            final FunctionRequest request = objectMapper
                    .convertValue(financialContractRequestMap.get("function_persisted"), FunctionRequest.class);

            if (request.getFunctions() != null && !request.getFunctions().isEmpty())
                for (final FunctionContract functionContract : request.getFunctions()) {
                    final HashMap<String, Object> indexObj = getFunctionIndexObject(functionContract);
                    elasticSearchRepository.index(FUNCTION_OBJECT_TYPE,
                            functionContract.getCode() + "-" + functionContract.getTenantId(), indexObj);
                }
        }

        if (financialContractRequestMap.get("bankbranch_persisted") != null) {

            final BankBranchRequest request = objectMapper
                    .convertValue(financialContractRequestMap.get("bankbranch_persisted"), BankBranchRequest.class);

            if (request.getBankBranches() != null && !request.getBankBranches().isEmpty())
                for (final BankBranchContract bankBranchContract : request.getBankBranches()) {
                    final HashMap<String, Object> indexObj = getBankBranchIndexObject(bankBranchContract);
                    elasticSearchRepository.index(BANKBRANCH_OBJECT_TYPE,
                            bankBranchContract.getCode() + "-" + bankBranchContract.getTenantId(), indexObj);
                }
        }

        if (financialContractRequestMap.get("bankaccount_persisted") != null) {

            final BankAccountRequest request = objectMapper
                    .convertValue(financialContractRequestMap.get("bankaccount_persisted"), BankAccountRequest.class);

            if (request.getBankAccounts() != null && !request.getBankAccounts().isEmpty())
                for (final BankAccountContract bankAccountContract : request.getBankAccounts()) {
                    final HashMap<String, Object> indexObj = getBankAccountIndexObject(bankAccountContract);
                    elasticSearchRepository.index(BANKACCOUNT_OBJECT_TYPE,
                            bankAccountContract.getAccountNumber() + "-" + bankAccountContract.getTenantId(), indexObj);
                }
        }

        if (financialContractRequestMap.get("accountcodepurpose_persisted") != null) {

            final AccountCodePurposeRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("accountcodepurpose_persisted"), AccountCodePurposeRequest.class);

            if (request.getAccountCodePurposes() != null && !request.getAccountCodePurposes().isEmpty())
                for (final AccountCodePurposeContract accountCodePurposeContract : request.getAccountCodePurposes()) {
                    final HashMap<String, Object> indexObj = getAccountCodePurposeContractIndexObject(
                            accountCodePurposeContract);
                    elasticSearchRepository.index(ACCOUNTCODEPURPOSE_OBJECT_TYPE,
                            accountCodePurposeContract.getName() + "-" + accountCodePurposeContract.getTenantId(),
                            indexObj);
                }
        }

        if (financialContractRequestMap.get("accountdetailtype_persisted") != null) {

            final AccountDetailTypeRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("accountdetailtype_persisted"), AccountDetailTypeRequest.class);

            if (request.getAccountDetailTypes() != null && !request.getAccountDetailTypes().isEmpty())
                for (final AccountDetailTypeContract accountDetailTypeContract : request.getAccountDetailTypes()) {
                    final HashMap<String, Object> indexObj = getAccountDetailTypeContractIndexObject(
                            accountDetailTypeContract);
                    elasticSearchRepository.index(ACCOUNTDETAILTYPE_OBJECT_TYPE,
                            accountDetailTypeContract.getName() + "-" + accountDetailTypeContract.getTenantId(),
                            indexObj);
                }
        }

        if (financialContractRequestMap.get("accountdetailkey_persisted") != null) {

            final AccountDetailKeyRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("accountdetailkey_persisted"), AccountDetailKeyRequest.class);

            if (request.getAccountDetailKeys() != null && !request.getAccountDetailKeys().isEmpty())
                for (final AccountDetailKeyContract accountDetailKeyContract : request.getAccountDetailKeys()) {
                    final HashMap<String, Object> indexObj = getAccountDetailKeyContractIndexObject(
                            accountDetailKeyContract);
                    elasticSearchRepository.index(ACCOUNTDETAILKEY_OBJECT_TYPE,
                            accountDetailKeyContract.getKey() + "-" + accountDetailKeyContract.getTenantId(), indexObj);
                }
        }

        if (financialContractRequestMap.get("accountentity_persisted") != null) {

            final AccountEntityRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("accountentity_persisted"), AccountEntityRequest.class);

            if (request.getAccountEntities() != null && !request.getAccountEntities().isEmpty())
                for (final AccountEntityContract accountEntityContract : request.getAccountEntities()) {
                    final HashMap<String, Object> indexObj = getAccountEntityContractIndexObject(accountEntityContract);
                    elasticSearchRepository.index(ACCOUNTENTITY_OBJECT_TYPE,
                            accountEntityContract.getCode() + "-" + accountEntityContract.getTenantId(), indexObj);
                }
        }

        if (financialContractRequestMap.get("budgetgroup_persisted") != null) {

            final BudgetGroupRequest request = objectMapper
                    .convertValue(financialContractRequestMap.get("budgetgroup_persisted"), BudgetGroupRequest.class);

            if (request.getBudgetGroups() != null && !request.getBudgetGroups().isEmpty())
                for (final BudgetGroupContract budgetGroupContract : request.getBudgetGroups()) {
                    final HashMap<String, Object> indexObj = getBudgetGroupContractIndexObject(budgetGroupContract);
                    elasticSearchRepository.index(BUDGETGROUP_OBJECT_TYPE,
                            budgetGroupContract.getTenantId() + "-" + budgetGroupContract.getName(), indexObj);
                }
        }

        if (financialContractRequestMap.get("chartofaccount_persisted") != null) {

            final ChartOfAccountRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("chartofaccount_persisted"), ChartOfAccountRequest.class);

            if (request.getChartOfAccounts() != null && !request.getChartOfAccounts().isEmpty())
                for (final ChartOfAccountContract chartOfAccountContract : request.getChartOfAccounts()) {
                    final HashMap<String, Object> indexObj = getChartOfAccountContractIndexObject(
                            chartOfAccountContract);
                    elasticSearchRepository.index(CHARTOFACCOUNT_OBJECT_TYPE,
                            chartOfAccountContract.getTenantId() + "-" + chartOfAccountContract.getGlcode(), indexObj);
                }
        }

        if (financialContractRequestMap.get("chartofaccountdetail_persisted") != null) {

            final ChartOfAccountDetailRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("chartofaccountdetail_persisted"),
                    ChartOfAccountDetailRequest.class);

            if (request.getChartOfAccountDetails() != null && !request.getChartOfAccountDetails().isEmpty())
                for (final ChartOfAccountDetailContract chartOfAccountDetailContract : request
                        .getChartOfAccountDetails()) {
                    final HashMap<String, Object> indexObj = getChartOfAccountDetailContractIndexObject(
                            chartOfAccountDetailContract);
                    elasticSearchRepository.index(CHARTOFACCOUNTDETAIL_OBJECT_TYPE,
                            chartOfAccountDetailContract.getTenantId() + "-"
                                    + chartOfAccountDetailContract.getChartOfAccount().getGlcode(),
                            indexObj);
                }
        }

        if (financialContractRequestMap.get("financialyear_persisted") != null) {

            final FinancialYearRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("financialyear_persisted"), FinancialYearRequest.class);

            if (request.getFinancialYears() != null && !request.getFinancialYears().isEmpty())
                for (final FinancialYearContract financialYearContract : request.getFinancialYears()) {
                    final HashMap<String, Object> indexObj = getFinancialYearContractIndexObject(financialYearContract);
                    elasticSearchRepository.index(FINANCIALYEAR_OBJECT_TYPE,
                            financialYearContract.getTenantId() + "-" + financialYearContract.getFinYearRange(),
                            indexObj);
                }
        }

        if (financialContractRequestMap.get("fiscalperiod_persisted") != null) {

            final FiscalPeriodRequest request = objectMapper
                    .convertValue(financialContractRequestMap.get("fiscalperiod_persisted"), FiscalPeriodRequest.class);

            if (request.getFiscalPeriods() != null && !request.getFiscalPeriods().isEmpty())
                for (final FiscalPeriodContract fiscalPeriodContract : request.getFiscalPeriods()) {
                    final HashMap<String, Object> indexObj = getFiscalPeriodContractIndexObject(fiscalPeriodContract);
                    elasticSearchRepository.index(FISCALPERIOD_OBJECT_TYPE,
                            fiscalPeriodContract.getTenantId() + "-" + fiscalPeriodContract.getName(), indexObj);
                }
        }

        if (financialContractRequestMap.get("functionary_persisted") != null) {

            final FunctionaryRequest request = objectMapper
                    .convertValue(financialContractRequestMap.get("functionary_persisted"), FunctionaryRequest.class);

            if (request.getFunctionaries() != null && !request.getFunctionaries().isEmpty())
                for (final FunctionaryContract functionaryContract : request.getFunctionaries()) {
                    final HashMap<String, Object> indexObj = getFunctionaryContractIndexObject(functionaryContract);
                    elasticSearchRepository.index(FUNCTIONARY_OBJECT_TYPE,
                            functionaryContract.getTenantId() + "-" + functionaryContract.getName(), indexObj);
                }
        }

        if (financialContractRequestMap.get("fundsource_persisted") != null) {

            final FundsourceRequest request = objectMapper
                    .convertValue(financialContractRequestMap.get("fundsource_persisted"), FundsourceRequest.class);

            if (request.getFundsources() != null && !request.getFundsources().isEmpty())
                for (final FundsourceContract fundsourceContract : request.getFundsources()) {
                    final HashMap<String, Object> indexObj = getFundsourceContractIndexObject(fundsourceContract);
                    elasticSearchRepository.index(FUNDSOURCE_OBJECT_TYPE,
                            fundsourceContract.getTenantId() + "-" + fundsourceContract.getName(), indexObj);
                }
        }

        if (financialContractRequestMap.get("scheme_persisted") != null) {

            final SchemeRequest request = objectMapper.convertValue(financialContractRequestMap.get("scheme_persisted"),
                    SchemeRequest.class);

            if (request.getSchemes() != null && !request.getSchemes().isEmpty())
                for (final SchemeContract schemeContract : request.getSchemes()) {
                    final HashMap<String, Object> indexObj = getSchemeContractIndexObject(schemeContract);
                    elasticSearchRepository.index(SCHEME_OBJECT_TYPE,
                            schemeContract.getTenantId() + "-" + schemeContract.getName(), indexObj);
                }
        }

        if (financialContractRequestMap.get("subscheme_persisted") != null) {

            final SubSchemeRequest request = objectMapper
                    .convertValue(financialContractRequestMap.get("subscheme_persisted"), SubSchemeRequest.class);

            if (request.getSubSchemes() != null && !request.getSubSchemes().isEmpty())
                for (final SubSchemeContract subSchemeContract : request.getSubSchemes()) {
                    final HashMap<String, Object> indexObj = getSubSchemeContractIndexObject(subSchemeContract);
                    elasticSearchRepository.index(SUBSCHEME_OBJECT_TYPE,
                            subSchemeContract.getTenantId() + "-" + subSchemeContract.getName(), indexObj);
                }
        }

        if (financialContractRequestMap.get("supplier_persisted") != null) {

            final SupplierRequest request = objectMapper
                    .convertValue(financialContractRequestMap.get("supplier_persisted"), SupplierRequest.class);

            if (request.getSuppliers() != null && !request.getSuppliers().isEmpty())
                for (final SupplierContract supplierContract : request.getSuppliers()) {
                    final HashMap<String, Object> indexObj = getSupplierContractIndexObject(supplierContract);
                    elasticSearchRepository.index(SUPPLIER_OBJECT_TYPE,
                            supplierContract.getTenantId() + "-" + supplierContract.getName(), indexObj);
                }
        }

        if (financialContractRequestMap.get("financialstatus_persisted") != null) {

            final FinancialStatusRequest request = objectMapper.convertValue(
                    financialContractRequestMap.get("financialstatus_persisted"), FinancialStatusRequest.class);

            if (request.getFinancialStatuses() != null && !request.getFinancialStatuses().isEmpty())
                for (final FinancialStatusContract financialStatusContract : request.getFinancialStatuses()) {
                    final HashMap<String, Object> indexObj = getFinancialStatusContractIndexObject(
                            financialStatusContract);
                    elasticSearchRepository.index(FINANCIALSTATUS_OBJECT_TYPE,
                            financialStatusContract.getTenantId() + "-" + financialStatusContract.getCode(), indexObj);
                }
        }

        if (financialContractRequestMap.get("budget_persisted") != null) {

            final FinancialStatusRequest request = objectMapper
                    .convertValue(financialContractRequestMap.get("budget_persisted"), FinancialStatusRequest.class);

            if (request.getFinancialStatuses() != null && !request.getFinancialStatuses().isEmpty())
                for (final FinancialStatusContract financialStatusContract : request.getFinancialStatuses()) {
                    final HashMap<String, Object> indexObj = getFinancialStatusContractIndexObject(
                            financialStatusContract);
                    elasticSearchRepository.index(FINANCIALSTATUS_OBJECT_TYPE,
                            financialStatusContract.getTenantId() + "-" + financialStatusContract.getCode(), indexObj);
                }
        }

        if (financialContractRequestMap.get("recovery_persisted") != null) {

            final RecoveryRequest request = objectMapper
                    .convertValue(financialContractRequestMap.get("recovery_persisted"), RecoveryRequest.class);

            if (request.getRecoverys() != null && !request.getRecoverys().isEmpty())
                for (final RecoveryContract recoveryContract : request.getRecoverys()) {
                    final HashMap<String, Object> indexObj = getRecoveryContractIndexObject(recoveryContract);
                    elasticSearchRepository.index(RECOVERYCONTRACT_OBJECT_TYPE,
                            recoveryContract.getTenantId() + "-" + recoveryContract.getName(), indexObj);
                }
        }

    }

    private HashMap<String, Object> getRecoveryContractIndexObject(final RecoveryContract recoveryContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", recoveryContract.getId());
        indexObj.put("name", recoveryContract.getName());
        indexObj.put("accountNumber", recoveryContract.getAccountNumber());
        indexObj.put("tenantId", recoveryContract.getTenantId());
        indexObj.put("code", recoveryContract.getCode());
        indexObj.put("chartOfAccount", recoveryContract.getChartOfAccount());
        indexObj.put("type", recoveryContract.getType());
        indexObj.put("flat", recoveryContract.getFlat());
        indexObj.put("percentage", recoveryContract.getPercentage());
        indexObj.put("remitted", recoveryContract.getRemitted());
        indexObj.put("active", recoveryContract.getActive());
        indexObj.put("ifscCode", recoveryContract.getIfscCode());
        indexObj.put("mode", recoveryContract.getMode());
        indexObj.put("remittanceMode", recoveryContract.getRemittanceMode());
        indexObj.put("ifscCode", recoveryContract.getIfscCode());

        indexObj.put("createdBy", recoveryContract.getCreatedBy());
        indexObj.put("lastModifiedBy", recoveryContract.getLastModifiedBy());

        if (recoveryContract.getCreatedDate() != null)
            indexObj.put("createdDate", formatter.format(recoveryContract.getCreatedDate()));
        else
            indexObj.put("createdDate", null);
        if (recoveryContract.getLastModifiedDate() != null)
            indexObj.put("lastModifiedDate", formatter.format(recoveryContract.getLastModifiedDate()));
        else
            indexObj.put("lastModifiedDate", null);

        return indexObj;
    }

    private HashMap<String, Object> getFundIndexObject(final FundContract fundContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", fundContract.getId());
        indexObj.put("name", fundContract.getName());
        indexObj.put("code", fundContract.getCode());
        indexObj.put("active", fundContract.getActive());
        indexObj.put("createdBy", fundContract.getCreatedBy());
        indexObj.put("identifier", fundContract.getIdentifier());
        indexObj.put("lastModifiedBy", fundContract.getLastModifiedBy());
        indexObj.put("level", fundContract.getLevel());
        indexObj.put("parent", fundContract.getParent());
        indexObj.put("tenantId", fundContract.getTenantId());

        if (fundContract.getCreatedDate() != null)
            indexObj.put("createdDate", formatter.format(fundContract.getCreatedDate()));
        else
            indexObj.put("createdDate", null);
        if (fundContract.getLastModifiedDate() != null)
            indexObj.put("lastModifiedDate", formatter.format(fundContract.getLastModifiedDate()));
        else
            indexObj.put("lastModifiedDate", null);

        return indexObj;
    }

    private HashMap<String, Object> getBankIndexObject(final BankContract bankContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", bankContract.getId());
        indexObj.put("name", bankContract.getName());
        indexObj.put("code", bankContract.getCode());
        indexObj.put("active", bankContract.getActive());
        indexObj.put("description", bankContract.getActive());
        indexObj.put("createdBy", bankContract.getCreatedBy());
        indexObj.put("type", bankContract.getType());
        indexObj.put("lastModifiedBy", bankContract.getLastModifiedBy());
        indexObj.put("tenantId", bankContract.getTenantId());

        if (bankContract.getCreatedDate() != null)
            indexObj.put("createdDate", formatter.format(bankContract.getCreatedDate()));
        else
            indexObj.put("createdDate", null);
        if (bankContract.getLastModifiedDate() != null)
            indexObj.put("lastModifiedDate", formatter.format(bankContract.getLastModifiedDate()));
        else
            indexObj.put("lastModifiedDate", null);

        return indexObj;
    }

    private HashMap<String, Object> getFunctionIndexObject(final FunctionContract functionContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", functionContract.getId());
        indexObj.put("name", functionContract.getName());
        indexObj.put("code", functionContract.getCode());
        indexObj.put("active", functionContract.getActive());
        indexObj.put("level", functionContract.getLevel());
        indexObj.put("createdBy", functionContract.getCreatedBy());
        indexObj.put("parentId", functionContract.getParentId());
        indexObj.put("lastModifiedBy", functionContract.getLastModifiedBy());
        indexObj.put("tenantId", functionContract.getTenantId());

        if (functionContract.getCreatedDate() != null)
            indexObj.put("createdDate", formatter.format(functionContract.getCreatedDate()));
        else
            indexObj.put("createdDate", null);
        if (functionContract.getLastModifiedDate() != null)
            indexObj.put("lastModifiedDate", formatter.format(functionContract.getLastModifiedDate()));
        else
            indexObj.put("lastModifiedDate", null);

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
        indexObj.put("contactPerson", bankBranchContract.getContactPerson());
        indexObj.put("active", bankBranchContract.getActive());
        indexObj.put("description", bankBranchContract.getDescription());
        indexObj.put("createdBy", bankBranchContract.getCreatedBy());
        indexObj.put("lastModifiedBy", bankBranchContract.getLastModifiedBy());
        indexObj.put("tenantId", bankBranchContract.getTenantId());

        if (bankBranchContract.getCreatedDate() != null)
            indexObj.put("createdDate", formatter.format(bankBranchContract.getCreatedDate()));
        else
            indexObj.put("createdDate", null);
        if (bankBranchContract.getLastModifiedDate() != null)
            indexObj.put("lastModifiedDate", formatter.format(bankBranchContract.getLastModifiedDate()));
        else
            indexObj.put("lastModifiedDate", null);

        return indexObj;
    }

    private HashMap<String, Object> getAccountCodePurposeContractIndexObject(
            final AccountCodePurposeContract accountCodePurposeContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", accountCodePurposeContract.getId());
        indexObj.put("name", accountCodePurposeContract.getName());
        indexObj.put("createdBy", accountCodePurposeContract.getCreatedBy());
        indexObj.put("lastModifiedBy", accountCodePurposeContract.getLastModifiedBy());
        indexObj.put("tenantId", accountCodePurposeContract.getTenantId());

        if (accountCodePurposeContract.getCreatedDate() != null)
            indexObj.put("createdDate", formatter.format(accountCodePurposeContract.getCreatedDate()));
        else
            indexObj.put("createdDate", null);
        if (accountCodePurposeContract.getLastModifiedDate() != null)
            indexObj.put("lastModifiedDate", formatter.format(accountCodePurposeContract.getLastModifiedDate()));
        else
            indexObj.put("lastModifiedDate", null);

        return indexObj;
    }

    private HashMap<String, Object> getAccountDetailTypeContractIndexObject(
            final AccountDetailTypeContract accountDetailTypeContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", accountDetailTypeContract.getId());
        indexObj.put("name", accountDetailTypeContract.getName());
        indexObj.put("description", accountDetailTypeContract.getDescription());
        indexObj.put("tableName", accountDetailTypeContract.getTableName());
        indexObj.put("active", accountDetailTypeContract.getActive());
        indexObj.put("fullyQualifiedName", accountDetailTypeContract.getFullyQualifiedName());
        indexObj.put("createdBy", accountDetailTypeContract.getCreatedBy());
        indexObj.put("lastModifiedBy", accountDetailTypeContract.getLastModifiedBy());
        indexObj.put("tenantId", accountDetailTypeContract.getTenantId());

        if (accountDetailTypeContract.getCreatedDate() != null)
            indexObj.put("createdDate", formatter.format(accountDetailTypeContract.getCreatedDate()));
        else
            indexObj.put("createdDate", null);
        if (accountDetailTypeContract.getLastModifiedDate() != null)
            indexObj.put("lastModifiedDate", formatter.format(accountDetailTypeContract.getLastModifiedDate()));
        else
            indexObj.put("lastModifiedDate", null);

        return indexObj;
    }

    private HashMap<String, Object> getAccountDetailKeyContractIndexObject(
            final AccountDetailKeyContract accountDetailKeyContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", accountDetailKeyContract.getId());
        indexObj.put("key", accountDetailKeyContract.getKey());
        indexObj.put("accountDetailType", accountDetailKeyContract.getAccountDetailType());
        indexObj.put("createdBy", accountDetailKeyContract.getCreatedBy());
        indexObj.put("lastModifiedBy", accountDetailKeyContract.getLastModifiedBy());
        indexObj.put("tenantId", accountDetailKeyContract.getTenantId());

        if (accountDetailKeyContract.getCreatedDate() != null)
            indexObj.put("createdDate", formatter.format(accountDetailKeyContract.getCreatedDate()));
        else
            indexObj.put("createdDate", null);
        if (accountDetailKeyContract.getLastModifiedDate() != null)
            indexObj.put("lastModifiedDate", formatter.format(accountDetailKeyContract.getLastModifiedDate()));
        else
            indexObj.put("lastModifiedDate", null);

        return indexObj;
    }

    private HashMap<String, Object> getAccountEntityContractIndexObject(
            final AccountEntityContract accountEntityContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", accountEntityContract.getId());
        indexObj.put("code", accountEntityContract.getCode());
        indexObj.put("name", accountEntityContract.getName());
        indexObj.put("description", accountEntityContract.getDescription());
        indexObj.put("accountDetailType", accountEntityContract.getAccountDetailType());
        indexObj.put("active", accountEntityContract.getActive());
        indexObj.put("createdBy", accountEntityContract.getCreatedBy());
        indexObj.put("lastModifiedBy", accountEntityContract.getLastModifiedBy());
        indexObj.put("tenantId", accountEntityContract.getTenantId());

        if (accountEntityContract.getCreatedDate() != null)
            indexObj.put("createdDate", formatter.format(accountEntityContract.getCreatedDate()));
        else
            indexObj.put("createdDate", null);
        if (accountEntityContract.getLastModifiedDate() != null)
            indexObj.put("lastModifiedDate", formatter.format(accountEntityContract.getLastModifiedDate()));
        else
            indexObj.put("lastModifiedDate", null);

        return indexObj;
    }

    private HashMap<String, Object> getBudgetGroupContractIndexObject(final BudgetGroupContract budgetGroupContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", budgetGroupContract.getId());
        indexObj.put("name", budgetGroupContract.getName());
        indexObj.put("description", budgetGroupContract.getDescription());
        indexObj.put("majorCode", budgetGroupContract.getMajorCode());
        indexObj.put("maxCode", budgetGroupContract.getMaxCode());
        indexObj.put("minCode", budgetGroupContract.getMinCode());
        indexObj.put("accountType", budgetGroupContract.getAccountType());
        indexObj.put("active", budgetGroupContract.getActive());
        indexObj.put("budgetingType", budgetGroupContract.getBudgetingType());

        indexObj.put("createdBy", budgetGroupContract.getCreatedBy());
        indexObj.put("lastModifiedBy", budgetGroupContract.getLastModifiedBy());
        indexObj.put("tenantId", budgetGroupContract.getTenantId());

        if (budgetGroupContract.getCreatedDate() != null)
            indexObj.put("createdDate", formatter.format(budgetGroupContract.getCreatedDate()));
        else
            indexObj.put("createdDate", null);
        if (budgetGroupContract.getLastModifiedDate() != null)
            indexObj.put("lastModifiedDate", formatter.format(budgetGroupContract.getLastModifiedDate()));
        else
            indexObj.put("lastModifiedDate", null);

        return indexObj;
    }

    private HashMap<String, Object> getChartOfAccountContractIndexObject(
            final ChartOfAccountContract chartOfAccountContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", chartOfAccountContract.getId());
        indexObj.put("glcode", chartOfAccountContract.getGlcode());
        indexObj.put("name", chartOfAccountContract.getName());
        indexObj.put("accountCodePurpose", chartOfAccountContract.getAccountCodePurpose());
        indexObj.put("description", chartOfAccountContract.getDescription());
        indexObj.put("isActiveForPosting", chartOfAccountContract.getIsActiveForPosting());
        indexObj.put("parentId", chartOfAccountContract.getParentId());
        indexObj.put("type", chartOfAccountContract.getType());
        indexObj.put("classification", chartOfAccountContract.getClassification());
        indexObj.put("functionRequired", chartOfAccountContract.getFunctionRequired());

        indexObj.put("budgetCheckRequired", chartOfAccountContract.getBudgetCheckRequired());
        indexObj.put("majorCode", chartOfAccountContract.getMajorCode());
        indexObj.put("isSubledger", chartOfAccountContract.getIsSubLedger());

        indexObj.put("createdBy", chartOfAccountContract.getCreatedBy());
        indexObj.put("lastModifiedBy", chartOfAccountContract.getLastModifiedBy());
        indexObj.put("tenantId", chartOfAccountContract.getTenantId());

        if (chartOfAccountContract.getCreatedDate() != null)
            indexObj.put("createdDate", formatter.format(chartOfAccountContract.getCreatedDate()));
        else
            indexObj.put("createdDate", null);
        if (chartOfAccountContract.getLastModifiedDate() != null)
            indexObj.put("lastModifiedDate", formatter.format(chartOfAccountContract.getLastModifiedDate()));
        else
            indexObj.put("lastModifiedDate", null);

        return indexObj;
    }

    private HashMap<String, Object> getChartOfAccountDetailContractIndexObject(
            final ChartOfAccountDetailContract chartOfAccountDetailContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", chartOfAccountDetailContract.getId());
        indexObj.put("accountDetailType", chartOfAccountDetailContract.getAccountDetailType());
        indexObj.put("chartOfAccount", chartOfAccountDetailContract.getChartOfAccount());
        indexObj.put("createdBy", chartOfAccountDetailContract.getCreatedBy());
        indexObj.put("lastModifiedBy", chartOfAccountDetailContract.getLastModifiedBy());
        indexObj.put("tenantId", chartOfAccountDetailContract.getTenantId());

        if (chartOfAccountDetailContract.getCreatedDate() != null)
            indexObj.put("createdDate", formatter.format(chartOfAccountDetailContract.getCreatedDate()));
        else
            indexObj.put("createdDate", null);
        if (chartOfAccountDetailContract.getLastModifiedDate() != null)
            indexObj.put("lastModifiedDate", formatter.format(chartOfAccountDetailContract.getLastModifiedDate()));
        else
            indexObj.put("lastModifiedDate", null);

        return indexObj;
    }

    private HashMap<String, Object> getFinancialYearContractIndexObject(
            final FinancialYearContract financialYearContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", financialYearContract.getId());
        indexObj.put("finYearRange", financialYearContract.getFinYearRange());
        indexObj.put("startingDate", financialYearContract.getStartingDate());
        indexObj.put("endingDate", financialYearContract.getEndingDate());
        indexObj.put("isActiveForPosting", financialYearContract.getIsActiveForPosting());
        indexObj.put("isClosed", financialYearContract.getIsClosed());
        indexObj.put("active", financialYearContract.getActive());
        indexObj.put("transferClosingBalance", financialYearContract.getTransferClosingBalance());
        indexObj.put("createdBy", financialYearContract.getCreatedBy());
        indexObj.put("lastModifiedBy", financialYearContract.getLastModifiedBy());
        indexObj.put("tenantId", financialYearContract.getTenantId());

        if (financialYearContract.getCreatedDate() != null)
            indexObj.put("createdDate", formatter.format(financialYearContract.getCreatedDate()));
        else
            indexObj.put("createdDate", null);
        if (financialYearContract.getLastModifiedDate() != null)
            indexObj.put("lastModifiedDate", formatter.format(financialYearContract.getLastModifiedDate()));
        else
            indexObj.put("lastModifiedDate", null);

        return indexObj;
    }

    private HashMap<String, Object> getFiscalPeriodContractIndexObject(
            final FiscalPeriodContract fiscalPeriodContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", fiscalPeriodContract.getId());
        indexObj.put("name", fiscalPeriodContract.getName());
        indexObj.put("financialYear", fiscalPeriodContract.getFinancialYear());
        indexObj.put("isActiveForPosting", fiscalPeriodContract.getIsActiveForPosting());
        indexObj.put("isClosed", fiscalPeriodContract.getIsClosed());
        indexObj.put("active", fiscalPeriodContract.getActive());
        indexObj.put("startingDate", fiscalPeriodContract.getStartingDate());
        indexObj.put("endingDate", fiscalPeriodContract.getEndingDate());
        indexObj.put("createdBy", fiscalPeriodContract.getCreatedBy());
        indexObj.put("lastModifiedBy", fiscalPeriodContract.getLastModifiedBy());
        indexObj.put("tenantId", fiscalPeriodContract.getTenantId());

        if (fiscalPeriodContract.getCreatedDate() != null)
            indexObj.put("createdDate", formatter.format(fiscalPeriodContract.getCreatedDate()));
        else
            indexObj.put("createdDate", null);
        if (fiscalPeriodContract.getLastModifiedDate() != null)
            indexObj.put("lastModifiedDate", formatter.format(fiscalPeriodContract.getLastModifiedDate()));
        else
            indexObj.put("lastModifiedDate", null);

        return indexObj;
    }

    private HashMap<String, Object> getFunctionaryContractIndexObject(final FunctionaryContract functionaryContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", functionaryContract.getId());
        indexObj.put("code", functionaryContract.getCode());
        indexObj.put("name", functionaryContract.getName());
        indexObj.put("active", functionaryContract.getActive());
        indexObj.put("createdBy", functionaryContract.getCreatedBy());
        indexObj.put("lastModifiedBy", functionaryContract.getLastModifiedBy());
        indexObj.put("tenantId", functionaryContract.getTenantId());

        if (functionaryContract.getCreatedDate() != null)
            indexObj.put("createdDate", formatter.format(functionaryContract.getCreatedDate()));
        else
            indexObj.put("createdDate", null);
        if (functionaryContract.getLastModifiedDate() != null)
            indexObj.put("lastModifiedDate", formatter.format(functionaryContract.getLastModifiedDate()));
        else
            indexObj.put("lastModifiedDate", null);

        return indexObj;
    }

    private HashMap<String, Object> getFundsourceContractIndexObject(final FundsourceContract fundsourceContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", fundsourceContract.getId());
        indexObj.put("code", fundsourceContract.getCode());
        indexObj.put("name", fundsourceContract.getName());
        indexObj.put("fundSource", fundsourceContract.getParent());
        indexObj.put("type", fundsourceContract.getType());
        indexObj.put("llevel", fundsourceContract.getLlevel());
        indexObj.put("active", fundsourceContract.getActive());
        indexObj.put("isParent", fundsourceContract.getIsParent());
        indexObj.put("createdBy", fundsourceContract.getCreatedBy());
        indexObj.put("lastModifiedBy", fundsourceContract.getLastModifiedBy());
        indexObj.put("tenantId", fundsourceContract.getTenantId());

        if (fundsourceContract.getCreatedDate() != null)
            indexObj.put("createdDate", formatter.format(fundsourceContract.getCreatedDate()));
        else
            indexObj.put("createdDate", null);
        if (fundsourceContract.getLastModifiedDate() != null)
            indexObj.put("lastModifiedDate", formatter.format(fundsourceContract.getLastModifiedDate()));
        else
            indexObj.put("lastModifiedDate", null);

        return indexObj;
    }

    private HashMap<String, Object> getSchemeContractIndexObject(final SchemeContract schemeContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", schemeContract.getId());
        indexObj.put("code", schemeContract.getCode());
        indexObj.put("name", schemeContract.getName());
        indexObj.put("fund", schemeContract.getFund());
        indexObj.put("validFrom", schemeContract.getValidFrom());
        indexObj.put("validTo", schemeContract.getValidTo());
        indexObj.put("boundary", schemeContract.getBoundary());
        indexObj.put("description", schemeContract.getDescription());
        indexObj.put("createdBy", schemeContract.getCreatedBy());
        indexObj.put("lastModifiedBy", schemeContract.getLastModifiedBy());
        indexObj.put("tenantId", schemeContract.getTenantId());

        if (schemeContract.getCreatedDate() != null)
            indexObj.put("createdDate", formatter.format(schemeContract.getCreatedDate()));
        else
            indexObj.put("createdDate", null);
        if (schemeContract.getLastModifiedDate() != null)
            indexObj.put("lastModifiedDate", formatter.format(schemeContract.getLastModifiedDate()));
        else
            indexObj.put("lastModifiedDate", null);

        return indexObj;
    }

    private HashMap<String, Object> getSubSchemeContractIndexObject(final SubSchemeContract subSchemeContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", subSchemeContract.getId());
        indexObj.put("scheme", subSchemeContract.getScheme());
        indexObj.put("code", subSchemeContract.getCode());
        indexObj.put("name", subSchemeContract.getName());
        indexObj.put("departmentId", subSchemeContract.getDepartmentId());
        indexObj.put("validFrom", subSchemeContract.getValidFrom());
        indexObj.put("validTo", subSchemeContract.getValidTo());
        indexObj.put("createdBy", subSchemeContract.getCreatedBy());
        indexObj.put("lastModifiedBy", subSchemeContract.getLastModifiedBy());
        indexObj.put("tenantId", subSchemeContract.getTenantId());

        if (subSchemeContract.getCreatedDate() != null)
            indexObj.put("createdDate", formatter.format(subSchemeContract.getCreatedDate()));
        else
            indexObj.put("createdDate", null);
        if (subSchemeContract.getLastModifiedDate() != null)
            indexObj.put("lastModifiedDate", formatter.format(subSchemeContract.getLastModifiedDate()));
        else
            indexObj.put("lastModifiedDate", null);

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
        indexObj.put("panNo", supplierContract.getPanNo());
        indexObj.put("tinNo", supplierContract.getTinNo());
        indexObj.put("registationNo", supplierContract.getRegistationNo());
        indexObj.put("bankAccount", supplierContract.getBankAccount());
        indexObj.put("ifscCode", supplierContract.getIfscCode());
        indexObj.put("bank", supplierContract.getBank());

        indexObj.put("createdBy", supplierContract.getCreatedBy());
        indexObj.put("lastModifiedBy", supplierContract.getLastModifiedBy());
        indexObj.put("tenantId", supplierContract.getTenantId());

        if (supplierContract.getCreatedDate() != null)
            indexObj.put("createdDate", formatter.format(supplierContract.getCreatedDate()));
        else
            indexObj.put("createdDate", null);
        if (supplierContract.getLastModifiedDate() != null)
            indexObj.put("lastModifiedDate", formatter.format(supplierContract.getLastModifiedDate()));
        else
            indexObj.put("lastModifiedDate", null);

        return indexObj;
    }

    private HashMap<String, Object> getFinancialStatusContractIndexObject(
            final FinancialStatusContract financialStatusContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", financialStatusContract.getId());
        indexObj.put("moduleType", financialStatusContract.getModuleType());
        indexObj.put("code", financialStatusContract.getCode());
        indexObj.put("description", financialStatusContract.getDescription());
        indexObj.put("createdBy", financialStatusContract.getCreatedBy());
        indexObj.put("lastModifiedBy", financialStatusContract.getLastModifiedBy());
        indexObj.put("tenantId", financialStatusContract.getTenantId());

        if (financialStatusContract.getCreatedDate() != null)
            indexObj.put("createdDate", formatter.format(financialStatusContract.getCreatedDate()));
        else
            indexObj.put("createdDate", null);
        if (financialStatusContract.getLastModifiedDate() != null)
            indexObj.put("lastModifiedDate", formatter.format(financialStatusContract.getLastModifiedDate()));
        else
            indexObj.put("lastModifiedDate", null);

        return indexObj;
    }

    private HashMap<String, Object> getBankAccountIndexObject(final BankAccountContract bankAccountContract) {

        final HashMap<String, Object> indexObj = new HashMap<String, Object>();
        indexObj.put("id", bankAccountContract.getId());
        indexObj.put("bankBranch", bankAccountContract.getBankBranch());
        indexObj.put("chartOfAccount", bankAccountContract.getChartOfAccount());
        indexObj.put("fund", bankAccountContract.getFund());
        indexObj.put("accountNumber", bankAccountContract.getAccountNumber());
        indexObj.put("accountType", bankAccountContract.getAccountType());
        indexObj.put("active", bankAccountContract.getActive());
        indexObj.put("description", bankAccountContract.getDescription());
        indexObj.put("payTo", bankAccountContract.getPayTo());
        indexObj.put("type", bankAccountContract.getType());
        indexObj.put("createdBy", bankAccountContract.getCreatedBy());
        indexObj.put("lastModifiedBy", bankAccountContract.getLastModifiedBy());
        indexObj.put("tenantId", bankAccountContract.getTenantId());

        if (bankAccountContract.getCreatedDate() != null)
            indexObj.put("createdDate", formatter.format(bankAccountContract.getCreatedDate()));
        else
            indexObj.put("createdDate", null);
        if (bankAccountContract.getLastModifiedDate() != null)
            indexObj.put("lastModifiedDate", formatter.format(bankAccountContract.getLastModifiedDate()));
        else
            indexObj.put("lastModifiedDate", null);

        return indexObj;
    }

}
