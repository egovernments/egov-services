-----enable script for wcms

update service set enabled=true where code='WCMS' and enabled=false and tenantId='default';


-----update service

update service set contextroot=null,ordernumber=1 where code='CATEGORYMASTERS' and contextroot='wcms' and tenantid='default';

update service set contextroot=null,ordernumber=2 where code='PIPESIZEMASTER' and contextroot='wcms' and tenantid='default';

update service set contextroot=null,ordernumber=3 where code='SOURCETYPE' and contextroot='wcms' and tenantid='default';

update service set contextroot=null,ordernumber=4 where code='SUPPLYTYPE' and contextroot='wcms' and tenantid='default';

update service set contextroot=null,ordernumber=5 where code='DOCUMENTTYPE' and contextroot='wcms' and tenantid='default';

-----Delete scripts for UI screens

----categoryType
delete from eg_roleaction where roleCode in ('SUPERUSER') and actionid = (select id from eg_action where name='CreateCategoryMaster');
delete from eg_action where name='CreateCategoryMaster' and url='/wc/createCategoryType'; 

delete from eg_roleaction where roleCode in ('SUPERUSER') and actionid = (select id from eg_action where name='ModifyCategoryMaster');
delete from eg_action where name='ModifyCategoryMaster' and url='/wc/categoryType/edit'; 

delete from eg_roleaction where roleCode in ('SUPERUSER') and actionid = (select id from eg_action where name='ViewCategoryMaster');
delete from eg_action where name='ViewCategoryMaster' and url='/wc/categoryType/view'; 

-----pipesize

delete from eg_roleaction where roleCode in ('SUPERUSER') and actionid = (select id from eg_action where name='CreatePipeSizeMaster');
delete from eg_action where name='CreatePipeSizeMaster' and url='/wc/createPipeSize'; 

delete from eg_roleaction where roleCode in ('SUPERUSER') and actionid = (select id from eg_action where name='ModifyPipeSizeMaster');
delete from eg_action where name='ModifyPipeSizeMaster' and url='/wc/pipeSize/edit'; 

delete from eg_roleaction where roleCode in ('SUPERUSER') and actionid = (select id from eg_action where name='ViewPipeSizeMaster');
delete from eg_action where name='ViewPipeSizeMaster' and url='/wc/pipeSize/view'; 

------sourcetype

delete from eg_roleaction where roleCode in ('SUPERUSER') and actionid = (select id from eg_action where name='CreateSourceTypeMaster');
delete from eg_action where name='CreateSourceTypeMaster' and url='/wc/createWaterSourceType'; 

delete from eg_roleaction where roleCode in ('SUPERUSER') and actionid = (select id from eg_action where name='ModifyWaterSourceTypeMaster');
delete from eg_action where name='ModifyWaterSourceTypeMaster' and url='/wc/waterSourceType/edit'; 

delete from eg_roleaction where roleCode in ('SUPERUSER') and actionid = (select id from eg_action where name='ViewWaterSourceTypeMaster');
delete from eg_action where name='ViewWaterSourceTypeMaster' and url='/wc/waterSourceType/view'; 

------supplytype

delete from eg_roleaction where roleCode in ('SUPERUSER') and actionid = (select id from eg_action where name='CreateSupplyTypeMaster');
delete from eg_action where name='CreateSupplyTypeMaster' and url='/wc/createSupplyType'; 

delete from eg_roleaction where roleCode in ('SUPERUSER') and actionid = (select id from eg_action where name='ModifyWaterSupplyTypeMaster');
delete from eg_action where name='ModifyWaterSupplyTypeMaster' and url='/wc/supplyType/edit'; 

delete from eg_roleaction where roleCode in ('SUPERUSER') and actionid = (select id from eg_action where name='ViewWaterSupplyTypeMaster');
delete from eg_action where name='ViewWaterSupplyTypeMaster' and url='/wc/supplyType/view'; 

------documenttype

