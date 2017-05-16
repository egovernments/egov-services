---service category masters
INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES
 (nextval('SEQ_SERVICE'),'CATEGORYMASTERS','CategoryMasters',true,'wcms','Category Master',1,(select id from service where code='WCMS MASTERS'),'default');

---action category

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(nextval('SEQ_EG_ACTION'),'CreateCategoryMaster','/app/create/create-category-master.html','CATEGORYMASTER',null,(select id from service where code='CATEGORYMASTER' and contextroot='wcms'),1,'Create Category',true,1,now(),1,now(),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(nextval('SEQ_EG_ACTION'),'Creat Category Master','/category/_create','CATEGORYMASTER',null,(select id from service where code='CATEGORYMASTER' and contextroot='wcms'),2,'Create Category',false,1,now(),1,now(),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(nextval('SEQ_EG_ACTION'),'ModifyCategoryMaster','/app/common/show-category-master.html','CATEGORYMASTER',null,(select id from service where code='CATEGORYMASTER' and contextroot='wcms'),3,'Modify Category',true,1,now(),1,now(),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'Category Modify Master','/category/_update/','CATEGORYMASTER',null,(select id from service where code='CATEGORYMASTER' and contextroot='wcms'),4,'CategoryModify',false,1,now(),1,now(),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(nextval('SEQ_EG_ACTION'),'ViewCategoryMaster','/app/common/show-category-master.html','CATEGORYMASTER',null,(select id from service where code='CATEGORYMASTER' and contextroot='wcms'),5,'View Category',true,1,now(),1,now(),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(nextval('SEQ_EG_ACTION'),'SearchCategoryMaster','/category/_search','CATEGORYMASTER',null,(select id from service where code='CATEGORYMASTER' and contextroot='wcms'),6,'Search Category',false,1,now(),1,now(),'default');

---role action mapping
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name ='CreateCategoryMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Creat Category Master'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ModifyCategoryMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Category Modify Master'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ViewCategoryMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchCategoryMaster'),'default');
