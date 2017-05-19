---service pipesize masters
INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES
 (nextval('SEQ_SERVICE'),'PIPESIZEMASTER','PipeSize Master',true,'wcms','Pipe Size Master',1,(select id from service where code='WCMS MASTERS'),'default');

---action pipesize

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(nextval('SEQ_EG_ACTION'),'CreatePipeSizeMaster','/app/create/create-pipe-size.html','PIPESIZEMASTER',null,(select id from service where code='PIPESIZEMASTER' and contextroot='wcms'),1,'Create Pipe Size',true,1,now(),1,now(),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(nextval('SEQ_EG_ACTION'),'Creat PipeSize Master','/pipesize/_create','PIPESIZEMASTER',null,(select id from service where code='PIPESIZEMASTER' and contextroot='wcms'),2,'Create Pipe Size',false,1,now(),1,now(),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(nextval('SEQ_EG_ACTION'),'ModifyPipeSizeMaster','/app/common/show-pipe-size.html','PIPESIZEMASTER',null,(select id from service where code='PIPESIZEMASTER' and contextroot='wcms'),3,'Modify Pipe Size',true,1,now(),1,now(),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(nextval('SEQ_EG_ACTION'),'PipeSize Modify Master','/pipesize/_update/','PIPESIZEMASTER',null,(select id from service where code='PIPESIZEMASTER' and contextroot='wcms'),4,'PipeSizeModify',false,1,now(),1,now(),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(nextval('SEQ_EG_ACTION'),'ViewPipeSizeMaster','/app/common/show-pipe-size.html','PIPESIZEMASTER',null,(select id from service where code='PIPESIZEMASTER' and contextroot='wcms'),5,'View Pipe Size',true,1,now(),1,now(),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(nextval('SEQ_EG_ACTION'),'SearchPipeSizeMaster','/pipesize/_search','PIPESIZEMASTER',null,(select id from service where code='PIPESIZEMASTER' and contextroot='wcms'),6,'Search Pipe Size',false,1,now(),1,now(),'default');

---role action mapping
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name ='CreatePipeSizeMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Creat PipeSize Master'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ModifyPipeSizeMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'PipeSize Modify Master'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ViewPipeSizeMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchPipeSizeMaster'),'default');