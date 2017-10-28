insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values (nextval('SEQ_EG_ACTION'),'ImmovableAssetRegister','/report/asset/_get','Asset Service',null,(select id from service where name='Asset Service Maha'and tenantId='default'),1,'Immovable Asset Register',false,1,now(),1,now());


insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values (nextval('SEQ_EG_ACTION'),'ImmovableRegisterMetadata','/report/asset/metadata/_get','Asset Service',null,(select id from service where name='Asset Service Maha'and tenantId='default'),1,'Immovable Asset Register Metadata',false,1,now(),1,now());


insert into eg_roleaction(roleCode, actionid, tenantId) 

values ('SUPERUSER', (select id from eg_action where name='ImmovableAssetRegister' and url='/report/asset/_get' and displayname='Immovable Asset Register'), 'default');

insert into eg_roleaction(roleCode, actionid, tenantId) 

values ('SUPERUSER', (select id from eg_action where name='ImmovableRegisterMetadata' and url='/report/asset/metadata/_get' and displayname='Immovable Asset Register Metadata'), 'default');


