---service Meter Water Rates masters
INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES
 (nextval('SEQ_SERVICE'),'METERWATERRATES','MeterWaterRates',true,null,'Meter Water Rates',13,(select id from service where code='WCMS MASTERS' and tenantid='default'),'default');

---action Meter Water Rates

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'CreatMeterWaterRatesApi','/wcms/masters/meterwaterrates/_create','METERWATERRATES',null,(select id from service where code='METERWATERRATES' and tenantid='default'),1,'Create Meter Water Rates',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'ModifyMeterWaterRatesApi','/wcms/masters/meterwaterrates/_update','METERWATERRATES',null,(select id from service where code='METERWATERRATES' and tenantid='default'),2,'Modify Meter Water Rates',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'SearchMeterWaterRatesApi','/wcms/masters/meterwaterrates/_search','METERWATERRATES',null,(select id from service where code='METERWATERRATES' and tenantid='default'),3,'Search Meter Water Rates',true,1,now(),1,now()); 

---role action mapping 
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'CreatMeterWaterRatesApi'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ModifyMeterWaterRatesApi'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchMeterWaterRatesApi'),'default');

------application types

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'SearchApplicationTypeApi','/wcms/masters/master/_getapplicationtypes','DOCUMENTAPPLICATION',null,(select id from service where code='DOCUMENTAPPLICATION' and tenantid='default'),1,'Search Application Type',false,1,now(),1,now());

---reservoir types
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'SearchReservoirTypesApi','/wcms/masters/master/_getreservoirtypes','STORAGERESERVOIR',null,(select id from service where code='STORAGERESERVOIR' and tenantid='default'),2,'Search Resevoir Types',false,1,now(),1,now());

-----plant types
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'SearchPlantTypesApi','/wcms/masters/master/_getplanttypes','TREATMENTPLANT',null,(select id from service where code='TREATMENTPLANT' and tenantid='default'),3,'Search Plant Types',false,1,now(),1,now()); 

---role action mapping 
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchApplicationTypeApi'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchReservoirTypesApi'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchPlantTypesApi'),'default');


