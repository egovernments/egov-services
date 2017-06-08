package org.egov.egf.listner;

import java.util.HashMap;

import org.egov.egf.persistence.queue.contract.AccountCodePurposeContractResponse;
import org.egov.egf.persistence.queue.contract.AccountDetailKeyContractResponse;
import org.egov.egf.persistence.queue.contract.AccountDetailTypeContractResponse;
import org.egov.egf.persistence.queue.contract.AccountEntityContractResponse;
import org.egov.egf.persistence.queue.contract.BankAccountContractResponse;
import org.egov.egf.persistence.queue.contract.BankBranchContractResponse;
import org.egov.egf.persistence.queue.contract.BankContractResponse;
import org.egov.egf.persistence.queue.contract.BudgetGroupContractResponse;
import org.egov.egf.persistence.queue.contract.ChartOfAccountContractResponse;
import org.egov.egf.persistence.queue.contract.ChartOfAccountDetailContractResponse;
import org.egov.egf.persistence.queue.contract.FinancialYearContractResponse;
import org.egov.egf.persistence.queue.contract.FiscalPeriodContractResponse;
import org.egov.egf.persistence.queue.contract.FunctionContractResponse;
import org.egov.egf.persistence.queue.contract.FunctionaryContractResponse;
import org.egov.egf.persistence.queue.contract.FundContractResponse;
import org.egov.egf.persistence.queue.contract.FundsourceContractResponse;
import org.egov.egf.persistence.queue.contract.SchemeContractResponse;
import org.egov.egf.persistence.queue.contract.SubSchemeContractResponse;
import org.egov.egf.persistence.queue.contract.SupplierContractResponse;
import org.egov.egf.persistence.service.AccountCodePurposeService;
import org.egov.egf.persistence.service.AccountDetailKeyService;
import org.egov.egf.persistence.service.AccountDetailTypeService;
import org.egov.egf.persistence.service.AccountEntityService;
import org.egov.egf.persistence.service.BankAccountService;
import org.egov.egf.persistence.service.BankBranchService;
import org.egov.egf.persistence.service.BankService;
import org.egov.egf.persistence.service.BudgetGroupService;
import org.egov.egf.persistence.service.ChartOfAccountDetailService;
import org.egov.egf.persistence.service.ChartOfAccountService;
import org.egov.egf.persistence.service.FinancialYearService;
import org.egov.egf.persistence.service.FiscalPeriodService;
import org.egov.egf.persistence.service.FunctionService;
import org.egov.egf.persistence.service.FunctionaryService;
import org.egov.egf.persistence.service.FundService;
import org.egov.egf.persistence.service.FundsourceService;
import org.egov.egf.persistence.service.SchemeService;
import org.egov.egf.persistence.service.SubSchemeService;
import org.egov.egf.persistence.service.SupplierService;
import org.egov.egf.producer.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class FinancialMastersListener {

	@Value("${kafka.topics.egf.masters.completed.topic}")
	private String completedTopic;

	@Value("${kafka.topics.egf.masters.bank.completed.key}")
	private String bankCompletedKey;

	@Value("${kafka.topics.egf.masters.bankbranch.completed.key}")
	private String bankBranchCompletedKey;

	@Value("${kafka.topics.egf.masters.bankaccount.completed.key}")
	private String bankAccountCompletedKey;

	@Value("${kafka.topics.egf.masters.accountcodepurpose.completed.key}")
	private String accountCodePurposeCompletedKey;

	@Value("${kafka.topics.egf.masters.accountdetailkey.completed.key}")
	private String accountDetailKeyCompletedKey;

	@Value("${kafka.topics.egf.masters.accountdetailtype.completed.key}")
	private String accountDetailTypeCompletedKey;

	@Value("${kafka.topics.egf.masters.accountentity.completed.key}")
	private String accountEntityCompletedKey;

	@Value("${kafka.topics.egf.masters.budgetgroup.completed.key}")
	private String budgetGroupCompletedKey;

	@Value("${kafka.topics.egf.masters.chartofaccount.completed.key}")
	private String chartOfAccountCompletedKey;

	@Value("${kafka.topics.egf.masters.chartofaccountdetail.completed.key}")
	private String chartOfAccountDetailCompletedKey;

	@Value("${kafka.topics.egf.masters.financialyear.completed.key}")
	private String financialYearCompletedKey;

	@Value("${kafka.topics.egf.masters.fiscalperiod.completed.key}")
	private String fiscalPeriodCompletedKey;

	@Value("${kafka.topics.egf.masters.functionary.completed.key}")
	private String functionaryCompletedKey;

	@Value("${kafka.topics.egf.masters.function.completed.key}")
	private String functionCompletedKey;

	@Value("${kafka.topics.egf.masters.fund.completed.key}")
	private String fundCompletedKey;

	@Value("${kafka.topics.egf.masters.fundsource.completed.key}")
	private String fundSourceCompletedKey;

	@Value("${kafka.topics.egf.masters.scheme.completed.key}")
	private String schemeCompletedKey;

	@Value("${kafka.topics.egf.masters.subscheme.completed.key}")
	private String subSchemeCompletedKey;

	@Value("${kafka.topics.egf.masters.supplier.completed.key}")
	private String supplierCompletedKey;

	@Autowired
	private BankService bankService;

	@Autowired
	private BankBranchService bankBranchService;

	@Autowired
	private BankAccountService bankAccountService;

	@Autowired
	private AccountCodePurposeService accountCodePurposeService;

	@Autowired
	private AccountDetailKeyService accountDetailKeyService;

	@Autowired
	private AccountDetailTypeService accountDetailTypeService;

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
	private FunctionService functionService;

	@Autowired
	private FundService fundService;

	@Autowired
	private FundsourceService fundsourceService;

	@Autowired
	private SchemeService schemeService;

	@Autowired
	private SubSchemeService subSchemeService;

	@Autowired
	private SupplierService supplierService;

	@Autowired
	private FinancialProducer financialProducer;

	@KafkaListener(id = "${kafka.topics.egf.masters.validated.id}", topics = "${kafka.topics.egf.masters.validated.topic}", group = "${kafka.topics.egf.masters.validated.group}")
	public void process(HashMap<String, Object> financialContractRequestMap) {

		if (financialContractRequestMap.get("AccountCodePurposeCreate") != null) {

			AccountCodePurposeContractResponse accountCodePurposeContractResponse = accountCodePurposeService
					.create(financialContractRequestMap);

			HashMap<String, Object> accountCodePurposeContractResponseMap = new HashMap<String, Object>();
			accountCodePurposeContractResponseMap.put("AccountCodePurpose", accountCodePurposeContractResponse);
			financialProducer.sendMessage(completedTopic, accountCodePurposeCompletedKey, accountCodePurposeContractResponseMap);
		}

		if (financialContractRequestMap.get("AccountCodePurposeUpdate") != null) {

			AccountCodePurposeContractResponse accountCodePurposeContractResponse = accountCodePurposeService
					.update(financialContractRequestMap);

			HashMap<String, Object> accountCodePurposeContractResponseMap = new HashMap<String, Object>();
			accountCodePurposeContractResponseMap.put("AccountCodePurpose", accountCodePurposeContractResponse);
			financialProducer.sendMessage(completedTopic, accountCodePurposeCompletedKey,
					accountCodePurposeContractResponseMap);

		}

		if (financialContractRequestMap.get("BankBranchCreate") != null) {
			BankBranchContractResponse bankBranchContractResponse = bankBranchService
					.create(financialContractRequestMap);
			HashMap<String, Object> bankBranchContractResponseMap = new HashMap<String, Object>();
			bankBranchContractResponseMap.put("BankBranch", bankBranchContractResponse);
			financialProducer.sendMessage(completedTopic, bankBranchCompletedKey, bankBranchContractResponseMap);
		}

		if (financialContractRequestMap.get("BankBranchUpdate") != null) {
			BankBranchContractResponse bankBranchContractResponse = bankBranchService
					.update(financialContractRequestMap);
			HashMap<String, Object> bankBranchContractResponseMap = new HashMap<String, Object>();
			bankBranchContractResponseMap.put("BankBranch", bankBranchContractResponse);
			financialProducer.sendMessage(completedTopic, bankBranchCompletedKey, bankBranchContractResponseMap);
		}
		
		
		if (financialContractRequestMap.get("BankCreate") != null) {
			BankContractResponse bankContractResponse = bankService.create(financialContractRequestMap);
			HashMap<String, Object> bankContractResponseMap = new HashMap<String, Object>();
			bankContractResponseMap.put("Bank", bankContractResponse);
			financialProducer.sendMessage(completedTopic, bankCompletedKey, bankContractResponseMap);
		}

		if (financialContractRequestMap.get("BankUpdateAll") != null) {
			BankContractResponse bankContractResponse = bankService.updateAll(financialContractRequestMap);
			HashMap<String, Object> bankContractResponseMap = new HashMap<String, Object>();
			bankContractResponseMap.put("Bank", bankContractResponse);
			financialProducer.sendMessage(completedTopic, bankCompletedKey, bankContractResponseMap);
		}

		if (financialContractRequestMap.get("BankUpdate") != null) {
			BankContractResponse bankContractResponse = bankService.update(financialContractRequestMap);
			HashMap<String, Object> bankContractResponseMap = new HashMap<String, Object>();
			bankContractResponseMap.put("Bank", bankContractResponse);
			financialProducer.sendMessage(completedTopic, bankCompletedKey, bankContractResponseMap);
		}


		if (financialContractRequestMap.get("BankAccountCreate") != null) {
			BankAccountContractResponse bankAccountContractResponse = bankAccountService
					.create(financialContractRequestMap);
			HashMap<String, Object> bankAccountContractResponseMap = new HashMap<String, Object>();
			bankAccountContractResponseMap.put("BankAccount", bankAccountContractResponse);
			financialProducer.sendMessage(completedTopic, bankAccountCompletedKey, bankAccountContractResponseMap);
		}

		if (financialContractRequestMap.get("BankAccountUpdateAll") != null) {
			BankAccountContractResponse bankAccountContractResponse = bankAccountService
					.updateAll(financialContractRequestMap);
			HashMap<String, Object> bankAccountContractResponseMap = new HashMap<String, Object>();
			bankAccountContractResponseMap.put("BankAccount", bankAccountContractResponse);
			financialProducer.sendMessage(completedTopic, bankAccountCompletedKey, bankAccountContractResponseMap);
		}

		if (financialContractRequestMap.get("BankAccountUpdate") != null) {
			BankAccountContractResponse bankAccountContractResponse = bankAccountService
					.update(financialContractRequestMap);
			HashMap<String, Object> bankAccountContractResponseMap = new HashMap<String, Object>();
			bankAccountContractResponseMap.put("BankAccount", bankAccountContractResponse);
			financialProducer.sendMessage(completedTopic, bankAccountCompletedKey, bankAccountContractResponseMap);
		}

		if (financialContractRequestMap.get("AccountDetailKeyCreate") != null) {
			AccountDetailKeyContractResponse accountDetailKeyContractResponse = accountDetailKeyService
					.create(financialContractRequestMap);
			HashMap<String, Object> accountDetailKeyContractResponseMap = new HashMap<String, Object>();
			accountDetailKeyContractResponseMap.put("AccountDetailKey", accountDetailKeyContractResponse);
			financialProducer.sendMessage(completedTopic, accountDetailKeyCompletedKey,
					accountDetailKeyContractResponseMap);
		}

		if (financialContractRequestMap.get("AccountDetailKeyUpdateAll") != null) {
			AccountDetailKeyContractResponse accountDetailKeyContractResponse = accountDetailKeyService
					.updateAll(financialContractRequestMap);
			HashMap<String, Object> accountDetailKeyContractResponseMap = new HashMap<String, Object>();
			accountDetailKeyContractResponseMap.put("AccountDetailKey", accountDetailKeyContractResponse);
			financialProducer.sendMessage(completedTopic, accountDetailKeyCompletedKey,
					accountDetailKeyContractResponseMap);
		}

		if (financialContractRequestMap.get("AccountDetailKeyUpdate") != null) {
			AccountDetailKeyContractResponse accountDetailKeyContractResponse = accountDetailKeyService
					.update(financialContractRequestMap);
			HashMap<String, Object> accountDetailKeyContractResponseMap = new HashMap<String, Object>();
			accountDetailKeyContractResponseMap.put("AccountDetailKey", accountDetailKeyContractResponse);
			financialProducer.sendMessage(completedTopic, accountDetailKeyCompletedKey,
					accountDetailKeyContractResponseMap);
		}

		if (financialContractRequestMap.get("AccountDetailTypeCreate") != null) {
			AccountDetailTypeContractResponse accountDetailTypeContractResponse = accountDetailTypeService
					.create(financialContractRequestMap);
			HashMap<String, Object> accountDetailTypeContractResponseMap = new HashMap<String, Object>();
			accountDetailTypeContractResponseMap.put("AccountDetailType", accountDetailTypeContractResponse);
			financialProducer.sendMessage(completedTopic, accountDetailTypeCompletedKey,
					accountDetailTypeContractResponseMap);
		}

		if (financialContractRequestMap.get("AccountDetailTypeUpdateAll") != null) {
			AccountDetailTypeContractResponse accountDetailTypeContractResponse = accountDetailTypeService
					.updateAll(financialContractRequestMap);
			HashMap<String, Object> accountDetailTypeContractResponseMap = new HashMap<String, Object>();
			accountDetailTypeContractResponseMap.put("AccountDetailType", accountDetailTypeContractResponse);
			financialProducer.sendMessage(completedTopic, accountDetailTypeCompletedKey,
					accountDetailTypeContractResponseMap);
		}

		if (financialContractRequestMap.get("AccountDetailTypeUpdate") != null) {
			AccountDetailTypeContractResponse accountDetailTypeContractResponse = accountDetailTypeService
					.update(financialContractRequestMap);
			HashMap<String, Object> accountDetailTypeContractResponseMap = new HashMap<String, Object>();
			accountDetailTypeContractResponseMap.put("AccountDetailType", accountDetailTypeContractResponse);
			financialProducer.sendMessage(completedTopic, accountDetailTypeCompletedKey,
					accountDetailTypeContractResponseMap);
		}

		if (financialContractRequestMap.get("AccountEntityCreate") != null) {
			AccountEntityContractResponse accountEntityContractResponse = accountEntityService
					.create(financialContractRequestMap);
			HashMap<String, Object> accountEntityContractResponseMap = new HashMap<String, Object>();
			accountEntityContractResponseMap.put("AccountEntity", accountEntityContractResponse);
			financialProducer.sendMessage(completedTopic, accountEntityCompletedKey, accountEntityContractResponseMap);
		}

		if (financialContractRequestMap.get("AccountEntityUpdateAll") != null) {
			AccountEntityContractResponse accountEntityContractResponse = accountEntityService
					.updateAll(financialContractRequestMap);
			HashMap<String, Object> accountEntityContractResponseMap = new HashMap<String, Object>();
			accountEntityContractResponseMap.put("AccountEntity", accountEntityContractResponse);
			financialProducer.sendMessage(completedTopic, accountEntityCompletedKey, accountEntityContractResponseMap);
		}

		if (financialContractRequestMap.get("AccountEntityUpdate") != null) {
			AccountEntityContractResponse accountEntityContractResponse = accountEntityService
					.update(financialContractRequestMap);
			HashMap<String, Object> accountEntityContractResponseMap = new HashMap<String, Object>();
			accountEntityContractResponseMap.put("AccountEntity", accountEntityContractResponse);
			financialProducer.sendMessage(completedTopic, accountEntityCompletedKey, accountEntityContractResponseMap);
		}

		if (financialContractRequestMap.get("BudgetGroupCreate") != null) {
			BudgetGroupContractResponse budgetGroupContractResponse = budgetGroupService
					.create(financialContractRequestMap);
			HashMap<String, Object> budgetGroupContractResponseMap = new HashMap<String, Object>();
			budgetGroupContractResponseMap.put("BudgetGroup", budgetGroupContractResponse);
			financialProducer.sendMessage(completedTopic, budgetGroupCompletedKey, budgetGroupContractResponseMap);
		}

		if (financialContractRequestMap.get("BudgetGroupUpdateAll") != null) {
			BudgetGroupContractResponse budgetGroupContractResponse = budgetGroupService
					.updateAll(financialContractRequestMap);
			HashMap<String, Object> budgetGroupContractResponseMap = new HashMap<String, Object>();
			budgetGroupContractResponseMap.put("BudgetGroup", budgetGroupContractResponse);
			financialProducer.sendMessage(completedTopic, budgetGroupCompletedKey, budgetGroupContractResponseMap);
		}

		if (financialContractRequestMap.get("BudgetGroupUpdate") != null) {
			BudgetGroupContractResponse budgetGroupContractResponse = budgetGroupService
					.update(financialContractRequestMap);
			HashMap<String, Object> budgetGroupContractResponseMap = new HashMap<String, Object>();
			budgetGroupContractResponseMap.put("BudgetGroup", budgetGroupContractResponse);
			financialProducer.sendMessage(completedTopic, budgetGroupCompletedKey, budgetGroupContractResponseMap);
		}

		if (financialContractRequestMap.get("ChartOfAccountCreate") != null) {
			ChartOfAccountContractResponse chartOfAccountContractResponse = chartOfAccountService
					.create(financialContractRequestMap);
			HashMap<String, Object> chartOfAccountContractResponseMap = new HashMap<String, Object>();
			chartOfAccountContractResponseMap.put("ChartOfAccount", chartOfAccountContractResponse);
			financialProducer.sendMessage(completedTopic, chartOfAccountCompletedKey,
					chartOfAccountContractResponseMap);
		}

		if (financialContractRequestMap.get("ChartOfAccountUpdateAll") != null) {
			ChartOfAccountContractResponse chartOfAccountContractResponse = chartOfAccountService
					.updateAll(financialContractRequestMap);
			HashMap<String, Object> chartOfAccountContractResponseMap = new HashMap<String, Object>();
			chartOfAccountContractResponseMap.put("ChartOfAccount", chartOfAccountContractResponse);
			financialProducer.sendMessage(completedTopic, chartOfAccountCompletedKey,
					chartOfAccountContractResponseMap);
		}

		if (financialContractRequestMap.get("ChartOfAccountUpdate") != null) {
			ChartOfAccountContractResponse chartOfAccountContractResponse = chartOfAccountService
					.update(financialContractRequestMap);
			HashMap<String, Object> chartOfAccountContractResponseMap = new HashMap<String, Object>();
			chartOfAccountContractResponseMap.put("ChartOfAccount", chartOfAccountContractResponse);
			financialProducer.sendMessage(completedTopic, chartOfAccountCompletedKey,
					chartOfAccountContractResponseMap);
		}

		if (financialContractRequestMap.get("ChartOfAccountDetailCreate") != null) {
			ChartOfAccountDetailContractResponse chartOfAccountDetailContractResponse = chartOfAccountDetailService
					.create(financialContractRequestMap);
			HashMap<String, Object> chartOfAccountDetailContractResponseMap = new HashMap<String, Object>();
			chartOfAccountDetailContractResponseMap.put("ChartOfAccountDetail", chartOfAccountDetailContractResponse);
			financialProducer.sendMessage(completedTopic, chartOfAccountDetailCompletedKey,
					chartOfAccountDetailContractResponseMap);
		}

		if (financialContractRequestMap.get("ChartOfAccountDetailUpdateAll") != null) {
			ChartOfAccountDetailContractResponse chartOfAccountDetailContractResponse = chartOfAccountDetailService
					.updateAll(financialContractRequestMap);
			HashMap<String, Object> chartOfAccountDetailContractResponseMap = new HashMap<String, Object>();
			chartOfAccountDetailContractResponseMap.put("ChartOfAccountDetail", chartOfAccountDetailContractResponse);
			financialProducer.sendMessage(completedTopic, chartOfAccountDetailCompletedKey,
					chartOfAccountDetailContractResponseMap);
		}

		if (financialContractRequestMap.get("ChartOfAccountDetailUpdate") != null) {
			ChartOfAccountDetailContractResponse chartOfAccountDetailContractResponse = chartOfAccountDetailService
					.update(financialContractRequestMap);
			HashMap<String, Object> chartOfAccountDetailContractResponseMap = new HashMap<String, Object>();
			chartOfAccountDetailContractResponseMap.put("ChartOfAccountDetail", chartOfAccountDetailContractResponse);
			financialProducer.sendMessage(completedTopic, chartOfAccountDetailCompletedKey,
					chartOfAccountDetailContractResponseMap);
		}

		if (financialContractRequestMap.get("FinancialYearCreate") != null) {
			FinancialYearContractResponse financialYearContractResponse = financialYearService
					.create(financialContractRequestMap);
			HashMap<String, Object> financialYearContractResponseMap = new HashMap<String, Object>();
			financialYearContractResponseMap.put("FinancialYear", financialYearContractResponse);
			financialProducer.sendMessage(completedTopic, financialYearCompletedKey, financialYearContractResponseMap);
		}

		if (financialContractRequestMap.get("FinancialYearUpdateAll") != null) {
			FinancialYearContractResponse financialYearContractResponse = financialYearService
					.updateAll(financialContractRequestMap);
			HashMap<String, Object> financialYearContractResponseMap = new HashMap<String, Object>();
			financialYearContractResponseMap.put("FinancialYear", financialYearContractResponse);
			financialProducer.sendMessage(completedTopic, financialYearCompletedKey, financialYearContractResponseMap);
		}

		if (financialContractRequestMap.get("FinancialYearUpdate") != null) {
			FinancialYearContractResponse financialYearContractResponse = financialYearService
					.update(financialContractRequestMap);
			HashMap<String, Object> financialYearContractResponseMap = new HashMap<String, Object>();
			financialYearContractResponseMap.put("FinancialYear", financialYearContractResponse);
			financialProducer.sendMessage(completedTopic, financialYearCompletedKey, financialYearContractResponseMap);
		}

		if (financialContractRequestMap.get("FiscalPeriodCreate") != null) {
			FiscalPeriodContractResponse fiscalPeriodContractResponse = fiscalPeriodService
					.create(financialContractRequestMap);
			HashMap<String, Object> fiscalPeriodContractResponseMap = new HashMap<String, Object>();
			fiscalPeriodContractResponseMap.put("FiscalPeriod", fiscalPeriodContractResponse);
			financialProducer.sendMessage(completedTopic, fiscalPeriodCompletedKey, fiscalPeriodContractResponseMap);
		}

		if (financialContractRequestMap.get("FiscalPeriodUpdateAll") != null) {
			FiscalPeriodContractResponse fiscalPeriodContractResponse = fiscalPeriodService
					.updateAll(financialContractRequestMap);
			HashMap<String, Object> fiscalPeriodContractResponseMap = new HashMap<String, Object>();
			fiscalPeriodContractResponseMap.put("FiscalPeriod", fiscalPeriodContractResponse);
			financialProducer.sendMessage(completedTopic, fiscalPeriodCompletedKey, fiscalPeriodContractResponseMap);
		}

		if (financialContractRequestMap.get("FiscalPeriodUpdate") != null) {
			FiscalPeriodContractResponse fiscalPeriodContractResponse = fiscalPeriodService
					.update(financialContractRequestMap);
			HashMap<String, Object> fiscalPeriodContractResponseMap = new HashMap<String, Object>();
			fiscalPeriodContractResponseMap.put("FiscalPeriod", fiscalPeriodContractResponse);
			financialProducer.sendMessage(completedTopic, fiscalPeriodCompletedKey, fiscalPeriodContractResponseMap);
		}

		if (financialContractRequestMap.get("FunctionaryCreate") != null) {
			FunctionaryContractResponse functionaryContractResponse = functionaryService
					.create(financialContractRequestMap);
			HashMap<String, Object> functionaryContractResponseMap = new HashMap<String, Object>();
			functionaryContractResponseMap.put("Functionary", functionaryContractResponse);
			financialProducer.sendMessage(completedTopic, functionaryCompletedKey, functionaryContractResponseMap);
		}

		if (financialContractRequestMap.get("FunctionaryUpdateAll") != null) {
			FunctionaryContractResponse functionaryContractResponse = functionaryService
					.updateAll(financialContractRequestMap);
			HashMap<String, Object> functionaryContractResponseMap = new HashMap<String, Object>();
			functionaryContractResponseMap.put("Functionary", functionaryContractResponse);
			financialProducer.sendMessage(completedTopic, functionaryCompletedKey, functionaryContractResponseMap);
		}

		if (financialContractRequestMap.get("FunctionaryUpdate") != null) {
			FunctionaryContractResponse functionaryContractResponse = functionaryService
					.update(financialContractRequestMap);
			HashMap<String, Object> functionaryContractResponseMap = new HashMap<String, Object>();
			functionaryContractResponseMap.put("Functionary", functionaryContractResponse);
			financialProducer.sendMessage(completedTopic, functionaryCompletedKey, functionaryContractResponseMap);
		}

		if (financialContractRequestMap.get("FunctionCreate") != null) {
			FunctionContractResponse functionContractResponse = functionService.create(financialContractRequestMap);
			HashMap<String, Object> functionContractResponseMap = new HashMap<String, Object>();
			functionContractResponseMap.put("Function", functionContractResponse);
			financialProducer.sendMessage(completedTopic, functionCompletedKey, functionContractResponseMap);
		}

		if (financialContractRequestMap.get("FunctionUpdateAll") != null) {
			FunctionContractResponse functionContractResponse = functionService.updateAll(financialContractRequestMap);
			HashMap<String, Object> functionContractResponseMap = new HashMap<String, Object>();
			functionContractResponseMap.put("Function", functionContractResponse);
			financialProducer.sendMessage(completedTopic, functionCompletedKey, functionContractResponseMap);
		}

		if (financialContractRequestMap.get("FunctionUpdate") != null) {
			FunctionContractResponse functionContractResponse = functionService.update(financialContractRequestMap);
			HashMap<String, Object> functionContractResponseMap = new HashMap<String, Object>();
			functionContractResponseMap.put("Function", functionContractResponse);
			financialProducer.sendMessage(completedTopic, functionCompletedKey, functionContractResponseMap);
		}

		if (financialContractRequestMap.get("FundCreate") != null) {
			FundContractResponse fundContractResponse = fundService.create(financialContractRequestMap);
			HashMap<String, Object> fundContractResponseMap = new HashMap<String, Object>();
			fundContractResponseMap.put("Fund", fundContractResponse);
			financialProducer.sendMessage(completedTopic, fundCompletedKey, fundContractResponseMap);
		}

		if (financialContractRequestMap.get("FundUpdateAll") != null) {
			FundContractResponse fundContractResponse = fundService.updateAll(financialContractRequestMap);
			HashMap<String, Object> fundContractResponseMap = new HashMap<String, Object>();
			fundContractResponseMap.put("Fund", fundContractResponse);
			financialProducer.sendMessage(completedTopic, fundCompletedKey, fundContractResponseMap);
		}

		if (financialContractRequestMap.get("FundUpdate") != null) {
			FundContractResponse fundContractResponse = fundService.update(financialContractRequestMap);
			HashMap<String, Object> fundContractResponseMap = new HashMap<String, Object>();
			fundContractResponseMap.put("Fund", fundContractResponse);
			financialProducer.sendMessage(completedTopic, fundCompletedKey, fundContractResponseMap);
		}

		if (financialContractRequestMap.get("FundSourceCreate") != null) {
			FundsourceContractResponse fundsourceContractResponse = fundsourceService
					.create(financialContractRequestMap);
			HashMap<String, Object> fundsourceContractResponseMap = new HashMap<String, Object>();
			fundsourceContractResponseMap.put("FundSource", fundsourceContractResponse);
			financialProducer.sendMessage(completedTopic, fundSourceCompletedKey, fundsourceContractResponseMap);
		}

		if (financialContractRequestMap.get("FundSourceUpdateAll") != null) {
			FundsourceContractResponse fundsourceContractResponse = fundsourceService
					.updateAll(financialContractRequestMap);
			HashMap<String, Object> fundsourceContractResponseMap = new HashMap<String, Object>();
			fundsourceContractResponseMap.put("FundSource", fundsourceContractResponse);
			financialProducer.sendMessage(completedTopic, fundSourceCompletedKey, fundsourceContractResponseMap);
		}

		if (financialContractRequestMap.get("FundSourceUpdate") != null) {
			FundsourceContractResponse fundsourceContractResponse = fundsourceService
					.update(financialContractRequestMap);
			HashMap<String, Object> fundsourceContractResponseMap = new HashMap<String, Object>();
			fundsourceContractResponseMap.put("FundSource", fundsourceContractResponse);
			financialProducer.sendMessage(completedTopic, fundSourceCompletedKey, fundsourceContractResponseMap);
		}

		if (financialContractRequestMap.get("SchemeCreate") != null) {
			SchemeContractResponse schemeContractResponse = schemeService.create(financialContractRequestMap);
			HashMap<String, Object> schemeContractResponseMap = new HashMap<String, Object>();
			schemeContractResponseMap.put("Scheme", schemeContractResponse);
			financialProducer.sendMessage(completedTopic, fundSourceCompletedKey, schemeContractResponseMap);
		}

		if (financialContractRequestMap.get("SchemeUpdateAll") != null) {
			SchemeContractResponse schemeContractResponse = schemeService.updateAll(financialContractRequestMap);
			HashMap<String, Object> schemeContractResponseMap = new HashMap<String, Object>();
			schemeContractResponseMap.put("Scheme", schemeContractResponse);
			financialProducer.sendMessage(completedTopic, fundSourceCompletedKey, schemeContractResponseMap);
		}

		if (financialContractRequestMap.get("SchemeUpdate") != null) {
			SchemeContractResponse schemeContractResponse = schemeService.update(financialContractRequestMap);
			HashMap<String, Object> schemeContractResponseMap = new HashMap<String, Object>();
			schemeContractResponseMap.put("Scheme", schemeContractResponse);
			financialProducer.sendMessage(completedTopic, schemeCompletedKey, schemeContractResponseMap);
		}

		if (financialContractRequestMap.get("SubSchemeCreate") != null) {
			SubSchemeContractResponse subSchemeContractResponse = subSchemeService.create(financialContractRequestMap);
			HashMap<String, Object> subSchemeContractResponseMap = new HashMap<String, Object>();
			subSchemeContractResponseMap.put("SubScheme", subSchemeContractResponse);
			financialProducer.sendMessage(completedTopic, fundSourceCompletedKey, subSchemeContractResponseMap);
		}

		if (financialContractRequestMap.get("SubSchemeUpdateAll") != null) {
			SubSchemeContractResponse subSchemeContractResponse = subSchemeService
					.updateAll(financialContractRequestMap);
			HashMap<String, Object> subSchemeContractResponseMap = new HashMap<String, Object>();
			subSchemeContractResponseMap.put("SubScheme", subSchemeContractResponse);
			financialProducer.sendMessage(completedTopic, fundSourceCompletedKey, subSchemeContractResponseMap);
		}

		if (financialContractRequestMap.get("SubSchemeUpdate") != null) {
			SubSchemeContractResponse subSchemeContractResponse = subSchemeService.update(financialContractRequestMap);
			HashMap<String, Object> subSchemeContractResponseMap = new HashMap<String, Object>();
			subSchemeContractResponseMap.put("SubScheme", subSchemeContractResponse);
			financialProducer.sendMessage(completedTopic, subSchemeCompletedKey, subSchemeContractResponseMap);
		}

		if (financialContractRequestMap.get("SupplierCreate") != null) {
			SupplierContractResponse supplierContractResponse = supplierService.create(financialContractRequestMap);
			HashMap<String, Object> supplierContractResponseMap = new HashMap<String, Object>();
			supplierContractResponseMap.put("Supplier", supplierContractResponse);
			financialProducer.sendMessage(completedTopic, fundSourceCompletedKey, supplierContractResponseMap);
		}

		if (financialContractRequestMap.get("SupplierUpdateAll") != null) {
			SupplierContractResponse supplierContractResponse = supplierService.updateAll(financialContractRequestMap);
			HashMap<String, Object> supplierContractResponseMap = new HashMap<String, Object>();
			supplierContractResponseMap.put("Supplier", supplierContractResponse);
			financialProducer.sendMessage(completedTopic, fundSourceCompletedKey, supplierContractResponseMap);
		}

		if (financialContractRequestMap.get("SupplierUpdate") != null) {
			SupplierContractResponse supplierContractResponse = supplierService.update(financialContractRequestMap);
			HashMap<String, Object> supplierContractResponseMap = new HashMap<String, Object>();
			supplierContractResponseMap.put("Supplier", supplierContractResponse);
			financialProducer.sendMessage(completedTopic, supplierCompletedKey, supplierContractResponseMap);
		}

	}

}
