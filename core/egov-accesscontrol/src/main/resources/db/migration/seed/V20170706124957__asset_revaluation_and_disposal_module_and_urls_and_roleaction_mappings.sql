--Asset Transactions Module
INSERT INTO service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantid) 
VALUES (NEXTVAL('SEQ_SERVICE'),'AssetTransactions' ,'Asset Transactions', true, 'asset-web',
	'Transactions', 1,(select id from service where name='Asset Management' and tenantid = 'default'), 'default');

--Asset Revaluation Module
INSERT INTO service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantid) 
VALUES (NEXTVAL('SEQ_SERVICE'),'AssetRevaluation' ,'Asset Revaluation', true, 'asset-web',
	'Asset Revaluation', 1,(select id from service where name='Asset Transactions' and tenantid = 'default'), 'default');

--Create Asset Revaluation Sub Module
INSERT INTO service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantid) 
VALUES (NEXTVAL('SEQ_SERVICE'), 'CreateAssetRevaluation','CreateAssetRevaluation', true, 'asset-web', 
	'Create Asset Revaluation', 1,(select id from service where name='Asset Revaluation' and tenantid = 'default'),'default');

--View Asset Revaluation Sub Module
INSERT INTO service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantid) 
VALUES (NEXTVAL('SEQ_SERVICE'), 'ViewAssetRevaluation','ViewAssetRevaluation' ,true, 'asset-web', 
	'View Asset Revaluation', 2,(select id from service where name='Asset Revaluation' and tenantid = 'default'), 'default');

--Asset Sale/Disposal Module
INSERT INTO service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantid) 
VALUES (NEXTVAL('SEQ_SERVICE'), 'AssetSaleAndDisposal','Asset Sale And Disposal', true, 'asset-web', 
	'Asset Sale/Disposal', 2,(select id from service where name='Asset Transactions' and tenantid = 'default'),'default');

--Create Asset Sale/Disposal Sub Module
INSERT INTO service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantid) 
VALUES (NEXTVAL('SEQ_SERVICE'), 'CreateAssetSaleAndDisposal','CreateAssetSaleAndDisposal', true, 'asset-web', 
	'Create Asset Sale/Disposal', 1,(select id from service where name='Asset Sale And Disposal' and tenantid = 'default'), 'default');

--View Asset Sale/Disposal Sub Module
INSERT INTO service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantid) 
VALUES (NEXTVAL('SEQ_SERVICE'), 'ViewAssetSaleAndDisposal','ViewAssetSaleAndDisposal', true, 'asset-web', 
	'View Asset Sale/Disposal', 2,(select id from service where name='Asset Sale And Disposal' and tenantid = 'default'), 'default');

--eg_action for asset Revaluation create GET
insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,
	enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) values (nextval('SEQ_EG_ACTION'),
	'AssetRevaluationCreate','/asset-web/app/asset/create-asset-revaluation.html',
	'AssetRevaluationCreate',null,(select id from service where name = 'CreateAssetRevaluation' and tenantid = 'default'),
	1,'Create Asset Revaluation',true,1,now(),1,now());

--eg_action for asset Revaluation create POST
insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,
	enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) values (nextval('SEQ_EG_ACTION'),
	'AssetRevaluationCreateService','/asset-services/assets/revaluation/_create',
	'AssetRevaluationCreateService',null,(select id from service where name = 'CreateAssetRevaluation' and tenantid = 'default'),
	1,'Create Asset Revaluation',false,1,now(),1,now());

--eg_action for asset Revaluation view GET
insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,
	enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) values (nextval('SEQ_EG_ACTION'),
	'AssetRevaluationView','/asset-web/app/asset/search-asset-revaluation.html',
	'AssetRevaluationView',null,(select id from service where name = 'ViewAssetRevaluation' and tenantid = 'default'),
	2,'View Asset Revaluation',true,1,now(),1,now());

--eg_action for asset reevaluate view POST
insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,
	enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) values (nextval('SEQ_EG_ACTION'),
	'AssetRevaluationViewService','/asset-services/assets/revaluation/_search',
	'AssetRevaluationViewService',null,(select id from service where name = 'ViewAssetRevaluation' and tenantid = 'default'),
	2,'View Asset Revaluation',false,1,now(),1,now());

--eg_action for asset Sale/Disposal create GET
insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,
	enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) values (nextval('SEQ_EG_ACTION'),
	'AssetSaleAndDisposalCreate','/asset-web/app/asset/create-asset-sale.html',
	'AssetSaleAndDisposalCreate',null,(select id from service where name = 'CreateAssetSaleAndDisposal' and tenantid = 'default'),
	1,'Create Asset Sale/Disposal',true,1,now(),1,now());

--eg_action for asset Sale/Disposal create POST
insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,
	enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) values (nextval('SEQ_EG_ACTION'),
	'AssetSaleAndDisposalCreateService','/asset-services/assets/dispose/_create',
	'AssetSaleAndDisposalCreateService',null,(select id from service where name = 'CreateAssetSaleAndDisposal' and tenantid = 'default'),
	1,'Create Asset Sale/Disposal',false,1,now(),1,now());

