insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'UpdateLegacyLicense','/tl-services/license/v1/_update',
'TLTRANSACTIONS',null,(select id from service where code='TLTRANSACTIONS' and tenantid='default'),1,
'Update Legacy License',false,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_CREATOR',
 (select id from eg_action where name = 'UpdateLegacyLicense' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'UpdateLegacyLicense' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'UpdateLegacyLicense' ),'default');