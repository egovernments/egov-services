-----Delete script

delete from service where code='USAGETYPEMASTER';
delete from eg_action where servicecode='USAGETYPEMASTER' ;

delete from eg_action where servicecode='PIPESIZEMASTER'; 

delete from eg_action where servicecode='CATEGORYMASTER';


---action category

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'CreateCategoryMaster','/wc/createCategoryType','CATEGORYMASTERS',null,(select id from service where code='CATEGORYMASTERS' and contextroot='wcms' and tenantid='default'),1,'Create Category Type',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Creat Category Master','/categorytype/_create','CATEGORYMASTERS',null,(select id from service where code='CATEGORYMASTERS' and contextroot='wcms' and tenantid='default'),2,'Create Category Type',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'ModifyCategoryMaster','/wc/categoryType/edit','CATEGORYMASTERS',null,(select id from service where code='CATEGORYMASTERS' and contextroot='wcms' and tenantid='default'),3,'Modify Category Type',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'Category Modify Master','/categorytype/{code}/_update','CATEGORYMASTERS',null,(select id from service where code='CATEGORYMASTERS' and contextroot='wcms' and tenantid='default'),4,'Modify Category Type',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'ViewCategoryMaster','/wc/categoryType/view','CATEGORYMASTERS',null,(select id from service where code='CATEGORYMASTERS' and contextroot='wcms' and tenantid='default'),5,'View Category Type',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'SearchCategoryMaster','/categorytype/_search','CATEGORYMASTERS',null,(select id from service where code='CATEGORYMASTERS' and contextroot='wcms' and tenantid='default'),6,'Search Category Type',false,1,now(),1,now());

---role action mapping
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name ='CreateCategoryMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Creat Category Master'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ModifyCategoryMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Category Modify Master'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ViewCategoryMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchCategoryMaster'),'default');

---action pipesize

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'CreatePipeSizeMaster','/wc/createPipeSize','PIPESIZEMASTER',null,(select id from service where code='PIPESIZEMASTER' and contextroot='wcms' and tenantid='default'),1,'Create Pipe Size',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Creat PipeSize Master','/pipesize/_create','PIPESIZEMASTER',null,(select id from service where code='PIPESIZEMASTER' and contextroot='wcms' and tenantid='default'),2,'Create Pipe Size',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'ModifyPipeSizeMaster','/wc/pipeSize/edit','PIPESIZEMASTER',null,(select id from service where code='PIPESIZEMASTER' and contextroot='wcms' and tenantid='default'),3,'Modify Pipe Size',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'PipeSize Modify Master','/pipesize/{code}/_update','PIPESIZEMASTER',null,(select id from service where code='PIPESIZEMASTER' and contextroot='wcms' and tenantid='default'),4,'PipeSizeModify',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'ViewPipeSizeMaster','/wc/pipeSize/view','PIPESIZEMASTER',null,(select id from service where code='PIPESIZEMASTER' and contextroot='wcms' and tenantid='default'),5,'View Pipe Size',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'SearchPipeSizeMaster','/pipesize/_search','PIPESIZEMASTER',null,(select id from service where code='PIPESIZEMASTER' and contextroot='wcms' and tenantid='default'),6,'Search Pipe Size',false,1,now(),1,now());

---role action mapping
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name ='CreatePipeSizeMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Creat PipeSize Master'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ModifyPipeSizeMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'PipeSize Modify Master'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ViewPipeSizeMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchPipeSizeMaster'),'default');

---service sourcetype masters
INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES
 (nextval('SEQ_SERVICE'),'SOURCETYPE','Source Type Master',true,'wcms','Water Source Type Master',1,(select id from service where code='WCMS MASTERS' and tenantid='default'),'default');

---action sourcetype

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'CreateSourceTypeMaster','/wc/createWaterSourceType','SOURCETYPE',null,(select id from service where code='SOURCETYPE' and contextroot='wcms' and tenantid='default'),1,'Create Water Source Type',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'Creat SourceType Master','/sourcetype/_create','SOURCETYPE',null,(select id from service where code='SOURCETYPE' and contextroot='wcms' and tenantid='default'),2,'Create Water Source Type',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'ModifyWaterSourceTypeMaster','/wc/waterSourceType/edit','SOURCETYPE',null,(select id from service where code='SOURCETYPE' and contextroot='wcms' and tenantid='default'),3,'Modify Water Source Type',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'ModifySourceTypeMaster','/sourcetype/{code}/_update','SOURCETYPE',null,(select id from service where code='SOURCETYPE' and contextroot='wcms' and tenantid='default'),4,'Modify Water Source Type',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'ViewWaterSourceTypeMaster','/wc/waterSourceType/view','SOURCETYPE',null,(select id from service where code='SOURCETYPE' and contextroot='wcms' and tenantid='default'),5,'View Water Source Type',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'SearchWaterSourceTypeMaster','/sourcetype/_search','SOURCETYPE',null,(select id from service where code='SOURCETYPE' and contextroot='wcms' and tenantid='default'),6,'Search Water Source Type',false,1,now(),1,now()); 

