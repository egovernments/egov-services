insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId)
values (nextval('SEQ_SERVICE'), 'Material Store Mapping', 'Material Store Mapping', false, 'Material Store Mapping',1,
(select id from service where code = 'INVENTORY MASTERS' and tenantId = 'default') , 'default');


insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Material Store Mapping Create','/inventory-services/materialstoremapping/_create','Material Store Mapping',null,1,
'Create Material Store Mapping',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Material Store Mapping Update','/inventory-services/materialstoremapping/_update','Material Store Mapping',null,2,
'Update Material Store Mapping',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Material Store Mapping Search','/inventory-services/materialstoremapping/_search','Material Store Mapping',null,3,
'Search Material Store Mapping',false,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Material Store Mapping Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Material Store Mapping Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Material Store Mapping Search' ),'default');