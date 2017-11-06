insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ModifyAssetMovable'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchAssetMovable'),'default');
