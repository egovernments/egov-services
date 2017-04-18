INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'Financials_MS', false, '/egf-masters', NULL, 'Financials', 20);
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'Financials_Masters_MS', false, '/egf-masters', (select id from eg_module where name='Financials_MS' and parentmodule is null), 'Masters', 20);
--Account Detail key
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'Account Detail key', false, '/egf-masters', (select id from eg_module where name='Masters'), 'Account Detail key', 1);

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'createAccountDetailKey',
'/accountdetailkeys/_create',null,(select id from eg_module where name='Account Detail key' and contextroot='/egf-masters'),1,'Create Account Detail Key',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'updateAccountDetailKey',
'/accountdetailkeys/{id}/_update',null,(select id from eg_module where name='Account Detail key' and contextroot='/egf-masters'),2,'Modify Account Detail Key',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'searchAccountDetailKey',
'/accountdetailkeys/_search',null,(select id from eg_module where name='Account Detail key' and contextroot='/egf-masters'),3,'Search Account Detail Key',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'createAccountDetailKey' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'updateAccountDetailKey' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'searchAccountDetailKey' and contextroot = '/egf-masters'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'createAccountDetailKey' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'updateAccountDetailKey' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'searchAccountDetailKey' and contextroot = '/egf-masters'));

--Bank
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'Bank_MS', false, '/egf-masters', (select id from eg_module where name='Masters'), 'Bank', 2);

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'createBank',
'/banks/_create',null,(select id from eg_module where name='Bank_MS' and contextroot='/egf-masters'),1,'Create Bank',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'updateBank',
'/banks/{id}/_update',null,(select id from eg_module where name='Bank_MS' and contextroot='/egf-masters'),3,'Modify Bank',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'searchBank',
'/banks/_search',null,(select id from eg_module where name='Bank_MS' and contextroot='/egf-masters'),3,'Search Bank',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'createBank' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'updateBank' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'searchBank' and contextroot = '/egf-masters'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'createBank' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'updateBank' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'searchBank' and contextroot = '/egf-masters'));

--Bankaccounts
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'Bankaccount', false, '/egf-masters', (select id from eg_module where name='Masters'), 'Bankaccount', 3);

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'createBankAccount',
'/bankaccounts/_create',null,(select id from eg_module where name='Bankaccount' and contextroot='/egf-masters'),1,'Create BankAccount',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'updateBankAccount',
'/bankaccounts/{id}/_update',null,(select id from eg_module where name='Bankaccount' and contextroot='/egf-masters'),2,'Modify BankAccount',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'searchBankAccount',
'/bankaccounts/_search',null,(select id from eg_module where name='Bankaccount' and contextroot='/egf-masters'),3,'Search BankAccount',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'createBankAccount' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'updateBankAccount' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'searchBankAccount' and contextroot = '/egf-masters'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'createBankAccount' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'updateBankAccount' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'searchBankAccount' and contextroot = '/egf-masters'));

--Account Code Purpose
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'AccountCodePurpose', false, '/egf-masters', (select id from eg_module where name='Masters'), 'Account Code Purpose', 4);

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'createAccountCodePurpose',
'/accountcodepurposes/_create',null,(select id from eg_module where name='AccountCodePurpose' and contextroot='/egf-masters'),1,'Create Account Code Purpose',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'updateAccountCodePurpose',
'/accountcodepurposes/{id}/_update',null,(select id from eg_module where name='AccountCodePurpose' and contextroot='/egf-masters'),2,'Modify Account Code Purpose',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'searchAccountCodePurpose',
'/accountcodepurposes/_search',null,(select id from eg_module where name='AccountCodePurpose' and contextroot='/egf-masters'),3,'Search Account Code Purpose',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'createAccountCodePurpose' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'updateAccountCodePurpose' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'searchAccountCodePurpose' and contextroot = '/egf-masters'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'createAccountCodePurpose' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'updateAccountCodePurpose' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'searchAccountCodePurpose' and contextroot = '/egf-masters'));


--Supplier
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'Supplier', false, '/egf-masters', (select id from eg_module where name='Masters'), 'Supplier', 5);

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'createSupplier',
'/suppliers/_create',null,(select id from eg_module where name='Supplier' and contextroot='/egf-masters'),1,'Create Supplier',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'updateSupplier',
'/suppliers/{id}/_update',null,(select id from eg_module where name='Supplier' and contextroot='/egf-masters'),2,'Modify Supplier',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'searchSupplier',
'/suppliers/_search',null,(select id from eg_module where name='Supplier' and contextroot='/egf-masters'),3,'Search Supplier',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'createSupplier' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'updateSupplier' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'searchSupplier' and contextroot = '/egf-masters'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'createSupplier' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'updateSupplier' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'searchSupplier' and contextroot = '/egf-masters'));

