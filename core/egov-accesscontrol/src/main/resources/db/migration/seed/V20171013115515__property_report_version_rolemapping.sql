insert into eg_action(id,  name, url, servicecode, queryparams, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'), 'PT V1 Report', '/report/property/v1/_get', 'PTIS_REPORTS', null, 1, 'PT Report', false, 1, now(), 1, now());

insert into eg_roleaction(roleCode, actionid, tenantId) select 'SUPERUSER', id, 'default' from eg_action where name='PT V1 Report' and url = '/report/property/v1/_get';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'ULB Operator', id, 'default' from eg_action where name='PT V1 Report' and url = '/report/property/v1/_get';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'Property Verifier', id, 'default' from eg_action where name='PT V1 Report'  and url = '/report/property/v1/_get';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'Property Approver', id, 'default' from eg_action where name='PT V1 Report'  and url = '/report/property/v1/_get';

insert into eg_action(id,  name, url, servicecode, queryparams, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) values(nextval('SEQ_EG_ACTION'), 'PT Report V1 Metadata', '/report/property/v1/metadata/_get', 'PTIS_REPORTS', null, 2, 'PT Report Metadata', false, 1, now(), 1, now());

insert into eg_roleaction(roleCode, actionid, tenantId) select 'SUPERUSER', id, 'default' from eg_action where name='PT Report V1 Metadata' and url = '/report/property/v1/metadata/_get';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'ULB Operator', id, 'default' from eg_action where name='PT Report V1 Metadata' and url = '/report/property/v1/metadata/_get';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'Property Verifier', id, 'default' from eg_action where name='PT Report V1 Metadata'  and url = '/report/property/v1/metadata/_get';
insert into eg_roleaction(roleCode, actionid, tenantId) select 'Property Approver', id, 'default' from eg_action where name='PT Report V1 Metadata'  and url = '/report/property/v1/metadata/_get';
