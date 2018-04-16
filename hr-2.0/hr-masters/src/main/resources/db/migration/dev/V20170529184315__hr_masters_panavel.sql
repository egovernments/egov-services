INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Senior Assistant', 'SASST', NULL, NULL, true, 'panavel');
INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid) VALUES (nextval('seq_egeis_designation'), 'Accounts Officer', 'AO', NULL, NULL, true, 'panavel');

insert into egeis_hrstatus(id,objectname,code,description,tenantid) values (nextval('seq_egeis_hrStatus'),'Employee Master',
'EMPLOYED','Currently employee of the system','panavel');

INSERT INTO egeis_departmentdesignation (id, departmentid, designationid, tenantid) 
VALUES (nextval('seq_egeis_departmentDesignation'), (select id from eg_department where code = 'ADM' and tenantid = 'panavel'), 
(select id from egeis_designation where code = 'SASST' and tenantid = 'panavel'), 'panavel');
INSERT INTO egeis_departmentdesignation (id, departmentid, designationid, tenantid) 
VALUES (nextval('seq_egeis_departmentDesignation'), (select id from eg_department where code = 'ENG' and tenantid = 'panavel'),
(select id from egeis_designation where code = 'AO' and tenantid = 'panavel'), 'panavel');


INSERT INTO egeis_position (id, name, deptdesigid, ispostoutsourced, active, tenantid) 
VALUES (nextval('seq_egeis_position'), 'ENG_Assistant Engineer_2', 
(select id from egeis_departmentdesignation where departmentid = (select id from eg_department where code = 'ADM' and tenantid = 'panavel') and tenantid = 'panavel'), false, true, 'panavel');
INSERT INTO egeis_position (id, name, deptdesigid, ispostoutsourced, active, tenantid) 
VALUES (nextval('seq_egeis_position'), 'Acc_Senior Account_2', 
(select id from egeis_departmentdesignation where departmentid = (select id from eg_department where code = 'ENG' and tenantid = 'panavel') and tenantid = 'panavel'), false, true, 'panavel');

INSERT INTO egeis_employeetype (id, name, chartofaccounts, tenantid) VALUES (nextval('seq_egeis_employeeType'), 'Permanent', NULL, 'panavel');


