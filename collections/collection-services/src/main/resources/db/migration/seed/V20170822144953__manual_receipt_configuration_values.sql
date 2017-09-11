INSERT INTO egcl_configuration(id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId) VALUES (nextval('seq_egcl_configuration'),'MANUAL_RECEIPT_DETAILS_REQUIRED_OR_NOT',
'Manual receipt details for legacy data is required or not',1,now(),1,now(),'default');

INSERT INTO egcl_configurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId) VALUES (nextval('seq_egcl_configurationvalues'),
(SELECT id from egcl_configuration WHERE keyname = 'MANUAL_RECEIPT_DETAILS_REQUIRED_OR_NOT'),'Yes',now(),1,now(),1,now(),'default');

INSERT INTO egcl_configuration(id, keyname, description, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId) VALUES (nextval('seq_egcl_configuration'),'CUTOFF_DATE_FOR_MANUAL_RECEIPT_DETAILS',
'CutOff Date for Manual receipt details for legacy data',1,now(),1,now(),'default');

INSERT INTO egcl_configurationvalues(id, keyid, value, effectivefrom, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantId) VALUES (nextval('seq_egcl_configurationvalues'),
(SELECT id from egcl_configuration WHERE keyname = 'CUTOFF_DATE_FOR_MANUAL_RECEIPT_DETAILS'),'01-04-2017',now(),1,now(),1,now(),'default');
