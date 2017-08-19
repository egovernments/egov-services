---service Non Meter Water Rates masters
INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES
 (nextval('SEQ_SERVICE'),'NONMETERWATERRATES','NonMeterWaterRates',true,null,'Non-Meter Water Rates',14,(select id from service where code='WCMS MASTERS' and tenantid='default'),'default');

---action Non Meter Water Rates

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'CreatNonMeterWaterRatesApi','/wcms/masters/nonmeterwaterrates/_create','NONMETERWATERRATES',null,(select id from service where code='NONMETERWATERRATES' and tenantid='default'),1,'Create Non-Meter Water Rates',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'ModifyNonMeterWaterRatesApi','/wcms/masters/nonmeterwaterrates/_update','NONMETERWATERRATES',null,(select id from service where code='NONMETERWATERRATES' and tenantid='default'),2,'Modify Non-Meter Water Rates',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'SearchNonMeterWaterRatesApi','/wcms/masters/nonmeterwaterrates/_search','NONMETERWATERRATES',null,(select id from service where code='NONMETERWATERRATES' and tenantid='default'),3,'Search Non-Meter Water Rates',true,1,now(),1,now()); 

---role action mapping 
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'CreatNonMeterWaterRatesApi'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ModifyNonMeterWaterRatesApi'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchNonMeterWaterRatesApi'),'default');