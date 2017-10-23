update eg_action set displayname='View Asset Service Maha' where name='ViewAssetServiceMaha' and displayname='View Asset Service';
update eg_action set displayname='Create Asset Service Maha' where name='CreateAssetServiceMaha' and displayname='Create Asset Service';

update eg_action set url='/asset-services-maha/assets/_create' where name='CreateAssetServiceMaha' and displayname='Create Asset Service Maha';
update eg_action set url='/asset-services-maha/assets/_search' where name='ViewAssetServiceMaha' and displayname='View Asset Service Maha';
insert into eg_roleaction(roleCode, actionid, tenantId) 

values ('SUPERUSER', (select id from eg_action where name='ViewAssetServiceMaha' and url='/asset-services-maha/assets/_search' and displayname='View Asset Service Maha'), 'default');

insert into eg_roleaction(roleCode, actionid, tenantId) 

values ('SUPERUSER', (select id from eg_action where name='CreateAssetServiceMaha' and url='/asset-services-maha/assets/_create' and displayname='Create Asset Service Maha'), 'default');

