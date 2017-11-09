--Service Data

insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 
'INVENTORY TRANSACTIONS', 'Inventory Transactions', true, 'Inventory Transactions',2, (select id from service where code = 'INVENTORY' and tenantId = 'default') , 'default');
--Indent
insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'INDENT',
'Indent', true, 'Indent',1, (select id from service where code = 'INVENTORY TRANSACTIONS' and tenantId = 'default') , 'default');
--Indent Issues
insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'),
'INDENT ISSUES', 'Indent Issues', true, 'Indent Issues',2, (select id from service where code = 'INVENTORY TRANSACTIONS' and tenantId = 'default') , 'default');
--Indent Notes
insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'),
'INDENT NOTES', 'Indent Notes', true, 'Indent Notes',3, (select id from service where code = 'INVENTORY TRANSACTIONS' and tenantId = 'default') , 'default');
--Scrap 
insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'),
'SCRAP', 'Scrap', true, 'Scrap',4, (select id from service where code = 'INVENTORY TRANSACTIONS' and tenantId = 'default') , 'default');
--Purchase Indent
insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'),
'PURCHASE INDENT', 'Purchase Indent', true, 'Purchase Indent',5, (select id from service where code = 'INVENTORY TRANSACTIONS' and tenantId = 'default') , 'default');
--Purchase Material
insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'),
'PURCHASE MATERIAL', 'Purchase Material', true, 'Purchase Material',6, (select id from service where code = 'INVENTORY TRANSACTIONS' and tenantId = 'default') , 'default');
--Purchase Order
insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'),
'PURCHASE ORDER', 'Purchase Order', true, 'Purchase Order',7, (select id from service where code = 'INVENTORY TRANSACTIONS' and tenantId = 'default') , 'default');
--Receipt Note
insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'),
'RECEIPT NOTE', 'Receipt Note', true, 'Receipt Note',8, (select id from service where code = 'INVENTORY TRANSACTIONS' and tenantId = 'default') , 'default');
--Transfer Indent Note
insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'),
'TRANSFER INDENT NOTE', 'Transfer Indent Note', true, 'Transfer Indent Note',9, (select id from service where code = 'INVENTORY TRANSACTIONS' and tenantId = 'default') , 'default');
--Transfer Inwards
insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'),
'TRANSFER INWARDS', 'Transfer Inwards', true, 'Transfer Inwards',10, (select id from service where code = 'INVENTORY TRANSACTIONS' and tenantId = 'default') , 'default');
--Transfer Outwards
insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'),
'TRANSFER OUTWARDS', 'Transfer Outwards', true, 'Transfer Outwards',11, (select id from service where code = 'INVENTORY TRANSACTIONS' and tenantId = 'default') , 'default');
--Opening Balance
insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'),
'OPENING BALANCE', 'Opening Balance', true, 'Opening Balance',12, (select id from service where code = 'INVENTORY TRANSACTIONS' and tenantId = 'default') , 'default');

-- Action Data

--Indent
insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Indent Create','/inventory-services/indents/_create','INDENT',null,1,
'Create Indent',false,1,now(),1,now());


insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Indent Update','/inventory-services/indents/_update','INDENT',null,2,
'Update Indent',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Indent Search','/inventory-services/indents/_search','INDENT',null,3,
'Search Indent',false,1,now(),1,now());

--Indent Issue
insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Indent Issue Create','/inventory-services/indentissues/_create','INDENT ISSUES',
null,1,'Create Indent Issue',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Indent Issue Update','/inventory-services/indentissues/_update','INDENT ISSUES',
null,2,'Update Indent Issue',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Indent Issue Search','/inventory-services/indentissues/_search','INDENT ISSUES',
null,3,'Search Indent Issue',false,1,now(),1,now());

--Indent Notes
insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Indent Note Create','/inventory-services/indentnotes/_create','INDENT NOTES',
null,1,'Create Indent Note',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Indent Note Update','/inventory-services/indentnotes/_update','INDENT NOTES',
null,2,'Update Indent Note',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Indent Note Search','/inventory-services/indentnotes/_search','INDENT NOTES',
null,3,'Search Indent Note',false,1,now(),1,now());

--Scrap
insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Scrap Create','/inventory-services/scraps/_create','SCRAP',
null,1,'Create Scrap',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Scrap Update','/inventory-services/scraps/_update','SCRAP',
null,2,'Update Scrap',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Scrap Search','/inventory-services/scraps/_search','SCRAP',
null,3,'Search Scrap',false,1,now(),1,now());

--Purchase Indent
insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Purchase Indent Create','/inventory-services/purchaseindents/_create','PURCHASE INDENT',
null,1,'Create Purchase Indent',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Purchase Indent Update','/inventory-services/purchaseindents/_update','PURCHASE INDENT',
null,2,'Update Purchase Indent',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Purchase Indent Search','/inventory-services/purchaseindents/_search','PURCHASE INDENT',
null,3,'Search Purchase Indent',false,1,now(),1,now());

