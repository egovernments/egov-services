INSERT INTO eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname, enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
VALUES (NEXTVAL('SEQ_EG_ACTION'),'AssetDepreciationReportService','/asset-services/assets/depreciations/_search','AssetReports',null,
(select id from service where name ='Asset Depreciation' and tenantid = 'default'),1,'Asset Depreciation Report',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',
	(select id from eg_action where name='AssetDepreciationReportService'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('Asset Administrator',
	(select id from eg_action where name='AssetDepreciationReportService'),'default');
