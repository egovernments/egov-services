insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,
lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'PerfAssmtKpiValueCreate','/perfmanagement/v1/kpivalue/_create',
'PERF_ASSESSMENT',NULL, (select id from service where code ='PERF_ASSESSMENT' and tenantid='default'),1,
'Create KPI Value',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='PerfAssmtKpiValueCreate'),'default');


insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,
lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'PerfAssmtKpiValueUpdate','/perfmanagement/v1/kpivalue/_update',
'PERF_ASSESSMENT',NULL, (select id from service where code ='PERF_ASSESSMENT' and tenantid='default'),1,
'Update KPI Value',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='PerfAssmtKpiValueUpdate'),'default');



insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,
lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'PerfAssmtKpiValueSearch','/perfmanagement/v1/kpivalue/_search',
'PERF_ASSESSMENT',NULL, (select id from service where code ='PERF_ASSESSMENT' and tenantid='default'),1,
'Search KPI Value',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='PerfAssmtKpiValueSearch'),'default');

