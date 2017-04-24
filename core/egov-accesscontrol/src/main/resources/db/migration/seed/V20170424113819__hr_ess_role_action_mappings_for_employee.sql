insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE', (select id from eg_action where name = 'View ESS Employee'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE', (select id from eg_action where name = 'ESS Leave Application'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE', (select id from eg_action where name = 'Create Leave Applications'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE', (select id from eg_action where name = 'Leave Application Update'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE', (select id from eg_action where name = 'Search Leave Application'),'default');

--rollback scripts
--rollback delete from eg_roleaction where rolecode in ('EMPLOYEE') and actionid in (select id from eg_action where name in ('ESS Leave Application','View ESS Employee','Create Leave Applications','Leave Application Update','Search Leave Application'));