--Fund
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'Fund_MS', false, '/egf-masters', (select id from eg_module where name='Masters'), 'Fund', 6);

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'createFund',
'/funds/_create',null,(select id from eg_module where name='Fund_MS' and contextroot='/egf-masters'),1,'Create Fund',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'updateFund',
'/funds/{id}/_update',null,(select id from eg_module where name='Fund_MS' and contextroot='/egf-masters'),2,'Modify Fund',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'searchFund',
'/funds/_search',null,(select id from eg_module where name='Fund_MS' and contextroot='/egf-masters'),3,'Search Fund',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'createFund' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'updateFund' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'searchFund' and contextroot = '/egf-masters'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'createFund' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'updateFund' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'searchFund' and contextroot = '/egf-masters'));


--subschemes
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'SubScheme', false, '/egf-masters', (select id from eg_module where name='Masters'), 'SubScheme', 7);

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'createSubScheme_MS',
'/subschemes/_create',null,(select id from eg_module where name='SubScheme' and contextroot='/egf-masters'),1,'Create SubScheme',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'updateSubScheme_MS',
'/subschemes/{id}/_update',null,(select id from eg_module where name='SubScheme' and contextroot='/egf-masters'),2,'Modify SubScheme',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'searchSubScheme_MS',
'/subschemes/_search',null,(select id from eg_module where name='SubScheme' and contextroot='/egf-masters'),3,'Search SubScheme',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'createSubScheme_MS' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'updateSubScheme_MS' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'searchSubScheme_MS' and contextroot = '/egf-masters'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'createSubScheme_MS' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'updateSubScheme_MS' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'searchSubScheme_MS' and contextroot = '/egf-masters'));

--functions
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'Function_MS', false, '/egf-masters', (select id from eg_module where name='Masters'), 'Function', 8);
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'createFunction_MS',
'/functions/_create',null,(select id from eg_module where name='Function_MS' and contextroot='/egf-masters'),1,'Create Function',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'updateFunction_MS',
'/functions/{id}/_update',null,(select id from eg_module where name='Function_MS' and contextroot='/egf-masters'),2,'Modify Function',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'searchFunction_MS',
'/functions/_search',null,(select id from eg_module where name='Function_MS' and contextroot='/egf-masters'),3,'Search Function',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'createFunction_MS' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'updateFunction_MS' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'searchFunction_MS' and contextroot = '/egf-masters'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'createFunction_MS' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'updateFunction_MS' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'searchFunction_MS' and contextroot = '/egf-masters'));

--budgetgroups
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'BudgetGroup', false, '/egf-masters', (select id from eg_module where name='Masters'), 'Budget Group', 9);

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'createBudgetGroup_MS',
'/budgetgroups/_create',null,(select id from eg_module where name='BudgetGroup' and contextroot='/egf-masters'),1,'Create Budget Group',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'updateBudgetGroup_MS',
'/budgetgroups/{id}/_update',null,(select id from eg_module where name='BudgetGroup' and contextroot='/egf-masters'),2,'Modify Budget Group',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'searchBudgetGroup_MS',
'/budgetgroups/_search',null,(select id from eg_module where name='BudgetGroup' and contextroot='/egf-masters'),3,'Search Budget Group',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'createBudgetGroup_MS' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'updateBudgetGroup_MS' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'searchBudgetGroup_MS' and contextroot = '/egf-masters'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'createBudgetGroup_MS' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'updateBudgetGroup_MS' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'searchBudgetGroup_MS' and contextroot = '/egf-masters'));


--schemes
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'Scheme', false, '/egf-masters', (select id from eg_module where name='Masters'), 'Scheme', 10);

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'createScheme_MS',
'/schemes/_create',null,(select id from eg_module where name='Scheme' and contextroot='/egf-masters'),1,'Create Scheme',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'updateScheme_MS',
'/schemes/{id}/_update',null,(select id from eg_module where name='Scheme' and contextroot='/egf-masters'),2,'Modify Scheme',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'searchScheme_MS',
'/schemes/_search',null,(select id from eg_module where name='Scheme' and contextroot='/egf-masters'),3,'Search Scheme',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'createScheme_MS' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'updateScheme_MS' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'searchScheme_MS' and contextroot = '/egf-masters'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'createScheme_MS' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'updateScheme_MS' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'searchScheme_MS' and contextroot = '/egf-masters'));

