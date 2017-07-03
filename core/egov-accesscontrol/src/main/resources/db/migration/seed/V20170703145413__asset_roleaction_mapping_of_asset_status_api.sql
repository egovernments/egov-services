delete from eg_roleaction where actionid = (select id from eg_action where name = 'AssetStatusService');
delete from eg_action where name = 'AssetStatusService';

insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
	lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'AssetStatusService',
		'/asset-services/assetstatuses/_search','AssetStatusService',null,
			(select id from service where name='Asset Service' and tenantId='default'),1,
				'Search Asset Status',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', 
	(select id from eg_action where name = 'AssetStatusService'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('Asset Administrator', 
	(select id from eg_action where name = 'AssetStatusService'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('AssetCreator', 
	(select id from eg_action where name = 'AssetStatusService'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('AssetReportViewer', 
	(select id from eg_action where name = 'AssetStatusService'),'default');

--rollback delete from eg_roleaction where actionid = (select id from eg_action where name = 'AssetStatusService')
--rollback		and roleCode in ('SUPERUSER','Asset Administrator','AssetCreator','AssetReportViewer');
--rollback delete from eg_action where name = 'AssetStatusService';
	
--rollback insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
--rollback lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'AssetStatusService',
--rollback		'/GET_STATUS','Asset Service',null,
--rollback			(select id from service where name='Asset Service' and tenantId='default'),1,
--rollback				'Get Status',false,1,now(),1,now());
	
--rollback insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', 
--rollback	(select id from eg_action where name = 'AssetStatusService'),'default');
--rollback insert into eg_roleaction(roleCode,actionid,tenantid)values('Asset Administrator', 
--rollback	(select id from eg_action where name = 'AssetStatusService'),'default');
--rollback insert into eg_roleaction(roleCode,actionid,tenantid)values('AssetCreator', 
--rollback	(select id from eg_action where name = 'AssetStatusService'),'default');