delete from eg_roleaction where roleCode in ('SUPERUSER') and actionid = (select id from eg_action where name='CreateDocumentTypeMaster');
delete from eg_action where name='CreateDocumentTypeMaster' and url='/wc/createDocumentType'; 

delete from eg_roleaction where roleCode in ('SUPERUSER') and actionid = (select id from eg_action where name='ModifyDocumentTypeMaster');
delete from eg_action where name='ModifyDocumentTypeMaster' and url='/wc/documentType/edit'; 

delete from eg_roleaction where roleCode in ('SUPERUSER') and actionid = (select id from eg_action where name='ViewDocumentTypeMaster');
delete from eg_action where name='ViewDocumentTypeMaster' and url='/wc/documentType/view'; 


----------------------------------------------------------------------------------------------------------------------------------
----update scripts

--category type
update eg_action set enabled=true,url='/wcms/masters/categorytype/_create',ordernumber=1 where name='CreateCategoryMasterApi' and url='/categorytype/_create' ;

update eg_action set url='/wcms/masters/categorytype/{code}/_update',ordernumber=2 where name='ModifyCategoryMasterApi' and url='/categorytype/{code}/_update' ;

update eg_action set url='/wcms/masters/categorytype/_search',ordernumber=3,enabled=true where name='SearchCategoryMaster' and url='/categorytype/_search' ;

---pipesize
update eg_action set enabled=true,url='/wcms/masters/pipesize/_create',ordernumber=1 where name='CreatePipeSizeMasterApi' and url='/pipesize/_create'  ;

update eg_action set name='ModifyPipeSizeMasterApi' ,url='/wcms/masters/pipesize/{code}/_update',ordernumber=2 where name='PipeSize Modify Master' and url='/pipesize/{code}/_update' ;

update eg_action set url='/wcms/masters/pipesize/_search',ordernumber=3,enabled=true where name='SearchPipeSizeMaster' and url='/pipesize/_search' ;

--sourceType
update eg_action set enabled=true,url='/wcms/masters/sourcetype/_create',ordernumber=1 where name='CreateSourceTypeMasterApi' and url='/sourcetype/_create' ;

update eg_action set enabled=true,url='/wcms/masters/sourcetype/{code}/_update',ordernumber=2 where name='ModifySourceTypeMaster' and url='/sourcetype/{code}/_update' ;

update eg_action set enabled=true,url='/wcms/masters/sourcetype/_search',ordernumber=3 where name='SearchWaterSourceTypeMaster' and url='/sourcetype/_search' ;


---supplytype

update eg_action set enabled=true,url='/wcms/masters/supplytype/_create',ordernumber=1 where name='CreateSupplyTypeMasterApi' and url='/supplytype/_create'  ;

update eg_action set enabled=true,url='/wcms/masters/supplytype/{code}/_update',ordernumber=2 where name='ModifySupplyTypeMaster' and url='/supplytype/{code}/_update' ;

update eg_action set enabled=true,url='/wcms/masters/supplytype/_search',ordernumber=3 where name='SearchWaterSupplyTypeMaster' and url='/supplytype/_search' ;

----documenttype

update eg_action set enabled=true,url='/wcms/masters/documenttype/_create',ordernumber=1 where name='CreateDocumentTypeMasterApi' and url='/documenttype/_create' ;

update eg_action set enabled=true,url='/wcms/masters/documenttype/{code}/_update',ordernumber=2 where name='DocumentTypeModify' and url='/documenttype/{code}/_update' ;

update eg_action set enabled=true,url='/wcms/masters/documenttype/_search',ordernumber=3 where name='SearchDocumentTypeMaster' and url='/documenttype/_search' ;

------------------------------------------------------------------------------------------------------------------------------------

---service DocumentTypeApplicationType masters
INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES
 (nextval('SEQ_SERVICE'),'DOCUMENTAPPLICATION','DocumentApplication',true,null,'Document Application Master',6,(select id from service where code='WCMS MASTERS' and tenantid='default'),'default');

