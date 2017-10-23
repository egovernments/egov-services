insert into eg_action(id,  name, url, servicecode, queryparams, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'), 'Top ComplaintTypes', '/pgr/dashboard/complainttype', 'DSHBRD', null, 1, 'Top ComplaintTypes', false, 1, now(), 1, now());

insert into eg_roleaction(roleCode, actionid, tenantId) 
values ('GA', (select id from eg_action where name='Top ComplaintTypes' and url='/pgr/dashboard/complainttype' and displayname='Top ComplaintTypes'), 'default');
