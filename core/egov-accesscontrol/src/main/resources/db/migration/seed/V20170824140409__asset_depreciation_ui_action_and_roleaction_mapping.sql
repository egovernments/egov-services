--enabled asset depreciation module
update service set enabled = true,ordernumber=3 where code = 'AssetDepreciation';

--action for asset depreciation UI
INSERT INTO eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname, enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
VALUES (NEXTVAL('SEQ_EG_ACTION'),'AssetDepreciationCreate','/app/asset/asset-depreciation.html','AssetDepreciation',null,
(select id from service where name ='Asset Depreciation'),1,'Create Depreciation',true,1,now(),1,now());

update eg_action set url = '/app/asset/create-asset-sale.html' where name = 'AssetSaleAndDisposalCreate';

--roleaction for asset depreciation UI
insert into eg_roleaction(roleCode,actionid,tenantid) values ('SUPERUSER',
	(select id from eg_action where name = 'AssetDepreciationCreate'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid) values ('Asset Administrator',
	(select id from eg_action where name = 'AssetDepreciationCreate'),'default');
