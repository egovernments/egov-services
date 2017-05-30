INSERT INTO service_definition (code, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PPTPH', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PPTPHAF', 'N', 'number', 'Y', NULL, 1, '100', 'PPTPH', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('DOCUMENTS', 'Y', 'multivaluelist', 'Y', NULL, 1, 'core.documents', 'PPTPH', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('CHECKLIST', 'Y', 'multivaluelist', 'Y', NULL, 1, 'core.checklist', 'PPTPH', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('APPLICATIONFEE', 'N', 'number', 'Y', NULL, 2, 'core.application100.fee', 'PPTPH', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PROCESSINGFEE', 'Y', 'number', 'N', NULL, 3, 'core.processing.fee', 'PPTPH', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('RFT', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.reason.transfer', 'PPTPH', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('RDN', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.registration.documentno', 'PPTPH', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('RDD', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.registration.documentdate', 'PPTPH', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('PSV', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.parties.value', 'PPTPH', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('DGV', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.departmentguildelines.values', 'PPTPH', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO attribute_definition (code, variable, datatype, required, datatypedescription, ordernum, description, servicecode, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby)
VALUES ('SDED', 'Y', 'singlevaluelist', 'Y', NULL, 1, 'core.saledeed.date', 'PPTPH', 'panavel', 0, now(), NULL, 0, NULL);

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PPTPH', 'DOCUMENTS', 'PD', 'core.patte.document', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PPTPH', 'DOCUMENTS', 'PW', 'core.property.will', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PPTPH', 'DOCUMENTS', 'PDEED', 'core.property.deed', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PPTPH', 'DOCUMENTS', 'PSB', 'core.affidavit.sellerorbuyer', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PPTPH', 'DOCUMENTS', 'POWN', 'core.photograph.owner', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PPTPH', 'DOCUMENTS', 'PDP', 'core.address.proof', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PPTPH', 'CHECKLIST', 'PTND', 'core.ptax.paid', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PPTPH', 'CHECKLIST', 'WTND', 'core.sewarage.tax', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PPTPH', 'CHECKLIST', 'STND', 'core.water.tax', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PPTPH', 'CHECKLIST', 'LRND', 'core.licenses.ondate', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PPTPH', 'CHECKLIST', 'TT', 'core.tt.dept', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PPTPH', 'RFT', 'TR', 'core.transfer.reason', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PPTPH', 'RDN', 'RDNO', 'core.registrationdocument.number', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PPTPH', 'RDD', 'REDD', 'core.registrationdocument.date', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PPTPH', 'PSV', 'PVA', 'core.parties.value', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PPTPH', 'DGV', 'DGVA', 'core.departmentguildelines.values', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PPTPH', 'SDED', 'SDD', 'core.saledeed.date', 'panavel', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('PPTPH', 'PPTPHAF', 'PTPAF', 'core.application.fee', 'panavel', 0, now(), NULL, 0, NULL, 'Y');
