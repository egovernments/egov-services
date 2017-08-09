

insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId)
values (nextval('SEQ_SERVICE'),'STCONFIG','STConfiguration',true, 'pgr' ,'ServiceType Configuration', 8 ,(select id from service where name ='Masters' and code='MSTR' and tenantId='default') ,'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Create ST Config','/pgr-master/servicetypeconfiguration/v1/_create','STCONFIG',NULL, (select id from service where name ='STConfiguration' and contextroot='pgr' and tenantid='default'),1,'Create ServiceType Configuration',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Update ST Config','/pgr-master/servicetypeconfiguration/v1/_update','STCONFIG',NULL, (select id from service where name ='STConfiguration' and contextroot='pgr' and tenantid='default'),3,'Update ServiceType Configuration',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Search ST Config','/pgr-master/servicetypeconfiguration/v1/_search','STCONFIG',NULL, (select id from service where name ='STConfiguration' and contextroot='pgr' and tenantid='default'),2,'Search ServiceType Configuration',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SRC',(select id from eg_action where name='Create ST Config'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SRC',(select id from eg_action where name='Search ST Config'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SRC',(select id from eg_action where name='Update ST Config'),'default');
