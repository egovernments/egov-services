INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Financials', 'Financials', false, '/egf-masters', NULL, 'Financials', 20,'default');
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Financials_Masters', 'Financials_Masters', false, '/egf-masters', (select id from service where name='Financials'), 'Masters', 20,'default');
--Account Detail key
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Account Detail key', 'Account Detail key', false, '/egf-masters', (select id from service where name='Financials_Masters'), 'Account Detail key', 1,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Account Detail key'),'createAccountDetailKey',
'/egf-masters/accountdetailkeys/_create',null,(select id from service where name='Account Detail key'),1,'Create Account Detail Key',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Account Detail key'),'updateAccountDetailKey',
'/egf-masters/accountdetailkeys/_update',null,(select id from service where name='Account Detail key'),2,'Modify Account Detail Key',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Account Detail key'),'searchAccountDetailKey',
'/egf-masters/accountdetailkeys/_search',null,(select id from service where name='Account Detail key'),3,'Search Account Detail Key',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createAccountDetailKey'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateAccountDetailKey'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchAccountDetailKey'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createAccountDetailKey'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateAccountDetailKey'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchAccountDetailKey'),'default');

--Bank
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Bank', 'Bank', false, '/egf-masters', (select id from service where name='Financials_Masters'), 'Bank', 2,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Bank'),'createBank',
'/egf-masters/banks/_create',null,(select id from service where name='Bank'),1,'Create Bank',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Bank'),'updateBank',
'/egf-masters/banks/_update',null,(select id from service where name='Bank'),3,'Modify Bank',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Bank'),'searchBank',
'/egf-masters/banks/_search',null,(select id from service where name='Bank'),3,'Search Bank',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createBank'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateBank'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchBank'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createBank'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateBank'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchBank'),'default');

--Bankaccounts
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Bankaccount', 'Bankaccount', false, '/egf-masters', (select id from service where name='Financials_Masters'), 'Bankaccount', 3,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Bankaccount'),'createBankAccount',
'/egf-masters/bankaccounts/_create',null,(select id from service where name='Bankaccount'),1,'Create BankAccount',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Bankaccount'),'updateBankAccount',
'/egf-masters/bankaccounts/_update',null,(select id from service where name='Bankaccount'),2,'Modify BankAccount',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Bankaccount'),'searchBankAccount',
'/egf-masters/bankaccounts/_search',null,(select id from service where name='Bankaccount'),3,'Search BankAccount',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createBankAccount'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateBankAccount'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchBankAccount'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createBankAccount'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateBankAccount'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchBankAccount'),'default');

--Account Code Purpose
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'AccountCodePurpose', 'AccountCodePurpose', false, '/egf-masters', (select id from service where name='Financials_Masters'), 'Account Code Purpose', 4,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='AccountCodePurpose'),'createAccountCodePurpose',
'/egf-masters/accountcodepurposes/_create',null,(select id from service where name='AccountCodePurpose'),1,'Create Account Code Purpose',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='AccountCodePurpose'),'updateAccountCodePurpose',
'/egf-masters/accountcodepurposes/_update',null,(select id from service where name='AccountCodePurpose'),2,'Modify Account Code Purpose',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='AccountCodePurpose'),'searchAccountCodePurpose',
'/egf-masters/accountcodepurposes/_search',null,(select id from service where name='AccountCodePurpose'),3,'Search Account Code Purpose',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createAccountCodePurpose'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateAccountCodePurpose'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchAccountCodePurpose'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createAccountCodePurpose'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateAccountCodePurpose'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchAccountCodePurpose'),'default');


--Supplier
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Supplier', 'Supplier', false, '/egf-masters', (select id from service where name='Financials_Masters'), 'Supplier', 5,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Supplier'),'createSupplier',
'/egf-masters/suppliers/_create',null,(select id from service where name='Supplier'),1,'Create Supplier',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Supplier'),'updateSupplier',
'/egf-masters/suppliers/_update',null,(select id from service where name='Supplier'),2,'Modify Supplier',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Supplier'),'searchSupplier',
'/egf-masters/suppliers/_search',null,(select id from service where name='Supplier'),3,'Search Supplier',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createSupplier'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateSupplier'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchSupplier'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createSupplier'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateSupplier'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchSupplier'),'default');

