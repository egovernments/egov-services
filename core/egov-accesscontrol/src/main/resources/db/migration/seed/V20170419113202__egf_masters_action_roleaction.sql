INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Financials_MS', 'Financials_MS', false, '/egf-masters', NULL, 'Financials', 20,'default');
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Financials_Masters_MS', 'Financials_Masters_MS', false, '/egf-masters', (select id from service where name='Financials_MS'), 'Masters', 20,'default');
--Account Detail key
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Account Detail key', 'Account Detail key', false, '/egf-masters', (select id from service where name='Financials_Masters_MS'), 'Account Detail key', 1,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'createAccountDetailKey','createAccountDetailKey',
'/accountdetailkeys/_create',null,(select id from service where name='Account Detail key'),1,'Create Account Detail Key',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'updateAccountDetailKey','updateAccountDetailKey',
'/accountdetailkeys/{id}/_update',null,(select id from service where name='Account Detail key'),2,'Modify Account Detail Key',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'searchAccountDetailKey','searchAccountDetailKey',
'/accountdetailkeys/_search',null,(select id from service where name='Account Detail key'),3,'Search Account Detail Key',false,1,now(),1,now(),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createAccountDetailKey'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateAccountDetailKey'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchAccountDetailKey'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createAccountDetailKey'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateAccountDetailKey'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchAccountDetailKey'),'default');

--Bank
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Bank_MS', 'Bank_MS', false, '/egf-masters', (select id from service where name='Financials_Masters_MS'), 'Bank', 2,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'createBank','createBank',
'/banks/_create',null,(select id from service where name='Bank_MS'),1,'Create Bank',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'updateBank','updateBank',
'/banks/{id}/_update',null,(select id from service where name='Bank_MS'),3,'Modify Bank',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'searchBank','searchBank',
'/banks/_search',null,(select id from service where name='Bank_MS'),3,'Search Bank',false,1,now(),1,now(),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createBank'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateBank'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchBank'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createBank'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateBank'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchBank'),'default');

--Bankaccounts
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Bankaccount', 'Bankaccount', false, '/egf-masters', (select id from service where name='Financials_Masters_MS'), 'Bankaccount', 3,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'createBankAccount','createBankAccount',
'/bankaccounts/_create',null,(select id from service where name='Bankaccount'),1,'Create BankAccount',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'updateBankAccount','updateBankAccount',
'/bankaccounts/{id}/_update',null,(select id from service where name='Bankaccount'),2,'Modify BankAccount',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'searchBankAccount','searchBankAccount',
'/bankaccounts/_search',null,(select id from service where name='Bankaccount'),3,'Search BankAccount',false,1,now(),1,now(),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createBankAccount'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateBankAccount'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchBankAccount'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createBankAccount'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateBankAccount'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchBankAccount'),'default');

--Account Code Purpose
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'AccountCodePurpose', 'AccountCodePurpose', false, '/egf-masters', (select id from service where name='Financials_Masters_MS'), 'Account Code Purpose', 4,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'createAccountCodePurpose','createAccountCodePurpose',
'/accountcodepurposes/_create',null,(select id from service where name='AccountCodePurpose'),1,'Create Account Code Purpose',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'updateAccountCodePurpose','updateAccountCodePurpose',
'/accountcodepurposes/{id}/_update',null,(select id from service where name='AccountCodePurpose'),2,'Modify Account Code Purpose',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'searchAccountCodePurpose','searchAccountCodePurpose',
'/accountcodepurposes/_search',null,(select id from service where name='AccountCodePurpose'),3,'Search Account Code Purpose',false,1,now(),1,now(),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createAccountCodePurpose'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateAccountCodePurpose'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchAccountCodePurpose'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createAccountCodePurpose'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateAccountCodePurpose'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchAccountCodePurpose'),'default');


