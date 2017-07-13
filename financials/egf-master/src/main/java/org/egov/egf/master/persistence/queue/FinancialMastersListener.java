package org.egov.egf.master.persistence.queue;

import java.util.HashMap;

import org.egov.common.web.contract.CommonRequest;
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
import org.egov.egf.master.domain.model.EgfStatus;
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
import org.egov.egf.master.domain.service.EgfStatusService;
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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FinancialMastersListener {

	@Value("${kafka.topics.egf.masters.completed.topic}")
	private String completedTopic;

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
	private EgfStatusService egfStatusService;

	@KafkaListener(id = "${kafka.topics.egf.masters.validated.id}", topics = "${kafka.topics.egf.masters.validated.topic}", group = "${kafka.topics.egf.masters.validated.group}")
	public void process(HashMap<String, CommonRequest<?>> mastersMap) {

		// CommonRequest request =(CommonRequest)
		// mastersMap.values().toArray()[0];
		System.out.println("consuming topic" + mastersMap);

		if (mastersMap.get("fundcontract_create") != null) {

			CommonRequest<FundContract> request = objectMapper.convertValue(mastersMap.get("fundcontract_create"),
					new TypeReference<CommonRequest<FundContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (FundContract fundContract : request.getData()) {
				Fund domain = mapper.map(fundContract, Fund.class);
				fundService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("FundContract_Completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("fundcontract_update") != null) {

			CommonRequest<FundContract> request = objectMapper.convertValue(mastersMap.get("fundcontract_update"),
					new TypeReference<CommonRequest<FundContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (FundContract fundContract : request.getData()) {
				Fund domain = mapper.map(fundContract, Fund.class);
				fundService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("FundContract_Completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}

		if (mastersMap.get("bankcontract_create") != null) {

			CommonRequest<BankContract> request = objectMapper.convertValue(mastersMap.get("bankcontract_create"),
					new TypeReference<CommonRequest<BankContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (BankContract bankContract : request.getData()) {
				Bank domain = mapper.map(bankContract, Bank.class);
				bankService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("bankcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("bankcontract_update") != null)

		{

			CommonRequest<BankContract> request = objectMapper.convertValue(mastersMap.get("bankcontract_update"),
					new TypeReference<CommonRequest<BankContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (BankContract bankContract : request.getData()) {
				Bank domain = mapper.map(bankContract, Bank.class);
				bankService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("bankcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}

		if (mastersMap.get("functioncontract_create") != null) {

			CommonRequest<FunctionContract> request = objectMapper.convertValue(
					mastersMap.get("functioncontract_create"), new TypeReference<CommonRequest<FunctionContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (FunctionContract functionContract : request.getData()) {
				Function domain = mapper.map(functionContract, Function.class);
				functionService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("functioncontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("functioncontract_update") != null) {

			CommonRequest<FunctionContract> request = objectMapper.convertValue(
					mastersMap.get("functioncontract_update"), new TypeReference<CommonRequest<FunctionContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (FunctionContract functionContract : request.getData()) {
				Function domain = mapper.map(functionContract, Function.class);
				functionService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("functioncontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}

		if (mastersMap.get("bankbranchcontract_create") != null) {

			CommonRequest<BankBranchContract> request = objectMapper.convertValue(
					mastersMap.get("bankbranchcontract_create"),
					new TypeReference<CommonRequest<BankBranchContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (BankBranchContract bankBranchContract : request.getData()) {
				BankBranch domain = mapper.map(bankBranchContract, BankBranch.class);
				bankBranchService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("bankBranchcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("bankbranchcontract_update") != null) {

			CommonRequest<BankBranchContract> request = objectMapper.convertValue(
					mastersMap.get("bankbranchcontract_update"),
					new TypeReference<CommonRequest<BankBranchContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (BankBranchContract bankBranchContract : request.getData()) {
				BankBranch domain = mapper.map(bankBranchContract, BankBranch.class);
				bankBranchService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("bankBranchcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}

		if (mastersMap.get("bankaccountcontract_create") != null) {

			CommonRequest<BankAccountContract> request = objectMapper.convertValue(
					mastersMap.get("bankaccountcontract_create"),
					new TypeReference<CommonRequest<BankAccountContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (BankAccountContract bankAccountContract : request.getData()) {
				BankAccount domain = mapper.map(bankAccountContract, BankAccount.class);
				bankAccountService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("bankAccountcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("bankaccountcontract_update") != null) {

			CommonRequest<BankAccountContract> request = objectMapper.convertValue(
					mastersMap.get("bankaccountcontract_update"),
					new TypeReference<CommonRequest<BankAccountContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (BankAccountContract bankAccountContract : request.getData()) {
				BankAccount domain = mapper.map(bankAccountContract, BankAccount.class);
				bankAccountService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("bankAccountcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("accountcodepurposecontract_create") != null) {

			CommonRequest<AccountCodePurposeContract> request = objectMapper.convertValue(
					mastersMap.get("accountcodepurposecontract_create"),
					new TypeReference<CommonRequest<AccountCodePurposeContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (AccountCodePurposeContract accountCodePurposeContract : request.getData()) {
				AccountCodePurpose domain = mapper.map(accountCodePurposeContract, AccountCodePurpose.class);
				accountCodePurposeService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("accountCodePurposecontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("accountcodepurposecontract_update") != null) {

			CommonRequest<AccountCodePurposeContract> request = objectMapper.convertValue(
					mastersMap.get("accountcodepurposecontract_update"),
					new TypeReference<CommonRequest<AccountCodePurposeContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (AccountCodePurposeContract accountCodePurposeContract : request.getData()) {
				AccountCodePurpose domain = mapper.map(accountCodePurposeContract, AccountCodePurpose.class);
				accountCodePurposeService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("accountCodePurposecontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("accountdetailtypecontract_create") != null) {

			CommonRequest<AccountDetailTypeContract> request = objectMapper.convertValue(
					mastersMap.get("accountdetailtypecontract_create"),
					new TypeReference<CommonRequest<AccountDetailTypeContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (AccountDetailTypeContract accountDetailTypeContract : request.getData()) {
				AccountDetailType domain = mapper.map(accountDetailTypeContract, AccountDetailType.class);
				accountDetailTypeService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("accountDetailTypecontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("accountdetailtypecontract_update") != null) {

			CommonRequest<AccountDetailTypeContract> request = objectMapper.convertValue(
					mastersMap.get("accountdetailtypecontract_update"),
					new TypeReference<CommonRequest<AccountDetailTypeContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (AccountDetailTypeContract accountDetailTypeContract : request.getData()) {
				AccountDetailType domain = mapper.map(accountDetailTypeContract, AccountDetailType.class);
				accountDetailTypeService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("accountDetailTypecontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("accountdetailkeycontract_create") != null) {

			CommonRequest<AccountDetailKeyContract> request = objectMapper.convertValue(
					mastersMap.get("accountdetailkeycontract_create"),
					new TypeReference<CommonRequest<AccountDetailKeyContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (AccountDetailKeyContract accountDetailKeyContract : request.getData()) {
				AccountDetailKey domain = mapper.map(accountDetailKeyContract, AccountDetailKey.class);
				accountDetailKeyService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("accountDetailKeycontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("accountdetailkeycontract_update") != null) {

			CommonRequest<AccountDetailKeyContract> request = objectMapper.convertValue(
					mastersMap.get("accountdetailkeycontract_update"),
					new TypeReference<CommonRequest<AccountDetailKeyContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (AccountDetailKeyContract accountDetailKeyContract : request.getData()) {
				AccountDetailKey domain = mapper.map(accountDetailKeyContract, AccountDetailKey.class);
				accountDetailKeyService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("accountDetailKeycontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("accountentitycontract_create") != null) {

			CommonRequest<AccountEntityContract> request = objectMapper.convertValue(
					mastersMap.get("accountentitycontract_create"),
					new TypeReference<CommonRequest<AccountEntityContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (AccountEntityContract accountEntityContract : request.getData()) {
				AccountEntity domain = mapper.map(accountEntityContract, AccountEntity.class);
				accountEntityService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("accountEntitycontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("accountentitycontract_update") != null) {

			CommonRequest<AccountEntityContract> request = objectMapper.convertValue(
					mastersMap.get("accountentitycontract_update"),
					new TypeReference<CommonRequest<AccountEntityContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (AccountEntityContract accountEntityContract : request.getData()) {
				AccountEntity domain = mapper.map(accountEntityContract, AccountEntity.class);
				accountEntityService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("accountEntitycontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("budgetgroupcontract_create") != null) {

			CommonRequest<BudgetGroupContract> request = objectMapper.convertValue(
					mastersMap.get("budgetgroupcontract_create"),
					new TypeReference<CommonRequest<BudgetGroupContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (BudgetGroupContract budgetGroupContract : request.getData()) {
				BudgetGroup domain = mapper.map(budgetGroupContract, BudgetGroup.class);
				budgetGroupService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("budgetGroupcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("budgetgroupcontract_update") != null) {

			CommonRequest<BudgetGroupContract> request = objectMapper.convertValue(
					mastersMap.get("budgetgroupcontract_update"),
					new TypeReference<CommonRequest<BudgetGroupContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (BudgetGroupContract budgetGroupContract : request.getData()) {
				BudgetGroup domain = mapper.map(budgetGroupContract, BudgetGroup.class);
				budgetGroupService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("budgetGroupcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}

		if (mastersMap.get("chartofaccountcontract_create") != null) {

			CommonRequest<ChartOfAccountContract> request = objectMapper.convertValue(
					mastersMap.get("chartofaccountcontract_create"),
					new TypeReference<CommonRequest<ChartOfAccountContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (ChartOfAccountContract chartOfAccountContract : request.getData()) {
				ChartOfAccount domain = mapper.map(chartOfAccountContract, ChartOfAccount.class);
				chartOfAccountService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("chartOfAccountcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("chartofaccountcontract_update") != null) {

			CommonRequest<ChartOfAccountContract> request = objectMapper.convertValue(
					mastersMap.get("chartofaccountcontract_update"),
					new TypeReference<CommonRequest<ChartOfAccountContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (ChartOfAccountContract chartOfAccountContract : request.getData()) {
				ChartOfAccount domain = mapper.map(chartOfAccountContract, ChartOfAccount.class);
				chartOfAccountService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("chartOfAccountcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}

		if (mastersMap.get("chartofaccountdetailcontract_create") != null) {

			CommonRequest<ChartOfAccountDetailContract> request = objectMapper.convertValue(
					mastersMap.get("chartofaccountdetailcontract_create"),
					new TypeReference<CommonRequest<ChartOfAccountDetailContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (ChartOfAccountDetailContract chartOfAccountDetailContract : request.getData()) {
				ChartOfAccountDetail domain = mapper.map(chartOfAccountDetailContract, ChartOfAccountDetail.class);
				chartOfAccountDetailService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("chartOfAccountDetailcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("chartofaccountdetailcontract_update") != null) {

			CommonRequest<ChartOfAccountDetailContract> request = objectMapper.convertValue(
					mastersMap.get("chartofaccountdetailcontract_update"),
					new TypeReference<CommonRequest<ChartOfAccountDetailContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (ChartOfAccountDetailContract chartOfAccountDetailContract : request.getData()) {
				ChartOfAccountDetail domain = mapper.map(chartOfAccountDetailContract, ChartOfAccountDetail.class);
				chartOfAccountDetailService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("chartOfAccountDetailcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}

		if (mastersMap.get("financialyearcontract_create") != null) {

			CommonRequest<FinancialYearContract> request = objectMapper.convertValue(
					mastersMap.get("financialyearcontract_create"),
					new TypeReference<CommonRequest<FinancialYearContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (FinancialYearContract financialYearContract : request.getData()) {
				FinancialYear domain = mapper.map(financialYearContract, FinancialYear.class);
				financialYearService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("financialYearcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("financialyearcontract_update") != null) {

			CommonRequest<FinancialYearContract> request = objectMapper.convertValue(
					mastersMap.get("financialyearcontract_update"),
					new TypeReference<CommonRequest<FinancialYearContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (FinancialYearContract financialYearContract : request.getData()) {
				FinancialYear domain = mapper.map(financialYearContract, FinancialYear.class);
				financialYearService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("financialYearcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}

		if (mastersMap.get("fiscalperiodcontract_create") != null) {

			CommonRequest<FiscalPeriodContract> request = objectMapper.convertValue(
					mastersMap.get("fiscalperiodcontract_create"),
					new TypeReference<CommonRequest<FiscalPeriodContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (FiscalPeriodContract fiscalPeriodContract : request.getData()) {
				FiscalPeriod domain = mapper.map(fiscalPeriodContract, FiscalPeriod.class);
				fiscalPeriodService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("fiscalPeriodcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("fiscalperiodcontract_update") != null) {

			CommonRequest<FiscalPeriodContract> request = objectMapper.convertValue(
					mastersMap.get("fiscalperiodcontract_update"),
					new TypeReference<CommonRequest<FiscalPeriodContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (FiscalPeriodContract fiscalPeriodContract : request.getData()) {
				FiscalPeriod domain = mapper.map(fiscalPeriodContract, FiscalPeriod.class);
				fiscalPeriodService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("fiscalPeriodcontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}

		if (mastersMap.get("functionarycontract_create") != null) {

			CommonRequest<FunctionaryContract> request = objectMapper.convertValue(
					mastersMap.get("functionarycontract_create"),
					new TypeReference<CommonRequest<FunctionaryContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (FunctionaryContract functionaryContract : request.getData()) {
				Functionary domain = mapper.map(functionaryContract, Functionary.class);
				functionaryService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("functionarycontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("functionarycontract_update") != null) {

			CommonRequest<FunctionaryContract> request = objectMapper.convertValue(
					mastersMap.get("functionarycontract_update"),
					new TypeReference<CommonRequest<FunctionaryContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (FunctionaryContract functionaryContract : request.getData()) {
				Functionary domain = mapper.map(functionaryContract, Functionary.class);
				functionaryService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("functionarycontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}

		if (mastersMap.get("fundsourcecontract_create") != null) {

			CommonRequest<FundsourceContract> request = objectMapper.convertValue(
					mastersMap.get("fundsourcecontract_create"),
					new TypeReference<CommonRequest<FundsourceContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (FundsourceContract fundsourceContract : request.getData()) {
				Fundsource domain = mapper.map(fundsourceContract, Fundsource.class);
				fundsourceService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("fundsourcecontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("fundsourcecontract_update") != null) {

			CommonRequest<FundsourceContract> request = objectMapper.convertValue(
					mastersMap.get("fundsourcecontract_update"),
					new TypeReference<CommonRequest<FundsourceContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (FundsourceContract fundsourceContract : request.getData()) {
				Fundsource domain = mapper.map(fundsourceContract, Fundsource.class);
				fundsourceService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("fundsourcecontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}

		if (mastersMap.get("schemecontract_create") != null) {

			CommonRequest<SchemeContract> request = objectMapper.convertValue(mastersMap.get("schemecontract_create"),
					new TypeReference<CommonRequest<SchemeContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (SchemeContract schemeContract : request.getData()) {
				Scheme domain = mapper.map(schemeContract, Scheme.class);
				schemeService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("schemecontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("schemecontract_update") != null) {

			CommonRequest<SchemeContract> request = objectMapper.convertValue(mastersMap.get("schemecontract_update"),
					new TypeReference<CommonRequest<SchemeContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (SchemeContract schemeContract : request.getData()) {
				Scheme domain = mapper.map(schemeContract, Scheme.class);
				schemeService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("schemecontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}

		if (mastersMap.get("subschemecontract_create") != null) {

			CommonRequest<SubSchemeContract> request = objectMapper.convertValue(
					mastersMap.get("subschemecontract_create"), new TypeReference<CommonRequest<SubSchemeContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (SubSchemeContract subSchemeContract : request.getData()) {
				SubScheme domain = mapper.map(subSchemeContract, SubScheme.class);
				subSchemeService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("subschemecontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("subschemecontract_update") != null) {

			CommonRequest<SubSchemeContract> request = objectMapper.convertValue(
					mastersMap.get("subschemecontract_update"), new TypeReference<CommonRequest<SubSchemeContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (SubSchemeContract subSchemeContract : request.getData()) {
				SubScheme domain = mapper.map(subSchemeContract, SubScheme.class);
				subSchemeService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("subSubSchemecontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("suppliercontract_create") != null) {

			CommonRequest<SupplierContract> request = objectMapper.convertValue(
					mastersMap.get("suppliercontract_create"), new TypeReference<CommonRequest<SupplierContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (SupplierContract supplierContract : request.getData()) {
				Supplier domain = mapper.map(supplierContract, Supplier.class);
				supplierService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("suppliercontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("suppliercontract_update") != null) {

			CommonRequest<SupplierContract> request = objectMapper.convertValue(
					mastersMap.get("suppliercontract_update"), new TypeReference<CommonRequest<SupplierContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (SupplierContract supplierContract : request.getData()) {
				Supplier domain = mapper.map(supplierContract, Supplier.class);
				supplierService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("suppliercontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}

		if (mastersMap.get("egfStatuscontract_create") != null) {

			CommonRequest<EgfStatusContract> request = objectMapper.convertValue(
					mastersMap.get("egfStatuscontract_create"), new TypeReference<CommonRequest<EgfStatusContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (EgfStatusContract egfStatusContract : request.getData()) {
				EgfStatus domain = mapper.map(egfStatusContract, EgfStatus.class);
				egfStatusService.save(domain);
			}

			mastersMap.clear();
			mastersMap.put("egfStatuscontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
		if (mastersMap.get("egfStatuscontract_update") != null) {

			CommonRequest<EgfStatusContract> request = objectMapper.convertValue(
					mastersMap.get("egfStatuscontract_update"), new TypeReference<CommonRequest<EgfStatusContract>>() {
					});

			ModelMapper mapper = new ModelMapper();
			for (EgfStatusContract egfStatusContract : request.getData()) {
				EgfStatus domain = mapper.map(egfStatusContract, EgfStatus.class);
				egfStatusService.update(domain);
			}

			mastersMap.clear();
			mastersMap.put("egfStatuscontract_completed", request);
			financialProducer.sendMessage(completedTopic, completedTopic, mastersMap);
		}
	}

}