--bank brances
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'BankBranch', false, '/egf-masters', (select id from eg_module where name='Masters'), 'Bank Branch', 11);

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'createBankBranch',
'/bankbranches/_create',null,(select id from eg_module where name='BankBranch' and contextroot='/egf-masters'),1,'Create BankBranch',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'updateBankBranch',
'/bankbranches/{id}/_update',null,(select id from eg_module where name='BankBranch' and contextroot='/egf-masters'),2,'Modify BankBranch',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'searchBankBranch',
'/bankbranches/_search',null,(select id from eg_module where name='BankBranch' and contextroot='/egf-masters'),3,'Search BankBranch',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'createBankBranch' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'updateBankBranch' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'searchBankBranch' and contextroot = '/egf-masters'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'createBankBranch' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'updateBankBranch' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'searchBankBranch' and contextroot = '/egf-masters'));

--fundsources
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'FundSource', false, '/egf-masters', (select id from eg_module where name='Masters'), 'Fund Source', 12);

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'createFundSource',
'/fundsources/_create',null,(select id from eg_module where name='FundSource' and contextroot='/egf-masters'),1,'Create Fund Source',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'updateFundSource',
'/fundsources/{id}/_update',null,(select id from eg_module where name='FundSource' and contextroot='/egf-masters'),2,'Modify Fund Source',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'searchFundSource',
'/fundsources/_search',null,(select id from eg_module where name='FundSource' and contextroot='/egf-masters'),3,'Search Fund Source',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'createFundSource' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'updateFundSource' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'searchFundSource' and contextroot = '/egf-masters'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'createFundSource' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'updateFundSource' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'searchFundSource' and contextroot = '/egf-masters'));

--functionaries
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'Functionary', false, '/egf-masters', (select id from eg_module where name='Masters'), 'Functionary', 13);

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'createFunctionary',
'/functionaries/_create',null,(select id from eg_module where name='Functionary' and contextroot='/egf-masters'),1,'Create Functionary',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'updateFunctionary',
'/functionaries/{id}/_update',null,(select id from eg_module where name='Functionary' and contextroot='/egf-masters'),2,'Modify Functionary',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'searchFunctionary',
'/functionaries/_search',null,(select id from eg_module where name='Functionary' and contextroot='/egf-masters'),3,'Search Functionary',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'createFunctionary' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'updateFunctionary' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'searchFunctionary' and contextroot = '/egf-masters'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'createFunctionary' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'updateFunctionary' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'searchFunctionary' and contextroot = '/egf-masters'));

--COA
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'ChartOfAccount', false, '/egf-masters', (select id from eg_module where name='Masters'), 'Chart Of Accounts', 14);

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'createChartOfAccount',
'/chartofaccounts/_create',null,(select id from eg_module where name='ChartOfAccount' and contextroot='/egf-masters'),1,'Create Chart Of Accounts',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'updateChartOfAccount',
'/chartofaccounts/{id}/_update',null,(select id from eg_module where name='ChartOfAccount' and contextroot='/egf-masters'),2,'Modify Chart Of Accounts',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'searchChartOfAccount',
'/chartofaccounts/_search',null,(select id from eg_module where name='ChartOfAccount' and contextroot='/egf-masters'),3,'Search Chart Of Accounts',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'createChartOfAccount' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'updateChartOfAccount' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'searchChartOfAccount' and contextroot = '/egf-masters'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'createChartOfAccount' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'updateChartOfAccount' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'searchChartOfAccount' and contextroot = '/egf-masters'));

--COADetails
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'ChartOfAccountDetail', false, '/egf-masters', (select id from eg_module where name='Masters'), 'Chart Of Accounts Detail', 15);

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'createChartOfAccountDetail',
'/chartofaccountdetails/_create',null,(select id from eg_module where name='ChartOfAccountDetail' and contextroot='/egf-masters'),1,'Create Chart Of Accounts Detail',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'updateChartOfAccountDetail',
'/chartofaccountdetails/{id}/_update',null,(select id from eg_module where name='ChartOfAccountDetail' and contextroot='/egf-masters'),2,'Modify Chart Of Accounts Detail',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'searchChartOfAccountDetail',
'/chartofaccountdetails/_search',null,(select id from eg_module where name='ChartOfAccountDetail' and contextroot='/egf-masters'),3,'Search Chart Of Accounts Detail',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'createChartOfAccountDetail' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'updateChartOfAccountDetail' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'searchChartOfAccountDetail' and contextroot = '/egf-masters'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'createChartOfAccountDetail' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'updateChartOfAccountDetail' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'searchChartOfAccountDetail' and contextroot = '/egf-masters'));

