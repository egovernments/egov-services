INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES (nextval('SEQ_SERVICE'),'Leave Mapping', 'Leave Mapping', true, 'eis', 'Leave Mapping', 13, (select id from service where name='EIS'), 'default');



insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'CreateLeave Mapping','/hr-web/app/hr/leavemaster/leave-mapping.html','EIS','tenantId=',(select id from service where name='Leave Mapping' and contextroot='eis'),1,'Create',true,1,now(),1,now(),'default');
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'CreateLeave Mappings','/hr-leave/leaveallotments/_create','EIS','tenantId=',(select id from service where name='Leave Mapping' and contextroot='eis'),2,'CreateLeave Mappings',false,1,now(),1,now(),'default');
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'UpdateLeave Mapping','/hr-web/app/hr/common/show-leave-mapping.html','EIS','type=update&tenantId=',(select id from service where name='Leave Mapping' and contextroot='eis'),3,'Update',true,1,now(),1,now(),'default');
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'Leave MappingUpdate','/hr-leave/leaveallotments/_update','EIS','tenantId=',(select id from service where name='Leave Mapping' and contextroot='eis'),4,'Leave MappingUpdate',false,1,now(),1,now(),'default');
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'ViewLeave Mapping','/hr-web/app/hr/common/show-leave-mapping.html','EIS','type=view&tenantId=',(select id from service where name='Leave Mapping' and contextroot='eis'),5,'View',true,1,now(),1,now(),'default');
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'SearchLeave Mapping','/hr-leave/leaveallotments/_search','EIS','tenantId=',(select id from service where name='Leave Mapping' and contextroot='eis'),6,'Search Leave Mapping',false,1,now(),1,now(),'default');


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'CreateLeave Mapping'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'CreateLeave Mappings'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'UpdateLeave Mapping'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Leave MappingUpdate'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ViewLeave Mapping'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchLeave Mapping'),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'CreateLeave Mapping'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'CreateLeave Mappings'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'UpdateLeave Mapping'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'Leave MappingUpdate'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'ViewLeave Mapping'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'SearchLeave Mapping'),'default');