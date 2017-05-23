package org.egov.egf.master.index.consumer;

import java.util.HashMap;

import org.egov.egf.master.index.domain.model.RequestContext;
import org.egov.egf.master.index.json.ObjectMapperFactory;
import org.egov.egf.master.index.persistence.queue.contract.AccountCodePurposeContract;
import org.egov.egf.master.index.persistence.queue.contract.AccountCodePurposeContractResponse;
import org.egov.egf.master.index.persistence.queue.contract.AccountDetailKeyContract;
import org.egov.egf.master.index.persistence.queue.contract.AccountDetailKeyContractResponse;
import org.egov.egf.master.index.persistence.queue.contract.AccountDetailTypeContract;
import org.egov.egf.master.index.persistence.queue.contract.AccountDetailTypeContractResponse;
import org.egov.egf.master.index.persistence.queue.contract.AccountEntityContract;
import org.egov.egf.master.index.persistence.queue.contract.AccountEntityContractResponse;
import org.egov.egf.master.index.persistence.queue.contract.BankAccountContract;
import org.egov.egf.master.index.persistence.queue.contract.BankAccountContractResponse;
import org.egov.egf.master.index.persistence.queue.contract.BankBranchContract;
import org.egov.egf.master.index.persistence.queue.contract.BankBranchContractResponse;
import org.egov.egf.master.index.persistence.queue.contract.BankContract;
import org.egov.egf.master.index.persistence.queue.contract.BankContractResponse;
import org.egov.egf.master.index.persistence.queue.contract.BudgetGroupContract;
import org.egov.egf.master.index.persistence.queue.contract.BudgetGroupContractResponse;
import org.egov.egf.master.index.persistence.queue.contract.ChartOfAccountContract;
import org.egov.egf.master.index.persistence.queue.contract.ChartOfAccountContractResponse;
import org.egov.egf.master.index.persistence.queue.contract.ChartOfAccountDetailContract;
import org.egov.egf.master.index.persistence.queue.contract.ChartOfAccountDetailContractResponse;
import org.egov.egf.master.index.persistence.queue.contract.FinancialYearContract;
import org.egov.egf.master.index.persistence.queue.contract.FinancialYearContractResponse;
import org.egov.egf.master.index.persistence.queue.contract.FiscalPeriodContract;
import org.egov.egf.master.index.persistence.queue.contract.FiscalPeriodContractResponse;
import org.egov.egf.master.index.persistence.queue.contract.FunctionContract;
import org.egov.egf.master.index.persistence.queue.contract.FunctionContractResponse;
import org.egov.egf.master.index.persistence.queue.contract.FunctionaryContract;
import org.egov.egf.master.index.persistence.queue.contract.FunctionaryContractResponse;
import org.egov.egf.master.index.persistence.queue.contract.FundContract;
import org.egov.egf.master.index.persistence.queue.contract.FundContractResponse;
import org.egov.egf.master.index.persistence.queue.contract.FundsourceContract;
import org.egov.egf.master.index.persistence.queue.contract.FundsourceContractResponse;
import org.egov.egf.master.index.persistence.queue.contract.SchemeContract;
import org.egov.egf.master.index.persistence.queue.contract.SchemeContractResponse;
import org.egov.egf.master.index.persistence.queue.contract.SubSchemeContract;
import org.egov.egf.master.index.persistence.queue.contract.SubSchemeContractResponse;
import org.egov.egf.master.index.persistence.queue.contract.SupplierContract;
import org.egov.egf.master.index.persistence.queue.contract.SupplierContractResponse;
import org.egov.egf.master.index.persistence.repository.ElasticSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

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

	@Autowired
	private ElasticSearchRepository elasticSearchRepository;

	@KafkaListener(id = "${kafka.topics.egov.index.id}", topics = "${kafka.topics.egov.index.name}", group = "${kafka.topics.egov.index.group}")
	public void listen(HashMap<String, Object> financialContractRequestMap) {

		if (financialContractRequestMap.get("Bank") != null) {

			BankContractResponse bankContractResponse;

			bankContractResponse = ObjectMapperFactory.create().convertValue(financialContractRequestMap.get("Bank"),
					BankContractResponse.class);

			if (bankContractResponse.getBanks() != null && !bankContractResponse.getBanks().isEmpty()) {
				for (BankContract bankContract : bankContractResponse.getBanks()) {
					RequestContext.setId("" + bankContract);
					elasticSearchRepository.index(BANK_OBJECT_TYPE,
							bankContract.getTenantId() + "" + bankContract.getCode(), bankContract);
				}
			} else if (bankContractResponse.getBank() != null) {
				RequestContext.setId("" + bankContractResponse.getBank());
				elasticSearchRepository.index(BANK_OBJECT_TYPE,
						bankContractResponse.getBank().getTenantId() + "" + bankContractResponse.getBank().getCode(),
						bankContractResponse.getBank());
			}
		}

		if (financialContractRequestMap.get("BankBranch") != null) {

			BankBranchContractResponse bankBranchContractResponse;

			bankBranchContractResponse = ObjectMapperFactory.create()
					.convertValue(financialContractRequestMap.get("BankBranch"), BankBranchContractResponse.class);

			if (bankBranchContractResponse.getBankBranches() != null
					&& !bankBranchContractResponse.getBankBranches().isEmpty()) {
				for (BankBranchContract bankBranchContract : bankBranchContractResponse.getBankBranches()) {
					RequestContext.setId("" + bankBranchContract);
					elasticSearchRepository.index(BANKBRANCH_OBJECT_TYPE,
							bankBranchContract.getTenantId() + "" + bankBranchContract.getCode(), bankBranchContract);
				}
			} else if (bankBranchContractResponse.getBankBranch() != null) {
				RequestContext.setId("" + bankBranchContractResponse.getBankBranch());
				elasticSearchRepository.index(BANKBRANCH_OBJECT_TYPE,
						bankBranchContractResponse.getBankBranch().getTenantId() + ""
								+ bankBranchContractResponse.getBankBranch().getCode(),
						bankBranchContractResponse.getBankBranch());
			}
		}

		if (financialContractRequestMap.get("BankAccount") != null) {

			BankAccountContractResponse bankAccountContractResponse;

			bankAccountContractResponse = ObjectMapperFactory.create()
					.convertValue(financialContractRequestMap.get("BankAccount"), BankAccountContractResponse.class);

			if (bankAccountContractResponse.getBankAccounts() != null
					&& !bankAccountContractResponse.getBankAccounts().isEmpty()) {
				for (BankAccountContract bankAccountContract : bankAccountContractResponse.getBankAccounts()) {
					RequestContext.setId("" + bankAccountContract);
					elasticSearchRepository.index(BANKACCOUNT_OBJECT_TYPE,
							bankAccountContract.getTenantId() + "" + bankAccountContract.getAccountNumber(),
							bankAccountContract);
				}
			} else if (bankAccountContractResponse.getBankAccount() != null) {
				RequestContext.setId("" + bankAccountContractResponse.getBankAccount());
				elasticSearchRepository.index(BANKACCOUNT_OBJECT_TYPE,
						bankAccountContractResponse.getBankAccount().getTenantId() + ""
								+ bankAccountContractResponse.getBankAccount().getAccountNumber(),
						bankAccountContractResponse.getBankAccount());
			}
		}

		if (financialContractRequestMap.get("AccountCodePurpose") != null) {

			AccountCodePurposeContractResponse accountCodePurposeContractResponse;

			accountCodePurposeContractResponse = ObjectMapperFactory.create().convertValue(
					financialContractRequestMap.get("AccountCodePurpose"), AccountCodePurposeContractResponse.class);

			if (accountCodePurposeContractResponse.getAccountCodePurposes() != null
					&& !accountCodePurposeContractResponse.getAccountCodePurposes().isEmpty()) {
				for (AccountCodePurposeContract accountCodePurposeContract : accountCodePurposeContractResponse
						.getAccountCodePurposes()) {
					RequestContext.setId("" + accountCodePurposeContract);
					elasticSearchRepository.index(ACCOUNTCODEPURPOSE_OBJECT_TYPE,
							accountCodePurposeContract.getTenantId() + "" + accountCodePurposeContract.getName(),
							accountCodePurposeContract);
				}
			} else if (accountCodePurposeContractResponse.getAccountCodePurpose() != null) {
				RequestContext.setId("" + accountCodePurposeContractResponse.getAccountCodePurpose());
				elasticSearchRepository.index(ACCOUNTCODEPURPOSE_OBJECT_TYPE,
						accountCodePurposeContractResponse.getAccountCodePurpose().getTenantId() + ""
								+ accountCodePurposeContractResponse.getAccountCodePurpose().getName(),
						accountCodePurposeContractResponse.getAccountCodePurpose());
			}
		}

		if (financialContractRequestMap.get("AccountDetailKey") != null) {

			AccountDetailKeyContractResponse accountDetailKeyContractResponse;

			accountDetailKeyContractResponse = ObjectMapperFactory.create().convertValue(
					financialContractRequestMap.get("AccountDetailKey"), AccountDetailKeyContractResponse.class);

			if (accountDetailKeyContractResponse.getAccountDetailKeys() != null
					&& !accountDetailKeyContractResponse.getAccountDetailKeys().isEmpty()) {
				for (AccountDetailKeyContract accountDetailKeyContract : accountDetailKeyContractResponse
						.getAccountDetailKeys()) {
					RequestContext.setId("" + accountDetailKeyContract);
					elasticSearchRepository.index(ACCOUNTDETAILKEY_OBJECT_TYPE,
							accountDetailKeyContract.getTenantId() + "" + accountDetailKeyContract.getKey(),
							accountDetailKeyContract);
				}
			} else if (accountDetailKeyContractResponse.getAccountDetailKey() != null) {
				RequestContext.setId("" + accountDetailKeyContractResponse.getAccountDetailKey());
				elasticSearchRepository.index(ACCOUNTDETAILKEY_OBJECT_TYPE,
						accountDetailKeyContractResponse.getAccountDetailKey().getTenantId() + ""
								+ accountDetailKeyContractResponse.getAccountDetailKey().getKey(),
						accountDetailKeyContractResponse.getAccountDetailKey());
			}
		}

		if (financialContractRequestMap.get("AccountDetailType") != null) {

			AccountDetailTypeContractResponse accountDetailTypeContractResponse;

			accountDetailTypeContractResponse = ObjectMapperFactory.create().convertValue(
					financialContractRequestMap.get("AccountDetailType"), AccountDetailTypeContractResponse.class);

			if (accountDetailTypeContractResponse.getAccountDetailTypes() != null
					&& !accountDetailTypeContractResponse.getAccountDetailTypes().isEmpty()) {
				for (AccountDetailTypeContract accountDetailTypeContract : accountDetailTypeContractResponse
						.getAccountDetailTypes()) {
					RequestContext.setId("" + accountDetailTypeContract);
					elasticSearchRepository.index(ACCOUNTDETAILTYPE_OBJECT_TYPE,
							accountDetailTypeContract.getTenantId() + "" + accountDetailTypeContract.getName(),
							accountDetailTypeContract);
				}
			} else if (accountDetailTypeContractResponse.getAccountDetailType() != null) {
				RequestContext.setId("" + accountDetailTypeContractResponse.getAccountDetailType());
				elasticSearchRepository.index(ACCOUNTDETAILTYPE_OBJECT_TYPE,
						accountDetailTypeContractResponse.getAccountDetailType().getTenantId() + ""
								+ accountDetailTypeContractResponse.getAccountDetailType().getName(),
						accountDetailTypeContractResponse.getAccountDetailType());
			}
		}

		if (financialContractRequestMap.get("AccountEntity") != null) {

			AccountEntityContractResponse accountEntityContractResponse;

			accountEntityContractResponse = ObjectMapperFactory.create().convertValue(
					financialContractRequestMap.get("AccountEntity"), AccountEntityContractResponse.class);

			if (accountEntityContractResponse.getAccountEntities() != null
					&& !accountEntityContractResponse.getAccountEntities().isEmpty()) {
				for (AccountEntityContract accountEntityContract : accountEntityContractResponse.getAccountEntities()) {
					RequestContext.setId("" + accountEntityContract);
					elasticSearchRepository.index(ACCOUNTENTITY_OBJECT_TYPE,
							accountEntityContract.getTenantId() + "" + accountEntityContract.getName(),
							accountEntityContract);
				}
			} else if (accountEntityContractResponse.getAccountEntity() != null) {
				RequestContext.setId("" + accountEntityContractResponse.getAccountEntity());
				elasticSearchRepository.index(ACCOUNTENTITY_OBJECT_TYPE,
						accountEntityContractResponse.getAccountEntity().getTenantId() + ""
								+ accountEntityContractResponse.getAccountEntity().getName(),
						accountEntityContractResponse.getAccountEntity());
			}
		}

		if (financialContractRequestMap.get("BudgetGroup") != null) {

			BudgetGroupContractResponse budgetGroupContractResponse;

			budgetGroupContractResponse = ObjectMapperFactory.create()
					.convertValue(financialContractRequestMap.get("BudgetGroup"), BudgetGroupContractResponse.class);

			if (budgetGroupContractResponse.getBudgetGroups() != null
					&& !budgetGroupContractResponse.getBudgetGroups().isEmpty()) {
				for (BudgetGroupContract budgetGroupContract : budgetGroupContractResponse.getBudgetGroups()) {
					RequestContext.setId("" + budgetGroupContract);
					elasticSearchRepository.index(BUDGETGROUP_OBJECT_TYPE,
							budgetGroupContract.getTenantId() + "" + budgetGroupContract.getName(),
							budgetGroupContract);
				}
			} else if (budgetGroupContractResponse.getBudgetGroup() != null) {
				RequestContext.setId("" + budgetGroupContractResponse.getBudgetGroup());
				elasticSearchRepository.index(BUDGETGROUP_OBJECT_TYPE,
						budgetGroupContractResponse.getBudgetGroup().getTenantId() + ""
								+ budgetGroupContractResponse.getBudgetGroup().getName(),
						budgetGroupContractResponse.getBudgetGroup());
			}
		}

		if (financialContractRequestMap.get("ChartOfAccount") != null) {

			ChartOfAccountContractResponse chartOfAccountContractResponse;

			chartOfAccountContractResponse = ObjectMapperFactory.create().convertValue(
					financialContractRequestMap.get("ChartOfAccount"), ChartOfAccountContractResponse.class);

			if (chartOfAccountContractResponse.getChartOfAccounts() != null
					&& !chartOfAccountContractResponse.getChartOfAccounts().isEmpty()) {
				for (ChartOfAccountContract chartOfAccountContract : chartOfAccountContractResponse
						.getChartOfAccounts()) {
					RequestContext.setId("" + chartOfAccountContract);
					elasticSearchRepository.index(CHARTOFACCOUNT_OBJECT_TYPE,
							chartOfAccountContract.getTenantId() + "" + chartOfAccountContract.getName(),
							chartOfAccountContract);
				}
			} else if (chartOfAccountContractResponse.getChartOfAccount() != null) {
				RequestContext.setId("" + chartOfAccountContractResponse.getChartOfAccount());
				elasticSearchRepository.index(CHARTOFACCOUNT_OBJECT_TYPE,
						chartOfAccountContractResponse.getChartOfAccount().getTenantId() + ""
								+ chartOfAccountContractResponse.getChartOfAccount().getName(),
						chartOfAccountContractResponse.getChartOfAccount());
			}
		}

		if (financialContractRequestMap.get("ChartOfAccountDetail") != null) {

			ChartOfAccountDetailContractResponse chartOfAccountDetailContractResponse;

			chartOfAccountDetailContractResponse = ObjectMapperFactory.create().convertValue(
					financialContractRequestMap.get("ChartOfAccountDetail"),
					ChartOfAccountDetailContractResponse.class);

			if (chartOfAccountDetailContractResponse.getChartOfAccountDetails() != null
					&& !chartOfAccountDetailContractResponse.getChartOfAccountDetails().isEmpty()) {
				for (ChartOfAccountDetailContract chartOfAccountDetailContract : chartOfAccountDetailContractResponse
						.getChartOfAccountDetails()) {
					RequestContext.setId("" + chartOfAccountDetailContract);
					elasticSearchRepository
							.index(CHARTOFACCOUNTDETAIL_OBJECT_TYPE,
									chartOfAccountDetailContract.getTenantId() + ""
											+ chartOfAccountDetailContract.getChartOfAccount(),
									chartOfAccountDetailContract);
				}
			} else if (chartOfAccountDetailContractResponse.getChartOfAccountDetail() != null) {
				RequestContext.setId("" + chartOfAccountDetailContractResponse.getChartOfAccountDetail());
				elasticSearchRepository.index(CHARTOFACCOUNTDETAIL_OBJECT_TYPE,
						chartOfAccountDetailContractResponse.getChartOfAccountDetail().getTenantId() + ""
								+ chartOfAccountDetailContractResponse.getChartOfAccountDetail().getChartOfAccount(),
						chartOfAccountDetailContractResponse.getChartOfAccountDetail());
			}
		}

		if (financialContractRequestMap.get("FinancialYear") != null) {

			FinancialYearContractResponse financialYearContractResponse;

			financialYearContractResponse = ObjectMapperFactory.create().convertValue(
					financialContractRequestMap.get("FinancialYear"), FinancialYearContractResponse.class);

			if (financialYearContractResponse.getFinancialYears() != null
					&& !financialYearContractResponse.getFinancialYears().isEmpty()) {
				for (FinancialYearContract financialYearContract : financialYearContractResponse.getFinancialYears()) {
					RequestContext.setId("" + financialYearContract);
					elasticSearchRepository.index(FINANCIALYEAR_OBJECT_TYPE,
							financialYearContract.getTenantId() + "" + financialYearContract.getFinYearRange(),
							financialYearContract);
				}
			} else if (financialYearContractResponse.getFinancialYear() != null) {
				RequestContext.setId("" + financialYearContractResponse.getFinancialYear());
				elasticSearchRepository.index(FINANCIALYEAR_OBJECT_TYPE,
						financialYearContractResponse.getFinancialYear().getTenantId() + ""
								+ financialYearContractResponse.getFinancialYear().getFinYearRange(),
						financialYearContractResponse.getFinancialYear());
			}
		}

		if (financialContractRequestMap.get("FiscalPeriod") != null) {

			FiscalPeriodContractResponse fiscalPeriodContractResponse;

			fiscalPeriodContractResponse = ObjectMapperFactory.create()
					.convertValue(financialContractRequestMap.get("FiscalPeriod"), FiscalPeriodContractResponse.class);

			if (fiscalPeriodContractResponse.getFiscalPeriods() != null
					&& !fiscalPeriodContractResponse.getFiscalPeriods().isEmpty()) {
				for (FiscalPeriodContract fiscalPeriodContract : fiscalPeriodContractResponse.getFiscalPeriods()) {
					RequestContext.setId("" + fiscalPeriodContract);
					elasticSearchRepository.index(FISCALPERIOD_OBJECT_TYPE, fiscalPeriodContract.getTenantId() + ""
							+ fiscalPeriodContract.getStartingDate() + "" + fiscalPeriodContract.getEndingDate(),
							fiscalPeriodContract);
				}
			} else if (fiscalPeriodContractResponse.getFiscalPeriod() != null) {
				RequestContext.setId("" + fiscalPeriodContractResponse.getFiscalPeriod());
				elasticSearchRepository.index(FISCALPERIOD_OBJECT_TYPE,
						fiscalPeriodContractResponse.getFiscalPeriod().getTenantId() + ""
								+ fiscalPeriodContractResponse.getFiscalPeriod().getStartingDate() + ""
								+ fiscalPeriodContractResponse.getFiscalPeriod().getEndingDate(),
						fiscalPeriodContractResponse.getFiscalPeriod());
			}
		}

		if (financialContractRequestMap.get("Functionary") != null) {

			FunctionaryContractResponse functionaryContractResponse;

			functionaryContractResponse = ObjectMapperFactory.create()
					.convertValue(financialContractRequestMap.get("Functionary"), FunctionaryContractResponse.class);

			if (functionaryContractResponse.getFunctionaries() != null
					&& !functionaryContractResponse.getFunctionaries().isEmpty()) {
				for (FunctionaryContract functionaryContract : functionaryContractResponse.getFunctionaries()) {
					RequestContext.setId("" + functionaryContract);
					elasticSearchRepository.index(FUNCTIONARY_OBJECT_TYPE,
							functionaryContract.getTenantId() + "" + functionaryContract.getName(),
							functionaryContract);
				}
			} else if (functionaryContractResponse.getFunctionary() != null) {
				RequestContext.setId("" + functionaryContractResponse.getFunctionary());
				elasticSearchRepository.index(FUNCTIONARY_OBJECT_TYPE,
						functionaryContractResponse.getFunctionary().getTenantId() + ""
								+ functionaryContractResponse.getFunctionary().getName(),
						functionaryContractResponse.getFunctionary());
			}
		}

		if (financialContractRequestMap.get("Function") != null) {

			FunctionContractResponse functionContractResponse;

			functionContractResponse = ObjectMapperFactory.create()
					.convertValue(financialContractRequestMap.get("Function"), FunctionContractResponse.class);

			if (functionContractResponse.getFunctions() != null && !functionContractResponse.getFunctions().isEmpty()) {
				for (FunctionContract functionContract : functionContractResponse.getFunctions()) {
					RequestContext.setId("" + functionContract);
					elasticSearchRepository.index(FUNCTION_OBJECT_TYPE,
							functionContract.getTenantId() + "" + functionContract.getName(), functionContract);
				}
			} else if (functionContractResponse.getFunction() != null) {
				RequestContext.setId("" + functionContractResponse.getFunction());
				elasticSearchRepository.index(FUNCTION_OBJECT_TYPE,
						functionContractResponse.getFunction().getTenantId() + ""
								+ functionContractResponse.getFunction().getName(),
						functionContractResponse.getFunction());
			}
		}

		if (financialContractRequestMap.get("Fund") != null) {

			FundContractResponse fundContractResponse;

			fundContractResponse = ObjectMapperFactory.create().convertValue(financialContractRequestMap.get("Fund"),
					FundContractResponse.class);

			if (fundContractResponse.getFunds() != null && !fundContractResponse.getFunds().isEmpty()) {
				for (FundContract fundContract : fundContractResponse.getFunds()) {
					RequestContext.setId("" + fundContract);
					elasticSearchRepository.index(FUND_OBJECT_TYPE,
							fundContract.getTenantId() + "" + fundContract.getName(), fundContract);
				}
			} else if (fundContractResponse.getFund() != null) {
				RequestContext.setId("" + fundContractResponse.getFund());
				elasticSearchRepository.index(FUND_OBJECT_TYPE,
						fundContractResponse.getFund().getTenantId() + "" + fundContractResponse.getFund().getName(),
						fundContractResponse.getFund());
			}
		}

		if (financialContractRequestMap.get("FundSource") != null) {

			FundsourceContractResponse fundsourceContractResponse;

			fundsourceContractResponse = ObjectMapperFactory.create()
					.convertValue(financialContractRequestMap.get("FundSource"), FundsourceContractResponse.class);

			if (fundsourceContractResponse.getFundsources() != null
					&& !fundsourceContractResponse.getFundsources().isEmpty()) {
				for (FundsourceContract fundsourceContract : fundsourceContractResponse.getFundsources()) {
					RequestContext.setId("" + fundsourceContract);
					elasticSearchRepository.index(FUNDSOURCE_OBJECT_TYPE,
							fundsourceContract.getTenantId() + "" + fundsourceContract.getName(), fundsourceContract);
				}
			} else if (fundsourceContractResponse.getFundsource() != null) {
				RequestContext.setId("" + fundsourceContractResponse.getFundsource());
				elasticSearchRepository.index(FUNDSOURCE_OBJECT_TYPE,
						fundsourceContractResponse.getFundsource().getTenantId() + ""
								+ fundsourceContractResponse.getFundsource().getName(),
						fundsourceContractResponse.getFundsource());
			}
		}

		if (financialContractRequestMap.get("Scheme") != null) {

			SchemeContractResponse schemeContractResponse;

			schemeContractResponse = ObjectMapperFactory.create()
					.convertValue(financialContractRequestMap.get("Scheme"), SchemeContractResponse.class);

			if (schemeContractResponse.getSchemes() != null && !schemeContractResponse.getSchemes().isEmpty()) {
				for (SchemeContract schemeContract : schemeContractResponse.getSchemes()) {
					RequestContext.setId("" + schemeContract);
					elasticSearchRepository.index(SCHEME_OBJECT_TYPE,
							schemeContract.getTenantId() + "" + schemeContract.getName(), schemeContract);
				}
			} else if (schemeContractResponse.getScheme() != null) {
				RequestContext.setId("" + schemeContractResponse.getScheme());
				elasticSearchRepository
						.index(SCHEME_OBJECT_TYPE,
								schemeContractResponse.getScheme().getTenantId() + ""
										+ schemeContractResponse.getScheme().getName(),
								schemeContractResponse.getScheme());
			}
		}

		if (financialContractRequestMap.get("SubScheme") != null) {

			SubSchemeContractResponse subSchemeContractResponse;

			subSchemeContractResponse = ObjectMapperFactory.create()
					.convertValue(financialContractRequestMap.get("SubScheme"), SubSchemeContractResponse.class);

			if (subSchemeContractResponse.getSubSchemes() != null
					&& !subSchemeContractResponse.getSubSchemes().isEmpty()) {
				for (SubSchemeContract subSchemeContract : subSchemeContractResponse.getSubSchemes()) {
					RequestContext.setId("" + subSchemeContract);
					elasticSearchRepository.index(SUBSCHEME_OBJECT_TYPE,
							subSchemeContract.getTenantId() + "" + subSchemeContract.getName(), subSchemeContract);
				}
			} else if (subSchemeContractResponse.getSubScheme() != null) {
				RequestContext.setId("" + subSchemeContractResponse.getSubScheme());
				elasticSearchRepository.index(SUBSCHEME_OBJECT_TYPE,
						subSchemeContractResponse.getSubScheme().getTenantId() + ""
								+ subSchemeContractResponse.getSubScheme().getName(),
						subSchemeContractResponse.getSubScheme());
			}
		}

		if (financialContractRequestMap.get("Supplier") != null) {

			SupplierContractResponse supplierContractResponse;

			supplierContractResponse = ObjectMapperFactory.create()
					.convertValue(financialContractRequestMap.get("Supplier"), SupplierContractResponse.class);

			if (supplierContractResponse.getSuppliers() != null && !supplierContractResponse.getSuppliers().isEmpty()) {
				for (SupplierContract supplierContract : supplierContractResponse.getSuppliers()) {
					RequestContext.setId("" + supplierContract);
					elasticSearchRepository.index(SUPPLIER_OBJECT_TYPE,
							supplierContract.getTenantId() + "" + supplierContract.getName(), supplierContract);
				}
			} else if (supplierContractResponse.getSupplier() != null) {
				RequestContext.setId("" + supplierContractResponse.getSupplier());
				elasticSearchRepository.index(SUPPLIER_OBJECT_TYPE,
						supplierContractResponse.getSupplier().getTenantId() + ""
								+ supplierContractResponse.getSupplier().getName(),
						supplierContractResponse.getSupplier());
			}
		}

	}

}