---action DocumentTypeApplicationType

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'CreatDocumentApplicationApi','/wcms/masters/documenttype-applicationtype/_create','DOCUMENTAPPLICATION',null,(select id from service where code='DOCUMENTAPPLICATION' and tenantid='default'),1,'Create Document Application',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'ModifyDocumentApplicationApi','/wcms/masters/documenttype-applicationtype/{docTypeAppliTypeId}/_update','DOCUMENTAPPLICATION',null,(select id from service where code='DOCUMENTAPPLICATION' and tenantid='default'),2,'Modify Document Application',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'SearchDocumentApplicationApi','/wcms/masters/documenttype-applicationtype/_search','DOCUMENTAPPLICATION',null,(select id from service where code='DOCUMENTAPPLICATION' and tenantid='default'),3,'Search Document Application',true,1,now(),1,now()); 

---role action mapping 
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'CreatDocumentApplicationApi'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ModifyDocumentApplicationApi'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchDocumentApplicationApi'),'default');


---service Donation masters
INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES
 (nextval('SEQ_SERVICE'),'DONATION','Donation',true,null,'Donation Master',7,(select id from service where code='WCMS MASTERS' and tenantid='default'),'default');

---action Donation

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'CreatDonationApi','/wcms/masters/donation/_create','DONATION',null,(select id from service where code='DONATION' and tenantid='default'),1,'Create Donation',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'ModifyDonationApi','/wcms/masters/donation/{donationId}/_update','DONATION',null,(select id from service where code='DONATION' and tenantid='default'),2,'Modify Donation',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'SearchDonationApi','/wcms/masters/donation/_search','DONATION',null,(select id from service where code='DONATION' and tenantid='default'),3,'Search Donation',true,1,now(),1,now()); 

---role action mapping 
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'CreatDonationApi'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ModifyDonationApi'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchDonationApi'),'default');


---service PropertyPipeSize masters
INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES
 (nextval('SEQ_SERVICE'),'PROPERTYPIPESIZE','PropertyPipeSize',true,null,'Property PipeSize Master',8,(select id from service where code='WCMS MASTERS' and tenantid='default'),'default');

---action PropertyPipeSize

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'CreatPropertyPipeSizeApi','/wcms/masters/propertytype-pipesize/_create','PROPERTYPIPESIZE',null,(select id from service where code='PROPERTYPIPESIZE' and tenantid='default'),1,'Create Property PipeSize',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'ModifyPropertyPipeSizeApi','/wcms/masters/propertytype-pipesize/{propertyPipeSizeId}/_update','PROPERTYPIPESIZE',null,(select id from service where code='PROPERTYPIPESIZE' and tenantid='default'),2,'Modify Property PipeSize',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'SearchPropertyPipeSizeApi','/wcms/masters/propertytype-pipesize/_search','PROPERTYPIPESIZE',null,(select id from service where code='PROPERTYPIPESIZE' and tenantid='default'),3,'Search Property PipeSize',true,1,now(),1,now()); 

---role action mapping 
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'CreatPropertyPipeSizeApi'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ModifyPropertyPipeSizeApi'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchPropertyPipeSizeApi'),'default');

---service PropertyCategory masters
INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES
 (nextval('SEQ_SERVICE'),'PROPERTYCATEGORY','PropertyCategory',true,null,'Property Category Master',9,(select id from service where code='WCMS MASTERS' and tenantid='default'),'default');

---action PropertyCategory

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'CreatPropertyCategoryApi','/wcms/masters/propertytype-categorytype/_create','PROPERTYCATEGORY',null,(select id from service where code='PROPERTYCATEGORY' and tenantid='default'),1,'Create Property Category',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'ModifyPropertyCategoryApi','/wcms/masters/propertytype-categorytype/{propertyCategoryId}/_update','PROPERTYCATEGORY',null,(select id from service where code='PROPERTYCATEGORY' and tenantid='default'),2,'Modify Property Category',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'SearchPropertyCategoryApi','/wcms/masters/propertytype-categorytype/_search','PROPERTYCATEGORY',null,(select id from service where code='PROPERTYCATEGORY' and tenantid='default'),3,'Search Property Category',true,1,now(),1,now()); 

