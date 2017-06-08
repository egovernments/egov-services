INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PPTNDC', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('DOCUMENTS', 'Y', 'multivaluelist', 'Y', NULL, 1, 'core.documents', 'PPTNDC', 'panavel', 0, 'now()', NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CHECKLIST', 'Y', 'multivaluelist', 'Y', NULL, 1, 'core.checklist', 'PPTNDC', 'panavel', 0, 'now()', NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PPID', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.property.id', 'PPTNDC', 'panavel', 0, 'now()', NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('APPLICATIONFEE', 'N', 'number', 'Y', NULL, 2, 'core.application40.fee', 'PPTNDC', 'panavel', 0, 'now()', NULL, 0, NULL);

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PPTNDC', 'DOCUMENTS', 'LPP', 'core.lastpaid.property', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PPTNDC', 'CHECKLIST', 'HYP', 'core.halfyear.property', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PPTNDC', 'CHECKLIST', 'OP', 'core.own.property', 'panavel', 0, now(), NULL, 0, NULL, 'Y');