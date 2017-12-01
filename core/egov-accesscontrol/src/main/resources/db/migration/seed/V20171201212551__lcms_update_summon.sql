
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Update Summon','/lcms-services/legalcase/summon/_update','CASE',null,(select id from service where code='CASE' and tenantid='default'),1,'Update Summon',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Update Summon' ),'default');
