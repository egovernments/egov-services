delete from service where code = 'Asset Service' and name = 'Asset Service';

update service set ordernumber = 2 where code = 'AssetTransactions';
update service set ordernumber = 3 where code = 'AssetReports';

update eg_action set servicecode ='Asset' where name = 'CreateAsset';
update eg_action set servicecode ='Asset Category' where name ='CreateAssetCategory';
update eg_action set servicecode ='Asset Category' where name ='ViewAssetCategory';
update eg_action set url = '/asset-services'||url,servicecode ='Asset',displayname='Create Asset',enabled = true where 
name ='CreateAssetService';
update eg_action set url = '/asset-services'||url,servicecode ='Asset',displayname='View Asset',ordernumber = 2,enabled = true where 
name ='ViewAssetService';
update eg_action set url = '/asset-services/assets/_update',servicecode ='Asset',displayname='Modify Asset',ordernumber = 3,enabled = true where 
name ='ModifyAssetService';
update eg_action set url = '/asset-services'||url,servicecode ='Asset Category',displayname='Create Asset Category',enabled = true where 
name ='CreateAssetCategoryService'; 
update eg_action set url = '/asset-services'||url,servicecode ='Asset Category',displayname='View Asset Category',ordernumber = 2,enabled = true where 
name ='ViewAssetCategoryService'; 

insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
	lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'ModifyAssetCategoryService','/asset-services/assetCategories/_update','Asset Category',
		null,null,3,'Modify Asset Category',true,1,now(),1,now());
	
update eg_action set servicecode ='Asset Category',displayname='Modify Asset Category' where name ='ModifyAssetCategory'; 
update eg_action set url = '/asset-services'||url,servicecode ='Asset Masters' where name ='AssetCategoryTypeService'; 

insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
	lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'GetDepreciationMethodService','/asset-services/GET_DEPRECIATION_METHOD','Asset Masters',
		null,null,3,'Get Asset Depreciation Methods',false,1,now(),1,now());
		
insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
	lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'GetModeOfAcquisitionService','/asset-services/GET_MODE_OF_ACQUISITION','Asset Masters',
		null,null,3,'Get Asset Mode of Acquisition',false,1,now(),1,now());

update eg_action set servicecode ='Asset' where name ='ModifyAsset'; 
update eg_action set servicecode ='Asset' where name ='ViewAsset'; 
update eg_action set servicecode ='AssetReports' where name ='AssetRegisterReportsSearchPage'; 
update eg_action set servicecode ='AssetReports',enabled = true where name ='SearchAssetRegisterReports'; 
update eg_action set servicecode ='Asset Masters' where name ='AssetStatusService'; 
update eg_action set servicecode ='Asset' where name ='AssetCurrentValueSearchService'; 
update eg_action set servicecode ='AssetRevaluation',enabled = true where name ='AssetRevaluationViewService'; 
update eg_action set servicecode ='AssetRevaluation' where name ='AssetRevaluationSearchToCreate'; 
update eg_action set servicecode ='AssetSaleAndDisposal' where name ='AssetSaleAndDisposalView'; 
update eg_action set servicecode ='AssetSaleAndDisposal',enabled=true where name ='AssetSaleAndDisposalViewService'; 
update eg_action set servicecode ='AssetSaleAndDisposal' where name ='AssetSaleAndDisposalSearchToView'; 
update eg_action set servicecode ='AssetSaleAndDisposal' where name ='AssetSaleAndDisposalSearchToCreate'; 
update eg_action set servicecode ='AssetSaleAndDisposal',enabled=true where name ='AssetSaleAndDisposalCreateService'; 
update eg_action set servicecode ='AssetSaleAndDisposal',enabled=true where name ='AssetSaleAndDisposalSearchService'; 
update eg_action set servicecode ='AssetRevaluation' where name ='AssetRevaluationSearchToView'; 
update eg_action set servicecode ='AssetRevaluation',ordernumber=1,enabled=true where name ='AssetRevaluationCreate'; 
update eg_action set servicecode ='AssetRevaluation',enabled=true where name ='AssetRevaluationCreateService'; 
update eg_action set servicecode ='AssetRevaluation',enabled=true where name ='AssetRevaluationSearchService'; 
update eg_action set servicecode ='Asset Management' where name ='AssetConfigurationService'; 

delete from eg_action where name in('AssetRevaluationSearchService','AssetSaleAndDisposalSearchService');

insert into eg_roleaction(roleCode,actionid,tenantId) values('SUPERUSER',(select id from eg_action where name='GetDepreciationMethodService'),
'default');
insert into eg_roleaction(roleCode,actionid,tenantId) values('Asset Administrator',
	(select id from eg_action where name='GetDepreciationMethodService'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId) values('AssetCreator',
	(select id from eg_action where name='GetDepreciationMethodService'),'default');
	
insert into eg_roleaction(roleCode,actionid,tenantId) values('SUPERUSER',(select id from eg_action where name='GetModeOfAcquisitionService'),
'default');
insert into eg_roleaction(roleCode,actionid,tenantId) values('Asset Administrator',
	(select id from eg_action where name='GetModeOfAcquisitionService'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId) values('AssetCreator',
	(select id from eg_action where name='GetModeOfAcquisitionService'),'default');
	

insert into eg_roleaction(roleCode,actionid,tenantId) values('SUPERUSER',(select id from eg_action where name='ModifyAssetCategoryService'),
'default');
insert into eg_roleaction(roleCode,actionid,tenantId) values('Asset Administrator',
	(select id from eg_action where name='ModifyAssetCategoryService'),'default');