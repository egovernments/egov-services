insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) values (nextval('SEQ_SERVICE'),'EIS','EIS',true,'eis' ,'Employee',NULL ,NULL ,'default');

---Creating new module
insert into service (ID,CODE,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER,TENANTID) VALUES (NEXTVAL('SEQ_SERVICE'),'ATTENDANCE','Attendance','true','eis',(select id from service where name = 'EIS'),'Attendance', NULL, 'default');

---Attendance action entries
insert into eg_action (ID,NAME,URL,SERVICECODE,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,TENANTID) values (NEXTVAL('SEQ_EG_ACTION'),'SearchAttendancePage','/hr-web/app/hr/common/employee-attendance.html','EIS',null,(select id from service where name='Attendance' and contextroot='eis'),0,'Create/Update',true,1,now(),1,now(),'default');
insert into eg_action (ID,NAME,URL,SERVICECODE,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,TENANTID) values (NEXTVAL('SEQ_EG_ACTION'),'AttendanceResults','/hr-web/app/hr/attendance/attendance.html','EIS',null,(select id from service where name='Attendance' and contextroot='eis'),1,'Search Attendances',false,1,now(),1,now(),'default');
insert into eg_action (ID,NAME,URL,SERVICECODE,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,TENANTID) values (NEXTVAL('SEQ_EG_ACTION'),'AjaxSearchAttendances','/hr-attendance/attendances/_search','EIS',null,(select id from service where name='Attendance' and contextroot='eis'),0,'Ajax Search Attendances','false',1,now(),1,now(),'default');
insert into eg_action (ID,NAME,URL,SERVICECODE,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,TENANTID) values (NEXTVAL('SEQ_EG_ACTION'),'AjaxCreateAttendances','/hr-attendance/attendances/_create','EIS',null,(select id from service where name='Attendance' and contextroot='eis'),0,'Ajax Create Attendances','false',1,now(),1,now(),'default');
insert into eg_action (ID,NAME,URL,SERVICECODE,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,TENANTID) values (NEXTVAL('SEQ_EG_ACTION'),'AjaxUpdateAttendances','/hr-attendance/attendances/_update','EIS',null,(select id from service where name='Attendance' and contextroot='eis'),0,'Ajax Update Attendances','false',1,now(),1,now(),'default');

---adding role action mappings
insert into eg_roleaction(roleCode, actionid, tenantId) values ('EMPLOYEE ADMIN', (select id from eg_action where name = 'SearchAttendancePage'), 'default');
insert into eg_roleaction(roleCode, actionid, tenantId) values ('EMPLOYEE ADMIN', (select id from eg_action where name = 'AttendanceResults'), 'default');
insert into eg_roleaction(roleCode, actionid, tenantId) values ('EMPLOYEE ADMIN', (select id from eg_action where name = 'AjaxSearchAttendances'), 'default');
insert into eg_roleaction(roleCode, actionid, tenantId) values ('EMPLOYEE ADMIN', (select id from eg_action where name = 'AjaxCreateAttendances'), 'default');
insert into eg_roleaction(roleCode, actionid, tenantId) values ('EMPLOYEE ADMIN', (select id from eg_action where name = 'AjaxUpdateAttendances'), 'default');
insert into eg_roleaction(roleCode, actionid, tenantId) values ('SUPERUSER', (select id from eg_action where name = 'SearchAttendancePage'), 'default');
insert into eg_roleaction(roleCode, actionid, tenantId) values ('SUPERUSER', (select id from eg_action where name = 'AttendanceResults'), 'default');
insert into eg_roleaction(roleCode, actionid, tenantId) values ('SUPERUSER', (select id from eg_action where name = 'AjaxSearchAttendances'), 'default');
insert into eg_roleaction(roleCode, actionid, tenantId) values ('SUPERUSER', (select id from eg_action where name = 'AjaxCreateAttendances'), 'default');
insert into eg_roleaction(roleCode, actionid, tenantId) values ('SUPERUSER', (select id from eg_action where name = 'AjaxUpdateAttendances'), 'default');

--rollback scripts
--rollback delete from eg_roleaction where rolecode in ('EMPLOYEE ADMIN', 'SUPERUSER') and actionid in (select id from eg_action where name in ('AjaxSearchAttendances','AjaxCreateAttendances','AjaxUpdateAttendances', 'SearchAttendancePage', 'AttendanceResults'));
--rollback delete from eg_action where name in ('AjaxUpdateAttendances', 'AjaxCreateAttendances', 'AjaxSearchAttendances', 'SearchAttendancePage', 'AttendanceResults');
--rollback delete from service where name = 'Attendance';