---role action mapping 
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'CreatPropertyCategoryApi'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ModifyPropertyCategoryApi'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchPropertyCategoryApi'),'default');


---service PropertyUsage masters
INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES
 (nextval('SEQ_SERVICE'),'PROPERTYUSAGE','PropertyUsage',true,null,'Property Usage Master',10,(select id from service where code='WCMS MASTERS' and tenantid='default'),'default');

---action PropertyUsage

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'CreatPropertyUsageApi','/wcms/masters/propertytype-usagetype/_create','PROPERTYUSAGE',null,(select id from service where code='PROPERTYUSAGE' and tenantid='default'),1,'Create Property Usage',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'ModifyPropertyUsageApi','/wcms/masters/propertytype-usagetype/{propertyUsageId}/_update','PROPERTYUSAGE',null,(select id from service where code='PROPERTYUSAGE' and tenantid='default'),2,'Modify Property Usage',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'SearchPropertyUsageApi','/wcms/masters/propertytype-usagetype/_search','PROPERTYUSAGE',null,(select id from service where code='PROPERTYUSAGE' and tenantid='default'),3,'Search Property Usage',true,1,now(),1,now()); 

---role action mapping 
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'CreatPropertyUsageApi'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ModifyPropertyUsageApi'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchPropertyUsageApi'),'default');

---service StorageReservoir masters
INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES
 (nextval('SEQ_SERVICE'),'STORAGERESERVOIR','StorageReservoir',true,null,'Storage Reservoir Master',11,(select id from service where code='WCMS MASTERS' and tenantid='default'),'default');

---action StorageReservoir

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'CreatStorageReservoirApi','/wcms/masters/storagereservoir/_create','STORAGERESERVOIR',null,(select id from service where code='STORAGERESERVOIR' and tenantid='default'),1,'Create Storage Reservoir',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'ModifyStorageReservoirApi','/wcms/masters/storagereservoir/_update','STORAGERESERVOIR',null,(select id from service where code='STORAGERESERVOIR' and tenantid='default'),2,'Modify Storage Reservoir',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'SearchStorageReservoirApi','/wcms/masters/storagereservoir/_search','STORAGERESERVOIR',null,(select id from service where code='STORAGERESERVOIR' and tenantid='default'),3,'Search Storage Reservoir',true,1,now(),1,now()); 

---role action mapping 
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'CreatStorageReservoirApi'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ModifyStorageReservoirApi'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchStorageReservoirApi'),'default');


---service TreatmentPlant masters
INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES
 (nextval('SEQ_SERVICE'),'TREATMENTPLANT','TreatmentPlant',true,null,'Treatment Plant Master',12,(select id from service where code='WCMS MASTERS' and tenantid='default'),'default');

---action TreatmentPlant

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'CreatTreatmentPlantApi','/wcms/masters/treatmentplant/_create','TREATMENTPLANT',null,(select id from service where code='TREATMENTPLANT' and tenantid='default'),1,'Create Treatment Plant',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'ModifyTreatmentPlantApi','/wcms/masters/treatmentplant/_update','TREATMENTPLANT',null,(select id from service where code='TREATMENTPLANT' and tenantid='default'),2,'Modify Treatment Plant',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'SearchTreatmentPlantApi','/wcms/masters/treatmentplant/_search','TREATMENTPLANT',null,(select id from service where code='TREATMENTPLANT' and tenantid='default'),3,'Search Treatment Plant',true,1,now(),1,now()); 

---role action mapping 
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'CreatTreatmentPlantApi'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ModifyTreatmentPlantApi'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchTreatmentPlantApi'),'default');
