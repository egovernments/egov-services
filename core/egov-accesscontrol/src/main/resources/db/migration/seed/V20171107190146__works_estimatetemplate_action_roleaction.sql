update eg_action set enabled=true where name = 'Create SOR' and url='/works-masters/scheduleofrates/_create';

insert into eg_action(id, name, url, servicecode, queryparams, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'), 'Create Estimate Template', '/works-masters/estimatetemplates/_create', 'WORKS_MASTERS', null, 0, 'Create Estimate Template', true, 1, now(), 1, now());
insert into eg_action(id, name, url, servicecode, queryparams, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'), 'Update Estimate Template', '/works-masters/estimatetemplates/_update', 'WORKS_MASTERS', null, 0, 'Update Estimate Template', false, 1, now(), 1, now());
insert into eg_action(id, name, url, servicecode, queryparams, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'), 'Search Estimate Template', '/works-masters/estimatetemplates/_search', 'WORKS_MASTERS', null, 0, 'Search Estimate Template', false, 1, now(), 1, now());

insert into eg_roleaction(roleCode, actionid, tenantid) values ('SUPERUSER', (select id from eg_action where name = 'Create Estimate Template' and url='/works-masters/estimatetemplates/_create'), 'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Update Estimate Template' and url='/works-masters/estimatetemplates/_update' ), 'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Search Estimate Template' and url='/works-masters/estimatetemplates/_search' ), 'default');
