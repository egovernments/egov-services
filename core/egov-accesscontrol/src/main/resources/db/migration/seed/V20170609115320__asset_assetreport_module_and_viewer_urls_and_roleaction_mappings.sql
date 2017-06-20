--Asset Report Module
INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES (nextval('SEQ_SERVICE'),
	'AssetReports', 'Asset Reports', true, 'asset-web', 'Reports', 5,
	(select id from service where name='Asset Management' and tenantId='default'), 'default');

--Asset Register Report Sub Module
INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES (nextval('SEQ_SERVICE'),
	'AssetRegisterReport', 'Asset Register Report', true, 'asset-web', 'Asset Register Report', 1,
	(select id from service where name='Asset Reports' and tenantId='default'), 'default');

--Asset Register Report HTML Page
insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
	lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'AssetRegisterReportsSearchPage',
		'/asset-web/app/common/search-asset-register-report.html','AssetRegisterReportsSearchPage',
			null,(select id from service where name='Asset Register Report' and  tenantId='default'),1,'Asset Register Report',true,1,now(),1,now());

--Asset Register Report Service
insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
	lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'SearchAssetRegisterReports','/asset-services/assets/_search',
		'SearchAssetRegisterReports',null,(select id from service where name='Asset Register Report' and tenantId='default'),1,
			'Asset Register Report',false,1,now(),1,now());
	
-- Role Action Mapping of Asset Register Report with SUPERUSER
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', 
	(select id from eg_action where name = 'AssetRegisterReportsSearchPage'),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', 
	(select id from eg_action where name = 'SearchAssetRegisterReports'),'default');

--Role Action Mapping of Asset Register Report with Asset Report Viewer
INSERT INTO EG_ROLEACTION (ROLECODE,ACTIONID,TENANTID) values ('AssetReportViewer',
	(select id from eg_action where name = 'SearchAssetRegisterReports'),'default');
INSERT INTO EG_ROLEACTION (ROLECODE,ACTIONID,TENANTID) values ('AssetReportViewer',
	(select id from eg_action where name = 'AssetRegisterReportsSearchPage'),'default');
INSERT INTO EG_ROLEACTION (ROLECODE,ACTIONID,TENANTID) values ('AssetReportViewer',
	(select id from eg_action where name = 'AssetCategoryTypeService'),'default');
INSERT INTO EG_ROLEACTION (ROLECODE,ACTIONID,TENANTID) values ('AssetReportViewer',
	(select id from eg_action where name = 'ViewAssetCategoryService'),'default');
INSERT INTO EG_ROLEACTION (ROLECODE,ACTIONID,TENANTID) values ('AssetReportViewer',
	(select id from eg_action where name = 'BoundarySearch'),'default');
INSERT INTO EG_ROLEACTION (ROLECODE,ACTIONID,TENANTID) values ('AssetReportViewer',
	(select id from eg_action where name = 'Get Boundaries by boundarytype and hierarchy Type'),'default');
INSERT INTO EG_ROLEACTION (ROLECODE,ACTIONID,TENANTID) values ('AssetReportViewer',
	(select id from eg_action where name = 'CommonDepartmentsSearch'),'default');
INSERT INTO EG_ROLEACTION (ROLECODE,ACTIONID,TENANTID) values ('AssetReportViewer',
	(select id from eg_action where name = 'ModeOfAcquisitionService'),'default');
INSERT INTO EG_ROLEACTION (ROLECODE,ACTIONID,TENANTID) values ('AssetReportViewer',
	(select id from eg_action where name = 'AssetStatusService'),'default');
INSERT INTO EG_ROLEACTION (ROLECODE,ACTIONID,TENANTID) values ('AssetReportViewer',
	(select id from eg_action where name = 'ViewAssetService'),'default');
INSERT INTO EG_ROLEACTION (ROLECODE,ACTIONID,TENANTID) values ('AssetReportViewer',
	(select id from eg_action where name = 'CreateAsset'),'default');

--rollback delete from eg_roleaction where rolecode = 'AssetReportViewer' and actionid in (
--rollback (select id from eg_action where name = 'SearchAssetRegisterReports'),
--rollback (select id from eg_action where name = 'AssetRegisterReportsSearchPage'),
--rollback (select id from eg_action where name = 'AssetCategoryTypeService'),
--rollback (select id from eg_action where name = 'ViewAssetCategoryService'),
--rollback (select id from eg_action where name = 'BoundarySearch'),
--rollback (select id from eg_action where name = 'Get Boundaries by boundarytype and hierarchy Type'),
--rollback (select id from eg_action where name = 'CommonDepartmentsSearch'),
--rollback (select id from eg_action where name = 'ModeOfAcquisitionService'),
--rollback (select id from eg_action where name = 'AssetStatusService'),
--rollback (select id from eg_action where name = 'ViewAssetService'),
--rollback (select id from eg_action where name = 'CreateAsset')) and tenantid = 'default';
	
--rollback delete from eg_roleaction where rolecode = 'SUPERUSER' and actionid in (
--rollback (select id from eg_action where name = 'SearchAssetRegisterReports'),
--rollback (select id from eg_action where name = 'AssetRegisterReportsSearchPage')) and tenantid = 'default';

--rollback delete from eg_action where name in ('SearchAssetRegisterReports','AssetRegisterReportsSearchPage');
	
--rollback delete from service where name in ('Asset Register Report','Asset Reports');
