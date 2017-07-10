--insertion and updation for revaluation and disposal names in eg_action

INSERT INTO EG_ACTION (id , name ,url ,servicecode,queryparams,parentmodule ,ordernumber , displayname, 
enabled,createdby ,createddate ,lastmodifiedby ,lastmodifieddate) 
VALUES 
(NEXTVAL('SEQ_EG_ACTION'),'AssetSaleAndDisposalSearchToView','/asset-web/app/asset/search-asset-sale.html',
'AssetSaleAndDisposalSearchToView','type=view',(select id from service where name ='Asset Sale And Disposal'),
2,'View Asset Sale/Disposal HTML Page',true,1,now(),1,now());


update eg_action set name='AssetSaleAndDisposalSearchToCreate', 
	parentmodule =(select id from service where name ='Asset Sale And Disposal'),
	servicecode='AssetSaleAndDisposalSearchToCreate',url='/asset-web/app/asset/search-asset-sale.html'
where  name='AssetSaleAndDisposalCreate';

update eg_action set parentmodule =(select id from service where name ='Asset Sale And Disposal')
where name='AssetSaleAndDisposalCreateService';

INSERT INTO EG_ACTION (id , name ,url ,servicecode,queryparams,parentmodule ,ordernumber , displayname,
enabled,createdby ,createddate ,lastmodifiedby ,lastmodifieddate)
VALUES 
(NEXTVAL('SEQ_EG_ACTION'),'AssetSaleAndDisposalSearchService','/asset-services/assets/dispose/_search',
'AssetSaleAndDisposalSearchService',null,(select id from service where name ='Asset Sale And Disposal'),
2,'View Asset Sale/Disposal',false,1,now(),1,now());

update eg_action set name='AssetRevaluationSearchToCreate',
	parentmodule =(select id from service where name ='Asset Revaluation'), 
	servicecode='AssetRevaluationSearchToCreate' ,url='/asset-web/app/asset/search-asset-revaluation.html'
where  name='AssetRevaluationCreate';

update eg_action set name='AssetRevaluationSearchToView',
	parentmodule =(select id from service where name ='Asset Revaluation') ,
	queryparams='type=view', servicecode='AssetRevaluationSearchToView' 
where  name='AssetRevaluationView';

INSERT INTO EG_ACTION (id , name ,url ,servicecode,queryparams,parentmodule ,ordernumber , displayname, 
enabled,createdby ,createddate ,lastmodifiedby ,lastmodifieddate) 
VALUES 
(NEXTVAL('SEQ_EG_ACTION'),'AssetRevaluationCreate','/asset-web/app/asset/create-asset-revaluation.html',
'AssetRevaluationCreate',null,(select id from service where name ='Asset Revaluation'),
3,'Create Asset Revaluation',false,1,now(),1,now());

update eg_action set parentmodule=(select id from service where name ='Asset Revaluation') 
where name='AssetRevaluationCreateService';

INSERT INTO EG_ACTION (id , name ,url ,servicecode,queryparams,parentmodule ,ordernumber , displayname,
enabled,createdby ,createddate ,lastmodifiedby ,lastmodifieddate)
 VALUES 
(NEXTVAL('SEQ_EG_ACTION'),'AssetRevaluationSearchService','/asset-services/assets/revaluation/_search',
'AssetRevaluationSearchService',null,(select id from service where name ='Asset Revaluation'),
2,'View Asset Revaluation',false,1,now(),1,now());

INSERT INTO EG_ACTION (id , name ,url ,servicecode,queryparams,parentmodule ,ordernumber , displayname, 
enabled,createdby ,createddate ,lastmodifiedby ,lastmodifieddate) 
VALUES 
(NEXTVAL('SEQ_EG_ACTION'),'AssetSaleAndDisposalCreate','/asset-web/app/asset/create-asset-sale.html',
'AssetSaleAndDisposalCreate',null,(select id from service where name ='Asset Sale And Disposal'),
1,'Create Asset Sale/Disposal',false,1,now(),1,now());

--deletion of create and view from service for revaluation and disposal

  delete from service where name='CreateAssetRevaluation';
  delete from service where name='CreateAssetSaleAndDisposal';
  delete from service where name='ViewAssetSaleAndDisposal';
  delete from service where name='ViewAssetRevaluation';
