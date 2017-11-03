insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 

values(nextval('SEQ_EG_ACTION'),'ModifyAssetServiceMaha','/asset-services-maha/assets/_update','Asset Service',null,(select id from service where name='Asset Service' and tenantId='default')
,1,'Modify Asset Service Maha',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid) values ('SUPERUSER', (select id from eg_action where name = 'ModifyAssetServiceMaha' and 

url='/asset-services-maha/assets/_update'),'default');
