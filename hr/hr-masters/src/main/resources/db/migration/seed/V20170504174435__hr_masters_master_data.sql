INSERT INTO egeis_employeetype (id, name, chartofaccounts, tenantid) VALUES (nextval('seq_egeis_employeeType'), 'Temporary', NULL, 'default');
INSERT INTO egeis_employeetype (id, name, chartofaccounts, tenantid) VALUES (nextval('seq_egeis_employeeType'), 'Deputation', NULL, 'default');
INSERT INTO egeis_employeetype (id, name, chartofaccounts, tenantid) VALUES (nextval('seq_egeis_employeeType'), 'Outsourced', NULL, 'default');

insert into egeis_recruitmentMode(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentMode'),'APPSC','Andhra Public Service','default');
insert into egeis_recruitmentMode(id,name,description,tenantid) values(nextval('seq_egeis_recruitmentMode'),'Compassionate','Compassionate','default');