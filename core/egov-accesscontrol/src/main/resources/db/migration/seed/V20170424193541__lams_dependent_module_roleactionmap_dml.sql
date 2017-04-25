insert into eg_roleaction(roleCode,actionid,tenantid)values('ULB Operator', (select id from eg_action where name = 'CommonDepartmentsSearch'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('Property Verifier', (select id from eg_action where name = 'CommonDepartmentsSearch'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('Property Approver', (select id from eg_action where name = 'CommonDepartmentsSearch'),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('ULB Operator', (select id from eg_action where name = 'loaddesignations'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('Property Verifier', (select id from eg_action where name = 'loaddesignations'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('Property Approver', (select id from eg_action where name = 'loaddesignations'),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('ULB Operator', (select id from eg_action where name = 'processsearch'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('Property Verifier', (select id from eg_action where name = 'processsearch'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('Property Approver', (select id from eg_action where name = 'processsearch'),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('ULB Operator', (select id from eg_action where name = 'tasksearch'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('Property Verifier', (select id from eg_action where name = 'tasksearch'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('Property Approver', (select id from eg_action where name = 'tasksearch'),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('ULB Operator', (select id from eg_action where name = 'searchhistory'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('Property Verifier', (select id from eg_action where name = 'searchhistory'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('Property Approver', (select id from eg_action where name = 'searchhistory'),'default');
