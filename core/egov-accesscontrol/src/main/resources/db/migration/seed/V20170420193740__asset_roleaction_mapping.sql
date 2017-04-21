delete from eg_roleaction where actionid in (Select id from eg_action where servicecode in ('Asset Management','Asset Service'));
delete from eg_action where servicecode in ('Asset Management','Asset Service');
delete from service where contextroot in ('asset-web','asset-services');





INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES (nextval('SEQ_SERVICE'),'Asset Management', 'Asset Management', true, 'asset-web', 'Asset Management', 1, null, 'default');

INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES (nextval('SEQ_SERVICE'),'Asset Masters', 'Asset Masters', true, 'asset-web', 'Masters', 1, (select id from service where name='Asset Management' and tenantId='default'),'default');

INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES (nextval('SEQ_SERVICE'),'Asset Category', 'Asset Category', true, 'asset-web', 'Asset Category', 1, (select id from service where name='Asset Masters' and tenantId='default'), 'default');

INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES (nextval('SEQ_SERVICE'),'Asset', 'Asset', true, 'asset-web', 'Asset', 2, (select id from service where name='Asset Masters' and tenantId='default'), 'default');

INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES (nextval('SEQ_SERVICE'),'Asset Service', 'Asset Service', true, 'asset-services', 'Asset Service', 1, null, 'default');



-- Action mapping for asset 


--html page url
insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'CreateAsset','/app/asset/create-asset.html','Asset Management',null,(select id from service where name='Asset' and  tenantId='default'),1,'Create Asset',true,1,now(),1,now(),'default');

insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'ViewAsset','/app/asset/search-asset.html','Asset Management',null,(select id from service where name='Asset' and tenantId='default'),2,'View Asset',true,1,now(),1,now(),'default');


insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'ModifyAsset','/app/asset/search-asset.html','Asset Management',null,(select id from service where name='Asset' and tenantId='default'),3,'Modify Asset',true,1,now(),1,now(),'default');

insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'CreateAssetCategory','/app/asset/create-asset-category.html','Asset Management',null,(select id from service where name='Asset Category' and tenantId='default'),1,'Create Asset Category',true,1,now(),1,now(),'default');

insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'ViewAssetCategory','/app/asset/search-asset-category.html','Asset Management',null,(select id from service where name='Asset Category' and tenantId='default'),2,'View Asset Category',true,1,now(),1,now(),'default');


insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'ModifyAssetCategory','/app/asset/create-asset.html','Asset Management',null,(select id from service where name='Asset Category' and tenantId='default'),3,'Modify Asset Category',true,1,now(),1,now(),'default');

--service url


insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'CreateAssetService','/assets/_create','Asset Service',null,(select id from service where name='Asset Service' and tenantId='default'),1,'Create Asset Service',false,1,now(),1,now(),'default');

insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'ViewAssetService','/assets/_search','Asset Service',null,(select id from service where name='Asset Service'and tenantId='default'),1,'View Asset Service',false,1,now(),1,now(),'default');

insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'ModifyAssetService','/assets/_update/{code}','Asset Service',null,(select id from service where name='Asset Service' and tenantId='default'),1,'Modify Asset Service',false,1,now(),1,now(),'default');


insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'CreateAssetCategoryService','/assetCategories/_create','Asset Service',null,(select id from service where name='Asset Service' and tenantId='default'),1,'Create Asset Category Service',false,1,now(),1,now(),'default');

insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'ViewAssetCategoryService','/assetCategories/_search','Asset Service',null,(select id from service where name='Asset Service' and tenantId='default'),1,'View Asset Category Service',false,1,now(),1,now(),'default');


insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'ModeOfAcquisitionService','/GET_MODE_OF_ACQUISITION','Asset Service',null,(select id from service where name='Asset Service' and contextroot='asset' and tenantId='default'),1,'Get Mode Of Acquisition',false,1,now(),1,now(),'default');


insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'DepreciationMethodService','/asset-services/GET_DEPRECIATION_METHOD','Asset Service',null,(select id from service where name='Asset Service' and tenantId='default'),1,'Get Depreciation Method Service',false,1,now(),1,now(),'default');

insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'Status','/GET_STATUS','Asset Management',null,(select id from service where name='Asset Management' and tenantId='default'),1,'Get Status',false,1,now(),1,now(),'default');

insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'Asset Category Type','/GET_ASSET_CATEGORY_TYPE','Asset Service',null,(select id from service where name='Asset Service' and tenantId='default'),1,'Get Asset Category Type',false,1,now(),1,now(),'default');


-------Roleaction mappings

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'CreateAsset' and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ViewAsset' and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ModifyAsset' and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'CreateAssetService' and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ViewAssetService' and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ModifyAssetService' and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'CreateAssetCategoryService' and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ViewAssetCategoryService' and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ModifyAssetCategory' and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('Asset Administrator', (select id from eg_action where name = 'CreateAsset' and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('Asset Administrator', (select id from eg_action where name = 'ViewAsset' and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('Asset Administrator', (select id from eg_action where name = 'ModifyAsset' and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('Asset Administrator', (select id from eg_action where name = 'CreateAssetService' and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('Asset Administrator', (select id from eg_action where name = 'ViewAssetService' and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('Asset Administrator', (select id from eg_action where name = 'ModifyAssetService' and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('Asset Administrator', (select id from eg_action where name = 'CreateAssetCategoryService' and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('Asset Administrator', (select id from eg_action where name = 'ViewAssetCategoryService' and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('Asset Administrator', (select id from eg_action where name = 'ModifyAssetCategory' and tenantId='default' ),'default');






