insert into eg_action(id,  name, url, servicecode, queryparams, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'), 'ComplaintTypes Ageing', '/pgr/dashboard/ageing', 'DSHBRD', null, 1, 'ComplaintTypes Ageing', false, 1, now(), 1, now());

insert into eg_roleaction(roleCode, actionid, tenantId) 
values ('GA', (select id from eg_action where name='ComplaintTypes Ageing' and url='/pgr/dashboard/ageing' and displayname='ComplaintTypes Ageing'), 'default');

