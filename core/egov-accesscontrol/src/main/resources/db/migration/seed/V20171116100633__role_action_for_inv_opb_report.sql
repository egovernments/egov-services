
insert into service (id, code, name, enabled, displayname, ordernumber,parentmodule, tenantId) 
values (nextval('SEQ_SERVICE'), 'INVRPT', 'Inventory Report', true, 'Inventory Reports', 1, (select id from service where code = 'INVENTORY' and tenantId = 'default'),'default');


INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)  
VALUES (nextval('SEQ_EG_ACTION'), 'Inventory Report MetaData', '/report/inventory/metadata/_get', 'INVRPT',null,   (select id from service where name='INV Report' and tenantid='default'), 1,   'Get Inventory Report Metadata', false, 1, now(), 1, now());

INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)  
VALUES (nextval('SEQ_EG_ACTION'), 'Inventory Report', '/report/inventory/_get', 'INVRPT',null,   (select id from service where name='INV Report' and tenantid='default'), 1,   'Get Inventory Report', false, 1, now(), 1, now());

INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)  
VALUES (nextval('SEQ_EG_ACTION'), 'Inventory Report Reload', '/report/inventory/_reload', 'INVRPT',null,   (select id from service where name='INV Report' and tenantid='default'), 1,   'Inventory Report Reload', false, 1, now(), 1, now());


insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Inventory Report MetaData'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Inventory Report'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Inventory Report Reload'),'default');



INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)  
VALUES (nextval('SEQ_EG_ACTION'), 'Opening Balance Report', '/inventory/reports/openingBalanceReport', 'INVRPT',null, 
(select id from service where name='INV Report' and tenantid='default'), 5, 'Opening Balance Report', true, 1, now(), 1, now());


insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Opening Balance Report'),'default');
