
INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)  
VALUES (nextval('SEQ_EG_ACTION'), 'Supplier wise Purchase Order History', '/inventory/reports/SupplierwisesPurchaseOrderHistoryReport', 'INVRPT',null, 
(select id from service where name='INV Report' and tenantid='default'), 4, 'Supplier wise Purchase Order History', true, 1, now(), 1, now());


insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Supplier wise Purchase Order History'),'default');


INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)  
VALUES (nextval('SEQ_EG_ACTION'), 'Material wise Purchase Order History', '/inventory/reports/MaterialwisePurchaseOrderHistoryReport', 'INVRPT',null, 
(select id from service where name='INV Report' and tenantid='default'), 5, 'Material wise Purchase Order History', true, 1, now(), 1, now());


insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Material wise Purchase Order History'),'default');
