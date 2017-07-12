insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Create Draft'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Search Draft'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Update Draft'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Delete Draft'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Create Draft'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Search Draft'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Update Draft'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Delete Draft'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Create Draft'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Search Draft'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Update Draft'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Delete Draft'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Create Draft'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Search Draft'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Update Draft'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Delete Draft'),'default');
