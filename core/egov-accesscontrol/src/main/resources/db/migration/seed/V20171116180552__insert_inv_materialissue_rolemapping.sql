insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'MATERIAL ISSUES',
'Material Issues', true, 'Material Issues',13, (select id from service where code = 'INVENTORY TRANSACTIONS' and tenantId = 'default') , 'default');

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Material Issue Create','/inventory-services/materialIssues/_create','MATERIAL ISSUES',null,1,
'Create Material Issue',false,1,now(),1,now());


insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Material Issue Update','/inventory-services/materialIssues/_update','MATERIAL ISSUES',null,2,
'Update Material Issue',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Material Issue Search','/inventory-services/materialIssues/_search','MATERIAL ISSUES',null,3,
'Search Material Issue',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Material Issue Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Material Issue Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Material Issue Search' ),'default');
