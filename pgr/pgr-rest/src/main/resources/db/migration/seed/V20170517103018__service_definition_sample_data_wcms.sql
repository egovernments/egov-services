INSERT into egpgr_complainttype_category (id,name,description,version,tenantid) values (nextval('seq_egpgr_complainttype_category'),'Water connection','Water connection',0,'default');

INSERT INTO egpgr_complainttype (id, name, department, version, code, isactive, description, createddate, lastmodifieddate, createdby, lastmodifiedby, slahours, hasfinancialimpact, category, metadata, type, keywords,tenantid)
VALUES (nextval('SEQ_EGPGR_COMPLAINTTYPE'), 'Water connection', NULL, 0, 'WCMS', true, NULL, '2010-01-01 00:00:00', '2015-01-01 00:00:00', 1, 1, 24, false, (select id from egpgr_complainttype_category where name='Water connection'), true, 'realtime','','default');

INSERT INTO service_definition(code, tenantid, createddate, createdby) VALUES ('WCMS', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
    servicecode, tenantid, createddate, createdby) VALUES ('DOCUMENTS', 'Y', 'multivaluelist', 'Y', null, 1,
'Documents', 'WCMS', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('WCMS', 'DOCUMENTS', 'WPOC', 'core.propertyownership.certificate', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('CHECKLIST', 'Y', 'multivaluelist', 'Y', null, 1,
'Water Connection CheckLists', 'WCMS', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('WCMS', 'CHECKLIST', 'WNDP', 'core.nodue.property', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('WCMS', 'CHECKLIST', 'NDD', 'core.nodue.drainage', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('WCMSCT', 'Y', 'singlevaluelist', 'Y', null, 1,
'Connection Type', 'WCMS', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('WCMS', 'WCMSCT', 'CT', 'core.connection.type', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('WCMSC', 'Y', 'singlevaluelist', 'Y', null, 1,
'WCMS Category', 'WCMS', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('WCMS', 'WCMSC', 'CA', 'core.category', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('WCMSST', 'Y', 'singlevaluelist', 'Y', null, 1,
'WCMS Source Type', 'WCMS', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('WCMS', 'WCMSST', 'WST', 'core.watersource.type', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('WCMSPT', 'Y', 'singlevaluelist', 'Y', null, 1,
'WCMS Property Type', 'WCMS', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('WCMS', 'WCMSPT', 'WPT', 'core.property.type', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('WCMSUT', 'Y', 'singlevaluelist', 'Y', null , 1,
'WCMS Usage Type', 'WCMS', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('WCMS', 'WCMSUT', 'WUT', 'core.usage.type', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('WCMSHP', 'Y', 'singlevaluelist', 'Y', null, 1,
'WCMS HSC Pipe', 'WCMS', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('WCMS', 'WCMSHP', 'HSCP', 'core.hsc.pipe', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('WCMSSC', 'Y', 'singlevaluelist', 'Y', null, 1,
'WCMS Sump Capacity', 'WCMS', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('WCMS', 'WCMSSC', 'WSC', 'core.sump.capacity', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('WCMSNOP', 'Y', 'singlevaluelist', 'Y', null, 1,
'WCMS NO OF Persons', 'WCMS', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('WCMS', 'WCMSNOP', 'NOP', 'core.noof.persons', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('WCMSAF', 'N', 'number', 'Y', null, 1,
'100', 'WCMS', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('WCMS', 'WCMSAF', 'WAF', 'core.application.fee', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('WCMSO', 'Y', 'String', 'Y', null, 1,
'Water Connection Appplication Fee', 'WCMS', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('WCMS', 'WCMSO', 'WOPT', 'core.sanction.generation', 'default', now(), 0);