--Fund
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Fund', 'Fund', false, '/egf-masters', (select id from service where name='Financials_Masters'), 'Fund', 6,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Fund'),'createFund',
'/egf-masters/funds/_create',null,(select id from service where name='Fund'),1,'Create Fund',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Fund'),'updateFund',
'/egf-masters/funds/_update',null,(select id from service where name='Fund'),2,'Modify Fund',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Fund'),'searchFund',
'/egf-masters/funds/_search',null,(select id from service where name='Fund'),3,'Search Fund',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createFund'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateFund'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchFund'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createFund'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateFund'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchFund'),'default');


--subschemes
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'SubScheme', 'SubScheme', false, '/egf-masters', (select id from service where name='Financials_Masters'), 'SubScheme', 7,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='SubScheme'),'createSubScheme',
'/egf-masters/subschemes/_create',null,(select id from service where name='SubScheme'),1,'Create SubScheme',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='SubScheme'),'updateSubScheme',
'/egf-masters/subschemes/_update',null,(select id from service where name='SubScheme'),2,'Modify SubScheme',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='SubScheme'),'searchSubScheme',
'/egf-masters/subschemes/_search',null,(select id from service where name='SubScheme'),3,'Search SubScheme',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createSubScheme'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateSubScheme'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchSubScheme'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createSubScheme'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateSubScheme'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchSubScheme'),'default');

--functions
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Function', 'Function', false, '/egf-masters', (select id from service where name='Financials_Masters'), 'Function', 8,'default');
insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Function'),'createFunction',
'/egf-masters/functions/_create',null,(select id from service where name='Function'),1,'Create Function',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Function'),'updateFunction',
'/egf-masters/functions/_update',null,(select id from service where name='Function'),2,'Modify Function',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Function'),'searchFunction',
'/egf-masters/functions/_search',null,(select id from service where name='Function'),3,'Search Function',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createFunction'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateFunction'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchFunction'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createFunction'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateFunction'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchFunction'),'default');

--budgetgroups
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'BudgetGroup', 'BudgetGroup', false, '/egf-masters', (select id from service where name='Financials_Masters'), 'Budget Group', 9,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='BudgetGroup'),'createBudgetGroup',
'/egf-masters/budgetgroups/_create',null,(select id from service where name='BudgetGroup'),1,'Create Budget Group',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='BudgetGroup'),'updateBudgetGroup',
'/egf-masters/budgetgroups/_update',null,(select id from service where name='BudgetGroup'),2,'Modify Budget Group',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='BudgetGroup'),'searchBudgetGroup',
'/egf-masters/budgetgroups/_search',null,(select id from service where name='BudgetGroup'),3,'Search Budget Group',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createBudgetGroup'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateBudgetGroup'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchBudgetGroup'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createBudgetGroup'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateBudgetGroup'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchBudgetGroup'),'default');


--schemes
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
 (nextval('seq_service'), 'Scheme', 'Scheme', false, '/egf-masters', (select id from service where name='Financials_Masters'), 'Scheme', 10,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Scheme'),'createScheme',
'/egf-masters/schemes/_create',null,(select id from service where name='Scheme'),1,'Create Scheme',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Scheme'),'updateScheme',
'/egf-masters/schemes/_update',null,(select id from service where name='Scheme'),2,'Modify Scheme',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Scheme'),'searchScheme',
'/egf-masters/schemes/_search',null,(select id from service where name='Scheme'),3,'Search Scheme',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createScheme'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateScheme'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchScheme'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createScheme'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateScheme'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchScheme'),'default');

--bank brances
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'BankBranch', 'BankBranch', false, '/egf-masters', (select id from service where name='Financials_Masters'), 'Bank Branch', 11,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='BankBranch'),'createBankBranch',
'/egf-masters/bankbranches/_create',null,(select id from service where name='BankBranch'),1,'Create BankBranch',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='BankBranch'),'updateBankBranch',
'/egf-masters/bankbranches/_update',null,(select id from service where name='BankBranch'),2,'Modify BankBranch',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='BankBranch'),'searchBankBranch',
'/egf-masters/bankbranches/_search',null,(select id from service where name='BankBranch'),3,'Search BankBranch',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createBankBranch'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateBankBranch'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchBankBranch'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createBankBranch'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateBankBranch'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchBankBranch'),'default');

