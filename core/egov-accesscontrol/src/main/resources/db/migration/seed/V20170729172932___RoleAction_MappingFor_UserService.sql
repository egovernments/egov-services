insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values
(nextval('SEQ_EG_ACTION'), 'Create Citizen','/user/citizen/_create','ADMIN', NULL,(select id from service where code='ADMIN' and tenantid='default'), 1,'Create Citizen',false,1,now(),1,now());

insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values
(nextval('SEQ_EG_ACTION'), 'Search User Details','/user/v1/_search','ADMIN', NULL,(select id from service where code='ADMIN' and tenantid='default'), 1,'Search User Details',false,1,now(),1,now());

insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values
(nextval('SEQ_EG_ACTION'), 'Get User','/user/_details','ADMIN', NULL,(select id from service where code='ADMIN' and tenantid='default'), 1,'Get User Details',false,1,now(),1,now());

insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values
(nextval('SEQ_EG_ACTION'), 'UpdateUserWithoutValidation','/user/users/{id}/_updatenovalidate','ADMIN', NULL,(select id from service where code='ADMIN' and tenantid='default'), 1,'Update User WithoutValidation',false,1,now(),1,now());


insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values
(nextval('SEQ_EG_ACTION'), 'Profile Update','/user/profile/_update','ADMIN', NULL,(select id from service where code='ADMIN' and tenantid='default'), 1,'Profile Update',false,1,now(),1,now());

insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values
(nextval('SEQ_EG_ACTION'), 'Update Password','/user/password/_update','ADMIN', NULL,(select id from service where code='ADMIN' and tenantid='default'), 1,'Update Password',false,1,now(),1,now());

insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values
(nextval('SEQ_EG_ACTION'), 'UpdatePasswordForNonLoggedInUser','/user/password/nologin/_update','ADMIN', NULL,(select id from service where code='ADMIN' and tenantid='default'), 1,'UpdatePassword For NonLogged InUser',false,1,now(),1,now());

insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values
(nextval('SEQ_EG_ACTION'), 'Delete Token','/user/_logout','ADMIN', NULL,(select id from service where code='ADMIN' and tenantid='default'), 1,'Delete Token',false,1,now(),1,now());



insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Create Citizen'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Search User Details'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Get User'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='UpdateUserWithoutValidation'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Profile Update'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Update Password'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='UpdatePasswordForNonLoggedInUser'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Delete Token'),'default');