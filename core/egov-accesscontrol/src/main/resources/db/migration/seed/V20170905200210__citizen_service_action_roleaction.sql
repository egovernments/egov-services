insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'PGResponseValidate','/citizen-services/v1/pgresponse/_validate',
'CITIZEN',null,(select id from service where code='CITIZEN' and tenantid='default'),0,
'PG Response Validate',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('CITIZEN',
 (select id from eg_action where name = 'PGResponseValidate' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'PGResponseValidate' ),'default');
