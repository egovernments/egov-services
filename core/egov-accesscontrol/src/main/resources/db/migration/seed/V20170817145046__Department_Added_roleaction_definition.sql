insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId)
	values (nextval('SEQ_SERVICE'),'ADMIN','Administration',true, 'admin' ,'Administration', 1 ,null ,'default');
insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId)
	values (nextval('SEQ_SERVICE'),'DEPT','Department',true, 'dept' ,'Depratment', 1 ,(select id from service where name ='Masters' and code='ADMIN' and tenantId='default') ,'default');


insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Create Department','/egov-common-masters/departments/v1/_create','DEPT',NULL, (select id from service where code ='DEPT'  and tenantid='default'),1,'Create Department',true,1,now(),1,now());


insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Modify Department','/egov-common-masters/departments/v1/_search','DEPT',NULL, (select id from service where code ='DEPT'  and tenantid='default'),2,'Modify Department',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'View Department','/egov-common-masters/departments/v1/_search','DEPT',NULL, (select id from service where code ='DEPT' and tenantid='default'),3,'View Department',true,1,now(),1,now());



insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Update Department','/egov-common-masters/departments/v1/_update','DEPT',NULL, (select id from service where code ='DEPT'  and tenantid='default'),2,'Update Department',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Create Department'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Modify Department'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='View Department'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Update Department'),'default');



