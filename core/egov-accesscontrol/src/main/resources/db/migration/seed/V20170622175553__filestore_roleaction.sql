insert into eg_roleaction  (rolecode,actionid,tenantid)
values('EMPLOYEE ADMIN',(Select id from eg_action where name='uploadfiles'),'default');

insert into eg_roleaction  (rolecode,actionid,tenantid)
values('EMPLOYEE ADMIN',(Select id from eg_action where name='filesearch'),'default');

insert into eg_roleaction  (rolecode,actionid,tenantid)
values('EMPLOYEE ADMIN',(Select id from eg_action where name='filesearchbytag'),'default');