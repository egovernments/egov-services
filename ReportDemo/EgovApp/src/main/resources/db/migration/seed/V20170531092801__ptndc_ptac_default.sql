INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CHECKLIST', 'Y', 'multivaluelist', 'Y', NULL, 1, 'core.checklist', 'PTNDC', 'default', 0, 'now()', NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('DOCUMENTS', 'Y', 'multivaluelist', 'Y', NULL, 1, 'core.documents', 'PTNDC', 'default', 0, 'now()', NULL, 0, NULL);

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PTNDC', 'DOCUMENTS', 'LPP', 'core.lastpaid.property', 'default', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PTNDC', 'CHECKLIST', 'HYP', 'core.halfyear.property', 'default', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PTNDC', 'CHECKLIST', 'OP', 'core.own.property', 'default', 0, now(), NULL, 0, NULL, 'Y');


INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CHECKLIST', 'Y', 'multivaluelist', 'Y', NULL, 1, 'core.checklist', 'PTAC', 'default', 0, 'now()', NULL, 0, NULL);


INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('DOCUMENTS', 'Y', 'multivaluelist', 'Y', NULL, 1, 'core.documents', 'PTAC', 'default', 0, 'now()', NULL, 0, NULL);

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PTAC', 'DOCUMENTS', 'LPP', 'core.lastpaid.property', 'default', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PTAC', 'CHECKLIST', 'HYP', 'core.halfyear.property', 'default', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PTAC', 'CHECKLIST', 'OP', 'core.own.property', 'default', 0, now(), NULL, 0, NULL, 'Y');