--accountentities
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'AccountEntiy', false, '/egf-masters', (select id from eg_module where name='Masters'), 'Account Entity', 16);

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'createAccountEntiy',
'/accountentities/_create',null,(select id from eg_module where name='AccountEntiy' and contextroot='/egf-masters'),1,'Create Account Entity',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'updateAccountEntiy',
'/accountentities/{id}/_update',null,(select id from eg_module where name='AccountEntiy' and contextroot='/egf-masters'),2,'Modify Account Entity',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'searchAccountEntiy',
'/accountentities/_search',null,(select id from eg_module where name='AccountEntiy' and contextroot='/egf-masters'),3,'Search Account Entity',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'createAccountEntiy' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'updateAccountEntiy' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'searchAccountEntiy' and contextroot = '/egf-masters'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'createAccountEntiy' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'updateAccountEntiy' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'searchAccountEntiy' and contextroot = '/egf-masters'));

--financialyears
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'FinancialYear', false, '/egf-masters', (select id from eg_module where name='Masters'), 'Financial Year', 17);

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'createFinancialYear',
'/financialyears/_create',null,(select id from eg_module where name='FinancialYear' and contextroot='/egf-masters'),1,'Create Financial Year',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'updateFinancialYear',
'/financialyears/{id}/_update',null,(select id from eg_module where name='FinancialYear' and contextroot='/egf-masters'),2,'Modify Financial Year',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'searchFinancialYear',
'/financialyears/_search',null,(select id from eg_module where name='FinancialYear' and contextroot='/egf-masters'),3,'Search Financial Year',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'createFinancialYear' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'updateFinancialYear' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'searchFinancialYear' and contextroot = '/egf-masters'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'createFinancialYear' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'updateFinancialYear' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'searchFinancialYear' and contextroot = '/egf-masters'));

--fiscalperiods
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'FiscalPeriod', false, '/egf-masters', (select id from eg_module where name='Masters'), 'Fiscal Period', 18);

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'createFiscalPeriod',
'/fiscalperiods/_create',null,(select id from eg_module where name='FiscalPeriod' and contextroot='/egf-masters'),1,'Create Fiscal Period',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'updateFiscalPeriod',
'/fiscalperiods/{id}/_update',null,(select id from eg_module where name='FiscalPeriod' and contextroot='/egf-masters'),2,'Modify Fiscal Period',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'searchFiscalPeriod',
'/fiscalperiods/_search',null,(select id from eg_module where name='FiscalPeriod' and contextroot='/egf-masters'),3,'Search Fiscal Period',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'createFiscalPeriod' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'updateFiscalPeriod' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'searchFiscalPeriod' and contextroot = '/egf-masters'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'createFiscalPeriod' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'updateFiscalPeriod' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'searchFiscalPeriod' and contextroot = '/egf-masters'));

--accountdetailtypes
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'AccountDetailType', false, '/egf-masters', (select id from eg_module where name='Masters'), 'Account Detail Type', 19);

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'createAccountDetailType',
'/accountdetailtypes/_create',null,(select id from eg_module where name='AccountDetailType' and contextroot='/egf-masters'),1,'Create Account Detail Type',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'updateAccountDetailType',
'/accountdetailtypes/{id}/_update',null,(select id from eg_module where name='AccountDetailType' and contextroot='/egf-masters'),2,'Modify Account Detail Type',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'searchAccountDetailType',
'/accountdetailtypes/_search',null,(select id from eg_module where name='AccountDetailType' and contextroot='/egf-masters'),3,'Search Account Detail Type',false,'/egf-masters',0,1,now(),1,now(),(select id from eg_module where name='Financials_MS' and parentmodule is null));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'createAccountDetailType' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'updateAccountDetailType' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Financial Administrator'), (select id from eg_action where name = 'searchAccountDetailType' and contextroot = '/egf-masters'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'createAccountDetailType' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'updateAccountDetailType' and contextroot = '/egf-masters'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'searchAccountDetailType' and contextroot = '/egf-masters'));