---role action mapping 
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name ='CreateSourceTypeMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Creat SourceType Master'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ModifyWaterSourceTypeMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ModifySourceTypeMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ViewWaterSourceTypeMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchWaterSourceTypeMaster'),'default');



----------------------------------------------------------------------------------------------------------------

---service supplytype masters
INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES
 (nextval('SEQ_SERVICE'),'SUPPLYTYPE','Supply Type Master',true,'wcms','Water Supply Type Master',1,(select id from service where code='WCMS MASTERS' and tenantid='default'),'default');

---action supplytype

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'CreateSupplyTypeMaster','/wc/createSupplyType','SUPPLYTYPE',null,(select id from service where code='SUPPLYTYPE' and contextroot='wcms' and tenantid='default'),1,'Create Water Supply Type',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'Creat SupplyType Master','/supplytype/_create','SUPPLYTYPE',null,(select id from service where code='SUPPLYTYPE' and contextroot='wcms' and tenantid='default'),2,'Create Water Supply Type',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'ModifyWaterSupplyTypeMaster','/wc/supplyType/edit','SUPPLYTYPE',null,(select id from service where code='SUPPLYTYPE' and contextroot='wcms' and tenantid='default'),3,'Modify Water Supply Type',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'ModifySupplyTypeMaster','/supplytype/{code}/_update','SUPPLYTYPE',null,(select id from service where code='SUPPLYTYPE' and contextroot='wcms' and tenantid='default'),4,'Modify Water Supply Type',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'ViewWaterSupplyTypeMaster','/wc/supplyType/view','SUPPLYTYPE',null,(select id from service where code='SUPPLYTYPE' and contextroot='wcms' and tenantid='default'),5,'View Water Supply Type',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'SearchWaterSupplyTypeMaster','/supplytype/_search','SUPPLYTYPE',null,(select id from service where code='SUPPLYTYPE' and contextroot='wcms' and tenantid='default'),6,'Search Water Supply Type',false,1,now(),1,now()); 

---role action mapping 
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name ='CreateSupplyTypeMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Creat SupplyType Master'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ModifyWaterSupplyTypeMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ModifySupplyTypeMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ViewWaterSupplyTypeMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchWaterSupplyTypeMaster'),'default');


---service documenttype masters

INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES
 (nextval('SEQ_SERVICE'),'DOCUMENTTYPE','Document Type Master',true,'wcms','Document Type Master',1,(select id from service where code='WCMS MASTERS' and tenantid='default'),'default');

---action documenttype

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'CreateDocumentTypeMaster','/wc/createDocumentType','DOCUMENTTYPE',null,(select id from service where code='DOCUMENTTYPE' and contextroot='wcms' and tenantid='default'),1,'Create Document Type',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'Creat Document Type Master','/documenttype/_create','DOCUMENTTYPE',null,(select id from service where code='DOCUMENTTYPE' and contextroot='wcms' and tenantid='default'),2,'Create Document Type',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'ModifyDocumentTypeMaster','/wc/documentType/edit','DOCUMENTTYPE',null,(select id from service where code='DOCUMENTTYPE' and contextroot='wcms' and tenantid='default'),3,'Modify Document Type',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'DocumentTypeModify','/documenttype/{code}/_update','DOCUMENTTYPE',null,(select id from service where code='DOCUMENTTYPE' and contextroot='wcms' and tenantid='default'),4,'Modify Document Type',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'ViewDocumentTypeMaster','/wc/documentType/view','DOCUMENTTYPE',null,(select id from service where code='DOCUMENTTYPE' and contextroot='wcms' and tenantid='default'),5,'View Document Type',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'SearchDocumentTypeMaster','/documenttype/_search','DOCUMENTTYPE',null,(select id from service where code='DOCUMENTTYPE' and contextroot='wcms' and tenantid='default'),6,'Search Document Type',false,1,now(),1,now()); 

---role action mapping 
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name ='CreateDocumentTypeMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Creat Document Type Master'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ModifyDocumentTypeMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'DocumentTypeModify'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ViewDocumentTypeMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchDocumentTypeMaster'),'default');