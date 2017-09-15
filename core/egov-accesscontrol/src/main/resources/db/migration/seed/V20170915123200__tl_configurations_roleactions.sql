insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'SearchTLConfigurations','/tl-services/configurations/v1/_search','TLTRANSACTIONS',null,(select id from service where code='TLTRANSACTIONS' and tenantid='default'),0,'Search TL Configurations',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='SearchTLConfigurations'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_ADMIN',(select id from eg_action where name='SearchTLConfigurations'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_CREATOR',(select id from eg_action where name='SearchTLConfigurations'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_APPROVER',(select id from eg_action where name='SearchTLConfigurations'),'default');
