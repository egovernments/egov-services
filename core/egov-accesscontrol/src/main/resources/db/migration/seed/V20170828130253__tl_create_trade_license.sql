insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'CreateNewLicense','/tl-services/license/v1/_create',
'TLTRANSACTIONS',null,(select id from service where code='TLTRANSACTIONS' and tenantid='default'),0,
'Create New License',true,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_CREATOR',
 (select id from eg_action where name = 'CreateNewLicense' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'CreateNewLicense' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'CreateNewLicense' ),'default');