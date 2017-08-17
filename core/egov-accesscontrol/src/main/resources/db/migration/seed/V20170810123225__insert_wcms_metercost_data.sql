insert into service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantId)
VALUES(nextval('SEQ_SERVICE'),'METERCOSTMASTER','Meter Cost Master',true,null,'Meter Cost Master',14,
(select id from service where code='WCMS MASTERS' and tenantid='default'),'default');



insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'CreateMeterCostMaster','/wcms/masters/metercosts/_create','METERCOSTMASTER',null,1,'Create Meter Cost',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'UpdateMeterCostMaster','/wcms/masters/metercosts/_update','METERCOSTMASTER',null,2,'Update Meter Cost',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'SearchMeterCostMaster','/wcms/masters/metercosts/_search','METERCOSTMASTER',null,3,'View Meter Cost',true,1,now(),1,now());