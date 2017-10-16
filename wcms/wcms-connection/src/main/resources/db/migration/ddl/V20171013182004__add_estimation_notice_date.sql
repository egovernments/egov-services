alter table egwtr_estimationnotice_audit_log drop column dateofletter;
alter table egwtr_estimationnotice_audit_log drop column createdtime;
alter table egwtr_estimationnotice_audit_log drop column applicationdate ;
alter table egwtr_estimationnotice_audit_log add column dateofletter bigint;
alter table egwtr_estimationnotice_audit_log add column createdtime bigint;
alter table egwtr_estimationnotice_audit_log add column applicationdate bigint ;

alter table egwtr_workorder_audit_log drop column workorderdate;
alter table egwtr_workorder_audit_log add column workorderdate bigint;