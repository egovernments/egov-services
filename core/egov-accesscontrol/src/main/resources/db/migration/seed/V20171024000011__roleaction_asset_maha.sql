update eg_action set displayname='View Asset Service Maha' where name='ViewAssetServiceMaha' and displayname='View Asset Service';
update eg_action set displayname='Create Asset Service Maha' where name='CreateAssetServiceMaha' and displayname='Create Asset Service';


insert into eg_roleaction(roleCode, actionid, tenantId) 

values ('SUPERUSER', (select id from eg_action where name='ViewAssetServiceMaha' and url='/assets/_search' and displayname='View Asset Service Maha'), 'default');

insert into eg_roleaction(roleCode, actionid, tenantId) 

values ('SUPERUSER', (select id from eg_action where name='CreateAssetServiceMaha' and url='/assets/_create' and displayname='Create Asset Service Maha'), 'default');

