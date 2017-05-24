INSERT INTO EG_ROLEACTION (ROLECODE,ACTIONID,TENANTID) values ('AssetCreator',(select id from eg_action where name = 'uploadfiles'),'default');
INSERT INTO EG_ROLEACTION (ROLECODE,ACTIONID,TENANTID) values ('AssetCreator',(select id from eg_action where name = 'filesearch'),'default');
INSERT INTO EG_ROLEACTION (ROLECODE,ACTIONID,TENANTID) values ('AssetCreator',(select id from eg_action where name = 'filesearchbytag'),'default');
INSERT INTO EG_ROLEACTION (ROLECODE,ACTIONID,TENANTID) values ('Asset Administrator',(select id from eg_action where name = 'uploadfiles'),'default');
INSERT INTO EG_ROLEACTION (ROLECODE,ACTIONID,TENANTID) values ('Asset Administrator',(select id from eg_action where name = 'filesearch'),'default');
INSERT INTO EG_ROLEACTION (ROLECODE,ACTIONID,TENANTID) values ('Asset Administrator',(select id from eg_action where name = 'filesearchbytag'),'default');

--rollback delete from eg_roleaction where actionid in ((select id from eg_action where name = 'uploadfiles'),
--rollback												(select id from eg_action where name = 'filesearch'),
--rollback												(select id from eg_action where name = 'filesearchbytag'))
--rollback								and rolecode in ('AssetCreator','Asset Administrator');