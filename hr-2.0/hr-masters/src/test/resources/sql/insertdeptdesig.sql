INSERT INTO egeis_designation (id, name, code, description, chartofaccounts, active, tenantid)
VALUES (1, 'Junior Assistant', 'JASST', NULL, NULL, true, 'default');

INSERT INTO egeis_departmentdesignation (id, departmentid, designationid, tenantid)
VALUES (1, 1,
(select id from egeis_designation where code = 'JASST' and tenantid = 'default'), 'default');
