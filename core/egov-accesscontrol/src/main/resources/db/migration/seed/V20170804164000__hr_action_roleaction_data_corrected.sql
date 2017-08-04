update eg_action set enabled = 'false' where name = 'SearchBloodGroups';
update eg_action set enabled = 'false' where name = 'SearchMaritalStatuses';
update eg_action set enabled = 'false' where name = 'SearchCategories';
update eg_action set url = '/hr-leave/leavetypes/{leaveTypeId}/_update' where name = 'Leave Type Update';
update eg_action set url = '/hr-leave/leaveapplications/{leaveApplicationId}/_update' where name = 'Leave Application Update';
update eg_action set url = '/hr-employee-movement/movements/{movementId}/_update' where name = 'Employee Movement Update';
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'PositionUpdateById','/hr-masters/positions/{id}/_update','Position','',(select id from service where name='Position' and contextroot='EIS Masters' and tenantid = 'default'),2,'Update Position by id',false,1,now(),1,now());
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'LeaveMappingUpdateById','/hr-leave/leaveallotments/{leavetypeId}/_update','Leave Mapping','',(select id from service where name='HR Leave' and contextroot='hr-leave' and tenantid = 'default'),2,'LeaveMappingUpdateById',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'PositionUpdateById'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'PositionUpdateById'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'LeaveMappingUpdateById'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'LeaveMappingUpdateById'),'default');