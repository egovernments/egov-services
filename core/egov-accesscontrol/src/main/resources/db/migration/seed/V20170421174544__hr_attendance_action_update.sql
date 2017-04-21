update eg_action set url='/app/hr/common/employee-attendance.html' where name='SearchAttendancePage';
update eg_action set url='/app/hr/attendance/attendance.html' where name='AttendanceResults';
update eg_action set url='/attendances/_search',parentmodule=(select id from service where name='HR Attendance') where name='AjaxSearchAttendances';
update eg_action set url='/attendances/_create',parentmodule=(select id from service where name='HR Attendance') where name='AjaxCreateAttendances';
update eg_action set url='/attendances/_update',parentmodule=(select id from service where name='HR Attendance') where name='AjaxUpdateAttendances';