INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('COMPLAINT', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid,url, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PRIORITY', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'pgr.priority', 'COMPLAINT', 'default', NULL, 0, now(), NULL, 0, NULL);

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('COMPLAINT', 'PRIORITY', 'PRIORITY-1', 'pgr.priority.one', 'default', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('COMPLAINT', 'PRIORITY', 'PRIORITY-2', 'pgr.priority.two', 'default', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('COMPLAINT', 'PRIORITY', 'PRIORITY-3', 'pgr.priority.three', 'default', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO attribute_role_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('COMPLAINT', 'PRIORITY', 'EMPLOYEE', 'default', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_action_definition (servicecode, attributecode, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('COMPLAINT', 'PRIORITY', 'UPDATE', 'default', 0, now(), NULL, 0, NULL);