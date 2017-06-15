INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid)
VALUES (nextval('seq_egeis_designation'), 'Junior Assistant', 'JASST', NULL, NULL, true, 'panavel');

INSERT INTO egeis_departmentdesignation (id, departmentid, designationid, tenantid)
VALUES (nextval('seq_egeis_departmentDesignation'), (select id from eg_department where code = 'ENG' and tenantid = 'panavel'),
(select id from egeis_designation where code = 'JASST' and tenantid = 'panavel'), 'panavel');


INSERT INTO egeis_position (id, name, deptdesigid, ispostoutsourced, active, tenantid)
VALUES (nextval('seq_egeis_position'), 'ENG_Junior Assistant_1',
(select id from egeis_departmentdesignation where departmentid = (select id from eg_department where code = 'ENG' and tenantid = 'panavel') and
 designationid=(select id from egeis_designation where code='JASST' and tenantid = 'panavel') and tenantid = 'panavel'),
false, true, 'panavel');