--Supplier
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Supplier', 'Supplier', false, '/egf-masters', (select id from service where name='Financials_Masters_MS'), 'Supplier', 5,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'createSupplier','createSupplier',
'/suppliers/_create',null,(select id from service where name='Supplier'),1,'Create Supplier',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'updateSupplier','updateSupplier',
'/suppliers/{id}/_update',null,(select id from service where name='Supplier'),2,'Modify Supplier',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'searchSupplier','searchSupplier',
'/suppliers/_search',null,(select id from service where name='Supplier'),3,'Search Supplier',false,1,now(),1,now(),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createSupplier'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateSupplier'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchSupplier'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createSupplier'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateSupplier'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchSupplier'),'default');

--Fund
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Fund_MS', 'Fund_MS', false, '/egf-masters', (select id from service where name='Financials_Masters_MS'), 'Fund', 6,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'createFund','createFund',
'/funds/_create',null,(select id from service where name='Fund_MS'),1,'Create Fund',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'updateFund','updateFund',
'/funds/{id}/_update',null,(select id from service where name='Fund_MS'),2,'Modify Fund',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'searchFund','searchFund',
'/funds/_search',null,(select id from service where name='Fund_MS'),3,'Search Fund',false,1,now(),1,now(),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createFund'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateFund'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchFund'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createFund'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateFund'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchFund'),'default');


--subschemes
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'SubScheme', 'SubScheme', false, '/egf-masters', (select id from service where name='Financials_Masters_MS'), 'SubScheme', 7,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'createSubScheme_MS','createSubScheme_MS',
'/subschemes/_create',null,(select id from service where name='SubScheme'),1,'Create SubScheme',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'updateSubScheme_MS','updateSubScheme_MS',
'/subschemes/{id}/_update',null,(select id from service where name='SubScheme'),2,'Modify SubScheme',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'searchSubScheme_MS','searchSubScheme_MS',
'/subschemes/_search',null,(select id from service where name='SubScheme'),3,'Search SubScheme',false,1,now(),1,now(),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createSubScheme_MS'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateSubScheme_MS'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchSubScheme_MS'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createSubScheme_MS'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateSubScheme_MS'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchSubScheme_MS'),'default');

--functions
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Function_MS', 'Function_MS', false, '/egf-masters', (select id from service where name='Financials_Masters_MS'), 'Function', 8,'default');
insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'createFunction_MS','createFunction_MS',
'/functions/_create',null,(select id from service where name='Function_MS'),1,'Create Function',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'updateFunction_MS','updateFunction_MS',
'/functions/{id}/_update',null,(select id from service where name='Function_MS'),2,'Modify Function',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'searchFunction_MS','searchFunction_MS',
'/functions/_search',null,(select id from service where name='Function_MS'),3,'Search Function',false,1,now(),1,now(),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createFunction_MS'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateFunction_MS'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchFunction_MS'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createFunction_MS'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateFunction_MS'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchFunction_MS'),'default');

--budgetgroups
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'BudgetGroup', 'BudgetGroup', false, '/egf-masters', (select id from service where name='Financials_Masters_MS'), 'Budget Group', 9,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'createBudgetGroup_MS','createBudgetGroup_MS',
'/budgetgroups/_create',null,(select id from service where name='BudgetGroup'),1,'Create Budget Group',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'updateBudgetGroup_MS','updateBudgetGroup_MS',
'/budgetgroups/{id}/_update',null,(select id from service where name='BudgetGroup'),2,'Modify Budget Group',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'searchBudgetGroup_MS','searchBudgetGroup_MS',
'/budgetgroups/_search',null,(select id from service where name='BudgetGroup'),3,'Search Budget Group',false,1,now(),1,now(),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createBudgetGroup_MS'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateBudgetGroup_MS'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchBudgetGroup_MS'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createBudgetGroup_MS'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateBudgetGroup_MS'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchBudgetGroup_MS'),'default');


--schemes
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
 (nextval('seq_service'), 'Scheme', 'Scheme', false, '/egf-masters', (select id from service where name='Financials_Masters_MS'), 'Scheme', 10,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'createScheme_MS','createScheme_MS',
'/schemes/_create',null,(select id from service where name='Scheme'),1,'Create Scheme',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'updateScheme_MS','updateScheme_MS',
'/schemes/{id}/_update',null,(select id from service where name='Scheme'),2,'Modify Scheme',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'searchScheme_MS','searchScheme_MS',
'/schemes/_search',null,(select id from service where name='Scheme'),3,'Search Scheme',false,1,now(),1,now(),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createScheme_MS'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateScheme_MS'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchScheme_MS'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createScheme_MS'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateScheme_MS'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchScheme_MS'),'default');

