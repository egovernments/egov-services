insert into eg_roleaction(roleCode,actionid,tenantid)values('GA',(select id from eg_action where name = 'Create Service Group' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('GA',(select id from eg_action where name = 'Get Boundary by Boundary Type' ),'default');
