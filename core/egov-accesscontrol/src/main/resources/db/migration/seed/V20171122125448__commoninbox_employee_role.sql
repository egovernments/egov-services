insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='Common Inbox Report MetaData'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='Common Inbox Report'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('EMPLOYEE',(select id from eg_action where name='Common Inbox Report Reload'),'default');
