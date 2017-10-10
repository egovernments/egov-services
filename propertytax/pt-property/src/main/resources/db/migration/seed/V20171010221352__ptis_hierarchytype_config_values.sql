insert into egpt_mstr_configuration (id, tenantid, keyname, description, createdby, lastmodifiedby, createdtime, lastmodifiedtime) values (nextval('seq_egpt_mstr_configuration'), 'default', 'PT_RevenueBoundaryHierarchy', 'Configuration for boundary hierarchy type to be used', 1, 1, 1507593600000, 1507593600000);

insert into egpt_mstr_configurationvalues (id, tenantId, keyid, value, effectiveFrom, createdby, lastmodifiedby, createdtime, lastmodifiedtime) values (nextval('seq_egpt_mstr_configurationvalues'), 'default', (select id from egpt_mstr_configuration where keyname='PT_RevenueBoundaryHierarchy' and tenantid = 'default'), 'REVENUE_PTIS', now(), 1, 1, 1507593600000, 1507593600000);