--fundsources
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'FundSource', 'FundSource', false, '/egf-masters', (select id from service where name='Financials_Masters'), 'Fund Source', 12,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='FundSource'),'createFundSource',
'/egf-masters/fundsources/_create',null,(select id from service where name='FundSource'),1,'Create Fund Source',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='FundSource'),'updateFundSource',
'/egf-masters/fundsources/_update',null,(select id from service where name='FundSource'),2,'Modify Fund Source',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='FundSource'),'searchFundSource',
'/egf-masters/fundsources/_search',null,(select id from service where name='FundSource'),3,'Search Fund Source',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createFundSource'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateFundSource'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchFundSource'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createFundSource'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateFundSource'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchFundSource'),'default');

--functionaries
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'Functionary', 'Functionary', false, '/egf-masters', (select id from service where name='Financials_Masters'), 'Functionary', 13,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Functionary'),'createFunctionary',
'/egf-masters/functionaries/_create',null,(select id from service where name='Functionary'),1,'Create Functionary',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Functionary'),'updateFunctionary',
'/egf-masters/functionaries/_update',null,(select id from service where name='Functionary'),2,'Modify Functionary',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Functionary'),'searchFunctionary',
'/egf-masters/functionaries/_search',null,(select id from service where name='Functionary'),3,'Search Functionary',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createFunctionary'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateFunctionary'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchFunctionary'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createFunctionary'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateFunctionary'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchFunctionary'),'default');

--COA
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'ChartOfAccount', 'ChartOfAccount', false, '/egf-masters', (select id from service where name='Financials_Masters'), 'Chart Of Accounts', 14,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='ChartOfAccount'),'createChartOfAccount',
'/egf-masters/chartofaccounts/_create',null,(select id from service where name='ChartOfAccount'),1,'Create Chart Of Accounts',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='ChartOfAccount'),'updateChartOfAccount',
'/egf-masters/chartofaccounts/_update',null,(select id from service where name='ChartOfAccount'),2,'Modify Chart Of Accounts',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='ChartOfAccount'),'searchChartOfAccount',
'/egf-masters/chartofaccounts/_search',null,(select id from service where name='ChartOfAccount'),3,'Search Chart Of Accounts',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createChartOfAccount'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateChartOfAccount'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchChartOfAccount'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createChartOfAccount'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateChartOfAccount'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchChartOfAccount'),'default');

--COADetails
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'ChartOfAccountDetail', 'ChartOfAccountDetail', false, '/egf-masters', (select id from service where name='Financials_Masters'), 'Chart Of Accounts Detail', 15,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='ChartOfAccountDetail'),'createChartOfAccountDetail',
'/egf-masters/chartofaccountdetails/_create',null,(select id from service where name='ChartOfAccountDetail'),1,'Create Chart Of Accounts Detail',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='ChartOfAccountDetail'),'updateChartOfAccountDetail',
'/egf-masters/chartofaccountdetails/_update',null,(select id from service where name='ChartOfAccountDetail'),2,'Modify Chart Of Accounts Detail',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='ChartOfAccountDetail'),'searchChartOfAccountDetail',
'/egf-masters/chartofaccountdetails/_search',null,(select id from service where name='ChartOfAccountDetail'),3,'Search Chart Of Accounts Detail',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createChartOfAccountDetail'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateChartOfAccountDetail'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchChartOfAccountDetail'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createChartOfAccountDetail'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateChartOfAccountDetail'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchChartOfAccountDetail'),'default');

