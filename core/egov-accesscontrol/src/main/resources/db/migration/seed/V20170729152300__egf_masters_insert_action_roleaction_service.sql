INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Financial_Master', 'Financial_Master', false, '/egf-master', NULL, 'Financials', 20,'default');
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Financials_Master', 'Financials_Master', false, '/egf-master', (select id from service where name='Financial_Master'), 'Masters', 20,'default');
--Account Detail key for egf_master service not for egf_masters
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Account_Detail_key_Master', 'Account_Detail_key_Master', false, '/egf-master', (select id from service where name='Financials_Master'), 'Account Detail key', 1,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Account_Detail_key_Master'),'createAccountDetailKey_Master',
'/egf-master/accountdetailkeys/_create',null,(select id from service where name='Account_Detail_key_Master'),1,'Create Account Detail Key',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Account_Detail_key_Master'),'updateAccountDetailKey_Master',
'/egf-master/accountdetailkeys/_update',null,(select id from service where name='Account_Detail_key_Master'),2,'Modify Account Detail Key',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Account_Detail_key_Master'),'searchAccountDetailKey_Master',
'/egf-master/accountdetailkeys/_search',null,(select id from service where name='Account_Detail_key_Master'),3,'Search Account Detail Key',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createAccountDetailKey_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateAccountDetailKey_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchAccountDetailKey_Master'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('SUPERUSER', (select id from eg_action where name = 'createAccountDetailKey_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('SUPERUSER', (select id from eg_action where name = 'updateAccountDetailKey_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('SUPERUSER', (select id from eg_action where name = 'searchAccountDetailKey_Master'),'default');

--Bank
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Bank_Master', 'Bank_Master', false, '/egf-master', (select id from service where name='Financials_Master'), 'Bank', 2,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Bank'),'createBank_Master',
'/egf-master/banks/_create',null,(select id from service where name='Bank_Master'),1,'Create Bank',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Bank'),'updateBank_Master',
'/egf-master/banks/_update',null,(select id from service where name='Bank_Master'),3,'Modify Bank',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Bank'),'searchBank_Master',
'/egf-master/banks/_search',null,(select id from service where name='Bank_Master'),3,'Search Bank',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createBank_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateBank_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchBank_Master'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('SUPERUSER', (select id from eg_action where name = 'createBank_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('SUPERUSER', (select id from eg_action where name = 'updateBank_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('SUPERUSER', (select id from eg_action where name = 'searchBank_Master'),'default');

--Bankaccounts
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Bank_Account_Master', 'Bank_Account_Master', false, '/egf-master', (select id from service where name='Financials_Master'), 'Bank Account', 3,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Bank_Account_Master'),'createBankAccount_Master',
'/egf-master/bankaccounts/_create',null,(select id from service where name='Bank_Account_Master'),1,'Create BankAccount',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Bank_Account_Master'),'updateBankAccount_Master',
'/egf-master/bankaccounts/_update',null,(select id from service where name='Bank_Account_Master'),2,'Modify BankAccount',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Bank_Account_Master'),'searchBankAccount_Master',
'/egf-master/bankaccounts/_search',null,(select id from service where name='Bank_Account_Master'),3,'Search BankAccount',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createBankAccount_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateBankAccount_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchBankAccount_Master'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('SUPERUSER', (select id from eg_action where name = 'createBankAccount_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('SUPERUSER', (select id from eg_action where name = 'updateBankAccount_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('SUPERUSER', (select id from eg_action where name = 'searchBankAccount_Master'),'default');

--Account Code Purpose
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Account_Code_Purpose_Master', 'Account_Code_Purpose_Master', false, '/egf-master', (select id from service where name='Financials_Master'), 'Account Code Purpose', 4,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Account_Code_Purpose_Master'),'createAccountCodePurpose_Master',
'/egf-master/accountcodepurposes/_create',null,(select id from service where name='Account_Code_Purpose_Master'),1,'Create Account Code Purpose',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Account_Code_Purpose_Master'),'updateAccountCodePurpose_Master',
'/egf-master/accountcodepurposes/_update',null,(select id from service where name='Account_Code_Purpose_Master'),2,'Modify Account Code Purpose',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Account_Code_Purpose_Master'),'searchAccountCodePurpose_Master',
'/egf-master/accountcodepurposes/_search',null,(select id from service where name='Account_Code_Purpose_Master'),3,'Search Account Code Purpose',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createAccountCodePurpose_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateAccountCodePurpose_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchAccountCodePurpose_Master'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('SUPERUSER', (select id from eg_action where name = 'createAccountCodePurpose_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('SUPERUSER', (select id from eg_action where name = 'updateAccountCodePurpose_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('SUPERUSER', (select id from eg_action where name = 'searchAccountCodePurpose_Master'),'default');


--Supplier
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Supplier_Master', 'Supplier_Master', false, '/egf-master', (select id from service where name='Financials_Master'), 'Supplier', 5,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Supplier_Master'),'createSupplier_Master',
'/egf-master/suppliers/_create',null,(select id from service where name='Supplier_Master'),1,'Create Supplier',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Supplier_Master'),'updateSupplier_Master',
'/egf-master/suppliers/_update',null,(select id from service where name='Supplier_Master'),2,'Modify Supplier',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Supplier_Master'),'searchSupplier_Master',
'/egf-master/suppliers/_search',null,(select id from service where name='Supplier_Master'),3,'Search Supplier',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createSupplier_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateSupplier_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchSupplier_Master'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('SUPERUSER', (select id from eg_action where name = 'createSupplier_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('SUPERUSER', (select id from eg_action where name = 'updateSupplier_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('SUPERUSER', (select id from eg_action where name = 'searchSupplier_Master'),'default');

--Fund
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Fund_Master', 'Fund_Master', false, '/egf-master', (select id from service where name='Financials_Master'), 'Fund', 6,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Fund_Master'),'createFund_Master',
'/egf-master/funds/_create',null,(select id from service where name='Fund_Master'),1,'Create Fund',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Fund_Master'),'updateFund_Master',
'/egf-master/funds/_update',null,(select id from service where name='Fund_Master'),2,'Modify Fund',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Fund_Master'),'searchFund_Master',
'/egf-master/funds/_search',null,(select id from service where name='Fund_Master'),3,'Search Fund',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createFund_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateFund_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchFund_Master'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('SUPERUSER', (select id from eg_action where name = 'createFund_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('SUPERUSER', (select id from eg_action where name = 'updateFund_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('SUPERUSER', (select id from eg_action where name = 'searchFund_Master'),'default');


--subschemes
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'SubScheme_Master', 'SubScheme_Master', false, '/egf-master', (select id from service where name='Financials_Master'), 'SubScheme', 7,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='SubScheme_Master'),'createSubScheme_Master',
'/egf-master/subschemes/_create',null,(select id from service where name='SubScheme_Master'),1,'Create SubScheme',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='SubScheme_Master'),'updateSubScheme_Master',
'/egf-master/subschemes/_update',null,(select id from service where name='SubScheme_Master'),2,'Modify SubScheme',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='SubScheme_Master'),'searchSubScheme_Master',
'/egf-master/subschemes/_search',null,(select id from service where name='SubScheme_Master'),3,'Search SubScheme',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createSubScheme_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateSubScheme_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchSubScheme_Master'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('SUPERUSER', (select id from eg_action where name = 'createSubScheme_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('SUPERUSER', (select id from eg_action where name = 'updateSubScheme_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('SUPERUSER', (select id from eg_action where name = 'searchSubScheme_Master'),'default');


--done
--functions
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Function_Master', 'Function_Master', false, '/egf-master', (select id from service where name='Financials_Masters'), 'Function', 8,'default');
insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Function_Master'),'createFunction_Master',
'/egf-master/functions/_create',null,(select id from service where name='Function_Master'),1,'Create Function',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Function_Master'),'updateFunction_Master',
'/egf-master/functions/_update',null,(select id from service where name='Function_Master'),2,'Modify Function',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Function_Master'),'searchFunction_Master',
'/egf-master/functions/_search',null,(select id from service where name='Function_Master'),3,'Search Function',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createFunction_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateFunction_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchFunction_Master'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createFunction_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateFunction_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchFunction_Master'),'default');

--budgetgroups
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'BudgetGroup_Master', 'BudgetGroup_Master', false, '/egf-master', (select id from service where name='Financials_Masters'), 'Budget Group', 9,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='BudgetGroup_Master'),'createBudgetGroup_Master',
'/egf-master/budgetgroups/_create',null,(select id from service where name='BudgetGroup_Master'),1,'Create Budget Group',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='BudgetGroup_Master'),'updateBudgetGroup_Master',
'/egf-master/budgetgroups/_update',null,(select id from service where name='BudgetGroup_Master'),2,'Modify Budget Group',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='BudgetGroup_Master'),'searchBudgetGroup_Master',
'/egf-master/budgetgroups/_search',null,(select id from service where name='BudgetGroup_Master'),3,'Search Budget Group',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createBudgetGroup_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateBudgetGroup_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchBudgetGroup_Master'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createBudgetGroup_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateBudgetGroup_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchBudgetGroup_Master'),'default');