--Purchase Material
insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Purchase Material Create','/inventory-services/purchasematerials/_create','PURCHASE MATERIAL',
null,1,'Create Purchase Material',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Purchase Material Update','/inventory-services/purchasematerials/_update','PURCHASE MATERIAL',
null,2,'Update Purchase Material',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Purchase Material Search','/inventory-services/purchasematerials/_search','PURCHASE MATERIAL',
null,3,'Search Purchase Material',false,1,now(),1,now());

--Purchase Order
insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Purchase Order Create','/inventory-services/purchaseorders/_create','PURCHASE ORDER',
null,1,'Create Purchase Order',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Purchase Order Update','/inventory-services/purchaseorders/_update','PURCHASE ORDER',
null,2,'Update Purchase Order',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Purchase Order Search','/inventory-services/purchaseorders/_search','PURCHASE ORDER',
null,3,'Search Purchase Order',false,1,now(),1,now());

--Receipt Note
insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Receipt Note Create','/inventory-services/receiptnotes/_create','RECEIPT NOTE',
null,1,'Create Receipt Note',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Receipt Note Update','/inventory-services/receiptnotes/_update','RECEIPT NOTE',
null,2,'Update Receipt Note',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Receipt Note Search','/inventory-services/receiptnotes/_search','RECEIPT NOTE',
null,3,'Search Receipt Note',false,1,now(),1,now());

--Transfer Indent Note
insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Transfer Indent Note Create','/inventory-services/transferindentnotes/_create','TRANSFER INDENT NOTE',
null,1,'Create Transfer Indent Note',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Transfer Indent Note Update','/inventory-services/transferindentnotes/_update','TRANSFER INDENT NOTE',
null,2,'Update Transfer Indent Note',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Transfer Indent Note Search','/inventory-services/transferindentnotes/_search','TRANSFER INDENT NOTE',
null,3,'Search Transfer Indent Note',false,1,now(),1,now());

--Transfer Inwards
insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Transfer Inwards Create','/inventory-services/transferinwards/_create','TRANSFER INWARDS',
null,1,'Create Transfer Inwards',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Transfer Inwards Update','/inventory-services/transferinwards/_update','TRANSFER INWARDS',
null,2,'Update Transfer Inwards',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Transfer Inwards Search','/inventory-services/transferinwards/_search','TRANSFER INWARDS',
null,3,'Search Transfer Inwards',false,1,now(),1,now());

--Transfer Outwards
insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Transfer Outwards Create','/inventory-services/transferoutwards/_create','TRANSFER OUTWARDS',
null,1,'Create Transfer Outwards',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Transfer Outwards Update','/inventory-services/transferoutwards/_update','TRANSFER OUTWARDS',
null,2,'Update Transfer Outwards',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Transfer Outwards Search','/inventory-services/transferoutwards/_search','TRANSFER OUTWARDS',
null,3,'Search Transfer Outwards',false,1,now(),1,now());

--Opening Balance
insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Opening Balance Create','/inventory-services/openingbalance/_create','OPENING BALANCE',
null,1,'Create Opening Balance',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Opening Balance Update','/inventory-services/openingbalance/_update','OPENING BALANCE',
null,2,'Update Opening Balance',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Opening Balance Search','/inventory-services/openingbalance/_search','OPENING BALANCE',
null,3,'Search Opening Balance',false,1,now(),1,now());



-- RoleAction Data
--Indent
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Indent Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Indent Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Indent Search' ),'default');

--Indent Issue
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Indent Issue Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Indent Issue Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Indent Issue Search' ),'default');

--Indent Notes
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Indent Note Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Indent Note Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Indent Note Search' ),'default');

--Scrap
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Scrap Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Scrap Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Scrap Search' ),'default');

--Purchase Indent
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Purchase Indent Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Purchase Indent Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Purchase Indent Search' ),'default');

--Purchase Material
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Purchase Material Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Purchase Material Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Purchase Material Search' ),'default');

--Purchase Order
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Purchase Order Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Purchase Order Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Purchase Order Search' ),'default');

--Receipt Note
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Receipt Note Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Receipt Note Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Receipt Note Search' ),'default');

--Transfer Indent Note
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Transfer Indent Note Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Transfer Indent Note Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Transfer Indent Note Search' ),'default');

--Transfer Inwards
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Transfer Inwards Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Transfer Inwards Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Transfer Inwards Search' ),'default');

--Transfer Outwards
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Transfer Outwards Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Transfer Outwards Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Transfer Outwards Search' ),'default');

--Opening Balance
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Opening Balance Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Opening Balance Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Opening Balance Search' ),'default');
