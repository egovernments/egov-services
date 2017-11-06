INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES (nextval('SEQ_SERVICE'),'Asset Immovable', 'Asset Immovable', true, 'asset-web', 'Asset Immovable', 2, (select id from service where name='Asset' and tenantId='default'), 'default');

INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES (nextval('SEQ_SERVICE'),'Asset Immovable', 'Asset movable', true, 'asset-web', 'Asset movable', 2, (select id from service where name='Asset' and tenantId='default'), 'default');

update eg_action set servicecode='Asset Immovable',parentmodule=(select id from service where name='Asset Immovable' AND tenantId='default') where name='CreateAssetImmovable' AND displayname='Create Asset Immovable';

update eg_action set name='ModifyAssetImmovable',servicecode='Asset Immovable',displayname='Modify Asset Immovable',parentmodule=(select id from service where name='Asset Immovable' AND tenantId='default') where name='ModifyAsset' AND displayname='Modify Asset';

update eg_action set name='ViewAssetImmovable',displayname='View Asset Immovable',servicecode='Asset Immovable',parentmodule=(select id from service where name='Asset Immovable' AND tenantId='default') where name='ViewAsset' AND displayname='View Asset';

update eg_action set servicecode='Asset Immovable',parentmodule=(select id from service where name='Asset Movable' AND tenantId='default') where name='CreateAssetMovable' AND displayname='Create Asset movable';

insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'ModifyAssetMovable','/app/asset/search-asset.html','Asset Movable',null,(select id from service where name='Asset Movable' and tenantId='default'),3,'Modify Asset Movable',true,1,now(),1,now());

insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'SearchAssetMovable','/app/asset/search-asset.html','Asset Movable',null,(select id from service where name='Asset Movable' and tenantId='default'),3,'Search Asset Movable',true,1,now(),1,now());
