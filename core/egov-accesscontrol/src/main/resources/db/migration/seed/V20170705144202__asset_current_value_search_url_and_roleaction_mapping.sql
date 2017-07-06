--Asset Current Value Search API
insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
	lastmodifieddate) values(nextval('SEQ_EG_ACTION'),'AssetCurrentValueSearchService','/asset-services/currentvalue/_search',
		'AssetCurrentValueSearchService',null,(select id from service where name='Asset Service' and tenantId='default'),1,
			'Get Current Asset Value',false,1,now(),1,now());
			
-- Role Action Mapping of Voucher API with SUPERUSER
insert into eg_roleaction(roleCode,actionid,tenantid) values ('SUPERUSER',
	(select id from eg_action where name = 'AssetCurrentValueSearchService'),'default');

-- Role Action Mapping of Voucher API with Asset Administrator
insert into eg_roleaction(roleCode,actionid,tenantid) values ('Asset Administrator',
	(select id from eg_action where name = 'AssetCurrentValueSearchService'),'default');

--rollback delete from eg_roleaction where roleCode in ('SUPERUSER','Asset Administrator') and 
--rollback	actionid in ((select id from eg_action where name = 'AssetCurrentValueSearchService'));
--rollback delete from eg_action where name = 'AssetCurrentValueSearchService';