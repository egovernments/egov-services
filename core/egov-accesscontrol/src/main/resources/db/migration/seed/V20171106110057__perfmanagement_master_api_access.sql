INSERT INTO SERVICE (id, code, name, enabled, displayname, ordernumber, tenantid) 
VALUES (nextval('seq_service'), 'PERF_ASSESSMENT', 'Performance Assessment', true, 'Performance Assessment', 1, 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,
lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'PerfAssmtKpiMasterCreate','/perfmanagement/v1/kpimaster/_create',
'PERF_ASSESSMENT',NULL, (select id from service where code ='PERF_ASSESSMENT' and tenantid='default'),1,
'Create KPI Master',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='PerfAssmtKpiMasterCreate'),'default');


insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,
lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'PerfAssmtKpiMasterUpdate','/perfmanagement/v1/kpimaster/_update',
'PERF_ASSESSMENT',NULL, (select id from service where code ='PERF_ASSESSMENT' and tenantid='default'),1,
'Update KPI Master',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='PerfAssmtKpiMasterUpdate'),'default');



insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,
lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'PerfAssmtKpiMasterDelete','/perfmanagement/v1/kpimaster/_delete',
'PERF_ASSESSMENT',NULL, (select id from service where code ='PERF_ASSESSMENT' and tenantid='default'),1,
'Delete KPI Master',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='PerfAssmtKpiMasterDelete'),'default');



insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,
lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'PerfAssmtKpiMasterSearch','/perfmanagement/v1/kpimaster/_search',
'PERF_ASSESSMENT',NULL, (select id from service where code ='PERF_ASSESSMENT' and tenantid='default'),1,
'Search KPI Master',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='PerfAssmtKpiMasterSearch'),'default');

