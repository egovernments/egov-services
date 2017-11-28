
delete from eg_roleaction where actionid in (select id from eg_action where name='CreateAssetDepreciationServiceMaha' );
delete from eg_roleaction where actionid in (select id from eg_action where name='ViewAssetDepreciationServiceMaha' );
delete from eg_roleaction where actionid in (select id from eg_action where name='Generate Depreciation' );

delete from eg_action where  name='CreateAssetDepreciationServiceMaha' ;
delete from eg_action where  name='ViewAssetDepreciationServiceMaha' ;
delete from eg_action where  name='Generate Depreciation' ;



insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'CreateAssetDepreciationServiceMaha','/asset-services-maha/assets/depreciations/_create','Asset Service',null,(select id from service where name='Asset Service Maha' and tenantId='default'),1,'Create AssetDepreciation Service Maha',false,1,now(),1,now());

insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'ViewAssetDepreciationServiceMaha','/asset-services-maha/assets/depreciations/_search','Asset Service',null,(select id from service where name='Asset Service Maha'and tenantId='default'),1,'View AssetDepreciation Service Maha',false,1,now(),1,now());




insert into eg_roleaction(roleCode, actionid, tenantId) values ('SUPERUSER', (select id from eg_action where name='ViewAssetDepreciationServiceMaha' and url='/asset-services-maha/assets/depreciations/_search' and displayname='View AssetDepreciation Service Maha'), 'default');

insert into eg_roleaction(roleCode, actionid, tenantId) values ('SUPERUSER', (select id from eg_action where name='CreateAssetDepreciationServiceMaha' and url='/asset-services-maha/assets/depreciations/_create' and displayname='Create AssetDepreciation Service Maha'), 'default');




--roleaction for menu tree


insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'Generate Depreciation','/app/asset/asset-depreciation.html','AssetTransactions',null,(select id from service where name='Asset Transactions' and tenantId='default'),3,'Generate Depreciation',false,1,now(),1,now());

insert into eg_roleaction(roleCode, actionid, tenantId) values ('SUPERUSER', (select id from eg_action where name='Generate Depreciation' and url='/app/asset/asset-depreciation.html' and displayname='Generate Depreciation'), 'default');


