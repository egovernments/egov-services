insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Get Actions from MDMS' ),'mh');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Get Actions from MDMS' ),'mh.rohatest');
