update service set enabled=false where name='Asset Service' and code='Asset Service' and tenantId='default';

update eg_action set url='/app/asset/modify-asset.html' where name='ModifyAsset' and tenantId='default';

update eg_action set url='/app/asset/modify-asset-category.html' where name='ModifyAssetCategory' and tenantId='default';

update eg_action set name='AssetCategoryTypeService' where name='Asset Category Type' and tenantId='default' and url='/GET_ASSET_CATEGORY_TYPE';

update eg_action set name='AssetStatusService',servicecode='Asset Service',parentmodule=(select id from service where name='Asset Service' and tenantId='default')  where name='Status' and tenantId='default'and url='/GET_STATUS';

update eg_action set url='/GET_DEPRECIATION_METHOD' where name='DepreciationMethodService' and tenantId='default';

update eg_action set parentmodule=(select id from service where name='Asset Service' and tenantId='default') where name='ModeOfAcquisitionService' and tenantId='default';


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'CreateAssetCategory' and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ViewAssetCategory' and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ModeOfAcquisitionService'and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'DepreciationMethodService'and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'AssetStatusService'and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'AssetCategoryTypeService'and tenantId='default' ),'default');


insert into eg_roleaction(roleCode,actionid,tenantid)values('Asset Administrator', (select id from eg_action where name = 'CreateAssetCategory' 
and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('Asset Administrator', (select id from eg_action where name = 'ViewAssetCategory' and tenantId='default' ),'default');


insert into eg_roleaction(roleCode,actionid,tenantid)values('Asset Administrator', (select id from eg_action where name = 'ModeOfAcquisitionService' and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('Asset Administrator', (select id from eg_action where name = 'DepreciationMethodService' and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('Asset Administrator', (select id from eg_action where name = 'AssetStatusService'and tenantId='default' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('Asset Administrator', (select id from eg_action where name = 'AssetCategoryTypeService' and tenantId='default' ),'default');

