insert into eg_roleaction(roleCode,actionid,tenantId)values('Asset Administrator',
	(select id from eg_action where name='searchFinancialYear'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('AssetCreator',
	(select id from eg_action where name='searchFinancialYear'),'default');

--rollback delete from eg_roleaction where roleCode in ('Asset Administrator','AssetCreator') and 
--rollback 	actionid = (select id from eg_action where name='searchFinancialYear');