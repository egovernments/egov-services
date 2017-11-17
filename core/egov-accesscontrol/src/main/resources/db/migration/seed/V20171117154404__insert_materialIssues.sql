delete from service where name ='Material Issues' and tenantId = 'default';
delete from eg_action where name ='Material Issue Create';
delete from eg_action where name ='Material Issue Update';
delete from eg_action where name ='Material Issue Search';

delete from eg_roleaction where actionid=(select id from eg_action where name = 'Material Issue Create' );
delete from eg_roleaction where actionid=(select id from eg_action where name = 'Material Issue Update' );
delete from eg_roleaction where actionid=(select id from eg_action where name = 'Material Issue Search' );


insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'MATERIAL ISSUE',
'Material Issue', true, 'Material Issue',13, (select id from service where code = 'INVENTORY TRANSACTIONS' and tenantId = 'default') , 'default');

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Material Issues Create','/inventory-services/materialissues/_create','MATERIAL ISSUE',null,1,
'Create Material Issues',false,1,now(),1,now());


insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Material Issues Update','/inventory-services/materialissues/_update','MATERIAL ISSUE',null,2,
'Update Material Issues',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Material Issues Search','/inventory-services/materialissues/_search','MATERIAL ISSUE',null,3,
'Search Material Issues',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Material Issues Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Material Issues Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Material Issues Search' ),'default');
