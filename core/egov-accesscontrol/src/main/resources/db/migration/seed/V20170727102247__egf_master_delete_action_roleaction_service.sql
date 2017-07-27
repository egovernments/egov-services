
delete from eg_roleaction where actionid in(select id from eg_action where name in('createAccountDetailKey','updateAccountDetailKey',
'searchAccountDetailKey','createBank','updateBank','searchBank','createBankAccount','updateBankAccount','searchBankAccount',
'createAccountCodePurpose','updateAccountCodePurpose','searchAccountCodePurpose','createSupplier','updateSupplier','searchSupplier',
'createFund','updateFund','searchFund','createSubScheme_MS','updateSubScheme_MS','searchSubScheme_MS','createFunction_MS',
'updateFunction_MS','searchFunction_MS','createBudgetGroup_MS','updateBudgetGroup_MS','searchBudgetGroup_MS','createScheme_MS',
'updateScheme_MS','searchScheme_MS','createBankBranch','updateBankBranch','searchBankBranch','createFundSource',
'updateFundSource','searchFundSource','createFunctionary','updateFunctionary','searchFunctionary','createChartOfAccount',
'updateChartOfAccount','searchChartOfAccount','createChartOfAccountDetail','updateChartOfAccountDetail',
'searchChartOfAccountDetail','createAccountEntiy','updateAccountEntiy','searchAccountEntiy','createFinancialYear','updateFinancialYear',
'searchFinancialYear','createFiscalPeriod','updateFiscalPeriod','searchFiscalPeriod','createAccountDetailType','updateAccountDetailType',
'searchAccountDetailType'));

delete from eg_action where name in('createAccountDetailKey','updateAccountDetailKey',
'searchAccountDetailKey','createBank','updateBank','searchBank','createBankAccount','updateBankAccount','searchBankAccount',
'createAccountCodePurpose','updateAccountCodePurpose','searchAccountCodePurpose','createSupplier','updateSupplier','searchSupplier',
'createFund','updateFund','searchFund','createSubScheme_MS','updateSubScheme_MS','searchSubScheme_MS','createFunction_MS',
'updateFunction_MS','searchFunction_MS','createBudgetGroup_MS','updateBudgetGroup_MS','searchBudgetGroup_MS','createScheme_MS',
'updateScheme_MS','searchScheme_MS','createBankBranch','updateBankBranch','searchBankBranch','createFundSource',
'updateFundSource','searchFundSource','createFunctionary','updateFunctionary','searchFunctionary','createChartOfAccount',
'updateChartOfAccount','searchChartOfAccount','createChartOfAccountDetail','updateChartOfAccountDetail',
'searchChartOfAccountDetail','createAccountEntiy','updateAccountEntiy','searchAccountEntiy','createFinancialYear','updateFinancialYear',
'searchFinancialYear','createFiscalPeriod','updateFiscalPeriod','searchFiscalPeriod','createAccountDetailType','updateAccountDetailType',
'searchAccountDetailType');

delete from service where name in('Account Detail key','Bank_MS','Bankaccount','AccountCodePurpose','Supplier','Fund_MS','SubScheme',
'Function_MS','BudgetGroup','Scheme','BankBranch','FundSource','Functionary','ChartOfAccount','ChartOfAccountDetail','AccountEntiy',
'FinancialYear','FiscalPeriod','AccountDetailType','Financials_Masters_MS','Financials_MS');

