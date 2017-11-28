INSERT INTO SERVICE (id, code, name, enabled, displayname, ordernumber, tenantid, parentmodule) 
VALUES (nextval('seq_service'), 'PERF_ASSESSMENT_TRGT', 'KPI Targets', true, 'KPI Targets', 1, 'default',
(select id from service where code = 'PERF_ASSESSMENT'));

INSERT INTO eg_action (id, name, url, servicecode, parentmodule, ordernumber, displayname, enabled, createdby, createddate, 
lastmodifiedby, lastmodifieddate) VALUES (nextval('SEQ_EG_ACTION'), 'PerfAssmtKpiTargetCreate', '/perfmanagement/v1/kpitarget/_create', 
'PERF_ASSESSMENT_TRGT',(select id from service where code ='PERF_ASSESSMENT' and tenantid='default'),1, 'Create KPI Targets', true, 1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='PerfAssmtKpiTargetCreate'),'default');

INSERT INTO eg_action (id, name, url, servicecode, parentmodule, ordernumber, displayname, enabled, createdby, createddate, 
lastmodifiedby, lastmodifieddate) VALUES (nextval('SEQ_EG_ACTION'), 'PerfAssmtKpiTargetUpdate', '/perfmanagement/v1/kpitarget/_update', 
'PERF_ASSESSMENT_TRGT',(select id from service where code ='PERF_ASSESSMENT' and tenantid='default'),1, 'Update KPI Targets', true, 1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='PerfAssmtKpiTargetUpdate'),'default');

