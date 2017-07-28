update eg_action set enabled = false where name in ('AssetRevaluationCreate','AssetSaleAndDisposalView');
update eg_action set displayname = 'View Asset Sale/Disposal' where name = 'AssetSaleAndDisposalSearchToView';

update eg_action set url = '/app/asset/search-asset-category.html',queryparams = 'type=update' where name = 'ModifyAssetCategory';
update eg_action set queryparams = 'type=view' where name = 'ViewAssetCategory';

--rollback update eg_action set url = '/app/asset/modify-asset-category.html',queryparams = null where name = 'ModifyAssetCategory'
--rollback update eg_action set queryparams = null where name = 'ViewAssetCategory'

--rollback update eg_action set enabled = true where name in ('AssetRevaluationCreate','AssetSaleAndDisposalView');
--rollback update eg_action set displayname = 'View Asset Sale/Disposal HTML Page' where name = 'AssetSaleAndDisposalSearchToView';