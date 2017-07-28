update eg_action set enabled = false where name in ('AssetRevaluationViewService','AssetSaleAndDisposalViewService',
'AssetRevaluationCreateService','ModifyAssetCategoryService','AssetSaleAndDisposalCreateService','CreateAssetService',
'ViewAssetService','ModifyAssetService','CreateAssetCategoryService','ViewAssetCategoryService','SearchAssetRegisterReports');

delete from eg_action where name in('ModeOfAcquisitionService','DepreciationMethodService');

insert into eg_roleaction(roleCode,actionid,tenantId) values('AssetCreator',
	(select id from eg_action where name='ViewAssetCategoryService'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId) values('AssetCreator',
	(select id from eg_action where name='AssetCategoryTypeService'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId) values('Asset Administrator',
	(select id from eg_action where name='searchFinancialYear'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId) values('AssetCreator',
	(select id from eg_action where name='searchFinancialYear'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId) values('Asset Administrator',
	(select id from eg_action where name='searchAccountCodePurpose'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId) values('Asset Administrator',
	(select id from eg_action where name='AssetSaleAndDisposalCreate'),'default');
