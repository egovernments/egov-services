delete from eg_roleaction where actionid = (select id from eg_action where url='/user/users/_createnovalidate') and tenantId = 'default';
delete from eg_roleaction where actionid = (select id from eg_action where url='/user/_search') and tenantId = 'default';

delete from eg_action where name = 'SearchUser';

delete from eg_action where name = 'CreateUsernovalidate';

insert into eg_action(id,  name, url, servicecode, queryparams, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'), 'SearchUser', '/user/_search', 'ADMIN', null, 1, 'Search User', false, 1, now(), 1, now());
insert into eg_action(id,  name, url, servicecode, queryparams, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'), 'CreateUsernovalidate', '/user/users/_createnovalidate', 'ADMIN', null, 1, 'Create User novalidate', false, 1, now(), 1, now());

insert into eg_roleaction(rolecode, actionid, tenantid) values ('SUPERUSER', (select id from eg_action where url='/user/users/_createnovalidate'),'default');
insert into eg_roleaction(rolecode, actionid, tenantid) values ('SUPERUSER', (select id from eg_action where url='/user/_search'),'default');
