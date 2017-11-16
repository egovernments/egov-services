INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)
VALUES (nextval('SEQ_EG_ACTION'), 'Supplier Report', '/inventory/reports/supplierreport', 'INVRPT',null,
(select id from service where name='INV Report' and tenantid='default'), 2, 'Supplier List', true, 1, now(), 1, now());


insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Supplier Report'),'default');