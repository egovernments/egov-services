----------------------------------------------------Insert Actions-----------------------------------------------------------------------------------------------------------------------------
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(nextval('SEQ_EG_ACTION'),'Create Complaint','/pgr/seva/_create','PGR','',(select code from service where name='PGR'and contextroot='pgr'),null,'Seva',false,1,now(),1,now(),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(nextval('SEQ_EG_ACTION'),'Update Complaint','/pgr/seva/_update','PGR','',(select code from service where name='PGR'and contextroot='pgr'),null,'Seva',false,1,now(),1,now(),'default');


insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(nextval('SEQ_EG_ACTION'),'Search Complaint','/pgr/seva/_search','PGR','',(select code from service where name='PGR'and contextroot='pgr'),null,'Seva',false,1,now(),1,now(),'default');

-------------------------------------------------End-------------------------------------------------------------------------------------------------------------------------------------------

------------------------------------------------Insert Role Action-----------------------------------------------------------------------------------------------------------------------------
insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Create Complaint'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Create Complaint'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Create Complaint'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Create Complaint'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Create Complaint'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='Create Complaint'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Create Complaint'),'default');


insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Update Complaint'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Update Complaint'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Update Complaint'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Update Complaint'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Update Complaint'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='Update Complaint'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Update Complaint'),'default');


insert into eg_roleaction(roleCode,actionid,tenantId)values('GA',(select id from eg_action where name='Search Complaint'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('GRO',(select id from eg_action where name='Search Complaint'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('RO',(select id from eg_action where name='Search Complaint'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('GO',(select id from eg_action where name='Search Complaint'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Search Complaint'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='Search Complaint'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('CITIZEN',(select id from eg_action where name='Search Complaint'),'default');
-------------------------------------------------------------------End-------------------------------------------------------------------------------------------------------------------------
