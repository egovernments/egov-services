INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PMC', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('DOCUMENTS', 'Y', 'multivaluelist', 'Y', NULL, 1, 'core.documents', 'PMC', 'panavel', 0, 'now()', NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CHECKLIST', 'Y', 'multivaluelist', 'Y', NULL, 1, 'core.checklist', 'PMC', 'panavel', 0, 'now()', NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('MCBN', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.bride.name', 'PMC', 'panavel', 0, 'now()', NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('MCBAD', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.bride.address', 'PMC', 'panavel', 0, 'now()', NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('MCBAG', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.bride.age', 'PMC', 'panavel', 0, 'now()', NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('MCBR', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.bride.religion', 'PMC', 'panavel', 0, 'now()', NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('MCGN', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.groom.name', 'PMC', 'panavel', 0, 'now()', NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('MCGAD', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.groom.address', 'PMC', 'panavel', 0, 'now()', NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('MCGAG', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.groom.age', 'PMC', 'panavel', 0, 'now()', NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('MCGR', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.groom.religion', 'PMC', 'panavel', 0, 'now()', NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('MCWNO', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.witness1.name', 'PMC', 'panavel', 0, 'now()', NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('MCWAO', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.witness1.age', 'PMC', 'panavel', 0, 'now()', NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('MCWADO', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.witness1.address', 'PMC', 'panavel', 0, 'now()', NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('MCWNT', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.witness2.name', 'PMC', 'panavel', 0, 'now()', NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('MCWAT', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.witness2.age', 'PMC', 'panavel', 0, 'now()', NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('MCWADT', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.witness2.address', 'PMC', 'panavel', 0, 'now()', NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('APPLICATIONFEE', 'N', 'number', 'Y', NULL, 2, 'core.application40.fee', 'PMC', 'panavel', 0, 'now()', NULL, 0, NULL);

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PMC', 'DOCUMENTS', 'ADP', 'core.address.proof', 'panavel', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PMC', 'DOCUMENTS', 'AGP', 'core.age.proof', 'panavel', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PMC', 'DOCUMENTS', 'DRI', 'core.relegiousinstitution.document', 'panavel', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PMC', 'DOCUMENTS', 'LPH', 'core.latest.photograph', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PMC', 'CHECKLIST', 'BGIC', 'core.isbridegroom.citizen', 'panavel', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PMC', 'CHECKLIST', 'IMP', 'core.marriage.published', 'panavel', 0, now(), NULL, 0, NULL, 'Y');
INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PMC', 'CHECKLIST', 'OR', 'core.objection.raised', 'panavel', 0, now(), NULL, 0, NULL, 'Y');