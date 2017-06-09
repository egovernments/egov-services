INSERT into egpgr_complainttype_category (id,name,description,version,tenantid) values (nextval('seq_egpgr_complainttype_category'),'Property Tax','Property Tax',0,'default');

INSERT INTO egpgr_complainttype (id, name, department, version, code, isactive, description, createddate, lastmodifieddate, createdby, lastmodifiedby, slahours, hasfinancialimpact, category, metadata, type, keywords,tenantid)
VALUES (nextval('SEQ_EGPGR_COMPLAINTTYPE'), 'Assessment certificate', NULL, 0, 'PTAC', true, NULL, '2010-01-01 00:00:00', '2015-01-01 00:00:00', 1, 1, 24, false, (select id from egpgr_complainttype_category where name='Property Tax'), true, 'realtime','','default');

INSERT INTO service_definition(code, tenantid, createddate, createdby) VALUES ('PTAC', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('PID', 'Y', 'String', 'Y', null, 1,
'Property ID', 'PTAC', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('PTAC', 'PID', 'PIDV', 'core.property.id', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('PTACO', 'Y', 'String', 'Y', null, 1,
'Output', 'PTAC', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('PTAC', 'PTACO', 'PTP', 'core.propertytax.portal', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('PTACAF', 'N', 'number', 'Y', null, 1,
'40', 'PTAC', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('PTAC', 'PTACAF', 'ACAF', 'core.application.fee', 'default', now(), 0);

INSERT INTO egpgr_complainttype (id, name, department, version, code, isactive, description, createddate, lastmodifieddate, createdby, lastmodifiedby, slahours, hasfinancialimpact, category, metadata, type, keywords,tenantid)
VALUES (nextval('SEQ_EGPGR_COMPLAINTTYPE'), 'No Due certificate', NULL, 0, 'PTNDC', true, NULL, '2010-01-01 00:00:00', '2015-01-01 00:00:00', 1, 1, 24, false, (select id from egpgr_complainttype_category where name='Property Tax'), true, 'realtime','','default');

INSERT INTO service_definition(code, tenantid, createddate, createdby) VALUES ('PTNDC', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('PIDP', 'Y', 'String', 'Y', null, 1,
'Property ID Should provide payment option for them to pay', 'PTNDC', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('PTNDC', 'PIDP', 'PIDPV', 'core.propertyid.payment', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('PTNDO', 'Y', 'String', 'Y', null, 1,
'Output', 'PTNDC', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('PTNDC', 'PTNDO', 'PND', 'core.generateproperty.nodue', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('PTNDAF', 'N', 'number', 'Y', null, 1,
'40', 'PTNDC', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('PTNDC', 'PTNDAF', 'NDAF', 'core.application.fee', 'default', now(), 0);


INSERT INTO egpgr_complainttype (id, name, department, version, code, isactive, description, createddate, lastmodifieddate, createdby, lastmodifiedby, slahours, hasfinancialimpact, category, metadata, type, keywords,tenantid)
VALUES (nextval('SEQ_EGPGR_COMPLAINTTYPE'), 'Transfer of property Heredity', NULL, 0, 'PTPH', true, NULL, '2010-01-01 00:00:00', '2015-01-01 00:00:00', 1, 1, 24, false, (select id from egpgr_complainttype_category where name='Property Tax'), true, 'realtime','','default');

INSERT INTO service_definition(code, tenantid, createddate, createdby) VALUES ('PTPH', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('DOCUMENTS', 'Y', 'multivaluelist', 'Y', null, 1,
'Transfer of property Heredity documents attachment', 'PTPH', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('PTPH', 'DOCUMENTS', 'PD', 'core.patte.document', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('PTPH', 'DOCUMENTS', 'PW', 'core.property.will', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('PTPH', 'DOCUMENTS', 'PDEED', 'core.property.deed', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('PTPH', 'DOCUMENTS', 'PSB', 'core.affidavit.sellerorbuyer', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('PTPH', 'DOCUMENTS', 'POWN', 'core.photograph.owner', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('PTPH', 'DOCUMENTS', 'PDP', 'core.address.proof', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('CHECKLIST', 'Y', 'multivaluelist', 'Y', null , 1,
'Transfer of property Heredity Checklist', 'PTPH', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('PTPH', 'CHECKLIST', 'PTND', 'core.property.nodue', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('PTPH', 'CHECKLIST', 'WTND', 'core.water.nodue', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('PTPH', 'CHECKLIST', 'STND', 'core.sewarage.nodue', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('PTPH', 'CHECKLIST', 'LRND', 'core.lincence.renewed', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('PTPH', 'CHECKLIST', 'TT', 'core.title.transfer', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('RFT', 'Y', 'singlevaluelist', 'Y', null, 1,
'Reason for transfer', 'PTPH', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('PTPH', 'RFT', 'TR', 'core.transfer.reason', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('RDN', 'Y', 'singlevaluelist', 'Y', null, 1,
'Registration document number', 'PTPH', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('PTPH', 'RDN', 'RDNO', 'core.registrationdocument.number', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('RDD', 'Y', 'singlevaluelist', 'Y', null, 1,
'Registration document date', 'PTPH', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('PTPH', 'RDD', 'REDD', 'core.registrationdocument.date', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('PSV', 'Y', 'singlevaluelist', 'Y', null, 1,
'Parties consideration value', 'PTPH', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('PTPH', 'PSV', 'PVA', 'core.parties.value', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('DGV', 'Y', 'singlevaluelist', 'Y', null, 1,
'Department guidelines value', 'PTPH', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('PTPH', 'DGV', 'DGVA', 'core.departmentguildelines.values', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('SDED', 'Y', 'singlevaluelist', 'Y', null, 1,
'Sale deed executed Date', 'PTPH', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('PTPH', 'SDED', 'SDD', 'core.saledeed.date', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('PTPHAF', 'N', 'number', 'Y', null, 1,
'100', 'PTPH', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('PTPH', 'PTPHAF', 'PTPAF', 'core.application.fee', 'default', now(), 0);

INSERT INTO attribute_definition(code, variable, datatype, required, datatypedescription, ordernum, description,
servicecode, tenantid, createddate, createdby) VALUES ('PTPHO', 'Y', 'String', 'Y', null, 1,
'Output', 'PTPH', 'default', now(), 0);

INSERT INTO value_definition(servicecode, attributecode, key, name, tenantid, createddate, createdby) VALUES
('PTPH', 'PTPHO', 'PTPO', 'core.commissioner.approved', 'default', now(), 0);


