insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Search User Details'),'default');

