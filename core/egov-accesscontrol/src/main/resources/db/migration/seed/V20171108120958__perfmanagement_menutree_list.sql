INSERT INTO SERVICE (id, code, name, enabled, displayname, ordernumber, tenantid, parentmodule) 
VALUES (nextval('seq_service'), 'PERF_ASSESSMENT_MSTR', 'Performance Assessment Masters', true, 'KPI Masters', 1, 'default',
(select id from service where code = 'PERF_ASSESSMENT'));

INSERT INTO SERVICE (id, code, name, enabled, displayname, ordernumber, tenantid, parentmodule) 
VALUES (nextval('seq_service'), 'PERF_ASSESSMENT_VAL', 'Performance Assessment Actuals', true, 'KPI Actuals', 1, 'default',
(select id from service where code = 'PERF_ASSESSMENT'));

UPDATE eg_action SET servicecode = 'PERF_ASSESSMENT_MSTR' where name = 'PerfAssmtKpiMasterCreate';
UPDATE eg_action SET servicecode = 'PERF_ASSESSMENT_MSTR' where name = 'PerfAssmtKpiMasterUpdate';
UPDATE eg_action SET servicecode = 'PERF_ASSESSMENT_MSTR' where name = 'PerfAssmtKpiMasterSearch';
UPDATE eg_action SET servicecode = 'PERF_ASSESSMENT_MSTR' where name = 'PerfAssmtKpiMasterDelete';

UPDATE eg_action SET servicecode = 'PERF_ASSESSMENT_VAL' where name = 'PerfAssmtKpiValueCreate';
UPDATE eg_action SET servicecode = 'PERF_ASSESSMENT_VAL' where name = 'PerfAssmtKpiValueUpdate';
UPDATE eg_action SET servicecode = 'PERF_ASSESSMENT_VAL' where name = 'PerfAssmtKpiValueSearch';

