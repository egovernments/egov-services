update service set parentmodule=(select id from service where name='Leave Management') where name='Leave Type';
update eg_action set url='/app/hr/leavemaster/leave-type.html',queryparams=null where name='Create Leave Type';
update eg_action set url='/leavetypes/_create',queryparams=null,parentmodule=(select id from service where name='HR Leave') where name='Create LeaveType';
update eg_action set url='/app/hr/common/show-leave-type.html',queryparams='type=update' where name='Update Leave Type';
update eg_action set url='/leavetypes/{id}/_update',queryparams=null,parentmodule=(select id from service where name='HR Leave') where name='Leave Type Update';
update eg_action set url='/app/hr/common/show-leave-type.html',queryparams='type=view' where name='View Leave Type';
update eg_action set url='/leavetypes/_search',queryparams=null,parentmodule=(select id from service where name='HR Leave') where name='Search Leave Type';