--bank brances
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'BankBranch', 'BankBranch', false, '/egf-masters', (select id from service where name='Financials_Masters_MS'), 'Bank Branch', 11,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'createBankBranch','createBankBranch',
'/bankbranches/_create',null,(select id from service where name='BankBranch'),1,'Create BankBranch',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'updateBankBranch','updateBankBranch',
'/bankbranches/{id}/_update',null,(select id from service where name='BankBranch'),2,'Modify BankBranch',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'searchBankBranch','searchBankBranch',
'/bankbranches/_search',null,(select id from service where name='BankBranch'),3,'Search BankBranch',false,1,now(),1,now(),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createBankBranch'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateBankBranch'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchBankBranch'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createBankBranch'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateBankBranch'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchBankBranch'),'default');

--fundsources
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'FundSource', 'FundSource', false, '/egf-masters', (select id from service where name='Financials_Masters_MS'), 'Fund Source', 12,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'createFundSource','createFundSource',
'/fundsources/_create',null,(select id from service where name='FundSource'),1,'Create Fund Source',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'updateFundSource','updateFundSource',
'/fundsources/{id}/_update',null,(select id from service where name='FundSource'),2,'Modify Fund Source',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'searchFundSource','searchFundSource',
'/fundsources/_search',null,(select id from service where name='FundSource'),3,'Search Fund Source',false,1,now(),1,now(),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createFundSource'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateFundSource'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchFundSource'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createFundSource'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateFundSource'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchFundSource'),'default');

--functionaries
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'Functionary', 'Functionary', false, '/egf-masters', (select id from service where name='Financials_Masters_MS'), 'Functionary', 13,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'createFunctionary','createFunctionary',
'/functionaries/_create',null,(select id from service where name='Functionary'),1,'Create Functionary',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'updateFunctionary','updateFunctionary',
'/functionaries/{id}/_update',null,(select id from service where name='Functionary'),2,'Modify Functionary',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'searchFunctionary','searchFunctionary',
'/functionaries/_search',null,(select id from service where name='Functionary'),3,'Search Functionary',false,1,now(),1,now(),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createFunctionary'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateFunctionary'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchFunctionary'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createFunctionary'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateFunctionary'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchFunctionary'),'default');

--COA
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'ChartOfAccount', 'ChartOfAccount', false, '/egf-masters', (select id from service where name='Financials_Masters_MS'), 'Chart Of Accounts', 14,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'createChartOfAccount','createChartOfAccount',
'/chartofaccounts/_create',null,(select id from service where name='ChartOfAccount'),1,'Create Chart Of Accounts',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'updateChartOfAccount','updateChartOfAccount',
'/chartofaccounts/{id}/_update',null,(select id from service where name='ChartOfAccount'),2,'Modify Chart Of Accounts',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'searchChartOfAccount','searchChartOfAccount',
'/chartofaccounts/_search',null,(select id from service where name='ChartOfAccount'),3,'Search Chart Of Accounts',false,1,now(),1,now(),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createChartOfAccount'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateChartOfAccount'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchChartOfAccount'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createChartOfAccount'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateChartOfAccount'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchChartOfAccount'),'default');

--COADetails
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'ChartOfAccountDetail', 'ChartOfAccountDetail', false, '/egf-masters', (select id from service where name='Financials_Masters_MS'), 'Chart Of Accounts Detail', 15,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'createChartOfAccountDetail','createChartOfAccountDetail',
'/chartofaccountdetails/_create',null,(select id from service where name='ChartOfAccountDetail'),1,'Create Chart Of Accounts Detail',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'updateChartOfAccountDetail','updateChartOfAccountDetail',
'/chartofaccountdetails/{id}/_update',null,(select id from service where name='ChartOfAccountDetail'),2,'Modify Chart Of Accounts Detail',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'searchChartOfAccountDetail','searchChartOfAccountDetail',
'/chartofaccountdetails/_search',null,(select id from service where name='ChartOfAccountDetail'),3,'Search Chart Of Accounts Detail',false,1,now(),1,now(),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createChartOfAccountDetail'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateChartOfAccountDetail'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchChartOfAccountDetail'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createChartOfAccountDetail'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateChartOfAccountDetail'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchChartOfAccountDetail'),'default');

