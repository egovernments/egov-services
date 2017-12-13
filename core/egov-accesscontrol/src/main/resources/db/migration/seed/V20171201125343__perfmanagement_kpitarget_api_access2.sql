INSERT INTO eg_action (id, name, url, servicecode, parentmodule, ordernumber, displayname, enabled, createdby, createddate, 
lastmodifiedby, lastmodifieddate) VALUES (nextval('SEQ_EG_ACTION'), 'PerfAssmtKpiTargetSearch', '/perfmanagement/v1/kpitarget/_search', 
'PERF_ASSESSMENT_TRGT',(select id from service where code ='PERF_ASSESSMENT' and tenantid='default'),1, 'Search KPI Targets', true, 1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='PerfAssmtKpiTargetSearch'),'default');

