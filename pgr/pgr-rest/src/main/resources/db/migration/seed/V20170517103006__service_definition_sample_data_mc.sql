INSERT into egpgr_complainttype_category (id,name,description,version,tenantid) values (nextval('seq_egpgr_complainttype_category'),'Marriage certificate','Marriage certificate',0,'default');

INSERT INTO egpgr_complainttype (id, name, department, version, code, isactive, description, createddate, lastmodifieddate, createdby, lastmodifiedby, slahours, hasfinancialimpact, category, metadata, type, keywords,tenantid)
VALUES (nextval('SEQ_EGPGR_COMPLAINTTYPE'), 'Marriage certificate', NULL, 0, 'MC', true, NULL, '2010-01-01 00:00:00', '2015-01-01 00:00:00', 1, 1, 24, false, (select id from egpgr_complainttype_category where name='Marriage certificate'), true, 'realtime','','default');

INSERT INTO service_definition(code, tenantid, createddate, createdby) VALUES ('MC', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('DOCUMENTS', 'Y', 'multivaluelist', 'Y', null, 1,
'Marraige Certificate Documents', 'MC', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('MC', 'DOCUMENTS', 'MCA', 'core.address.proof', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('MC', 'DOCUMENTS', 'MCAGE', 'core.age.proof', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('MC', 'DOCUMENTS', 'MCRI', 'core.relegiousinstitution.document', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('MC', 'DOCUMENTS', 'MCP', 'core.latest.photograph', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('CHECKLIST', 'Y', 'multivaluelist', 'Y', null , 1,
'Marriage Certificate Checklist', 'MC', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('MC', 'CHECKLIST', 'MCBGR', 'core.bridegroom.religion', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('MC', 'CHECKLIST', 'MCOR', 'core.objection.raised', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('MC', 'CHECKLIST', 'MCMI', 'core.marriage.intent', 'default', now(), 0);


INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('MCAF', 'N', 'number', 'Y', null , 1,
'40', 'MC', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('MC', 'MCAF', 'MCAF', 'core.marriage.applicationfee', 'default', now(), 0);

