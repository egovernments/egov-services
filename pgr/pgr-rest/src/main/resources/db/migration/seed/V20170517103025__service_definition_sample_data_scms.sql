INSERT into egpgr_complainttype_category (id,name,description,version,tenantid) values (nextval('seq_egpgr_complainttype_category'),'Drainage Connection','Drainage Connection',0,'default');

INSERT INTO egpgr_complainttype (id, name, department, version, code, isactive, description, createddate, lastmodifieddate, createdby, lastmodifiedby, slahours, hasfinancialimpact, category, metadata, type, keywords,tenantid)
VALUES (nextval('SEQ_EGPGR_COMPLAINTTYPE'), 'Drainage Connection', NULL, 0, 'SCMS', true, NULL, '2010-01-01 00:00:00', '2015-01-01 00:00:00', 1, 1, 24, false, (select id from egpgr_complainttype_category where name='Drainage Connection'), true, 'realtime','','default');

INSERT INTO service_definition(code, tenantid, createddate, createdby) VALUES ('SCMS', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('DOCUMENTS', 'Y', 'multivaluelist', 'Y', null, 1,
'Drainage Connection documents attachment', 'SCMS', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('SCMS', 'DOCUMENTS', 'SPOC', 'core.propertyownership.certificate', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('CHECKLIST', 'Y', 'multivaluelist', 'Y', null, 1,
'Drainage Connection CheckLists', 'SCMS', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('SCMS', 'CHECKLIST', 'SNDP', 'core.nodue.property', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('SCMS', 'CHECKLIST', 'WDD', 'core.nodue.water', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('SCMSCR', 'Y', 'singlevaluelist', 'Y', null, 1,
'Closets Residential', 'SCMS', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('SCMS', 'SCMSCR', 'CR', 'core.closets.resedential', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('SCMSCNR', 'Y', 'singlevaluelist', 'Y', null, 1,
'Closets Non Residential', 'SCMS', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('SCMS', 'SCMSCNR', 'CNR', 'core.closets.nonresedential', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('SCMSPT', 'Y', 'singlevaluelist', 'Y', null, 1,
'Property Type', 'SCMS', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('SCMS', 'SCMSPT', 'SNDP', 'core.nodue.property', 'default', now(), 0);


INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('SCMSAF', 'N', 'number', 'Y', null, 1,
'100', 'SCMS', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('SCMS', 'SCMSAF', 'SAF', 'core.application.fee', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('SCMSO', 'Y', 'String', 'Y', null , 1,
'Output', 'SCMS', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('SCMS', 'SCMSO', 'SOPT', 'core.sanctionsewerage.generation', 'default', now(), 0);