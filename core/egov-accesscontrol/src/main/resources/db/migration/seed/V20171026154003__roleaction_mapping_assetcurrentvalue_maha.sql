insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'CreateAssetCurrentValueServiceMaha','asset-services-maha/assets/currentvalues/_create','Asset Service',null,(select id from service where name='Asset Service Maha' and tenantId='default'),1,'Create AssetCurrentValue Service Maha',false,1,now(),1,now());


insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'ViewAsseturrentValueServiceMaha','asset-services-maha/assets/currentvalues/_search','Asset Service',null,(select id from service where name='Asset Service Maha'and tenantId='default'),1,'View AssetCurrentValue Service Maha',false,1,now(),1,now());


insert into eg_roleaction(roleCode, actionid, tenantId) values ('SUPERUSER', (select id from eg_action where name='ViewAsseturrentValueServiceMaha' and url='asset-services-maha/assets/currentvalues/_search' and displayname='View AssetCurrentValue Service Maha'), 'default');

insert into eg_roleaction(roleCode, actionid, tenantId) values ('SUPERUSER', (select id from eg_action where name='CreateAssetCurrentValueServiceMaha' and url='asset-services-maha/assets/currentvalues/_create' and displayname='Create AssetCurrentValue Service Maha'), 'default');


