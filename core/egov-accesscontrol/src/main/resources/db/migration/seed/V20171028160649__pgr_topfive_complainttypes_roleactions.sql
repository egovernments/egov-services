insert into eg_action(id,  name, url, servicecode, queryparams, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'), 'Top Five ComplaintTypes', '/pgr/dashboard/complainttype/topfive', 'DSHBRD', null, 1, 'Top Five ComplaintTypes', false, 1, now(), 1, now());

insert into eg_roleaction(roleCode, actionid, tenantId) 
values ('GA', (select id from eg_action where name='Top Five ComplaintTypes' and url='/pgr/dashboard/complainttype/topfive' and displayname='Top Five ComplaintTypes'), 'default');

