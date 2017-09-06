insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) 
values (nextval('SEQ_SERVICE'), 'CITIZEN', 'CITIZEN SERVICES', true, 'Citizen Services', 4, null, 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'RequestSearch','/citizen-services/v1/requests/_search',
'CITIZEN',null,(select id from service where code='CITIZEN' and tenantid='default'),0,
'Search Request',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'RequestCreate','/citizen-services/v1/requests/_create',
'CITIZEN',null,(select id from service where code='CITIZEN' and tenantid='default'),0,
'Create Request',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'RequestUpdate','/citizen-services/v1/requests/_update',
'CITIZEN',null,(select id from service where code='CITIZEN' and tenantid='default'),0,
'Update Request',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'RequestCreateNew','/citizen-services/v1/pgrequest/_create',
'CITIZEN',null,(select id from service where code='CITIZEN' and tenantid='default'),0,
'Create Request',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'RequestValidate','/citizen-services/v1/pgrequest/_validate',
'CITIZEN',null,(select id from service where code='CITIZEN' and tenantid='default'),0,
'Validate Request',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'ServicesSearch','/citizen-services/v1/_search',
'CITIZEN',null,(select id from service where code='CITIZEN' and tenantid='default'),0,
'Search Service',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'PGResponse','/citizen-services/v1/pgresponse',
'CITIZEN',null,(select id from service where code='CITIZEN' and tenantid='default'),0,
'PG Response',false,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('CITIZEN',
 (select id from eg_action where name = 'RequestSearch' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('CITIZEN',
 (select id from eg_action where name = 'RequestCreate' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('CITIZEN',
 (select id from eg_action where name = 'RequestUpdate' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('CITIZEN',
 (select id from eg_action where name = 'RequestCreateNew' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('CITIZEN',
 (select id from eg_action where name = 'RequestValidate' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('CITIZEN',
 (select id from eg_action where name = 'ServicesSearch' ),'default');
 insert into eg_roleaction(roleCode,actionid,tenantid)values('CITIZEN',
 (select id from eg_action where name = 'PGResponse' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'RequestSearch' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'RequestCreate' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'RequestUpdate' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'RequestCreateNew' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'RequestValidate' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'ServicesSearch' ),'default');

 insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'PGResponse' ),'default')
