INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)
    VALUES (nextval('SEQ_EG_ACTION'), 'CreateBulkEmployees', '/hr-employee/employees/_bulkcreate', 'EIS', null,
        (SELECT id from service WHERE name='Employee Masters' AND tenantid='default'), 1, 'Create Bulk Employees', false, 1, now(), 1, now());
INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)
    VALUES (nextval('SEQ_EG_ACTION'), 'CreateBulkPositions', '/hr-masters/positions/_bulkcreate', 'Position', null,
        (SELECT id from service WHERE name='HR Masters' AND tenantid='default'), 1, 'Search Bulk Positions', false, 1, now(), 1, now());

INSERT INTO eg_roleaction (roleCode, actionid, tenantId)
    VALUES ('SUPERUSER', (SELECT id FROM eg_action WHERE name='CreateBulkEmployees'),'default');
INSERT INTO eg_roleaction (roleCode, actionid, tenantId)
    VALUES ('EMPLOYEE ADMIN', (SELECT id FROM eg_action WHERE name='CreateBulkEmployees' ),'default');

INSERT INTO eg_roleaction (roleCode, actionid, tenantId)
    VALUES ('SUPERUSER',(SELECT id FROM eg_action WHERE name='CreateBulkPositions' ),'default');
INSERT INTO eg_roleaction (roleCode, actionid, tenantId)
    VALUES ('EMPLOYEE ADMIN',(SELECT id FROM eg_action WHERE name='CreateBulkPositions' ),'default');
