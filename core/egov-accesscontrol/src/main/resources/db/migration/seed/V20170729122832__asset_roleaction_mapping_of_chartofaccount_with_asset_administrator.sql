insert into eg_roleaction(roleCode,actionid,tenantId) values('Asset Administrator',
	(select id from eg_action where name='searchChartOfAccount'),'default');

--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='searchChartOfAccount') and rolecode = 'Asset Administrator';
