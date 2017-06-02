update value_definition set name='core.isbridegroom.citizen' where key='MCBGR' and servicecode='MC' and tenantid='default';

update value_definition set name='core.marriage.published' where key='MCMI' and servicecode='MC' and tenantid='default';

update value_definition set name='core.ptax.paid' where key='WNDP' and servicecode='WCMS' and tenantid='default';

delete from value_definition where key='NDD'and attributecode='CHECKLIST' and servicecode='WCMS' and tenantid='default';

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('WCMS', 'CHECKLIST', 'ST', 'core.sewarage.tax', 'default', 0, now(), NULL, 0, NULL, 'Y');

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('WCMS', 'CHECKLIST', 'LOND', 'core.licenses.ondate', 'default', 0, now(), NULL, 0, NULL, 'Y');

update value_definition set name='core.ptax.paid' where key='SNDP' and servicecode='WCMS' and tenantid='default';

update value_definition set name='core.water.tax' where key='WDD' and servicecode='WCMS' and tenantid='default';

INSERT INTO value_definition (servicecode, attributecode, key, name, tenantid, version, createddate, lastmodifieddate, createdby, lastmodifiedby, active)
VALUES ('SCMS', 'CHECKLIST', 'SOND', 'core.licenses.ondate', 'default', 0, now(), NULL, 0, NULL, 'Y');

update value_definition set name='core.ptax.paid' where key='SNDP' and servicecode='SCMS' and tenantid='default';

update value_definition set name='core.water.tax' where key='WDD' and servicecode='SCMS' and tenantid='default';

update value_definition set name='core.ptax.paid' where key='PTND' and servicecode='PTPH' and tenantid='default';
update value_definition set name='core.sewarage.tax' where key='STND' and servicecode='PTPH' and tenantid='default';
update value_definition set name='core.water.tax' where key='WTND' and servicecode='PTPH' and tenantid='default';
update value_definition set name='core.licenses.ondate' where key='LRND' and servicecode='PTPH' and tenantid='default';
update value_definition set name='core.tt.dept' where key='TT' and servicecode='PTPH' and tenantid='default';