--accountentities
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'AccountEntiy', 'AccountEntiy', false, '/egf-masters', (select id from service where name='Financials_Masters_MS'), 'Account Entity', 16,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'createAccountEntiy','createAccountEntiy',
'/accountentities/_create',null,(select id from service where name='AccountEntiy'),1,'Create Account Entity',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'updateAccountEntiy','updateAccountEntiy',
'/accountentities/{id}/_update',null,(select id from service where name='AccountEntiy'),2,'Modify Account Entity',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'searchAccountEntiy','searchAccountEntiy',
'/accountentities/_search',null,(select id from service where name='AccountEntiy'),3,'Search Account Entity',false,1,now(),1,now(),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createAccountEntiy'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateAccountEntiy'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchAccountEntiy'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createAccountEntiy'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateAccountEntiy'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchAccountEntiy'),'default');

--financialyears
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'FinancialYear', 'FinancialYear', false, '/egf-masters', (select id from service where name='Financials_Masters_MS'), 'Financial Year', 17,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'createFinancialYear','createFinancialYear',
'/financialyears/_create',null,(select id from service where name='FinancialYear'),1,'Create Financial Year',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'updateFinancialYear','updateFinancialYear',
'/financialyears/{id}/_update',null,(select id from service where name='FinancialYear'),2,'Modify Financial Year',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'searchFinancialYear','searchFinancialYear',
'/financialyears/_search',null,(select id from service where name='FinancialYear'),3,'Search Financial Year',false,1,now(),1,now(),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createFinancialYear'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateFinancialYear'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchFinancialYear'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createFinancialYear'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateFinancialYear'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchFinancialYear'),'default');

--fiscalperiods
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'FiscalPeriod', 'FiscalPeriod', false, '/egf-masters', (select id from service where name='Financials_Masters_MS'), 'Fiscal Period', 18,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'createFiscalPeriod','createFiscalPeriod',
'/fiscalperiods/_create',null,(select id from service where name='FiscalPeriod'),1,'Create Fiscal Period',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'updateFiscalPeriod','updateFiscalPeriod',
'/fiscalperiods/{id}/_update',null,(select id from service where name='FiscalPeriod'),2,'Modify Fiscal Period',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'searchFiscalPeriod','searchFiscalPeriod',
'/fiscalperiods/_search',null,(select id from service where name='FiscalPeriod'),3,'Search Fiscal Period',false,1,now(),1,now(),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createFiscalPeriod'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateFiscalPeriod'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchFiscalPeriod'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createFiscalPeriod'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateFiscalPeriod'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchFiscalPeriod'),'default');

--accountdetailtypes
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'AccountDetailType', 'AccountDetailType', false, '/egf-masters', (select id from service where name='Financials_Masters_MS'), 'Account Detail Type', 19,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'createAccountDetailType','createAccountDetailType',
'/accountdetailtypes/_create',null,(select id from service where name='AccountDetailType'),1,'Create Account Detail Type',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'updateAccountDetailType','updateAccountDetailType',
'/accountdetailtypes/{id}/_update',null,(select id from service where name='AccountDetailType'),2,'Modify Account Detail Type',false,1,now(),1,now(),'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'searchAccountDetailType','searchAccountDetailType',
'/accountdetailtypes/_search',null,(select id from service where name='AccountDetailType'),3,'Search Account Detail Type',false,1,now(),1,now(),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createAccountDetailType'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateAccountDetailType'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchAccountDetailType'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createAccountDetailType'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateAccountDetailType'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchAccountDetailType'),'default');