--accountentities
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'AccountEntiy', 'AccountEntiy', false, '/egf-masters', (select id from service where name='Financials_Masters'), 'Account Entity', 16,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='AccountEntiy'),'createAccountEntiy',
'/egf-masters/accountentities/_create',null,(select id from service where name='AccountEntiy'),1,'Create Account Entity',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='AccountEntiy'),'updateAccountEntiy',
'/egf-masters/accountentities/_update',null,(select id from service where name='AccountEntiy'),2,'Modify Account Entity',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='AccountEntiy'),'searchAccountEntiy',
'/egf-masters/accountentities/_search',null,(select id from service where name='AccountEntiy'),3,'Search Account Entity',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createAccountEntiy'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateAccountEntiy'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchAccountEntiy'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createAccountEntiy'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateAccountEntiy'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchAccountEntiy'),'default');

--financialyears
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'FinancialYear', 'FinancialYear', false, '/egf-masters', (select id from service where name='Financials_Masters'), 'Financial Year', 17,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='FinancialYear'),'createFinancialYear',
'/egf-masters/financialyears/_create',null,(select id from service where name='FinancialYear'),1,'Create Financial Year',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='FinancialYear'),'updateFinancialYear',
'/egf-masters/financialyears/_update',null,(select id from service where name='FinancialYear'),2,'Modify Financial Year',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='FinancialYear'),'searchFinancialYear',
'/egf-masters/financialyears/_search',null,(select id from service where name='FinancialYear'),3,'Search Financial Year',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createFinancialYear'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateFinancialYear'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchFinancialYear'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createFinancialYear'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateFinancialYear'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchFinancialYear'),'default');

--fiscalperiods
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'FiscalPeriod', 'FiscalPeriod', false, '/egf-masters', (select id from service where name='Financials_Masters'), 'Fiscal Period', 18,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='FiscalPeriod'),'createFiscalPeriod',
'/egf-masters/fiscalperiods/_create',null,(select id from service where name='FiscalPeriod'),1,'Create Fiscal Period',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='FiscalPeriod'),'updateFiscalPeriod',
'/egf-masters/fiscalperiods/_update',null,(select id from service where name='FiscalPeriod'),2,'Modify Fiscal Period',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='FiscalPeriod'),'searchFiscalPeriod',
'/egf-masters/fiscalperiods/_search',null,(select id from service where name='FiscalPeriod'),3,'Search Fiscal Period',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createFiscalPeriod'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateFiscalPeriod'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchFiscalPeriod'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createFiscalPeriod'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateFiscalPeriod'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchFiscalPeriod'),'default');

--accountdetailtypes
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'AccountDetailType', 'AccountDetailType', false, '/egf-masters', (select id from service where name='Financials_Masters'), 'Account Detail Type', 19,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='AccountDetailType'),'createAccountDetailType',
'/egf-masters/accountdetailtypes/_create',null,(select id from service where name='AccountDetailType'),1,'Create Account Detail Type',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='AccountDetailType'),'updateAccountDetailType',
'/egf-masters/accountdetailtypes/_update',null,(select id from service where name='AccountDetailType'),2,'Modify Account Detail Type',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='AccountDetailType'),'searchAccountDetailType',
'/egf-masters/accountdetailtypes/_search',null,(select id from service where name='AccountDetailType'),3,'Search Account Detail Type',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createAccountDetailType'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateAccountDetailType'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchAccountDetailType'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createAccountDetailType'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateAccountDetailType'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchAccountDetailType'),'default');

INSERT INTO EG_ROLEACTION (ROLECODE, ACTIONID, TENANTID) values ('EMPLOYEE ADMIN', (select id from eg_action where name = 'searchBank'), 'default');
INSERT INTO EG_ROLEACTION (ROLECODE, ACTIONID, TENANTID) values ('EMPLOYEE ADMIN', (select id from eg_action where name = 'searchBankBranch'), 'default');
INSERT INTO EG_ROLEACTION (ROLECODE, ACTIONID, TENANTID) values ('EMPLOYEE ADMIN', (select id from eg_action where name = 'searchFund'), 'default');
INSERT INTO EG_ROLEACTION (ROLECODE, ACTIONID, TENANTID) values ('EMPLOYEE ADMIN', (select id from eg_action where name = 'searchFunction'), 'default');
INSERT INTO EG_ROLEACTION (ROLECODE, ACTIONID, TENANTID) values ('EMPLOYEE ADMIN', (select id from eg_action where name = 'searchFunctionary'), 'default');

