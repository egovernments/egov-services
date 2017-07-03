INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled,
        createdby, createddate, lastmodifiedby, lastmodifieddate)
    VALUES (nextval('SEQ_EG_ACTION'), 'SearchGender', '/genders/_search', 'EIS', null,
        (SELECT id FROM service WHERE name = 'EGOV Common Masters' and tenantid='default'), 0, 'SearchGender', false, 1, now(), 1, now());
INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled,
        createdby, createddate, lastmodifiedby, lastmodifieddate)
    VALUES (nextval('SEQ_EG_ACTION'), 'SearchBloodGroup', '/bloodgroups/_search', 'EIS', null,
        (SELECT id FROM service WHERE name = 'EGOV Common Masters' and tenantid='default'), 0, 'SearchBloodGroup', false, 1, now(), 1, now());
INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled,
        createdby, createddate, lastmodifiedby, lastmodifieddate)
    VALUES (nextval('SEQ_EG_ACTION'), 'SearchRelationship', '/relationships/_search', 'EIS', null,
        (SELECT id FROM service WHERE name = 'EGOV Common Masters' and tenantid='default'), 0, 'SearchRelationship', false, 1, now(), 1, now());
INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled,
        createdby, createddate, lastmodifiedby, lastmodifieddate)
    VALUES (nextval('SEQ_EG_ACTION'), 'SearchMaritalStatus', '/maritalstatuses/_search', 'EIS', null,
        (SELECT id FROM service WHERE name = 'EGOV Common Masters' and tenantid='default'), 0, 'SearchMaritalStatus', false, 1, now(), 1, now());

INSERT INTO eg_roleaction (roleCode, actionid, tenantid)
    VALUES ('SUPERUSER', (SELECT id FROM eg_action WHERE name = 'SearchGender'), 'default');
INSERT INTO eg_roleaction (roleCode, actionid, tenantid)
    VALUES ('SUPERUSER', (SELECT id FROM eg_action WHERE name = 'SearchBloodGroup'), 'default');
INSERT INTO eg_roleaction (roleCode, actionid, tenantid)
    VALUES ('SUPERUSER', (SELECT id FROM eg_action WHERE name = 'SearchRelationship'), 'default');
INSERT INTO eg_roleaction (roleCode, actionid, tenantid)
    VALUES ('SUPERUSER', (SELECT id FROM eg_action WHERE name = 'SearchMaritalStatus'), 'default');

INSERT INTO eg_roleaction (roleCode, actionid, tenantid)
    VALUES ('EMPLOYEE ADMIN', (SELECT id FROM eg_action WHERE name = 'SearchGender'), 'default');
INSERT INTO eg_roleaction (roleCode, actionid, tenantid)
    VALUES ('EMPLOYEE ADMIN', (SELECT id FROM eg_action WHERE name = 'SearchBloodGroup'), 'default');
INSERT INTO eg_roleaction (roleCode, actionid, tenantid)
    VALUES ('EMPLOYEE ADMIN', (SELECT id FROM eg_action WHERE name = 'SearchRelationship'), 'default');
INSERT INTO eg_roleaction (roleCode, actionid, tenantid)
    VALUES ('EMPLOYEE ADMIN', (SELECT id FROM eg_action WHERE name = 'SearchMaritalStatus'), 'default');

INSERT INTO eg_roleaction (roleCode, actionid, tenantid)
    VALUES ('EMPLOYEE', (SELECT id FROM eg_action WHERE name = 'SearchGender'), 'default');
INSERT INTO eg_roleaction (roleCode, actionid, tenantid)
    VALUES ('EMPLOYEE', (SELECT id FROM eg_action WHERE name = 'SearchBloodGroup'), 'default');
INSERT INTO eg_roleaction (roleCode, actionid, tenantid)
    VALUES ('EMPLOYEE', (SELECT id FROM eg_action WHERE name = 'SearchRelationship'), 'default');
INSERT INTO eg_roleaction (roleCode, actionid, tenantid)
    VALUES ('EMPLOYEE', (SELECT id FROM eg_action WHERE name = 'SearchMaritalStatus'), 'default');
