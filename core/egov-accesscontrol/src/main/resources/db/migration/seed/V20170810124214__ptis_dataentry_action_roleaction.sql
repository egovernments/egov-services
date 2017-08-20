insert into eg_action(id,  name, url, servicecode, queryparams, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'), 'CreateDataEntryProperty', '/pt-property/properties/_create', 'NEW_PROPERTY', null, 1, 'Create Data Entry', true, 1, now(), 1, now());

insert into eg_roleaction(roleCode, actionid, tenantId) values ('ULB Operator', (select id from eg_action where name='CreateDataEntryProperty' and url='/pt-property/properties/_create' and displayname='Create Data Entry'), 'default');
insert into eg_roleaction(roleCode, actionid, tenantId) values ('SUPERUSER', (select id from eg_action where name='CreateDataEntryProperty' and url='/pt-property/properties/_create' and displayname='Create Data Entry'), 'default');
