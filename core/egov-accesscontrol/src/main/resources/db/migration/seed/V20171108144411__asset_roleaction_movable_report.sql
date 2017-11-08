update eg_Action set enabled=true,servicecode='AssetReports',name='AssetMovableRegister'  where name='MovableRegisterMetadata' and servicecode='AssetReports';

update eg_Action set enabled=true,servicecode='AssetReports',name='AssetMovableRegister'  where name='MovableRegisterMetadata' and servicecode='Asset Register Report';


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'AssetMovableRegister' AND servicecode='AssetReports' ),'default');