--schemes
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
 (nextval('seq_service'), 'Scheme_Master', 'Scheme_Master', false, '/egf-master', (select id from service where name='Financials_Masters'), 'Scheme', 10,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Scheme_Master'),'createScheme_Master',
'/egf-master/schemes/_create',null,(select id from service where name='Scheme_Master'),1,'Create Scheme',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Scheme_Master'),'updateScheme_Master',
'/egf-master/schemes/_update',null,(select id from service where name='Scheme_Master'),2,'Modify Scheme',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Scheme_Master'),'searchScheme_Master',
'/egf-master/schemes/_search',null,(select id from service where name='Scheme_Master'),3,'Search Scheme',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createScheme_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateScheme_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchScheme_Master'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createScheme_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateScheme_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchScheme_Master'),'default');

--bank brances
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'BankBranch_Master', 'BankBranch_Master', false, '/egf-master', (select id from service where name='Financials_Masters'), 'Bank Branch', 11,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='BankBranch_Master'),'createBankBranch_Master',
'/egf-master/bankbranches/_create',null,(select id from service where name='BankBranch_Master'),1,'Create BankBranch',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='BankBranch_Master'),'updateBankBranch_Master',
'/egf-master/bankbranches/_update',null,(select id from service where name='BankBranch_Master'),2,'Modify BankBranch',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='BankBranch_Master'),'searchBankBranch_Master',
'/egf-master/bankbranches/_search',null,(select id from service where name='BankBranch_Master'),3,'Search BankBranch',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createBankBranch_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateBankBranch_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchBankBranch_Master'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createBankBranch_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateBankBranch_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchBankBranch_Master'),'default');

