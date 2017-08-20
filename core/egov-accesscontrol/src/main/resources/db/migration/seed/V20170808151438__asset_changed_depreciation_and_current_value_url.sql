update eg_action set url ='/'||url where name = 'AssetDepreciationCreateService';

update eg_action set url='/asset-services/assets/currentvalue/_search' where name = 'AssetCurrentValueSearchService';