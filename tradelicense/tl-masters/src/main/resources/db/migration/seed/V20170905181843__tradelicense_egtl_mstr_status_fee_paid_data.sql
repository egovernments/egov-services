update egtl_mstr_status set code = 'FINALAPPROVALCOMPLETED' where code = 'APPROVALCOMPLETED' and moduletype = 'NEW LICENSE';


INSERT INTO egtl_mstr_status(id, tenantid, name, code, active, createdby, lastmodifiedby, createdtime, lastmodifiedtime, moduletype)VALUES (nextval('seq_egtl_mstr_status'), 'default', 'License Fee Paid', 'LICENSEFEEPAID', 'true', 1, 1, 1503386127924, 1503386127924,'NEW LICENSE');