--fundsources
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'FundSource_Master', 'FundSource_Master', false, '/egf-master', (select id from service where name='Financials_Masters'), 'Fund Source', 12,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='FundSource_Master'),'createFundSource_Master',
'/egf-master/fundsources/_create',null,(select id from service where name='FundSource_Master'),1,'Create Fund Source',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='FundSource_Master'),'updateFundSource_Master',
'/egf-master/fundsources/_update',null,(select id from service where name='FundSource_Master'),2,'Modify Fund Source',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='FundSource_Master'),'searchFundSource_Master',
'/egf-master/fundsources/_search',null,(select id from service where name='FundSource_Master'),3,'Search Fund Source',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createFundSource_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateFundSource_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchFundSource_Master'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createFundSource_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateFundSource_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchFundSource_Master'),'default');

--functionaries
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'Functionary_Master', 'Functionary_Master', false, '/egf-master', (select id from service where name='Financials_Masters'), 'Functionary', 13,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Functionary_Master'),'createFunctionary_Master',
'/egf-master/functionaries/_create',null,(select id from service where name='Functionary_Master'),1,'Create Functionary',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Functionary_Master'),'updateFunctionary_Master',
'/egf-master/functionaries/_update',null,(select id from service where name='Functionary_Master'),2,'Modify Functionary',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Functionary_Master'),'searchFunctionary_Master',
'/egf-master/functionaries/_search',null,(select id from service where name='Functionary_Master'),3,'Search Functionary',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createFunctionary_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateFunctionary_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchFunctionary_Master'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createFunctionary_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateFunctionary_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchFunctionary_Master'),'default');

