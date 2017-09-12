insert into service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantId)
VALUES(nextval('SEQ_SERVICE'),'WATERCHARGESCONFIGURATIONMASTER','Configuration Master',true,null,'Configuration Master',24,
(select id from service where code='WCMS MASTERS' and tenantid='default'),'default');

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'SearchWaterChargeConfigurationMaster','/wcms/masters/waterchargesconfig/_search','WATERCHARGESCONFIGURATIONMASTER',null,1,'Search Configuration',true,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='SearchWaterChargeConfigurationMaster'),'default');