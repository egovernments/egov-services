insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'INVENTORY', 'Inventory', true, 'Inventory', 9, null , 'default');
insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'INVENTORY MASTERS', 'Inventory Masters', true, 'Inventory Masters',1, (select id from service where code = 'INVENTORY' and tenantId = 'default') , 'default');


insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'SUPPLIER MASTER', 'Supplier Master', true, 'Supplier Master',1, (select id from service where code = 'INVENTORY MASTERS' and tenantId = 'default') , 'default');


insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Supplier Create','/inventory-services/suppliers/_create','SUPPLIER MASTER',null,1,'Create Supplier',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Supplier Update','/inventory-services/suppliers/_update','SUPPLIER MASTER',null,2,'Update Supplier',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Supplier Search','/inventory-services/suppliers/_search','SUPPLIER MASTER',null,3,'Search Supplier',true,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Supplier Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Supplier Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Supplier Search' ),'default');