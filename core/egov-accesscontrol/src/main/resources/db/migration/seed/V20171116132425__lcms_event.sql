insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'CASEEVENTS', 'Case Events', false, 'Case Events', 1, (select id from service where code = 'CASE' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Search Event','/lcms-services/legalcase/event/_get','CASEEVENTS',null,(select id from service where code='CASEEVENTS' and tenantid='default'),1,'Search Event',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Search Event' ),'default');