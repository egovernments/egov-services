update service set parentmodule = (select id from service where code ='EIS' and tenantid = 'default')  where code = 'HRPT' and tenantid= 'default';

update eg_action set parentmodule = (select id from service where code ='HRPT' and tenantid = 'default') 
where servicecode='HRPT' and name = 'HR Report MetaData' and url = '/report/hr/metadata/_get';

update eg_action set parentmodule = (select id from service where code ='HRPT' and tenantid = 'default') 
where servicecode='HRPT' and name = 'HR Report' and url = '/report/hr/_get';

--Report urls for HR module
INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)  
VALUES (nextval('SEQ_EG_ACTION'), 'HR Employee History Report', '/hr/report/employeeHistory', 'HRPT',null,   (select id from service where name='HR Report' and tenantid='default'), 1,   'HR Employee History Report', true, 1, now(), 1, now());

INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)  
VALUES (nextval('SEQ_EG_ACTION'), 'HR Attendance Report', '/hr/report/attendance', 'HRPT',null,   (select id from service where name='HR Report' and tenantid='default'), 1,   'HR Attendance Report', true, 1, now(), 1, now());

INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)  
VALUES (nextval('SEQ_EG_ACTION'), 'HR Employees Leave Report', '/hr/report/employeeLeaves', 'HRPT',null,   (select id from service where name='HR Report' and tenantid='default'), 1,   'HR Employees Leave Report', true, 1, now(), 1, now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE ADMIN',(select id from eg_action where name='HR Employee History Report'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE ADMIN',(select id from eg_action where name='HR Attendance Report'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE ADMIN',(select id from eg_action where name='HR Employees Leave Report'),'default');





