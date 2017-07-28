update service set name = 'Employee Management' where code = 'EIS' and tenantid='default';
update service set name = 'Employee Masters' where code = 'EIS Masters' and tenantid = 'default';

------- role action scripts to search categories
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='SearchCategories'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE',(select id from eg_action where name='SearchCategories'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN',(select id from eg_action where name='SearchCategories'),'default');