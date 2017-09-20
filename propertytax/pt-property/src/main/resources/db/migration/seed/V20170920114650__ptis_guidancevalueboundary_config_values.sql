insert into egpt_mstr_configuration (id, tenantid, keyname, description, createdby, lastmodifiedby, createdtime, lastmodifiedtime) values (nextval('seq_egpt_mstr_configuration'), 'default', 'GuidanceBoundary', 'Guidance Boundary Configuration', 1, 1, 1504796400000, 1504796400000);

insert into egpt_mstr_configurationvalues (id, tenantId, keyid, value, effectiveFrom, createdby, lastmodifiedby, createdtime, lastmodifiedtime) values (nextval('seq_egpt_mstr_configurationvalues'), 'default', (select id from egpt_mstr_configuration where keyname='GuidanceBoundary'), 'Zone', now(), 1, 1, 1504796400000, 1504796400000);

