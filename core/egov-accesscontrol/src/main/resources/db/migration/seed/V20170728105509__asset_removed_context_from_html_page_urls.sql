update eg_action set url = replace(url,'/asset-web/','/') where name in('AssetSaleAndDisposalSearchToView','AssetRevaluationSearchToView',
'AssetRevaluationCreate','AssetRegisterReportsSearchPage','AssetRevaluationSearchToCreate','AssetSaleAndDisposalView',
'AssetSaleAndDisposalSearchToCreate');

--rollback update eg_action set url = replace(url,'/','/asset-web/') where name in('AssetSaleAndDisposalSearchToView','AssetRevaluationSearchToView',
--rollback 'AssetRevaluationCreate','AssetRegisterReportsSearchPage','AssetRevaluationSearchToCreate','AssetSaleAndDisposalView',
--rollback 'AssetSaleAndDisposalSearchToCreate')

