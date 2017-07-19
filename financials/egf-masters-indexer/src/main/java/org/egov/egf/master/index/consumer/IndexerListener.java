package org.egov.egf.master.index.consumer;

import java.util.HashMap;

import org.egov.common.web.contract.CommonRequest;
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
import org.egov.egf.master.web.contract.EgfStatusContract;
import org.egov.egf.master.web.contract.FinancialYearContract;
import org.egov.egf.master.web.contract.FiscalPeriodContract;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.contract.FunctionaryContract;
import org.egov.egf.master.web.contract.FundContract;
import org.egov.egf.master.web.contract.FundsourceContract;
import org.egov.egf.master.web.contract.SchemeContract;
import org.egov.egf.master.web.contract.SubSchemeContract;
import org.egov.egf.master.web.contract.SupplierContract;
import org.egov.tracer.model.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
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
	private static final String EGFSTATUSCONTRACT_OBJECT_TYPE = "egfstatuscontract";

	@Autowired
	private ElasticSearchRepository elasticSearchRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@KafkaListener(id = "${kafka.topics.egov.index.id}", topics = "${kafka.topics.egov.index.name}", group = "${kafka.topics.egov.index.group}")
	public void listen(HashMap<String, Object> financialContractRequestMap) {

		if (financialContractRequestMap.get("fundcontract_persisted") != null) {

			CommonRequest<FundContract> request = objectMapper.convertValue(
					financialContractRequestMap.get("fundcontract_persisted"),
					new TypeReference<CommonRequest<FundContract>>() {
					});

			if (request.getData() != null && !request.getData().isEmpty()) {
				for (FundContract fundContract : request.getData()) {
					RequestContext.setId("" + fundContract);
					elasticSearchRepository.index(FUND_OBJECT_TYPE,
							fundContract.getTenantId() + "-" + fundContract.getName(), fundContract);
				}
			}
		}

		if (financialContractRequestMap.get("bankcontract_persisted") != null) {

			CommonRequest<BankContract> request = objectMapper.convertValue(
					financialContractRequestMap.get("bankcontract_persisted"),
					new TypeReference<CommonRequest<BankContract>>() {
					});

			if (request.getData() != null && !request.getData().isEmpty()) {
				for (BankContract bankContract : request.getData()) {
					RequestContext.setId("" + bankContract);
					elasticSearchRepository.index(BANK_OBJECT_TYPE,
							bankContract.getTenantId() + "-" + bankContract.getName(), bankContract);
				}
			}
		}

		if (financialContractRequestMap.get("functioncontract_persisted") != null) {

			CommonRequest<FunctionContract> request = objectMapper.convertValue(
					financialContractRequestMap.get("functioncontract_persisted"),
					new TypeReference<CommonRequest<FunctionContract>>() {
					});

			if (request.getData() != null && !request.getData().isEmpty()) {
				for (FunctionContract functionContract : request.getData()) {
					RequestContext.setId("" + functionContract);
					elasticSearchRepository.index(FUNCTION_OBJECT_TYPE,
							functionContract.getTenantId() + "-" + functionContract.getName(), functionContract);
				}
			}
		}

		if (financialContractRequestMap.get("bankBranchcontract_persisted") != null) {

			CommonRequest<BankBranchContract> request = objectMapper.convertValue(
					financialContractRequestMap.get("bankBranchcontract_persisted"),
					new TypeReference<CommonRequest<BankBranchContract>>() {
					});

			if (request.getData() != null && !request.getData().isEmpty()) {
				for (BankBranchContract bankBranchContract : request.getData()) {
					RequestContext.setId("" + bankBranchContract);
					elasticSearchRepository.index(BANKBRANCH_OBJECT_TYPE,
							bankBranchContract.getTenantId() + "-" + bankBranchContract.getName(), bankBranchContract);
				}
			}
		}

		if (financialContractRequestMap.get("bankaccountcontract_persisted") != null) {

			CommonRequest<BankAccountContract> request = objectMapper.convertValue(
					financialContractRequestMap.get("bankaccountcontract_persisted"),
					new TypeReference<CommonRequest<BankAccountContract>>() {
					});

			if (request.getData() != null && !request.getData().isEmpty()) {
				for (BankAccountContract bankAccountContract : request.getData()) {
					RequestContext.setId("" + bankAccountContract);
					elasticSearchRepository.index(BANKACCOUNT_OBJECT_TYPE,
							bankAccountContract.getTenantId() + "-" + bankAccountContract.getAccountNumber(),
							bankAccountContract);
				}
			}
		}

		if (financialContractRequestMap.get("accountcodepurposecontract_persisted") != null) {

			CommonRequest<AccountCodePurposeContract> request = objectMapper.convertValue(
					financialContractRequestMap.get("accountcodepurposecontract_persisted"),
					new TypeReference<CommonRequest<AccountCodePurposeContract>>() {
					});

			if (request.getData() != null && !request.getData().isEmpty()) {
				for (AccountCodePurposeContract accountCodePurposeContract : request.getData()) {
					RequestContext.setId("" + accountCodePurposeContract);
					elasticSearchRepository.index(ACCOUNTCODEPURPOSE_OBJECT_TYPE,
							accountCodePurposeContract.getTenantId() + "-" + accountCodePurposeContract.getName(),
							accountCodePurposeContract);
				}
			}
		}

		if (financialContractRequestMap.get("accountdetailtypecontract_persisted") != null) {

			CommonRequest<AccountDetailTypeContract> request = objectMapper.convertValue(
					financialContractRequestMap.get("accountdetailtypecontract_persisted"),
					new TypeReference<CommonRequest<AccountDetailTypeContract>>() {
					});

			if (request.getData() != null && !request.getData().isEmpty()) {
				for (AccountDetailTypeContract accountDetailTypeContract : request.getData()) {
					RequestContext.setId("" + accountDetailTypeContract);
					elasticSearchRepository.index(ACCOUNTDETAILTYPE_OBJECT_TYPE,
							accountDetailTypeContract.getTenantId() + "-" + accountDetailTypeContract.getName(),
							accountDetailTypeContract);
				}
			}
		}

		if (financialContractRequestMap.get("accountdetailkeycontract_persisted") != null) {

			CommonRequest<AccountDetailKeyContract> request = objectMapper.convertValue(
					financialContractRequestMap.get("accountdetailkeycontract_persisted"),
					new TypeReference<CommonRequest<AccountDetailKeyContract>>() {
					});

			if (request.getData() != null && !request.getData().isEmpty()) {
				for (AccountDetailKeyContract accountDetailKeyContract : request.getData()) {
					RequestContext.setId("" + accountDetailKeyContract);
					elasticSearchRepository.index(ACCOUNTDETAILKEY_OBJECT_TYPE,
							accountDetailKeyContract.getTenantId() + "-" + accountDetailKeyContract.getKey(),
							accountDetailKeyContract);
				}
			}
		}

		if (financialContractRequestMap.get("accountentitycontract_persisted") != null) {

			CommonRequest<AccountEntityContract> request = objectMapper.convertValue(
					financialContractRequestMap.get("accountentitycontract_persisted"),
					new TypeReference<CommonRequest<AccountEntityContract>>() {
					});

			if (request.getData() != null && !request.getData().isEmpty()) {
				for (AccountEntityContract accountEntityContract : request.getData()) {
					RequestContext.setId("" + accountEntityContract);
					elasticSearchRepository.index(ACCOUNTENTITY_OBJECT_TYPE,
							accountEntityContract.getTenantId() + "-" + accountEntityContract.getName(),
							accountEntityContract);
				}
			}
		}

		if (financialContractRequestMap.get("budgetgroupcontract_persisted") != null) {

			CommonRequest<BudgetGroupContract> request = objectMapper.convertValue(
					financialContractRequestMap.get("budgetgroupcontract_persisted"),
					new TypeReference<CommonRequest<BudgetGroupContract>>() {
					});

			if (request.getData() != null && !request.getData().isEmpty()) {
				for (BudgetGroupContract budgetGroupContract : request.getData()) {
					RequestContext.setId("" + budgetGroupContract);
					elasticSearchRepository.index(BUDGETGROUP_OBJECT_TYPE,
							budgetGroupContract.getTenantId() + "-" + budgetGroupContract.getName(),
							budgetGroupContract);
				}
			}
		}

		if (financialContractRequestMap.get("chartofaccountcontract_persisted") != null) {

			CommonRequest<ChartOfAccountContract> request = objectMapper.convertValue(
					financialContractRequestMap.get("chartofaccountcontract_persisted"),
					new TypeReference<CommonRequest<ChartOfAccountContract>>() {
					});

			if (request.getData() != null && !request.getData().isEmpty()) {
				for (ChartOfAccountContract chartOfAccountContract : request.getData()) {
					RequestContext.setId("" + chartOfAccountContract);
					elasticSearchRepository.index(CHARTOFACCOUNT_OBJECT_TYPE,
							chartOfAccountContract.getTenantId() + "-" + chartOfAccountContract.getGlcode(),
							chartOfAccountContract);
				}
			}
		}

		if (financialContractRequestMap.get("chartofaccountdetailcontract_persisted") != null) {

			CommonRequest<ChartOfAccountDetailContract> request = objectMapper.convertValue(
					financialContractRequestMap.get("chartofaccountdetailcontract_persisted"),
					new TypeReference<CommonRequest<ChartOfAccountDetailContract>>() {
					});

			if (request.getData() != null && !request.getData().isEmpty()) {
				for (ChartOfAccountDetailContract chartOfAccountDetailContract : request.getData()) {
					RequestContext.setId("" + chartOfAccountDetailContract);
					elasticSearchRepository.index(CHARTOFACCOUNTDETAIL_OBJECT_TYPE,
							chartOfAccountDetailContract.getTenantId() + "-"
									+ chartOfAccountDetailContract.getChartOfAccount().getGlcode(),
							chartOfAccountDetailContract);
				}
			}
		}

		if (financialContractRequestMap.get("financialyearcontract_persisted") != null) {

			CommonRequest<FinancialYearContract> request = objectMapper.convertValue(
					financialContractRequestMap.get("financialyearcontract_persisted"),
					new TypeReference<CommonRequest<FinancialYearContract>>() {
					});

			if (request.getData() != null && !request.getData().isEmpty()) {
				for (FinancialYearContract financialYearContract : request.getData()) {
					RequestContext.setId("" + financialYearContract);
					elasticSearchRepository.index(FINANCIALYEAR_OBJECT_TYPE,
							financialYearContract.getTenantId() + "-" + financialYearContract.getFinYearRange(),
							financialYearContract);
				}
			}
		}

		if (financialContractRequestMap.get("fiscalperiodcontract_persisted") != null) {

			CommonRequest<FiscalPeriodContract> request = objectMapper.convertValue(
					financialContractRequestMap.get("fiscalperiodcontract_persisted"),
					new TypeReference<CommonRequest<FiscalPeriodContract>>() {
					});

			if (request.getData() != null && !request.getData().isEmpty()) {
				for (FiscalPeriodContract fiscalPeriodContract : request.getData()) {
					RequestContext.setId("" + fiscalPeriodContract);
					elasticSearchRepository.index(FISCALPERIOD_OBJECT_TYPE,
							fiscalPeriodContract.getTenantId() + "-" + fiscalPeriodContract.getName(),
							fiscalPeriodContract);
				}
			}
		}

		if (financialContractRequestMap.get("functionarycontract_persisted") != null) {

			CommonRequest<FunctionaryContract> request = objectMapper.convertValue(
					financialContractRequestMap.get("functionarycontract_persisted"),
					new TypeReference<CommonRequest<FunctionaryContract>>() {
					});

			if (request.getData() != null && !request.getData().isEmpty()) {
				for (FunctionaryContract functionaryContract : request.getData()) {
					RequestContext.setId("" + functionaryContract);
					elasticSearchRepository.index(FUNCTIONARY_OBJECT_TYPE,
							functionaryContract.getTenantId() + "-" + functionaryContract.getName(),
							functionaryContract);
				}
			}
		}

		if (financialContractRequestMap.get("fundsourcecontract_persisted") != null) {

			CommonRequest<FundsourceContract> request = objectMapper.convertValue(
					financialContractRequestMap.get("fundsourcecontract_persisted"),
					new TypeReference<CommonRequest<FundsourceContract>>() {
					});

			if (request.getData() != null && !request.getData().isEmpty()) {
				for (FundsourceContract fundsourceContract : request.getData()) {
					RequestContext.setId("" + fundsourceContract);
					elasticSearchRepository.index(FUNDSOURCE_OBJECT_TYPE,
							fundsourceContract.getTenantId() + "-" + fundsourceContract.getName(), fundsourceContract);
				}
			}
		}

		if (financialContractRequestMap.get("schemecontract_persisted") != null) {

			CommonRequest<SchemeContract> request = objectMapper.convertValue(
					financialContractRequestMap.get("schemecontract_persisted"),
					new TypeReference<CommonRequest<SchemeContract>>() {
					});

			if (request.getData() != null && !request.getData().isEmpty()) {
				for (SchemeContract schemeContract : request.getData()) {
					RequestContext.setId("" + schemeContract);
					elasticSearchRepository.index(SCHEME_OBJECT_TYPE,
							schemeContract.getTenantId() + "-" + schemeContract.getName(), schemeContract);
				}
			}
		}

		if (financialContractRequestMap.get("subschemecontract_persisted") != null) {

			CommonRequest<SubSchemeContract> request = objectMapper.convertValue(
					financialContractRequestMap.get("subschemecontract_persisted"),
					new TypeReference<CommonRequest<SubSchemeContract>>() {
					});

			if (request.getData() != null && !request.getData().isEmpty()) {
				for (SubSchemeContract subSchemeContract : request.getData()) {
					RequestContext.setId("" + subSchemeContract);
					elasticSearchRepository.index(SUBSCHEME_OBJECT_TYPE,
							subSchemeContract.getTenantId() + "-" + subSchemeContract.getName(), subSchemeContract);
				}
			}
		}

		if (financialContractRequestMap.get("suppliercontract_persisted") != null) {

			CommonRequest<SupplierContract> request = objectMapper.convertValue(
					financialContractRequestMap.get("suppliercontract_persisted"),
					new TypeReference<CommonRequest<SupplierContract>>() {
					});

			if (request.getData() != null && !request.getData().isEmpty()) {
				for (SupplierContract supplierContract : request.getData()) {
					RequestContext.setId("" + supplierContract);
					elasticSearchRepository.index(SUPPLIER_OBJECT_TYPE,
							supplierContract.getTenantId() + "-" + supplierContract.getName(), supplierContract);
				}
			}
		}

		if (financialContractRequestMap.get("egfstatuscontract_persisted") != null) {

			CommonRequest<EgfStatusContract> request = objectMapper.convertValue(
					financialContractRequestMap.get("egfstatuscontract_persisted"),
					new TypeReference<CommonRequest<EgfStatusContract>>() {
					});

			if (request.getData() != null && !request.getData().isEmpty()) {
				for (EgfStatusContract egfStatusContract : request.getData()) {
					RequestContext.setId("" + egfStatusContract);
					elasticSearchRepository.index(EGFSTATUSCONTRACT_OBJECT_TYPE,
							egfStatusContract.getTenantId() + "-" + egfStatusContract.getCode(), egfStatusContract);
				}
			}
		}

	}

}
