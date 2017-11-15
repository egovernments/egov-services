insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,
lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'PerfAssmtGetKpiType','/perfmanagement/v1/kpimaster/_getkpitype',
'PERF_ASSESSMENT',NULL, (select id from service where code ='PERF_ASSESSMENT' and tenantid='default'),1,
'Get KPI Type',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='PerfAssmtGetKpiType'),'default');

