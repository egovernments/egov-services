insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'WORKS_MASTERS', 'Works Masters', false, 'Masters', 0, (select id from service where code = 'WMS' and tenantId='default'), 'default');

insert into eg_action(id, name, url, servicecode, queryparams, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'), 'Create SOR', '/works-masters/scheduleofrates/_create', 'WORKS_MASTERS', null, 0, 'Create SOR', false, 1, now(), 1, now());
insert into eg_action(id, name, url, servicecode, queryparams, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'), 'Update SOR', '/works-masters/scheduleofrates/_update', 'WORKS_MASTERS', null, 0, 'Update SOR', false, 1, now(), 1, now());
insert into eg_action(id, name, url, servicecode, queryparams, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'), 'Search SOR', '/works-masters/scheduleofrates/_search', 'WORKS_MASTERS', null, 0, 'Search SOR', false, 1, now(), 1, now());

insert into eg_roleaction(roleCode, actionid, tenantid) values ('SUPERUSER', (select id from eg_action where name = 'Create SOR' and url='/works-masters/scheduleofrates/_create'), 'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Update SOR' and url='/works-masters/scheduleofrates/_update' ), 'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Search SOR' and url='/works-masters/scheduleofrates/_search' ), 'default');
