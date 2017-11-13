insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'CreateAssetRevaluationServiceMaha','/asset-services-maha/assets/revaluation/_create','Asset Service',null,(select id from service where name='Asset Service Maha' and tenantId='default'),1,'Create AssetRevaluation Service Maha',false,1,now(),1,now());

insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'ViewAssetRevaluationServiceMaha','/asset-services-maha/assets/revaluation/_search','Asset Service',null,(select id from service where name='Asset Service Maha'and tenantId='default'),1,'View AssetRevaluation Service Maha',false,1,now(),1,now());

insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'ModifyAssetRevaluationServiceMaha','/asset-services-maha/assets/revaluation/_update','Asset Service',null,(select id from service where name='Asset Service Maha' and tenantId='default'),1,'Modify AssetRevaluation Service Maha',false,1,now(),1,now());


insert into eg_roleaction(roleCode, actionid, tenantId) values ('SUPERUSER', (select id from eg_action where name='ViewAssetRevaluationServiceMaha' and url='/asset-services-maha/assets/revaluation/_search' and displayname='View AssetRevaluation Service Maha'), 'default');

insert into eg_roleaction(roleCode, actionid, tenantId) values ('SUPERUSER', (select id from eg_action where name='CreateAssetRevaluationServiceMaha' and url='/asset-services-maha/assets/revaluation/_create' and displayname='Create AssetRevaluation Service Maha'), 'default');

insert into eg_roleaction(roleCode, actionid, tenantId) values ('SUPERUSER', (select id from eg_action where name='ModifyAssetRevaluationServiceMaha' and url='/asset-services-maha/assets/revaluation/_update' and displayname='Modify AssetRevaluation Service Maha'), 'default');




insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'CreateAssetDisposalServiceMaha','/asset-services-maha/assets/dispose/_create','Asset Service',null,(select id from service where name='Asset Service Maha' and tenantId='default'),1,'Create AssetDisposal Service Maha',false,1,now(),1,now());

insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'ViewAssetDisposalServiceMaha','/asset-services-maha/assets/dispose/_search','Asset Service',null,(select id from service where name='Asset Service Maha'and tenantId='default'),1,'View AssetDisposal Service Maha',false,1,now(),1,now());

insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'ModifyAssetDisposalServiceMaha','/asset-services-maha/assets/dispose/_update','Asset Service',null,(select id from service where name='Asset Service Maha' and tenantId='default'),1,'Modify AssetDisposal Service Maha',false,1,now(),1,now());




insert into eg_roleaction(roleCode, actionid, tenantId) values ('SUPERUSER', (select id from eg_action where name='ViewAssetDisposalServiceMaha' and url='/asset-services-maha/assets/dispose/_search' and displayname='View AssetDisposal Service Maha'), 'default');


insert into eg_roleaction(roleCode, actionid, tenantId) values ('SUPERUSER', (select id from eg_action where name='CreateAssetDisposalServiceMaha' and url='/asset-services-maha/assets/dispose/_create' and displayname='Create AssetDisposal Service Maha'), 'default');


insert into eg_roleaction(roleCode, actionid, tenantId) values ('SUPERUSER', (select id from eg_action where name='ModifyAssetDisposalServiceMaha' and url='/asset-services-maha/assets/dispose/_update' and displayname='Modify AssetDisposal Service Maha'), 'default');