--COA
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'ChartOfAccount_Master', 'ChartOfAccount_Master', false, '/egf-master', (select id from service where name='Financials_Masters'), 'Chart Of Accounts', 14,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='ChartOfAccount_Master'),'createChartOfAccount_Master',
'/egf-master/chartofaccounts/_create',null,(select id from service where name='ChartOfAccount_Master'),1,'Create Chart Of Accounts',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='ChartOfAccount_Master'),'updateChartOfAccount_Master',
'/egf-master/chartofaccounts/_update',null,(select id from service where name='ChartOfAccount_Master'),2,'Modify Chart Of Accounts',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='ChartOfAccount_Master'),'searchChartOfAccount_Master',
'/egf-master/chartofaccounts/_search',null,(select id from service where name='ChartOfAccount_Master'),3,'Search Chart Of Accounts',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createChartOfAccount_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateChartOfAccount_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchChartOfAccount_Master'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createChartOfAccount_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateChartOfAccount_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchChartOfAccount_Master'),'default');

--COADetails
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'ChartOfAccountDetail_Master', 'ChartOfAccountDetail_Master', false, '/egf-master', (select id from service where name='Financials_Masters'), 'Chart Of Accounts Detail', 15,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='ChartOfAccountDetail_Master'),'createChartOfAccountDetail_Master',
'/egf-master/chartofaccountdetails/_create',null,(select id from service where name='ChartOfAccountDetail_Master'),1,'Create Chart Of Accounts Detail',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='ChartOfAccountDetail_Master'),'updateChartOfAccountDetail_Master',
'/egf-master/chartofaccountdetails/_update',null,(select id from service where name='ChartOfAccountDetail_Master'),2,'Modify Chart Of Accounts Detail',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='ChartOfAccountDetail_Master'),'searchChartOfAccountDetail_Master',
'/egf-master/chartofaccountdetails/_search',null,(select id from service where name='ChartOfAccountDetail_Master'),3,'Search Chart Of Accounts Detail',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createChartOfAccountDetail_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateChartOfAccountDetail_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchChartOfAccountDetail_Master'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createChartOfAccountDetail_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateChartOfAccountDetail_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchChartOfAccountDetail_Master'),'default');

--accountentities
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'AccountEntiy_Master', 'AccountEntiy_Master', false, '/egf-master', (select id from service where name='Financials_Masters'), 'Account Entity', 16,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='AccountEntiy_Master'),'createAccountEntiy_Master',
'/egf-master/accountentities/_create',null,(select id from service where name='AccountEntiy_Master'),1,'Create Account Entity',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='AccountEntiy_Master'),'updateAccountEntiy_Master',
'/egf-master/accountentities/_update',null,(select id from service where name='AccountEntiy_Master'),2,'Modify Account Entity',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='AccountEntiy_Master'),'searchAccountEntiy_Master',
'/egf-master/accountentities/_search',null,(select id from service where name='AccountEntiy_Master'),3,'Search Account Entity',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createAccountEntiy_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateAccountEntiy_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchAccountEntiy_Master'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createAccountEntiy_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateAccountEntiy_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchAccountEntiy_Master'),'default');

--financialyears
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'FinancialYear_Master', 'FinancialYear_Master', false, '/egf-master', (select id from service where name='Financials_Masters'), 'Financial Year', 17,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='FinancialYear_Master'),'createFinancialYear_Master',
'/egf-master/financialyears/_create',null,(select id from service where name='FinancialYear_Master'),1,'Create Financial Year',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='FinancialYear_Master'),'updateFinancialYear_Master',
'/egf-master/financialyears/_update',null,(select id from service where name='FinancialYear_Master'),2,'Modify Financial Year',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='FinancialYear_Master'),'searchFinancialYear_Master',
'/egf-master/financialyears/_search',null,(select id from service where name='FinancialYear_Master'),3,'Search Financial Year',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createFinancialYear_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateFinancialYear_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchFinancialYear_Master'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createFinancialYear_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateFinancialYear_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchFinancialYear_Master'),'default');

