insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'STORE MASTER', 'Store Master', true, 'Store Master',3, (select id from service where code = 'INVENTORY MASTERS' and tenantId = 'default') , 'default');
insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'MATERIAL MASTER', 'Material Master', true, 'Material Master',4, (select id from service where code = 'INVENTORY MASTERS' and tenantId = 'default') , 'default');
insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'PRICE LIST MASTER', 'Price List Master', true, 'Price List Master',5, (select id from service where code = 'INVENTORY MASTERS' and tenantId = 'default') , 'default');


insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Store Create','/inventory-services/stores/_create','STORE MASTER',null,1,'Create Store',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Store Update','/inventory-services/stores/_update','STORE MASTER',null,2,'Update Store',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Store Search','/inventory-services/stores/_search','STORE MASTER',null,3,'Search Store',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Material Create','/inventory-services/materials/_create','MATERIAL MASTER',null,1,'Create Material',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Material Update','/inventory-services/materials/_update','MATERIAL MASTER',null,2,'Update Material',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Material Search','/inventory-services/materials/_search','MATERIAL MASTER',null,3,'Search Material',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'PriceList Create','/inventory-services/pricelists/_create','PRICE LIST MASTER',null,1,'Create PriceList',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'PriceList Update','/inventory-services/pricelists/_update','PRICE LIST MASTER',null,2,'Update PriceList',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'PriceList Search','/inventory-services/pricelists/_search','PRICE LIST MASTER',null,3,'Search PriceList',false,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Store Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Store Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Store Search' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Material Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Material Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Material Search' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'PriceList Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'PriceList Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'PriceList Search' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('STORE INCHARGE',(select id from eg_action where name = 'Store Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('STORE INCHARGE',(select id from eg_action where name = 'Store Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('STORE INCHARGE',(select id from eg_action where name = 'Store Search' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('STORE INCHARGE',(select id from eg_action where name = 'Material Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('STORE INCHARGE',(select id from eg_action where name = 'Material Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('STORE INCHARGE',(select id from eg_action where name = 'Material Search' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('STORE INCHARGE',(select id from eg_action where name = 'Material Store Mapping Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('STORE INCHARGE',(select id from eg_action where name = 'Material Store Mapping Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('STORE INCHARGE',(select id from eg_action where name = 'Material Store Mapping Search' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('STORE INCHARGE',(select id from eg_action where name = 'Supplier Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('STORE INCHARGE',(select id from eg_action where name = 'Supplier Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('STORE INCHARGE',(select id from eg_action where name = 'Supplier Search' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('STORE INCHARGE',(select id from eg_action where name = 'PriceList Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('STORE INCHARGE',(select id from eg_action where name = 'PriceList Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('STORE INCHARGE',(select id from eg_action where name = 'PriceList Search' ),'default');