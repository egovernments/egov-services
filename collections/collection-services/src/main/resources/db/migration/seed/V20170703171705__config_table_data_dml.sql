INSERT INTO egcl_configuration(id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId) 
VALUES (nextval('seq_egcl_configuration'), 'RECEIPT_PREAPPROVED_OR_APPROVED', 
'PREAPPROVED or APPROVED will be the value, if value is PREAPPROVED then workflow is initiated or else not', 61, current_timestamp, 61, current_timestamp, 'default');

INSERT INTO egcl_configuration(id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId) 
VALUES (nextval('seq_egcl_configuration'), 'WORKFLOW_MANUAL_OR_AUTO', 
'MANUAL or AUTO will be the value, user decides workflow if MANUAL or else auto configured workflow is intiated', 61, current_timestamp, 61, current_timestamp, 'default');

INSERT INTO egcl_configurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId) 
VALUES (nextval('seq_egcl_configurationvalues'), (SELECT id from egcl_configuration WHERE keyname = 'RECEIPT_PREAPPROVED_OR_APPROVED'), 
'PREAPPROVED', current_timestamp, 61, current_timestamp, 61, current_timestamp, 'default');

INSERT INTO egcl_configurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId) 
VALUES (nextval('seq_egcl_configurationvalues'), (SELECT id from egcl_configuration WHERE keyname = 'RECEIPT_PREAPPROVED_OR_APPROVED'), 
'APPROVED', current_timestamp, 61, current_timestamp, 61, current_timestamp, 'default');

INSERT INTO egcl_configurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId) 
VALUES (nextval('seq_egcl_configurationvalues'), (SELECT id from egcl_configuration WHERE keyname = 'WORKFLOW_MANUAL_OR_AUTO'), 
'MANUAL', current_timestamp, 61, current_timestamp, 61, current_timestamp, 'default');

INSERT INTO egcl_configurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId) 
VALUES (nextval('seq_egcl_configurationvalues'), (SELECT id from egcl_configuration WHERE keyname = 'WORKFLOW_MANUAL_OR_AUTO'), 
'AUTO', current_timestamp, 61, current_timestamp, 61, current_timestamp, 'default');
