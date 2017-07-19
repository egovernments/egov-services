insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
	lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'AssetConfigurationService','/asset-services/assetconfigurations/_search',
	'AssetConfigurationService',null,(select id from service where name = 'Asset Service' and tenantid = 'default'),1,
	'Asset Configuration Service',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='AssetConfigurationService'),
	'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('Asset Administrator',
	(select id from eg_action where name='AssetConfigurationService'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('AssetCreator',(select id from eg_action where name='AssetConfigurationService'),
	'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('AssetReportViewer',
	(select id from eg_action where name='AssetConfigurationService'),'default');

--rollback delete from eg_roleaction where roleCode in ('SUPERUSER','Asset Administrator',
--rollback 'AssetCreator','AssetReportViewer') and 
--rollback	actionid = (select id from eg_action where name='AssetConfigurationService');

--rollback delete from eg_action where name = 'AssetConfigurationService' and servicecode = 'AssetConfigurationService';
