INSERT INTO service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantId)
    VALUES (nextval('SEQ_SERVICE'),'Nominee', 'Nominee', true, 'eis', 'Nominee', 13,
        (SELECT id FROM service WHERE name = 'EIS Masters' and tenantid='default'), 'default');

INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled,
        createdby, createddate, lastmodifiedby, lastmodifieddate)
    VALUES (nextval('SEQ_EG_ACTION'), 'NomineeSearch', '/nominees/_search', 'EIS', null,
        (SELECT id FROM service WHERE name = 'HR Employee' and tenantid='default'), 0, 'NomineeSearch', false, 1, now(), 1, now());
INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled,
        createdby, createddate, lastmodifiedby, lastmodifieddate)
    VALUES (nextval('SEQ_EG_ACTION'), 'NomineeCreate', '/nominees/_create', 'EIS', null,
        (SELECT id FROM service WHERE name = 'HR Employee' and tenantid='default'), 0, 'NomineeCreate', false, 1, now(), 1, now());
INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled,
        createdby, createddate, lastmodifiedby, lastmodifieddate)
    VALUES (nextval('SEQ_EG_ACTION'), 'NomineeUpdate', '/nominees/_update', 'EIS', null,
        (SELECT id FROM service WHERE name = 'HR Employee' and tenantid='default'), 0, 'NomineeUpdate', false, 1, now(), 1, now());
INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled,
        createdby, createddate, lastmodifiedby, lastmodifieddate)
    VALUES (nextval('SEQ_EG_ACTION'), 'CreateNominee', '/app/hr/common/employee-search-common.html', 'EIS', 'value=nominee',
        (SELECT id FROM service WHERE name = 'Nominee' and tenantid='default'), 1, 'Create', true, 1, now(), 1, now());
INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled,
        createdby, createddate, lastmodifiedby, lastmodifieddate)
    VALUES (nextval('SEQ_EG_ACTION'), 'CreateNomineeMaster', '/app/hr/nomineeMaster/nominee.html', 'EIS', null,
        (SELECT id FROM service WHERE name = 'Nominee' and tenantid='default'), 0, 'CreateNomineeMaster', false, 1, now(), 1, now());

INSERT INTO eg_roleaction (roleCode, actionid, tenantid)
    VALUES ('SUPERUSER', (SELECT id FROM eg_action WHERE name = 'NomineeSearch'), 'default');
INSERT INTO eg_roleaction (roleCode, actionid, tenantid)
    VALUES ('SUPERUSER', (SELECT id FROM eg_action WHERE name = 'NomineeCreate'), 'default');
INSERT INTO eg_roleaction (roleCode, actionid, tenantid)
    VALUES ('SUPERUSER', (SELECT id FROM eg_action WHERE name = 'NomineeUpdate'), 'default');
INSERT INTO eg_roleaction (roleCode, actionid, tenantid)
    VALUES ('SUPERUSER', (SELECT id FROM eg_action WHERE name = 'CreateNominee'), 'default');
INSERT INTO eg_roleaction (roleCode, actionid, tenantid)
    VALUES ('SUPERUSER', (SELECT id FROM eg_action WHERE name = 'CreateNomineeMaster'), 'default');

INSERT INTO eg_roleaction (roleCode, actionid, tenantid)
    VALUES ('EMPLOYEE ADMIN', (SELECT id FROM eg_action WHERE name = 'NomineeSearch'), 'default');
INSERT INTO eg_roleaction (roleCode, actionid, tenantid)
    VALUES ('EMPLOYEE ADMIN', (SELECT id FROM eg_action WHERE name = 'NomineeCreate'), 'default');
INSERT INTO eg_roleaction (roleCode, actionid, tenantid)
    VALUES ('EMPLOYEE ADMIN', (SELECT id FROM eg_action WHERE name = 'NomineeUpdate'), 'default');
INSERT INTO eg_roleaction (roleCode, actionid, tenantid)
    VALUES ('EMPLOYEE ADMIN', (SELECT id FROM eg_action WHERE name = 'CreateNominee'), 'default');
INSERT INTO eg_roleaction (roleCode, actionid, tenantid)
    VALUES ('EMPLOYEE ADMIN', (SELECT id FROM eg_action WHERE name = 'CreateNomineeMaster'), 'default');
