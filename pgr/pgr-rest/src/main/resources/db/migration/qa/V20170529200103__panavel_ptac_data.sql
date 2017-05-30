INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PPTAC', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('DOCUMENTS', 'Y', 'multivaluelist', 'Y', NULL, 1, 'core.documents', 'PPTAC', 'panavel', 0, 'now()', NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CHECKLIST', 'Y', 'multivaluelist', 'Y', NULL, 1, 'core.checklist', 'PPTAC', 'panavel', 0, 'now()', NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PTID', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.property.id', 'PPTAC', 'panavel', 0, 'now()', NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('APPLICATIONFEE', 'N', 'number', 'Y', NULL, 2, 'core.application40.fee', 'PPTAC', 'panavel', 0, 'now()', NULL, 0, NULL);

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PPTAC', 'DOCUMENTS', 'LPP', 'core.lastpaid.property', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PPTAC', 'CHECKLIST', 'HYP', 'core.halfyear.property', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PPTAC', 'CHECKLIST', 'OP', 'core.own.property', 'panavel', 0, now(), NULL, 0, NULL, 'Y');