insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) values (nextval('SEQ_SERVICE'),'TENANT','Tenant',false,'/tenant','Tenant',NULL,NULL,'default');


insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values
(nextval('SEQ_EG_ACTION'), 'CreateTenant','/tenant/v1/tenant/_create','TENANT', NULL,(select id from service where code='TENANT' and tenantid='default'), 1,'Create Tenant',false,1,now(),1,now());


insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values
(nextval('SEQ_EG_ACTION'), 'UpdateTenant','/tenant/v1/tenant/_update','TENANT', NULL,(select id from service where code='TENANT' and tenantid='default'), 1,'Update Tenant',false,1,now(),1,now());

insert into eg_action(id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values
(nextval('SEQ_EG_ACTION'), 'SearchTenant','/tenant/v1/tenant/_search','TENANT', NULL,(select id from service where code='TENANT' and tenantid='default'), 1,'Search Tenant',false,1,now(),1,now());



insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='CreateTenant'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='UpdateTenant'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='SearchTenant'),'default');