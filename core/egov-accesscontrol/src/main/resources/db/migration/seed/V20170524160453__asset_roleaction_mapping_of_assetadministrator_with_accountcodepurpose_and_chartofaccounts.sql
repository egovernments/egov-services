INSERT INTO EG_ROLEACTION (ROLECODE,ACTIONID,TENANTID) values ('Asset Administrator',(select id from eg_action where name = 'searchAccountCodePurpose'),'default');
INSERT INTO EG_ROLEACTION (ROLECODE,ACTIONID,TENANTID) values ('Asset Administrator',(select id from eg_action where name = 'searchChartOfAccount'),'default');

--rollback delete from eg_roleaction where actionid in ((select id from eg_action where name = 'searchChartOfAccount'),
--rollback												(select id from eg_action where name = 'searchAccountCodePurpose')) 
--rollback								and	rolecode in ('Asset Administrator');