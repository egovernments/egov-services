insert into eg_action(id,  name, url, servicecode, queryparams, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'), 'PT Report', '/report/property/_get', 'PTIS_REPORTS', null, 1, 'PT Report', false, 1, now(), 1, now());

insert into eg_roleaction(roleCode, actionid, tenantId) select 'SUPERUSER', id, 'default' from eg_action where name='PT Report' and url = '/report/property/_get';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'ULB Operator', id, 'default' from eg_action where name='PT Report' and url = '/report/property/_get';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'Property Verifier', id, 'default' from eg_action where name='PT Report'  and url = '/report/property/_get';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'Property Approver', id, 'default' from eg_action where name='PT Report'  and url = '/report/property/_get';



insert into eg_action(id,  name, url, servicecode, queryparams, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'), 'PT Report Metadata', '/report/property/metadata/_get', 'PTIS_REPORTS', null, 2, 'PT Report Metadata', false, 1, now(), 1, now());

insert into eg_roleaction(roleCode, actionid, tenantId) select 'SUPERUSER', id, 'default' from eg_action where name='PT Report Metadata' and url = '/report/property/metadata/_get';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'ULB Operator', id, 'default' from eg_action where name='PT Report Metadata' and url = '/report/property/metadata/_get';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'Property Verifier', id, 'default' from eg_action where name='PT Report Metadata'  and url = '/report/property/metadata/_get';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'Property Approver', id, 'default' from eg_action where name='PT Report Metadata'  and url = '/report/property/metadata/_get';




