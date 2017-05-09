INSERT INTO EG_ROLEACTION (ROLECODE,ACTIONID,TENANTID) values ('AssetCreator',(select id from eg_action where name like 'CreateAsset'),'default');
INSERT INTO EG_ROLEACTION (ROLECODE,ACTIONID,TENANTID) values ('AssetCreator',(select id from eg_action where name like 'CreateAssetService'),'default');
INSERT INTO EG_ROLEACTION (ROLECODE,ACTIONID,TENANTID) values ('AssetCreator',(select id from eg_action where name like 'ViewAssetService'),'default');
INSERT INTO EG_ROLEACTION (ROLECODE,ACTIONID,TENANTID) values ('AssetCreator',(select id from eg_action where name like 'ModifyAssetService'),'default');
INSERT INTO EG_ROLEACTION (ROLECODE,ACTIONID,TENANTID) values ('AssetCreator',(select id from eg_action where name like 'Create Agreement'),'default');
INSERT INTO EG_ROLEACTION (ROLECODE,ACTIONID,TENANTID) values ('AssetCreator',(select id from eg_action where name like 'ModifyAsset'),'default');
INSERT INTO EG_ROLEACTION (ROLECODE,ACTIONID,TENANTID) values ('AssetCreator',(select id from eg_action where name like 'ViewAsset'),'default');

--rollback delete from eg_roleaction where rolecode = 'AssetCreator' and actionid in (
	--(select id from eg_action where name like 'CreateAsset'),
	--(select id from eg_action where name like 'CreateAssetService'),
	--(select id from eg_action where name like 'Search and Edit-Asset'),
	--(select id from eg_action where name like 'ViewAssetService'),
	--(select id from eg_action where name like 'ModifyAssetService'),
	--(select id from eg_action where name like 'Create Agreement'),
	--(select id from eg_action where name like 'ModifyAsset'),
	--(select id from eg_action where name like 'ViewAsset')) and tenantid = 'default');