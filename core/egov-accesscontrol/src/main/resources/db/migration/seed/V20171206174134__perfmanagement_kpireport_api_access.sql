INSERT INTO SERVICE (id, code, name, enabled, displayname, ordernumber, tenantid, parentmodule) 
VALUES (nextval('seq_service'), 'PERF_ASSESSMENT_DASHBOARD', 'Dashboard', true, 'Dashboard', 1, 'default',
(select id from service where code = 'PERF_ASSESSMENT'));

update eg_action set servicecode = 'PERF_ASSESSMENT_DASHBOARD', displayname = 'KPI Dashboard', enabled = TRUE WHERE name = 'PerfAssmtKpiValueCompareSearch';


