INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Senior Assistant', 'SASST', NULL, NULL, true, 'default');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Accounts Officer', 'AO', NULL, NULL, true, 'default');

INSERT INTO egeis_departmentdesignation (id, departmentid, designationid, tenantid) 
VALUES (nextval('seq_egeis_departmentDesignation'), (select id from eg_department where code = 'ADM' and tenantid = 'default'), 
(select id from egeis_designation where code = 'SASST' and tenantid = 'default'), 'default');
INSERT INTO egeis_departmentdesignation (id, departmentid, designationid, tenantid) 
VALUES (nextval('seq_egeis_departmentDesignation'), (select id from eg_department where code = 'ACC' and tenantid = 'default'), 
(select id from egeis_designation where code = 'AO' and tenantid = 'default'), 'default');


INSERT INTO egeis_position (id, name, deptdesigid, ispostoutsourced, active, tenantid) 
VALUES (nextval('seq_egeis_position'), 'ENG_Assistant Engineer_1', 
(select id from egeis_departmentdesignation where departmentid = (select id from eg_department where code = 'ADM' and tenantid = 'default') and tenantid = 'default'), false, true, 'default');
INSERT INTO egeis_position (id, name, deptdesigid, ispostoutsourced, active, tenantid) 
VALUES (nextval('seq_egeis_position'), 'Acc_Senior Account_1', 
(select id from egeis_departmentdesignation where departmentid = (select id from eg_department where code = 'ACC' and tenantid = 'default') and tenantid = 'default'), false, true, 'default');

INSERT INTO egeis_employeetype (id, name, chartofaccounts, tenantid) VALUES (nextval('seq_egeis_employeeType'), 'Permanent', NULL, 'default');

