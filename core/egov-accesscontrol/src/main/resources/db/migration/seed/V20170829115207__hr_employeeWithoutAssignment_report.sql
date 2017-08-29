
--Employee without assignments Report role actions

INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)  
VALUES (nextval('SEQ_EG_ACTION'), 'Employee without assignments', '/hr/report/employeewithoutassignments', 'HRPT',null, 
(select id from service where name='HR Report' and tenantid='default'), 5, 'HR Employee without assignments Report', true, 1, now(), 1, now());


insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE ADMIN',(select id from eg_action where name='Employee without assignments'),'default');

