insert into eg_action (id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values (nextval('SEQ_EG_ACTION'),'CreateAssetMovable','/app/asset/create-asset-movable.html','Asset Management',null,(select id from service where name='Asset' and  tenantId='default'),1,'Create Asset movable',true,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'CreateAssetMovable' and displayname='Create Asset movable' AND url='/app/asset/create-asset-movable.html'),'default');



update eg_action set name='CreateAssetImmovable',displayname='Create Asset Immovable' where name='CreateAsset' AND displayname='Create Asset' AND 
url='/app/asset/create-asset.html';

insert into eg_action (id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values (nextval('SEQ_EG_ACTION'),'AssetImmovableRegister','/report/asset/metadata/_get','AssetImmovableRegister',null,(select id from service where name='Asset Register Report' and  tenantId='default'),1,'Asset Immovable Register',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'AssetImmovableRegister' and displayname='Asset Immovable Register' AND url='/report/asset/metadata/_get'),'default');



