delete from egeis_employeetype where tenantid='pb.mohali' and name in('Temporary','Deputation','Outsourced','Permanent');

INSERT INTO egeis_employeetype (id, name, chartofaccounts, tenantid) VALUES (nextval('seq_egeis_employeeType'), 'Temporary', NULL, 'pb.mohali');
INSERT INTO egeis_employeetype (id, name, chartofaccounts, tenantid) VALUES (nextval('seq_egeis_employeeType'), 'Deputation', NULL, 'pb.mohali');
INSERT INTO egeis_employeetype (id, name, chartofaccounts, tenantid) VALUES (nextval('seq_egeis_employeeType'), 'Outsourced', NULL, 'pb.mohali');
INSERT INTO egeis_employeetype (id, name, chartofaccounts, tenantid) VALUES (nextval('seq_egeis_employeeType'), 'Permanent', NULL, 'pb.mohali');