--eg_action for asset Sale/Disposal view GET
insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,
	enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) values (nextval('SEQ_EG_ACTION'),
	'AssetSaleAndDisposalView','/asset-web/app/asset/search-asset-sale.html',
	'AssetSaleAndDisposalView',null,(select id from service where name = 'ViewAssetSaleAndDisposal' and tenantid = 'default'),
	2,'View Asset Sale/Disposal',true,1,now(),1,now());

--eg_action for asset Sale/Disposal view POST
insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,
	enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) values (nextval('SEQ_EG_ACTION'),
	'AssetSaleAndDisposalViewService','/asset-services/assets/dispose/_search',
	'AssetSaleAndDisposalViewService',null,(select id from service where name = 'ViewAssetSaleAndDisposal' and tenantid = 'default'),
	2,'View Asset Sale/Disposal',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid) values ('SUPERUSER',
	(select id from eg_action where name = 'AssetRevaluationCreateService'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid) values ('SUPERUSER',
	(select id from eg_action where name = 'AssetRevaluationCreate'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid) values ('SUPERUSER',
	(select id from eg_action where name = 'AssetRevaluationViewService'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid) values ('SUPERUSER',
	(select id from eg_action where name = 'AssetRevaluationView'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid) values ('SUPERUSER',
	(select id from eg_action where name = 'AssetSaleAndDisposalCreateService'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid) values ('SUPERUSER',
	(select id from eg_action where name = 'AssetSaleAndDisposalCreate'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid) values ('SUPERUSER',
	(select id from eg_action where name = 'AssetSaleAndDisposalViewService'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid) values ('SUPERUSER',
	(select id from eg_action where name = 'AssetSaleAndDisposalView'),'default');

insert into eg_roleaction(roleCode,actionid,tenantid) values ('Asset Administrator',
	(select id from eg_action where name = 'AssetRevaluationCreateService'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid) values ('Asset Administrator',
	(select id from eg_action where name = 'AssetRevaluationCreate'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid) values ('Asset Administrator',
	(select id from eg_action where name = 'AssetRevaluationViewService'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid) values ('Asset Administrator',
	(select id from eg_action where name = 'AssetRevaluationView'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid) values ('Asset Administrator',
	(select id from eg_action where name = 'AssetSaleAndDisposalCreateService'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid) values ('Asset Administrator',
	(select id from eg_action where name = 'AssetSaleAndDisposalCreate'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid) values ('Asset Administrator',
	(select id from eg_action where name = 'AssetSaleAndDisposalViewService'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid) values ('Asset Administrator',
	(select id from eg_action where name = 'AssetSaleAndDisposalView'),'default');

--rollback delete from EG_ROLEACTION where ACTIONID in (
--rollback  (select id from eg_action where name ='AssetRevaluationCreateService'),
--rollback	(select id from eg_action where name ='AssetRevaluationCreate'),
--rollback  (select id from eg_action where name ='AssetRevaluationViewService'),
--rollback  (select id from eg_action where name ='AssetRevaluationView'),
--rollback 	(select id from eg_action where name ='AssetSaleAndDisposalCreateService'),
--rollback  (select id from eg_action where name ='AssetSaleAndDisposalCreate'),
--rollback  (select id from eg_action where name ='AssetSaleAndDisposalViewService'),
--rollback 	(select id from eg_action where name ='AssetSaleAndDisposalView'))
--rollback 	and rolecode in ('SUPERUSER','Asset Administrator');

--rollback delete from eg_action where name in ('AssetSaleAndDisposalViewService','AssetSaleAndDisposalView') and
--rollback 	parentmodule = (select id from service where name = 'ViewAssetSaleAndDisposal');
--rollback delete from eg_action where name in ('AssetSaleAndDisposalCreateService','AssetSaleAndDisposalCreate') and
--rollback 	parentmodule = (select id from service where name = 'CreateAssetSaleAndDisposal');
--rollback delete from eg_action where name in ('AssetRevaluationViewService','AssetRevaluationView') and
--rollback 	parentmodule = (select id from service where name = 'ViewAssetRevaluation');
--rollback delete from eg_action where name in ('AssetRevaluationCreateService','AssetRevaluationCreate') and
--rollback 	parentmodule = (select id from service where name = 'CreateAssetRevaluation');

--rollback delete from service where name in ('CreateAssetSaleAndDisposal','ViewAssetSaleAndDisposal') 
--rollback 	and parentmodule = (select id from service where name = 'Asset Sale And Disposal');
--rollback delete from service where name in ('CreateAssetRevaluation','ViewAssetRevaluation') 
--rollback  and parentmodule = (select id from service where name = 'Asset Revaluation');

--rollback delete from service where name in ('Asset Revaluation','Asset Sale And Disposal') 
--rollback  and parentmodule = (select id from service where name='Asset Transactions');

--rollback delete from service where name = 'Asset Transactions' and 
--rollback	parentmodule = (select id from service where name='Asset Management');