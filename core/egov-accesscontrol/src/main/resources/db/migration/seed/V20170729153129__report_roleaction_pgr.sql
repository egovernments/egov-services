insert into eg_roleaction(roleCode,actionid,tenantid)values('RO', (select id from eg_action where name = 'Get Report Data'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('RO', (select id from eg_action where name = 'Get Report MetaData'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('RO', (select id from eg_action where name = 'Get Report Reload'),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('GRO', (select id from eg_action where name = 'Get Report Data'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('GRO', (select id from eg_action where name = 'Get Report MetaData'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('GRO', (select id from eg_action where name = 'Get Report Reload'),'default');


insert into eg_roleaction(roleCode,actionid,tenantid)values('GA', (select id from eg_action where name = 'Get Report Data'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('GA', (select id from eg_action where name = 'Get Report MetaData'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('GA', (select id from eg_action where name = 'Get Report Reload'),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('GO', (select id from eg_action where name = 'Get Report Data'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('GO', (select id from eg_action where name = 'Get Report MetaData'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('GO', (select id from eg_action where name = 'Get Report Reload'),'default');

