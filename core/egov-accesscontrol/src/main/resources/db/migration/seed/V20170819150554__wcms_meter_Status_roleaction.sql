insert into service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantId)
VALUES(nextval('SEQ_SERVICE'),'METERSTATUSMASTER','Meter Status Master',true,null,'Meter Status Master',15,
(select id from service where code='WCMS MASTERS' and tenantid='default'),'default');



insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'CreateMeterStatusMaster','/wcms/masters/meterStatus/_create','METERSTATUSMASTER',null,1,'Create Meter Status',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'UpdateMeterStatusMaster','/wcms/masters/meterStatus/_update','METERSTATUSMASTER',null,2,'Update Meter Status',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'SearchMeterStatusMaster','/wcms/masters/meterStatus/_search','METERSTATUSMASTER',null,3,'View Meter Status',true,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='CreateMeterStatusMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='UpdateMeterStatusMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='SearchMeterStatusMaster'),'default');
