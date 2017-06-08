INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PWCMS', 'panavel', 0, now(), NULL, 0, NULL);


INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CHECKLIST', 'Y', 'multivaluelist', 'Y', NULL, 1, 'core.checklist', 'PWCMS', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('DOCUMENTS', 'Y', 'multivaluelist', 'Y', NULL, 1, 'core.documents', 'PWCMS', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('WCMSCT', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.connection.type', 'PWCMS', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('WCMSC', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.wcms.category', 'PWCMS', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('WCMSST', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.wcms.sourcetype', 'PWCMS', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('WCMST', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.propertytype', 'PWCMS', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('WCMSUT', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.wcms.usagetype', 'PWCMS', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('WCMSHP', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.wcms.hsc', 'PWCMS', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('WCMSSC', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.wcms.sumpcapacity', 'PWCMS', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('WCMSNOP', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.no.of.persons', 'PWCMS', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('APPLICATIONFEE', 'N', 'number', 'Y', NULL, 2, 'core.application100.fee', 'PWCMS', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PROCESSINGFEE', 'Y', 'number', 'N', NULL, 3, 'core.processing.fee', 'PWCMS', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PWCMS', 'DOCUMENTS', 'WPOC', 'core.propertyownership.certificate', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PWCMS', 'CHECKLIST', 'WNDP', 'core.ptax.paid', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PWCMS', 'CHECKLIST', 'ST', 'core.sewarage.tax', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PWCMS', 'CHECKLIST', 'LOND', 'core.licenses.ondate', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PWCMS', 'WCMSCT', 'CT', 'core.connection.type', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PWCMS', 'WCMSC', 'CA', 'core.category', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PWCMS', 'WCMSST', 'WST', 'core.watersource.type', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PWCMS', 'WCMST', 'WPT', 'core.property.type', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PWCMS', 'WCMSUT', 'WUT', 'core.usage.type', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PWCMS', 'WCMSHP', 'HSCP', 'core.hsc.pipe', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PWCMS', 'WCMSSC', 'WSC', 'core.sump.capacity', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PWCMS', 'WCMSNOP', 'NOP', 'core.noof.persons', 'panavel', 0, now(), NULL, 0, NULL, 'Y');
