--Asset Depreciation Module
INSERT INTO service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantid) 
VALUES (NEXTVAL('SEQ_SERVICE'),'AssetDepreciation' ,'Asset Depreciation', false, 'asset-web',
	'Asset Depreciation', 1,(select id from service where name='Asset Transactions' and tenantid = 'default'), 'default');

--eg_action for asset Depreciation create POST
insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,
	enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) values (nextval('SEQ_EG_ACTION'),
	'AssetDepreciationCreateService','asset-services/assets/depreciations/_create',
	'AssetDepreciation',null,(select id from service where name = 'Asset Depreciation' and tenantid = 'default'),
	1,'Create Asset Depreciation',false,1,now(),1,now());

--roleaction for asset depreciation
insert into eg_roleaction(roleCode,actionid,tenantid) values ('SUPERUSER',
	(select id from eg_action where name = 'AssetDepreciationCreateService'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid) values ('Asset Administrator',
	(select id from eg_action where name = 'AssetDepreciationCreateService'),'default');

--rollback delete from eg_roleaction where roleCode in ('SUPERUSER','Asset Administrator') and actionid in ((select id from eg_action where name = 'AssetDepreciationCreateService'));

--rollback delete from eg_action where name = 'AssetDepreciationCreateService';

--rollback delete from service where name = 'AssetDepreciation';
