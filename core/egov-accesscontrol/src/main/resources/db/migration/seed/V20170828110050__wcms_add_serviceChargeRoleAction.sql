insert into service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantId)
VALUES(nextval('SEQ_SERVICE'),'SERVICECHARGEMASTER','ServiceCharge Master',true,null,'ServiceCharge Master',23,
(select id from service where code='WCMS MASTERS' and tenantid='default'),'default');



insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'CreateServiceChargeMaster','/wcms/masters/serviceCharges/_create','SERVICECHARGEMASTER',null,1,'Create ServiceCharge',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'UpdateServiceChargeMaster','/wcms/masters/serviceCharges/_update','SERVICECHARGEMASTER',null,2,'Update ServiceCharge',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'SearchServiceChargeMaster','/wcms/masters/serviceCharges/_search','SERVICECHARGEMASTER',null,3,'View ServiceCharge',true,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='CreateServiceChargeMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='UpdateServiceChargeMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='SearchServiceChargeMaster'),'default');
