insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='SearchEmployee'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'processsearch'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE', (select id from eg_action where name = 'processsearch'),'default');