--fiscalperiods
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'FiscalPeriod_Master', 'FiscalPeriod_Master', false, '/egf-master', (select id from service where name='Financials_Masters'), 'Fiscal Period', 18,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='FiscalPeriod_Master'),'createFiscalPeriod_Master',
'/egf-master/fiscalperiods/_create',null,(select id from service where name='FiscalPeriod_Master'),1,'Create Fiscal Period',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='FiscalPeriod_Master'),'updateFiscalPeriod_Master',
'/egf-master/fiscalperiods/_update',null,(select id from service where name='FiscalPeriod_Master'),2,'Modify Fiscal Period',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='FiscalPeriod_Master'),'searchFiscalPeriod_Master',
'/egf-master/fiscalperiods/_search',null,(select id from service where name='FiscalPeriod_Master'),3,'Search Fiscal Period',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createFiscalPeriod_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateFiscalPeriod_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchFiscalPeriod_Master'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createFiscalPeriod_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateFiscalPeriod_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchFiscalPeriod_Master'),'default');

--accountdetailtypes
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'AccountDetailType_Master', 'AccountDetailType_Master', false, '/egf-master', (select id from service where name='Financials_Masters'), 'Account Detail Type', 19,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='AccountDetailType_Master'),'createAccountDetailType_Master',
'/egf-master/accountdetailtypes/_create',null,(select id from service where name='AccountDetailType_Master'),1,'Create Account Detail Type',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='AccountDetailType_Master'),'updateAccountDetailType_Master',
'/egf-master/accountdetailtypes/_update',null,(select id from service where name='AccountDetailType_Master'),2,'Modify Account Detail Type',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='AccountDetailType_Master'),'searchAccountDetailType_Master',
'/egf-master/accountdetailtypes/_search',null,(select id from service where name='AccountDetailType_Master'),3,'Search Account Detail Type',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createAccountDetailType_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateAccountDetailType_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchAccountDetailType_Master'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createAccountDetailType_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateAccountDetailType_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchAccountDetailType_Master'),'default');

--FinancialConfigurations
INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES
(nextval('seq_service'), 'FinancialConfiguration_Master', 'FinancialConfiguration_Master', false, '/egf-master', (select id from service where name='Financials_Masters'), 'Financial Configuration', 18,'default');

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='FinancialConfiguration_Master'),'createFinancialConfiguration_Master',
'/egf-master/financialconfigurations/_create',null,(select id from service where name='FinancialConfiguration_Master'),1,'Create Financial Configuration',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='FinancialConfiguration_Master'),'updateFinancialConfiguration_Master',
'/egf-master/financialconfigurations/_update',null,(select id from service where name='FinancialConfiguration_Master'),2,'Modify Financial Configuration',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='FinancialConfiguration_Master'),'searchFinancialConfiguration_Master',
'/egf-master/financialconfigurations/_search',null,(select id from service where name='FinancialConfiguration_Master'),3,'Search Financial Configuration',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'createFinancialConfiguration_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'updateFinancialConfiguration_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Financial Administrator', (select id from eg_action where name = 'searchFinancialConfiguration_Master'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'createFinancialConfiguration_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'updateFinancialConfiguration_Master'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'searchFinancialConfiguration_Master'),'default');


INSERT INTO EG_ROLEACTION (ROLECODE, ACTIONID, TENANTID) values ('EMPLOYEE ADMIN', (select id from eg_action where name = 'searchBank_Master'), 'default');
INSERT INTO EG_ROLEACTION (ROLECODE, ACTIONID, TENANTID) values ('EMPLOYEE ADMIN', (select id from eg_action where name = 'searchBankBranch_Master'), 'default');
INSERT INTO EG_ROLEACTION (ROLECODE, ACTIONID, TENANTID) values ('EMPLOYEE ADMIN', (select id from eg_action where name = 'searchFund_Master'), 'default');
INSERT INTO EG_ROLEACTION (ROLECODE, ACTIONID, TENANTID) values ('EMPLOYEE ADMIN', (select id from eg_action where name = 'searchFunction_Master'), 'default');
INSERT INTO EG_ROLEACTION (ROLECODE, ACTIONID, TENANTID) values ('EMPLOYEE ADMIN', (select id from eg_action where name = 'searchFunctionary_Master'), 'default');